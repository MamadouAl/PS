package AlgLin;

import java.util.Scanner;

public class HilbertMatrice extends Matrice {
	public HilbertMatrice(int ordre) {
		super(ordre, ordre);
		for (int i = 0; i < this.nbLigne(); i++) {
			for (int j = 0; j < this.nbColonne(); j++) {
				this.remplacecoef(i, j, 1.0 / (i + j + 1));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Entrez l'ordre de la matrice de Hilbert : ");
		int ordre = scanner.nextInt();

		HilbertMatrice hilbertMatrice = new HilbertMatrice(ordre);
		System.out.println(BLUE + "\nMatrice de Hilbert pour n = " + ordre + " :" + RESET);
		System.out.println(hilbertMatrice.toString());
		Matrice matrice = new Matrice(hilbertMatrice.nbLigne(), hilbertMatrice.nbColonne());
		matrice.recopie(hilbertMatrice);

		// Calcul de l'inverse
		Matrice inverse = hilbertMatrice.inverse();
		System.out.println(BLUE + "Matrice inverse :" + RESET);
		System.out.println(inverse.toString());

		// Vérification
		System.out.println(BLUE + "\n********* VERIFICATION *********\n" + RESET);
		Matrice produit = Matrice.produit(matrice, inverse); // A * A^-1
		Matrice identite = Matrice.identite(ordre);
		System.out.println(BLUE + "Produit A * A^-1 :" + RESET);
		System.out.println(produit.toString());

		System.out.println(BLUE + "********* NORMES ET CONDITIONNEMENTS *********\n" + RESET);

		Matrice difference = Matrice.addition(produit, identite.produit(-1)); // A * A^-1 - I

		double norme1 = difference.norme_1();
		System.out.println("Norme 1 :" + norme1);
		if (norme1 < Matrice.EPSILON) {
			System.out.println(GREEN + "La norme 1 est nulle\n" + RESET);
		} else {
			System.out.println(RED + "La norme 1 est non nulle\n" + RESET);
		}

		double normeInfini = difference.norme_inf();
		System.out.println("Norme infinie :" + normeInfini);
		if (normeInfini < Matrice.EPSILON) {
			System.out.println(GREEN + "La norme infinie est nulle\n" + RESET);
		} else {
			System.out.println(RED + "La norme infinie est non nulle\n" + RESET);
		}

		// Calcul des conditionnements
		double cond_1 = inverse.cond_1();
		double cond_inf = inverse.cond_inf();
		System.out.println("Conditionnement (norme 1) : " + cond_1);
		System.out.println("Conditionnement (norme INFINI) : " + cond_inf);

		scanner.close();
	}
}
