package org.lucci.up.data.rendering.point;

import org.lucci.up.data.*;
import org.lucci.up.system.*;


/**
 * @author Luc Hogie
 */

public class OriginPointConnectedPointRenderer extends PointRenderer
{
	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Point point = (Point) object;
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();


		int x = xDimension.convertToGraphicsCoordonateSystem(point.getX());
		int y = yDimension.convertToGraphicsCoordonateSystem(point.getY());
		space.getGraphics().drawLine(x, y, (int) space.getOriginPoint().getX(), (int) space.getOriginPoint().getY());
	}
}
