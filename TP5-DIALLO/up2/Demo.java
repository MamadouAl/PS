import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

import org.lucci.up.*;
import org.lucci.up.data.*;
import org.lucci.up.data.math.*;
import org.lucci.up.data.rendering.*;
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
	//	throws Throwable
	{
		System.out.println( "starting");
		JFrame frame = new JFrame( "Up demo" );
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int side = (int) (screenSize.getHeight() * 0.5);
		frame.setSize( side, side );
		frame.setLocation((int) (screenSize.getWidth() - side) / 2, (int) (screenSize.getHeight() - side) / 2);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout( new GridLayout( 1, 1 ) );
		contentPane.add(createPointEtCourbe());
		frame.setVisible( true );
	}


        private static SwingPlotter createPointEtCourbe()
        {
		Figure f1 = new Figure();
		f1.addPoint( new Point( -1, -1 ) );
		f1.addPoint( new Point( 1, 0.4 ) );
		f1.addPoint( new Point( 4, -0.5 ) );
		DataElementRenderer renderer1 = new ConnectedLineFigureRenderer(); 
		renderer1.setColor(Color.blue);
		f1.addRenderer(renderer1);

		Figure f2 = new Figure();
		for (int i = 0; i < 10; ++i) {
                    f2.addPoint( new Point(i/2.-1, i/5.-1));
		}
		DataElementRenderer renderer2 = new PointAsDotRenderer(); 
		renderer2.setColor(Color.red);
		f2.addRenderer(renderer2);

		
		Function function = new Function() {
			public Point evaluate( double t ) {
				return new Point( t * Math.cos( t ), Math.sin( t ) );
			}
		};

		function.setDefinitionValues( 0, 2 * Math.PI, Math.PI / 100 );

		Figure f3 = function.createFigure();
		DataElementRenderer renderer3 = new ConnectedLineFigureRenderer();
		renderer3.setColor(Color.green);
		f3.addRenderer(renderer3);
		

		Figure figureList = new Figure();
		figureList.addFigure( f1 );
		figureList.addFigure( f2 );
		figureList.addFigure( f3 );


		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figureList );
		Space space = plotter.getGraphics2DPlotter().getSpace();

		space.setMode(Space.PHYSICS);
		space.setBackgroundColor(Color.white);
		space.setColor(Color.black);
		space.getLegend().setText("Exemple de tracés");

		space.getXDimension().getLegend().setText("Abscisses");
		space.getYDimension().getLegend().setText("Ordonnées");

		return plotter;

        }	
}
