package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class SpecialBee extends FlyingObject implements Award, Enemy{
	private static BufferedImage[] images;
	static {
		images=new BufferedImage[11];
		for (int i = 0; i < images.length; i++) {
			images[i]=LoadImage(i+".png");
		}
	}
	private int xspeed,yspeed,awaredType;
	public SpecialBee() {
		super(56, 48);
		Random rand=new Random();
		int[] arr=new int[] {1,-1};
		xspeed=arr[rand.nextInt(2)];
		yspeed=2;
		awaredType=rand.nextInt(2);
	}
	public void step() {
		x+=xspeed;
		y+=yspeed;
		if (x<=0||x>=World.WIDTH-this.width) {
			xspeed*=-1;
		}
			
	}
	int index=1;
	public BufferedImage getImage() {
		if (isLife())
			return images[0];
		else if(isDead()) {
			BufferedImage img=images[index++];
			if(index==images.length)// if disappeared, remove image
				state=REMOVE;
			return img;
		}
		return null;
	}
	public boolean outOfBounds() {
		return this.y>=World.HEIGHT;
	}
	public int getType() {
		return awaredType;
	}
	public int getScore() {
		return 1;
	}
}
