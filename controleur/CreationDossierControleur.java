package controleur;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import objet.ArretTravail;
import objet.Assure;
import objet.Attestation;
import objet.DossierIndemnisation;
import objet.Employeur;
import objet.Medecin;
import systeme.Connexion;
import systeme.DateActuelle;
import dao.ArretTravailDao;
import dao.AssureDao;
import dao.AttestationDao;
import dao.DossierIndemnisationDao;
import dao.EmployeurDao;
import dao.MedecinDao;
import enumeration.Emploi;
import enumeration.Etat;
import enumeration.Motif;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;
import exception.ObjetNonTrouveException;

public class CreationDossierControleur {

	private static CreationDossierControleur instance;
	
	/**
	 * Initialise l'instance
	 * 
	 * @return l'instance
	 */
	public static CreationDossierControleur getInstance() {
		if (instance == null) {
			instance = new CreationDossierControleur();
		}
		return instance;
	}

	/**
	 * Recherche les noms d'employeurs corcordant au paramètre
	 * 
	 * @param string
	 * @return la liste des noms d'employeur
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public List<String> rechercheNomEmployeur(String string)
			throws ConnexionException, FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		EmployeurDao empDao = new EmployeurDao();
		List<Employeur> liste = new ArrayList<Employeur>();
		try {
			liste = empDao.trouverTout(con);
		} catch (SQLException | ObjetNonTrouveException e) {
		}
		List<String> listeRetour = new ArrayList<String>();
		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i).getNom().toLowerCase().trim()
					.indexOf(string.toLowerCase().trim()) != -1)
				listeRetour.add(liste.get(i).getNom());
		}
		Collections.sort(listeRetour);
		return listeRetour;
	}

	/**
	 * Vérifie la conformité du formulaire d'arrêt de travail
	 * 
	 * @param assure
	 * @param medecin
	 * @param dateDebut
	 * @param dateFin
	 * @param motif
	 * @return Le résultat textuel de la vérification
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public String verifierArretTravail(int assure, int medecin, Date dateDebut,
			Date dateFin, Motif motif) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		String s = "";
		s += (dateDebut.after(dateFin) ? "La date de début est plus tard que celle de la fin. "
				: "");
		AssureDao assDao = new AssureDao();
		Assure ass = new Assure();
		ass.setId(assure);
		List<Assure> listeAss = new ArrayList<Assure>();
		try {
			listeAss = assDao.getObjetsEgaux(con, ass);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		MedecinDao medDao = new MedecinDao();
		Medecin med = new Medecin();
		med.setId(medecin);
		List<Medecin> listeMed = new ArrayList<Medecin>();
		try {
			listeMed = medDao.getObjetsEgaux(con, med);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		ArretTravailDao arrDao = new ArretTravailDao();
		ArretTravail arr = new ArretTravail();
		if (listeAss.size() == 0) {
			s += "Le numéro d'assuré n'est pas répertorié. ";
			arr.setAssure(new Assure());
		} else {
			arr.setAssure(listeAss.get(0));
		}
		if (listeMed.size() == 0) {
			s += "Le numéro de prescripteur n'est pas répertorié. ";
			arr.setMedecin(new Medecin());
		} else {
			arr.setMedecin(listeMed.get(0));
		}
		arr.setDebutArret(dateDebut);
		arr.setFinArret(dateFin);
		List<ArretTravail> listeArr = new ArrayList<ArretTravail>();
		try {
			listeArr = arrDao.getObjetsEgaux(con, arr);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		if (listeArr.size() > 0) {
			s = "L'arrêt de travail saisi existe déjà.";
		}
		return s;
	}

	/**
	 * Vérifie la conformité du formulaire d'attestation
	 * 
	 * @param assure
	 * @param employeur
	 * @param dateDebut
	 * @param dateFin
	 * @param emploi
	 * @param dateEmbauche
	 * @param heures
	 * @param salaires
	 * @param casSpecial
	 * @return Le résultat textuel de la vérification
	 * @throws EchecSQLException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public String verifierAttestation(int assure, String employeur,
			Date dateDebut, Date dateFin, Emploi emploi, Date dateEmbauche,
			int heures, float salaires, boolean casSpecial)
			throws EchecSQLException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		String s = "";
		s += (dateDebut.after(dateFin) ? "La date de début est plus tard que celle de la fin. "
				: "");
		AssureDao assDao = new AssureDao();
		Assure ass = new Assure();
		ass.setId(assure);
		List<Assure> listeAss = new ArrayList<Assure>();
		try {
			listeAss = assDao.getObjetsEgaux(con, ass);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		EmployeurDao empDao = new EmployeurDao();
		Employeur emp = new Employeur();
		emp.setNom(employeur);
		List<Employeur> listeEmp = new ArrayList<Employeur>();
		try {
			listeEmp = empDao.getObjetsEgaux(con, emp);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		AttestationDao attDao = new AttestationDao();
		Attestation att = new Attestation();
		if (listeAss.size() == 0) {
			s += "Le numéro d'assuré n'est pas répertorié. ";
			att.setAssure(new Assure());
		} else {
			att.setAssure(listeAss.get(0));
		}
		if (listeEmp.size() == 0) {
			s += "Le nom de l'employeur n'est pas répertorié. ";
			att.setEmployeur(new Employeur());
		} else {
			att.setEmployeur(listeEmp.get(0));
		}
		att.setDebutArret(dateDebut);
		att.setFinArret(dateFin);
		List<Attestation> listeArr = new ArrayList<Attestation>();
		try {
			listeArr = attDao.getObjetsEgaux(con, att);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		if (listeArr.size() > 0) {
			s = "L'attestation saisie existe déjà.";
		}
		return s;
	}

	/**
	 * Crée l'arrêt de travail dans la base de données
	 * 
	 * @param assure
	 * @param medecin
	 * @param dateDebut
	 * @param dateFin
	 * @param motif
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void validerArretTravail(int assure, int medecin, Date dateDebut,
			Date dateFin, Motif motif) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		AssureDao assDao = new AssureDao();
		Assure ass = new Assure();
		ass.setId(assure);
		List<Assure> listeAss = new ArrayList<Assure>();
		try {
			listeAss = assDao.getObjetsEgaux(con, ass);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		MedecinDao medDao = new MedecinDao();
		Medecin med = new Medecin();
		med.setId(medecin);
		List<Medecin> listeMed = new ArrayList<Medecin>();
		try {
			listeMed = medDao.getObjetsEgaux(con, med);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		ArretTravailDao arrDao = new ArretTravailDao();
		ArretTravail arr = new ArretTravail();
		arr.setAssure(listeAss.get(0));
		arr.setMedecin(listeMed.get(0));
		arr.setDebutArret(dateDebut);
		arr.setFinArret(dateFin);
		arr.setMotifMedical(motif);
		try {
			arrDao.creer(con, arr);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		AttestationDao attDao = new AttestationDao();
		Attestation att = new Attestation();
		att.setAssure(ass);
		att.setDebutArret(dateDebut);
		att.setFinArret(dateFin);
		List<Attestation> listeAtt = new ArrayList<Attestation>();
		try {
			listeAtt = attDao.getObjetsEgaux(con, att);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		DossierIndemnisation dos = new DossierIndemnisation();
		if (listeAtt.size() > 0) {
			dos.setAttestation(listeAtt.get(0));
			List<DossierIndemnisation> listeDos = new ArrayList<DossierIndemnisation>();
			try {
				listeDos = dosDao.getObjetsEgaux(con, dos);
			} catch (SQLException e) {
				throw new EchecSQLException("Erreur, veuillez recommencer.");
			}
			if (listeDos.size() > 0) {
				listeDos.get(0).setArretTravail(arr);
				listeDos.get(0).setEtat(Etat.COMPLET);
				try {
					dosDao.modifier(con, listeDos.get(0));
				} catch (ObjetNonTrouveException e) {
				} catch (SQLException e) {
					throw new EchecSQLException("Erreur, veuillez recommencer.");
				}
			}
		} else {
			dos = new DossierIndemnisation();
			dos.setArretTravail(arr);
			dos.setEtat(Etat.INCOMPLET);
			dos.setDateDernierEtat(DateActuelle.getDateActuelle());
			dos.setMontantTotal(0);
			dos.setIndemniteJournaliere(0);
			dos.setRatio(100);
			try {
				dosDao.creer(con, dos);
			} catch (SQLException e) {
				throw new EchecSQLException("Erreur, veuillez recommencer.");
			}
		}
	}

	/**
	 * Crée l'attestation dans la base de données
	 * 
	 * @param assure
	 * @param employeur
	 * @param dateDebut
	 * @param dateFin
	 * @param emploi
	 * @param dateEmbauche
	 * @param heures
	 * @param salaires
	 * @param casSpecial
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 * @throws EchecSQLException
	 */
	public void validerAttestation(int assure, String employeur, Date dateDebut,
			Date dateFin, Emploi emploi, Date dateEmbauche, int heures,
			float salaires, boolean casSpecial) throws ConnexionException,
			FichierConfigurationException, EchecSQLException {
		Connection con = Connexion.getInstance();
		if (!Connexion.isValide()) {
			throw new ConnexionException("Problème de connexion au serveur.");
		}
		AssureDao assDao = new AssureDao();
		Assure ass = new Assure();
		ass.setId(assure);
		List<Assure> listeAss = new ArrayList<Assure>();
		try {
			listeAss = assDao.getObjetsEgaux(con, ass);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		EmployeurDao empDao = new EmployeurDao();
		Employeur emp = new Employeur();
		emp.setNom(employeur);
		List<Employeur> listeEmp = new ArrayList<Employeur>();
		try {
			listeEmp = empDao.getObjetsEgaux(con, emp);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		AttestationDao attDao = new AttestationDao();
		Attestation att = new Attestation();
		att.setAssure(listeAss.get(0));
		att.setEmployeur(listeEmp.get(0));
		att.setDebutArret(dateDebut);
		att.setFinArret(dateFin);
		att.setEmploi(emploi);
		att.setDateEmbauche(dateEmbauche);
		att.setHeuresTravaillees(heures);
		att.setSalairesPercus(salaires);
		att.setCasSpecial(casSpecial);
		try {
			attDao.creer(con, att);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		ArretTravailDao arrDao = new ArretTravailDao();
		ArretTravail arr = new ArretTravail();
		arr.setAssure(ass);
		arr.setDebutArret(dateDebut);
		arr.setFinArret(dateFin);
		List<ArretTravail> listeArr = new ArrayList<ArretTravail>();
		try {
			listeArr = arrDao.getObjetsEgaux(con, arr);
		} catch (SQLException e) {
			throw new EchecSQLException("Erreur, veuillez recommencer.");
		}
		DossierIndemnisationDao dosDao = new DossierIndemnisationDao();
		DossierIndemnisation dos = new DossierIndemnisation();
		if (listeArr.size() > 0) {
			dos.setArretTravail(listeArr.get(0));
			List<DossierIndemnisation> listeDos = new ArrayList<DossierIndemnisation>();
			try {
				listeDos = dosDao.getObjetsEgaux(con, dos);
			} catch (SQLException e) {
				throw new EchecSQLException("Erreur, veuillez recommencer.");
			}
			if (listeDos.size() > 0) {
				listeDos.get(0).setAttestation(att);
				listeDos.get(0).setEtat(Etat.COMPLET);
				try {
					dosDao.modifier(con, listeDos.get(0));
				} catch (ObjetNonTrouveException e) {
				} catch (SQLException e) {
					throw new EchecSQLException("Erreur, veuillez recommencer.");
				}
			}
		} else {
			dos = new DossierIndemnisation();
			dos.setAttestation(att);
			dos.setEtat(Etat.INCOMPLET);
			dos.setDateDernierEtat(DateActuelle.getDateActuelle());
			dos.setMontantTotal(0);
			dos.setIndemniteJournaliere(0);
			dos.setRatio(100);
			try {
				dosDao.creer(con, dos);
			} catch (SQLException e) {
				throw new EchecSQLException("Erreur, veuillez recommencer.");
			}
		}
	}

}
