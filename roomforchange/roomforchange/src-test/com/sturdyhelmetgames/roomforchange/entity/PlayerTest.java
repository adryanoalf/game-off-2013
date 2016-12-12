package com.sturdyhelmetgames.roomforchange.entity;

import junit.framework.Assert;

import org.junit.Test;
import com.sturdyhelmetgames.roomforchange.level.Level;
import com.sturdyhelmetgames.roomforchange.screen.GameScreen;

@SuppressWarnings("deprecation")
public class PlayerTest {
	GameScreen gameScreen = new GameScreen();
	Level level = new Level(gameScreen);
	Player player = new Player(0, 0, level);
	
	@Test
	public void getTryHitTimeTest() {
		player.setTryHitTime(0.5f);
		Assert.assertTrue(player.getTryHitTime() == 0.5f);
	}
	
	@Test
	public void pickUpItemTest() {
		Ray ray = new Ray(1, 1, level);
		player.pickupItem(ray);
		Assert.assertTrue(player.usableItem == ray);
	}
	
	@Test
	public void useItemTest() {
		player.useItem();
		Assert.assertTrue(player.usableItem == null);
	}
	
	@Test
	public void takeDamageTest() {
		player.takeDamage();
		Assert.assertTrue(player.health == 4);
	}
	
	@Test
	public void isIvulnerableeTest() {
		Assert.assertTrue(player.isInvulnerable());
	}
	
	@Test
	public void canFinishGame() {
		player.gotGem = true;
		player.gotScroll = true;
		player.gotTalisman = true;
		Assert.assertTrue(player.canFinishGame());
	}
}
