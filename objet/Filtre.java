package objet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe Filtre représentant la table filtre de la base de données
 */
public class Filtre implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String nom = null;
	private boolean active = false;
	private ArrayList<CritereInterface> criteres = new ArrayList<CritereInterface>();

	/**
	 * Constructeurs
	 */

	public Filtre() {
	}

	public Filtre(int id) {
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

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ArrayList<CritereInterface> getCriteres() {
		return this.criteres;
	}

	public void setCriteres(ArrayList<CritereInterface> criteres) {
		this.criteres = criteres;
	}

	public void addCritere(CritereInterface critere) {
		this.criteres.add(critere);
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param nom
	 * @param active
	 * @param criteres
	 */
	public void setAttributs(int id, String nom, boolean active,
			ArrayList<CritereInterface> criteres) {
		this.id = id;
		this.nom = nom;
		this.active = active;
		this.criteres = criteres;
	}

	public boolean controler(DossierIndemnisation dos) {
		Iterator<CritereInterface> it = criteres.iterator();
		while (it.hasNext()) {
			CritereInterface cri = it.next();
			if (!cri.controler(dos))
				return false;
		}
		return true;
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
	public boolean isEgal(Filtre objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.nom == null) {
			if (objet.getNom() != null)
				return (false);
		} else if (!this.nom.equals(objet.getNom())) {
			return (false);
		}
		if (objet.isActive() != this.active) {
			return (false);
		}
		if (this.criteres == null) {
			if (objet.getCriteres() != null)
				return (false);
		} else if (this.criteres.size() == objet.getCriteres().size()) {
			for (int i = 0; i < this.criteres.size(); i++) {
				if (!this.criteres.get(i).isEgal(objet.getCriteres().get(i)))
					return (false);
			}
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
		str.append("\nFiltre\n");
		str.append("id = " + this.id + "\n");
		str.append("nom = " + this.nom + "\n");
		str.append("active = " + this.active + "\n");
		str.append("criteres = ");
		for (int i = 0; i < this.criteres.size(); i++) {
			str.append(this.criteres.get(i) + "\n");
		}
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
		Filtre copie = new Filtre();
		copie.setId(this.id);
		if (this.nom != null)
			copie.setNom(new String(this.nom));
		copie.setActive(this.active);
		if (this.criteres != null) {
			ArrayList<CritereInterface> criteresCopie = new ArrayList<CritereInterface>();
			for (int i = 0; i < this.criteres.size(); i++) {
				criteresCopie.add((CritereInterface) this.criteres.get(i)
						.copie());
			}
			copie.setCriteres(criteresCopie);
		}
		return copie;
	}

}