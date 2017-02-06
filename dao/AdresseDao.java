package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Adresse;

import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Adresse Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class AdresseDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Adresse> cache;

	/**
	 * Constructeur
	 */
	public AdresseDao() {
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
	public Adresse creerObjet() {
		return new Adresse();
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
	public Adresse getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Adresse objet = creerObjet();
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
	public void trouver(Connection con, Adresse objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM adresse WHERE (id = ? ) ";
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
	public List<Adresse> trouverTout(Connection con) throws SQLException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM adresse ORDER BY id ASC ";
		List<Adresse> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Adresse objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO adresse ( numero, rue, code, "
					+ "ville, pays) VALUES (?, ?, ?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNumero());
			state.setString(2, objet.getRue());
			state.setString(3, objet.getCode());
			state.setString(4, objet.getVille());
			state.setString(5, objet.getPays());
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
	public void modifier(Connection con, Adresse objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE adresse SET numero = ?, rue = ?, code = ?, "
				+ "ville = ?, pays = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNumero());
			state.setString(2, objet.getRue());
			state.setString(3, objet.getCode());
			state.setString(4, objet.getVille());
			state.setString(5, objet.getPays());
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
	public void supprimer(Connection con, Adresse objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "DELETE FROM adresse WHERE (id = ? ) ";
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
		String sql = "DELETE FROM adresse";
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
		String sql = "SELECT count(*) FROM adresse";
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
	public List<Adresse> getObjetsEgaux(Connection con, Adresse objet)
			throws SQLException {
		List<Adresse> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM adresse WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getNumero() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND numero LIKE '").append(objet.getNumero())
					.append("%' ");
		}
		if (objet.getRue() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND rue LIKE '").append(objet.getRue()).append("%' ");
		}
		if (objet.getCode() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND code LIKE '").append(objet.getCode()).append("%' ");
		}
		if (objet.getVille() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND ville LIKE '").append(objet.getVille())
					.append("%' ");
		}
		if (objet.getPays() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND pays LIKE '").append(objet.getPays()).append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Adresse>();
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
			Adresse objet) throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setNumero(resultat.getString("numero"));
				objet.setRue(resultat.getString("rue"));
				objet.setCode(resultat.getString("code"));
				objet.setVille(resultat.getString("ville"));
				objet.setPays(resultat.getString("pays"));
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
	protected List<Adresse> requeteListe(Connection con, PreparedStatement state)
			throws SQLException {
		ArrayList<Adresse> liste = new ArrayList<Adresse>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				Adresse obj = creerObjet();
				obj.setId(resultat.getInt("id"));
				obj.setNumero(resultat.getString("numero"));
				obj.setRue(resultat.getString("rue"));
				obj.setCode(resultat.getString("code"));
				obj.setVille(resultat.getString("ville"));
				obj.setPays(resultat.getString("pays"));
				liste.add(obj);
			}
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
		return (List<Adresse>) liste;
	}

}