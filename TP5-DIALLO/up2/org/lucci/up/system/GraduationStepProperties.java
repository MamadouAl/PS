package org.lucci.up.system;

import java.awt.*;


/**
 * @author Luc Hogie
 *
 * GraduationStepProperties defines the value of the step, its color, its font and
 * its angle at each step of a graduation.
 * This is a default implementation and users may derive it.
 * 
 * Why subclassing it? Because maybe you want to put some special text at
 * determined steps, or particular color? You can set to graduation line where
 * you want... And a lot more.
 */
public class GraduationStepProperties
{
    public String getTextAt( float step )
    {
		int intStep = (int) step;

		if ( step == intStep )
		{
			return Integer.toString( intStep );
		}
		else
		{
			return Float.toString( step );
		}
    }

    public Color getLineColorAt( float step )
    {
        return null;
    }

    public Color getTextColorAt( float step )
    {
        return null;
    }

    public int getLineLengthAt( float step )
    {
        return 3;
    }

	/**
	 * This feature is not yet supported.
	 * @param textAngle
	 */
    public int getTextAngleAt( float step )
    {
        return 0;
    }
}
