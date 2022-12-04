/* Drew Schuster */

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/* This class controls all sound effects*/
public class GameSounds {

    Clip nomNom;
    Clip newGame;
    Clip death;
    /* Keeps track of whether the eating sound is playing*/
    boolean stopped;

    /* Initialize audio files */
    public GameSounds() {
        stopped = true;

        try {
            // Pacman eating sound
            AudioInputStream audioIn = getAudioInputStream("sounds/nomnom.wav");
            nomNom = AudioSystem.getClip();
            nomNom.open(audioIn);

            // newGame        
            audioIn = getAudioInputStream( "sounds/newGame.wav");
            newGame = AudioSystem.getClip();
            newGame.open(audioIn);

            // death        
            audioIn = getAudioInputStream( "sounds/death.wav");
            death = AudioSystem.getClip();
            death.open(audioIn);

        } catch (Exception ignored) {
        }
    }

    private AudioInputStream getAudioInputStream(String name) throws UnsupportedAudioFileException, IOException {
        URL url;
        AudioInputStream audioIn;
        url = this.getClass().getClassLoader().getResource(name);
        audioIn = AudioSystem.getAudioInputStream(url);
        return audioIn;
    }

    /* Play pacman eating sound */
    public void nomNom() {
        /* If it's already playing, don't start it playing again!*/
        if (!stopped)
            return;

        stopped = false;
        nomNom.stop();
        nomNom.setFramePosition(0);
        nomNom.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /* Stop pacman eating sound */
    public void nomNomStop() {
        stopped = true;
        nomNom.stop();
        nomNom.setFramePosition(0);
    }

    /* Play new game sound */
    public void newGame() {
        newGame.stop();
        newGame.setFramePosition(0);
        newGame.start();
    }

    /* Play pacman death sound */
    public void death() {
        death.stop();
        death.setFramePosition(0);
        death.start();
    }
}
