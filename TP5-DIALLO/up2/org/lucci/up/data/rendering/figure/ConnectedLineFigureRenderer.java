package org.lucci.up.data.rendering.figure;


import org.lucci.up.data.DataElement;
import org.lucci.up.data.Point;
import org.lucci.up.data.Figure;
import org.lucci.up.system.Dimension;
import org.lucci.up.system.Space;
/**
 * @author Luc Hogie
 */


public class ConnectedLineFigureRenderer extends FigureRenderer
{
	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Figure pointList = (Figure) object;
		int pointCount = pointList.getPointCount();
	
		for (int i = 1; i < pointCount; ++i)
		{
			Point point = pointList.getPointAt(i);
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int px = xDimension.convertToGraphicsCoordonateSystem(point.getX());
			int py = yDimension.convertToGraphicsCoordonateSystem(point.getY());

			Point previousPoint = pointList.getPointAt( i - 1 );
			int ppx = xDimension.convertToGraphicsCoordonateSystem(previousPoint.getX());
			int ppy = yDimension.convertToGraphicsCoordonateSystem(previousPoint.getY());

			space.getGraphics().drawLine( ppx, ppy, px, py );
		}
	}
}
