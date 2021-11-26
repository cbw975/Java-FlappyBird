package angryflappybird;

import javafx.scene.media.AudioClip;

/**
 * Manages the properties and methods of each audio clip in the game.
 */
public class Audio {
    
    private AudioClip soundEffect;  // AudioClip object with sound effect

    /**
     * Creates an audio clip, playable in the game.

     * @param filePath (local) location of the audio clip
     */
    public Audio(String filePath) {
        soundEffect = new AudioClip(getClass().getResource(filePath).toExternalForm());
    }

    /**
     * Plays the audio/sound of the clip.
     */
    public void playAudio() {
        soundEffect.play();
    }
}