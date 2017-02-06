package objet;

import java.io.Serializable;

import enumeration.SecteurActivite;

/**
 * Classe Employeur représentant la table employeur de la base de données
 */
public class Employeur implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Adresse adresse = null;
	private String nom = null;
	private SecteurActivite secteurActivite = null;

	/**
	 * Constructeurs
	 */

	public Employeur() {
	}

	public Employeur(int id) {
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

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public SecteurActivite getSecteurActivite() {
		return this.secteurActivite;
	}

	public void setSecteurActivite(SecteurActivite secteurActivite) {
		this.secteurActivite = secteurActivite;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param adresse
	 * @param nom
	 * @param secteurActivite
	 */
	public void setAttributs(int id, Adresse adresse, String nom,
			SecteurActivite secteurActivite) {
		this.id = id;
		this.adresse = adresse;
		this.nom = nom;
		this.secteurActivite = secteurActivite;
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
	public boolean isEgal(Employeur objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.adresse == null) {
			if (objet.getAdresse() != null)
				return (false);
		} else if (!this.adresse.isEgal(objet.getAdresse())) {
			return (false);
		}
		if (this.nom == null) {
			if (objet.getNom() != null)
				return (false);
		} else if (!this.nom.equals(objet.getNom())) {
			return (false);
		}
		if (this.secteurActivite == null) {
			if (objet.getSecteurActivite() != null)
				return (false);
		} else if (!this.secteurActivite.equals(objet.getSecteurActivite())) {
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
		str.append("\nEmployeur\n");
		str.append("id = " + this.id + "\n");
		str.append("adresse = " + this.adresse + "\n");
		str.append("nom = " + this.nom + "\n");
		str.append("secteurActivite = " + this.secteurActivite + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.nom);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.nom + " ");
		str.append(this.secteurActivite.getNom());
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Employeur copie = new Employeur();
		copie.setId(this.id);
		if (this.adresse != null)
			copie.setAdresse((Adresse) this.adresse.copie());
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		if (this.secteurActivite != null)
			copie.setSecteurActivite(this.secteurActivite);
		return copie;
	}

}