package org.lucci.up.data;

import java.util.*;

import org.lucci.up.data.event.*;

/**
 * <p>
 * The point is the basic data entity that is drawn on
 * the graphics. A point is 2-dimensional.
 * </p>
 * 
 * <p>
 * Points are always contained in a figure. It is not possible to
 * display a point that would't be contained in a figure. If you
 * have a single point to display, you have a create a figure
 * that will contain this point and display this figure.
 * </p>
 *
 * @author Luc Hogie
 *
 */
public class Point extends DataElement
{
	private float x, y;

	public Point( int x, int y )
	{
		this( (float) x, (float) y );
	}

	public Point( double x, double y )
	{
		this( (float) x, (float) y );
	}

	public Point( float x, float y )
	{
		this.x = x;
		this.y = y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
	
	public Object clone()
	{
		Point point = new Point(getX(), getY());
		int rendererCount = getRendererCount();
		
		for (int i = 0; i < rendererCount; ++i)
		{
			point.addRenderer(getRendererAt(i));
		}

		return point;
	}

	public void setX( float x )
	{
		if ( x != this.x )
		{
			float oldX = this.x;
			this.x = x;

			if ( getListeners() != null )
			{
				fireXChanged( this, oldX, x );
			}
		}
	}

	public void setY( float y )
	{
		if ( y != this.y )
		{
			float oldY = this.y;
			this.y = y;

			if ( getListeners() != null )
			{
				fireYChanged( this, oldY, y );
			}
		}
	}

	private void fireXChanged( Point point, float oldX, float newX )
	{
		Collection listeners = getListeners();

		if ( listeners != null )
		{
			Iterator i = listeners.iterator();
			
			while ( i.hasNext() )
			{
				((PointListener) i.next()).xChanged(point, oldX, newX);
			}
		}
	}

	private void fireYChanged( Point point, float oldY, float newY )
	{
		Collection listeners = getListeners();

		if ( listeners != null )
		{
			Iterator i = listeners.iterator();
			
			while ( i.hasNext() )
			{
				((PointListener) i.next()).yChanged(point, oldY, newY);
			}
		}
	}

    public void translate(float x, float y)
    {
    	setX( getX() + x );
    	setY( getY() + y );
    }
    
    public String toString()
    {
    	return "(" + x + ", " + y + ")";
    }
}

