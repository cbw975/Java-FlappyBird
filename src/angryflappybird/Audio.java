package angryflappybird;

import javafx.scene.media.AudioClip;

public class Audio {
    private AudioClip soundEffect;

    public Audio(String filePath) {
        soundEffect = new AudioClip(getClass().getResource(filePath).toExternalForm());
    }

    public void playAudio() {
        soundEffect.play();
    }
}