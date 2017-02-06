package objet;

import java.io.Serializable;

/**
 * Classe Adresse représentant la table adresse de la base de données
 */
public class Adresse implements Cloneable, Serializable {

	/**
	 * Variables représentant les champs de la table
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String numero = null;
	private String rue = null;
	private String code = null;
	private String ville = null;
	private String pays = null;

	/**
	 * Constructeurs
	 */

	public Adresse() {
	}

	public Adresse(int id) {
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

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRue() {
		return this.rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return this.pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	/**
	 * Attribue les valeurs aux variables
	 * 
	 * @param id
	 * @param numero
	 * @param rue
	 * @param code
	 * @param ville
	 * @param pays
	 */
	public void setAttributs(int id, String numero, String rue, String code,
			String ville, String pays) {
		this.id = id;
		this.numero = numero;
		this.rue = rue;
		this.code = code;
		this.ville = ville;
		this.pays = pays;
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
	public boolean isEgal(Adresse objet) {
		if (objet.getId() != this.id) {
			return (false);
		}
		if (this.numero == null) {
			if (objet.getNumero() != null)
				return (false);
		} else if (!this.numero.equals(objet.getNumero())) {
			return (false);
		}
		if (this.rue == null) {
			if (objet.getRue() != null)
				return (false);
		} else if (!this.rue.equals(objet.getRue())) {
			return (false);
		}
		if (this.code == null) {
			if (objet.getCode() != null)
				return (false);
		} else if (!this.code.equals(objet.getCode())) {
			return (false);
		}
		if (this.ville == null) {
			if (objet.getVille() != null)
				return (false);
		} else if (!this.ville.equals(objet.getVille())) {
			return (false);
		}
		if (this.pays == null) {
			if (objet.getPays() != null)
				return (false);
		} else if (!this.pays.equals(objet.getPays())) {
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
		str.append("\nAdresse :\n");
		str.append("id = " + this.id + "\n");
		str.append("numero = " + this.numero + "\n");
		str.append("rue = " + this.rue + "\n");
		str.append("code = " + this.code + "\n");
		str.append("ville = " + this.ville + "\n");
		str.append("pays = " + this.pays + "\n");
		return str.toString();
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.numero + " " + this.rue + " " + this.code + " "
				+ this.ville + " " + this.pays);
		return str.toString();
	}

	public String toStringValeur() {
		StringBuffer str = new StringBuffer();
		str.append(this.numero + " ");
		str.append(this.rue + " ");
		str.append(this.code + " ");
		str.append(this.ville + " ");
		str.append(this.pays);
		return str.toString();
	}

	/**
	 * Retourne une copie de l'objet et de ses variables.
	 * 
	 * @return l'objet copié
	 */
	public Object copie() {
		Adresse copie = new Adresse();
		copie.setId(this.id);
		if (this.numero != null)
			copie.setNumero(new String(this.numero));
		if (this.rue != null)
			copie.setRue(new String(this.rue));
		if (this.code != null)
			copie.setCode(new String(this.code));
		if (this.ville != null)
			copie.setVille(new String(this.ville));
		if (this.pays != null)
			copie.setPays(new String(this.pays));
		return copie;
	}

}