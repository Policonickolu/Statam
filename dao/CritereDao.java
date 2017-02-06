package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import objet.CritereAge;
import objet.CritereEmploi;
import objet.CritereInterface;
import objet.CritereJoursArret;
import objet.CritereMotifMedical;
import objet.CritereSecteurActivite;

import enumeration.Comparateur;
import exception.ObjetNonTrouveException;

/**
 * Data Access Object des classes Critere Lien avec la base de données
 * 
 * D'après les classes java du générateur de DAO, Daogen 2.4.1
 * http://titaniclinux.net/daogen/
 */
public class CritereDao {

	/**
	 * Cache
	 */
	private boolean cacheActive;
	private List<CritereInterface> cache;
	private String typeCritere;

	/**
	 * Constructeur
	 */
	public CritereDao() {
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
	public CritereInterface creerObjet() {
		switch (this.typeCritere) {
		case "Âge":
			return new CritereAge();
		case "Jours arrêt":
			return new CritereJoursArret();
		case "Emploi":
			return new CritereEmploi();
		case "Motif médical":
			return new CritereMotifMedical();
		case "Secteur activité":
			return new CritereSecteurActivite();
		default:
			return null;
		}
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
	public CritereInterface getObjet(Connection con, int id)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT typeCritere FROM critere WHERE id = ?";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			state = con.prepareStatement(sql);
			state.setInt(1, id);
			resultat = state.executeQuery();
			if (resultat.next()) {
				this.typeCritere = resultat.getString("typeCritere");
			} else
				throw new ObjetNonTrouveException("Objet introuvable");
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
		CritereInterface objet = creerObjet();
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
	public void trouver(Connection con, CritereInterface objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "SELECT * FROM critere WHERE (id = ? ) ";
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
	public List<CritereInterface> trouverTout(Connection con)
			throws SQLException {
		if (cacheActive) {
			return cache;
		}
		String sql = "SELECT * FROM critere ORDER BY id ASC ";
		List<CritereInterface> liste = requeteListe(con,
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
	public synchronized void creer(Connection con, CritereInterface objet)
			throws SQLException {
		String sql = "";
		PreparedStatement state = null;
		ResultSet resultat = null;
		try {
			sql = "INSERT INTO critere ( typeCritere, comparateur, valeurComparee) VALUES (?, ?, ?) ";
			state = con.prepareStatement(sql);
			state.setString(1, objet.getTypeCritere());
			if (objet.getComparateur() != null) {
				state.setString(2, objet.getComparateur().toString());
			} else {
				state.setNull(2, Types.NULL);
			}
			state.setString(3, objet.getValeurComparee());
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
	public void modifier(Connection con, CritereInterface objet)
			throws ObjetNonTrouveException, SQLException {
		String sql = "UPDATE critere SET typeCritere = ?, comparateur = ?, valeurComparee = ? WHERE (id = ? ) ";
		PreparedStatement state = null;
		try {
			state = con.prepareStatement(sql);
			state.setString(1, objet.getTypeCritere());
			if (objet.getComparateur() != null) {
				state.setString(2, objet.getComparateur().toString());
			} else {
				state.setNull(2, Types.NULL);
			}
			state.setString(3, objet.getValeurComparee());
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
	public void supprimer(Connection con, CritereInterface objet)
			throws ObjetNonTrouveException, SQLException {
		String sql1 = "DELETE FROM critere WHERE (id = ? ) ";
		String sql2 = "DELETE FROM filtre_critere WHERE (critere_id = ? ) ";
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
		String sql1 = "DELETE FROM critere";
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
		String sql = "SELECT count(*) FROM critere";
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
	public List<CritereInterface> getObjetsEgaux(Connection con,
			CritereInterface objet) throws SQLException {
		List<CritereInterface> liste;
		this.typeCritere = objet.getTypeCritere();
		boolean bool = true;
		StringBuffer sql = new StringBuffer("SELECT * FROM critere WHERE 1=1 ");
		if (objet.getId() != 0) {
			if (bool) {
				bool = false;
			}
			sql.append("AND id = ").append(objet.getId()).append(" ");
		}
		if (objet.getTypeCritere() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND typeCritere LIKE '").append(objet.getTypeCritere())
					.append("%' ");
		}
		if (objet.getComparateur() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND comparateur LIKE '").append(objet.getComparateur())
					.append("%' ");
		}
		if (objet.getValeurComparee() != null) {
			if (bool) {
				bool = false;
			}
			sql.append("AND valeurComparee LIKE '")
					.append(objet.getValeurComparee()).append("%' ");
		}
		sql.append("ORDER BY id ASC ");
		if (bool)
			liste = new ArrayList<CritereInterface>();
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
			CritereInterface objet) throws ObjetNonTrouveException,
			SQLException {
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			if (resultat.next()) {
				objet.setId(resultat.getInt("id"));
				if (resultat.getString("comparateur") != null)
					objet.setComparateur(Comparateur.valueOf(resultat
							.getString("comparateur")));
				else
					objet.setComparateur(null);
				objet.setValeurComparee(resultat.getString("valeurComparee"));
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
	protected List<CritereInterface> requeteListe(Connection con,
			PreparedStatement state) throws SQLException {
		ArrayList<CritereInterface> liste = new ArrayList<CritereInterface>();
		ResultSet resultat = null;
		try {
			resultat = state.executeQuery();
			while (resultat.next()) {
				CritereInterface obj = creerObjet();
				obj.setId(resultat.getInt("id"));
				if (resultat.getString("comparateur") != null)
					obj.setComparateur(Comparateur.valueOf(resultat
							.getString("comparateur")));
				else
					obj.setComparateur(null);
				obj.setValeurComparee(resultat.getString("valeurComparee"));
				liste.add(obj);
			}
		} finally {
			if (resultat != null)
				resultat.close();
			if (state != null)
				state.close();
		}
		return (List<CritereInterface>) liste;
	}

}