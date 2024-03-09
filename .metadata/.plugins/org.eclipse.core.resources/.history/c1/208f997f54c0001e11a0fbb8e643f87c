package AlgLin;

public class Vecteur extends Matrice {
	/**
	 * Constructeur qui cree un vecteur de taille nbligne
	 * @param nbligne
	 */
	public Vecteur(int nbligne) {
		super(nbligne, 1);
	}

	/**
	 * Constructeur qui cree un vecteur a partir d'un tableau
	 * @param tableau : tableau de double
	 */
	public Vecteur(double[] tableau) {
		super(tableau.length, 1);
		for(int i=0; i< tableau.length; i++) {
			this.coefficient[i][0] = tableau[i];
		}
		
	}
	
	/**
	 * Constructeur qui cree un vecteur a partir d'un fichier
	 * @param fichier : fichier de double
	 * @throws Exception
	 */
	public Vecteur(String fichier) throws Exception {
		super(fichier);
		if(this.nbColonne() != 1) throw new Exception("Le fichier ne contient pas un vecteur");
	}
	
	/**
	 * Retourne la taille du vecteur
	 * @return
	 */
	public int getTaille() {
		return this.nbLigne();
	}
	
	/**
	 * Retourne le coefficient a la position pos
	 * @param pos
	 * @return
	 */
	public double getCoeff(int pos) {
		return this.getCoef(pos, 0);
	}
	
	/**
	 * Remplace le coefficient a la position pos par la valeur
	 * @param pos
	 * @param valeur
	 */
	public void remplacecoef(int pos, double valeur) {
		this.remplacecoef(pos, 0, valeur);
	}
	
	/**
	 * Permet de faire le produit de deux vecteurs
	 */
	public static Vecteur produit(Vecteur a, Vecteur b) throws Exception {
		if(a.getTaille() != b.getTaille()) 
			throw new Exception("Les deux vecteurs n'ont pas la meme taille");
		Vecteur produit = new Vecteur(a.getTaille());
		for(int i =0; i < a.getTaille(); i++) {
			produit.remplacecoef(i, a.getCoeff(i) * b.getCoeff(i));
		}
		return produit;
	}
	
	public static Vecteur difference(Vecteur a, Vecteur b) throws Exception {
		if (a.getTaille() != b.getTaille())
			throw new Exception("Les deux vecteurs n'ont pas la meme taille");
		Vecteur difference = new Vecteur(a.getTaille());
		for (int i = 0; i < a.getTaille(); i++) {
			difference.remplacecoef(i, a.getCoeff(i) - b.getCoeff(i));
		}
		return difference;
	}
	
	/**
	 * Methode d'affichage
	 */
	public String toString() {
		String res ="";
		for(int i=0; i< getTaille(); i++) {
			res += getCoeff(i) +" ";
		}
		return res;
	}
	/**
	 * Methode qui retourne la norme L1 d'un vecteur
	 */
	public static double normeL1(Vecteur vect) {
		double norme = 0.0;
		for (int i = 0; i < vect.getTaille(); i++) {
			norme += Math.abs(vect.getCoeff(i));
		}
		return norme;
	}
	
	/**
	 * Methode qui retourne la norme L2 d'un vecteur
	 */
	public static double normeL2(Vecteur vect) {
		double norme = 0.0;
		for (int i = 0; i < vect.getTaille(); i++) {
			norme += Math.pow(vect.getCoeff(i), 2);
		}
		return Math.sqrt(norme);
	}
	
	/**
     * Methode qui retourne la norme Linfini d'un vecteur
     */
	public static double normeInfini(Vecteur vect) {
		double norme = 0.0;
		for (int i = 0; i < vect.getTaille(); i++) {
			norme = Math.max(norme, Math.abs(vect.getCoeff(i)));
		}
		return norme;
	}
	
	public static void main(String[] args) throws Exception {
		double tab[] = {1,2,3};
		double tab2[] = {4,5,6};
		
		Vecteur vect1 = new Vecteur(tab);
		Vecteur vect2 = new Vecteur(tab2);
		System.out.println("vect1 : " +vect1);
		System.out.println("Coeff : " + vect1.getCoeff(1));
		
		vect1.remplacecoef(1, 5.5);
		System.out.println("vect1 modifié : " +vect1);
		System.out.println("vect2 : " +vect2);
		System.out.println("vect1 x vect2 " +produit(vect1, vect2));
	}
	
}
