package org.lucci.up.system;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import org.lucci.up.data.*;

/**
 * <p>
 * A space is the abstract thing that defines the dimension X and Y.
 * This models the mathematical concept of space.
 * </p>
 * 
 * <p>
 * A space is made of:
 * <ul>
 * 	<li>2 dimensions (X and Y),
 * 	<li>an origin point with coordinates (0, 0),
 * 	<li>a scale that calculates the position on a Java Graphics2D
 * 	of a logic coordinate.
 * </ul>
 * </p>
 * 
 * <p>
 * The space features also a legend that will be drawn on top of
 * the graphics representation.
 * </p>
 * 
 * @author Luc Hogie.
 */


public class Space extends GraphicalElement
{
	private Legend legend = new Legend( "Ultimate Plotter" );
    private Dimension xDimension = null;
    private Dimension yDimension = null;
    private Point2D originPoint = new Point2D.Float( 0, 0 );
    private Graphics2D graphics = null;
    private ImageObserver imageObserver;
	private Color backgroundColor = Color.white;

    public Space()
    {
		init();
    }

	private void init()
	{
		Dimension xDimension = new Dimension();
		setXDimension( xDimension );
		xDimension.getLowerBoundAxis().getGraduation().setDedicatedPixelCount( 15 );
		xDimension.getUpperBoundAxis().getGraduation().setDedicatedPixelCount( 15 );
		xDimension.getLegend().setText( "X dimension" );

		Dimension yDimension = new Dimension();
		setYDimension( yDimension );
		yDimension.getLowerBoundAxis().getGraduation().setDedicatedPixelCount( 25 );
		yDimension.getUpperBoundAxis().getGraduation().setDedicatedPixelCount( 25 );
		yDimension.getLegend().setText( "Y dimension" );
		legend.setFont( new Font( null, Font.PLAIN, 20 ) );
	}

	/**
	 * Sets the X dimension of the space.
	 * @param xDimension
	 */
    public void setXDimension( Dimension xDimension )
    {
		if ( xDimension == null )
		    throw new IllegalArgumentException( "X dimension cannot be set to null");
	
        this.xDimension = xDimension;
		xDimension.setParent( this );
    }

	/**
	 * Gets the X dimension of the space.
	 * @return Dimension
	 */
    public Dimension getXDimension()
    {
        return xDimension;
    }

	/**
	 * Sets the Y dimension of the space.
	 * @param yDimension
	 */
    public void setYDimension( Dimension yDimension )
    {
		if ( yDimension == null )
			throw new IllegalArgumentException( "Y dimension cannot be set to null");
	
        this.yDimension = yDimension;
		yDimension.setParent( this );
    }

	/**
	 * Gets the X dimension of the space.
	 * @return Dimension
	 */
    public Dimension getYDimension()
    {
        return yDimension;
    }

	/**
	 * Gets the origin point (the point with (0, 0) coordonates)
	 * of the space. Obviously, the real position (on the Graphics2D)
	 * of this point is not (0, 0).
	 * @return Point2D
	 */
    public Point2D getOriginPoint()
    {
        return originPoint;
    }

	/**
	 * Gets the Java2D Graphics2D on which the space is to be drawn.
	 * @return Graphics2D
	 */
    public Graphics2D getGraphics()
    {
		return graphics;
    }


	/**
	 * Sets the range of the space. This is a facility method:
	 * this can be done directly manipulating the dimensions of
	 * the space.
	 * 
	 * Warning! This is a convenience method. The
	 * Dimension, Graduation and AxisLine classes
	 * allow you to have a better control.
	 * 
	 * @param xmin
	 * @param xmax
	 * @param xstep
	 * @param ymin
	 * @param ymax
	 * @param ystep
	 */
    public void setRange( float xmin, float xmax, float xstep,
			    float ymin, float ymax, float ystep )
    {
	    xDimension.setBounds(xmin, xmax);
	    xDimension.getLowerBoundAxis().getGraduation().setStep( xstep );
	    xDimension.getUpperBoundAxis().getGraduation().setStep( xstep );
	    xDimension.getOriginAxis().getGraduation().setStep( xstep );

	    yDimension.setBounds( ymin, ymax );
	    yDimension.getLowerBoundAxis().getGraduation().setStep( ystep );
	    yDimension.getUpperBoundAxis().getGraduation().setStep( ystep );
	    yDimension.getOriginAxis().getGraduation().setStep( ystep );
    }

	/**
	 * Sets the visibility of the grid.
	 * 
	 * Warning! This is a convenience method. The
	 * grid class feature many methods that allow
	 * you to have a better control.
	 * 
	 * @param gridTracing
	 */
    public void setGridVisible( boolean gridTracing )
    {
	    getXDimension().getGrid().setVisible(gridTracing);
	    getYDimension().getGrid().setVisible(gridTracing);
    }

    protected void update( Graphics2D g, Figure figure )
    {
		this.graphics = g;

		if ( figure != null )
		{
		    xDimension.update( figure );
		    yDimension.update( figure );
		}

	    double xRange = xDimension.getMax() - xDimension.getMin();
	    double yRange = yDimension.getMax() - yDimension.getMin();
	    originPoint.setLocation( (int) (g.getClipBounds().getWidth() * -xDimension.getMin() / xRange),
 		    (int) (g.getClipBounds().getHeight() * yDimension.getMax() / yRange) );
    }



    protected void draw( Graphics2D graphics, Graphics2D subGraphics )
    {
		if ( isVisible() )
		{
			// the first thing to do is to initialize the graduations and draw the grids
		    xDimension.getLowerBoundAxis().getGraduation().update( subGraphics );
		    xDimension.getOriginAxis().getGraduation().update( subGraphics );
		    xDimension.getUpperBoundAxis().getGraduation().update( subGraphics );
		    yDimension.getLowerBoundAxis().getGraduation().update( subGraphics );
		    yDimension.getOriginAxis().getGraduation().update( subGraphics );
		    yDimension.getUpperBoundAxis().getGraduation().update( subGraphics );

		    xDimension.getGrid().draw(subGraphics);
		    yDimension.getGrid().draw(subGraphics);

			// then the rest can be drawn
		    xDimension.draw( graphics, subGraphics );
		    yDimension.draw( graphics, subGraphics );
		}
    }

	/**
	 * Returns the imageObserver.
	 * @return ImageObserver
	 */
	public ImageObserver getImageObserver()
	{
		return imageObserver;
	}

	/**
	 * Sets the imageObserver.
	 * @param imageObserver The imageObserver to set
	 */
	public void setImageObserver(ImageObserver imageObserver)
	{
		this.imageObserver = imageObserver;
	}

	public Legend getLegend()
	{
		return legend;
	}
	
	public void setLegend( Legend newLegend )
	{
		if ( newLegend == null )
			throw new IllegalArgumentException( "the legend cannot be set to null" );

		this.legend = newLegend;
		legend.setParent(this);
	}	
	/**
	 * Returns the backgroundColor.
	 * @return Color
	 */
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * Sets the backgroundColor.
	 * @param backgroundColor The backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
		if ( backgroundColor == null )
			throw new IllegalArgumentException( "backgroundColor cannot be set to null" );

		this.backgroundColor = backgroundColor;
	}
	
	
	public static final int MATHS = 1;
	public static final int PHYSICS = 2;
	public static final int GNUPLOT = 3;
	
	public void setMode( int mode )
	{
		init();
		
		if ( mode == MATHS )
		{
			getLegend().setText("Maths mode");

			getXDimension().getLowerBoundAxis().setVisible(false);
			getXDimension().getUpperBoundAxis().setVisible(false);
			getXDimension().getLegend().setText("X");

			getYDimension().getLowerBoundAxis().setVisible(false);
			getYDimension().getUpperBoundAxis().setVisible(false);
			getYDimension().getLegend().setText("Y");
		}
		else if ( mode == PHYSICS )
		{
			getLegend().setText("Physics mode");
			setBackgroundColor(Color.black);
			setColor(Color.white);

			getXDimension().getOriginAxis().setVisible(false);
			getXDimension().getLegend().setText("X");

			getYDimension().getOriginAxis().setVisible(false);
			getYDimension().getLegend().setText("Y");
			
			getXDimension().getLegend().setFont( new Font(null, Font.PLAIN, 12));
			getYDimension().getLegend().setFont( new Font(null, Font.PLAIN, 12));
			getXDimension().getOriginAxis().setVisible(false);
			getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
			getXDimension().getUpperBoundAxis().getLine().getArrow().setVisible(false);
			getYDimension().getOriginAxis().setVisible(false);
			getYDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
			getYDimension().getUpperBoundAxis().getLine().getArrow().setVisible(false);
		}
		else if ( mode == GNUPLOT )
		{
			getLegend().setText("GNUPlot mode");
		}
	}
}