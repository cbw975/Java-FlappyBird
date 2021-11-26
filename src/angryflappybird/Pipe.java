package angryflappybird;

/**
 * Manages the properties and methods of each pipe in the game.
 */
public class Pipe {
    
    private Sprite pipe;
    private double positionX = 400;
    private double positionY;
    private double width = 60;

    /**
     * Creates a pipe.

     * @param isBottom specifies a bottom pipe vs top pipe in a set
     * @param height height of bottom pipe (gives corresponding pipes' heights)
     */
    public Pipe(boolean isBottom,int height) {
        this.pipe = new Sprite();
        String imgPath;
        if(isBottom){
            imgPath = getClass().getResource("../resources/images/bottomPipe.png").toExternalForm();
            this.positionY = Defines.SCENE_HEIGHT - height;
        }else{
            imgPath = getClass().getResource("../resources/images/topPipe.png").toExternalForm();
            this.positionY = 0;
        }
        this.pipe.setImageSize(imgPath, width, height);
        this.pipe.setPositionXY(positionX, positionY);
    }

    /**
     * Returns the Sprite corresponding to a pipe

     * @return Sprite representing pipe
     */
    public Sprite getPipe() {
        return pipe;
    }
    
}
