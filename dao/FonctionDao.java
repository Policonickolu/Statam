package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Droit;
import objet.Fonction;

import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Fonction Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class FonctionDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Fonction> cache;

	/**
	 * Constructeur
	 */
	public FonctionDao() {
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
	public Fonction creerObjet() {
		return new Fonction();
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
	public Fonction getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Fonction objet = creerObjet();
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
	public void trouver(Connection con, Fonction objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM fonction WHERE (id = ? ) ";
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
	public List<Fonction> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM fonction ORDER BY id ASC ";
		List<Fonction> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Fonction objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO fonction ( nom) VALUES (?) ";
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNom());
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
		try {
			ArrayList<Droit> droits = objet.getDroits();
			for (int i = 0; i < droits.size(); i++) {
				sql = "INSERT INTO fonction_droit ( fonction_id, droit_id) VALUES (?, ?) ";
				state = con.prepareStatement(sql);
				state.setInt(1, objet.getId());
				state.setInt(2, droits.get(i).getId());
				int nbLignes = miseAJour(con, state);
				if (nbLignes != 1) {
					throw new SQLException(
							"Erreur lors de l'insertion de l'objet dans la BDD : aucun ou plusieurs objets insérés");
				}
			}
		} finally {
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
	public void modifier(Connection con, Fonction objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE fonction SET nom = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNom());
			state.setInt(2, objet.getId());
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
		try {
			sql = "DELETE FROM fonction_droit WHERE (fonction_id = ? ) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getId());
			int nbLignes = miseAJour(con, state);
			ArrayList<Droit> droits = objet.getDroits();
			int id = objet.getId();
			for (int i = 0; i < droits.size(); i++) {
				sql = "INSERT INTO fonction_droit ( fonction_id, droit_id) VALUES (?, ?) ";
				state = con.prepareStatement(sql);
				state.setInt(1, id);
				state.setInt(2, droits.get(i).getId());
				nbLignes = miseAJour(con, state);
				if (nbLignes != 1) {
					throw new SQLException(
							"Erreur lors de l'insertion de l'objet dans la BDD : aucun ou plusieurs objets insérés");
				}
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
	public void supprimer(Connection con, Fonction objet)
			throws ObjetNonTrouveException, SQLException {
		String sql1 = "DELETE FROM fonction WHERE (id = ? ) ";
		String sql2 = "DELETE FROM fonction_droit WHERE (fonction_id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql1);
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
			state = con.prepareStatement(sql2);
			state.setInt(1, objet.getId());
			nbLignes = miseAJour(con, state);
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
		String sql1 = "DELETE FROM fonction";
		String sql2 = "DELETE FROM fonction_droit";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql1);
			miseAJour(con, state);
			state = con.prepareStatement(sql2);
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
		String sql = "SELECT count(*) FROM fonction";
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
	public List<Fonction> getObjetsEgaux(Connection con, Fonction objet)
			throws SQLException {
		List<Fonction> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM fonction WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getNom() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND nom LIKE '").append(objet.getNom()).append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Fonction>();
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
			Fonction objet) throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setNom(resultat.getString("nom"));
				String sql = "";
				ArrayList<Droit> droits = new ArrayList<Droit>();
				sql = "SELECT droit_id FROM fonction_droit WHERE fonction_id = ? ORDER BY droit_id ASC";
				state = con.prepareStatement(sql);
				state.setInt(1, objet.getId());
				resultat = state.executeQuery();
				DroitDao droitDao = new DroitDao();
				while (resultat.next()) {
					droits.add(droitDao.getObjet(con,
							resultat.getInt("droit_id")));
				}
				objet.setDroits(droits);
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
	protected List<Fonction> requeteListe(Connection con,
			PreparedStatement state) throws SQLException {
		ArrayList<Fonction> liste = new ArrayList<Fonction>();
		ResultSet resultat1 = null;
		ResultSet resultat2 = null;
		try {
			resultat1 = state.executeQuery();
			while (resultat1.next()) {
				try {
					Fonction obj = creerObjet();
					obj.setId(resultat1.getInt("id"));
					obj.setNom(resultat1.getString("nom"));
					String sql = "";
					ArrayList<Droit> droits = new ArrayList<Droit>();
					sql = "SELECT droit_id FROM fonction_droit WHERE fonction_id = ? ORDER BY droit_id ASC";
					state = con.prepareStatement(sql);
					state.setInt(1, resultat1.getInt("id"));
					resultat2 = state.executeQuery();
					DroitDao droitDao = new DroitDao();
					while (resultat2.next()) {
						droits.add(droitDao.getObjet(con,
								resultat2.getInt("droit_id")));
					}
					obj.setDroits(droits);
					liste.add(obj);
				} catch (ObjetNonTrouveException e) {
				}
			}
		} finally {
			if (resultat1 != null)
				resultat1.close();
			if (state != null)
				state.close();
		}
		return (List<Fonction>) liste;
	}

}