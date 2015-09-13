package edu.oswego.moxie.eguimaraes.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.oswego.moxie.eguimaraes.control.Control;
import edu.oswego.moxie.eguimaraes.domain.Train;
import edu.oswego.moxie.eguimaraes.domain.TrainStation;

//http://zetcode.com/tutorials/javagamestutorial/animation/
public class Board extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8204712341398769043L;
	public static final int WIDTH = 686;
	public static final int HEIGHT = 548;
	private final int DELAY = 25;

	private Control control;
	private Thread animator;

	Image background;

	public Board(Control control) {
		this.control = control;
		init();
	}

	private void init() {
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		background = new ImageIcon("img/grass.png").getImage();
		control.loadImages();
		control.initStartLocations();
	}

	@Override
	public void addNotify() {
		super.addNotify();

		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g, background, 0, 0);
		for (Train train : control.trains) {
			rotateAndDraw(train.getAngle(), g, train.getImage(), train.getX(), train.getY());
		}
		
		for (TrainStation station : control.stations) {
			draw(g, station.getImage(), station.getX(), station.getY());
		}
	}

	// private void drawStar(Graphics g) {
	// g.drawImage(star, x, y, this);
	// Toolkit.getDefaultToolkit().sync();
	// }
	//
	// private void drawGrass(Graphics g) {
	// g.drawImage(grass, 0, 0, this);
	// Toolkit.getDefaultToolkit().sync();
	// }
	//
	//
	// private void drawstation(Graphics g) {
	// g.drawImage(trainStation, 200, 300, this);
	// Toolkit.getDefaultToolkit().sync();
	// }

	private void draw(Graphics g, Image image, int x, int y) {
		g.drawImage(image, x, y, this);
		Toolkit.getDefaultToolkit().sync();
	}

	private void rotateAndDraw(double radios, Graphics g, Image image, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform at = new AffineTransform();
		at.setToRotation(radios, x, y);
		g2d.setTransform(at);
		g2d.drawImage(image, x, y, this);
		g2d.dispose();
		Toolkit.getDefaultToolkit().sync();
	}

	private void cycle() {

		for (Train train : control.trains) {
			train.updatePosition();
		}

		// x += 1;
		// y += 1;
		//
		// if (y > B_HEIGHT) {
		//
		// y = INITIAL_Y;
		// x = INITIAL_X;
		// }
	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {

			cycle();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("Interrupted: " + e.getMessage());
			}

			beforeTime = System.currentTimeMillis();
		}
	}
}