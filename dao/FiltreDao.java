package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.CritereInterface;
import objet.Filtre;

import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Filtre Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class FiltreDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Filtre> cache;

	/**
	 * Constructeur
	 */
	public FiltreDao() {
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
	public Filtre creerObjet() {
		return new Filtre();
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
	public Filtre getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Filtre objet = creerObjet();
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
	public void trouver(Connection con, Filtre objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM filtre WHERE (id = ? ) ";
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
	public List<Filtre> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM filtre ORDER BY id ASC ";
		List<Filtre> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Filtre objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO filtre ( nom, active) VALUES (?, ?) ";
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNom());
			state.setInt(2, (objet.isActive() ? 1 : 0));
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
			ArrayList<CritereInterface> criteres = objet.getCriteres();
			for (int i = 0; i < criteres.size(); i++) {
				sql = "INSERT INTO filtre_critere ( filtre_id, critere_id) VALUES (?, ?) ";
				state = con.prepareStatement(sql);
				state.setInt(1, objet.getId());
				state.setInt(2, criteres.get(i).getId());
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
	public void modifier(Connection con, Filtre objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE filtre SET nom = ?, active = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setString(1, objet.getNom());
			state.setInt(2, (objet.isActive() ? 1 : 0));
			state.setInt(3, objet.getId());
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
			sql = "DELETE FROM filtre_critere WHERE (filtre_id = ? ) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getId());
			int nbLignes = miseAJour(con, state);
			ArrayList<CritereInterface> criteres = objet.getCriteres();
			int id = objet.getId();
			for (int i = 0; i < criteres.size(); i++) {
				sql = "INSERT INTO filtre_critere ( filtre_id, critere_id) VALUES (?, ?) ";
				state = con.prepareStatement(sql);
				state.setInt(1, id);
				state.setInt(2, criteres.get(i).getId());
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
	public void supprimer(Connection con, Filtre objet)
			throws ObjetNonTrouveException, SQLException {
		String sql1 = "DELETE FROM filtre WHERE (id = ? ) ";
		String sql2 = "DELETE FROM filtre_critere WHERE (filtre_id = ? ) ";
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
		String sql1 = "DELETE FROM filtre";
		String sql2 = "DELETE FROM filtre_critere";
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
		String sql = "SELECT count(*) FROM filtre";
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
	public List<Filtre> getObjetsEgaux(Connection con, Filtre objet)
			throws SQLException {
		List<Filtre> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM filtre WHERE 1=1 ");
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
		if (objet.isActive() || !objet.isActive()) {
			if (bool) {
				bool = false;
			}
			sql.append("AND active = ").append(objet.isActive()).append(" ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Filtre>();
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
	protected void requete(Connection con, PreparedStatement state, Filtre objet)
			throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setNom(resultat.getString("nom"));
				objet.setActive((resultat.getInt("active") == 1 ? true : false));
				String sql = "";
				ArrayList<CritereInterface> criteres = new ArrayList<CritereInterface>();
				sql = "SELECT critere_id FROM filtre_critere WHERE filtre_id = ? ORDER BY critere_id ASC";
				state = con.prepareStatement(sql);
				state.setInt(1, objet.getId());
				resultat = state.executeQuery();
				CritereDao critereDao = new CritereDao();
				while (resultat.next()) {
					criteres.add(critereDao.getObjet(con,
							resultat.getInt("critere_id")));
				}
				objet.setCriteres(criteres);
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
	protected List<Filtre> requeteListe(Connection con, PreparedStatement state)
			throws SQLException {
		ArrayList<Filtre> liste = new ArrayList<Filtre>();
		ResultSet resultat1 = null;
		ResultSet resultat2 = null;
		try {
			resultat1 = state.executeQuery();
			while (resultat1.next()) {
				try {
					Filtre obj = creerObjet();
					obj.setId(resultat1.getInt("id"));
					obj.setNom(resultat1.getString("nom"));
					obj.setActive((resultat1.getInt("active") == 1 ? true
							: false));
					String sql = "";
					ArrayList<CritereInterface> criteres = new ArrayList<CritereInterface>();
					sql = "SELECT critere_id FROM filtre_critere WHERE filtre_id = ? ORDER BY critere_id ASC";
					state = con.prepareStatement(sql);
					state.setInt(1, resultat1.getInt("id"));
					resultat2 = state.executeQuery();
					CritereDao critereDao = new CritereDao();
					while (resultat2.next()) {
						criteres.add(critereDao.getObjet(con,
								resultat2.getInt("critere_id")));
					}
					obj.setCriteres(criteres);
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
		return (List<Filtre>) liste;
	}

}