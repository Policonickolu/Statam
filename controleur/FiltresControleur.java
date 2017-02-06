package controleur;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import objet.CritereInterface;
import objet.Filtre;
import systeme.Connexion;
import dao.CritereDao;
import dao.FiltreDao;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;
import exception.ObjetNonTrouveException;

public class FiltresControleur {

	private static FiltresControleur instance;
	
	/**
	 * Initialise l'instance
	 * 
	 * @return l'instance
	 */
	public static FiltresControleur getInstance() {
		if (instance == null) {
			instance = new FiltresControleur();
		}
		return instance;
	}

	/**
	 * Récupère la liste des filtres selon le paramètre
	 * 
	 * @param recherche
	 * @return La liste des filtres
	 * @throws EchecSQLException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public List<Filtre> getFiltres(String recherche) throws EchecSQLException,
			ConnexionException, FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		FiltreDao filDao = new FiltreDao();
		List<Filtre> liste = new ArrayList<Filtre>();
		try {
			liste.addAll(filDao.trouverTout(con));
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (ObjetNonTrouveException e) {
		}

		if (recherche != null) {
			Iterator<Filtre> it = liste.iterator();
			List<Filtre> liste2 = new ArrayList<Filtre>();
			while (it.hasNext()) {
				Filtre f = it.next();
				if (f.getNom().toLowerCase().trim()
						.indexOf(recherche.toLowerCase().trim()) != -1) {
					liste2.add(f);
				}
			}
			liste.clear();
			liste.addAll(liste2);
		}
		return liste;
	}

	/**
	 * Supprimer le filtre
	 * 
	 * @param filtre
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void supprimer(Filtre filtre) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		FiltreDao filDao = new FiltreDao();
		try {
			filDao.supprimer(con, filtre);
		} catch (ObjetNonTrouveException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

	/**
	 * Modifie le filtre
	 * 
	 * @param filtre
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void validerFiltre(Filtre filtre) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		FiltreDao filDao = new FiltreDao();
		try {
			filDao.modifier(con, filtre);
		} catch (ObjetNonTrouveException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

	/**
	 * Crée un nouveau filtre
	 * 
	 * @param filtre
	 * @throws EchecSQLException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public void nouveauFiltre(Filtre filtre) throws EchecSQLException,
			ConnexionException, FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		FiltreDao filDao = new FiltreDao();
		try {
			filDao.creer(con, filtre);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

	/**
	 * Recupère la liste des filtres ayant le nom entré en paramètre
	 * 
	 * @param nom
	 * @return La liste des filtres
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public List<Filtre> getFiltresParNom(String nom) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		FiltreDao filDao = new FiltreDao();
		List<Filtre> liste = new ArrayList<Filtre>();
		try {
			liste.addAll(filDao.trouverTout(con));
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (ObjetNonTrouveException e) {
		}
		if (nom != null) {
			Iterator<Filtre> it = liste.iterator();
			List<Filtre> liste2 = new ArrayList<Filtre>();
			while (it.hasNext()) {
				Filtre f = it.next();
				if (f.getNom().equals(nom)) {
					liste2.add(f);
				}
			}
			liste.clear();
			liste.addAll(liste2);
		}
		return liste;
	}

	/**
	 * Crée un nouveau critère
	 * 
	 * @param critere
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void nouveauCritere(CritereInterface critere)
			throws ConnexionException, FichierConfigurationException,
			EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		CritereDao criDao = new CritereDao();
		try {
			criDao.creer(con, critere);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

}
