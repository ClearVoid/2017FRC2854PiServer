package VisualProcessing;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Does the math
 *
 */
public class VisualMath {

	/**
	 * Calculates the distance to a world space object
	 * @param selection The size of the image in screen space
	 * @param size The size of the object in world space
	 * @param focals The focal lengths of the camera
	 * @return a double array with {distance to width, distance to height}
	 */
	public static Rectangle getDimensions(Rectangle selection, double width, double[] focals ) {
		
//		double width = size[0] * focals[0] / selection.getWidth();
//		double height= size[1] * focals[1] / selection.getHeight();
//		return new double[]{width, height};
		
		Point topLeft = new Point(selection.x, selection.y);
		Point botRight = new Point(selection.x + selection.width, selection.y + selection.height);
		double ratio = width/(botRight.x - topLeft.x);
		Point projTopLeft = new Point((int)(topLeft.getX() * ratio), (int)(topLeft.getY() * ratio));
		Rectangle out = new Rectangle(projTopLeft.x, projTopLeft.y, (int)(selection.getWidth() * ratio),(int)( selection.getHeight() * ratio));
				
		return out;
	}
	


	
}
