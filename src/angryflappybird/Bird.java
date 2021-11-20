/** @author Chloe Wohlgemuth */

package angryflappybird;

import java.util.ArrayList;

public class Bird {
    
    private Sprite bird;  // sprite associated with bird
    private ArrayList<Sprite> frames = new ArrayList<>();  // Sprite with each flight animation frame
    private int numFrames = 4;
    private int indFrame = 0;  // current frame of flight animation
    private double positionX, positionY;
    private String[] BIRD_IMAGES = {"../resources/images/bird0","../resources/images/bird1","../resources/images/bird0","../resources/images/bird2"};

    public Bird(){
        setBirdFrames();
        bird = frames.get(0);  // first bird frame
    }

    /**
     * Sets the bird's flight animation frames
     */
    private void setBirdFrames(){
        for(int i=0; i<numFrames; i++){
            Sprite b = new Sprite();
            b.setImageSize(BIRD_IMAGES[i],Defines.BLOB_WIDTH,Defines.BLOB_HEIGHT);
            b.setPositionXY(positionX, positionY);
            frames.add(b);
        }
    }

    public Sprite getBird(){
        return bird;
    }

    /**
     * Animate bird's flight
     * @return next frame in bird's flight animation
     */
    public Sprite updateFrame(){
        if(indFrame >= frames.size()-1){
            indFrame = 0;  // restart flight frame cycle
        }
        return frames.get(indFrame++);
    }

}
