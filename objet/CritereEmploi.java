package objet;

import java.io.Serializable;

import enumeration.Comparateur;
import enumeration.Emploi;

/**
 * Classe CritereEmploi représentant la table critere de la base de données
 */
public class CritereEmploi implements CritereInterface, Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private final String typeCritere = "Emploi";
	private final Comparateur comparateur = null;
	private Emploi valeurComparee = null;

	public CritereEmploi() {
	}

	public CritereEmploi(int id) {
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

	public String getTypeCritere() {
		return this.typeCritere;
	}

	public void setTypeCritere(String typeCritere) {
	}

	public Comparateur getComparateur() {
		return this.comparateur;
	}

	public void setComparateur(Comparateur comparateur) {
	}

	public String getValeurComparee() {
		return this.valeurComparee + "";
	}

	public void setValeurComparee(String valeurComparee) {
		this.valeurComparee = Emploi.valueOf(valeurComparee);
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param valeurComparee
	 */
	public void setAttributs(int id, String valeurComparee) {
		this.id = id;
		this.valeurComparee = Emploi.valueOf(valeurComparee);
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param comparateur
	 * @param valeurComparee
	 */
	public void setAttributs(int id, Comparateur comparateur,
			String valeurComparee) {
		this.id = id;
		this.valeurComparee = Emploi.valueOf(valeurComparee);
	}

	/**
	 * Vérifie que le dossier répond au critère
	 * 
	 * @param dossier
	 * @return true si le dossier répond au critère
	 */
	public boolean controler(DossierIndemnisation dos) {
		return dos.getAttestation().getEmploi() == this.valeurComparee;
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
	public boolean isEgal(CritereInterface objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.typeCritere == null) {
			if (objet.getTypeCritere() != null)
				return (false);
		} else if (!this.typeCritere.equals(objet.getTypeCritere())) {
			return (false);
		}
		if (this.comparateur == null) {
			if (objet.getComparateur() != null)
				return (false);
		} else if (!this.comparateur.equals(objet.getComparateur())) {
			return (false);
		}
		if (this.valeurComparee == null) {
			if (objet.getValeurComparee() != null)
				return (false);
		} else if (!this.valeurComparee.equals(Emploi.valueOf(objet
				.getValeurComparee()))) {
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
		str.append("\nCritere\n");
		str.append("id = " + this.id + "\n");
		str.append("typeCritere = " + this.typeCritere + "\n");
		str.append("comparateur = " + this.comparateur + "\n");
		str.append("valeurComparee = " + this.valeurComparee + "\n");
		return str.toString();
	}

	public String toString() {
		return "Emploi : " + this.valeurComparee.getNom();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		CritereEmploi copie = new CritereEmploi();
		copie.setId(this.id);
		if (this.typeCritere != null)
			copie.setTypeCritere(new String(this.typeCritere));
		if (this.comparateur != null)
			copie.setComparateur(this.comparateur);
		if (this.valeurComparee != null)
			copie.setValeurComparee(this.valeurComparee.toString());
		return copie;
	}

}