package org.lucci.up.data.event;


import org.lucci.up.data.Point;


/**
 * @author Luc Hogie
 */

public interface PointListener extends DataElementListener
{
	void xChanged( Point point, float oldX, float newX );
	void yChanged( Point point, float oldY, float newY );
}
