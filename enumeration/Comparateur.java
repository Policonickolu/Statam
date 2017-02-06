package enumeration;

public enum Comparateur {

	EGAL("="), INFERIEUR("<"), INFERIEUR_EGAL("<="), SUPERIEUR(">"), SUPERIEUR_EGAL(
			">=");

	private final String nom;

	Comparateur(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public static Comparateur getComparateur(String str) {
		Comparateur[] comparateurs = Comparateur.values();
		Comparateur comparateur = null;

		for (Comparateur com : comparateurs) {
			if (com.getNom().equals(str)) {
				comparateur = com;
			}

		}
		return comparateur;
	}

}
