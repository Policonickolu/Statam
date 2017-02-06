package enumeration;

public enum Job {

	VISUALISATION_ETAT("Visualisation Etat"), TRAITEMENT_MANUEL(
			"Traitement Manuel"), GESTION_SYSTEME_CONTROLE(
			"Gestion Systme Contrôle"), CREATION_DOSSIER("Création Dossier"), CREATION_COMPTE(
			"Création Compte"), CONTROLE_DOSSIER("Contrôle Dossier");

	private final String nom;

	Job(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

}
