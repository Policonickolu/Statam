package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Medecin;

import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Medecin Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class MedecinDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Medecin> cache;

	/**
	 * Constructeur
	 */
	public MedecinDao() {
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
	public Medecin creerObjet() {
		return new Medecin();
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
	public Medecin getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Medecin objet = creerObjet();
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
	public void trouver(Connection con, Medecin objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM medecin WHERE (id = ? ) ";
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
	public List<Medecin> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM medecin ORDER BY id ASC ";
		List<Medecin> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Medecin objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			new AdresseDao().creer(con, objet.getAdresse());
			sql = "INSERT INTO medecin ( adresse_id, nom, prenom, "
					+ "specialite) VALUES (?, ?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getAdresse().getId());
			state.setString(2, objet.getNom());
			state.setString(3, objet.getPrenom());
			state.setString(4, objet.getSpecialite());
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
	public void modifier(Connection con, Medecin objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE medecin SET adresse_id = ?, nom = ?, prenom = ?, "
				+ "specialite = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			new AdresseDao().modifier(con, objet.getAdresse());
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getAdresse().getId());
			state.setString(2, objet.getNom());
			state.setString(3, objet.getPrenom());
			state.setString(4, objet.getSpecialite());
			state.setInt(5, objet.getId());
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
	public void supprimer(Connection con, Medecin objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "DELETE FROM medecin WHERE (id = ? ) ";
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
			new AdresseDao().supprimer(con, objet.getAdresse());
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
		String sql1 = "SELECT adresse_id FROM medecin";
		String sql2 = "DELETE FROM medecin";
		String sql3 = "DELETE FROM adresse WHERE id = ?";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			state = con.prepareStatement(sql1);
			resultat = state.executeQuery();
			state = con.prepareStatement(sql2);
			miseAJour(con, state);
			while (resultat.next()) {
				state = con.prepareStatement(sql3);
				state.setInt(1, resultat.getInt("adresse_id"));
				miseAJour(con, state);
			}
		} finally {
			if (resultat != null)
				resultat.close();
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
		String sql = "SELECT count(*) FROM medecin";
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
	public List<Medecin> getObjetsEgaux(Connection con, Medecin objet)
			throws SQLException {
		List<Medecin> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM medecin WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getAdresse() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND adresse_id = ").append(objet.getAdresse().getId())
					.append(" ");
		}
		if (objet.getNom() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND nom LIKE '").append(objet.getNom()).append("%' ");
		}
		if (objet.getPrenom() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND prenom LIKE '").append(objet.getPrenom())
					.append("%' ");
		}
		if (objet.getSpecialite() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND specialite LIKE '").append(objet.getSpecialite())
					.append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Medecin>();
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
			Medecin objet) throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setAdresse(new AdresseDao().getObjet(con,
						resultat.getInt("adresse_id")));
				objet.setNom(resultat.getString("nom"));
				objet.setPrenom(resultat.getString("prenom"));
				objet.setSpecialite(resultat.getString("specialite"));
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
	protected List<Medecin> requeteListe(Connection con, PreparedStatement state)
			throws SQLException {
		ArrayList<Medecin> liste = new ArrayList<Medecin>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				try {
					Medecin obj = creerObjet();
					obj.setId(resultat.getInt("id"));
					obj.setAdresse(new AdresseDao().getObjet(con,
							resultat.getInt("adresse_id")));
					obj.setNom(resultat.getString("nom"));
					obj.setPrenom(resultat.getString("prenom"));
					obj.setSpecialite(resultat.getString("specialite"));
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
		return (List<Medecin>) liste;
	}

}