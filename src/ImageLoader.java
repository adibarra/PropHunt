import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//Alec Ibarra
public class ImageLoader {
	
	static BufferedImage splashScreen, guiBack, guiPause, guiCheckedCheckbox, guiCheckbox;
	static BufferedImage effectFire, effectSmoke, player, noTexture, spawn;
	static BufferedImage cBox, wBox, barrel, lamp, bed, table, chair, washer, plate;
	static BufferedImage rShirt, gShirt, bShirt, basket;
	
	public ImageLoader()
	{
		try {//load images to use
        	
			cBox = ImageIO.read(getClass().getClassLoader().getResource("CardboardBox.png"));
			wBox = ImageIO.read(getClass().getClassLoader().getResource("WoodenBox.png"));
			barrel = ImageIO.read(getClass().getClassLoader().getResource("Barrel.png"));
			lamp = ImageIO.read(getClass().getClassLoader().getResource("Lamp.png"));
			bed = ImageIO.read(getClass().getClassLoader().getResource("Bed.png"));
			table = ImageIO.read(getClass().getClassLoader().getResource("Table.png"));
			chair = ImageIO.read(getClass().getClassLoader().getResource("Chair.png"));
			washer = ImageIO.read(getClass().getClassLoader().getResource("Washer.png"));
			plate = ImageIO.read(getClass().getClassLoader().getResource("Plate.png"));
			rShirt = ImageIO.read(getClass().getClassLoader().getResource("ShirtRed.png"));
			gShirt = ImageIO.read(getClass().getClassLoader().getResource("ShirtGreen.png"));
			bShirt = ImageIO.read(getClass().getClassLoader().getResource("ShirtBlue.png"));
			basket = ImageIO.read(getClass().getClassLoader().getResource("Basket.png"));
			
        	splashScreen = ImageIO.read(getClass().getClassLoader().getResource("SplashScreen.png"));
        	guiBack = ImageIO.read(getClass().getClassLoader().getResource("GUIBack.png"));
        	guiPause = ImageIO.read(getClass().getClassLoader().getResource("GUIPause.png"));
        	guiCheckedCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckedCheckbox.png"));
        	guiCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckbox.png"));

        	player = ImageIO.read(getClass().getClassLoader().getResource("Player.png"));
			spawn = ImageIO.read(getClass().getClassLoader().getResource("Spawn.png"));
        	effectFire = ImageIO.read(getClass().getClassLoader().getResource("EffectFire.png"));
        	effectSmoke = ImageIO.read(getClass().getClassLoader().getResource("EffectSmoke.png"));
        	noTexture = ImageIO.read(getClass().getClassLoader().getResource("NoTexture.png"));
        	
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static BufferedImage getImage(String imageName)
	{	
		//Game elements
		
		if(imageName.equals("objectCardboardBox"))
			return cBox;
		
		if(imageName.equals("objectWoodenBox"))
			return wBox;
		
		if(imageName.equals("objectBarrel"))
			return barrel;
		
		if(imageName.equals("objectLamp"))
			return lamp;
		
		if(imageName.equals("objectBed"))
			return bed;
		
		if(imageName.equals("objectTable"))
			return table;
		
		if(imageName.equals("objectChair"))
			return chair;
		
		if(imageName.equals("objectWasher"))
			return washer;
		
		if(imageName.equals("objectPlate"))
			return plate;
		
		if(imageName.equals("objectRShirt"))
			return rShirt;
		
		if(imageName.equals("objectGShirt"))
			return gShirt;
		
		if(imageName.equals("objectBShirt"))
			return bShirt;
		
		if(imageName.equals("objectBasket"))
			return basket;
		
		if(imageName.equals("player"))
			return player;
		
		if(imageName.equals("spawn"))
			return spawn;
		
		if(imageName.equals("effectFire"))
			return effectFire;
		
		if(imageName.equals("effectSmoke"))
			return effectSmoke;
		
		//GUI elements
		if(imageName.equals("splashScreen"))
			return splashScreen;
		
		if(imageName.equals("guiBack"))
			return guiBack;
		
		if(imageName.equals("guiPause"))
			return guiPause;
		
		if(imageName.equals("guiCheckedCheckbox"))
			return guiCheckedCheckbox;
		
		if(imageName.equals("guiCheckbox"))
			return guiCheckbox;

		return noTexture;
	}
	
	public static BufferedImage getImage(int imageNum)
	{	
		//Game elements
		
		if(imageNum == 1)
			return cBox;
		
		if(imageNum == 2)
			return wBox;
		
		if(imageNum == 3)
			return barrel;
		
		if(imageNum == 4)
			return lamp;
		
		if(imageNum == 5)
			return bed;
		
		if(imageNum == 6)
			return table;
		
		if(imageNum == 7)
			return chair;
		
		if(imageNum == 8)
			return washer;
		
		if(imageNum == 9)
			return plate;
		
		if(imageNum == 10)
			return player;

		return noTexture;
	}

}
