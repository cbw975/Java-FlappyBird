package angryflappybird;

public class Pipe {
    
    private Sprite pipe;
    private double positionX = 400;  //TODO: adjust if necessary
    private double positionY;
    private double width = 60;

    public Pipe(boolean isBottom,int height){
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

    public Sprite getPipe(){
        return pipe;
    }
    
}
