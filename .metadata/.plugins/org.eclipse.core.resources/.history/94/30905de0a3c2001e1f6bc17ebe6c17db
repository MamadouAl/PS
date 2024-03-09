package AlgLin;

public class SysTriangInfUnite extends SystTriangInf {

	public SysTriangInfUnite(Matrice m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
		
		// Vérifier que les coefficients diagonaux sont égaux à 1
        for (int i = 0; i < m.nbLigne(); i++) {
            if (Math.abs(m.getCoef(i, i) - 1.0) > Matrice.EPSILON) {
				throw new IrregularSysLinException("La diagonale n'est pas égale à 1.");
            }
        }
	}
        
    public static void main(String... args) {
            double mat[][] = { {1, 0, 0 }, { 2, 1, 0 }, { 3, 4, 1 } };
            Matrice matrice = new Matrice(mat);
            Vecteur vecteur = new Vecteur(new double[] {1, 2, 3});
            
			try {
				SysTriangInfUnite sys = new SysTriangInfUnite(matrice, vecteur);
				System.out.println("solution " + sys.resolution());
			} catch (IrregularSysLinException e) {
				e.printStackTrace();
			}
    }
	}