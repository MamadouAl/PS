package AlgLin;

public abstract class SysLin {
	
	private int ordre;
	protected Matrice matriceSystem;
	protected Vecteur secondMembre;
	
	public SysLin(Matrice m, Vecteur v) throws IrregularSysLinException {
		if (m.nbColonne() != m.nbLigne() || m.nbLigne() != v.getTaille())
			throw new IrregularSysLinException("Probleme de taille !");
		this.matriceSystem = m;
		this.secondMembre = v;
		this.ordre = m.nbLigne();
	}

	/**
	 * Retourne l'ordre du systeme
	 */
	public int getOrdre() {
		return this.ordre;
	}
	
	/**
	 * Retourne la matrice du systeme
	 */
	public Matrice getMatriceSystem() {
		return this.matriceSystem;
	}
	
	/**
	 * Retourne le second membre du systeme
	 */
	public Vecteur getSecondMembre() {
		return this.secondMembre;
	}
	
	/**
	 * Rrenvoie la résolution du système
	 */
	public abstract Vecteur resolution() throws IrregularSysLinException;
}
