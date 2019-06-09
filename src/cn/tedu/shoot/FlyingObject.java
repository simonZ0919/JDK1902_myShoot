package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
// public: method, private: variable(sub), protected: variable(super)
public abstract class FlyingObject {
	public static final int LIFE=0, DEAD=1, REMOVE=2;// 3 states of picture
	protected int state=LIFE; // default state	
	protected int width,height,x,y;
// constructor for Airplane, BigAirplane, Bee
	public FlyingObject(int width, int height){
		this.width=width;
		this.height=height;
		Random rand=new Random();// create random object
		x=rand.nextInt(World.WIDTH-this.width);
		y=-this.height;
	}
// constructor for Hero, Sky, Bullet
	public FlyingObject(int width, int height, int x, int y){
		this.width=width;
		this.height=height;
		this.x=x;
		this.y=y;
	}
// static: independent of object, save memory
	public static BufferedImage LoadImage(String filename) {
		try {
// read image in the same package
			BufferedImage img=ImageIO.read(FlyingObject.class.getResource(filename));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
// abstract method: overloaded in subclass
	public abstract void step();
	public abstract BufferedImage getImage() ;
	public abstract boolean outOfBounds();
	public Boolean isLife() {
		return state==LIFE;
	}
	public Boolean isDead() {
		return state==DEAD;
	}
	public Boolean isRemove() {
		return state==REMOVE;
	}
	public void paintObject(Graphics g) {// print image on screen
		g.drawImage(getImage(), x, y, null);
	}
// if object is touched  in neighboring region
	public boolean hit(FlyingObject other){
		int x1 = this.x-other.width;  
		int x2 = this.x+this.width;   
		int y1 = this.y-other.height; 
		int y2 = this.y+this.height;  
		int x = other.x; 
		int y = other.y; 
		return x>=x1 && x<=x2&&
			   y>=y1 && y<=y2; 
	}
	public void setDead() {
		state=DEAD;
	}
}
