package AlgLin;

public class SysTriangSupSupUnite extends SysTriangSup {

	public SysTriangSupSupUnite(Matrice m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
		for (int i = v.getTaille() - 1; i >= 0; i--) {
			if (Math.abs(m.getCoef(i, i) - 1.0) > Matrice.EPSILON) {
				throw new IrregularSysLinException("La diagonale n'est pas égale à 1.");
			}
		}
	}
	
	public static void main(String[] args) {
		double mat[][] = { { 1, 2, 3 }, { 0, 1, 5 }, { 0, 0, 1 } };
		Matrice matrice = new Matrice(mat);
		Vecteur vecteur = new Vecteur(new double[] { 1, 2, 3 });

		try {
			SysTriangSupSupUnite sys = new SysTriangSupSupUnite(matrice, vecteur);
			System.out.println("solution " + sys.resolution());
		} catch (IrregularSysLinException e) {
			e.printStackTrace();
		}
	}

}
