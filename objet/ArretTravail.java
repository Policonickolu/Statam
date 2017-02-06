package objet;

import java.io.Serializable;
import java.sql.Date;

import enumeration.Motif;

/**
 * Classe ArretTravail représentant la table arret_travail de la base de données
 */
public class ArretTravail implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private Medecin medecin = null;
	private Assure assure = null;
	private Date debutArret = null;
	private Date finArret = null;
	private Motif motifMedical = null;

	/**
	 * Constructeurs
	 */

	public ArretTravail() {
	}

	public ArretTravail(int id) {
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

	public Medecin getMedecin() {
		return this.medecin;
	}

	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}

	public Assure getAssure() {
		return this.assure;
	}

	public void setAssure(Assure assure) {
		this.assure = assure;
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

	public Motif getMotifMedical() {
		return this.motifMedical;
	}

	public void setMotifMedical(Motif motifMedical) {
		this.motifMedical = motifMedical;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param medecin
	 * @param assure
	 * @param debutArret
	 * @param finArret
	 * @param motifMedical
	 */
	public void setAttributs(int id, Medecin medecin, Assure assure,
			Date debutArret, Date finArret, Motif motifMedical) {
		this.id = id;
		this.medecin = medecin;
		this.assure = assure;
		this.debutArret = debutArret;
		this.finArret = finArret;
		this.motifMedical = motifMedical;
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
	public boolean isEgal(ArretTravail objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.medecin == null) {
			if (objet.getMedecin() != null)
				return (false);
		} else if (!this.medecin.isEgal(objet.getMedecin())) {
			return (false);
		}
		if (this.assure == null) {
			if (objet.getAssure() != null)
				return (false);
		} else if (!this.assure.isEgal(objet.getAssure())) {
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
		if (this.motifMedical == null) {
			if (objet.getMotifMedical() != null)
				return (false);
		} else if (!this.motifMedical.equals(objet.getMotifMedical())) {
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
		str.append("\nArretTravail\n");
		str.append("id = " + this.id + "\n");
		str.append("medecin = " + this.medecin + "\n");
		str.append("assure = " + this.assure + "\n");
		str.append("debutArret = " + this.debutArret + "\n");
		str.append("finArret = " + this.finArret + "\n");
		str.append("motifMedical = " + this.motifMedical + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.id);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.medecin.toStringValeur() + " ");
		str.append(this.assure.toStringValeur() + " ");
		str.append(this.debutArret + " ");
		str.append(this.finArret + " ");
		str.append(this.motifMedical.getNom());
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		ArretTravail copie = new ArretTravail();
		copie.setId(this.id);
		if (this.medecin != null)
			copie.setMedecin((Medecin) this.medecin.copie());
		if (this.assure != null)
			copie.setAssure((Assure) this.assure.copie());
		if (this.debutArret != null)
			copie.setDebutArret((Date) this.debutArret.clone());
		if (this.finArret != null)
			copie.setFinArret((Date) this.finArret.clone());
		if (this.motifMedical != null)
			copie.setMotifMedical(this.motifMedical);
		return copie;
	}

}