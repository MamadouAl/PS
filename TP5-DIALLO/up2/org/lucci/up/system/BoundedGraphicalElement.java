package org.lucci.up.system;

/**
 * <p>
 * The space object is made of (and itself is) bounded by
 * numeric values.
 * </p>
 * 
 * <p>
 * The mathematical concept of space does not talk about
 * an interval but here we have to render the graphical
 * representation of the space and so it is no possible
 * to consider the whole space: we want to display only
 * the interval required by the user.
 * </p>
 * 
 * <p>
 * A bounded graphical element is a graphical element plus
 * the conception of interval. This is used only for
 * rendering. This class only brings the concept of interval
 * used for painting.
 * </p>
 * 
 * <p>
 * Knowing that the interval may be automatically deduced from
 * the user data or even from the parent bounded graphical elements,
 * we assume that a bounded graphical element can be <i>auto bounded</i>.
 * </p>
 * 
 * <p>
 * Note: the bounds values are inclusive (ex: [-1, 1])
 * </p>
 * 
 * @author Luc Hogie
 */



public class BoundedGraphicalElement extends GraphicalElement
{
	private float min = -10, max = 10;
	private boolean autoBounded = true;

	/**
	 * @return the minimum value for the bounds.
	 * This cannot be lower than the maximum value.
	 */
   	public float getMin()
	{
		return min;
	}

	/**
	 * Sets the minimum value of the bounds.
	 * It cannot be set to a greater value than
	 * the maximum one.
	 * Manually setting the bound implies that 
	 * the element is no more (it is was) auto bounded.
	 */
	public void setMin( float min )
	{
		if ( min >= max )
			throw new IllegalArgumentException( "min >= max" );

		this.min = min;
		setAutoBounded(false);
	}

	/**
	 * @return the maximum value for the bounds.
	 * This cannot be greater than the minimum value.
	 */
	public float getMax()
	{
		return max;
	}

	/**
	 * Sets the maximum value of the bounds.
	 * It cannot be set to a lower value than
	 * the minimum one.
	 * Manually setting the bound implies that 
	 * the element is no more (it is was) auto bounded.
	 */
	public void setMax( float max )
	{
		if ( max <= min )
			throw new IllegalArgumentException( "max <= min" );

		this.max = max;
		setAutoBounded(false);
	}

	/**
	 * Sets the bounds. Obviously, <i>min</i> must
	 * be lower than <i>max</i>.
	 */
	public void setBounds( float min, float max )
	{
		if ( max <= min )
			throw new IllegalArgumentException( "max <= min" );

		this.min = min;
		this.max = max;
		setAutoBounded(false);
	}

	/**
	 * @return if the graphical element is auto bounded or not.
	 */
    public boolean isAutoBounded()
    {
        return autoBounded;
    }

	/**
	 * Sets if the element is auto bounded or not. If it is,
	 * the bounds will be automatically calculated.
	 */
    public void setAutoBounded(boolean autoBounded)
    {
        this.autoBounded = autoBounded;
    }
    
	/**
	 * @return if the graphical element is visible at the given position.
	 */
    public boolean isVisibleAt( float position )
    {
    	if ( isVisible() )
    	{
			return getMin() <= position && position <= getMax();
    	}
    	else
    	{
    		return false;
    	}
    }
}