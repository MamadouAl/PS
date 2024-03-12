package org.lucci.up.data.rendering.figure;
import org.lucci.up.data.*;
import org.lucci.up.system.*;
import java.awt.Polygon;
/**
 * @author Luc Hogie
 */
public class PolygonFigureRenderer extends FigureRenderer
{
	private boolean filled = true;

	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Figure pointList = (Figure) object;
		int pointCount = pointList.getPointCount();
		Polygon polygon = new Polygon();
	
		for (int i = 1; i < pointCount; ++i)
		{
			Point point = pointList.getPointAt(i);
			Dimension xDimension = space.getXDimension();
			Dimension yDimension = space.getYDimension();

			int px = xDimension.convertToGraphicsCoordonateSystem(point.getX());
			int py = yDimension.convertToGraphicsCoordonateSystem(point.getY());
			polygon.addPoint(px, py);
		}

		space.getGraphics().drawPolygon(polygon);
		
		if ( filled )
		{
			space.getGraphics().fillPolygon(polygon);
		}
	}

	/**
	 * Returns the filled.
	 * @return boolean
	 */
	public boolean isFilled()
	{
		return filled;
	}

	/**
	 * Sets the filled.
	 * @param filled The filled to set
	 */
	public void setFilled(boolean filled)
	{
		this.filled = filled;
	}
}
