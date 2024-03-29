package AlgLin;

public class Helder extends SysLin {


	public Helder(Matrice matrice, Vecteur secondMembre) throws IrregularSysLinException {
		super(matrice, secondMembre);
	}
	
	public void factorLDR() {
        int degre = getOrdre();
        Matrice A = getMatriceSystem();
        for (int i = 0; i < degre; i++) {
			for (int j = 0; j < i; j++) {
				// Calcul des coefficients de L
				double sumL = 0;
				for (int k = 0; k < j; k++) {
					sumL += A.getCoeff(i, k) * A.getCoeff(k, k) * A.getCoeff(k, j);
				}
				A.remplacecoef(i, j, (A.getCoeff(i, j) - sumL) / A.getCoeff(j, j));
			}

			// Calcul des coefficients de D
			double sumD = 0;
			for (int k = 0; k < i; k++) {
				sumD += A.getCoeff(i, k) * A.getCoeff(k, k) * A.getCoeff(k, i);
			}
			A.remplacecoef(i, i, A.getCoeff(i, i) - sumD);

			// Calcul des coefficients de R
			for (int j = i + 1; j < degre; j++) {
				double sumR = 0;
				for (int k = 0; k < i; k++) {
					sumR += A.getCoeff(i, k) * A.getCoeff(k, k) * A.getCoeff(k, j);
				}
				A.remplacecoef(i, j, (A.getCoeff(i, j) - sumR) / A.getCoeff(i, i));
			}
		}
    }
    
    /**
     * Permet de faire la résolution 
     */
    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur x = new Vecteur(getOrdre());
        Vecteur y = new Vecteur(getOrdre());
        Vecteur z = new Vecteur(getOrdre());
        Matrice A = new Matrice(getOrdre(), getOrdre());
        A.recopie(getMatriceSystem());
        Vecteur secondMembre = getSecondMembre();
        factorLDR();
        
        // Résolution de Ly = b
        SysTriangInfUnite L = new SysTriangInfUnite(getMatriceSystem(), secondMembre);
        y = L.resolution();
        
        // Résolution de Dz = y
        SysDiagonal D = new SysDiagonal(getMatriceSystem(), y);
        z = D.resolution();
        
        // Résolution de Rx = z
        SysTriangSupUnite R = new SysTriangSupUnite(getMatriceSystem(), z);
        x = R.resolution();
        return x;
    }

    /**
	 * Permet de faire la résolution partielle
	 */
    public Vecteur resolutionPartielle() throws IrregularSysLinException {
        Vecteur x = new Vecteur(getOrdre());
        Vecteur y = new Vecteur(getOrdre());
        Vecteur z = new Vecteur(getOrdre());
        Matrice A = new Matrice(getOrdre(), getOrdre());
        A.recopie(getMatriceSystem());
        Vecteur secondMembre = getSecondMembre();
        
        // Résolution de Ly = b
        SysTriangInfUnite L = new SysTriangInfUnite(A, secondMembre);
        y = L.resolution();        
        // Résolution de Dz = y
        SysDiagonal D = new SysDiagonal(A, y);
        z = D.resolution();        
        // Résolution de Rx = z
        SysTriangSupUnite R = new SysTriangSupUnite(A, z);
        x = R.resolution();        
        return x;
    }


    public void setSecondMembre(Vecteur newSecondMembre) {
        super.setSecondMembre(newSecondMembre);
    }

	public static void main(String[] args) throws Exception {
		// Cas 1 : Ax = b
		System.out.println("Cas 1 : Ax = b");
		Matrice matrice = new Matrice("./resources/matrice1.txt");
		Matrice A = new Matrice(matrice.nbLigne(), matrice.nbColonne());
		Vecteur secondMembre = new Vecteur("./resources/secondMembre.txt");
		A.recopie(matrice);

		Helder helder = new Helder(matrice, secondMembre);
		Vecteur solution = helder.resolution();
		System.out.println("Solution x de Ax = b : " + solution.toString());
		
		System.out.println("\n  Cas Ax - b : ");
		Vecteur Ax = Vecteur.produit(A, solution);
		System.out.println("   Ax : " + Ax.toString());
		helder.setSecondMembre(new Vecteur("./resources/secondMembre2.txt"));
		System.out.println("   -b : " + helder.getSecondMembre().toString());
		Vecteur Ax_b = Vecteur.addition(Ax, helder.getSecondMembre());
		Vecteur norme = new Vecteur(Ax_b.nbLigne());
		
		// Test de la norme
		double normeL1 = Vecteur.normeL1(Ax_b);
		System.out.println("Norme L1 : " + normeL1);
		if (normeL1 - 0.0 < Matrice.EPSILON) {
			System.out.println("\nLa norme L1 du vecteur est nulle ou très petite");
		}
		double normeL2 = Vecteur.normeL2(Ax_b);
		if (normeL2 - 0.0 < Matrice.EPSILON) {
			System.out.println("La norme L2 du vecteur est nulle ou très petite");
		}
		double normeInf = Vecteur.normeLinfini(Ax_b);
		if (normeInf - 0.0 < Matrice.EPSILON) {
			System.out.println("La norme infinie du vecteur est nulle ou très petite");
		}

		// Cas 2 : A²x = b
		System.out.println("\nCas 2 : A²x = b");
		Vecteur y = solution;//y tel que Ay =b
		Helder helder2 = new Helder(A, y); //Ax =y
		Vecteur solutionX = helder2.resolution();
		System.out.println("Solution x de A²x = b : " + solutionX.toString());
	}
}
