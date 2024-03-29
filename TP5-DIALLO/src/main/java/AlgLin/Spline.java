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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;

import org.lucci.up.*;
import org.lucci.up.data.*;
import org.lucci.up.data.rendering.*;
import org.lucci.up.data.rendering.point.PointAsDotRenderer;
import org.lucci.up.system.Space;

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
	public double evaluateSpline(double val) throws DataOutOfRangeException {
		// Vérification si la valeur x est dans l'intervalle des abscisses des points de
		if (val < abX[0] || val > abX[abX.length - 1]) {
			throw new DataOutOfRangeException("La valeur fournie est en dehors de la plage des données d'entrée.");
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

	/**
	 * Méthode qui permet d'afficher le graphe de la fonction interpolée par spline en utilisant la librairie UP
	 */
	private static void afficheGraphe(HashMap<Double, Double> coordonnees, HashMap<Double, Double> calculCord) {
		JFrame frame = new JFrame("Graphe sur les Spline Cubique");
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int side = (int) (screenSize.getHeight() * 0.5);
		frame.setSize(side, side);
		frame.setLocation((int) (screenSize.getWidth() - side) / 2, (int) (screenSize.getHeight() - side) / 2);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(1, 1));

		SwingPlotter plotter = new SwingPlotter();

		Figure f1 = new Figure();
		for (double i : calculCord.keySet()) {
			f1.addPoint(new Point(i, calculCord.get(i)));
		}
		DataElementRenderer renderer1 = new PointAsDotRenderer();
		renderer1.setColor(Color.red);
		f1.addRenderer(renderer1);

		Figure f2 = new Figure();
		for (double i : coordonnees.keySet()) {
			f2.addPoint(new Point(i, coordonnees.get(i)));
		}
		DataElementRenderer renderer2 = new PointAsDotRenderer();
		renderer2.setColor(Color.blue);
		f2.addRenderer(renderer2);

		Figure figureList = new Figure();
		figureList.addFigure(f1);
		figureList.addFigure(f2);

		plotter.getGraphics2DPlotter().setFigure(figureList);
		Space space = plotter.getGraphics2DPlotter().getSpace();
		space.setMode(Space.PHYSICS);
		space.setBackgroundColor(Color.white);
		space.setColor(Color.black);
		space.getLegend().setText("Representation graphique : Splines Cubiques");
		space.getXDimension().getLegend().setText("abs");
		space.getYDimension().getLegend().setText("ord");

		contentPane.add(plotter);
		frame.setVisible(true);
	}


	public static void main(String[] args) throws DataOutOfRangeException, IrregularSysLinException {
		Scanner sc = new Scanner(System.in);
		System.out.println("***** SPLINE CUBIQUE  *******\nSi vous êtes sur IntelliJ, assurez-vous que le terminal soit sur \n" +
						"le repertoire courant '.../src/main/java/AlgLin'");
		System.out.print("Saisir le nom du fichier de données(Exemple : fichier.txt) -> ");
		String filename = sc.nextLine();
		sc.close();

		ArrayList<Double> abs = new ArrayList<>();
		ArrayList<Double> ord = new ArrayList<>();

		try {
			Scanner fileScanner = new Scanner(new File(filename));
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();
				if (!line.isEmpty()) {
					if (line.startsWith("#")) {
						continue;
					}
					String[] values = line.split(" ");
					abs.add(Double.parseDouble(values[0]));
					ord.add(Double.parseDouble(values[1]));
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier : '" + filename +"' est introuvable.");
			return;
		}

		double[] abscisses = Arrays.stream(abs.toArray()).mapToDouble(o -> (double) o).toArray();
		double[] ordonnees = Arrays.stream(ord.toArray()).mapToDouble(o -> (double) o).toArray();

		Spline spline = new Spline(abscisses, ordonnees);
		// Calcul des points de la courbe
		double min = abs.get(0);
		double max = abs.get(abs.size() - 1);
		double interval = (max - min) / 100;

		HashMap<Double, Double> calculCord = new HashMap<>();
		for (int i = 0; i <= 100; i++) {
			double xi = min + i * interval;
			double yi = spline.evaluateSpline(xi); // Calcul de la valeur de la fonction interpolée par spline cubique
			calculCord.put(xi, yi);
		}

		HashMap<Double, Double> coordonnees = new HashMap<>();
		for (int i = 0; i < abs.size(); i++) {
			coordonnees.put(abs.get(i), ord.get(i));
		}
		afficheGraphe(coordonnees, calculCord);

	}


}
