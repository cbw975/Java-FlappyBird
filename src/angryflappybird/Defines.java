package angryflappybird;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Defines {
    
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final static int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

    // coefficients related to the blob
    final static int BLOB_WIDTH = 50;
    final static int BLOB_HEIGHT = 40;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -125;			// originally -40
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;

    // coefficients related to a pig
    final int PIG_POS_X = 400;              // same as positionX of Pipe
    final int PIG_POS_Y = 20;				// same as positionX of Pipe
    final int PIG_WIDTH = 60;
    final int PIG_HEIGHT = 45;
    final double PIG_DROP_VEL = 0.1;    	// the pig drop velocity
    final double PIG_PROB = 0.25;            // probability that a pig will spawn under pipe
    final double PIG_EGG_POINTS = 5;       // points lost from pig collecting (white) egg

    // coefficients related to an egg
    final int EGG_POS_X = 400;              // same as positionX of Pipe
    final int EGG_WIDTH = 60;               // same as width of Pipe
    final int EGG_HEIGHT = 70;
    final double EGG_PROB = 0.4;            // probability that an egg will spawn on a pipe
    final int EGG_POINTS = 5;               // points gained from collecting a (white) egg
    // final double EGG_GOLD_PROB = 0.25;      // probability that a spawned egg will be gold
    
    // coefficients related to a floor
    final int FLOOR_WIDTH = 600;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;

    // coefficients related to a pipe
    final int PIPE_HEIGHT_DO_SPACING = 325; // 425;  // larger value makes harder (smaller space b/w pipes)
    final int PIPE_MAX_HEIGHT = 300; // 400;
    final int PIPE_MIN_HEIGHT = 25;
    final double PIPE_SCROLL_VEL = -0.4;  // NOTE: set to be multiple of -0.2
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";
	private final String IMAGE_DIR = "../resources/images/";
    private final String AUDIO_DIR = "../resources/sounds/";
    final String[] IMAGE_FILES = {"background","bird0","bird1","bird2","bird3","floor","pig","whiteEgg","topPipe"};
    final String[] AUDIO_FILES = {"collision","death","score","flap","gameStart"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    final HashMap<String, Audio> AUDIO = new HashMap<String, Audio>();
    
    //nodes on the scene graph
    Button startButton;
    Text textWhiteEgg, textPig, textBird, textPipe;
    
    // constructor
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 5) {  // floor
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			} else if (i == 6) {  // pig
                img = new Image(pathImage(IMAGE_FILES[i]), PIG_WIDTH, PIG_HEIGHT, false, false);
            } else if (i == 7 || i == 8) {  // white egg
                img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
            } else if (i == 1 || i == 2 || i == 3 || i == 4){  // blob
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			} else {  // background
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}

        // initialize audio clips
        for(int i=0; i<AUDIO_FILES.length; i++) {
            Audio s = new Audio(AUDIO_DIR+AUDIO_FILES[i]+".mp3");
            AUDIO.put(AUDIO_FILES[i],s);
        }
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");

        // initialize icon descriptions
        textWhiteEgg = new Text("Bonus points. Pigs can steal them!"); // new Text(xCoord, yCoord, "Bonus points")
        textPig = new Text("Avoid pigs");
        textBird = new Text("Control the bird's flight");
        textPipe = new Text("Avoid pipes by flying in between");
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
