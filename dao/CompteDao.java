package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Compte;

import exception.ObjetNonTrouveException;

/**
 * Data Access Object de la classe Compte Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class CompteDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<Compte> cache;

	/**
	 * Constructeur
	 */
	public CompteDao() {
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
	public Compte creerObjet() {
		return new Compte();
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
	public Compte getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		Compte objet = creerObjet();
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
	public void trouver(Connection con, Compte objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM compte WHERE (id = ? ) ";
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
	public List<Compte> trouverTout(Connection con) throws SQLException,
			ObjetNonTrouveException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM compte ORDER BY id ASC ";
		List<Compte> liste = requeteListe(con, con.prepareStatement(sql));
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
	public synchronized void creer(Connection con, Compte objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO compte ( fonction_id, identifiant, motDePasse, sel, "
					+ "nom, prenom) VALUES (?, ?, ?, ?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setInt(1, objet.getFonction().getId());
			state.setString(2, objet.getIdentifiant());
			state.setString(3, objet.getMotDePasse());
			state.setString(4, objet.getSel());
			state.setString(5, objet.getNom());
			state.setString(6, objet.getPrenom());
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
	public void modifier(Connection con, Compte objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE compte SET identifiant = ?, motDePasse = ?, sel = ?, "
				+ "nom = ?, prenom = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setString(1, objet.getIdentifiant());
			state.setString(2, objet.getMotDePasse());
			state.setString(3, objet.getSel());
			state.setString(4, objet.getNom());
			state.setString(5, objet.getPrenom());
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
	public void supprimer(Connection con, Compte objet)
			throws ObjetNonTrouveException, SQLException {
		String sql1 = "DELETE FROM compte WHERE (id = ? ) ";
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
		String sql1 = "DELETE FROM compte";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql1);
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
		String sql = "SELECT count(*) FROM compte";
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
	public List<Compte> getObjetsEgaux(Connection con, Compte objet)
			throws SQLException {
		List<Compte> liste;
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM compte WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getIdentifiant() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND identifiant LIKE '").append(objet.getIdentifiant())
					.append("%' ");
		}
		if (objet.getMotDePasse() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND motDePasse LIKE '").append(objet.getMotDePasse())
					.append("%' ");
		}
		if (objet.getSel() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND sel LIKE '").append(objet.getSel()).append("%' ");
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
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<Compte>();
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
	protected void requete(Connection con, PreparedStatement state, Compte objet)
			throws ObjetNonTrouveException, SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				objet.setFonction(new FonctionDao().getObjet(con,
						resultat.getInt("fonction_id")));
				objet.setIdentifiant(resultat.getString("identifiant"));
				objet.setMotDePasse(resultat.getString("motDePasse"));
				objet.setSel(resultat.getString("sel"));
				objet.setNom(resultat.getString("nom"));
				objet.setPrenom(resultat.getString("prenom"));
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
	protected List<Compte> requeteListe(Connection con, PreparedStatement state)
			throws SQLException {
		ArrayList<Compte> liste = new ArrayList<Compte>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				try {
					Compte obj = creerObjet();
					obj.setId(resultat.getInt("id"));
					obj.setFonction(new FonctionDao().getObjet(con,
							resultat.getInt("fonction_id")));
					obj.setIdentifiant(resultat.getString("identifiant"));
					obj.setMotDePasse(resultat.getString("motDePasse"));
					obj.setSel(resultat.getString("sel"));
					obj.setNom(resultat.getString("nom"));
					obj.setPrenom(resultat.getString("prenom"));
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
		return (List<Compte>) liste;
	}

}