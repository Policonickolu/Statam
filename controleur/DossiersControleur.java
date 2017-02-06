package controleur;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import objet.Assure;
import objet.Compte;
import objet.DossierIndemnisation;
import systeme.Connexion;
import systeme.DateActuelle;
import dao.DossierIndemnisationDao;
import enumeration.Etat;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;
import exception.ObjetNonTrouveException;

public class DossiersControleur {

	private static DossiersControleur instance;
	
	/**
	 * Initialise l'instance
	 * 
	 * @return l'instance
	 */
	public static DossiersControleur getInstance() {
		if (instance == null) {
			instance = new DossiersControleur();
		}
		return instance;
	}

	/**
	 * Recupère les dossiers selon les paramètres
	 * 
	 * @param etat
	 * @param date
	 * @param str
	 * @param assure
	 * @param compte
	 * @return La liste des dossiers
	 * @throws ConnexionException
	 * @throws EchecSQLException
	 * @throws FichierConfigurationException
	 */
	public List<DossierIndemnisation> getDossiers(Etat etat, Date date,
			String str, Assure assure, Compte compte)
			throws ConnexionException, EchecSQLException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		DossierIndemnisation dos = new DossierIndemnisation();
		if (etat != null)
			dos.setEtat(etat);
		if (compte != null)
			dos.setPersonneEnCharge(compte);
		List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
		try {
			if (etat != null || compte != null) {
				liste.addAll(dosDao.getObjetsEgaux(con, dos));
			} else {
				liste.addAll(dosDao.trouverTout(con));
			}
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (ObjetNonTrouveException e) {
		}
		if (str != null) {
			Iterator<DossierIndemnisation> it = liste.iterator();
			List<DossierIndemnisation> liste2 = new ArrayList<DossierIndemnisation>();
			while (it.hasNext()) {
				DossierIndemnisation d = it.next();
				if (d.toStringValeur().toLowerCase().trim()
						.indexOf(str.toLowerCase().trim()) != -1) {
					liste2.add(d);
				}
			}
			liste.clear();
			liste.addAll(liste2);
		}
		if (date != null) {
			Iterator<DossierIndemnisation> it = liste.iterator();
			List<DossierIndemnisation> liste2 = new ArrayList<DossierIndemnisation>();
			while (it.hasNext()) {
				DossierIndemnisation d = it.next();
				if (!d.getDateDernierEtat().before(date)) {
					liste2.add(d);
				}
			}
			liste.clear();
			liste.addAll(liste2);
		}
		if (assure != null) {
			Iterator<DossierIndemnisation> it = liste.iterator();
			List<DossierIndemnisation> liste2 = new ArrayList<DossierIndemnisation>();
			while (it.hasNext()) {
				DossierIndemnisation d = it.next();
				if (d.getArretTravail() != null) {
					if (d.getArretTravail().getAssure().isEgal(assure)) {
						liste2.add(d);
					}

				} else if (d.getAttestation() != null) {
					if (d.getAttestation().getAssure().isEgal(assure)) {
						liste2.add(d);
					}
				}
			}
			liste.clear();
			liste.addAll(liste2);
		}
		return liste;
	}

	/**
	 * Récupère le dossier en vue d'un traitement
	 * 
	 * @param dossier
	 * @param etat
	 * @param compte
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void prendreEnCharge(DossierIndemnisation dossier, Etat etat,
			Compte compte) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		dossier.setPersonneEnCharge(compte);
		dossier.setEtat(etat);
		dossier.setDateDernierEtat(DateActuelle.getDateActuelle());
		try {
			dosDao.modifier(con, dossier);
		} catch (ObjetNonTrouveException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

	/**
	 * Traite le dossier et détermine les indemnités
	 * 
	 * @param dossier
	 * @param indemnites
	 * @param montant
	 * @param ratio
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void traiterManuellement(DossierIndemnisation dossier,
			double indemnites, double montant, double ratio)
			throws ConnexionException, FichierConfigurationException,
			EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		dossier.setIndemniteJournaliere(indemnites);
		dossier.setMontantTotal(montant);
		dossier.setRatio(ratio);
		dossier.setEtat(Etat.FIN_TRAITEMENT_MANUEL);
		dossier.setDateDernierEtat(DateActuelle.getDateActuelle());
		try {
			dosDao.modifier(con, dossier);
		} catch (ObjetNonTrouveException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

	/**
	 * Détermine la décision du contrôle
	 * 
	 * @param dossier
	 * @param choix
	 * @throws EchecSQLException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public void choixControle(DossierIndemnisation dossier, boolean choix)
			throws EchecSQLException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		if (choix) {
			dossier.setEtat(Etat.FIN_CONTROLE);
		} else {
			dossier.setEtat(Etat.REFUSE);
		}
		dossier.setDateDernierEtat(DateActuelle.getDateActuelle());
		try {
			dosDao.modifier(con, dossier);
		} catch (ObjetNonTrouveException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
	}

}
