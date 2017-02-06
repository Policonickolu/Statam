package objet;

import java.io.Serializable;
import java.sql.Date;

import enumeration.Comparateur;

/**
 * Classe CritereJoursArret représentant la table critere de la base de données
 */
public class CritereJoursArret implements CritereInterface, Cloneable,
		Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private final String typeCritere = "Jours arrêt";
	private Comparateur comparateur = null;
	private int valeurComparee = 0;

	public CritereJoursArret() {
	}

	public CritereJoursArret(int id) {
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
		this.comparateur = comparateur;
	}

	public String getValeurComparee() {
		return this.valeurComparee + "";
	}

	public void setValeurComparee(String valeurComparee) {
		this.valeurComparee = Integer.parseInt(valeurComparee);
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param valeurComparee
	 */
	public void setAttributs(int id, String valeurComparee) {
		this.id = id;
		this.valeurComparee = Integer.parseInt(valeurComparee);
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
		this.comparateur = comparateur;
		this.valeurComparee = Integer.parseInt(valeurComparee);
	}

	/**
	 * Vérifie que le dossier répond au critère
	 * 
	 * @param dossier
	 * @return true si le dossier répond au critère
	 */
	public boolean controler(DossierIndemnisation dos) {
		boolean b = false;
		ArretTravail at = dos.getArretTravail();
		switch (comparateur) {
		case EGAL:
			b = (convertirJoursArret(at.getDebutArret(), at.getFinArret()) == this.valeurComparee);
			break;
		case INFERIEUR:
			b = (convertirJoursArret(at.getDebutArret(), at.getFinArret()) < this.valeurComparee);
			break;
		case INFERIEUR_EGAL:
			b = (convertirJoursArret(at.getDebutArret(), at.getFinArret()) <= this.valeurComparee);
			break;
		case SUPERIEUR:
			b = (convertirJoursArret(at.getDebutArret(), at.getFinArret()) > this.valeurComparee);
			break;
		case SUPERIEUR_EGAL:
			b = (convertirJoursArret(at.getDebutArret(), at.getFinArret()) >= this.valeurComparee);
			break;
		}
		return b;
	}

	public int convertirJoursArret(Date d1, Date d2) {
		return Math
				.abs(((int) ((d2.getTime() - d1.getTime()) / 1000 / 60 / 60 / 24)));
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
		if (this.valeurComparee + "" == null) {
			if (objet.getValeurComparee() != null)
				return (false);
		} else if (!(this.valeurComparee + "")
				.equals(objet.getValeurComparee())) {
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
		return "Jours d'arret " + this.comparateur.getNom() + " "
				+ this.valeurComparee;
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		CritereJoursArret copie = new CritereJoursArret();
		copie.setId(this.id);
		if (this.typeCritere != null)
			copie.setTypeCritere(new String(this.typeCritere));
		if (this.comparateur != null)
			copie.setComparateur(this.comparateur);
		if (this.valeurComparee + "" != null)
			copie.setValeurComparee(new String(this.valeurComparee + ""));
		return copie;
	}

}