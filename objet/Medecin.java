package objet;

import java.io.Serializable;

/**
 * Classe Medecin représentant la table medecin de la base de données
 */
public class Medecin implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Adresse adresse = null;
	private String nom = null;
	private String prenom = null;
	private String specialite = null;

	/**
	 * Constructeurs
	 */

	public Medecin() {
	}

	public Medecin(int id) {
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

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getSpecialite() {
		return this.specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param adresse
	 * @param nom
	 * @param prenom
	 * @param specialite
	 */
	public void setAttributs(int id, Adresse adresse, String nom,
			String prenom, String specialite) {
		this.id = id;
		this.adresse = adresse;
		this.nom = nom;
		this.prenom = prenom;
		this.specialite = specialite;
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
	public boolean isEgal(Medecin objet) {
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
		if (this.prenom == null) {
			if (objet.getPrenom() != null)
				return (false);
		} else if (!this.prenom.equals(objet.getPrenom())) {
			return (false);
		}
		if (this.specialite == null) {
			if (objet.getSpecialite() != null)
				return (false);
		} else if (!this.specialite.equals(objet.getSpecialite())) {
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
		str.append("\nMedecin\n");
		str.append("id = " + this.id + "\n");
		str.append("adresse = " + this.adresse + "\n");
		str.append("nom = " + this.nom + "\n");
		str.append("prenom = " + this.prenom + "\n");
		str.append("specialite = " + this.specialite + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.prenom + " " + this.nom);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.nom + " ");
		str.append(this.prenom + " ");
		str.append(this.specialite);
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Medecin copie = new Medecin();
		copie.setId(this.id);
		if (this.adresse != null)
			copie.setAdresse((Adresse) this.adresse.copie());
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		if (this.prenom != null)
			copie.setPrenom(new String(this.prenom));
		if (this.specialite != null)
			copie.setSpecialite(new String(this.specialite));
		return copie;
	}

}