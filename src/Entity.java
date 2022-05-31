import java.awt.Dimension;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

//Alec Ibarra
public class Entity {
	
	private float XPos = 75;
	private float YPos = 100;
	private float XSpawn = 100;
	private float YSpawn = 100;
	private float XVelocity = 0;
	private float YVelocity = 0;
	private float Friction = .2f;
	private float speed = 1.5f;
	private float maxSpeed = 2.5f;
	private float rotation = 0;
	private int points = 0;
	private int ID = 0;
	private int ownerID = 0;
	private int teamID = 0;
	private int health = 100;
	private int maxHealth = 100;
	private int damage = 100;
	private int money = 0;
	private int currentCooldown = 100;
	private int maxCooldown = 100;
	private String owner = "unknown";
	private String type = "unknown";
	private String lore = "";
	private boolean dynamic = false;
	private boolean solid = true;
	private boolean neutral = false;
	private boolean friendly = false;
	private boolean hostile = false;
	private int iconNum = 0;
	private String[] icons;
	private BufferedImage icon = ImageLoader.getImage("noTexture");
	private int iconWidth = icon.getWidth();
	private int iconHeight = icon.getHeight();
	private Rectangle2D hitBox = new Rectangle2D.Double();
	private Dimension lastGoodPos = new Dimension(0,0);
	
	public Entity()
	{
		setIcon(ImageLoader.getImage("noTexture"));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos)
	{
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage("noTexture"));
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName)
	{
		setXPos(XPos);
		setYPos(YPos);
		setIcon(ImageLoader.getImage(iconName));
		setIconWidth(icon.getWidth());
		setIconHeight(icon.getHeight());
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName, int ID)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setIcon(ImageLoader.getImage(iconName));
		setIconWidth(icon.getWidth());
		setIconHeight(icon.getHeight());
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String iconName, int rotation, int ID, String lore)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setRotation(rotation);
		setIcon(ImageLoader.getImage(iconName));
		setIconWidth(icon.getWidth());
		setIconHeight(icon.getHeight());
		setLore(lore);
		updateHitBox();
	}
	
	public Entity(float XPos, float YPos, String[] icons, int rotation, boolean isSolid, int ID, String lore)
	{
		setXPos(XPos);
		setYPos(YPos);
		setID(ID);
		setRotation(rotation);
		setIcons(icons);
		setSolid(isSolid);
		setIconWidth(icon.getWidth());
		setIconHeight(icon.getHeight());
		setLore(lore);
		updateHitBox();
	}
	
	public void animate()
	{
		setCurrentCooldown(getCurrentCooldown()+1);
		
		if (getCurrentCooldown() > getMaxCooldown())
		{
			setCurrentCooldown(0);
			String[] temparray = getIcons();
			
			iconNum++;
			if(iconNum > getIconsLength()-1)
			{
				iconNum = 0;
			}
			setIcon(ImageLoader.getImage(temparray[iconNum]));
			updateHitBox();
		}
	}
	
	public void move(double timeElapsed)
	{	
		//System.out.println(timeElapsed);
		if(Logic.ControlsSpinMouse)
		{
			setRotation(getAngleToTarget(Logic.mousex-13,Logic.mousey-11)+90);
		}
		
		speed = 1.5f;
		//speed = (float)timeElapsed/Logic.displayFPS;

		if (rotation > 360)
		{
			rotation -= 360;
		}
		else if (rotation < 0)
		{
			rotation += 360;
		}
		
		if (!collisionDetect())//TODO or mouse is too close to player
		{
			lastGoodPos.width = (int) XPos;
			lastGoodPos.height = (int) YPos;
			
			if (Logic.spinReset)
			{
				if(Logic.ControlsSpinKey1 || Logic.ControlsSpinKey4)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Logic.spinReset = false;
				}
			}
			
			if (Logic.up)
			{
				if(Logic.ControlsMove1)
				{
					YPos -= speed * timeElapsed;
				}
				else if(Logic.ControlsMove2)
				{
					XPos += speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
					YPos += speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
				}
				else if(Logic.ControlsMove3)
				{
					XPos += speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
					YPos += speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
				}
				else if(Logic.ControlsMove4)
				{
					YPos -= speed * timeElapsed;
					setRotation(0);
				}
			}
			
			if (Logic.down)
			{
				if(Logic.ControlsMove1)
				{
					YPos += speed * timeElapsed;
				}
				else if(Logic.ControlsMove2)
				{
					XPos -= speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
			    	YPos -= speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
				}
				else if(Logic.ControlsMove3)
				{
					XPos -= speed * timeElapsed * Math.cos(Math.toRadians(rotation-90));
					YPos -= speed * timeElapsed * Math.sin(Math.toRadians(rotation-90));
				}
				else if(Logic.ControlsMove4)
				{
					YPos += speed * timeElapsed;
					setRotation(180);
				}
			}
			
			if (Logic.right)
			{
				if(Logic.ControlsMove1)
				{
					XPos += speed * timeElapsed;
				}
				else if(Logic.ControlsMove2)
				{
					XPos += speed * timeElapsed * Math.cos(Math.toRadians(rotation-0));
					YPos += speed * timeElapsed * Math.sin(Math.toRadians(rotation-0));
				}
				else if(Logic.ControlsSpinKey3)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Logic.right = false;
				}
				else if(Logic.ControlsMove4)
				{
					XPos += speed * timeElapsed;
					setRotation(90);
				}
			}
			
			if (Logic.left)
			{
				if(Logic.ControlsMove1)
				{
					XPos -= speed * timeElapsed;
				}
				else if(Logic.ControlsMove2)
				{
					XPos += speed * timeElapsed * Math.cos(Math.toRadians(rotation-180));
					YPos += speed * timeElapsed * Math.sin(Math.toRadians(rotation-180));
				}
				else if(Logic.ControlsSpinKey3)
				{
					if(getRotation() == 0)
					{
						setRotation(270);
					}
					else if(getRotation() == 270)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(90);
					}
					else
					{
						setRotation(0);
					}
					Logic.left = false;
				}
				else if(Logic.ControlsMove4)
				{
					XPos -= speed * timeElapsed;
					setRotation(270);
				}
			}
			
			if (Logic.spinLeft)
			{
				if(Logic.ControlsSpinKey1 || Logic.ControlsSpinKey4)
				{
					setRotation(getRotation()-4);
				}
				else if(Logic.ControlsSpinKey2)
				{
					if(getRotation() == 0)
					{
						setRotation(270);
					}
					else if(getRotation() == 270)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(90);
					}
					else
					{
						setRotation(0);
					}
					Logic.spinLeft = false;
				}
			}
			
			if (Logic.spinRight)
			{
				if(Logic.ControlsSpinKey1 || Logic.ControlsSpinKey4)
				{
					setRotation(getRotation()+4);
				}
				else if(Logic.ControlsSpinKey2)
				{
					if(getRotation() == 0)
					{
						setRotation(90);
					}
					else if(getRotation() == 90)
					{
						setRotation(180);
					}
					else if(getRotation() == 180)
					{
						setRotation(270);
					}
					else
					{
						setRotation(0);
					}
					Logic.spinRight = false;
				}
			}
			
			if(Logic.ControlsMove4)
			{
				if(Logic.up && Logic.right)
				{
					XPos = lastGoodPos.width;
					YPos = lastGoodPos.height;
					
					XPos += speed * (float)timeElapsed;
					YPos -= speed * (float)timeElapsed;
					setRotation(45);
				}
				else if(Logic.up && Logic.left)
				{
					XPos = lastGoodPos.width;
					YPos = lastGoodPos.height;
					
					XPos -= speed * (float)timeElapsed;
					YPos -= speed * (float)timeElapsed;
					setRotation(315);
				}
				else if(Logic.down && Logic.right)
				{
					XPos = lastGoodPos.width;
					YPos = lastGoodPos.height;
					
					XPos += speed * (float)timeElapsed;
					YPos += speed * (float)timeElapsed;
					setRotation(135);
				}
				else if(Logic.down && Logic.left)
				{
					XPos = lastGoodPos.width;
					YPos = lastGoodPos.height;
					
					XPos -= speed * (float)timeElapsed;
					YPos +=  speed * (float)timeElapsed;
					setRotation(225);
				}
			}
			
			if (collisionDetect())
			{
				XPos = lastGoodPos.width;
				YPos = lastGoodPos.height;
			}
			
		}
		else
		{
			XPos = lastGoodPos.width;
			YPos = lastGoodPos.height;
		}
	}
	
	public void moveAI(double timeElapsed)
	{	
		if(dynamic && neutral)
		{
			
		}
		else if(dynamic && friendly)
		{
			
		}
		else if(dynamic && hostile)
		{			
			//moveTo(Logic.player.getXPos(),Logic.player.getYPos(),2);
		}
	}
	
	public boolean moveTo(float targetX, float targetY, float speed)
	{
		float easingAmount = 0.05f;
		float xDistance = targetX - XPos;
		float yDistance = targetY - YPos;
		
		XPos += xDistance/speed * easingAmount;
		YPos += yDistance/speed * easingAmount;
		
		//If the target has been reached then retun true else false
		if(XPos == targetX && YPos == targetY)
			return true;
		
		return false;
	}
	
	public boolean collisionDetect()
	{	
		for(int k = 0; k < EntityKeeper.everything.size(); k++)
		{
			for(int j = 0; j < EntityKeeper.everything.get(k).size(); j++)
			{
				if(getHitBox().intersects(((Entity)EntityKeeper.everything.get(k).get(j)).getHitBox())
						&& (((Entity)EntityKeeper.everything.get(k).get(j)).getID() != getID()) 
						&& (((Entity)EntityKeeper.everything.get(k).get(j)).isSolid()))
				{
					return true;//if current Entity collides with anything (excluding itself) then return true
				}

				Area hitbox = new Area(getHitBox());
				hitbox.intersect(EntityKeeper.wallZone);
				if(!hitbox.isEmpty())     
				{
					return true;//if current Entity collides wall then return true
				}
			}	
		}
		return false;
	}
	
	public float getAngleToTarget(double targetx, double targety) {
	    float angle = (float) Math.toDegrees(Math.atan2(targety - getYPos(), targetx - getXPos()));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
	public void updateHitBox(){
		this.iconWidth = icon.getWidth();
		this.iconHeight = icon.getHeight();
		this.hitBox = new Rectangle2D.Double(XPos,YPos,iconWidth-1,iconHeight-1);
	}

	public float getXPos() {
		return XPos;
	}

	public void setXPos(float xPos) {
		XPos = xPos;
	}

	public float getYPos() {
		return YPos;
	}

	public void setYPos(float yPos) {
		YPos = yPos;
	}

	public float getXSpawn() {
		return XSpawn;
	}

	public void setXSpawn(float xSpawn) {
		XSpawn = xSpawn;
	}

	public float getYSpawn() {
		return YSpawn;
	}

	public void setYSpawn(float ySpawn) {
		YSpawn = ySpawn;
	}

	public float getXVelocity() {
		return XVelocity;
	}

	public void setXVelocity(float xVelocity) {
		XVelocity = xVelocity;
	}

	public float getYVelocity() {
		return YVelocity;
	}

	public void setYVelocity(float yVelocity) {
		YVelocity = yVelocity;
	}

	public float getFriction() {
		return Friction;
	}

	public void setFriction(float friction) {
		Friction = friction;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCooldown) {
		this.currentCooldown = currentCooldown;
	}

	public int getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isNeutral() {
		return neutral;
	}

	public void setNeutral(boolean neutral) {
		this.neutral = neutral;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public boolean isHostile() {
		return hostile;
	}

	public void setHostile(boolean hostile) {
		this.hostile = hostile;
	}

	public int getIconNum() {
		return iconNum;
	}

	public void setIconNum(int iconNum) {
		this.iconNum = iconNum;
	}

	public int getIconsLength() {
		return icons.length;
	}
	
	public String[] getIcons() {
		return icons;
	}

	public void setIcons(String[] icons) {
		this.icons = icons;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public int getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}

	public Rectangle2D getHitBox() {
		updateHitBox();
		return hitBox;
	}

	public void setHitBox(Rectangle2D hitBox) {
		this.hitBox = hitBox;
	}
	
	public Dimension getLastGoodPos() {
		return lastGoodPos;
	}

	public void setLastGoodPos(Dimension lastGoodPos) {
		this.lastGoodPos = lastGoodPos;
	}
	
	

}
