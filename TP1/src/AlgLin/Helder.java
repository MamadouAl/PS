package AlgLin;

public class Helder extends SysLin {

	public Helder(Matrice m, Vecteur v) throws IrregularSysLinException {
		super(m, v);
	}
	
	public void factorLDR() throws IrregularSysLinException {
		SysTriangInfUnite L = new SysTriangInfUnite(matriceSystem, secondMembre); // Ly = b
		SysDiagonal D = new SysDiagonal(getMatriceSystem(), L.resolution()); // Dz = y
		SysTriangSupSupUnite R = new SysTriangSupSupUnite(matriceSystem, D.resolution()); // Rx = z
		
		 Matrice.verif_produit(matriceSystem, L.matriceSystem); // on verifie que le produit est possible
        Matrice.produit(matriceSystem, L.matriceSystem); // on effectue le produit matriceSystem * L
        Matrice.verif_produit(matriceSystem, D.matriceSystem);
        Matrice.produit(matriceSystem, D.matriceSystem); // on effectue le produit matriceSystem * D
        Matrice.verif_produit(matriceSystem, R.matriceSystem); 
        Matrice.produit(matriceSystem, R.matriceSystem);		// on effectue le produit matriceSystem * R
	}

	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		factorLDR(); //on factorise la matrice en LDLx = b
		SysTriangInfUnite L = new SysTriangInfUnite(matriceSystem, getSecondMembre()); // ly = b
		SysDiagonal D = new SysDiagonal(matriceSystem, L.resolution()); // dz = y
		SysTriangSupSupUnite R = new SysTriangSupSupUnite(matriceSystem, D.resolution()); // rx = z
		return R.resolution();
	}

}
