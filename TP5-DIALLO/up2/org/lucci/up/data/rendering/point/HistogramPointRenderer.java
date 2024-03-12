package org.lucci.up.data.rendering.point;

import org.lucci.up.data.*;
import org.lucci.up.system.*;

import java.util.*;
import java.awt.Color;


/**
 * @author Luc Hogie
 */

public class HistogramPointRenderer extends PointRenderer
{
	private static List colorList = Arrays.asList( new Color[] { Color.blue, Color.red, Color.green, Color.orange, Color.magenta, Color.pink, Color.yellow } );

	private int barWidth = 1;
	private int colorIndex = 0;
	
	
	/**
	 * @see org.lucci.plt.data.DataRenderer#draw(DataObject, Space)
	 */
	public void drawImpl(DataElement object, Space space)
	{
		Point point = (Point) object;
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		int x1 = xDimension.convertToGraphicsCoordonateSystem(point.getX() - 0.5f);
		int y1 = yDimension.convertToGraphicsCoordonateSystem(point.getY());

		int x2 = xDimension.convertToGraphicsCoordonateSystem(point.getX() + 0.5f);
		int y2 = yDimension.convertToGraphicsCoordonateSystem(0);
		
		space.getGraphics().fillRect(x1, y1, x2 - x1, y2 - y1 );
		space.getGraphics().setColor(java.awt.Color.black);
		space.getGraphics().drawRect(x1, y1, x2 - x1, y2 - y1 );
	}
	
	private Color getNextColor()
	{
		if ( colorIndex == colorList.size() )
		{
			colorIndex = 0;
		}

		return (Color) colorList.get( colorIndex++ );
	}
}
