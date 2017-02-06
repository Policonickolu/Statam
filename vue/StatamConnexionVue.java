package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import objet.Compte;
import systeme.Configuration;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.ConnexionControleur;
import exception.CompteInexistantException;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class StatamConnexionVue {

	private static final long serialVersionUID = 4986951142320574886L;
	private JFrame frmStatam;
	private Properties proprietes;
	private JLabel lblMessage;
	private JPasswordField tfMotDePasse;
	private JTextField tfIdentifiant;

	/**
	 * Lance l'application
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StatamConnexionVue window = new StatamConnexionVue();
					window.frmStatam.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructeur
	 */
	public StatamConnexionVue() {
		initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	private void initialize() {

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamConnexionVue.class
					.getResource("/vue/font/OpenSansCondensedBold.ttf")
					.openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamConnexionVue.class
					.getResource("/vue/font/verdana.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamConnexionVue.class
					.getResource("/vue/font/verdanab.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamConnexionVue.class
					.getResource("/vue/font/verdanai.ttf").openStream()));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, StatamConnexionVue.class
					.getResource("/vue/font/verdanaz.ttf").openStream()));
		} catch (FontFormatException | IOException e) {
		}

		frmStatam = new JFrame();
		frmStatam.setIconImage(Toolkit.getDefaultToolkit().getImage(
				StatamConnexionVue.class
						.getResource("/vue/images/statamicone.png")));
		frmStatam.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStatam.setFont(new Font("Open Sans Condensed", Font.PLAIN, 12));
		frmStatam.getContentPane().setFont(
				new Font("Open Sans Condensed", Font.PLAIN, 12));
		frmStatam.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel bordureHaut = new JPanel();
		bordureHaut.setBackground(Color.BLUE);
		frmStatam.getContentPane().add(bordureHaut, BorderLayout.NORTH);
		bordureHaut.setLayout(new BorderLayout(0, 0));

		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(StatamConnexionVue.class
				.getResource("/vue/images/statambanniere.jpg")));
		bordureHaut.add(logo, BorderLayout.WEST);

		JPanel bordureBas = new JPanel();
		bordureBas.setPreferredSize(new Dimension(10, 50));

		bordureBas.setBackground(Color.BLUE);
		frmStatam.getContentPane().add(bordureBas, BorderLayout.SOUTH);

		JPanel connexionPanel = new JPanel();
		connexionPanel.setBackground(Color.WHITE);
		connexionPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("15px:grow"), ColumnSpec.decode("80px"),
				ColumnSpec.decode("15px"), ColumnSpec.decode("150px"),
				ColumnSpec.decode("15px:grow"), }, new RowSpec[] {
				RowSpec.decode("30px"), RowSpec.decode("20px"),
				RowSpec.decode("20px"), RowSpec.decode("20px"),
				RowSpec.decode("20px"), RowSpec.decode("20px"),
				RowSpec.decode("30px"), RowSpec.decode("30px"),
				RowSpec.decode("20px:grow"), }));

		lblMessage = new JLabel("Entrez vos identifiants");
		lblMessage.setFont(new Font("Verdana", Font.PLAIN, 12));
		connexionPanel.add(lblMessage, "2, 2, 3, 1");

		JLabel lblIdentifiant = new JLabel("Identifiant");
		lblIdentifiant.setForeground(Color.DARK_GRAY);
		lblIdentifiant.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		connexionPanel.add(lblIdentifiant, "2, 4, left, center");

		tfIdentifiant = new JTextField();
		tfIdentifiant.setBorder(new LineBorder(new Color(0, 255, 255)));
		tfIdentifiant.setFont(new Font("Verdana", Font.PLAIN, 12));
		tfIdentifiant.setBackground(new Color(0, 204, 255));
		connexionPanel.add(tfIdentifiant, "4, 4, fill, center");
		tfIdentifiant.setColumns(10);

		JLabel lblMotDePasse = new JLabel("Mot de Passe");
		lblMotDePasse.setForeground(Color.DARK_GRAY);
		lblMotDePasse.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		connexionPanel.add(lblMotDePasse, "2, 6, left, center");

		tfMotDePasse = new JPasswordField();
		tfMotDePasse.setBorder(new LineBorder(new Color(0, 255, 255)));
		tfMotDePasse.setFont(new Font("Verdana", Font.PLAIN, 12));
		tfMotDePasse.setBackground(new Color(0, 204, 255));
		connexionPanel.add(tfMotDePasse, "4, 6, fill, center");

		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setPreferredSize(new Dimension(125, 30));
		btnConnexion.addActionListener(new ActionConnexionListener());
		btnConnexion.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		btnConnexion.setForeground(Color.DARK_GRAY);
		connexionPanel.add(btnConnexion, "2, 8, 3, 1, center, center");

		frmStatam.getContentPane().add(connexionPanel, BorderLayout.CENTER);
		frmStatam.setTitle("Statam - Connexion");
		frmStatam.addComponentListener(new FenetreListener());

		int positionX = 100;
		int positionY = 100;
		int largeur = 600;
		int hauteur = 550;
		
		boolean locationNull = true;
		
		try {
			proprietes = Configuration.getConfiguration();
			String[] position = proprietes.getProperty("position_connexion")
					.split("\\D+");
			String[] taille = proprietes.getProperty("taille_connexion").split(
					"\\D+");
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
		
	}

	/**
	 * Listener du bouton de connexion
	 */
	public class ActionConnexionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							lblMessage.setText("");
						}
					});
					final String id = tfIdentifiant.getText().trim();
					final String mdp = String.valueOf(
							tfMotDePasse.getPassword()).trim();
					if (!id.equals("") && !mdp.equals("")) {
						ConnexionControleur cc = ConnexionControleur
								.getInstance();
						final Compte[] com = {new Compte()};
						try {
							com[0] = cc.authentifier(id, mdp);
						} catch (final CompteInexistantException | ConnexionException | FichierConfigurationException | EchecSQLException e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Toolkit.getDefaultToolkit().beep();
									lblMessage.setText(e.getMessage());
								}
							});
						}
						if (com[0] != null && com[0].getId() != 0) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									StatamVue window = new StatamVue(com[0] ,serialVersionUID);
									window.setVisible(true);
									frmStatam.setVisible(false);
									frmStatam.dispose();
								}
							});
						} else {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Toolkit.getDefaultToolkit().beep();
									lblMessage
											.setText("Identifiant ou mot de passe incorrect");
								}
							});
						}
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								Toolkit.getDefaultToolkit().beep();
								lblMessage.setText("Entrez vos identifiants");
							}
						});
					}
				}
			}).start();
		}

	}

	/**
	 * Listener de l'affichage de la fenêtre
	 */
	public class FenetreListener extends ComponentAdapter {

		@Override
		public void componentResized(final ComponentEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					JFrame frame = (JFrame) arg0.getSource();
					proprietes.setProperty(
							"taille_connexion",
							((int) frame.getWidth()) + "x"
									+ ((int) frame.getHeight()));
					try {
						Configuration.setConfiguration(proprietes);
					} catch (final FichierConfigurationException e) {		
					}
				}
			}).start();
		}

		@Override
		public void componentMoved(final ComponentEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					JFrame frame = (JFrame) arg0.getSource();
					proprietes.setProperty("position_connexion",
							((int) frame.getX()) + "x" + ((int) frame.getY()));
					try {
						Configuration.setConfiguration(proprietes);
					} catch (final FichierConfigurationException e) {
					}
				}
			}).start();
		}

	}

}
