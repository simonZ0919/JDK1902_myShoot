package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject{
	private static BufferedImage image;
	static {
		image=LoadImage("bullet.png");
	}
	private int speed;
	public Bullet(int x, int y){
		super(8, 14, x, y);
		speed=3;
	}
	public void step() {
		y-=speed;
	}
// get image or null, set state
	public BufferedImage getImage() {
		if (isLife())
			return image;
		else if (isDead())
			state=REMOVE;// remove the image 
		return null;
	}
	public boolean outOfBounds() {
		return this.y<=-this.height;
	}
}
