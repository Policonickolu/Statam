package controleur;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import objet.Compte;
import systeme.Connexion;
import dao.CompteDao;
import exception.CompteInexistantException;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class ConnexionControleur {

	private static ConnexionControleur instance;
	
	/**
	 * Initialise l'instance
	 * 
	 * @return l'instance
	 */
	public static ConnexionControleur getInstance() {
		if (instance == null) {
			instance = new ConnexionControleur();
		}
		return instance;
	}

	/**
	 * Recupère le compte avec l'identifiant et le mot de passe
	 * 
	 * @param id
	 * @param motDePasse
	 * @return le compte
	 * @throws EchecSQLException
	 * @throws CompteInexistantException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public Compte authentifier(String id, String motDePasse) throws EchecSQLException,
			CompteInexistantException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		CompteDao comDao = new CompteDao();
		Compte com = new Compte();
		com.setIdentifiant(id);
		List<Compte> liste = new ArrayList<Compte>();
		try {
			liste = comDao.getObjetsEgaux(con, com);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		if (liste.size() == 1) {
			Compte com2 = liste.get(0);
			if (comparerMotsDePasse(motDePasse, com2.getSel(), com2.getMotDePasse())) {
				return com2;
			} else {
				return null;
			}
		} else if (liste.size() == 0) {
			throw new CompteInexistantException("");
		} else {
			throw new EchecSQLException(
					"Erreur, veuillez recommencer sinon contacter l'administrateur.");
		}
	}

	/**
	 * Compare les mots de passe
	 * 
	 * @param mdpDonne
	 * @param sel
	 * @param mdpBDD
	 * @return vrai si les mots de passe concordent
	 */
	public boolean comparerMotsDePasse(String mdpDonne, String sel,
			String mdpBDD) {
		byte[] mot;
		MessageDigest md;
		String hashmdp = "";
		try {
			mot = (sel + mdpDonne).getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
			byte[] dig = md.digest(mot);
			BigInteger bigInt = new BigInteger(1, dig);
			hashmdp = bigInt.toString(16);
			while (hashmdp.length() < 32) {
				hashmdp = "0" + hashmdp;
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
		}
		return hashmdp.equals(mdpBDD);
	}

}
