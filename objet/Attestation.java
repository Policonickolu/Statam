package objet;

import java.io.Serializable;
import java.sql.Date;

import enumeration.Emploi;

/**
 * Classe Attestation représentant la table attestation de la base de données
 */
public class Attestation implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Assure assure = null;
	private Employeur employeur = null;
	private Date debutArret = null;
	private Date finArret = null;
	private int heuresTravaillees = 0;
	private double salairesPercus = 0;
	private Date dateEmbauche = null;
	private Emploi emploi = null;
	private boolean casSpecial = false;

	/**
	 * Constructeurs
	 */

	public Attestation() {
	}

	public Attestation(int id) {
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

	public Assure getAssure() {
		return this.assure;
	}

	public void setAssure(Assure assure) {
		this.assure = assure;
	}

	public Employeur getEmployeur() {
		return this.employeur;
	}

	public void setEmployeur(Employeur employeur) {
		this.employeur = employeur;
	}

	public Date getDebutArret() {
		return this.debutArret;
	}

	public void setDebutArret(Date debutArret) {
		this.debutArret = debutArret;
	}

	public Date getFinArret() {
		return this.finArret;
	}

	public void setFinArret(Date finArret) {
		this.finArret = finArret;
	}

	public int getHeuresTravaillees() {
		return this.heuresTravaillees;
	}

	public void setHeuresTravaillees(int heuresTravaillees) {
		this.heuresTravaillees = heuresTravaillees;
	}

	public double getSalairesPercus() {
		return this.salairesPercus;
	}

	public void setSalairesPercus(double salairesPercus) {
		this.salairesPercus = salairesPercus;
	}

	public Date getDateEmbauche() {
		return this.dateEmbauche;
	}

	public void setDateEmbauche(Date dateEmbauche) {
		this.dateEmbauche = dateEmbauche;
	}

	public Emploi getEmploi() {
		return this.emploi;
	}

	public void setEmploi(Emploi emploi) {
		this.emploi = emploi;
	}

	public boolean isCasSpecial() {
		return this.casSpecial;
	}

	public void setCasSpecial(boolean casSpecial) {
		this.casSpecial = casSpecial;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param assure
	 * @param employeur
	 * @param debutArret
	 * @param finArret
	 * @param heuresTravaillees
	 * @param salairesPercus
	 * @param dateEmbauche
	 * @param emploi
	 * @param casSpecial
	 */
	public void setAttributs(int id, Assure assure, Employeur employeur,
			Date debutArret, Date finArret, int heuresTravaillees,
			double salairesPercus, Date dateEmbauche, Emploi emploi,
			boolean casSpecial) {
		this.id = id;
		this.assure = assure;
		this.employeur = employeur;
		this.debutArret = debutArret;
		this.finArret = finArret;
		this.heuresTravaillees = heuresTravaillees;
		this.salairesPercus = salairesPercus;
		this.dateEmbauche = dateEmbauche;
		this.emploi = emploi;
		this.casSpecial = casSpecial;
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
	public boolean isEgal(Attestation objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.assure == null) {
			if (objet.getAssure() != null)
				return (false);
		} else if (!this.assure.isEgal(objet.getAssure())) {
			return (false);
		}
		if (this.employeur == null) {
			if (objet.getEmployeur() != null)
				return (false);
		} else if (!this.employeur.isEgal(objet.getEmployeur())) {
			return (false);
		}
		if (this.debutArret == null) {
			if (objet.getDebutArret() != null)
				return (false);
		} else if (!this.debutArret.equals(objet.getDebutArret())) {
			return (false);
		}
		if (this.finArret == null) {
			if (objet.getFinArret() != null)
				return (false);
		} else if (!this.finArret.equals(objet.getFinArret())) {
			return (false);
		}
		if (objet.getHeuresTravaillees() != this.heuresTravaillees) {
			return (false);
		}
		if (objet.getSalairesPercus() != this.salairesPercus) {
			return (false);
		}
		if (this.dateEmbauche == null) {
			if (objet.getDateEmbauche() != null)
				return (false);
		} else if (!this.dateEmbauche.equals(objet.getDateEmbauche())) {
			return (false);
		}
		if (this.emploi == null) {
			if (objet.getEmploi() != null)
				return (false);
		} else if (!this.emploi.equals(objet.getEmploi())) {
			return (false);
		}
		if (objet.isCasSpecial() != this.casSpecial) {
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
		str.append("\nAttestation\n");
		str.append("id = " + this.id + "\n");
		str.append("assure = " + this.assure + "\n");
		str.append("employeur = " + this.employeur + "\n");
		str.append("debutArret = " + this.debutArret + "\n");
		str.append("finArret = " + this.finArret + "\n");
		str.append("heuresTravaillees = " + this.heuresTravaillees + "\n");
		str.append("salairesPercus = " + this.salairesPercus + "\n");
		str.append("dateEmbauche = " + this.dateEmbauche + "\n");
		str.append("emploi = " + this.emploi + "\n");
		str.append("casSpecial = " + this.casSpecial + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.id);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.assure.toStringValeur() + " ");
		str.append(this.employeur.toStringValeur() + " ");
		str.append(this.debutArret + " ");
		str.append(this.finArret + " ");
		str.append(this.dateEmbauche + " ");
		str.append(this.emploi.getNom());
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Attestation copie = new Attestation();
		copie.setId(this.id);
		if (this.assure != null)
			copie.setAssure((Assure) this.assure.copie());
		if (this.employeur != null)
			copie.setEmployeur((Employeur) this.employeur.copie());
		if (this.debutArret != null)
			copie.setDebutArret((Date) this.debutArret.clone());
		if (this.finArret != null)
			copie.setFinArret((Date) this.finArret.clone());
		copie.setHeuresTravaillees(this.heuresTravaillees);
		copie.setSalairesPercus(this.salairesPercus);
		if (this.dateEmbauche != null)
			copie.setDateEmbauche((Date) this.dateEmbauche.clone());
		if (this.emploi != null)
			copie.setEmploi(this.emploi);
		copie.setCasSpecial(this.casSpecial);
		return copie;
	}

}