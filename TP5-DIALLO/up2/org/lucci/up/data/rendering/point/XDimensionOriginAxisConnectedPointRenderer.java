package org.lucci.up.data.rendering.point;

import org.lucci.up.data.*;
import org.lucci.up.system.Dimension;
import org.lucci.up.system.Space;

public class XDimensionOriginAxisConnectedPointRenderer extends PointRenderer
{

    public void drawImpl(DataElement object, Space space)
    {
		Point point = (Point) object;
		Dimension xDimension = space.getXDimension();
		Dimension yDimension = space.getYDimension();

		int x = xDimension.convertToGraphicsCoordonateSystem(point.getX());
		int y = yDimension.convertToGraphicsCoordonateSystem(point.getY());
		space.getGraphics().drawLine(x, y, x, (int) space.getOriginPoint().getY());
    }

}
