package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
// public static final: constants, used by any class
	public  static final int WIDTH=400, HEIGHT=700;
	public static final int START=0, RUNNING=1, PAUSE=2, GAME_OVER=3;
	private int state=START;
	private Sky sky=new Sky();
	private Hero hero=new Hero();
	private FlyingObject[] enemies= {};
	private Bullet[] bullets= {};

// load picture for state
	private static BufferedImage start, pause, gameover;
	static {
		start=FlyingObject.LoadImage("start.png");
		pause=FlyingObject.LoadImage("pause.png");
		gameover=FlyingObject.LoadImage("gameover.png");
	}
	
	public FlyingObject nextOne(){
		Random rand = new Random(); 
		int type = rand.nextInt(20); 
		if(type<7){
			return new Airplane();
		}else if(type<12){
			return new BigAirplane();
		}else if (type<16) {
			return new Bee();
		}else {
			return new SpecialBee();
		}
	}
	
	// enemies enter to picture
	int enterIndex = 0; 
	public void enterAction(){ 
		enterIndex++; 
		if(enterIndex%40==0){ 
			FlyingObject obj = nextOne(); 
			enemies = Arrays.copyOf(enemies,enemies.length+1); 
			enemies[enemies.length-1] = obj; 
		}
	}
	
	// object moves
	public void stepAction(){ 
		sky.step(); 
		for(int i=0;i<enemies.length;i++){ 
			enemies[i].step(); 
		}
		for(int i=0;i<bullets.length;i++){ 
			bullets[i].step(); 
		}
	}	
	
	int shootIndex = 0; 
	public void shootAction(){ 
		shootIndex++; 
		if(shootIndex%30==0){ 
			Bullet[] bs = hero.shoot(); 
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length); 
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length); 
		}
	}
		
	// delete object out of bound
	public void outOfBoundsAction(){ 
		int index = 0; 
		FlyingObject[] enemyLives = new FlyingObject[enemies.length]; 
		for(int i=0;i<enemies.length;i++){ 
			FlyingObject f = enemies[i]; 
			if(!f.outOfBounds() && !f.isRemove()){ 
				enemyLives[index] = f; 
				index++; 
			}
		}
		enemies = Arrays.copyOf(enemyLives,index); 
		
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length]; 
		for(int i=0;i<bullets.length;i++){ 
			Bullet b = bullets[i]; 
			if(!b.outOfBounds() && !b.isRemove()){
				bulletLives[index] = b; 
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives,index);
		
	}
	
	int score=0;
	public void bulletBangAction(){ 
		for(int i=0;i<bullets.length;i++){ 
			Bullet b = bullets[i]; 
			for(int j=0;j<enemies.length;j++){ 
				FlyingObject f = enemies[j]; 
				if(b.isLife() && f.isLife() && f.hit(b)){ 
					b.setDead(); 
					f.setDead(); 
// force covert to interface to add score					
					if (f instanceof Enemy) {
						Enemy e=(Enemy)f;
						score+=e.getScore();
					}
// operations to get award
					if (f instanceof Award) {
						Award a=(Award) f;
						switch (a.getType()) {
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						case Award.LIFE:
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}
	
	public void heroBangAction() {
		for(int i=0;i<enemies.length;i++){ 
			FlyingObject f = enemies[i]; 
			if(hero.isLife() && f.isLife() && f.hit(hero)){ 
				f.setDead(); 
				hero.subtractLife();
				hero.clearDoubleFire();
			}
		}
	}
	
	public void checkFinishAction() {
		if(hero.getLife()<=0) {
			hero.setDead();
			state=GAME_OVER;
		}
	}
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state==RUNNING) {
					int x=e.getX();
					int y=e.getY();
					hero.moveTo(x, y);					
				}
			}
			public void mouseClicked(MouseEvent e) {
				switch (state) {// action for mouse click
				case START:
					state=RUNNING;
					break;
				case GAME_OVER:// clear the screen first
					score=0;
					sky=new Sky();
					hero=new Hero();
					enemies=new FlyingObject[0];
					bullets=new Bullet[0];
					state=START;
					break;
				}
			}
			public void mouseExited(MouseEvent e) {// mouse move out of frame
				if (state==RUNNING)
					state=PAUSE;
			}

			public void mouseEntered(MouseEvent e) {// mouse move into window
				if (state==PAUSE)
					state=RUNNING;
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		
		int interval=10;
		Timer timer=new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (state==RUNNING) {
					enterAction();
					stepAction();
					shootAction();
					outOfBoundsAction();
					bulletBangAction();
					heroBangAction();
					checkFinishAction();					
				}
				repaint();		
			}
		}, interval, interval);
	}
// override paint in super class	r
	public void paint(Graphics g) {
		sky.paintObject(g);
		hero.paintObject(g);
		for (int i = 0; i < enemies.length; i++) {
			enemies[i].paintObject(g);
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].paintObject(g);
		}
// draw score and life
		g.drawString("Score:"+score, 10, 25);
		g.drawString("Life:"+hero.getLife(), 10, 45);
		
		switch (state) {
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
		}
		
		
	}
	public static void main(String[] args) {
		JFrame frame=new JFrame();
		World world=new World();	
		frame.add(world);
// stop the program when close the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// configure frame
		frame.setSize(WIDTH,HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);// visible window, call paint()
		world.action();
	}

}
