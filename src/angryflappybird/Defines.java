package angryflappybird;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final static int SCENE_HEIGHT = 570;
    final static int SCENE_WIDTH = 400;

    // coefficients related to the blob
    final static int BLOB_WIDTH = 50;
    final static int BLOB_HEIGHT = 50;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -150;  // originally -40
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;

    // coefficients related to a pig
    final int PIG_WIDTH = 80;
    final int PIG_HEIGHT = 80;
    final int PIG_DROP_VEL = 100;    		// the pig drop velocity

    // coefficients related to an egg
    final int EGG_WIDTH = 80;
    final int EGG_HEIGHT = 100;
    
    // coefficients related to a floor
    final int FLOOR_WIDTH = 600;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;

    // coefficients related to a pipe
    final int PIPE_HEIGHT_DO_SPACING = 350; // 425;  // larger value makes harder (smaller space b/w pipes)
    final int PIPE_MAX_HEIGHT = 325; // 400;
    final int PIPE_MIN_HEIGHT = 25;
    final double PIPE_SCROLL_VEL = -0.35;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../resources/images/";
    final String[] IMAGE_FILES = {"background","bird0","bird1","bird2","bird3","floor","pig","whiteEgg"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    //nodes on the scene graph
    Button startButton;
    Text textWhiteEgg, textPig;
    
    // constructor
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 5) {  // floor
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			} else if (i == 6) {  // pig
                img = new Image(pathImage(IMAGE_FILES[i]), PIG_WIDTH, PIG_HEIGHT, false, false);
            } else if (i == 7) {  // white egg
                img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
            } else if (i == 1 || i == 2 || i == 3 || i == 4){  // blob
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			} else {  // background
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");

        // initialize icon descriptions
        textWhiteEgg = new Text("Bonus points"); // new Text(xCoord, yCoord, "Bonus points")
        textPig = new Text("Avoid pigs");
	}
	
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}
