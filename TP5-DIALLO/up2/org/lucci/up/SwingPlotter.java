package org.lucci.up;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;

import org.lucci.up.system.*;


import java.awt.*;


/**
 * The SwingPlotter is a Swing component on which a graphical representation
 * will be plotted.
 * 
 * The SwingPlotter may be or may be not buffered. When it is, the full refresh is
 * slow but you can fastly move the widget, make it visible/unvisible. It will be
 * extremely fastly repainted.
 * If the buffer is disabled, the graphical representation is not stored and so it
 * has to be recalculated for each repaint process. This is useful when the content of
 * the data change often and the component has to be updated (eg: animations).
 * 
 * It has the ability to be periodically repainted: this allow the user
 * to program an animation. If the user want the widget to be
 * repainted according to a strict period, he has to define the
 * <i>task</i>. The SwingPlotter calculates the duration of the
 * task and waits only the remaining time (remaining = period - task duration)
 * before repating the component. The constrainst is that the task must
 * execute faster than one period.
 * If the user do not want a strict period, he can do whatever on the 
 * figure and call SwingPlotter.repaint(0) at any time.
 * 
 * @author Luc Hogie
 */

public class SwingPlotter extends JComponent
{
	// the image used as buffer for fast repaints
	private boolean redrawNeeded = true;
	private boolean imageBufferedUsed = true;
	private Image image;

	private Thread thread = null;
	private float autoUpdateFrequency = 0;
	private Runnable cyclicTask;

	private Graphics2DPlotter painter = new Graphics2DPlotter();


	public SwingPlotter()
	{
		painter.getSpace().setImageObserver(this);
		painter.getSpace().setBackgroundColor( Color.white );
		setDoubleBuffered( false );
	}

	public Graphics2DPlotter getGraphics2DPlotter()
	{
		return painter;
	}

	/**
	 * @see java.awt.Component#setForeground(Color)
	 */
	public void setForeground( Color fg )
	{
		throw new IllegalStateException( "this method shouldn't be used. Use Space.setColor() instead" );
	}

	/**
	 * @see java.awt.Component#getForeground()
	 */
	public Color getForeground()
	{
		return painter.getSpace().getColor();
	}
	
	/**
	 * @see java.awt.Component#setBackground(Color)
	 */
	public void setBackground( Color bg )
	{
		throw new IllegalStateException( "this method shouldn't be used. Use Space.setBackgroundColor() instead" );
	}

	/**
	 * @see java.awt.Component#getBackground()
	 */
	public Color getBackground()
	{
		return painter.getSpace().getBackgroundColor();
	}


	/**
	 * @see java.awt.Component#paint(Graphics)
	 */
	public void paint( Graphics g )
	{
		long startDate = System.currentTimeMillis();

		if ( isImageBufferedUsed() )
		{
			if ( isUpdateNeeded() )
			{
				setUpdateNeeded( false );
				java.awt.Dimension size = getSize();
				image = createImage( size.width, size.height );
				Graphics imageGraphics = image.getGraphics();
				imageGraphics.setClip( 0, 0, size.width, size.height );
				drawOnGraphics( imageGraphics );
			}

			g.drawImage( image, 0, 0, Color.white, this );
			image = null;
		}
		else
		{
			drawOnGraphics( g );
		}

		long endDate = System.currentTimeMillis();
		long duration = endDate - startDate;
//		System.out.println("drawn in " + duration + " ms" );
	}

	private void drawOnGraphics( Graphics g )
	{
		painter.paint( (Graphics2D) g );
	}

	/**
	 * Gets the update frequency of the plotter.
	 * @return int
	 */
	public float getAutoUpdateFrequency()
	{
		return autoUpdateFrequency;
	}



	/**
	 * Gets if the component has to be updated.
	 * @return boolean
	 */
	public boolean isUpdateNeeded()
	{
		if ( redrawNeeded )
		{
			return true;
		}
		else
		{
			// the first time, the image does not exist
			if ( image == null )
			{
				return true;
			}
			else
			{
				// if the size of the component has changed
				if ( image.getHeight( this ) != getSize().height || image.getWidth( this ) != getSize().width )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}


	/**
	 * Sets the frequency of the update process. There is a background
	 * thread (with maximum priority) that will invoke the cyclic task, the
	 * <code>setUpdatedNeed( true) </code> and <code>repaint()</code>
	 * methods of the component.
	 * @param newFrequency
	 */
	public void setAutoUpdateFrequency( float newFrequency )
	{
		if ( newFrequency != autoUpdateFrequency )
		{
			autoUpdateFrequency = newFrequency;

			if ( autoUpdateFrequency > 0 )
			{
				if ( thread == null )
				{
					thread = new Thread( new TaskThread() );
					thread.setPriority(Thread.MAX_PRIORITY);
					thread.start();
				}
				else
				{
					thread.interrupt();
				}
			}
			else
			{
				if ( thread != null )
				{
					thread = null;
				}
			}
		}
	}


	/**
	 * Sets if the component has to be updated. The update is
	 * needed if the data has changed.
	 * @param b
	 */
	public void setUpdateNeeded( boolean b )
	{
		redrawNeeded = b;
	}

	/**
	 * Gets if the image buffering is used. This allow
	 * very fast repaint. This is useful if the component
	 * if moved, hidden, made visible very frequently.
	 * @return boolean
	 */
	public boolean isImageBufferedUsed()
	{
		return imageBufferedUsed;
	}

	/**
	 * Sets if the image buffering is used. This allow
	 * very fast repaint. This is useful if the component
	 * if moved, hidden, made visible very frequently.
	 * @param imageBufferedUsed The imageBufferedUsed to set
	 */
	public void setImageBufferedUsed(boolean imageBufferedUsed)
	{
		this.imageBufferedUsed = imageBufferedUsed;
	}
	
	/**
	 * Gets the task that is periodically invoked. The task
	 * is invoked and then the repaint process is called.
	 * @return Runnable
	 */
    public Runnable getCyclicTask()
    {
        return cyclicTask;
    }

	/**
	 * Sets the task that is periodically invoked. The task
	 * is invoked and then the repaint process is called.
	 * @param cyclicTask
	 */
    public void setCyclicTask(Runnable cyclicTask)
    {
        this.cyclicTask = cyclicTask;
    }


	private class TaskThread extends Thread
	{
		public void run()
		{
			while ( thread != null )
			{
				try
				{
					long period = (long) (1000 / getAutoUpdateFrequency());
					System.out.println( "period : " + period);
					long start = System.currentTimeMillis();
					
					if ( cyclicTask != null )
					{
						cyclicTask.run();
					}
	
					long end = System.currentTimeMillis();
					long duration = end - start;
					
					if ( duration > period )
					{
						new Error( "the run() method of the cylic task took " + duration + " ms to execute, this is too long. You should optimize it or set a smaller grapic update frequency." ).printStackTrace();
					}
					else
					{
						Thread.sleep( period - duration );
					}
	
					setUpdateNeeded( true );
					repaint(0);
				}
				catch ( InterruptedException ex )
				{
				}
			}
		}
	}
}

