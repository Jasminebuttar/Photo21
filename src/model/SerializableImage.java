package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * SerializableImage class is the model to serialize the image
 * SerializableImage implements Serializable interface
 * @author Jasmine Buttar
 * @author Zalak Shingala
 */
public class SerializableImage implements Serializable {

	private int width, height;
	private int[][] pixels;
	
	/**
	 * no-argument SerializableImage constructor with no body
	 */
	public SerializableImage() {}
	
	/**
	 * void method setImage converts the image to 2d array of pixels
	 * @param image		image to convert
	 */
	public void setImage(Image image) {
		width = ((int) image.getWidth());
		height = ((int) image.getHeight());
		pixels = new int[width][height];
		
		PixelReader r = image.getPixelReader();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				pixels[i][j] = r.getArgb(i, j);
	}
	
	/**
	 * return method getImage gets the image from converting the 2d array of pixels to an Image
	 * @return Image object
	 */
	public Image getImage() {
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter w = image.getPixelWriter();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				w.setArgb(i, j, pixels[i][j]);
		
		return image;
	}
	
	/**
	 * return method getWidth to get the width of the image
	 * @return	width of image in int
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * return method get Height to get the height of the image
	 * @return	height of image in int
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * return method getPixels to get the 2D array of pixels
	 * @return	int 2D array of pixels
	 */
	public int[][] getPixels() {
		return pixels;
	}
	
	/**
	 * return method equals checks if two images are equal to each other
	 * @param si	image to be compared with
	 * @return boolean value of whether the two images compared are equal or not
	 */
	public boolean equals(SerializableImage si) {
		if (width != si.getWidth())
			return false;
		if (height != si.getHeight())
			return false;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (pixels[i][j] != si.getPixels()[i][j])
					return false;
		return true;
	}
	
}
