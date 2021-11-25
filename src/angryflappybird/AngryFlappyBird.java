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
//import java.util.Random;

//The Application layer
public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
    
    // time related attributes
    private long clickTime, startTime, flapTime, elapsedTime;   
    private AnimationTimer timer;
    
    // game components
	private Text scoreLabel;  // displayed score
	private Text livesLabel;  // displayed life count
	private int totalScore = 0;  // score
	private int lives = 3;  // lives remaining
    private Sprite blob;
    private ArrayList<Sprite> pigs;
    private ArrayList<Sprite> eggs;
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
        gameControl.getChildren().addAll(DEF.startButton,DEF.IMVIEW.get("whiteEgg"),DEF.textWhiteEgg,
										DEF.IMVIEW.get("pig"),DEF.textPig,DEF.IMVIEW.get("bird0"),DEF.textBird,
										DEF.IMVIEW.get("topPipe"), DEF.textPipe);
    }
    
    private void mouseClickHandler(MouseEvent e) {
		if(!OBSTACLE_COLLISION) {  // if not (already) colliding with obstacle
			CLICKED = true;  // register click
			DEF.AUDIO.get("flap").playAudio();
			if(GAME_START) {  // Game in progress
				clickTime = System.nanoTime(); 
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			} else {  // Start game if not yet started
				GAME_START = true;
			}
		} else {  // collision occured --> lose a life (restart playing if not gameover)
			decLives();
		}
		
    	if (GAME_OVER) {  // restart game loop
    		resetGameScene(true);  // reset the gameScene
        }
    }
    
    private void resetGameScene(boolean firstEntry) {
		DEF.AUDIO.get("gameStart").playAudio();
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
			totalScore = 0;
			updateScoreText(0);
			lives = 3;
			livesLabel = new Text(10, 50, "3 lives left");
			livesLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 25));

            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, scoreLabel, livesLabel);
    	}
		
    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
		OBSTACLE_COLLISION = false;
        floors = new ArrayList<>();
		pipes = new ArrayList<>();
		eggs = new ArrayList<>();
		pigs = new ArrayList<>();

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
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y, DEF.IMAGE.get("bird0"));
        blob.render(gc);

		// initialize pipes
		setPipes();

        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }

	// Egg stuff
	private void setEgg(int height) {
		if(Math.random() < DEF.EGG_PROB){  // Check if egg spawns
			Sprite e = new Sprite(DEF.EGG_POS_X, height - DEF.EGG_HEIGHT, DEF.IMAGE.get("whiteEgg"));
			e.setVelocity(DEF.PIPE_SCROLL_VEL, 0);
        	e.render(gc);
			eggs.add(e);
		}
	}

	// Pig stuff
	private void setPig(int height) {
		if(Math.random() < DEF.PIG_PROB){  // Check if pig spawns
			Sprite p = new Sprite(DEF.PIG_POS_X, DEF.PIG_POS_Y, DEF.IMAGE.get("pig"));
			p.setVelocity(DEF.PIPE_SCROLL_VEL, DEF.PIG_DROP_VEL);
        	p.render(gc);
			pigs.add(p);
		}
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
		setEgg(DEF.SCENE_HEIGHT - height);
        topPipe.getPipe().render(gc);
		setPig(DEF.PIPE_HEIGHT_DO_SPACING - height);

        pipes.add(botPipe);
        pipes.add(topPipe);
	}
	
	private void checkPipeScroll() {
		if (pipes.size() > 0) {
            Sprite p = pipes.get(pipes.size() - 1).getPipe();
            if (p.getPositionX() == DEF.SCENE_WIDTH / 2 - 80) {  // when current pipes past 1/2 of screen
                setPipes();  // prepare next set of pipes (moving onto screen)
            } else if (p.getPositionX() <= -p.getWidth()) {  // when pipes move off screen
                pipes.remove(0);  // remove (old) pipes
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
					DEF.AUDIO.get("score").playAudio();
                    break;
                }
            }
        }
	}

	// decrement life count, game over if reached 0 lives
	private void decLives() {
		if(lives <= 1){
			livesLabel.setText("0 lives left");
			GAME_OVER = true;
		} else {
			String lifeText = Integer.toString(--lives) + " lives left";
			livesLabel.setText(lifeText);
			timer.stop();
			resetGameScene(false);  // begin playing again
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
				checkEggCollection();  // check if egg collected
				updateScore();  // increment score if pass pipes

    	    	// step2: update blob
    	    	moveBlob();
    	    	checkCollision();  // handle any collision events
				
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
				blob.setImage(DEF.IMAGE.get("bird"+String.valueOf(imageIndex)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			} else {  // blob drops after a period of time without button click
			    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
			    CLICKED = false;
			}

			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
			blob.render(gc);
    	 }

		 // update pipes and any egg or pig on them
		 private void movePipes() {
			for (Pipe pipe : pipes) {
				Sprite p = pipe.getPipe();
				p.update(5);
				p.render(gc);
			}
			for (Sprite e : eggs) {
				e.update(5);
				e.render(gc);
			}
			for (Sprite p : pigs) {
				p.update(5);
				p.render(gc);
			}
		}

		private void checkEggCollection() {
			// check if pig collects egg for points loss
			for(Sprite pig : pigs) {
				for(Sprite e : eggs) {
					if(pig.intersectsSprite(e)) {
						totalScore -= DEF.PIG_EGG_POINTS;
						updateScoreText(totalScore);
						eggs.remove(e);
						break;
					}
				}
			}
			// check if bird collects egg for bonus points
			for(Sprite e : eggs) {
				if(blob.intersectsSprite(e)) {
					totalScore += DEF.EGG_POINTS;
					updateScoreText(totalScore);
					eggs.remove(e);
					break;
				}
			}
		}

		public void checkCollision() {
			// check collision with pipes
			if(!OBSTACLE_COLLISION) {  // if not already in collision
				for(Pipe p : pipes) {
					if(blob.intersectsSprite(p.getPipe())) {
						OBSTACLE_COLLISION = true;
						showHitEffect();  // collision animation
						endScroll();
						DEF.AUDIO.get("collision").playAudio();
						birdCollapseEffect();  // Bird falling after collision
						decLives();  // lose life
						break;
					}
				}
			}
			// check collision with floor
			for(Sprite floor : floors) {
				if(blob.intersectsSprite(floor)){
					OBSTACLE_COLLISION = true;
					DEF.AUDIO.get("collision").playAudio();
					GAME_OVER = true;
					break;
				}
			}
			// check collision with pigs
			for(Sprite pig : pigs) {
				if(blob.intersectsSprite(pig)) {
					OBSTACLE_COLLISION = true;
					DEF.AUDIO.get("collision").playAudio();
					GAME_OVER = true;
					break;
				}
			}

			// end the game when blob hits floor or pig
			if (GAME_OVER) {
				DEF.AUDIO.get("death").playAudio();
				showHitEffect();
				lives = 0;
				decLives();  // update display
				updateScoreText(0);
				for (Sprite floor: floors) {
					floor.setVelocity(0, 0);
				}
				timer.stop();
			}
    	}

		 private void endScroll() {
			for (Pipe p : pipes) {
				p.getPipe().setVelocity(0, 0);
			}
			for (Sprite f : floors) {
				f.setVelocity(0, 0);
			}
			for (Sprite e : eggs) {
				e.setVelocity(0, 0);
			}
			for (Sprite p : pigs) {
				p.setVelocity(0, 0);
			}
		}
		
		private void birdCollapseEffect() {
			// TODO: fix this
			// Animation of bird bouncing back and dropping immediately (upon collision)
			flapTime += 0.18;
			if (flapTime > 0.5) {
				blob.addVelocity(-200, DEF.BLOB_DROP_VEL);
				blob.render(gc);
				blob.update(elapsedTime);
				flapTime = 0;
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

