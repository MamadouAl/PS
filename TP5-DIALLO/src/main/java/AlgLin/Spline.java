package AlgLin;

/**
 * Classe qui permet de manipuler les méthodes d'interpolation par spline
 * cubique pour des fonctions réelles d'une variable réelle.
 * 
 * @author DIALLO Mamadou Aliou
 * 
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.*;

import org.lucci.up.*;
import org.lucci.up.data.*;
import org.lucci.up.data.math.*;
import org.lucci.up.data.rendering.*;
import org.lucci.up.data.rendering.figure.ConnectedLineFigureRenderer;

import org.lucci.up.data.rendering.point.PointAsDotRenderer;
public class Spline {
	// Attributs
	private double[] abX;
	private double[] ordY;
	private Vecteur deriveeSeconde;

	// Constructeur
	public Spline(double[] abscisses, double[] ordonnees) {
		if (abscisses.length != ordonnees.length) {
			throw new IllegalArgumentException("Les tableaux x et y doivent avoir la même longueur");
		}
		if (abscisses.length < 2) {
			throw new IllegalArgumentException("Il faut au moins deux points de support");
		}
		this.abX = abscisses;
		this.ordY = ordonnees;
		deriveeSeconde = calculDeriveeSeconde(abscisses, ordonnees);
	}

	/**
	 * Méthode qui permet de calculer la dérivée seconde de la fonction interpolée
	 * par spline cubique.
	 * 
	 * @throws IrregularSysLinException
	 */
	private Vecteur calculDeriveeSeconde(double[] x, double[] y) {
		int n = x.length;
		Vecteur g = new Vecteur(n + 2);
		double[] a = new double[n];
		double[] b = new double[n];
		double[] c = new double[n];
		double[] d = new double[n];

		g.remplacecoef(0, 0);
		g.remplacecoef(n - 1, 0);

		// Compute a, b, c, d
		b[0] = 2 * (x[2] - x[0]);
		c[0] = x[2] - x[1];
		d[0] = 6 * ((y[2] - y[1]) / (x[2] - x[1]) - (y[1] - y[0]) / (x[1] - x[0]));

		for (int j = 1; j <= n - 2; j++) {
			a[j] = c[j - 1];
			b[j] = 2 * (x[j + 1] - x[j - 1]);
			c[j] = x[j + 1] - x[j];
			d[j] = 6 * ((y[j + 1] - y[j]) / d[j - 1] - (y[j] - y[j - 1]) / a[j]);
		}
		a[n - 1] = c[n - 2];
		b[n - 1] = 2 * (x[n - 1] - x[n - 3]);
		d[n - 1] = 6 * ((y[n - 1] - y[n - 2]) / (x[n - 1] - x[n - 2]) - (y[n - 2] - y[n - 3]) / a[n - 2]);

		// On construit le système tridiagonal qui permet de calculer les dérivées
		// secondes de la fonction
		double[][] tab = { c, b, a }; // c : sous-diagonale, b : diagonale, a : sur-diagonale
		Vecteur dVect = new Vecteur(d);
		Mat3Diag diag = new Mat3Diag(tab);
		System.out.println("Mat3Diag : \n" + diag.toString());
		Thomas thomas = null;
		try {
			thomas = new Thomas(diag, dVect);
		} catch (IrregularSysLinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vecteur gbis = null;
		try {
			gbis = thomas.resolution();
		} catch (IrregularSysLinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i < n; i++) {
			g.remplacecoef(i, gbis.getCoeff(i - 1));
		}
		System.out.println("derivéeSeconde = " + g.toString());
		return g;
	}

	/**
	 * Méthode qui permet d'évaluer la fonction interpolée par spline cubique en un
	 * point donné.
	 */
	public double evaluateSpline(double val) {
		// Vérification si la valeur x est dans l'intervalle des abscisses des points de
		// support
		if (val < abX[0] || val > abX[abX.length - 1]) {
			return 0;
			//throw new DataOutOfRangeException("La valeur fournie est en dehors de la plage des données d'entrée.");
		}

		// Recherche de l'intervalle contenant la valeur
		int index = 0;
		for (int i = 0; i < abX.length - 1; i++) {
			if (val >= abX[i] && val <= abX[i + 1]) {
				index = i;
				break;
			}
		}

		// Calcul de la valeur interpolée par spline cubique
		double h = abX[index + 1] - abX[index];
		double t = (val - abX[index]) / h;
		double y = (1 - t) * ordY[index] + t * ordY[index + 1] + t * (t - 1)
				* ((deriveeSeconde.getCoeff(index + 1) * h * h) / 6 + (deriveeSeconde.getCoeff(index) * h * h) / 3);
		return y;
	}

	public void afficheGraphe()  {
		JFrame frame = new JFrame( "Graphe sur les splines Cubique" );
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int side = (int) (screenSize.getHeight() * 0.5);
		frame.setSize( side, side );
		frame.setLocation((int) (screenSize.getWidth() - side) / 2, (int) (screenSize.getHeight() - side) / 2);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout( new GridLayout( 1, 1 ) );
		//affichage des points
		Figure f1 = new Figure();
		f1.addPoint( new Point( -1, -1 ) );
		f1.addPoint( new Point( 1, 0.4 ) );
		f1.addPoint( new Point( 4, -0.5 ) );
		DataElementRenderer renderer1 = new PointAsDotRenderer();
		renderer1.setColor(Color.blue);
		f1.addRenderer(renderer1);

		//courbe
		Function function = new Function() {
			public Point evaluate( double t ) {
				//return new Point( t * Math.cos( t ), Math.sin( t ) );
				return new Point( t, evaluateSpline(t) );
			}
		};

		function.setDefinitionValues( 0, 20, 0.2 ); //(xn -x0)/100

		Figure f3 = function.createFigure();
		DataElementRenderer renderer3 = new ConnectedLineFigureRenderer();
		renderer3.setColor(Color.green);
		f3.addRenderer(renderer3);

		Figure figureList = new Figure();
		figureList.addFigure( f1 );
		//figureList.addFigure( f2 );
		figureList.addFigure( f3 );

		SwingPlotter plotter = new SwingPlotter();
		plotter.getGraphics2DPlotter().setFigure( figureList );
		contentPane.add(plotter);
		frame.setVisible( true );
	}



	public static void main(String[] args) {
        // Coordonnées des points de support
        double[] abscisses = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        double[] ordonnees = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};

        // Création de l'objet Spline
        Spline spline = new Spline(abscisses, ordonnees);

        // Calcul de l'intervalle délimitant les abscisses des points de support
        double minX = abscisses[0];
        double maxX = abscisses[abscisses.length - 1];
/*
        // Évaluation de la fonction d'interpolation par spline cubiques en 100 valeurs réparties régulièrement
        int nbValeurs = 100;
        double intervalle = (maxX - minX) / (nbValeurs - 1);
        double[] valeursInterpolees = new double[nbValeurs];
        for (int i = 0; i < nbValeurs; i++) {
            double val = minX + i * intervalle;
            try {
                valeursInterpolees[i] = spline.evaluate(val);
            } catch (DataOutOfRangeException e) {
                System.out.println(e.getMessage());
            }
        }

        // Affichage des valeurs interpolées
        System.out.println("Valeurs interpolées :");
        for (int i = 0; i < nbValeurs; i++) {
            System.out.println("Valeur " + (i + 1) + " : " + valeursInterpolees[i]);
        }*/
		spline.afficheGraphe();
    }

}
