package org.lucci.up.data.rendering;

import org.lucci.up.data.DataElement;
import org.lucci.up.system.*;

import java.awt.*;

/**
 * @author Luc Hogie
 */
public abstract class DataElementRenderer
{
//	private Color color = new Color( 255, 0, 0, 128 );
	private Color color = new Color( 0, 0, 255, 128 );
//	private Color color = new Color( 0, 0, 255, 128 );
	private Stroke stroke = new BasicStroke();


	public final void draw( DataElement object, Space space )
	{
		Graphics2D graphics = space.getGraphics();
		graphics.setColor(getColor());
		graphics.setStroke(getStroke());
		drawImpl(object, space);
	}


	protected abstract void drawImpl( DataElement object, Space space );

	/**
	 * Returns the color.
	 * @return Color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets the color.
	 * @param color The color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}


	public Stroke getStroke()
	{
	    return stroke;
	}
	
	public void setStroke(Stroke stroke)
	{
	    this.stroke = stroke;
	}
}
