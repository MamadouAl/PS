package org.lucci.up.data.rendering.figure;

import org.lucci.up.data.DataElement;
import org.lucci.up.data.Point;
import org.lucci.up.data.Figure;
import org.lucci.up.system.Dimension;
import org.lucci.up.system.Space;
/**
 * @author Luc Hogie
 */


public class BarycenterFigureRenderer extends FigureRenderer
{
	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Figure pointList = (Figure) object;
		int pointCount = pointList.getPointCount();

		if ( pointCount > 0 )
		{
			float x = pointList.getPointAt( 0 ).getX();
			float y = pointList.getPointAt( 0 ).getY();
	
			for (int i = 1; i < pointCount; ++i)
			{
				x += pointList.getPointAt( i ).getX();
				y += pointList.getPointAt( i ).getY();
			}

			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int bx = xDimension.convertToGraphicsCoordonateSystem(x / pointCount);
			int by = yDimension.convertToGraphicsCoordonateSystem(y / pointCount);

			for (int i = 0; i < pointCount; ++i)
			{
				Point point = pointList.getPointAt(i);
				int px = xDimension.convertToGraphicsCoordonateSystem(point.getX());
				int py = yDimension.convertToGraphicsCoordonateSystem(point.getY());
				space.getGraphics().drawLine( bx, by, px, py );
			}
		}
	}
}
