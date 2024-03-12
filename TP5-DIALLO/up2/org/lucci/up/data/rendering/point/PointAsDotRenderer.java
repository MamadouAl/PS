package org.lucci.up.data.rendering.point;

import org.lucci.up.data.*;
import org.lucci.up.system.*;

import java.awt.Color;

/**
 * @author Luc Hogie
 */
public class PointAsDotRenderer extends PointRenderer
{
	private int size = 5;
	private Color fillColor = Color.white;
	private boolean useGradient = false;
	
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

		if ( size == 1 )
		{
			space.getGraphics().drawLine(x, y, x, y);
		}
		else
		{
			Color color = getColor();

			if ( useGradient )
			{
				for (int i = 1; i <= size; ++i)
				{
					int hs = i / 2;
					Color c = new Color( color.getRed(), color.getGreen(), color.getBlue(), 255-i * 255 / size );
					space.getGraphics().setColor(c);
					space.getGraphics().drawOval(x - hs, y - hs, i, i);
				}
			}
			else
			{
				if ( fillColor != null )
				{
					space.getGraphics().setColor(fillColor);
					space.getGraphics().fillOval(x - size / 2, y - size / 2, size, size);
				}

				space.getGraphics().setColor(color);
				space.getGraphics().drawOval(x - size / 2, y - size / 2, size, size);
			}
		}
	}

	/**
	 * Returns the size.
	 * @return int
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Sets the size.
	 * @param size The size to set
	 */
	public void setSize(int size)
	{
		if ( size < 1 )
			throw new IllegalArgumentException( "size must be > 0" );

		this.size = size;
	}


	/**
	 * Returns the fillColor.
	 * @return Color
	 */
	public Color getFillColor()
	{
		return fillColor;
	}

	/**
	 * Returns the useGradient.
	 * @return boolean
	 */
	public boolean isUseGradient()
	{
		return useGradient;
	}


	/**
	 * Sets the fillColor.
	 * @param fillColor The fillColor to set
	 */
	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
	}

	/**
	 * Sets the useGradient.
	 * @param useGradient The useGradient to set
	 */
	public void setUseGradient(boolean useGradient)
	{
		this.useGradient = useGradient;
	}
}
