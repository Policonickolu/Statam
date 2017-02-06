package systeme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import exception.FichierConfigurationException;

public class Configuration {

	private static String chemin = "config.ini";

	/**
	 * lit les informations du fichier de configuration
	 * 
	 * @return les propriétés
	 * @throws FichierConfigurationException
	 */
	public static Properties getConfiguration()
			throws FichierConfigurationException {

		Properties proprietes = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(chemin);
			proprietes.load(in);
			in.close();
		} catch (IOException e) {
			throw new FichierConfigurationException(
					"Impossible de lire le fichier de configuration");
		}

		return proprietes;

	}

	/**
	 * écrit dans le fichier de configuration
	 * 
	 * @param proprietes
	 * @throws FichierConfigurationException
	 */
	public static void setConfiguration(Properties proprietes)
			throws FichierConfigurationException {

		FileOutputStream out;
		try {
			out = new FileOutputStream(chemin);
			proprietes.store(out, "-----configuration-----");
			out.close();
		} catch (IOException e) {
			throw new FichierConfigurationException(
					"Impossible d'écrire sur le fichier de configuration");
		}

	}

}
