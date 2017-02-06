package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import objet.Compte;
import objet.Droit;
import systeme.Configuration;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import enumeration.Job;
import exception.FichierConfigurationException;

public class StatamVue {

	private JFrame frmStatam;
	private JTabbedPane onglets;
	private Compte compte;
	private Properties proprietes;
	private JLabel message;
	private JLabel connexionNom;

	/**
	 * Constructeur
	 */
	public StatamVue(Compte com, long id) {
		if (id == 4986951142320574886L)
		compte = com;
		initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	private void initialize() {

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamVue.class
					.getResource("/vue/font/OpenSansCondensedBold.ttf")
					.openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamVue.class
					.getResource("/vue/font/verdana.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamVue.class
					.getResource("/vue/font/verdanab.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamVue.class
					.getResource("/vue/font/verdanai.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamVue.class
					.getResource("/vue/font/verdanaz.ttf").openStream()));
		} catch (FontFormatException | IOException e) {
		}

		frmStatam = new JFrame();

		frmStatam.setBackground(Color.WHITE);
		frmStatam.setIconImage(Toolkit.getDefaultToolkit().getImage(
				StatamVue.class.getResource("/vue/images/statamicone.png")));
		frmStatam.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStatam.setFont(new Font("Open Sans Condensed", Font.PLAIN, 12));
		frmStatam.getContentPane().setFont(
				new Font("Open Sans Condensed", Font.PLAIN, 12));
		frmStatam.getContentPane().setLayout(new BorderLayout(0, 0));
		frmStatam
				.setTitle("Statam - Système de traitement des arrêts de travail de l'Assurance Maladie");
		frmStatam.addComponentListener(new FenetreListener());

		int positionX = 100;
		int positionY = 100;
		int largeur = 900;
		int hauteur = 800;
		boolean locationNull = true;

		try {
			proprietes = Configuration.getConfiguration();

			String[] position = proprietes.getProperty("position_principale")
					.split("\\D+");
			String[] taille = proprietes.getProperty("taille_principale")
					.split("\\D+");

			if (position.length >= 2) {
				if (!position[0].equals("") && !position[1].equals("")) {
					positionX = Integer.parseInt(position[0]);
					positionY = Integer.parseInt(position[1]);
					locationNull = false;
				}
			}
			if (taille.length >= 2) {
				if (!taille[0].equals("") && !taille[1].equals("")) {
					largeur = Integer.parseInt(taille[0]);
					hauteur = Integer.parseInt(taille[1]);
				}
			}

		} catch (FichierConfigurationException e) {
			frmStatam.setLocationRelativeTo(null);
		}

		frmStatam.setBounds(positionX, positionY, largeur, hauteur);
		if (locationNull)
			frmStatam.setLocationRelativeTo(null);

		JPanel bordureHaut = new JPanel();
		bordureHaut.setBackground(Color.BLUE);
		frmStatam.getContentPane().add(bordureHaut, BorderLayout.NORTH);
		bordureHaut.setLayout(new BorderLayout(0, 0));

		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(StatamVue.class
				.getResource("/vue/images/statambanniere.jpg")));
		bordureHaut.add(logo, BorderLayout.WEST);

		connexionNom = new JLabel(compte.getPrenom() + " " + compte.getNom()
				+ "     ");
		connexionNom.setFont(new Font("Verdana", Font.PLAIN, 15));
		connexionNom.setForeground(Color.WHITE);
		bordureHaut.add(connexionNom, BorderLayout.EAST);

		JPanel bordureBas = new JPanel();
		bordureBas.setPreferredSize(new Dimension(10, 50));
		bordureBas.setBackground(Color.BLUE);
		frmStatam.getContentPane().add(bordureBas, BorderLayout.SOUTH);
		bordureBas.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), ColumnSpec.decode("300px:grow"),
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), }));

		message = new JLabel("");
		message.addPropertyChangeListener(new MessageListener());
		message.setFont(new Font("Verdana", Font.PLAIN, 13));
		message.setForeground(Color.WHITE);
		bordureBas.add(message, "2, 2");

		JPanel fondBlanc = new JPanel();
		fondBlanc.setBackground(Color.CYAN);
		frmStatam.getContentPane().add(fondBlanc, BorderLayout.CENTER);
		fondBlanc.setLayout(new BorderLayout(0, 0));

		onglets = new JTabbedPane(JTabbedPane.TOP);
		onglets.setBackground(Color.WHITE);
		onglets.setFont(new Font("Open Sans Condensed", Font.PLAIN, 12));

		Iterator<Droit> it = compte.getFonction().getDroits().iterator();
		ArrayList<String> al = new ArrayList<String>();
		while (it.hasNext()) {
			al.add(it.next().getNom());
		}

		if (al.contains(Job.VISUALISATION_ETAT + "")){
		VisualisationEtatVue visualisationEtatVue = new VisualisationEtatVue(
				message);
		onglets.addTab("Visualisation Etat", null, visualisationEtatVue, null);
		}

		if (al.contains(Job.CREATION_DOSSIER + "")){
		CreationDossierVue creationDossierVue = new CreationDossierVue(message);
		onglets.addTab("Création Dossier", null, creationDossierVue, null);
		}

		if (al.contains(Job.GESTION_SYSTEME_CONTROLE + "")){
		GestionSystemeControleVue gestionSystemeControleVue = new GestionSystemeControleVue(
				message);
		onglets.addTab("Gestion Système Contrôle", null,
				gestionSystemeControleVue, null);

		}

		if (al.contains(Job.CONTROLE_DOSSIER + "")){
		ControleDossierVue controleDossierVue = new ControleDossierVue(message,
				compte);
		onglets.addTab("Contrôle Dossier", null, controleDossierVue, null);

		}

		if (al.contains(Job.TRAITEMENT_MANUEL + "")){
		TraitementManuelVue traitementManuelVue = new TraitementManuelVue(
				message, compte);
		onglets.addTab("Traitement Manuel", null, traitementManuelVue, null);

		}

		fondBlanc.add(onglets, BorderLayout.CENTER);

	}

	/**
	 * Fait apparaître ou disparaître la fenêtre
	 * 
	 * @param b
	 */
	public void setVisible(boolean b) {
		frmStatam.setVisible(b);
	}

	/**
	 * Listener de l'affichage de la fenêtre
	 */
	public class FenetreListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent arg0) {
			final JFrame frame = (JFrame) arg0.getSource();
			new Thread(new Runnable() {
				public void run() {
					proprietes.setProperty(
							"taille_principale",
							((int) frame.getWidth()) + "x"
									+ ((int) frame.getHeight()));
					try {
						Configuration.setConfiguration(proprietes);
					} catch (FichierConfigurationException e) {
					}
				}
			}).start();
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			final JFrame frame = (JFrame) arg0.getSource();
			new Thread(new Runnable() {
				public void run() {
					proprietes.setProperty("position_principale",
							((int) frame.getX()) + "x" + ((int) frame.getY()));
					try {
						Configuration.setConfiguration(proprietes);
					} catch (FichierConfigurationException e) {
					}
				}
			}).start();
		}

	}
	
	/**
	 * Listener de message d'erreur
	 */
	public class MessageListener implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(5000);
						message.removePropertyChangeListener(message.getPropertyChangeListeners()[0]);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								message.setText("");
							}
						});
						message.addPropertyChangeListener(new MessageListener());
					} catch (InterruptedException e) {
					}	
				}
			}).start(); 			
		}
		
	}

}
