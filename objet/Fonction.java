package objet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe Fonction représentant la table fonction de la base de données
 */
public class Fonction implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String nom = null;
	private ArrayList<Droit> droits = new ArrayList<Droit>();

	/**
	 * Constructeurs
	 */

	public Fonction() {
	}

	public Fonction(int id) {
		this.id = id;
	}

	/**
	 * Accesseurs
	 */

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Droit> getDroits() {
		return this.droits;
	}

	public void setDroits(ArrayList<Droit> droits) {
		this.droits = droits;
	}

	public void addDroit(Droit droit) {
		this.droits.add(droit);
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param nom
	 * @param droits
	 */
	public void setAttributs(int id, String nom, ArrayList<Droit> droits) {
		this.id = id;
		this.nom = nom;
		this.droits = droits;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param nom
	 */
	public void setAttributs(int id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	/**
	 * Compare deux instances et retourne true si toutes les variables ont les
	 * mêmes valeurs et donc si elles relient la même ligne de la base de
	 * données
	 * 
	 * @param objet
	 *            l'objet à comparer
	 * @return true si les instances sont égales
	 */
	public boolean isEgal(Fonction objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.nom == null) {
			if (objet.getNom() != null)
				return (false);
		} else if (!this.nom.equals(objet.getNom())) {
			return (false);
		}
		if (this.droits == null) {
			if (objet.getDroits() != null)
				return (false);
		} else if (this.droits.size() == objet.getDroits().size()) {
			for (int i = 0; i < this.droits.size(); i++) {
				if (!this.droits.get(i).isEgal(objet.getDroits().get(i)))
					return (false);
			}
		}
		return true;
	}

	/**
	 * Retourne une représentation textuelle de l'objet
	 * 
	 * @return la chaîne de caractères
	 */
	public String toStringComplet() {
		StringBuffer str = new StringBuffer();
		str.append("\nFonction\n");
		str.append("id = " + this.id + "\n");
		str.append("nom = " + this.nom + "\n");
		str.append("droits = ");
		for (int i = 0; i < this.droits.size(); i++) {
			str.append(this.droits.get(i) + "\n");
		}
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.nom);
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Fonction copie = new Fonction();
		copie.setId(this.id);
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		if (this.droits != null) {
			ArrayList<Droit> droitsCopie = new ArrayList<Droit>();
			for (int i = 0; i < this.droits.size(); i++) {
				droitsCopie.add((Droit) this.droits.get(i).copie());
			}
			copie.setDroits(droitsCopie);
		}
		return copie;
	}

}