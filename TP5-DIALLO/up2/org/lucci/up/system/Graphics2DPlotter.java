package org.lucci.up.system;

import java.awt.*;
import org.lucci.up.data.*;
import org.lucci.up.system.*;

/**
 * The Graphics2DPlotter is the component that paint the user's Graphics2D.
 * The paint process is not implemented in a Swing component because the
 * user may want to get a graphical reprensentation of its 2D data without
 * showing it on a user interface widget: for example the user may want to
 * export the graphical representation to an image file or event
 * directly print it.
 * 
 * @author Luc Hogie
 */
public class Graphics2DPlotter
{
	private Figure figure;
	private Space space = new Space();

    /**
     * Returns the space that is used to layout the data representation.
     * @return Space
     */
	public Space getSpace()
	{
	    return space;
	}

	/**
     * Sets she space that is used to layout the data representation.
	 * @param space The space to set
	 */
	public void setSpace(Space space)
	{
		if ( space == null )
			throw new IllegalArgumentException( "Space cannot be set to null" );

		this.space = space;
	}


	/**
     * Returns the figure that will be drawn.
	 */
	public Figure getFigure()
	{
		return figure;
	}

	/**
     * Sets the figure that will be drawn.
	 * @param figure
     */
	public void setFigure( Figure figure )
	{
		this.figure = figure;
	}

	
	/**
	 * Paints the space and the figure on the given graphics.
	 */
	public void paint( Graphics2D g )
	{
		g.setColor( space.getBackgroundColor() );
		g.fillRect( 0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight() );
		drawEverything( (Graphics2D) g );
	}

	private void drawEverything( Graphics2D g )
	{
		Legend legend = space.getLegend();

		if ( !legend.isVisible() )
		{
			drawAxisLegendsAndAxisAndPoints( g );
		}
		else
		{
			int legendGraphicsX = 0;
			int legendGraphicsY = 0;
			int legendGraphicsW = (int) g.getClipBounds().getWidth();
			int legendGraphicsH = legend.getFont().getSize() * 2;
			Graphics2D legendGraphics = (Graphics2D) g.create( legendGraphicsX, legendGraphicsY, legendGraphicsW, legendGraphicsH );
			legend.draw( legendGraphics );

			int curveAndAxisLegendsGraphicsX = 0;
			int curveAndAxisLegendsGraphicsY = legendGraphicsH;
			int curveAndAxisLegendsGraphicsW = legendGraphicsW;
			int curveAndAxisLegendsGraphicsH = (int) (g.getClipBounds().getHeight() - legendGraphicsH);
			Graphics2D curveAndAxisLegendsGraphics = (Graphics2D) g.create( curveAndAxisLegendsGraphicsX, curveAndAxisLegendsGraphicsY, curveAndAxisLegendsGraphicsW, curveAndAxisLegendsGraphicsH );
			drawAxisLegendsAndAxisAndPoints( curveAndAxisLegendsGraphics );
		}
	}

	private void drawAxisLegendsAndAxisAndPoints( Graphics2D g )
	{
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();
		Legend xLegend = xDimension.getLegend();
		Legend yLegend = yDimension.getLegend();

		if ( xLegend.isVisible() && yLegend.isVisible() )
		{
			int curveGraphicsX = yLegend.getFont().getSize() * 2;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight() - xLegend.getFont().getSize() * 2;
			Graphics2D curveGraphics = (Graphics2D) g.create( curveGraphicsX, curveGraphicsY, curveGraphicsW, curveGraphicsH );
			drawAxisAndPoints( curveGraphics );

			int xLegendGraphicsX = curveGraphicsX;
			int xLegendGraphicsY = curveGraphicsH;
			int xLegendGraphicsW = curveGraphicsW;
			int xLegendGraphicsH = (int) (g.getClipBounds().getHeight() - curveGraphicsH);
			Graphics2D xLegendGraphics = (Graphics2D) g.create( xLegendGraphicsX, xLegendGraphicsY, xLegendGraphicsW, xLegendGraphicsH );
			xLegend.draw( xLegendGraphics );

			int yLegendGraphicsX = 0;
			int yLegendGraphicsY = 0;
			int yLegendGraphicsW = curveGraphicsX;
			int yLegendGraphicsH = curveGraphicsH;
			Graphics2D yLegendGraphics = (Graphics2D) g.create( yLegendGraphicsX, yLegendGraphicsY, yLegendGraphicsW, yLegendGraphicsH );
			yLegend.draw( yLegendGraphics );
		}
		else if ( xLegend.isVisible() )
		{
			int curveGraphicsX = 0;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight() - xLegend.getFont().getSize() * 2;
			Graphics2D curveGraphics = (Graphics2D) g.create( curveGraphicsX, curveGraphicsY, curveGraphicsW, curveGraphicsH );
			drawAxisAndPoints( curveGraphics );

			int xLegendGraphicsX = curveGraphicsX;
			int xLegendGraphicsY = curveGraphicsH;
			int xLegendGraphicsW = curveGraphicsW;
			int xLegendGraphicsH = (int) (g.getClipBounds().getHeight() - curveGraphicsH);
			Graphics2D xLegendGraphics = (Graphics2D) g.create( xLegendGraphicsX, xLegendGraphicsY, xLegendGraphicsW, xLegendGraphicsH );
			xLegend.draw( xLegendGraphics );
		}
		else if ( yLegend.isVisible() )
		{
			int curveGraphicsX = yLegend.getFont().getSize() * 2;
			int curveGraphicsY = 0;
			int curveGraphicsW = (int) (g.getClipBounds().getWidth() - curveGraphicsX);
			int curveGraphicsH = (int) g.getClipBounds().getHeight();
			Graphics2D curveGraphics = (Graphics2D) g.create( curveGraphicsX, curveGraphicsY, curveGraphicsW, curveGraphicsH );
			drawAxisAndPoints( curveGraphics );

			int yLegendGraphicsX = 0;
			int yLegendGraphicsY = 0;
			int yLegendGraphicsW = curveGraphicsX;
			int yLegendGraphicsH = curveGraphicsH;
			Graphics2D yLegendGraphics = (Graphics2D) g.create( yLegendGraphicsX, yLegendGraphicsY, yLegendGraphicsW, yLegendGraphicsH );
			yLegend.draw( yLegendGraphics );
		}
		else
		{
			drawAxisAndPoints( g );
		}
	}

	private void drawAxisAndPoints( Graphics2D g )
	{
		int xp = space.getXDimension().getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
		int yp = space.getYDimension().getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
		int xs = space.getXDimension().getUpperBoundAxis().getGraduation().getDedicatedPixelCount();
		int ys = space.getYDimension().getUpperBoundAxis().getGraduation().getDedicatedPixelCount();
		int curveGraphicsX = yp;
		int curveGraphicsY = xs;
		int curveGraphicsW = (int) g.getClipBounds().getWidth() - yp - ys;
		int curveGraphicsH = (int) g.getClipBounds().getHeight() - xp - xs;

		if ( curveGraphicsW > 0 && curveGraphicsH > 0 )
		{
			Graphics2D pointsGraphics = (Graphics2D) g.create( curveGraphicsX, curveGraphicsY, curveGraphicsW, curveGraphicsH );
			space.update( pointsGraphics, figure );
			space.draw( g, pointsGraphics );

			if ( figure != null )
			{
				figure.draw( space );
			}

//			pointsGraphics.setColor( Color.green );
//			pointsGraphics.drawRect(0, 0, (int)pointsGraphics.getClipBounds().getWidth()-1, (int)pointsGraphics.getClipBounds().getHeight()-1);
		}
	}
}
