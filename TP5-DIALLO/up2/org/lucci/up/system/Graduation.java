package org.lucci.up.system;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;


public class Graduation extends BoundedGraphicalElement
{
	private float step = 1;
	private boolean autoStep = true;
	private int dedicatedPixelCount = 30;
	private GraduationStepProperties stepProperties = new GraduationStepProperties();
	private int stepPixelInterval = 40;
	private int pixelCountBetweenAxisLineAndText = 5;
    private Font font = new Font( null, Font.PLAIN, 9 );



	/**
	 * Gets the number of pixels of the area where the
	 * graduation labels are drawn.
	 * @return int
	 */
	public int getDedicatedPixelCount()
	{
		if ( isVisible() )
		{
	        return dedicatedPixelCount;
		}
		else
		{
			return 5;
		}
	}

	/**
	 * Sets the number of pixels of the area where the
	 * graduation labels are drawn.
	 * @param dedicatedPixelCount
	 */
	public void setDedicatedPixelCount( int dedicatedPixelCount )
	{
		if ( dedicatedPixelCount < 0 )
			throw new IllegalArgumentException( "dedicated pixel count < 0" );

		this.dedicatedPixelCount = dedicatedPixelCount;
	}

	/**
	 * Gets if the graduation is automatically stepped: if the
	 * number and the position of graduation labels are automatically
	 * defined.
	 * @return boolean
	 */
	public boolean isAutoStepped()
	{
		return autoStep;
	}

	/**
	 * Sets if the graduation has to be automatically stepped: if the
	 * number and the position of graduation labels are automatically
	 * defined.
	 * @param autoStep
	 */
	public void setAutoStepped( boolean autoStep )
	{
		this.autoStep = autoStep;
	}

	/**
	 * Gets the step used to display the graduation labels.
	 * @return float
	 */
	public float getStep()
	{
		return step;
	}

	/**
	 * Sets the step used to display the graduation labels.
	 * @param step
	 */
	public void setStep( float step )
	{
		if ( step <= 0 )
			throw new IllegalArgumentException( "step <= 0" );

		this.step = step;
		setAutoStepped(false);
	}


	protected void update( Graphics2D subGraphics )
	{
	    if ( isAutoBounded() )
	    {
	    	Dimension dimension = (Dimension) getParent().getParent();
			setBounds( dimension.getMin(), dimension.getMax() );
			setAutoBounded(true);
	    }

	    Axis axis = (Axis) getParent();
	    Dimension dimension = (Dimension) axis.getParent();
		float step = getStep();

	    if ( isAutoStepped() )
	    {
		    int stepCount = getStepCount( subGraphics );
		    float range = dimension.getMax() - dimension.getMin();
		    step = range / stepCount;
		    step = normalizeStep(step);
		    this.step = step;
	    }
	}


	protected void draw( Graphics2D graphics, Graphics2D subGraphics )
	{
		if ( isVisible() )
		{
			// if the origin axis is too near to the primary axis, the graduation steps
			// of the origin axis are not displayed : this prevents an ugly effect that text
			// drawn over another
		    if ( !graduationStepsMustBeHidden(subGraphics) )
	    	{
			    Axis axis = (Axis) getParent();
			    Dimension dimension = (Dimension) axis.getParent();
				float min = Math.max(getMin(), dimension.getMin());
				float max = Math.min(getMax(), dimension.getMax());

			    for (float i = 0; i <= max; i += step)
			    {
					i = org.lucci.up.system.Utilities.blabla(i);

				    if ( i >= dimension.getMin() )
				    {
						if ( ((Dimension) axis.getParent()).getOrientation() == Dimension.X )
						{
						    drawHorizontalAxisGraduationStep( graphics, i );
						}
						else
						{
						    drawVerticalAxisGraduationStep( graphics, i );
						}
				    }
			    }
		
			    for (float i = -step; i >= min; i -= step)
			    {
					i = org.lucci.up.system.Utilities.blabla(i);

				    if ( i <= dimension.getMax() )
				    {
						if ( ((Dimension) axis.getParent()).getOrientation() == Dimension.X )
						{
						    drawHorizontalAxisGraduationStep( graphics, i );
						}
						else
						{
						    drawVerticalAxisGraduationStep( graphics, i );
						}
				    }
			    }
	    	}
		}
	}

	private boolean graduationStepMustBeHidden(int step)
	{
		Axis axis = (Axis) getParent();
		AxisLine line = axis.getLine();
		Arrow arrow = line.getArrow();
		
		if ( arrow.isVisible() )
		{
//			int p = space.getScale().convertXToGraphicsCoordonateSystem( value, space );
			return false;
		}
		else
		{
			return false;
		}
		
	}

	private void drawHorizontalAxisGraduationStep( Graphics2D graphics, float value )
	{
		Space space = (Space) getParent().getParent().getParent();
        Dimension xDimension = space.getXDimension();
        Dimension yDimension = space.getYDimension();
        GraduationStepProperties stepProperties = getStepProperties();
		int x = xDimension.convertToGraphicsCoordonateSystem( value )
			+ yDimension.getLowerBoundAxis().getGraduation().getDedicatedPixelCount();
		int y = getHorizontalAxisY(graphics);

		if ( stepProperties.getLineLengthAt( value ) > 0 )
		{
			setGraphicsColor( graphics, stepProperties.getLineColorAt( value ) );

			if ( ((Axis) getParent()).getPosition() == Axis.UPPER_BOUND )
			{
				graphics.drawLine( x, y, x, y + stepProperties.getLineLengthAt( value ) );
			}
			else
			{
				graphics.drawLine( x, y, x, y - stepProperties.getLineLengthAt( value ) );
			}
		}

		String text = stepProperties.getTextAt( value );

		if ( text != null )
		{
		    int shift = getPixelCountBetweenAxisLineAndText();
		    FontRenderContext frc = graphics.getFontRenderContext();
		    GlyphVector gv = font.createGlyphVector( frc, text );
		    Rectangle2D r = gv.getPixelBounds(null, 0, 0);
		    int textWidth = (int) r.getWidth();
		    int textHeight = (int) r.getHeight();
		    setGraphicsColor( graphics, stepProperties.getTextColorAt( value ) );

			// only the upper bound X axis graduation is drawn on the top of the axis line
			if ( ((Axis) getParent()).getPosition() == Axis.UPPER_BOUND )
			{
			    graphics.drawGlyphVector( gv, x - textWidth / 2, y - shift );
			}
			else
			{
				int xshift = 0;
				
				if ( ((Axis) getParent()).getPosition() == Axis.ORIGIN )
				{
					if ( value == 0 )
					{
						if ( space.getYDimension().getOriginAxis().getLine().isVisible() )
						{
							xshift = -textWidth / 2 - 5;
						}
					}
					else if ( value == ((Dimension) getParent().getParent()).getMin() )
					{
						if ( space.getYDimension().getLowerBoundAxis().getLine().isVisible() )
						{
							xshift = textWidth / 2 + 5;
						}
					}
					else if ( value == ((Dimension) getParent().getParent()).getMin() )
					{
						if ( space.getYDimension().getUpperBoundAxis().getLine().isVisible() )
						{
							xshift = -textWidth / 2 - 5;
						}
					}
				}

			    graphics.drawGlyphVector( gv, x - textWidth / 2 + xshift, y + textHeight + shift );
			}
		}
	}

	private int getHorizontalAxisY(Graphics2D graphics)
	{
		int position = ((Axis) getParent()).getPosition();
		
		if ( position == Axis.LOWER_BOUND )
		{
			return (int) graphics.getClipBounds().getHeight() - 1 - getDedicatedPixelCount();
		}
		else if ( position == Axis.UPPER_BOUND )
		{
			return getDedicatedPixelCount();
		}
		else
		{
			Dimension dimension = (Dimension) getParent().getParent();
			return dimension.getUpperBoundAxis().getGraduation().getDedicatedPixelCount()
				+ dimension.getSiblingDimension().convertToGraphicsCoordonateSystem( 0 );
		}
	}

	private void drawVerticalAxisGraduationStep( Graphics2D graphics, float value )
	{
		Space space = (Space) getParent().getParent().getParent();
        Dimension xDimension = space.getXDimension();
        Dimension yDimension = space.getYDimension();
        GraduationStepProperties stepProperties = getStepProperties();
		int x = getVerticalAxisX(graphics);
		int y = yDimension.convertToGraphicsCoordonateSystem( value )
			+ xDimension.getUpperBoundAxis().getGraduation().getDedicatedPixelCount();

		if ( stepProperties.getLineLengthAt( value ) > 0 )
		{
			setGraphicsColor(graphics, stepProperties.getLineColorAt( value ) );
			
			if ( ((Axis) getParent()).getPosition() == Axis.UPPER_BOUND )
			{
				graphics.drawLine( x, y, x - stepProperties.getLineLengthAt( value ), y );
			}
			else
			{
				graphics.drawLine( x, y, x + stepProperties.getLineLengthAt( value ), y );
			}
		}

		String text = stepProperties.getTextAt( value );

		if ( text != null )
		{
		    int shift = getPixelCountBetweenAxisLineAndText();
		    FontRenderContext frc = graphics.getFontRenderContext();
		    GlyphVector gv = font.createGlyphVector( frc, text );
		    Rectangle2D r = gv.getPixelBounds(null, 0, 0);
		    int textWidth = (int) r.getWidth();
		    int textHeight = (int) r.getHeight();
		    setGraphicsColor(graphics, stepProperties.getTextColorAt( value ) );

			if ( ((Axis) getParent()).getPosition() == Axis.UPPER_BOUND )
			{
			    graphics.drawGlyphVector( gv, x + shift, y + textHeight / 2 );
			}
			else
			{
				int yshift = 0;
				
				if ( ((Axis) getParent()).getPosition() == Axis.ORIGIN )
				{
					if ( value == 0 )
					{
						if ( xDimension.getOriginAxis().getLine().isVisibleAt(0) )
						{
							if ( xDimension.getOriginAxis().getGraduation().isVisibleAt( 0 ) )
							{
								// if the text of the two dimension is the same at the (0, 0) point
								if ( space.getXDimension().getOriginAxis().getGraduation().getStepProperties().getTextAt(0f).equals(getStepProperties().getTextAt(0f)))
								{
									return;
								}
								else
								{
									yshift = -5;
								}
							}
						}
					}
					else if ( value == yDimension.getMin() )
					{
						if ( space.getXDimension().getLowerBoundAxis().getLine().isVisible() )
						{
							yshift = textHeight / 2 + 5;
						}
					}
					else if ( value == ((Dimension) getParent().getParent()).getMin() )
					{
						if ( space.getXDimension().isVisible()
							&& space.getXDimension().getUpperBoundAxis().isVisible()
							&& space.getXDimension().getUpperBoundAxis().getLine().isVisible() )
						{
							yshift = -textHeight / 2 - 5;
						}
					}
				}

			    graphics.drawGlyphVector( gv, x - textWidth - shift, y + textHeight / 2 + yshift );
			}
		}
	}

	/**
	 * This method will be invoked only the Y dimension.
	 */
	private int getVerticalAxisX(Graphics2D graphics)
	{
		int position = ((Axis) getParent()).getPosition();
		
		if ( position == Axis.LOWER_BOUND )
		{
			return getDedicatedPixelCount();
		}
		else if ( position == Axis.UPPER_BOUND )
		{
			return (int) graphics.getClipBounds().getWidth() - 1 - getDedicatedPixelCount();
		}
		else
		{
			Dimension dimension = (Dimension) getParent().getParent();
			return ((Dimension) getParent().getParent()).getLowerBoundAxis().getGraduation().getDedicatedPixelCount()
				+ dimension.getSiblingDimension().convertToGraphicsCoordonateSystem( 0 );
		}
	}

	private boolean graduationStepsMustBeHidden(Graphics2D graphics)
	{
		Axis axis = (Axis) getParent();
		Dimension dimension = (Dimension) axis.getParent();

		if ( axis.getPosition() == Axis.ORIGIN )
		{
			if ( dimension.getLowerBoundAxis().getGraduation().isVisible()
				|| dimension.getUpperBoundAxis().getGraduation().isVisible() )
			{
				// if the origin is included in the dimension
				if ( dimension.getMin() < 0 && 0 < dimension.getMax() )
				{
					// calculates the number of pixels used by the negative area
					// of the sibbling dimension
					Dimension sibblingDimension = dimension.getSiblingDimension();
					int size = sibblingDimension.getGraphicsSize(graphics);
					float negativeArrayRate = -sibblingDimension.getMin()
						/ (sibblingDimension.getMax() - sibblingDimension.getMin());
					size = (int) ((float) size * negativeArrayRate);
	
					Graduation lowerBoundAxisGraduation = dimension.getLowerBoundAxis().getGraduation();
					if ( lowerBoundAxisGraduation.isVisible() )
					{
						// if less than 50 pixels are dedicated to the negative area of the
						// dimension, it's better the graduation step not to be displayed
						return size < 50;
					}
					else
					{
						// the lower bound axis graduation is not visible, so
						// the origin bound axis graduation HAS to be shown
						// BUT if the text may be cutted by the limit of the
						// graphics, it's better that it's invisible
						return size < 1.5 * getPixelCountBetweenAxisLineAndText() + getFont().getSize();
					}
				}
				else
				{
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	private int getStepCount(Graphics g)
	{
		if ( ((Dimension) getParent().getParent()).getOrientation() == Dimension.X )
		{
			return Math.max(0, (int) g.getClipBounds().getWidth()) / getStepInPixel();
		}
		else
		{
			return Math.max(0, (int) g.getClipBounds().getHeight()) / getStepInPixel();
		}
	}

	
	/**
	 * Returns the stepInPixel.
	 * @return int
	 */
	public int getStepInPixel()
	{
		return stepPixelInterval;
	}

	/**
	 * Sets the stepInPixel. This interval may not be strictly respected: it is
	 * used for automatic stepping, it helps the system to calculate the best
	 * step. Setting this property means that you want the plotter to
	 * use a stepping of, nearly, <code>n</code> pixels.
	 * @param stepPixelInterval The stepPixelInterval to set
	 */
	public void setStepInPixel(int stepPixelInterval)
	{
		if ( stepPixelInterval <= 0 )
			throw new IllegalArgumentException( "step pixel interval must be > 0" );
		
		this.stepPixelInterval = stepPixelInterval;
		setAutoStepped(true);
	}

	/**
	 * Returns the pixelCountBetweenAxisLineAndText.
	 * @return int
	 */
	public int getPixelCountBetweenAxisLineAndText()
	{
		return pixelCountBetweenAxisLineAndText;
	}

	/**
	 * Sets the pixelCountBetweenAxisLineAndText.
	 * @param pixelCountBetweenAxisLineAndText The pixelCountBetweenAxisLineAndText to set
	 */
	public void setPixelCountBetweenAxisLineAndText(int pixelCountBetweenAxisLineAndText)
	{
		this.pixelCountBetweenAxisLineAndText = pixelCountBetweenAxisLineAndText;
	}


    public Font getFont()
    {
        return font;
    }

    public void setFont( Font font)
    {
    	if ( font == null )
    		throw new IllegalArgumentException( "graduation font cannot be set to null" );

        this.font = font;
    }
    
   	private float normalizeStep( double step )
	{
		double intlog = Math.floor( Math.log( step ) / Math.log( 10 ) );
		step = step * Math.pow( 10, -intlog );
		step = Math.round( step );

		switch ( (int) step )
		{
			case 1: step = 1; break;
			case 2: step = 2; break;
			case 3: step = 2.5; break;
			case 4: step = 5; break;
			case 5: step = 5; break;
			case 6: step = 5; break;
			case 7: step = 5; break;
			case 8: step = 10; break;
			case 9: step = 10; break;
		}

		return (float) (step * Math.pow( 10, intlog ));
	}
	/**
	 * Returns the stepProperties.
	 * @return GraduationStepProperties
	 */
	public GraduationStepProperties getStepProperties()
	{
		return stepProperties;
	}

	/**
	 * Sets the stepProperties.
	 * @param stepProperties The stepProperties to set
	 */
	public void setStepProperties(GraduationStepProperties stepProperties)
	{
		if ( stepProperties == null )
			throw new IllegalArgumentException( "stepProperties cannot be set to null" );
		
		this.stepProperties = stepProperties;
	}

	private void setGraphicsColor( Graphics2D graphics, Color color ) 
	{
		if ( color == null )
		{
			color = getColor();
		}

		graphics.setColor(color);
	}
}
