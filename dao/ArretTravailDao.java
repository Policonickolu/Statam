package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.ArretTravail;

import enumeration.Motif;
import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe ArretTravail Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class ArretTravailDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<ArretTravail> cache;

	/**
	 * Constructeur
	 */
	public ArretTravailDao() {
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
	public ArretTravail creerObjet() {
		return new ArretTravail();
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
	public ArretTravail getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		ArretTravail objet = creerObjet();
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
	public void trouver(Connection con, ArretTravail objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM arret_travail WHERE (id = ? ) ";
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
	public List<ArretTravail> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM arret_travail ORDER BY id ASC ";
		List<ArretTravail> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, ArretTravail objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO arret_travail ( medecin_id, assure_id, debutArret, "
					+ "finArret, motifMedical) VALUES (?, ?, ?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getMedecin().getId());
			state.setInt(2, objet.getAssure().getId());
			state.setDate(3, objet.getDebutArret());
			state.setDate(4, objet.getFinArret());
			state.setString(5, objet.getMotifMedical().toString());
			int nbLignes = miseAJour(con, state);
			if (nbLignes != 1) {
				throw new SQLException(
						"Erreur lors de l'insertion de l'objet dans la BDD : aucun ou plusieurs objets insérés");
			}
		} finally {
			if(state != null)
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
	public void modifier(Connection con, ArretTravail objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE arret_travail SET medecin_id = ?, assure_id = ?, debutArret = ?, "
				+ "finArret = ?, motifMedical = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getMedecin().getId());
			state.setInt(2, objet.getAssure().getId());
			state.setDate(3, objet.getDebutArret());
			state.setDate(4, objet.getFinArret());
			state.setString(5, objet.getMotifMedical().toString());
			state.setInt(6, objet.getId());
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
	public void supprimer(Connection con, ArretTravail objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "DELETE FROM arret_travail WHERE (id = ? ) ";
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
		String sql = "DELETE FROM arret_travail";
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
		String sql = "SELECT count(*) FROM arret_travail";
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
	public List<ArretTravail> getObjetsEgaux(Connection con, ArretTravail objet)
			throws SQLException {
		List<ArretTravail> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM arret_travail WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getMedecin() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND medecin_id = ").append(objet.getMedecin().getId())
					.append(" ");
		}
		if (objet.getAssure() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND assure_id = ").append(objet.getAssure().getId())
					.append(" ");
		}
		if (objet.getDebutArret() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND debutArret = '").append(objet.getDebutArret())
					.append("' ");
		}
		if (objet.getFinArret() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND finArret = '").append(objet.getFinArret())
					.append("' ");
		}
		if (objet.getMotifMedical() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND motifMedical LIKE '")
					.append(objet.getMotifMedical()).append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<ArretTravail>();
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
			ArretTravail objet) throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setMedecin(new MedecinDao().getObjet(con,
						resultat.getInt("medecin_id")));
				objet.setAssure(new AssureDao().getObjet(con,
						resultat.getInt("assure_id")));
				objet.setDebutArret(resultat.getDate("debutArret"));
				objet.setFinArret(resultat.getDate("finArret"));
				objet.setMotifMedical(Motif.valueOf(resultat
						.getString("motifMedical")));
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
	protected List<ArretTravail> requeteListe(Connection con,
			PreparedStatement state) throws SQLException {
		ArrayList<ArretTravail> liste = new ArrayList<ArretTravail>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				try {
					ArretTravail obj = creerObjet();
					obj.setId(resultat.getInt("id"));
					obj.setMedecin(new MedecinDao().getObjet(con,
							resultat.getInt("medecin_id")));
					obj.setAssure(new AssureDao().getObjet(con,
							resultat.getInt("assure_id")));
					obj.setDebutArret(resultat.getDate("debutArret"));
					obj.setFinArret(resultat.getDate("finArret"));
					obj.setMotifMedical(Motif.valueOf(resultat
							.getString("motifMedical")));
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
		return (List<ArretTravail>) liste;
	}

}