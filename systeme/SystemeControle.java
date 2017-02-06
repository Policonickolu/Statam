package systeme;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import objet.DossierIndemnisation;
import objet.Filtre;

import dao.FiltreDao;

import enumeration.Etat;
import exception.ConnexionException;
import exception.FichierConfigurationException;
import exception.ObjetNonTrouveException;

public class SystemeControle {

	private static SystemeControle controle;

	/**
	 * controle les dossiers selon les filtres et critères définis
	 * 
	 * @param dos
	 * @throws SQLException
	 * @throws ObjetNonTrouveException
	 * @throws ConnexionException
	 * @throws FichierConfigurationException
	 */
	public void controler(DossierIndemnisation dos) throws SQLException,
			ObjetNonTrouveException, ConnexionException,
			FichierConfigurationException {
		Connection con = Connexion.getInstance();
		FiltreDao filtreDao = new FiltreDao();
		Filtre filtre = new Filtre();
		filtre.setActive(true);
		List<Filtre> liste = filtreDao.getObjetsEgaux(con, filtre);
		Iterator<Filtre> it = liste.iterator();
		dos.setEtat(Etat.FIN_CONTROLE);
		while (it.hasNext()) {
			Filtre fil = (Filtre) it.next();
			if (fil.controler(dos)) {
				dos.setEtat(Etat.A_CONTROLER);
				break;
			}
		}
		dos.setDateDernierEtat(DateActuelle.getDateActuelle());
	}

	/**
	 * Permet d'obtenir une instance unique
	 * 
	 * @return
	 */
	public static SystemeControle getInstance() {
		if (controle == null) {
			controle = new SystemeControle();
		}
		return controle;
	}

}
