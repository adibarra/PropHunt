import java.awt.Rectangle;

//Alec Ibarra
public class Door extends Entity implements Runnable {
	
	private Thread t;
	private int startX = 0;
	private int startY = 0;
	private int doorCooldown = 250;// 1/4 second
	private int doorSpeed = 10;// 1/100th second
	private int doorIncrement = 4;
	private int activationProgress = 100;
	private int currentWidth = 25;
	private int currentHeight = 25;
	private int width = 25;
	private int height = 25;
	private boolean finished = false;
	private boolean release = true;
	private String direction = "left";
	
	public Door(){}
	
	public Door(int XPos, int YPos, String direction)
	{
		setXPos(XPos);
		setYPos(YPos);
		setDirection(direction);
	}
	
	public void activate()
	{
		if(t == null)
		{
			t = new Thread(this);
			t.start();
		}
		else if(finished)
		{
			finished = false;
			t = null;
		}
	}
	
	public void run() 
	{
		try {
			if(direction.equals("left"))
			{
				activationProgress = 100;
				while(activationProgress > 0)//open
				{
					activationProgress -= doorIncrement;
					currentWidth = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
		
				while(!release)
				{
					Thread.sleep(doorCooldown);
				}
			
				while(activationProgress < 100)//close
				{
					activationProgress += doorIncrement;
					currentWidth = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
			}	
			else if(direction.equals("right"))
			{
				activationProgress = 0;
				while(activationProgress < 100)//open
				{
					activationProgress += doorIncrement;
					currentWidth = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
		
				while(!release)
				{
					Thread.sleep(doorCooldown);
				}
			
				while(activationProgress > 0)//close
				{
					activationProgress -= doorIncrement;
					currentWidth = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
			}	
			else if(direction.equals("up"))
			{
				activationProgress = 100;
				while(activationProgress > 0)//open
				{
					activationProgress -= doorIncrement;
					currentHeight = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
		
				while(!release)
				{
					Thread.sleep(doorCooldown);
				}
			
				while(activationProgress < 100)//close
				{
					activationProgress += doorIncrement;
					currentHeight = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
			}
			else if(direction.equals("down"))
			{
				activationProgress = 0;
				while(activationProgress < 100)//open
				{
					activationProgress += doorIncrement;
					currentHeight = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}
		
				while(!release)
				{
					Thread.sleep(doorCooldown);
				}
			
				while(activationProgress > 0)//close
				{
					activationProgress -= doorIncrement;
					currentHeight = activationProgress/4;
					updateHitBox(direction);
					Thread.sleep(doorSpeed);
				}	
			}
			finished = true;
		
		} catch (InterruptedException e) {}
	}

	public void prepare()
	{
		if(direction.equals("left"))
		{
			setStartX((int)getXPos()+getCurrentHeight());
			setStartY((int)getYPos()+(getCurrentHeight()/2));
			activationProgress = 100;
			currentWidth = 25;
			currentHeight = 25;
			setWidth(25);
			setHeight(25);
		}
		else if(direction.equals("right"))
		{
			setStartX((int)getXPos());
			setStartY((int)getYPos()+(getCurrentHeight()/2));
			activationProgress = 0;
			currentWidth = 0;
			currentHeight = 25;
			setWidth(25);
			setHeight(25);
		}
		else if(direction.equals("up"))
		{
			setStartX((int)getXPos()+(getCurrentWidth()/2));
			setStartY((int)getYPos()+getCurrentHeight());
			activationProgress = 0;
			currentWidth = 25;
			currentHeight = 25;
			setWidth(25);
			setHeight(25);
		}
		else if(direction.equals("down"))
		{
			setStartX((int)getXPos()+(getCurrentWidth()/2));
			setStartY((int)getYPos());
			activationProgress = 0;
			currentWidth = 25;
			currentHeight = 0;
			setWidth(25);
			setHeight(25);
		}
	}
	
	public void updateHitBox(String direction)
	{
		if(direction.equals("left"))
		{
			setHitBox(new Rectangle((int)getXPos(),(int)getYPos(),getCurrentWidth()-1,getHeight()-1));
		}
		else if(direction.equals("right"))
		{
			setHitBox(new Rectangle((int)getXPos()+getCurrentWidth(),(int)getYPos(),getWidth()-getCurrentWidth()-1,getHeight()-1));
		}
		else if(direction.equals("up"))
		{
			setHitBox(new Rectangle((int)getXPos(),(int)getYPos(),getWidth()-1,getCurrentHeight()-1));
		}
		else if(direction.equals("down"))
		{
			setHitBox(new Rectangle((int)getXPos(),(int)getYPos()-getCurrentHeight(),getWidth()-1,getHeight()-getCurrentHeight()-1));
		}
	}
	
	public Thread getThread() {
		return t;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isRelease() {
		return release;
	}

	public void setRelease(boolean release) {
		this.release = release;
	}

	public int getDoorSpeed() {
		return doorSpeed;
	}

	public void setDoorSpeed(int doorSpeed) {
		this.doorSpeed = doorSpeed;
	}
	
	public int getActivationProgress() {
		return activationProgress;
	}

	public void setActivationProgress(int activationProgress) {
		this.activationProgress = activationProgress;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public void setCurrentWidth(int currentWidth) {
		this.currentWidth = currentWidth;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getDoorCooldown() {
		return doorCooldown;
	}

	public void setDoorCooldown(int doorCooldown) {
		this.doorCooldown = doorCooldown;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public void setCurrentHeight(int currentHeight) {
		this.currentHeight = currentHeight;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDoorIncrement() {
		return doorIncrement;
	}

	public void setDoorIncrement(int doorIncrement) {
		this.doorIncrement = doorIncrement;
	}
}
