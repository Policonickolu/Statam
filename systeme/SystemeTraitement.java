package systeme;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import objet.Attestation;
import objet.DossierIndemnisation;

import dao.DossierIndemnisationDao;

import enumeration.Etat;
import exception.ConnexionException;
import exception.FichierConfigurationException;
import exception.ObjetNonTrouveException;

public class SystemeTraitement extends Thread {

	private SystemeControle systemeControle = SystemeControle.getInstance();
	private boolean arret = false;
	private boolean pause = false;
	private int delai = 300000;

	/**
	 * Arrête le programme de traitement
	 * 
	 * @param a
	 */
	public synchronized void setArret(boolean a) {
		this.arret = a;
		notifyAll();
	}

	/**
	 * Met en pause le programme de traitement
	 * 
	 * @param p
	 */
	public synchronized void setPause(boolean p) {
		this.pause = p;
		notifyAll();
	}

	/**
	 * Vérifie que le programme n'est pas en pause
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void isNotPause() throws InterruptedException {
		while (this.pause) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new InterruptedException("Interrompu");
			}
		}
	}

	/**
	 * change le délai entre deux traitements
	 * 
	 * @param d
	 */
	public synchronized void setDelai(int d) {
		this.delai = d * 6000;
	}

	/**
	 * Traitement de contrôle des dossiers
	 * 
	 * @throws SQLException
	 * @throws ObjetNonTrouveException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	private void traitementControle() throws SQLException,
			ObjetNonTrouveException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		DossierIndemnisationDao dossierDao = new DossierIndemnisationDao();
		DossierIndemnisation dossier = new DossierIndemnisation();

		dossier.setEtat(Etat.COMPLET);

		List<DossierIndemnisation> liste = dossierDao.getObjetsEgaux(con,
				dossier);
		Iterator<DossierIndemnisation> it = liste.iterator();
		while (it.hasNext()) {
			DossierIndemnisation dos = (DossierIndemnisation) it.next();
			this.systemeControle.controler(dos);
			dossierDao.modifier(con, dos);
		}
	}

	/**
	 * Traitement des dossiers spéciaux
	 * 
	 * @throws SQLException
	 * @throws ObjetNonTrouveException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	private void traitementCasSpeciaux() throws SQLException,
			ObjetNonTrouveException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		DossierIndemnisationDao dossierDao = new DossierIndemnisationDao();
		DossierIndemnisation dossier = new DossierIndemnisation();
		dossier.setEtat(Etat.FIN_CONTROLE);

		List<DossierIndemnisation> liste = dossierDao.getObjetsEgaux(con,
				dossier);
		Iterator<DossierIndemnisation> it = liste.iterator();
		while (it.hasNext()) {
			DossierIndemnisation dos = (DossierIndemnisation) it.next();
			Attestation att = dos.getAttestation();
			if (att.isCasSpecial()
					|| ((DateActuelle.getDateActuelle().getTime() - att
							.getDateEmbauche().getTime()) / 1000 / 60 / 60 / 24) <= 90) {
				dos.setEtat(Etat.A_TRAITER_MANUELLEMENT);
			} else {
				dos.setEtat(Etat.FIN_TRAITEMENT_MANUEL);
			}
			dos.setDateDernierEtat(DateActuelle.getDateActuelle());
			dossierDao.modifier(con, dos);
		}
	}

	/**
	 * Traitement des indemnisations des dossiers
	 * 
	 * @throws SQLException
	 * @throws ObjetNonTrouveException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	private void traitementIndemnisation() throws SQLException,
			ObjetNonTrouveException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		DossierIndemnisationDao dossierDao = new DossierIndemnisationDao();
		DossierIndemnisation dossier = new DossierIndemnisation();
		dossier.setEtat(Etat.FIN_TRAITEMENT_MANUEL);

		List<DossierIndemnisation> liste = dossierDao.getObjetsEgaux(con,
				dossier);
		Iterator<DossierIndemnisation> it = liste.iterator();
		while (it.hasNext()) {
			DossierIndemnisation dos = (DossierIndemnisation) it.next();
			dos.traitementAutomatique();
			dossierDao.modifier(con, dos);
		}
	}

	public void run() {

		while (!this.arret) {

			try {

				this.isNotPause();

				this.traitementControle();

				this.traitementCasSpeciaux();

				this.traitementIndemnisation();

				sleep(this.delai);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ObjetNonTrouveException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ConnexionException e) {
				e.printStackTrace();
			} catch (FichierConfigurationException e) {
				e.printStackTrace();
			}

		}
	}

}
