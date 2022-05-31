import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

//Alec Ibarra
public class MainLoop
{

    public static void main(String[] args)
    {
    	Logic game = new Logic();
		
		game.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
		{System.exit(0);}});
		game.setSize(1000,721);
		game.setResizable(false);
		game.setVisible(true);
    
		game.addComponentListener(new ComponentListener() {
        public void componentResized(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentShown(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
    });
    }
}

@SuppressWarnings("serial")
class Logic extends Frame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener
{
	
	//Game state control
	static boolean gameStarted = false;
	static boolean paused = false;
	
	//Game control
	static final int TARGET_FPS = 40;//Integer.valueOf(JOptionPane.showInputDialog("Please enter a target FPS",40));
	static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;//in nanoseconds
	static final float TICK_TIME = 10 / (float)TARGET_FPS;
	static long now = System.nanoTime();
	static long updateLength = 0;
	static double lastLoopTime = System.nanoTime();
	static long lastFpsTime = 0;
	static int fps = 0;
	static double delta = 0;
	static long delay = 0;
	static int displayFPS = 0;
	static int lag = 0;
	static int mousex = 0;
	static int mousey = 0;
	static int mousexclick = 0;
	static int mouseyclick = 0;
	
	//Game settings
	static boolean FogOfWar = true;
	static boolean ControlsSpinMouse = false;
	static boolean ControlsSpinKey1 = false;
	static boolean ControlsSpinKey2 = false;
	static boolean ControlsSpinKey3 = false;
	static boolean ControlsSpinKey4 = true;
	static boolean ControlsMove1 = false;
	static boolean ControlsMove2 = false;
	static boolean ControlsMove3 = false;
	static boolean ControlsMove4 = true;
	
	//Player control
	static boolean up = false;
	static boolean down = false;
	static boolean right = false;
	static boolean left = false;
	static boolean spinRight = false;
	static boolean spinLeft = false;
	static boolean spinReset = false;
	
	//Debug and game control
	static boolean debug = false;
	static boolean debug1 = false;
	static boolean debug2 = false;
	static boolean debug3 = false;
	static boolean debug4 = false;
	static boolean entityChooser = false;
	
	//Double buffering
	static int worldScale = 25;
	static int screenSizeX = 1000;
	static int screenSizeY = 721;
	static Graphics g2;
	static Image virtualMem = null;
	
	//Prepare class methods
	static ImageLoader im = new ImageLoader();
	//TODO static AudioLoader al = new AudioLoader();
	
	//Prepare instances
	static Player player = new Player(75,100,0,0);
	static Player ai = new Player(75,100,0,0,true);
	
	//Prepare temp instances
	static Player tempPlayer = new Player();
	static Entity tempEntity = new Entity();
	static Door tempDoor = new Door();
	
	
	public Logic()
	{
		super("PropHunt");
        addMouseListener(this);//adds neccessary listener
		addMouseMotionListener(this);//adds neccessary listener
		addMouseWheelListener(this);//adds neccessary listener
		addKeyListener(this);//adds neccessary listener
        requestFocusInWindow();//calls focus for window
        
        EntityKeeper.prepare();
        EntityKeeper.players.add(player);
        EntityKeeper.players.add(ai);
        MapLoader.prepare();
        
        //TODO ONLY FOR USE DURING TESTING ERASE AFTER
        gameStarted = true;
        FogOfWar = false;
	}
	
	public void update(Graphics g)
	{
		//prepare double buffer
		((Graphics2D) g).translate(0, 21);
		virtualMem = createImage(screenSizeX, screenSizeY);//create image with screen size
		g2 = virtualMem.getGraphics();
		
		if(gameStarted && !paused)
		{
			calcTime();
			gameLogic(delta);
			displayGame(g2);
		}
		else if(gameStarted && paused)
		{
			displayPauseScreen(g2);
		}
		else if(!gameStarted && !paused)
		{
			displayTitleScreen(g2);
		}
				
		g.drawImage(virtualMem, 0, 0, this);
		
		delay(27);
		repaint();
	}
	
	/** --LOGIC METHODS-- **/
	
	public void gameLogic(double timeElapsed)
	{
		worldLogic(timeElapsed);
		objectLogic(timeElapsed);
		playerLogic(timeElapsed);
	}
	
	public void worldLogic(double timeElapsed)
	{
		for(int k = 0; k < EntityKeeper.doors.size(); k++)
		{
			tempDoor = EntityKeeper.doors.get(k);
			boolean playersByCurrentDoor = false;
			
			for(int j = 0; j < EntityKeeper.players.size(); j++)
			{
				tempPlayer = EntityKeeper.players.get(j);
				
				if(Math.hypot((int)tempPlayer.getXPos()+(int)tempPlayer.getIconWidth()/2-(int)tempDoor.getStartX(),
						(int)tempPlayer.getYPos()+(int)tempPlayer.getIconHeight()/2-(int)tempDoor.getStartY()) < 50)
				{
					playersByCurrentDoor = true;
					tempDoor.setRelease(false);
					tempDoor.activate();
				}
			}
			
			if(!playersByCurrentDoor)
			{
				tempDoor.setRelease(true);
			}
		}
	}
	
	public void objectLogic(double timeElapsed)
	{
		for(int k = 0; k < EntityKeeper.objects.size(); k++)
		{
			tempEntity = EntityKeeper.objects.get(k);
			tempEntity.animate();
		}
	}
	
	public void playerLogic(double timeElapsed)
	{
		player.move(timeElapsed);
		ai.moveAI(timeElapsed);
	}
	
	
	/** --DISPLAY METHODS-- **/
	
	public void displayTitleScreen(Graphics g2)
	{
		
	}
	
	public void displayPauseScreen(Graphics g2)
	{
		g2.setFont(new Font("Ariel",Font.BOLD,20));
		
		//Fog of War setting
		g2.drawString("Fog Of War", 80, 70);
		if (FogOfWar)
			g2.drawImage(ImageLoader.getImage("guiCheckedCheckbox"), 50, 50, this);
		else
			g2.drawImage(ImageLoader.getImage("guiCheckbox"), 50, 50, this);
	}
	
	public void displayGame(Graphics g2)
	{
		displayWorld(g2);
		displayEntities(g2);
		displayEffects(g2);
		displayHUD(g2);
	}
	
	public void displayWorld(Graphics g2)
	{	
		AffineTransform at = new AffineTransform();
		at.scale(1,1);

		((Graphics2D) g2).drawImage(MapLoader.bgPic, at, this);
		
		for(int k = 0; k < EntityKeeper.doors.size(); k++)
		{
			tempDoor = EntityKeeper.doors.get(k);
				
			if(tempDoor.getDirection().equals("left"))
			{
				g2.setColor(new Color(240,240,240));
				g2.fillRect((int)tempDoor.getXPos(),(int)tempDoor.getYPos()+3,tempDoor.getCurrentWidth(),tempDoor.getHeight()-6);
			}
			else if(tempDoor.getDirection().equals("right"))
			{
				g2.setColor(new Color(240,240,240));
				g2.fillRect((int)tempDoor.getXPos()+tempDoor.getCurrentWidth(),(int)tempDoor.getYPos()+3,25-tempDoor.getCurrentWidth(),tempDoor.getHeight()-6);
			}
			else if(tempDoor.getDirection().equals("up"))
			{
				g2.setColor(new Color(240,240,240));
				g2.fillRect((int)tempDoor.getXPos()+3,(int)tempDoor.getYPos(),tempDoor.getWidth()-6,tempDoor.getCurrentHeight());
			}
			else if(tempDoor.getDirection().equals("down"))
			{
				g2.setColor(new Color(240,240,240));
				g2.fillRect((int)tempDoor.getXPos()+3,(int)tempDoor.getYPos()+tempDoor.getCurrentHeight(),tempDoor.getWidth()-6,tempDoor.getHeight()-tempDoor.getCurrentHeight());
			}
			
			if (debug)
			{
				g2.setColor(Color.blue);
				((Graphics2D) g2).draw(tempDoor.getHitBox());
			}
		}	
	}
	
	public void displayEntities(Graphics g2)
	{
		for(int k = 0; k < EntityKeeper.everything.size(); k++)
		{
			for(int j = 0; j < EntityKeeper.everything.get(k).size(); j++)
			{
				tempEntity = (Entity) EntityKeeper.everything.get(k).get(j);
				drawEntity(g2,tempEntity);
			}
		}
	}
	
	public void displayEffects(Graphics g2)
	{
			//Fog of war
			g2.setColor(Color.black);
			EntityKeeper.currentFog = (Area)EntityKeeper.fogOfWar.clone();
			EntityKeeper.currentRoom = new Area();
		
			for(int k = 0; k < EntityKeeper.rooms.size(); k++)
			{
				if(EntityKeeper.rooms.get(k).contains(player.getXPos()+player.getIconWidth()/2,player.getYPos()+player.getIconHeight()/2))
				{
					EntityKeeper.currentFog.subtract(new Area(EntityKeeper.rooms.get(k)));
					EntityKeeper.currentRoom.add(new Area(EntityKeeper.rooms.get(k)));
					break;
				}
			}
			
			for(int k = 0; k < EntityKeeper.connectors.size(); k++)
			{
				if(EntityKeeper.connectors.get(k).getArea().contains(player.getXPos()+player.getIconWidth()/2,player.getYPos()+player.getIconHeight()/2))
				{
					EntityKeeper.currentFog.subtract(new Area(EntityKeeper.rooms.get(EntityKeeper.connectors.get(k).getRoom1())));
					EntityKeeper.currentFog.subtract(new Area(EntityKeeper.rooms.get(EntityKeeper.connectors.get(k).getRoom2())));
					
					EntityKeeper.currentRoom.add(new Area(EntityKeeper.rooms.get(EntityKeeper.connectors.get(k).getRoom1())));
					EntityKeeper.currentRoom.add(new Area(EntityKeeper.rooms.get(EntityKeeper.connectors.get(k).getRoom2())));
					break;
				}
			}
		
		if(FogOfWar)
		{
			((Graphics2D) g2).fill(EntityKeeper.currentFog);
		}	
		
		if(debug)
		{
			if(debug2)
			{
				g2.setColor(Color.black);
				for(int k = 0; k < EntityKeeper.height; k++)//rows
					for(int j = 0; j < EntityKeeper.width; j++)//columns
					{
						g2.drawLine(j*worldScale, 0, j*worldScale, 700);
						g2.drawLine(j*worldScale-1, 0, j*worldScale-1, 700);
						g2.drawLine(0, k*worldScale, 1000, k*worldScale);
						g2.drawLine(0, k*worldScale-1, 1000, k*worldScale-1);
					}
			}
			
			if(debug3)
			{
				g2.setColor(new Color(200,50,50,200));
				((Graphics2D) g2).fill(EntityKeeper.wallZone);
				((Graphics2D) g2).fill(EntityKeeper.objectZone);
			}
		
			if (debug4)
			{
				for(int k = 0; k < EntityKeeper.rooms.size(); k++)
					{
						int r = (int)(Math.random()*246)+10;
						int g = (int)(Math.random()*246)+10;
						int b = (int)(Math.random()*246)+10;
						g2.setColor(new Color(r,g,b,50));
						((Graphics2D) g2).fill(EntityKeeper.rooms.get(k));
					}
				
				for(int k = 0; k < EntityKeeper.connectors.size(); k++)
				{
					int r = (int)(Math.random()*246)+10;
					int g = (int)(Math.random()*246)+10;
					int b = (int)(Math.random()*246)+10;
					g2.setColor(new Color(r,g,b,50));
					((Graphics2D) g2).fill(EntityKeeper.connectors.get(k).getArea());
				}
			}	
		}
	}
	
	@SuppressWarnings("unused")
	public void displayHUD(Graphics g2)
	{
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, 46, 14);
		g2.setColor(Color.yellow);
		g2.setFont(new Font("Ariel",Font.BOLD,12));
		g2.drawString("FPS: " + String.valueOf(displayFPS), 12, 22);
		
		g2.drawString("WASD for movement",12,688);
		g2.drawString("Q and E for spin",157,688);
		g2.drawString("Shift for spin reset",272,688);
		g2.drawString("Spacebar for prop menu",402,688);
		
		if(entityChooser)
		{
			g2.setColor(new Color(100,100,100));
			g2.fillRoundRect(100,100,800,500,50,50);
			//Row 1
			g2.drawImage(ImageLoader.getImage("player"),150,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectCardboardBox"),250,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectWoodenBox"),350,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectBarrel"),450,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectLamp"),550,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectBed"),650,150,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectTable"),750,150,100,100,this);
			//Row 2
			g2.drawImage(ImageLoader.getImage("objectChair"),150,250,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectWasher"),250,250,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectPlate"),350,250,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectRShirt"),450,250,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectGShirt"),550,250,100,100,this);
			g2.drawImage(ImageLoader.getImage("objectBShirt"),650,250,100,100,this);
			
			g2.setColor(Color.orange);
			//Row 1
			g2.drawString("Player",150,160);
			g2.drawString("Cardboard Box",250,160);
			g2.drawString("Wooden Crate",350,160);
			g2.drawString("Barrel",450,160);
			g2.drawString("Lamp",550,160);
			g2.drawString("Bed",650,160);
			g2.drawString("Table",750,160);
			//Row 2
			g2.drawString("Chair",150,260);
			g2.drawString("Washer",250,260);
			g2.drawString("Plate",350,260);
			g2.drawString("Red Shirt",450,260);
			g2.drawString("Green Shirt",550,260);
			g2.drawString("Blue Shirt",650,260);
		}
		
		/*//For displaying text on objects
		if(EntityKeeper.currentRoom.contains(mousex,mousey) && EntityKeeper.objectZone.contains(mousex,mousey))
		{
			//Must count backwards so that it detects objects that are on top instead of on bottom
			for(int k = EntityKeeper.objects.size()-1; k > -1; k--)
			{
				tempEntity = EntityKeeper.objects.get(k);
				AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(tempEntity.getRotation()),
						tempEntity.getXPos()+tempEntity.getIconWidth()/2,tempEntity.getYPos()+tempEntity.getIconHeight()/2);
				Shape rotatedHitbox = at2.createTransformedShape(tempEntity.getHitBox());
				
				if(rotatedHitbox.contains(mousex,mousey))
				{
					int numberOfLines=0;
					String tempStr = "";

					g2.setColor(Color.yellow);
					g2.setFont(new Font("Ariel",Font.BOLD,12));
					for (String line : tempEntity.getLore().split("\n")) {
					    numberOfLines++;
					}
					g2.setColor(new Color(0,0,0,100));
					g2.fillRect(mousex+13 - 2, mousey+10 - 12 + 2, 180 + 4, 12 * numberOfLines);
					g2.setColor(Color.yellow);
					numberOfLines=0;
					for (String line : tempEntity.getLore().split("\n")) 
					{
						if(line.indexOf("[YELLOW]") != -1)
						{
							tempStr = line.substring(0,line.indexOf("[YELLOW]"));
							line = line.substring(line.indexOf("[YELLOW]")+8);
							line = tempStr+line;
							g2.setColor(Color.yellow);
						}
						else if(line.indexOf("[GREEN]") != -1)
						{
							tempStr = line.substring(0,line.indexOf("[GREEN]"));
							line = line.substring(line.indexOf("[GREEN]")+7);
							line = tempStr+line;
							g2.setColor(Color.green);
						}
						else if(line.indexOf("[RED]") != -1)
						{
							tempStr = line.substring(0,line.indexOf("[RED]"));
							line = line.substring(line.indexOf("[RED]")+5);
							line = tempStr+line;
							g2.setColor(Color.red);
						}
						else if(line.indexOf("[BLUE]") != -1)
						{
							tempStr = line.substring(0,line.indexOf("[BLUE]"));
							line = line.substring(line.indexOf("[BLUE]")+6);
							line = tempStr+line;
							g2.setColor(Color.blue);
						}
						g2.drawString(line, mousex+13 , mousey+10 + (numberOfLines * 12));
					    numberOfLines++;
					}
					break;
				}
			}
		}
		*/
			
		if(debug)
		{
			if(debug1)
			{
				g2.setColor(new Color(0,0,0,150));
				g2.fillRect(46, 0, 59, 14);
				g2.fillRect(0, 24, 105, 50);
				g2.setColor(Color.yellow);
				g2.setFont(new Font("Ariel",Font.BOLD,12));
				g2.drawString("TickTime: "+delay, 12, 34);
				g2.drawString("Players: "+EntityKeeper.everything.get(1).size(), 12, 46);
				g2.drawString("Objects: "+EntityKeeper.everything.get(0).size(), 12, 58);
				g2.drawString("Mouse: "+mousex +" "+mousey, 12, 70);
				
			}
		}
	}
	
	public void drawEntity(Graphics g2, Entity tempEntity)
	{
		//for each player with correct orientation and location
		AffineTransform at = new AffineTransform();
		//translate image to use center as point of rotation
		at.translate(tempEntity.getXPos()+(tempEntity.getIconWidth()/2),
				tempEntity.getYPos()+(tempEntity.getIconHeight()/2));

		at.rotate(Math.toRadians(tempEntity.getRotation()));
		at.translate(-(tempEntity.getIconWidth()/2), -(tempEntity.getIconHeight()/2));//reposition image
		// draw the image
		//at.scale((float)worldScale/tempEntity.getIconWidth(),(float)worldScale/tempEntity.getIconHeight());
		((Graphics2D) g2).drawImage(tempEntity.getIcon(), at, this);
		at.rotate(Math.toRadians(-tempEntity.getRotation()));
		
		if (debug)
		{
			g2.setColor(Color.blue);
			AffineTransform at2 = AffineTransform.getRotateInstance(Math.toRadians(tempEntity.getRotation()),tempEntity.getXPos()+tempEntity.getIconWidth()/2,tempEntity.getYPos()+tempEntity.getIconHeight()/2);
			Shape rotatedHitbox = at2.createTransformedShape(tempEntity.getHitBox());
			((Graphics2D) g2).draw(rotatedHitbox);
		}
	}
	

	/** --LISTENERS-- **/
	
	public void mouseDragged(MouseEvent e) 
	{
		mousex = e.getX();
		mousey = e.getY()-21;
		mousexclick = e.getX();
		mouseyclick = e.getY()-21;
		
		if(entityChooser)
		{
			//Row 1
			if(new Rectangle(150,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("player"));
			}
			if(new Rectangle(250,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectCardboardBox"));
			}
			if(new Rectangle(350,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectWoodenBox"));
			}
			if(new Rectangle(450,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectBarrel"));
			}
			if(new Rectangle(550,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectLamp"));
			}
			if(new Rectangle(650,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectBed"));
			}
			if(new Rectangle(750,150,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectTable"));
			}
			
			//Row 2
			if(new Rectangle(150,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectChair"));
			}
			if(new Rectangle(250,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectWasher"));
			}
			if(new Rectangle(350,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectPlate"));
			}
			if(new Rectangle(450,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectRShirt"));
			}
			if(new Rectangle(550,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectGShirt"));
			}
			if(new Rectangle(650,250,100,100).contains(mousexclick,mouseyclick))
			{
				player.setIcon(ImageLoader.getImage("objectBShirt"));
			}
			
			entityChooser = !entityChooser;
		}
		if(gameStarted && paused)//displaying pause screen
		{
			if(new Rectangle(50,50,25,25).contains(mousexclick,mouseyclick))
			{
				FogOfWar = !FogOfWar;
			}
		}
	}
	
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_UP:
			{		
				up = true;
				break;
			}
			case KeyEvent.VK_DOWN:
			{
				down = true;
				break;
			}
			case KeyEvent.VK_LEFT:
			{		
				left = true;
				break;
			}
			case KeyEvent.VK_RIGHT:
			{
				right = true;
				break;
			}
			case KeyEvent.VK_W:
			{		
				up = true;
				break;
			}
			case KeyEvent.VK_S:
			{
				down = true;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = true;
				break;
			}
			case KeyEvent.VK_D:
			{
				right = true;
				break;
			}
			case KeyEvent.VK_Q:
			{		
				spinLeft = true;
				break;
			}
			case KeyEvent.VK_E:
			{
				spinRight = true;
				break;
			}
			case KeyEvent.VK_SHIFT:
			{
				spinReset = true;
				break;
			}
			case KeyEvent.VK_SPACE:
			{
				entityChooser = !entityChooser;
				break;
			}
			case KeyEvent.VK_ESCAPE:
			{
				paused = !paused;
				break;
			}
			case KeyEvent.VK_F12:
			{
				debug =!debug;
				break;
			}
			case KeyEvent.VK_F1:
			{
				debug1 = !debug1;
				break;
			}
			case KeyEvent.VK_F2:
			{
				debug2 = !debug2;
				break;
			}
			case KeyEvent.VK_F3:
			{
				debug3 = !debug3;
				break;
			}
			case KeyEvent.VK_F4:
			{
				debug4 = !debug4;
				break;
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
		
			case KeyEvent.VK_UP:
			{		
				up = false;
				break;
			}
			case KeyEvent.VK_DOWN:
			{
				down = false;
				break;
			}
			case KeyEvent.VK_LEFT:
			{		
				left = false;
				break;
			}
			case KeyEvent.VK_RIGHT:
			{
				right = false;
				break;
			}
			case KeyEvent.VK_W:
			{		
				up = false;
				break;
			}
			case KeyEvent.VK_S:
			{
				down = false;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = false;
				break;
			}
			case KeyEvent.VK_D:
			{
				right = false;
				break;
			}
			case KeyEvent.VK_Q:
			{		
				spinLeft = false;
				break;
			}
			case KeyEvent.VK_E:
			{
				spinRight = false;
				break;
			}
		}
	}
	
	/** --UTIL / EXTRA-- **/
	
	public void paint(Graphics g){
		update(g);
	}
	
	public void calcTime()
	{
		now = System.nanoTime();
		delta = (now - lastLoopTime)/10000000;
		lastFpsTime += (now - lastLoopTime);
	    delta = delta/2.6f;
	    lastLoopTime = now;
	    fps++;
	      
	    if (lastFpsTime >= 1000000000)
	    {
	       displayFPS = fps;
	       lastFpsTime = 0;
	       fps = 0;
	    }
	}	
	
	public static void delay(double nt)//better time delay method
	{
		try {
			Thread.sleep((long)nt);
		} catch (InterruptedException e) {}
	}
	
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		mousex = e.getX();
		mousey = e.getY()-21;
	}

	/** --UNUSED LISTENERS-- **/
	
	public void keyTyped(KeyEvent e) {}
	
	public void mouseWheelMoved(MouseWheelEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
}