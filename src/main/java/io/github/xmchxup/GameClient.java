package io.github.xmchxup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class GameClient extends JComponent {

	private static final GameClient INSTANCE = new GameClient();

	private Tank playerTank;
	private List<Tank> enemyTanks;
	private final List<Wall> walls;
	private final List<Missile> missiles;
	private final List<Explosion> explosions;

	private GameClient() {
		this.explosions = new ArrayList<>();
		this.playerTank = new Tank(400, 300, Direction.DOWN);
		this.missiles = new CopyOnWriteArrayList<>();
		this.walls = Arrays.asList(
				new Wall(200, 140, true, 15),
				new Wall(200, 540, true, 15),
				new Wall(100, 80, false, 15),
				new Wall(700, 80, false, 15));
		this.initEnemyTanks();
		this.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
	}

	private void initEnemyTanks() {
		this.enemyTanks = new CopyOnWriteArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				this.enemyTanks.add(new Tank(200 + j * 120, 400 + 40 * i, true,
						GameConfig.ENEMY_SPEED, Direction.UP));
			}
		}
	}

	void addExplosion(Explosion explosion) {
		explosions.add(explosion);
	}

	void add(Missile missile) {
		missiles.add(missile);
	}

	public static GameClient getInstance() {
		return INSTANCE;
	}

	List<Tank> getEnemyTanks() {
		return enemyTanks;
	}

	List<Wall> getWalls() {
		return walls;
	}

	Tank getPlayerTank() {
		return playerTank;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);

		if (!playerTank.isLive()) {
			g.setColor(Color.RED);
			g.setFont(new Font(null, Font.BOLD, 100));
			g.drawString("GAME OVER", 100, 200);
			g.setFont(new Font(null, Font.BOLD, 60));
			g.drawString("PRESS U TO RESTART", 60, 360);
		} else {
			playerTank.draw(g);

			enemyTanks.removeIf(t -> !t.isLive());
			if (enemyTanks.isEmpty()) {
				this.initEnemyTanks();
			}

			for (Tank tank : enemyTanks) {
				tank.draw(g);
			}

			for (Wall wall : walls) {
				wall.draw(g);
			}

			missiles.removeIf(m -> !m.isLive());
			for (Missile missile : missiles) {
				missile.draw(g);
			}

			explosions.removeIf(e -> !e.isLive());
			for (Explosion explosion : explosions) {
				explosion.draw(g);
			}
		}
	}

	void restart() {
		if (!playerTank.isLive()) {
			playerTank = new Tank(400, 100, Direction.DOWN);
		}
		this.initEnemyTanks();
	}

	public static void main(String[] args) {
		com.sun.javafx.application.PlatformImpl.startup(()->{});
		JFrame frame = new JFrame();
		frame.setTitle("坦克大战！");
		frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());

		GameClient client = GameClient.getInstance();
		client.repaint();

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				client.playerTank.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				client.playerTank.keyReleased(e);
			}
		});

		frame.add(client);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		while (true) {
			client.repaint();
			if (client.playerTank.isLive()) {
				for (Tank tank : client.enemyTanks) {
					tank.actRandomly();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
