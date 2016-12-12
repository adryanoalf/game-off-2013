package com.sturdyhelmetgames.roomforchange.entity;

import junit.framework.Assert;

import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sturdyhelmetgames.roomforchange.RoomForChangeGame;
import com.sturdyhelmetgames.roomforchange.entity.Entity.Direction;
import com.sturdyhelmetgames.roomforchange.entity.Entity.EntityState;
import com.sturdyhelmetgames.roomforchange.level.Level;
import com.sturdyhelmetgames.roomforchange.screen.GameScreen;

@SuppressWarnings("deprecation")
public class PlayerTest {
	GameScreen gameScreen = new GameScreen(new RoomForChangeGame());
	Level level = new Level(gameScreen);
	SpriteBatch spriteBatch = new SpriteBatch();
	
	@Test
	public void getTryHitTimeTest() {
		Player player = new Player(0, 0, level);
		player.setTryHitTime(0.5f);
		Assert.assertTrue(player.getTryHitTime() == 0.5f);
	}
	
	@Test
	public void pickUpItemTest() {
		Player player = new Player(0, 0, level);
		Ray ray = new Ray(1, 1, level);
		player.pickupItem(ray);
		Assert.assertTrue(player.usableItem == ray);
	}
	
	@Test
	public void useItemTest() {
		Player player = new Player(0, 0, level);
		player.useItem();
		Assert.assertTrue(player.usableItem == null);
	}
	
	@Test
	public void takeDamageTest() {
		Player player = new Player(0, 0, level);
		player.takeDamage();
		Assert.assertTrue(player.health == 4);
	}
	
	@Test
	public void isIvulnerableeTest() {
		Player player = new Player(0, 0, level);
		Assert.assertTrue(player.isInvulnerable());
	}
	
	@Test
	public void canFinishGameTest() {
		Player player = new Player(0, 0, level);
		player.gotGem = true;
		player.gotScroll = true;
		player.gotTalisman = true;
		Assert.assertTrue(player.canFinishGame());
	}
	
	@Test
	public void shieldUseTest() {
		Player player = new Player(0, 0, level);
		player.pickupItem(new Shield(1, 1, level));
		Assert.assertTrue(player.isInvulnerable());
	}
	
	@Test
	public void deathRayTest() {
		Player player = new Player(0, 0, level);
		Spider spider = new Spider(1, 1, level);
		player.pickupItem(new Ray(1, 1, level));
		player.useItem();
		Assert.assertTrue(spider.isAlive());
	}
	
	@Test
	public void auraTest() {
		Player player = new Player(0, 0, level);
		Spider spider = new Spider(1, 1, level);
		int hp = spider.health;
		player.pickupItem(new Aura(1, 1, level));
		player.useItem();
		Assert.assertTrue(spider.health < hp);
	}
	
	@Test
	public void drawAttackDownTest() {
	 	Player player = new Player(0, 0, level);
	 	player.direction = Direction.DOWN;
	 	player.setTryHitTime(0.2f);
	 	player.bounds.x = 0;
	 	player.bounds.y = 0;
 		player.drawAttack(spriteBatch);
	 			
	 	Assert.assertTrue(player.bounds.x == 90f);
	 }
	 
	 @Test
	 public void drawAttackRightTest() {
	 	Player player = new Player(0, 0, level);
	 	player.direction = Direction.RIGHT;
	 	player.setTryHitTime(0.2f);
	 	player.bounds.x = 0;
	 	player.bounds.y = 0;
	 	player.drawAttack(spriteBatch);
	 			
	 	Assert.assertTrue(player.bounds.x == 180f);
	 }
	 
	 @Test
	 public void drawAttackLeftTest() {
	 	Player player = new Player(0, 0, level);
	 	player.direction = Direction.LEFT;
	 	player.setTryHitTime(0.2f);
	 	player.bounds.x = 0;
	 	player.bounds.y = 0;
	 	player.drawAttack(spriteBatch);
	 
	 	Assert.assertTrue(player.bounds.x == .8f);
	 }
	 
	 @Test
	 public void drawPlayerFalling() {
	 	Player player = new Player(0, 0, level);
	 	player.state = EntityState.FALLING;
	 	player.drawPlayer(spriteBatch);
	 	
	 	Assert.assertTrue(player.state == EntityState.FALLING);
	 }
	 
	 @Test
	 public void drawPlayerDying() {
	 	Player player = new Player(0, 0, level);
	 	player.state = EntityState.DYING;
	 	player.drawPlayer(spriteBatch);
	 	
	 	Assert.assertTrue(player.state == EntityState.DYING);
	 }
	   
	 @Test
	 public void drawPlayerWalking() {
	 	Player player = new Player(0, 0, level);
	 	player.state = EntityState.WALKING;
	 	player.drawPlayer(spriteBatch);
	 
	 	Assert.assertTrue(player.state == EntityState.WALKING);
	 }
}
