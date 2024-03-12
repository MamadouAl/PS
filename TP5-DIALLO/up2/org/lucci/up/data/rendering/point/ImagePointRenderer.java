package org.lucci.up.data.rendering.point;

import org.lucci.up.data.*;
import org.lucci.up.system.*;

import java.awt.Image;
import java.awt.image.*;


/**
 * @author Luc Hogie
 */
public class ImagePointRenderer extends PointRenderer
{
	private Image image;

	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		ImageObserver imageObserver = space.getImageObserver();

		if ( image != null && imageObserver != null )
		{
			org.lucci.up.data.Point point = (org.lucci.up.data.Point) object;
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int px = xDimension.convertToGraphicsCoordonateSystem(point.getX());
			int py = yDimension.convertToGraphicsCoordonateSystem(point.getY());
			int x = px - image.getWidth(imageObserver) / 2 + getXShift();
			int y = py - image.getHeight(imageObserver) / 2 + getYShift();
			space.getGraphics().drawImage( image, x, y, imageObserver );
		}
	}
}
