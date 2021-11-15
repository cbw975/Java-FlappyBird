package angryflappybird;

public class Pipe {
    
    private Sprite pipe;
    private double positionX = 400;  //TODO: adjust if necessary
    private double positionY;
    private double width = 60;
    private double height;

    public Pipe(boolean isBottom,int height){
        this.pipe = new Sprite();
        String imgPath;
        this.height = height;
        if(isBottom){
            imgPath = "../resources/images/bottomPipe.png";
            this.positionY = Defines.SCENE_WIDTH - height;
        }else{
            imgPath = "../resources/images/topPipe.png";
            this.positionY = 0;
        }
        this.pipe.setImageSize(imgPath, width, height);
        this.pipe.setPositionXY(positionX, positionY);
    }

    public Sprite getPipe(){
        return pipe;
    }
    
}
