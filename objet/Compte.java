package objet;

import java.io.Serializable;

/**
 * Classe Compte représentant la table compte de la base de données
 */
public class Compte implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String identifiant = null;
	private String motDePasse = null;
	private String sel = null;
	private String nom = null;
	private String prenom = null;
	private Fonction fonction = null;

	/**
	 * Constructeurs
	 */

	public Compte() {
	}

	public Compte(int id) {
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

	public String getIdentifiant() {
		return this.identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getMotDePasse() {
		return this.motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getSel() {
		return this.sel;
	}

	public void setSel(String sel) {
		this.sel = sel;
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

	public Fonction getFonction() {
		return this.fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param identifiant
	 * @param motDePasse
	 * @param sel
	 * @param nom
	 * @param prenom
	 * @param fonction
	 */
	public void setAttributs(int id, String identifiant, String motDePasse,
			String sel, String nom, String prenom, Fonction fonction) {
		this.id = id;
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.sel = sel;
		this.nom = nom;
		this.prenom = prenom;
		this.fonction = fonction;
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
	public boolean isEgal(Compte objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.identifiant == null) {
			if (objet.getIdentifiant() != null)
				return (false);
		} else if (!this.identifiant.equals(objet.getIdentifiant())) {
			return (false);
		}
		if (this.motDePasse == null) {
			if (objet.getMotDePasse() != null)
				return (false);
		} else if (!this.motDePasse.equals(objet.getMotDePasse())) {
			return (false);
		}
		if (this.sel == null) {
			if (objet.getSel() != null)
				return (false);
		} else if (!this.sel.equals(objet.getSel())) {
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
		if (this.fonction == null) {
			if (objet.getFonction() != null)
				return (false);
		} else if (!this.fonction.equals(objet.getFonction())) {
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
		str.append("\nCompte\n");
		str.append("id = " + this.id + "\n");
		str.append("identifiant = " + this.identifiant + "\n");
		str.append("motDePasse = " + this.motDePasse + "\n");
		str.append("sel = " + this.sel + "\n");
		str.append("nom = " + this.nom + "\n");
		str.append("prenom = " + this.prenom + "\n");
		str.append("fonctions = " + this.fonction + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.identifiant);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.nom + " ");
		str.append(this.prenom);
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Compte copie = new Compte();
		copie.setId(this.id);
		if (this.identifiant != null)
			copie.setIdentifiant(new String(this.identifiant));
		if (this.motDePasse != null)
			copie.setMotDePasse(new String(this.motDePasse));
		if (this.sel != null)
			copie.setSel(new String(this.sel));
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		if (this.prenom != null)
			copie.setPrenom(new String(this.prenom));
		if (this.fonction != null) {
			copie.setFonction((Fonction) this.fonction.copie());
		}
		return copie;
	}

}