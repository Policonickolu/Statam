package objet;

import enumeration.Comparateur;

/**
 * Interface Critere représentant la table critere de la base de données qui
 * contient plusieurs sortes de critère
 */
public interface CritereInterface {

	/**
	 * Accesseurs
	 */

	public int getId();

	public void setId(int id);

	public String getTypeCritere();

	public Comparateur getComparateur();

	public void setComparateur(Comparateur comparateur);

	public String getValeurComparee();

	public void setValeurComparee(String valeurComparee);

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param valeurComparee
	 */
	public void setAttributs(int id, String valeurComparee);

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param comparateur
	 * @param valeurComparee
	 */
	public void setAttributs(int id, Comparateur comparateur,
			String valeurComparee);

	/**
	 * Vérifie que le dossier répond au critère
	 * 
	 * @param dossier
	 * @return true si le dossier répond au critère
	 */
	public boolean controler(DossierIndemnisation dossier);

	/**
	 * Compare deux instances et retourne true si toutes les variables ont les
	 * mêmes valeurs et donc si elles relient la même ligne de la base de
	 * données
	 * 
	 * @param objet
	 *            l'objet à comparer
	 * @return true si les instances sont égales
	 */
	public boolean isEgal(CritereInterface objet);

	/**
	 * Retourne une représentation textuelle de l'objet
	 * 
	 * @return la chaîne de caractères
	 */
	public String toString();

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie();

}