package systeme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import exception.ConnexionException;
import exception.FichierConfigurationException;

public class Connexion {

	private static String url = "jdbc:mysql://localhost/bdd_traitement_arret_travail";

	private static String identifiant = "root";

	private static String motDePasse = "";

	private static Connection connexion;

	/**
	 * Recupère une instance de connexion
	 * 
	 * @return l'instance de connexion
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public static Connection getInstance() throws ConnexionException,
			FichierConfigurationException {

		if (connexion == null) {
			try {

				getConfig();
				connexion = DriverManager.getConnection(url, identifiant,
						motDePasse);
			} catch (SQLException e) {
				throw new ConnexionException(
						"Problème de connexion au serveur.");
			}
		}
		return connexion;
	}

	/**
	 * Recupère le fichier de configuration pour obtenir l'adresse de connexion
	 * 
	 * @throws FichierConfigurationException
	 */
	private static void getConfig() throws FichierConfigurationException {

		Properties proprietes = Configuration.getConfiguration();

		url = "jdbc:mysql://" + proprietes.getProperty("adresse") + "/"
				+ proprietes.getProperty("bdd");
		identifiant = proprietes.getProperty("identifiant");
		motDePasse = proprietes.getProperty("mot_de_passe");

	}

	/**
	 * Vérifie la connexion au serveur
	 * 
	 * @return vrai si la connexion est valide
	 */
	public static boolean isValide() {
		if (connexion == null) {
			connexion = null;
			return false;
		}
		ResultSet teste = null;
		try {
			if (connexion.isClosed()) {
				connexion = null;
				return false;
			}
			teste = connexion.createStatement().executeQuery("SELECT 1");
			return teste.next();
		} catch (SQLException sql) {
			connexion = null;
			return false;
		} finally {
			if (teste != null) {
				try {
					teste.close();
				} catch (SQLException e) {
				}
			}
		}
	}

}
