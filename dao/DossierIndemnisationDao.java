package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import objet.DossierIndemnisation;

import enumeration.Etat;
import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe DossierIndemnisation Lien avec la base de
 * données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class DossierIndemnisationDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<DossierIndemnisation> cache;

	/**
	 * Constructeur
	 */
	public DossierIndemnisationDao() {
		viderCache();
	}

	/**
	 * Vide le cache.
	 */
	public void viderCache() {
		cacheActive = false;
		cache = null;
	}

	/**
	 * Crée un objet vide.
	 * 
	 * @return l'objet
	 */
	public DossierIndemnisation creerObjet() {
		return new DossierIndemnisation();
	}

	/**
	 * Récupère une instance depuis la base de données.
	 * 
	 * @param con
	 * @param id
	 * @return l'instance
	 * @throws ObjetNonTrouveException
	 * @throws SQLException
	 */
	public DossierIndemnisation getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		DossierIndemnisation objet = creerObjet();
		objet.setId(id);
		trouver(con, objet);
		return objet;
	}

	/**
	 * Recherche l'instance dans la base de données.
	 * 
	 * @param con
	 * @param objet
	 * @throws ObjetNonTrouveException
	 * @throws SQLException
	 */
	public void trouver(Connection con, DossierIndemnisation objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM dossier_indemnisation WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getId());
			requete(con, state, objet);
		} finally {
			if (state != null)
				state.close();
		}
	}

	/**
	 * Recherche toute la table de la base de données.
	 * 
	 * @param con
	 * @return la liste d'objets
	 * @throws SQLException
	 * @throws ObjetNonTrouveException
	 */
	public List<DossierIndemnisation> trouverTout(Connection con)
			throws SQLException, ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM dossier_indemnisation ORDER BY id ASC ";
		List<DossierIndemnisation> liste = requeteListe(con,
				con.prepareStatement(sql));
		cache = liste;
		cacheActive = true;
		return liste;
	}

	/**
	 * Place l'instance dans la base de données.
	 * 
	 * @param con
	 * @param objet
	 * @throws SQLException
	 */
	public synchronized void creer(Connection con, DossierIndemnisation objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO dossier_indemnisation ( arret_id, attestation_id, etat, "
					+ "indemniteJournaliere, montantTotal, ratio, "
					+ "personneEnCharge_id, dateDernierEtat) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
			state = con.prepareStatement(sql);
			if (objet.getArretTravail() != null) {
				state.setInt(1, objet.getArretTravail().getId());
			} else {
				state.setNull(1, Types.NULL);
			}
			if (objet.getAttestation() != null) {
				state.setInt(2, objet.getAttestation().getId());
			} else {
				state.setNull(2, Types.NULL);
			}
			state.setString(3, objet.getEtat().toString());
			state.setDouble(4, objet.getIndemniteJournaliere());
			state.setDouble(5, objet.getMontantTotal());
			state.setDouble(6, objet.getRatio());
			if (objet.getPersonneEnCharge() != null) {
				state.setInt(7, objet.getPersonneEnCharge().getId());
			} else {
				state.setNull(7, Types.NULL);
			}
			state.setDate(8, objet.getDateDernierEtat());
			int nbLignes = miseAJour(con, state);
			if (nbLignes != 1) {
				throw new SQLException(
						"Erreur lors de l'insertion de l'objet dans la BDD : aucun ou plusieurs objets insérés");
			}
		} finally {
			if (state != null)
				state.close();
		}
		sql = "SELECT last_insert_id()";
		try {
			state = con.prepareStatement(sql);
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId((int) resultat.getLong(1));
			} else {
				throw new SQLException(
						"Impossible de trouver l'id de l'objet inséré");
			}
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
	}

	/**
	 * Modifie l'instance dans la base de données.
	 * 
	 * @param con
	 * @param objet
	 * @throws ObjetNonTrouveException
	 * @throws SQLException
	 */
	public void modifier(Connection con, DossierIndemnisation objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE dossier_indemnisation SET arret_id = ?, attestation_id = ?, etat = ?, "
				+ "indemniteJournaliere = ?, montantTotal = ?, ratio = ?, "
				+ "personneEnCharge_id = ?, dateDernierEtat = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			if (objet.getArretTravail() != null)
				state.setInt(1, objet.getArretTravail().getId());
			else
				state.setNull(1, Types.NULL);
			if (objet.getAttestation() != null)
				state.setInt(2, objet.getAttestation().getId());
			else
				state.setNull(2, Types.NULL);
			state.setString(3, objet.getEtat().toString());
			state.setDouble(4, objet.getIndemniteJournaliere());
			state.setDouble(5, objet.getMontantTotal());
			state.setDouble(6, objet.getRatio());
			if (objet.getPersonneEnCharge() != null)
				state.setInt(7, objet.getPersonneEnCharge().getId());
			else
				state.setNull(7, Types.NULL);
			state.setDate(8, objet.getDateDernierEtat());
			state.setInt(9, objet.getId());
			int nbLignes = miseAJour(con, state);
			if (nbLignes == 0) {
				throw new ObjetNonTrouveException(
						"L'objet n'a pas pu être modifié : clé primaire introuvable");
			}
			if (nbLignes > 1) {
				throw new SQLException(
						"Erreur lors de la modification de l'objet dans la BDD : plusieurs objets modifiés");
			}
		} finally {
			if (state != null)
				state.close();
		}
	}

	/**
	 * Supprime l'instance dans la base de données.
	 *
	 * @param con
	 * @param objet
	 * @throws ObjetNonTrouveException
	 * @throws SQLException
	 */
	public void supprimer(Connection con, DossierIndemnisation objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "DELETE FROM dossier_indemnisation WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getId());
			int nbLignes = miseAJour(con, state);
			if (nbLignes == 0) {
				throw new ObjetNonTrouveException(
						"L'objet n'a pas pu être supprimé : clé primaire introuvable");
			}
			if (nbLignes > 1) {
				throw new SQLException(
						"Erreur lors de la suppression de l'objet dans la BDD : plusieurs objets supprimés");
			}
		} finally {
			if (state != null)
				state.close();
		}
	}

	/**
	 * Vide la table de la base de données.
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void supprimerTout(Connection con) throws SQLException {
		String sql = "DELETE FROM dossier_indemnisation";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			miseAJour(con, state);
		} finally {
			if (state != null)
				state.close();
		}
	}

	/**
	 * Compte le nombre de lignes dans la table de la base de données.
	 * 
	 * @param con
	 * @return le résultat
	 * @throws SQLException
	 */
	public int compter(Connection con) throws SQLException {
		if (cacheActive) {
			return cache.size();
		}
		String sql = "SELECT count(*) FROM dossier_indemnisation";
		PreparedStatement state = null;
		ResultSet resultat = null;
		int nbLignes = 0;
		try {
			state = con.prepareStatement(sql);
			resultat = state.executeQuery();
			if (resultat.next())
				nbLignes = resultat.getInt(1);
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
		return nbLignes;
	}

	/**
	 * Recupère les objets de la base de données égaux à l'objet entré en
	 * paramètre.
	 * 
	 * @param con
	 * @param objet
	 * @return la liste d'objets
	 * @throws SQLException
	 */
	public List<DossierIndemnisation> getObjetsEgaux(Connection con,
			DossierIndemnisation objet) throws SQLException {
		List<DossierIndemnisation> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM dossier_indemnisation WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getArretTravail() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND arret_id = ")
					.append(objet.getArretTravail().getId()).append(" ");
		}
		if (objet.getAttestation() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND attestation_id = ")
					.append(objet.getAttestation().getId()).append(" ");
		}
		if (objet.getEtat() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND etat LIKE '").append(objet.getEtat()).append("%' ");
		}
		if (objet.getIndemniteJournaliere() != -1) {
			if (bool) {
				bool = false;
			}
			sql.append("AND indemniteJournaliere = ")
					.append(objet.getIndemniteJournaliere()).append(" ");
		}
		if (objet.getMontantTotal() != -1) {
			if (bool) {
				bool = false;
			}
			sql.append("AND montantTotal = ").append(objet.getMontantTotal())
					.append(" ");
		}
		if (objet.getRatio() != -1) {
			if (bool) {
				bool = false;
			}
			sql.append("AND ratio = ").append(objet.getRatio()).append(" ");
		}
		if (objet.getPersonneEnCharge() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND personneEnCharge_id = ")
					.append(objet.getPersonneEnCharge().getId()).append(" ");
		}
		if (objet.getDateDernierEtat() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND dateDernierEtat = '")
					.append(objet.getDateDernierEtat()).append("' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<DossierIndemnisation>();
		else
			liste = requeteListe(con, con.prepareStatement(sql.toString()));
		return liste;
	}

	/**
	 * Mise à jour de la base de données. Exécute des requètes de mises à jour
	 * ne nécessitant pas de réponse. Vide le cache.
	 * 
	 * @param con
	 * @param state
	 * @return le résultat
	 * @throws SQLException
	 */
	protected int miseAJour(Connection con, PreparedStatement state)
			throws SQLException {
		int resultat = state.executeUpdate();
		viderCache();
		return resultat;
	}

	/**
	 * Recupère les données de la base de données pour en créer une instance.
	 * 
	 * @param con
	 * @param state
	 * @param objet
	 * @throws ObjetNonTrouveException
	 * @throws SQLException
	 */
	protected void requete(Connection con, PreparedStatement state,
			DossierIndemnisation objet) throws ObjetNonTrouveException,
			SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				if (resultat.getObject("arret_id") != null)
					objet.setArretTravail(new ArretTravailDao().getObjet(con,
							resultat.getInt("arret_id")));
				else
					objet.setArretTravail(null);
				if (resultat.getObject("attestation_id") != null)
					objet.setAttestation(new AttestationDao().getObjet(con,
							resultat.getInt("attestation_id")));
				else
					objet.setAttestation(null);
				objet.setEtat(Etat.valueOf(resultat.getString("etat")));
				objet.setIndemniteJournaliere(resultat
						.getDouble("indemniteJournaliere"));
				objet.setMontantTotal(resultat.getDouble("montantTotal"));
				objet.setRatio(resultat.getDouble("ratio"));
				if (resultat.getObject("personneEnCharge_id") != null)
					objet.setPersonneEnCharge(new CompteDao().getObjet(con,
							resultat.getInt("personneEnCharge_id")));
				else
					objet.setPersonneEnCharge(null);
				objet.setDateDernierEtat(resultat.getDate("dateDernierEtat"));
			} else {
				throw new ObjetNonTrouveException("Objet introuvable");
			}
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
	}

	/**
	 * Recupère les données de la base de données pour en créer plusieurs
	 * instances.
	 *  
	 * @param con
	 * @param state
	 * @return liste d'objet
	 * @throws SQLException
	 */
	protected List<DossierIndemnisation> requeteListe(Connection con,
			PreparedStatement state) throws SQLException {
		ArrayList<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				try {
					DossierIndemnisation obj = creerObjet();
					obj.setId(resultat.getInt("id"));
					if (resultat.getObject("arret_id") != null)
						obj.setArretTravail(new ArretTravailDao().getObjet(con,
								resultat.getInt("arret_id")));
					else
						obj.setArretTravail(null);
					if (resultat.getObject("attestation_id") != null)
						obj.setAttestation(new AttestationDao().getObjet(con,
								resultat.getInt("attestation_id")));
					else
						obj.setAttestation(null);
					obj.setEtat(Etat.valueOf(resultat.getString("etat")));
					obj.setIndemniteJournaliere(resultat
							.getDouble("indemniteJournaliere"));
					obj.setMontantTotal(resultat.getDouble("montantTotal"));
					obj.setRatio(resultat.getDouble("ratio"));
					if (resultat.getObject("personneEnCharge_id") != null)
						obj.setPersonneEnCharge(new CompteDao().getObjet(con,
								resultat.getInt("personneEnCharge_id")));
					else
						obj.setPersonneEnCharge(null);
					obj.setDateDernierEtat(resultat.getDate("dateDernierEtat"));
					liste.add(obj);
				} catch (ObjetNonTrouveException e) {
				}
			}
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
		return (List<DossierIndemnisation>) liste;
	}

}