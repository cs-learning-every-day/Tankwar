package io.github.xmchxup;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class TankTest {
	@Test
	void getImage() {
		for (Direction direction : Direction.values()) {
			Tank tank = new Tank(0, 0, false, direction);
			assertTrue(tank.getImage().isPresent(),
					direction + " cannot get valid image!");

			Tank enemyTank = new Tank(0, 0, true, direction);
			assertTrue(tank.getImage().isPresent(),
					direction + " cannot get valid image!");
		}
	}
}
