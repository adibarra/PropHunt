import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

//Alec Ibarra
public class EntityKeeper {
	
	@SuppressWarnings("rawtypes")
	static ArrayList<ArrayList> everything = new ArrayList<ArrayList>();
	static ArrayList<Player> players = new ArrayList<Player>();
	static ArrayList<Entity> objects = new ArrayList<Entity>();
	
	static ArrayList<Door> doors = new ArrayList<Door>();
	static ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
	static ArrayList<Connector> connectors = new ArrayList<Connector>();
	static ArrayList<Point> objectPoints = new ArrayList<Point>();
	static ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
	
	static Area fogOfWar = new Area();
	static Area currentRoom = new Area();
	static Area currentFog = new Area();
	static Area wallZone = new Area();
	static Area objectZone = new Area();
	static Rectangle2D bigScreen = new Rectangle(-100,-100,1100,800);
	static Rectangle2D screen = new Rectangle(0,0,1000,700);
	static int height = (Logic.screenSizeY-21)/Logic.worldScale;
	static int width = Logic.screenSizeX/Logic.worldScale;
	
	static Rectangle2D tempRec = new Rectangle();
	static Area tempArea = new Area();
	static Random r = new Random();
	
	static String CardboardBoxLore = "[YELLOW]Just a normal cardboard box\n";
	static String WoodenBoxLore = "[YELLOW]Just a normal wood box\n";
	static String BarrelLore = "[YELLOW]Just a normal barrel\n";
	static String LampLore = "[YELLOW]Just a normal lamp\n";
	static String BedLore = "[YELLOW]Just a normal bed\n";
	static String ChairLore = "[YELLOW]Just a normal chair\n";
	static String TableLore = "[YELLOW]Just a normal table\n";
	static String PlateLore = "[YELLOW]Just a normal plate\n";
	static String WasherLore = "[YELLOW]Just a normal washing machine\n";
	static String BasketLore = "[YELLOW]Just a normal laundry basket\n";
	
	public static void prepare()
	{
		everything.clear();
		everything.add(objects);
		everything.add(players);
	}
	
	public static void addObjects()
	{
		objects.clear();
		if(MapLoader.CurrentMap == 1)
		{
			//Room 1
			objects.add(new Entity(150,50,new String[]{"objectCardboardBox",},0,true,r.nextInt(),CardboardBoxLore));
			objects.add(new Entity(175,50,new String[]{"objectBarrel"},0,true,r.nextInt(),BarrelLore));
			objects.add(new Entity(385,50,new String[]{"objectLamp"},0,true,r.nextInt(),LampLore));
			objects.add(new Entity(400,50,new String[]{"objectBed"},0,true,r.nextInt(),BedLore));
			objects.add(new Entity(195,155,new String[]{"objectChair"},270,true,r.nextInt(),ChairLore));
			objects.add(new Entity(150,150,new String[]{"objectTable"},0,true,r.nextInt(),TableLore));
			objects.add(new Entity(178,156,new String[]{"objectPlate"},270,true,r.nextInt(),PlateLore));
			
			//Room 2
			objects.add(new Entity(450,50,new String[]{"objectCardboardBox"},0,true,r.nextInt(),CardboardBoxLore));
			objects.add(new Entity(450,75,new String[]{"objectCardboardBox"},0,true,r.nextInt(),CardboardBoxLore));
			objects.add(new Entity(475,50,new String[]{"objectCardboardBox"},0,true,r.nextInt(),CardboardBoxLore));
			objects.add(new Entity(525,150,new String[]{"objectWoodenBox"},0,true,r.nextInt(),WoodenBoxLore));
			objects.add(new Entity(550,150,new String[]{"objectWoodenBox"},0,true,r.nextInt(),WoodenBoxLore));
			objects.add(new Entity(550,125,new String[]{"objectWoodenBox"},0,true,r.nextInt(),WoodenBoxLore));
			
			//Room 3
			objects.add(new Entity(675,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(700,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(725,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(750,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(775,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(800,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(825,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(850,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(875,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(900,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			objects.add(new Entity(925,50,new String[]{"objectWasher"},0,true,r.nextInt(),WasherLore));
			
			objects.add(new Entity(600,150,new String[]{"objectBasket"},0,false,r.nextInt(),BasketLore));
			
		}
	}
	
	public static void buildRooms()
	{
		rooms.clear();
		connectors.clear();
		if(MapLoader.CurrentMap == 1)
		{
			/////////////Rooms//////////////
			//First Row ->>
			rooms.add(new Rectangle(50,50,75,125));//room 0
			rooms.add(new Rectangle(150,50,275,125));//room 1
			rooms.add(new Rectangle(450,50,125,125));//room 2
			rooms.add(new Rectangle(600,50,350,125));//room 3
			
			//Second row <<-
			rooms.add(new Rectangle(600,200,350,250));//room 4
			rooms.add(new Rectangle(50,200,525,175));//room 5
			
			//Thrid row ->>
			rooms.add(new Rectangle(50,400,250,100));//room 6
			rooms.add(new Rectangle(325,400,250,175));//room 7
			rooms.add(new Rectangle(600,475,225,100));//room 8
			rooms.add(new Rectangle(850,475,100,100));//room 9
			
			//Fourth row <<-
			rooms.add(new Rectangle(325,600,625,50));//room 10
			rooms.add(new Rectangle(50,525,250,125));//room 11
			
			///////////Connectors///////////
			//First row ->>
			connectors.add(new Connector(new Rectangle(125,75,25,75),0,1));//Connector 0
			connectors.add(new Connector(new Rectangle(300,175,50,25),1,5));//Connector 1
			connectors.add(new Connector(new Rectangle(425,125,25,50),1,2));//Connector 2
			connectors.add(new Connector(new Rectangle(575,50,25,50),2,3));//Connector 3
			connectors.add(new Connector(new Rectangle(800,175,50,25),3,4));//Connector 4
			
			//Second row <<-
			connectors.add(new Connector(new Rectangle(825,500,25,50),8,9));//Connector 5
			connectors.add(new Connector(new Rectangle(675,450,50,25),4,8));//Connector 6
			connectors.add(new Connector(new Rectangle(450,375,50,25),5,7));//Connector 7
			connectors.add(new Connector(new Rectangle(300,400,25,50),6,7));//Connector 8
			
			//Thrid row ->>
			connectors.add(new Connector(new Rectangle(125,500,50,25),6,11));//Connector 9
			connectors.add(new Connector(new Rectangle(525,575,50,25),7,10));//Connector 10
			connectors.add(new Connector(new Rectangle(900,575,50,25),9,10));//Connector 11
		}
		
		for(int k = 0; k < EntityKeeper.rooms.size(); k++)
		{
			fogOfWar.add(new Area (EntityKeeper.rooms.get(k)));
		}
	}
	
	public static void addDoors()
	{
		doors.clear();
		if(MapLoader.CurrentMap == 1)
		{
			//connector 1
			doors.add(new Door(300,175,"left"));
			doors.add(new Door(325,175,"right"));
			//connector 2
			doors.add(new Door(425,125,"up"));
			doors.add(new Door(425,150,"down"));
			//connector 3
			doors.add(new Door(575,50,"up"));
			doors.add(new Door(575,75,"down"));
			//connector 4
			doors.add(new Door(800,175,"left"));
			doors.add(new Door(825,175,"right"));
			//connector 5
			doors.add(new Door(825,500,"up"));
			doors.add(new Door(825,525,"down"));
			//connector 6
			doors.add(new Door(675,450,"left"));
			doors.add(new Door(700,450,"right"));
			//connector 7
			doors.add(new Door(450,375,"left"));
			doors.add(new Door(475,375,"right"));
			//connector 8
			doors.add(new Door(300,400,"up"));
			doors.add(new Door(300,425,"down"));
			//connector 9
			doors.add(new Door(125,500,"left"));
			doors.add(new Door(150,500,"right"));
			//connector 10
			doors.add(new Door(525,575,"left"));
			doors.add(new Door(550,575,"right"));
			//connector 11
			doors.add(new Door(900,575,"left"));
			doors.add(new Door(925,575,"right"));
		}
		
		for(int k = 0; k < EntityKeeper.doors.size(); k++)
		{
			EntityKeeper.doors.get(k).prepare();
		}
	}

	public static void buildWallZone()//call whenever a wall is placed
	{
		wallZone = new Area();
		
		//creates barrier around screen
		tempArea = new Area(bigScreen);
		wallZone.add(tempArea);
		tempArea = new Area(screen);
		wallZone.subtract(tempArea);
		
		//adds walls to wallzone
		for (int k = 0; k < EntityKeeper.walls.size(); k++)
        {
			tempRec = EntityKeeper.walls.get(k);
			tempArea = new Area(tempRec);
			wallZone.add(tempArea);
        }
		
		objectZone = new Area();
		
		//adds objects to objectzone
		for (int k = 0; k < EntityKeeper.objects.size(); k++)
        {
			tempRec = EntityKeeper.objects.get(k).getHitBox();
			tempArea = new Area(tempRec);
			objectZone.add(tempArea);
        }

	}

}
