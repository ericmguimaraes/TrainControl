package edu.oswego.moxie.eguimaraes.animation;

import java.awt.Image;

import javax.swing.ImageIcon;

public abstract class GraphicElement {
	
	private int x, y;
	private Image image;
	
	private String IMAGE_LOCATION;
	
	public GraphicElement(String image_location) {
		IMAGE_LOCATION = image_location;
	}
	
	public Image loadImage() {
		image = new ImageIcon(IMAGE_LOCATION).getImage();
		return image;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Image getImage(){
		return image;
	}
}
