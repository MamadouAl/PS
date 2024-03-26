package org.lucci.up.system;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.logging.Handler;

import org.lucci.up.data.*;


/**
 * <p>
 * This call models the mathematic concept of dimension:
 * a 2-dimensional space is constituted of 2 dimensions.
 * </p>
 * 
 * <p>
 * A dimension is itselft bounded because it may be graphically
 * represented on a different interval that its parent space.
 * </p>
 * 
 * @author Luc Hogie
 */
public class Dimension extends BoundedGraphicalElement
{
	public static final int X = 1;
	public static final int Y = 2;

	private Legend legend;
	private Axis lowerBoundAxis;
	private Axis upperBoundAxis;
	private Axis originAxis;
	private boolean autoRange = true;
	private float border = 0.1f;
	protected Grid grid;

	public Dimension()
	{
	    setLowerBoundAxis( new Axis() );
	    setUpperBoundAxis( new Axis() );
	    setOriginAxis( new Axis() );
	    setLegend( new Legend() );
	    setGrid(new Grid());
	}


	/**
	 * Gets the grid of the graduation.
	 * @return Grid
	 */
	public Grid getGrid()
	{
		return grid;
	}

	/**
	 * Sets the grid of the graduation.
	 * @param newGrid
	 */
	public void setGrid( Grid newGrid )
	{
		if ( newGrid == null )
			throw new IllegalArgumentException( "the grid cannot be set to null" );

		this.grid = newGrid;
		this.grid.setParent( this );
	}


	/**
	 * Gets the lower bounds axis of the dimension
	 * @return Axis
	 */
	public Axis getLowerBoundAxis()
	{
		return lowerBoundAxis;
	}

	/**
	 * Sets the lower bounds axis of the dimension.
	 * @param axis
	 */
	public void setLowerBoundAxis( Axis axis )
	{
		if ( axis == null )
			throw new IllegalArgumentException( "axis cannot be set to null" );

		this.lowerBoundAxis = axis;
		axis.setParent( this );
	}

	/**
	 * Gets the upper bounds axis of the dimension
	 * @return Axis
	 */
	public Axis getUpperBoundAxis()
	{
		return upperBoundAxis;
	}

	/**
	 * Sets the upper bounds axis of the dimension.
	 * @param axis
	 */
	public void setUpperBoundAxis( Axis axis )
	{
		if ( axis == null )
			throw new IllegalArgumentException( "axis cannot be set to null" );

		this.upperBoundAxis = axis;
		axis.setParent( this );
	}

	/**
	 * Gets the origin axis of the dimension
	 * @return Axis
	 */
	public Axis getOriginAxis()
	{
		return originAxis;
	}

	/**
	 * Sets the origin axis of the dimension.
	 * @param axis
	 */
	public void setOriginAxis( Axis axis )
	{
		if ( axis == null )
			throw new IllegalArgumentException( "axis cannot be set to null" );

		this.originAxis = axis;
		axis.setParent( this );
	}

	/**
	 * Returns the border.
	 * @return float
	 */
	public float getBorder()
	{
		return border;
	}

	/**
	 * Sets the border. 
	 * @param border The border to set
	 */
	public void setBorder(float border)
	{
		this.border = border;
	}


	/**
	 * Gets the legend of the dimension. This explains what is
	 * the dimension used for.
	 * @return Legend
	 */
	public Legend getLegend()
	{
		return legend;
	}

	/**
	 * Sets the legend of the dimension. This explains what is
	 * the dimension used for.
	 * @param newLegend
	 */
	public void setLegend( Legend newLegend )
	{
		if ( newLegend == null )
			throw new IllegalArgumentException( "axis legend cannot be set to null" );

		this.legend = newLegend;
		legend.setParent(this);
	}

	/**
	 * Gets the orientation of the dimension. The dimension
	 * is <code>X</code> or <code>Y</code>.
	 * @return int
	 */
	public int getOrientation()
	{
		Space scale = (Space) getParent();

		if ( this == scale.getXDimension() )
		{
			return X;
		}
		else if ( this == scale.getYDimension() )
		{
			return Y;
		}
		else
		{
			throw new IllegalStateException( "parent dimension does not belong to a scale" );
		}
	}

	protected void draw( Graphics2D graphics, Graphics2D subGraphics )
	{
		if ( isVisible() )
		{
			getLowerBoundAxis().draw(graphics, subGraphics);
			getOriginAxis().draw(graphics, subGraphics);
			getUpperBoundAxis().draw(graphics, subGraphics);
		}
	}
	
    protected void update( Figure figure )
    {
	    if ( isAutoBounded() )
	    {
	    	Figure.Extremi extremi = figure.getExtremums();
	    	
			if ( extremi == null )
			{
				setBounds( -10, 10 );
			}
			else
			{
				float min, max;
				
				if ( getOrientation() == X )
				{
					min	= extremi.minX;
					max = extremi.maxX;
				}
				else
				{
					min	= extremi.minY;
					max = extremi.maxY;
				}
	
				if ( min == max )
				{
					setBounds( min - 1, max + 1 );
				}
				else
				{
					float range = max - min;
					setBounds(min - range * border, max + range * border );
				}
			}
			
			setAutoBounded(true);
	    }
    }
    
    
	/**
	 * Returns the number of pixel used by the dimension on the given graphics.
	 * @param graphics
	 * @return int
	 */
    protected int getGraphicsSize(Graphics graphics)
    {
		if ( getOrientation() == X )
		{
			return (int) graphics.getClipBounds().getWidth();
		}
		else
		{
			return (int) graphics.getClipBounds().getHeight();
		}
    }
    
    protected Dimension getSiblingDimension()
    {
		Space space = (Space) getParent();

		if ( getOrientation() == X )
		{
			return space.getYDimension();
		}
		else
		{
			return space.getXDimension();
		}
    }

    public String toString()
    {
    	String orientation = getOrientation() == X ? "horizontal" : "vertical";
    	return orientation + " dimension";
    }
    
	public int convertToGraphicsCoordonateSystem( float value )
	{
	    Space space = (Space) getParent();
		int graphicsSize = getGraphicsSize(space.getGraphics());

		if ( value == getMin() )
		{
			return getOrientation() == X ? 0 : graphicsSize - 1;
		}

		if ( value == getMax() )
		{
			return getOrientation() == X ? graphicsSize - 1 : 0;
		}

	    double range = getMax() - getMin();
	    double factor = graphicsSize / range;
	    int valueOnGraphics = (int) (getOrientation() == X
	    	? factor * value + space.getOriginPoint().getX()
	    	: -factor * value + space.getOriginPoint().getY());

	    return valueOnGraphics;
    }
}
