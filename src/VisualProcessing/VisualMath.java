package VisualProcessing;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Does the math
 *
 */
public class VisualMath {

	/**
	 * Calculates the distance to a world space object, as well as its relative location and the angle of the object
	 * @param selection The size of the image in screen space
	 * @param size The size of the object in world space
	 * @param focals The focal lengths of the camera
	 * @return An object array with a rectangle with world space cords, an integer with the distance, a double with an approximation of the angle in degrees and the error is something 
	 */
	public static Object[] getDimensions(Rectangle selection, double size[], double[] focals ) {
		
//		double width = size[0] * focals[0] / selection.getWidth();
//		double height= size[1] * focals[1] / selection.getHeight();
//		return new double[]{width, height};
		
		Point topLeft = new Point(selection.x, selection.y);
		Point botRight = new Point(selection.x + selection.width, selection.y + selection.height);
		double ratioX = (botRight.x - topLeft.x)/size[0];
		double ratioY = (botRight.y - topLeft.y)/size[1];
		double error = Math.abs(ratioX - ratioY)/Math.max(ratioX, ratioY);
		double ratio = (ratioX + ratioY)/2d;
		Point projTopLeft = new Point((int)(topLeft.getX() / ratio), (int)(topLeft.getY() / ratio));
		Rectangle out = new Rectangle(projTopLeft.x, projTopLeft.y, (int)(selection.getWidth() / ratio),(int)( selection.getHeight() / ratio));
		Integer i = new Integer((int) ((1d/ratio) * (focals[0] + focals[1])/2f));
		Double d = Math.toDegrees(Math.acos((out.getWidth())/size[0]));
		
		return new Object[] {out, i, d, error};
	}
	


	
}
