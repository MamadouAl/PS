package org.lucci.up;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

import org.lucci.up.data.*;
import org.lucci.up.data.math.*;
import org.lucci.up.data.rendering.*;
//import org.lucci.up.data.rendering.figure.ClosedNaturalCubicSplineFigureRenderer;
import org.lucci.up.data.rendering.figure.ConnectedLineFigureRenderer;
import org.lucci.up.data.rendering.figure.FigureRenderer;
import org.lucci.up.data.rendering.point.HistogramPointRenderer;
import org.lucci.up.data.rendering.point.OriginPointConnectedPointRenderer;
import org.lucci.up.data.rendering.point.PointRenderer;
import org.lucci.up.data.rendering.point.TextPointRenderer;
import org.lucci.up.system.*;

import org.lucci.up.data.rendering.point.PointAsDotRenderer;


public class Demo
{
	public static void main( String[] args )
		throws Throwable
	{
		System.out.println( "starting");
		JFrame frame = new JFrame( "Up demo" );
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int side = (int) (screenSize.getHeight() * 0.5);
		frame.setSize( side, side );
		frame.setLocation((int) (screenSize.getWidth() - side) / 2, (int) (screenSize.getHeight() - side) / 2);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("default", createDefaultModel());
		tabbedPane.addTab("1", createMathematicalModel());
		tabbedPane.addTab("2", createStatisticalModel());
		tabbedPane.addTab("3", createMovingModel());
		tabbedPane.addTab("4", createPhysicalModel());
		tabbedPane.addTab("5", createAutoBoundModel());
		tabbedPane.addTab("6", createSpecialGraduationStepsModel());
		Container contentPane = frame.getContentPane();
		contentPane.setLayout( new GridLayout( 1, 1 ) );
		contentPane.add(tabbedPane);
		frame.setVisible( true );
	}

	
	private static SwingPlotter createStatisticalModel()
	{
		Figure figure = new Figure();
		figure.addPoint( makePoint( 1, 1 ) );
		figure.addPoint( makePoint( 2, 4 ) );
		figure.addPoint( makePoint( 3, 9 ) );
		figure.addPoint( makePoint( 4, 11 ) );
		figure.addPoint( makePoint( 5, 13 ) );
		figure.addPoint( makePoint( 6, 18 ) );
		figure.addPoint( makePoint( 7, 24 ) );
		
		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figure );
		
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.setBackgroundColor(new Color(12, 115, 10, 50));

		space.getXDimension().setBounds(0, 8);
		space.getYDimension().setBounds(0, 30);
		space.getYDimension().getLowerBoundAxis().getGraduation().setStep(5);
		space.getYDimension().getOriginAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getGrid().setColor(Color.black);

		space.getYDimension().getLegend().setText("Degrees");

		space.getXDimension().getOriginAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getGraduation().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
		space.getXDimension().getLegend().setVisible(false);
		space.getLegend().setText("Average temperature");
		return plotter;
	}

	private static Point makePoint( int i, int t )
	{
		Point p = new Point( i, t );
		DataElementRenderer r = new HistogramPointRenderer();
		r.setColor( new Color( 255 / 24 * t, 0, 255 / 24 * (24-t), 128 ) );
		p.addRenderer(r);
		return p;
	}


	private static SwingPlotter createMovingModel()
	{
		final Figure figure = new Figure();
		figure.addPoint( new Point( 1, 1 ) );
		figure.addPoint( new Point( 1, 1 ) );
		figure.addPoint( new Point( 1, 1 ) );
		/*DataElementRenderer renderer = new ClosedNaturalCubicSplineFigureRenderer();
		renderer.setColor(Color.black);
		figure.addRenderer(renderer); */

		SwingPlotter plotter = new SwingPlotter();
		plotter.setImageBufferedUsed(false);
		plotter.setCyclicTask( new Runnable() {
			float t = 0;
			public void run() {
				t += 0.025;

				for (int i = 0; i < 3; ++i) {
					Point p = figure.getPointAt( i );
					p.setX( (float) Math.cos( t * (i*i + 1) ) );
					p.setY( (float) Math.sin( t * (i + 1) ) );
				}
    	    }
		} );

		java.awt.Font font = new java.awt.Font( null, java.awt.Font.PLAIN, 12 );
		Point x = new Point( 0.5, -0.2 );
		TextPointRenderer xtpr = new TextPointRenderer();
		xtpr.setText("x");
		xtpr.setFont(font);
		x.addRenderer(xtpr);
		Point y = new Point( -0.2, 0.5 );
		TextPointRenderer ytpr = new TextPointRenderer();
		ytpr.setText("y");
		ytpr.setFont(font);
		y.addRenderer(ytpr);
		Figure f = new Figure();
		f.addPoint(x);
		f.addPoint(y);
		
		Figure fl = new Figure();
		fl.addFigure(figure);
		fl.addFigure(f);
		


		plotter.setAutoUpdateFrequency( 50 );
		plotter.getGraphics2DPlotter().setFigure(fl);
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.setBackgroundColor(Color.orange);

		space.getLegend().setText("Moving closed cubic splines");
		space.getLegend().setFont( new Font(null, Font.ITALIC, 14) );
		space.getLegend().setColor(Color.black);
		space.getXDimension().setBounds(-2f, 2f);
		space.getYDimension().setBounds(-2f, 2f);
		space.setColor(Color.gray);



		space.getYDimension().getLowerBoundAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getOriginAxis().getGraduation().setVisible(false);
		space.getYDimension().getOriginAxis().getLine().setBounds(0, 1);
		space.getYDimension().getLowerBoundAxis().getGraduation().setVisible(false);
		space.getYDimension().getLegend().setVisible(false);
		space.setGridVisible(false);


		space.getXDimension().getLowerBoundAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getXDimension().getOriginAxis().getLine().setBounds(0, 1);
		space.getXDimension().getOriginAxis().getGraduation().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getGraduation().setVisible(false);
		space.getXDimension().getLowerBoundAxis().getLine().getArrow().setVisible(false);
		space.getXDimension().getLegend().setText("50 img/s");
		return plotter;
	}

	private static SwingPlotter createMathematicalModel()
	{
		Function function = new Function() {
			public Point evaluate( double t ) {
				return new Point( t * Math.cos( t ), Math.sin( t ) );
			}
		};

		function.setDefinitionValues( 0, 2 * Math.PI, Math.PI / 100 );
		Figure figure = function.createFigure();
		figure.addRenderer(new ConnectedLineFigureRenderer());

		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figure );
		Space space = plotter.getGraphics2DPlotter().getSpace();

		space.getLegend().setText("x = t cos(t) ; y = sin(t)");
		space.getXDimension().getLegend().setVisible(false);
		space.getYDimension().getLegend().setVisible(false);
		space.getXDimension().getLowerBoundAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getLowerBoundAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);

		return plotter;
	}


	private static SwingPlotter createPhysicalModel()
	{
		final Function f1 = new Function() {
			public Point evaluate(double d) {
				return new Point( d, d * (d/2 - 1) * (d/4 - 2) + d + 2 );
			}
		};

		f1.setDefinitionValues(-3, 5, 0.1);
		Figure ff1 = f1.createFigure();
		FigureRenderer ff1r = new ConnectedLineFigureRenderer();
		ff1r.setColor(Color.blue);
		ff1.addRenderer(ff1r);

		Function f2 = new Function() {
			public Point evaluate(double d) {
				Point p = f1.evaluate(d);
				p.setX( p.getX() + 1 );
				p.setY( -p.getY() / 2 - p.getX());
				return p;
			}
		};

		f2.setDefinitionValues(-3, 5, 0.1);
		Figure ff2 = f2.createFigure();
		FigureRenderer ff2r = new ConnectedLineFigureRenderer();
		ff2r.setColor(Color.orange);
		ff2.addRenderer(ff2r);

		Function af1 = new Function() {
			public Point evaluate(double d) {
				Point p = f1.evaluate(d);
				p.translate(0f, (float) Math.random() * 3 - 1.5f);
				return p;
			}
		};

		af1.setDefinitionValues(-3, 5, 0.5);
		Figure aff1 = af1.createFigure();
		aff1.addRenderer(new PointAsDotRenderer());

		Figure figureList = new Figure();
		figureList.addFigure( ff1 );
		figureList.addFigure( aff1 );
		figureList.addFigure( ff2 );

		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figureList );
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.setMode(Space.PHYSICS);
		space.getLegend().setVisible(false);
		space.getXDimension().getLegend().setText("Speed");
		space.getXDimension().getLegend().setFont( new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setFont( new Font(null, Font.PLAIN, 12));
		space.getYDimension().getLegend().setText("Acceleration");

/*	
		ImageFactory im = new ImageFactory();
		im.setGraphics2DPlotter(plotter.getGraphics2DPlotter());
		String b = im.getSVGCode(500, 500);

		try
		{
			new FileOutputStream( "/home/hogie/a.svg" ).write( b.getBytes() );
		}
		catch ( IOException ex )
		{
		}
*/
		return plotter;
	}
	
	private static SwingPlotter createAutoBoundModel()
	{
		SwingPlotter plotter = new SwingPlotter();
		final Point a = new Point(0, 0);
		final Point b = new Point(0, 0);
		Figure figure = new Figure();
		figure.addPoint( a );
		figure.addPoint( b );
		PointRenderer figureRenderer = new OriginPointConnectedPointRenderer();
		figureRenderer.setColor( Color.green );
		figure.addRenderer(figureRenderer);

		TextPointRenderer aTextRenderer = new TextPointRenderer();
		aTextRenderer.setColor(Color.red);
		aTextRenderer.setText("A");
		aTextRenderer.setXShift(8);
		aTextRenderer.setYShift(8);
		a.addRenderer(aTextRenderer);
		PointAsDotRenderer aPointRenderer = new PointAsDotRenderer();
		aPointRenderer.setColor(Color.red);
		aPointRenderer.setSize(4);
		aPointRenderer.setFillColor(Color.red);
		a.addRenderer(aPointRenderer);

		TextPointRenderer bTextRenderer = new TextPointRenderer();
		bTextRenderer.setColor(Color.red);
		bTextRenderer.setText("B");
		bTextRenderer.setXShift(8);
		bTextRenderer.setYShift(8);
		b.addRenderer(bTextRenderer);
		PointAsDotRenderer bPointRenderer = new PointAsDotRenderer();
		bPointRenderer.setColor(Color.red);
		bPointRenderer.setSize(4);
		bPointRenderer.setFillColor(Color.red);
		b.addRenderer(bPointRenderer);


		figure.addPoint( new Point( 0, 0 ) );
		plotter.getGraphics2DPlotter().setFigure(figure);
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.getLegend().setText("Auto-bounding");
		space.setBackgroundColor(new Color(50, 50, 255));
		space.getXDimension().getOriginAxis().setColor(space.getXDimension().getGrid().getColor());
		space.getYDimension().getOriginAxis().setColor(space.getYDimension().getGrid().getColor());
		space.getXDimension().getOriginAxis().getGraduation().setVisible( false );
		space.getXDimension().getOriginAxis().getLine().getArrow().setVisible(false);
		space.getYDimension().getOriginAxis().getGraduation().setVisible( false );
		space.getYDimension().getOriginAxis().getLine().getArrow().setVisible(false);
		
		plotter.setCyclicTask(new Runnable()
		{
			double s = 0;
			double t = 0;
			
			public void run()
			{
				a.setX( (float) Math.cos( t ) );
				a.setY( (float) Math.sin( t ) );
				b.setX( (float) Math.cos( s ) );
				b.setY( (float) Math.sin( s ) );
				t += 0.005;
				s += 0.01;
			}
		} );
		
		plotter.setAutoUpdateFrequency(50);
		plotter.setImageBufferedUsed(false);
		
		return plotter;
	}
	
	private static SwingPlotter createSpecialGraduationStepsModel()
	{
		Function function = new Function() {
			public Point evaluate( double t ) {
				return new Point( t, Math.cos( t ) * t );
			}
		};

		function.setDefinitionValues( -5, 10, 0.1 );
		Figure figure = function.createFigure();
		figure.addRenderer(new ConnectedLineFigureRenderer());

		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figure );
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.getLegend().setText("Special graduation");
		space.getXDimension().getLegend().setVisible(false);
		space.getYDimension().getLegend().setVisible(false);
		space.getXDimension().getLowerBoundAxis().setVisible(false);
		space.getXDimension().getUpperBoundAxis().setVisible(false);
		space.getYDimension().getLowerBoundAxis().setVisible(false);
		space.getYDimension().getUpperBoundAxis().setVisible(false);

		space.getXDimension().getOriginAxis().getGraduation().setStepProperties( new GraduationStepProperties()
		{
			public String getTextAt( float step )
			{
				if ( step == -1 )
				{
					return null;
				}
				else if ( (int) step == step )
				{
					return super.getTextAt(step);
				}
				else
				{
					return null;	
				}
			}

			public int getLineLengthAt( float step )
			{
				if ( (int) step == step )
				{
					return 4;
				}
				else if ( (int) (step * 2) == step * 2 )
				{
					return 3;
				}
				
				else
				{
					return 1;	
				}
			}

			public Color getTextColorAt( float step )
			{
				if ( 2 < step && step < 7 )
				{
					return Color.red;
				}
				else
				{
					return null;	
				}
			}
		} );

		space.getXDimension().getOriginAxis().getGraduation().setStep(0.1f);
		space.getXDimension().getGrid().setVisible(false);
		space.getYDimension().getOriginAxis().getGraduation().setBounds(-8, 6);
		return plotter;
	}
	
	
	private static SwingPlotter createDefaultModel()
	{
		SwingPlotter plotter = new SwingPlotter();
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.setRange(-5f, 3f, 1f, -4f, 6f, 0.5f);
		return plotter;
	}

}