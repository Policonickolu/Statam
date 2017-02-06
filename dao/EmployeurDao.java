package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Employeur;

import enumeration.SecteurActivite;
import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Employeur Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class EmployeurDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Employeur> cache;

	/**
	 * Constructeur
	 */
	public EmployeurDao() {
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
	public Employeur creerObjet() {
		return new Employeur();
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
	public Employeur getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Employeur objet = creerObjet();
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
	public void trouver(Connection con, Employeur objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM employeur WHERE (id = ? ) ";
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
	public List<Employeur> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM employeur ORDER BY id ASC ";
		List<Employeur> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Employeur objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			new AdresseDao().creer(con, objet.getAdresse());
			sql = "INSERT INTO employeur ( adresse_id, nom, secteurActivite) VALUES (?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getAdresse().getId());
			state.setString(2, objet.getNom());
			state.setString(3, objet.getSecteurActivite().toString());
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
	public void modifier(Connection con, Employeur objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE employeur SET adresse_id = ?, nom = ?, secteurActivite = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			new AdresseDao().modifier(con, objet.getAdresse());
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getAdresse().getId());
			state.setString(2, objet.getNom());
			state.setString(3, objet.getSecteurActivite().toString());
			state.setInt(4, objet.getId());
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
	public void supprimer(Connection con, Employeur objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "DELETE FROM employeur WHERE (id = ? ) ";
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
		String sql1 = "SELECT adresse_id FROM employeur";
		String sql2 = "DELETE FROM employeur";
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
		String sql = "SELECT count(*) FROM employeur";
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
	public List<Employeur> getObjetsEgaux(Connection con, Employeur objet)
			throws SQLException {
		List<Employeur> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM employeur WHERE 1=1 ");
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
		if (objet.getSecteurActivite() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND secteurActivite LIKE '")
					.append(objet.getSecteurActivite()).append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Employeur>();
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
			Employeur objet) throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setAdresse(new AdresseDao().getObjet(con,
						resultat.getInt("adresse_id")));
				objet.setNom(resultat.getString("nom"));
				objet.setSecteurActivite(SecteurActivite.valueOf(resultat
						.getString("secteurActivite")));
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
	protected List<Employeur> requeteListe(Connection con,
			PreparedStatement state) throws SQLException {
		ArrayList<Employeur> liste = new ArrayList<Employeur>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				try {
					Employeur obj = creerObjet();
					obj.setId(resultat.getInt("id"));
					obj.setAdresse(new AdresseDao().getObjet(con,
							resultat.getInt("adresse_id")));
					obj.setNom(resultat.getString("nom"));
					obj.setSecteurActivite(SecteurActivite.valueOf(resultat
							.getString("secteurActivite")));
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
		return (List<Employeur>) liste;
	}

}