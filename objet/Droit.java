package objet;

import java.io.Serializable;

/**
 * Classe Droit représentant la table droit de la base de données
 */
public class Droit implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String nom = null;

	/**
	 * Constructeurs
	 */

	public Droit() {
	}

	public Droit(int id) {
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
	public boolean isEgal(Droit objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.nom == null) {
			if (objet.getNom() != null)
				return (false);
		} else if (!this.nom.equals(objet.getNom())) {
			return (false);
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
		str.append("\nDroit\n");
		str.append("id = " + this.id + "\n");
		str.append("nom = " + this.nom + "\n");
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
		Droit copie = new Droit();
		copie.setId(this.id);
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		return copie;
	}

}