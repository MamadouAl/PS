package AlgLin;

public class Thomas extends SysLin {
	// Couleurs pour les messages dans la console
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String BLUE = "\u001B[34m";
	public static final String BLACK = "\u001B[30m";
	
	public Thomas(Mat3Diag m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
	}

	/**
	 * Permet de faire la résolution d'un système tridiagonal avec la méthode de Thomas
	 */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		int n = secondMembre.nbLigne();
		if (matriceSystem.nbLigne() != 3 || matriceSystem.nbColonne() != n || n != secondMembre.nbLigne()) {
			throw new IrregularSysLinException("Taille incorrecte de la matrice ou du vecteur du second membre.");
		}
		Vecteur p = new Vecteur(n);
		Vecteur q = new Vecteur(n);
		Vecteur solution = new Vecteur(n);

		// 1- Calcul de p1 et q1
		p.remplacecoef(0, -matriceSystem.getCoeff(2, 0) / matriceSystem.getCoeff(1, 0));
		q.remplacecoef(0, secondMembre.getCoeff(0) / matriceSystem.getCoeff(1, 0));

		// 2- Calcul de pk et qk pour k allant de 2 à n - 1
		for (int k = 1; k <= n - 2; k++) {
			double beta = matriceSystem.getCoeff(0, k) * p.getCoeff(k - 1) + matriceSystem.getCoeff(1, k);
			p.remplacecoef(k, -matriceSystem.getCoeff(2, k) / beta);
			q.remplacecoef(k, (secondMembre.getCoeff(k) - matriceSystem.getCoeff(0, k) * q.getCoeff(k - 1)) / beta);
		}

		// 3- Calcul de xn
		double an = matriceSystem.getCoeff(0, n - 1);
		double bn = matriceSystem.getCoeff(1, n - 1);
		double dn = secondMembre.getCoeff(n - 1);
		solution.remplacecoef(n - 1, (dn - an * q.getCoeff(n - 2)) / (an * p.getCoeff(n - 2) + bn));

		// 4- Calcul de xk pour k allant de n - 1 à 1
		for (int k = n - 2; k >= 0; k--) {
			solution.remplacecoef(k, p.getCoeff(k) * solution.getCoeff(k + 1) + q.getCoeff(k));
		}
		return solution;
	}

	public static void main(String[] args) throws IrregularSysLinException {
		System.out.println("*** Methode de Thomas ***\n");
		//double[][] tableau = { { 4, 5, 0 }, { 6, 7, 3 }, { 0, 1, 2 } };
		double[][] tableau = {{0, -1, -1, -1}, {2,2,2,2}, {-1,-1,-1,0}};
		Mat3Diag mat3DiagA = new Mat3Diag(tableau);
		Vecteur vect = new Vecteur(new double[] { -2, -2,-2,23});
		
		Thomas thomas = new Thomas(mat3DiagA, vect);
		System.out.println("Mat3Diag A : \n" + mat3DiagA);
		System.out.println("Vecteur b : " + vect);
		Vecteur solutionX = thomas.resolution();
		System.out.println(GREEN +"Solution X : " + solutionX+ RESET);

		Vecteur Ax = Mat3Diag.produit(mat3DiagA, solutionX);
		Vecteur _b = new Vecteur(new double[] {+2, +2,+2, -23});
		System.out.println("\nMatrice produit : Ax : " + Ax);
		Vecteur Ax_b = Vecteur.addition(Ax, _b);

		// Calcul des normes
		double normeL1 = Vecteur.normeL1(Ax_b);
		double normeL2 = Vecteur.normeL2(Ax_b);
		double normeLinf = Vecteur.normeLinfini(Ax_b);

		System.out.println(BLUE+"\nNormeL1 (Ax-b) = " + normeL1);
		if (normeL1 < Matrice.EPSILON) {
			System.out.println("La norme L1 est nulle, donc Ax = b\n");
		} else {
			System.out.println(RED+"La norme L1 n'est pas nulle, donc Ax != b\n");
		}
		
		System.out.println("NormeL2 (Ax-b) = " + normeL2);
		if (normeL2 < Matrice.EPSILON) {
			System.out.println("La norme L2 est nulle, donc Ax = b\n");
		} else {
			System.out.println("La norme L2 n'est pas nulle, donc Ax != b\n");
		}
		
		System.out.println("NormeLinf (Ax-b) = " + normeLinf);
		if (normeLinf < Matrice.EPSILON) {
			System.out.println("La norme Linf est nulle, donc Ax = b\n");
		} else {
			System.out.println("La norme Linf n'est pas nulle, donc Ax != b\n");
		}

	}
}
