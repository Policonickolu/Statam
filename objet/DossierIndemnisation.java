package objet;

import java.io.Serializable;
import java.sql.Date;

import systeme.DateActuelle;

import enumeration.Etat;

/**
 * Classe DossierIndemnisation représentant la table dossier_indemnisation de la
 * base de données
 */
public class DossierIndemnisation implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private ArretTravail arretTravail = null;
	private Attestation attestation = null;
	private Etat etat = null;
	private double indemniteJournaliere = -1;
	private double montantTotal = -1;
	private double ratio = -1;
	private Compte personneEnCharge = null;
	private Date dateDernierEtat = null;

	/**
	 * Constructeurs
	 */

	public DossierIndemnisation() {
	}

	public DossierIndemnisation(int id) {
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

	public ArretTravail getArretTravail() {
		return this.arretTravail;
	}

	public void setArretTravail(ArretTravail arretTravail) {
		this.arretTravail = arretTravail;
	}

	public Attestation getAttestation() {
		return this.attestation;
	}

	public void setAttestation(Attestation attestation) {
		this.attestation = attestation;
	}

	public Etat getEtat() {
		return this.etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public double getIndemniteJournaliere() {
		return this.indemniteJournaliere;
	}

	public void setIndemniteJournaliere(double indemniteJournaliere) {
		this.indemniteJournaliere = indemniteJournaliere;
	}

	public double getMontantTotal() {
		return this.montantTotal;
	}

	public void setMontantTotal(double montantTotal) {
		this.montantTotal = montantTotal;
	}

	public double getRatio() {
		return this.ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public Compte getPersonneEnCharge() {
		return this.personneEnCharge;
	}

	public void setPersonneEnCharge(Compte personneEnCharge) {
		this.personneEnCharge = personneEnCharge;
	}

	public Date getDateDernierEtat() {
		return this.dateDernierEtat;
	}

	public void setDateDernierEtat(Date dateDernierEtat) {
		this.dateDernierEtat = dateDernierEtat;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param arretTravail
	 * @param attestation
	 * @param etat
	 * @param indemniteJournaliere
	 * @param montantTotal
	 * @param ratio
	 * @param personneEnCharge
	 * @param dateDernierEtat
	 */
	public void setAttributs(int id, ArretTravail arretTravail,
			Attestation attestation, Etat etat, double indemniteJournaliere,
			double montantTotal, double ratio, Compte personneEnCharge,
			Date dateDernierEtat) {
		this.id = id;
		this.arretTravail = arretTravail;
		this.attestation = attestation;
		this.etat = etat;
		this.indemniteJournaliere = indemniteJournaliere;
		this.montantTotal = montantTotal;
		this.ratio = ratio;
		this.personneEnCharge = personneEnCharge;
		this.dateDernierEtat = dateDernierEtat;
	}

	/**
	 * Calcul automatique des indemnités
	 */
	public void traitementAutomatique() {
		if (this.etat == Etat.FIN_TRAITEMENT_MANUEL) {
			if (this.attestation.getHeuresTravaillees() >= 200) {
				this.ratio = 1;
				this.indemniteJournaliere = (this.attestation
						.getSalairesPercus() / 180) * this.ratio;
				this.montantTotal = this.indemniteJournaliere
						* ((this.attestation.getFinArret().getTime() - this.attestation
								.getDebutArret().getTime()) / 1000 / 60 / 60 / 24);
				this.etat = Etat.TRAITE;
			} else {
				this.etat = Etat.REFUSE;
			}
			this.dateDernierEtat = DateActuelle.getDateActuelle();
		}
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
	public boolean isEgal(DossierIndemnisation objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.arretTravail == null) {
			if (objet.getArretTravail() != null)
				return (false);
		} else if (!this.arretTravail.isEgal(objet.getArretTravail())) {
			return (false);
		}
		if (this.attestation == null) {
			if (objet.getAttestation() != null)
				return (false);
		} else if (!this.attestation.isEgal(objet.getAttestation())) {
			return (false);
		}
		if (this.etat == null) {
			if (objet.getEtat() != null)
				return (false);
		} else if (!this.etat.equals(objet.getEtat())) {
			return (false);
		}
		if (objet.getIndemniteJournaliere() != this.indemniteJournaliere) {
			return (false);
		}
		if (objet.getMontantTotal() != this.montantTotal) {
			return (false);
		}
		if (objet.getRatio() != this.ratio) {
			return (false);
		}
		if (this.personneEnCharge == null) {
			if (objet.getPersonneEnCharge() != null)
				return (false);
		} else if (!this.personneEnCharge.isEgal(objet.getPersonneEnCharge())) {
			return (false);
		}
		if (this.dateDernierEtat == null) {
			if (objet.getDateDernierEtat() != null)
				return (false);
		} else if (!this.dateDernierEtat.equals(objet.getDateDernierEtat())) {
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
		str.append("\nDossierIndemnisation\n");
		str.append("id = " + this.id + "\n");
		str.append("arretTravail = " + this.arretTravail + "\n");
		str.append("attestation = " + this.attestation + "\n");
		str.append("etat = " + this.etat + "\n");
		str.append("indemniteJournaliere = " + this.indemniteJournaliere + "\n");
		str.append("montantTotal = " + this.montantTotal + "\n");
		str.append("ratio = " + this.ratio + "\n");
		str.append("personneEnCharge = " + this.personneEnCharge + "\n");
		str.append("dateDernierEtat = " + this.dateDernierEtat + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Dos " + this.id);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.id + " ");
		if (this.arretTravail != null)
			str.append(this.arretTravail.toStringValeur() + " ");
		if (this.attestation != null)
			str.append(this.attestation.toStringValeur() + " ");
		if (this.personneEnCharge != null)
			str.append(this.personneEnCharge.toStringValeur());
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		DossierIndemnisation copie = new DossierIndemnisation();
		copie.setId(this.id);
		if (this.arretTravail != null)
			copie.setArretTravail((ArretTravail) this.arretTravail.copie());
		if (this.attestation != null)
			copie.setAttestation((Attestation) this.attestation.copie());
		if (this.etat != null)
			copie.setEtat(this.etat);
		copie.setIndemniteJournaliere(this.indemniteJournaliere);
		copie.setMontantTotal(this.montantTotal);
		copie.setRatio(this.ratio);
		if (this.personneEnCharge != null)
			copie.setPersonneEnCharge((Compte) this.personneEnCharge.copie());
		if (this.dateDernierEtat != null)
			copie.setDateDernierEtat((Date) this.dateDernierEtat.clone());
		return copie;
	}

}