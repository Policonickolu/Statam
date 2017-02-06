package enumeration;

public enum SecteurActivite {

	AGROALIMENTAIRE("Agroalimentaire"),
	AUTRES_SERVICES("Autres services"),
	BANQUE_ASSURANCE("Banque / assurance"),
	BATIMENT_TRAVAUX_PUBLICS("Bâtiment / travaux publics"),
	BOIS_PAPIER_CARTON_IMPRIMERIE("Bois / papier / carton / imprimerie"),
	CHIMIE_PARACHIMIE("Chimie / parachimie"),
	COMMERCE_DISTRIBUTION_NEGOCE("Commerce / distribution / négoce"),
	ELECTRONIQUE_ELECTRICITE("Électronique / électricité"),
	ETUDES_ET_CONSEILS("Etudes et conseils"),
	INFORMATIQUE("Informatique"),
	MECANIQUE("Mécanique"),
	MECANIQUE_DE_PRECISION("Mécanique de précision"),
	METALLURGIE_TRAVAIL_DU_METAL("Métallurgie / travail du métal"),
	PHARMACIE("Pharmacie"),
	PLASTIQUE_CAOUTCHOUC("Plastique / caoutchouc"),
	TEXTILE_HABILLEMENT_CHAUSSURE("Textile / habillement / chaussure"),
	TRANSPORTS_LOGISTIQUE("Transports / logistique");

	private final String nom;

	SecteurActivite(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public static SecteurActivite getSecteurActivite(String str) {
		SecteurActivite[] secteurs = SecteurActivite.values();
		SecteurActivite secteur = null;

		for (SecteurActivite sec : secteurs) {
			if (sec.getNom().equals(str)) {
				secteur = sec;
			}

		}
		return secteur;
	}

}
