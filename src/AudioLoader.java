/*
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioPlayer;
*/
//Alec Ibarra
public class AudioLoader {
	/*
	static AudioInputStream step1 = null;
	static URL footstep;
	static int currentStep = 0;
	static long now = System.currentTimeMillis();
	static long lastTime = System.currentTimeMillis();
	
	public AudioLoader()
	{
		footstep = getClass().getClassLoader().getResource("Footsteps.wav");
		
		try {
			
			step1 = AudioSystem.getAudioInputStream(footstep);
			
		} catch (UnsupportedAudioFileException | IOException e) {e.printStackTrace();}
	}
	
	public static void playFootsteps()
	{
		//TODO its a mess...
		
		if(System.currentTimeMillis()-lastTime > 1000)
		{
			System.out.println("Excuting...");
			lastTime = System.currentTimeMillis();
			AudioPlayer.player.start(step1);	
		}
			
	}
	
*/	
}
