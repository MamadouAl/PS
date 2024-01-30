package AlgLin;

public class SysDiagonal extends SysLin {
	

	public SysDiagonal(Matrice m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
	}
	
	/**
	 * Renvoie la résolution du système diagonal
	 */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		Matrice matrice = getMatriceSystem();
		Vecteur res = new Vecteur(matrice.nbLigne());

		for(int i = 0; i < matrice.nbLigne(); i++) { // dx = b => x = b/d avec d!=0
			if (Math.abs(matrice.getCoef(i, i)) < Matrice.EPSILON) 
                throw new IrregularSysLinException("Le système diagonal est irrégulier.");
            
			res.remplacecoef(i, secondMembre.getCoeff(i) / matrice.getCoef(i, i));
		}
		return res;
	}
	

	public static void main(String[] args) throws IrregularSysLinException {
		double tab[] = {1,2,4};
		Vecteur vecteur = new Vecteur(tab);
		
		double mat[][]= {{1,0, 0},{0,1, 0},{0,0,1}};
		Matrice matrice = new Matrice(mat);								
		SysLin sys = new SysDiagonal(matrice, vecteur);					

		Vecteur resolution = sys.resolution();
		System.out.println("resolution "+ resolution);
		
	// A revoir
		//Test qui declenche une exception
		try {
			double mat2[][] = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } };
			Matrice matrice2 = new Matrice(mat2);
			Vecteur vecteur2 = new Vecteur(new double[] { 1, 2, 3, 4});
			SysDiagonal sys2 = new SysDiagonal(matrice2, vecteur2);
			Vecteur resolution2 = sys2.resolution();
			System.out.println("resolution " + resolution2);
		} catch (IrregularSysLinException e) {
			e.printStackTrace();
		}
		
		
	}

}
