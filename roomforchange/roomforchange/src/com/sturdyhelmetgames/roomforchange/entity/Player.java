/*    Copyright 2013 Antti Kolehmainen

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.sturdyhelmetgames.roomforchange.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sturdyhelmetgames.roomforchange.assets.Assets;
import com.sturdyhelmetgames.roomforchange.level.Level;
import com.sturdyhelmetgames.roomforchange.level.Level.LevelTile;
/**
 * This class represents the base player in the game
 * @author root
 *
 */
public class Player extends Entity {

	//Player attributes
	public float dyingAnimState = 0f;
	public float dyingTime = 0f;
	public float maxDyingTime = 3f;
	public int health = 5;
	public int bombs = 1;
	public int maxHealth = 5;
	public final Rectangle hitBounds = new Rectangle(0f, 0f, 0.8f, 0.8f);
	private float tryHitTime = 0.3f;
	//Itens attributes
	public UsableItem usableItem = null;
	public boolean gotScroll = false;
	public boolean gotTalisman = false;
	public boolean gotGem = false;
	
	/**
	*Player constructor
	* @param x height location
	* @param y width location
	* @param The level tha the player will spawn
	*/
	public Player(float x, float y, Level level) {
		super(x, y, 1f, 0.6f, level);
		direction = Direction.UP;
		this.maxHealth = 5;
	}
	
	public float getTryHitTime() {
		return tryHitTime;
	}

	public void setTryHitTime(float tryHitTime) {
		this.tryHitTime = tryHitTime;
	}

	/**
	* Renders the player in the screen
	* @param delta the size
	* @param batch the player image
	*/
	@Override
	public void render(float delta, SpriteBatch batch) {
		super.render(delta, batch);

		if (blinkTick < BLINK_TICK_MAX) {
			drawPlayer(batch);
		}

		drawAttack(batch);

	}
	/**
	* If the player have an iten, it will use it
	*/
	public void useItem(){
		if (usableItem != null){
			this.usableItem.useItem(this, level);
			this.usableItem = null;
		}
	}
	/**
	* gives the player an item
	* @param item the item that the player will receive
	*/	
	public void pickupItem(UsableItem item) {
		this.usableItem = item;
	}
	
	/**
	* Draws the player attack in the screen
	* @param batch the image of the player attacking
	*/
	public void drawAttack(SpriteBatch batch) {
		if (tryHitTime < 0.3) {
			float rotation = 0f;
			float x = this.bounds.x;
			float y = this.bounds.y;
			if (direction == Direction.UP) {
				rotation = 270f;
				y += .8f;
			} else if (direction == Direction.DOWN) {
				rotation = 90f;
				y -= .8f;
			} else if (direction == Direction.RIGHT) {
				rotation = 180f;
				x += .8f;
			} else if (direction == Direction.LEFT) {
				x -= .8f;
			}
			batch.draw(Assets.hitTarget.getKeyFrame(tryHitTime, true), x, y,
					1f / 2, 1f / 2, 1f, 1f, 1f, 1f, rotation);
		}
	}

	/**
	* Selects the correct animation for the player from a SpriteBaTch
	*/
	public void drawPlayer(SpriteBatch batch){
		Animation animation = null;
		
		if (isFalling()) {
			animation = Assets.playerFalling;
			batch.draw(animation.getKeyFrame(dyingAnimState),
					bounds.x - 0.1f, bounds.y, width, height + 0.4f);
		} else if (isDying() || isDead()) {
			animation = Assets.playerDying;
			batch.draw(animation.getKeyFrame(dyingAnimState),
					bounds.x - 0.1f, bounds.y, width, height + 0.4f);
		} else {
			if (direction == Direction.UP) {
				animation = Assets.playerWalkBack;
			} else if (direction == Direction.DOWN) {
				animation = Assets.playerWalkFront;
			} else if (direction == Direction.RIGHT) {
				animation = Assets.playerWalkRight;
			} else if (direction == Direction.LEFT) {
				animation = Assets.playerWalkLeft;
			}
			if (isNotWalking()) {
				batch.draw(animation.getKeyFrame(0.25f), bounds.x - 0.1f,
						bounds.y, width, height + 0.4f);
			} else {
				batch.draw(animation.getKeyFrame(stateTime, true),
						bounds.x - 0.1f, bounds.y, width, height + 0.4f);
			}
		}
	}

	/**
	* Updates the player attributes
	*/
	@Override
	public void update(float fixedStep) {

		if (tryHitTime < 0.3) {
			tryHitTime += fixedStep;
		}

		if (isDying() || isFalling()) {
			dyingAnimState += fixedStep;
			dyingTime += fixedStep;
			if (dyingTime >= maxDyingTime) {
				state = EntityState.DEAD;
				level.gameScreen.gameOver();
			}
		}

		super.update(fixedStep);

	}

	private static final float HIT_DISTANCE = 0.5f;
	/**
	 * tries to hit something in the level
	 */
	public void tryHit() {
		if (!isDying() && !isDead() && !isFalling()) {
			tryHitTime = 0f;
			hitBounds.x = bounds.x;
			hitBounds.y = bounds.y;
			if (direction == Direction.LEFT) {
				hitBounds.x -= HIT_DISTANCE;
			} else if (direction == Direction.RIGHT) {
				hitBounds.x += HIT_DISTANCE;
			} else if (direction == Direction.UP) {
				hitBounds.y += HIT_DISTANCE;
			} else if (direction == Direction.DOWN) {
				hitBounds.y -= HIT_DISTANCE;
			}

			for (int i = 0; i < level.entities.size; i++) {
				final Entity entity = level.entities.get(i);
				entity.hit(hitBounds);
			}

			// double the hit distance for tiles
			if (direction == Direction.LEFT) {
				hitBounds.x -= HIT_DISTANCE;
			} else if (direction == Direction.RIGHT) {
				hitBounds.x += HIT_DISTANCE;
			} else if (direction == Direction.UP) {
				hitBounds.y += HIT_DISTANCE;
			} else if (direction == Direction.DOWN) {
				hitBounds.y -= HIT_DISTANCE;
			}

			tryHitLever((int) hitBounds.x, (int) hitBounds.y);
			tryHitLever((int) hitBounds.x + 1, (int) hitBounds.y);
			tryHitLever((int) hitBounds.x, (int) hitBounds.y + 1);
		}
	}
	/**
	 * Tries to hi the lever
	 * @param x height
	 * @param y width
	 */
	protected void tryHitLever(int x, int y) {
		LevelTile tile = level.getTiles()[x][y];
		if (tile.type == Level.LevelTileType.LEVER) {
			level.gameScreen.openLeverScreen();
		}
	}
	/**
	 * Decreases the life of the player
	 * checks if his life is above 0
	 */
	public void takeDamage() {
		if (!isInvulnerable()) {
			health--;
			invulnerableTick = INVULNERABLE_TIME_MIN;
		}
		if (health <= 0 && !isDead() && !isDying()) {
			state = EntityState.DYING;
			Assets.getGameSound(Assets.SOUND_DEATH).play(0.5f);
		}
	}

	/**
	* Verifies if the player is falling
	* if it is true, plays a sound
	*/
	@Override
	protected void fall() {
		if (!isFalling() && !isDead()) {
			Assets.getGameSound(Assets.SOUND_DEATH).play(0.5f);
			super.fall();
		}
	}
	/**
	 * Increases the player life in 1
	 */
	public void gainHealth() {
		if (health < maxHealth && health > 0)
			health++;
	}
	/**
	 * Checks if the player is invulnerable
	 * @return
	 */
	public boolean isInvulnerable() {
		return invulnerableTick > 0f;
	}
	/**
	 * checks if the player has collected all the treasures in the game
	 * @return true case the player has collected all treasures in the game
	 */
	public boolean canFinishGame() {
		return gotGem && gotScroll && gotTalisman;
	}
	/**
	 * Checks if the player has bombs, if so, it drops a bomb in the player current tile
	 */
	public void dropBomb() {
		if (level.player.bombs > 0) {
			level.player.bombs--;
			level.entities.add(new ExplodingBomb(bounds.x, bounds.y, level));
		}
	}
}
