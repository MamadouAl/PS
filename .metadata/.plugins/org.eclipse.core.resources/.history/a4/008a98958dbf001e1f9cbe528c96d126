package AlgLin;

public class SysTriangSup extends SysLin {

	public SysTriangSup(Matrice m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
	}

	@Override
	public Vecteur resolution() throws IrregularSysLinException {
        Matrice matrice = getMatriceSystem();
        Vecteur res = new Vecteur(matrice.nbLigne());

        for (int i = matrice.nbLigne() - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < matrice.nbLigne(); j++) {
                sum += matrice.getCoef(i, j) * res.getCoeff(j);
            }
            if (Math.abs(matrice.getCoef(i, i)) < Matrice.EPSILON) 
                throw new IrregularSysLinException("Le système diagonal est irrégulier.");
            
            res.remplacecoef(i, (secondMembre.getCoeff(i) - sum) / matrice.getCoef(i, i));
        }

        return res;
    }
	
	
	public static void main(String[] args) {
		double mat[][] = { { 1, 2, 3 }, { 0, 4, 5 }, { 0, 0, 6 } };
		Matrice matrice = new Matrice(mat);
		Vecteur vecteur = new Vecteur(new double[] {1, 2, 3});

		try {
			SysTriangSup sys = new SysTriangSup(matrice, vecteur);
			System.out.println("solution " + sys.resolution());
		} catch (IrregularSysLinException e) {
			e.printStackTrace();
		}
	}

}
