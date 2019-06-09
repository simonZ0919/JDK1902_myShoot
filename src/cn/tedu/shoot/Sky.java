package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
// load static image(save resources)
	private static BufferedImage image;
	static {
		image=LoadImage("background.png");
	}
	
	private int speed,y1;
	public Sky() {
		super(World.WIDTH, World.HEIGHT, 0, 0);
		speed=1;
		y1=-height;
	}
// override the function in abstract(parent) class	
	public void step() {
		y+=speed;  
		y1+=speed; 
		if(y>=this.height){ 
			y=-this.height; 
		}
		if(y1>=this.height){
			y1=-this.height; 
		}
	}
	

	public BufferedImage getImage() {
		return image;
	}
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);// first image
		g.drawImage(getImage(), x, y1, null);// second image
	}
	public boolean outOfBounds() {
		return false;
	}
}
