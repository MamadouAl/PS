package org.lucci.up.data.rendering.point;

import org.lucci.up.data.rendering.DataElementRenderer;

public abstract class PointRenderer extends DataElementRenderer
{
	private int xShift = 0;
	private int yShift = 0;


    public int getXShift()
    {
        return xShift;
    }

    public int getYShift()
    {
        return yShift;
    }

    public void setXShift(int xShift)
    {
        this.xShift = xShift;
    }

    public void setYShift(int yShift)
    {
        this.yShift = yShift;
    }

	/**
	 * @see org.lucci.up.data.DataElementRenderer#getRenderableDataElementClass()
	 */
	public Class getRenderableDataElementClass()
	{
		return org.lucci.up.data.Point.class;
	}

}
