package org.lucci.up.data.rendering.figure;


import org.lucci.up.data.DataElement;
import org.lucci.up.data.Point;
import org.lucci.up.data.Figure;
import org.lucci.up.system.Dimension;
import org.lucci.up.system.Space;

/**
 * @author Luc Hogie
 */


public class CompleteNetworkFigureRenderer extends FigureRenderer
{
	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Figure pointList = (Figure) object;
		int pointCount = pointList.getPointCount();

		for (int i = 0; i < pointCount; ++i)
		{
			Point point = pointList.getPointAt(i);
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int px = xDimension.convertToGraphicsCoordonateSystem(point.getX());
			int py = yDimension.convertToGraphicsCoordonateSystem(point.getY());

			for (int j = 0; j < pointCount; ++j)
			{
				if ( i != j )
				{
					Point otherPoint = pointList.getPointAt(j);
					int opx = xDimension.convertToGraphicsCoordonateSystem(otherPoint.getX());
					int opy = yDimension.convertToGraphicsCoordonateSystem(otherPoint.getY());
					space.getGraphics().drawLine( opx, opy, px, py );
				}
			}
		}
	}
}
