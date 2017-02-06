package enumeration;

public enum Etat {

	INCOMPLET("incomplet"), COMPLET("complet"), A_CONTROLER("à contrôler"), EN_CONTROLE(
			"en contrôle"), FIN_CONTROLE("fin de contrôle"), A_TRAITER_MANUELLEMENT(
			"à traiter manuellement"), EN_TRAITEMENT_MANUEL(
			"en traitement manuel"), FIN_TRAITEMENT_MANUEL(
			"fin de traitement manuel"), REFUSE("refusé"), TRAITE("traité");

	private final String nom;

	Etat(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

}
