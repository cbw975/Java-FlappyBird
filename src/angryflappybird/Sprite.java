package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Manages the properties and methods of each character of the game.
 */
public class Sprite {  
	
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    /**
     * Creates invisible sprite with no velocity, located on bottom left corner of the scene.
     */
    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Creates a sprite that is displayed as the image, located at the given position.

     * @param pX X-coord of (corner of) Sprite location
     * @param pY Y-coord of (corner of) Sprite location
     * @param image to display for the Sprite
     */
    public Sprite(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Sets the image, height and width of the sprite.

     * @param image to set for the Sprite
     */
    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    /**
     * Sets the image, with specified height and width, of the sprite.

     * @param path for image to set for sprite
     * @param width to set for image
     * @param height to set for image
     */
    public void setImageSize(String path, double width, double height){
        setImage(new Image(path,width,height,false,false));
    }

    /**
     * Sets the coordinates of the sprite on the canvas.

     * @param positionX X-coordinate for Sprite
     * @param positionY Y-coordinate for Sprite
     */
    public void setPositionXY(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Returns the X-coordinate of sprite position.
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Returns the Y-coordinate of sprite position.
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * Sets the speed of the sprite.

     * @param velocityX X-component speed
     * @param velocityY Y-component speed
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Updates the speed of the sprite, basically accelerating or decelerating the sprite.

     * @param x value to be added to x-component speed
     * @param y value to be added to y-component speed
     */
    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }

    /**
     * Returns the horizontal velocity of the sprite.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the vertical velocity of the sprite.
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * returns the width of the Sprite.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Displays the image in the given position on the canvas.

     * @param gc canvas on which to display image
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    /**
     * Creates a new rectangular boundary corresponding to the sprite's position and dimensions.

     * @return boundary
     */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    /**
     * Checks if this sprite intersects with the @param s given sprite.
     */
    public boolean intersectsSprite(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    /**
     * Updates the position of a moving sprite with new positions.

     * @param time to elapse/update position over
     */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
}
