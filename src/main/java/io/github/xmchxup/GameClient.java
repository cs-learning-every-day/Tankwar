package io.github.xmchxup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class GameClient extends JComponent {

	private final Tank playerTank;

	private GameClient() {
		this.playerTank = new Tank(400, 300, Direction.DOWN);
		this.setPreferredSize(new Dimension(800, 600));
	}

	@Override
	protected void paintComponent(Graphics g) {
		playerTank.draw(g);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("坦克大战！");
		frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());

		GameClient client = new GameClient();
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
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
