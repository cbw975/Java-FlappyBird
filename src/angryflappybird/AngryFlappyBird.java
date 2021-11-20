package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

//The Application layer
public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
    
    // time related attributes
    private long clickTime, startTime, elapsedTime;   
    private AnimationTimer timer;
    
    // game components
	private Text scoreLabel;  // displayed score
	private Text livesLabel;  // displayed life count
	private int totalScore = 0;  // score
    private Sprite blob;
    private ArrayList<Sprite> floors;
	private ArrayList<Pipe> pipes;
    
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER, OBSTACLE_COLLISION;
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private GraphicsContext gc;		
    
	// the mandatory main method 
    public static void main(String[] args) {
        launch(args);
    }
       
    // the start method sets the Stage layer
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,15));
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
		
		// add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    // the getContent method sets the Scene layer
    private void resetGameControl() {
        
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        gameControl = new VBox();
        gameControl.getChildren().addAll(DEF.startButton,DEF.IMVIEW.get("whiteEgg"),DEF.textWhiteEgg,DEF.IMVIEW.get("pig"),DEF.textPig);
    }
    
    private void mouseClickHandler(MouseEvent e) {
    	if (GAME_OVER) {
            resetGameScene(false);
        }
    	else if (GAME_START){
			// TODO: audio clip of wing flap
            clickTime = System.nanoTime();   
        }
    	GAME_START = true;
        CLICKED = true;
    }
    
    private void resetGameScene(boolean firstEntry) {
    	
    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        floors = new ArrayList<>();
		pipes = new ArrayList<>();
        
    	if(firstEntry) {
    		// create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView background = DEF.IMVIEW.get("background");

			// initialize labels - score, lives, etc.
			scoreLabel = new Text(10, 20, "0");
			scoreLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 50));
			scoreLabel.setStroke(Color.BLACK);
			scoreLabel.setFill(Color.WHITE);
			livesLabel = new Text(10, 50, "3 lives left");
			livesLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 25));

			// TODO: initialize sounds (?)
            
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, scoreLabel, livesLabel);
    	}
    	
    	// initialize floor
    	for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    		
    		int posX = i * DEF.FLOOR_WIDTH;
    		int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
    		floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		floor.render(gc);
    		
    		floors.add(floor);
    	}
        
        // initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("blob0"));
        blob.render(gc);

		// initialize pipes
		setPipes();

        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }

	// Pipe stuff
	private int getRandomPipeHeight() {
        return (int) (Math.random() * (DEF.PIPE_MAX_HEIGHT - DEF.PIPE_MIN_HEIGHT)) + DEF.PIPE_MIN_HEIGHT;
    }
	
	private void setPipes() {
		int height = getRandomPipeHeight();  // bottom pipe height
		Pipe botPipe = new Pipe(true, height);
		Pipe topPipe = new Pipe(false, DEF.PIPE_HEIGHT_DO_SPACING - height);

        botPipe.getPipe().setVelocity(DEF.PIPE_SCROLL_VEL, 0);
        topPipe.getPipe().setVelocity(DEF.PIPE_SCROLL_VEL, 0);

        botPipe.getPipe().render(gc);
        topPipe.getPipe().render(gc);

        pipes.add(botPipe);
        pipes.add(topPipe);
	}
	
	private void checkPipeScroll() {
        if (pipes.size() > 0) {
            Sprite p = pipes.get(pipes.size() - 1).getPipe();
            if (p.getPositionX() == DEF.SCENE_WIDTH / 2 - 80) {
                setPipes();
            } else if (p.getPositionX() <= -p.getWidth()) {
                pipes.remove(0);
                pipes.remove(0);
            }
        }
    }

	private void updateScoreText(int score) {
        scoreLabel.setText(Integer.toString(score));
    }

	private void updateScore() {
		if (!OBSTACLE_COLLISION) {  // if passed pipes w/o collision
            for (Pipe pipe : pipes) {
                if (pipe.getPipe().getPositionX() == blob.getPositionX()) {
                    updateScoreText(++totalScore);  // increment score
					// TODO: play audio!
                    break;
                }
            }
        }
	}


    //timer stuff
    class MyTimer extends AnimationTimer {
    	
    	int counter = 0;
    	
    	 @Override
    	 public void handle(long now) {   		 
    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	     
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

			 // step1: update floor
			 moveFloor();

    	     if (GAME_START) {
				 // Step1: update and render pipes
				 movePipes();
				 checkPipeScroll();
				 updateScore();  // increment score if when pass pipes
				 
				 // TODO: ADD GAME LOGIC

    	    	 // step2: update blob
    	    	 moveBlob();
    	    	 checkCollision();
    	     }
    	 }
    	 
    	 // step1: update floor
    	 private void moveFloor() {
    		
    		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
    				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
    	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    	        	floors.get(i).setPositionXY(nextX, nextY);
    			}
    			floors.get(i).render(gc);
    			floors.get(i).update(DEF.SCENE_SHIFT_TIME);
    		}
    	 }
    	 
    	 // step2: update blob
    	 private void moveBlob() {
    		 
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("blob"+String.valueOf(imageIndex)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			}
			// blob drops after a period of time without button click
			else {
			    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
			    CLICKED = false;
			}

			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
    	 }

		 // update pipes
		 private void movePipes() {
			for (Pipe pipe : pipes) {
				Sprite p = pipe.getPipe();
				p.update(5);
				p.render(gc);
			}
		}
    	 
    	 public void checkCollision() {
    		 
    		// // check collision with floor
			// for (Sprite floor: floors) {
			// 	GAME_OVER = GAME_OVER || blob.intersectsSprite(floor);
			// }

			// check collision with floor
			if(!OBSTACLE_COLLISION){
				for(Sprite floor: floors) {
					if(blob.intersectsSprite(floor)){
						OBSTACLE_COLLISION = true;
						// TODO: Collision animation + Bird falls backwards
						GAME_OVER = true;
						break;
					}
				}
			}

			// check collision with pipes
			if(!OBSTACLE_COLLISION) {  // if not already in collision
				for(Pipe p: pipes) {
					if(blob.intersectsSprite(p.getPipe())) {
						OBSTACLE_COLLISION = true;
						// TODO: Collision animation + Bird falls backwards
						GAME_OVER = true;
						break;
					}
				}
			}

			// TODO: check collision with pigs (and eggs?)

			// end the game when blob hit stuff
			if (GAME_OVER) {
				showHitEffect();
				for (Sprite floor: floors) {
					floor.setVelocity(0, 0);
				}
				timer.stop();
			}
			
    	 }
    	 
	     private void showHitEffect() {
	        ParallelTransition parallelTransition = new ParallelTransition();
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
	        fadeTransition.setToValue(0);
	        fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
	        fadeTransition.setAutoReverse(true);
	        parallelTransition.getChildren().add(fadeTransition);
	        parallelTransition.play();
	     }
    	 
    } // End of MyTimer class

} // End of AngryFlappyBird Class

