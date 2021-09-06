import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;


/* This class controls all sound effects*/
public class GameSounds{
    
    Clip nomNom;
    Clip newGame;
    Clip death;
    Clip eatGhost;
    Clip powerPellet;
    /* Keeps track of whether or not the eating sound is playing*/
    boolean stopped, power_pellet;
       

/* Initialize audio files */ 
    public GameSounds(){
        stopped=true; 
        power_pellet=true; 
        URL url;
        AudioInputStream audioIn;
        
        try{
            // Pacman eating sound
            url = this.getClass().getClassLoader().getResource("sounds/nomnom.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            nomNom = AudioSystem.getClip();
            nomNom.open(audioIn);
            
            // newGame        
            url = this.getClass().getClassLoader().getResource("sounds/newGame.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            newGame = AudioSystem.getClip();
            newGame.open(audioIn);
            
            // death        
            url = this.getClass().getClassLoader().getResource("sounds/death.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            death = AudioSystem.getClip();
            death.open(audioIn);

            // eatGhost       
            url = this.getClass().getClassLoader().getResource("sounds/eatGhost.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            eatGhost = AudioSystem.getClip();
            eatGhost.open(audioIn);
            
            // powerPellet        
            url = this.getClass().getClassLoader().getResource("sounds/powerPellet.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            powerPellet = AudioSystem.getClip();
            powerPellet.open(audioIn);

        }catch(Exception e){}
    }
    
    /* Play pacman eating sound */
    public void nomNom(){
        /* If it's already playing, don't start it playing again!*/
        if (!stopped)
          return;

        stopped=false;
        nomNom.stop();
        nomNom.setFramePosition(0);
        nomNom.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /* Stop pacman eating sound */
    public void nomNomStop(){
        stopped=true;
        nomNom.stop();
        nomNom.setFramePosition(0);
    }
    
    /* Play new game sound */
    public void newGame(){
        newGame.stop();
        newGame.setFramePosition(0);
        newGame.start();
    }
    
    /* Play pacman death sound */
    public void death(){
        death.stop();
        death.setFramePosition(0);
        death.start();
    }

    /* Play eat ghost sound */
    public void eatGhost(){
        eatGhost.stop();
        eatGhost.setFramePosition(0);
        eatGhost.start();
    }

    /* Play power pellet sound */
    public void powerPellet(){
        /* If it's already going, don't start it again!*/
        if (!power_pellet)
          return;

        power_pellet=false;
        powerPellet.stop();
        powerPellet.setFramePosition(0);
        powerPellet.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /* Stop power pellet sound */
    public void powerPelletStop(){
        power_pellet=true;
        powerPellet.stop();
        powerPellet.setFramePosition(0);
    }
      
}
