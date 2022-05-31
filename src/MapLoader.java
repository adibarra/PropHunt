import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

//Alec Ibarra
public class MapLoader 
{
	static String mapSelected = "null";
	static int numRows = 28;//Maps must be 50x35 chars
	static int numCols = 40;
	static String background[];
	static String fileName = "null";
	static String newFileName = "";
	static int CurrentMap = 0;
	static int hSpawnX = -100;
	static int hSpawnY = -100;
	static int pSpawnX = -100;
	static int pSpawnY = -100;
	
	static Graphics g2;
	static BufferedImage bgPic = null;	
	Graphics2D g2D = (Graphics2D) g2;

	
	public static void prepare()
	{
		bgPic = new BufferedImage(Logic.screenSizeX, Logic.screenSizeY, BufferedImage.TYPE_INT_ARGB);
		g2 = bgPic.getGraphics();
		
		changeMap("1");
		
		for (int r = 0; r < numRows; r++)//Draws the map based on the map loaded
			for (int c = 0; c < numCols; c++)
				switch(background[r].charAt(c))
					{
					    case '#' : drawFloor(r,c); break;
						case '=' : drawSideWall(r,c); addWall(r,c); break;
						case 'I' : drawVertWall(r,c); addWall(r,c); break;
						case '.' : drawSpace(r,c); addWall(r,c); break;
						case 'o' : drawCorner(r,c); addWall(r,c); break;
						case 'p' : drawSpawnProp(r,c); break;
						case 'h' : drawSpawnHunter(r,c); break;
						case '<' : drawSideDoor(r,c); break;
						case '^' : drawVertDoor(r,c); break;
						case '|' : drawVertLines(r,c); break;
						default  : drawFloor(r,c);
					}
		EntityKeeper.addObjects();
		EntityKeeper.addDoors();
		EntityKeeper.buildWallZone();
		EntityKeeper.buildRooms();
	}
	
	public static void changeMap(String fileName)
	{
		if(fileName == "1")
		{
			CurrentMap = 1;
		}
		background = new String[numRows];
		try
		{
            BufferedReader inStream = new BufferedReader(new InputStreamReader(MainLoop.class.getClassLoader().getResourceAsStream(fileName+".dat")));
			String line;
			int row = 0;
			while((line = inStream.readLine()) != null)
			{
				background[row++] = line;
			}
			inStream.close();
		}
		catch (IOException e)
		{
			System.out.println("There were problems with the code as stated below\n");
			System.out.println(e.getMessage());
		}
		mapSelected = fileName;
		
		for (int r = 0; r < MapLoader.numRows; r++)//Draws the map based on the map loaded
			for (int c = 0; c < MapLoader.numCols; c++)
				switch(MapLoader.background[r].charAt(c))
				{
					case '?' : EntityKeeper.players.get(0).setXPos(c*20); EntityKeeper.players.get(0).setYPos(r*20);
				}
	}
	
	public static void chooseMap()//Level loader
	{	
		changeMap(JOptionPane.showInputDialog("Enter Level Name: MapTest"));
	}
	
	public static void addWall(int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		EntityKeeper.walls.add(new Rectangle(x,y,Logic.worldScale,Logic.worldScale));
	}

	public static int convert(int q)
	{
		return q * Logic.worldScale;
	}

	public static void drawSpace (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(Color.black);
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
	}

	public static void drawSideWall (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(Color.white);
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawLine(x,y,x+Logic.worldScale,y);
		g2.drawLine(x,y+Logic.worldScale-1,x+Logic.worldScale,y+Logic.worldScale-1);
	}
	
	public static void drawVertWall (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(Color.white);
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawLine(x,y,x,y+Logic.worldScale);
		g2.drawLine(x+Logic.worldScale-1,y,x+Logic.worldScale-1,y+Logic.worldScale-1);
	}
	
	public static void drawVertLines (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(new Color(200, 200, 200));//Light Gray
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawLine(x,y,x,y+Logic.worldScale);
		g2.drawLine(x+Logic.worldScale-1,y,x+Logic.worldScale-1,y+Logic.worldScale-1);
	}
	
	public static void drawSideDoor (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(new Color(200, 200, 200));//Light Gray
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawLine(x,y,x+Logic.worldScale,y);
		g2.drawLine(x,y+Logic.worldScale-1,x+Logic.worldScale,y+Logic.worldScale-1);
	}
	
	public static void drawVertDoor (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(new Color(200, 200, 200));//Light Gray
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawLine(x,y,x,y+Logic.worldScale);
		g2.drawLine(x+Logic.worldScale-1,y,x+Logic.worldScale-1,y+Logic.worldScale-1);
	}

	public static void drawFloor (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(new Color(200, 200, 200));//Light Gray
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
	}

	public static void drawCorner(int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(Color.white);
		g2.fillRect(x,y,Logic.worldScale-1,Logic.worldScale-1);
		g2.setColor(Color.black);
		g2.drawRect(x,y,Logic.worldScale-1,Logic.worldScale-1);
	}

	public static void drawSpawnProp (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		int halfSize = Logic.worldScale/2;
		//Inside
		g2.setColor(new Color(100, 100, 255));//Blue
		g2.fillRect(x+5,y+5,Logic.worldScale-10,Logic.worldScale-10);
		g2.drawImage(ImageLoader.getImage("spawn"), x, y, null);
		
		pSpawnX = x+halfSize;
		pSpawnY = y+halfSize;
	}

	public static void drawSpawnHunter (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		int halfSize = Logic.worldScale/2;
		//Inside
		g2.setColor(new Color(255, 100, 100));//Red
		g2.fillRect(x+5,y+5,Logic.worldScale-10,Logic.worldScale-10);
		g2.drawImage(ImageLoader.getImage("spawn"), x, y, null);
		
		hSpawnX = x+halfSize;
		hSpawnY = y+halfSize;
	}

	public static void drawUnknown (int r, int c)
	{
		int x = convert(c);
		int y = convert(r);
		g2.setColor(Color.pink);
		g2.fillRect(x,y,Logic.worldScale,Logic.worldScale);
		g2.setColor(Color.black);
		g2.drawOval(x,y,19,19);
		g2.drawString("?",x+6,y+15);
		
	}

}