package org.lucci.up.data.event;


import org.lucci.up.data.Figure;
import org.lucci.up.data.Point;


/**
 * @author Luc Hogie
 */

public interface FigureListener extends DataElementListener
{
	void pointInserted( Figure figure, Point point, int position );
	void pointRemoved( Figure figure, Point point, int position );
	void figureInserted( Figure figure, Figure subFigure, int position );
	void figureRemoved( Figure figure, Figure subFigure, int position );
}
