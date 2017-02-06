package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DateFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXDatePicker;

import systeme.DateActuelle;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.DossiersControleur;
import enumeration.Etat;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class VisualisationEtatVue extends JPanel {

	private static final long serialVersionUID = 8042623400902794070L;
	private JLabel message;
	private JLabel lblDate;
	private JLabel nbIncomplets;
	private JLabel nbComplets;
	private JLabel nbAControler;
	private JLabel nbControles;
	private JLabel nbATraiter;
	private JLabel nbTraitement;
	private JLabel nbRefuses;
	private JLabel nbIndemnises;
	private JXDatePicker datePicker;

	/**
	 * Constructeur
	 */
	public VisualisationEtatVue(JLabel message) {
		this.message = message;
		this.initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {

		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font("Open Sans Condensed", Font.PLAIN, 12));
		this.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();

		tabbedPane.addTab("Etat", null, panel, null);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("6px"),
						FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
						FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
						FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
						FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
						FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
						ColumnSpec.decode("225px"), ColumnSpec.decode("6px"),
						ColumnSpec.decode("150px"),
						ColumnSpec.decode("default:grow"),
						ColumnSpec.decode("6px"), },
				new RowSpec[] { RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("6px"),
						FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("30px"),
						FormFactory.DEFAULT_ROWSPEC,
						RowSpec.decode("default:grow"), RowSpec.decode("6px"), }));

		JLabel lblEtatsDesDossiers = new JLabel("Etats des dossiers");
		lblEtatsDesDossiers.setForeground(Color.DARK_GRAY);
		lblEtatsDesDossiers.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				15));
		panel.add(lblEtatsDesDossiers, "4, 2");

		JLabel lblDepuis = new JLabel("Depuis");
		lblDepuis.setForeground(Color.DARK_GRAY);
		lblDepuis.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblDepuis, "8, 2");

		JLabel label = new JLabel(":");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label, "10, 2");

		lblDate = new JLabel("choisir une date");
		lblDate.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(lblDate, "12, 2, center, default");

		datePicker = new JXDatePicker();
		datePicker.getEditor().setFont(
				new Font("Open Sans Condensed", Font.PLAIN, 15));
		datePicker.setBackground(Color.WHITE);
		datePicker.setFont(new Font("Open Sans Condensed", Font.PLAIN, 13));
		datePicker.setDate(new Date(DateActuelle.getDateActuelle().getTime()));
		panel.add(datePicker, "14, 2");
		datePicker.addActionListener(new DatePickerListener());

		JLabel lblNewLabel = new JLabel("Incomplets");
		lblNewLabel.setForeground(Color.DARK_GRAY);
		lblNewLabel.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel, "4, 6");

		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_1, "6, 6");

		nbIncomplets = new JLabel("0");
		nbIncomplets.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbIncomplets, "8, 6");

		JLabel lblNewLabel_2 = new JLabel("Complets");
		lblNewLabel_2.setForeground(Color.DARK_GRAY);
		lblNewLabel_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_2, "4, 8");

		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_2, "6, 8");

		nbComplets = new JLabel("0");
		nbComplets.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbComplets, "8, 8");

		JLabel lblNewLabel_18 = new JLabel("A contrôler");
		lblNewLabel_18.setForeground(Color.DARK_GRAY);
		lblNewLabel_18.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_18, "4, 10");

		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.DARK_GRAY);
		label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_3, "6, 10");

		nbAControler = new JLabel("0");
		nbAControler.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbAControler, "8, 10");

		JLabel lblNewLabel_4 = new JLabel("En cours de contrôle");
		lblNewLabel_4.setForeground(Color.DARK_GRAY);
		lblNewLabel_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_4, "4, 12");

		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_4, "6, 12");

		nbControles = new JLabel("0");
		nbControles.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbControles, "8, 12");

		JLabel lblNewLabel_19 = new JLabel("A traiter manuellement");
		lblNewLabel_19.setForeground(Color.DARK_GRAY);
		lblNewLabel_19.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_19, "4, 14");

		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.DARK_GRAY);
		label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_5, "6, 14");

		nbATraiter = new JLabel("0");
		nbATraiter.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbATraiter, "8, 14");

		JLabel lblNewLabel_6 = new JLabel("En cours de traitement manuel");
		lblNewLabel_6.setForeground(Color.DARK_GRAY);
		lblNewLabel_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_6, "4, 16");

		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_6, "6, 16, right, default");

		nbTraitement = new JLabel("0");
		nbTraitement.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbTraitement, "8, 16");

		JLabel lblNewLabel_10 = new JLabel("Refusés");
		lblNewLabel_10.setForeground(Color.DARK_GRAY);
		lblNewLabel_10.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_10, "4, 18");

		JLabel label_7 = new JLabel(":");
		label_7.setForeground(Color.DARK_GRAY);
		label_7.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_7, "6, 18");

		nbRefuses = new JLabel("0");
		nbRefuses.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbRefuses, "8, 18");

		JLabel lblNewLabel_8 = new JLabel("Traités");
		lblNewLabel_8.setForeground(Color.DARK_GRAY);
		lblNewLabel_8.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(lblNewLabel_8, "4, 20");

		JLabel label_8 = new JLabel(":");
		label_8.setForeground(Color.DARK_GRAY);
		label_8.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel.add(label_8, "6, 20");

		nbIndemnises = new JLabel("0");
		nbIndemnises.setFont(new Font("Verdana", Font.PLAIN, 12));
		panel.add(nbIndemnises, "8, 20");

		JLabel lblRefreshEtat = new JLabel("");
		lblRefreshEtat.addMouseListener(new BoutonActualiserEtatListener());
		lblRefreshEtat.setIcon(new ImageIcon(VisualisationEtatVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panel.add(lblRefreshEtat, "2, 22");

		JLabel btnActualiser = new JLabel("Actualiser");
		panel.add(btnActualiser, "4, 22, left, center");
		btnActualiser.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		btnActualiser.setForeground(Color.DARK_GRAY);

		OngletEtat panelIncomplets = new OngletEtat(Etat.INCOMPLET, message,
				datePicker);
		tabbedPane.addTab("Incomplets", null, panelIncomplets, null);

		OngletEtat panelComplets = new OngletEtat(Etat.COMPLET, message,
				datePicker);
		tabbedPane.addTab("Complets", null, panelComplets, null);

		OngletEtat panelAControler = new OngletEtat(Etat.A_CONTROLER, message,
				datePicker);
		tabbedPane.addTab("A Contrôler", null, panelAControler, null);

		OngletEtat panelEnControle = new OngletEtat(Etat.EN_CONTROLE, message,
				datePicker);
		tabbedPane.addTab("En Contrôle", null, panelEnControle, null);

		OngletEtat panelATraiter = new OngletEtat(Etat.A_TRAITER_MANUELLEMENT,
				message, datePicker);
		tabbedPane.addTab("A traiter", null, panelATraiter, null);

		OngletEtat panelEnTraitement = new OngletEtat(
				Etat.EN_TRAITEMENT_MANUEL, message, datePicker);
		tabbedPane.addTab("En traitement", null, panelEnTraitement, null);

		OngletEtat panelRefuses = new OngletEtat(Etat.REFUSE, message,
				datePicker);
		tabbedPane.addTab("Refusés", null, panelRefuses, null);

		OngletEtat panelTraites = new OngletEtat(Etat.TRAITE, message,
				datePicker);
		tabbedPane.addTab("Traités", null, panelTraites, null);

		this.addComponentListener(new FenetreEtatListener());

	}

	/**
	 * Actualiser les états des dossiers
	 */
	private void actualiserEtats() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final int[] nombres = new int[8];
				try {
					nombres[0] = dc.getDossiers(Etat.INCOMPLET,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[1] = dc.getDossiers(Etat.COMPLET,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[2] = dc.getDossiers(Etat.A_CONTROLER,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[3] = dc.getDossiers(Etat.EN_CONTROLE,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[4] = dc.getDossiers(Etat.A_TRAITER_MANUELLEMENT,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[5] = dc.getDossiers(Etat.EN_TRAITEMENT_MANUEL,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[6] = dc.getDossiers(Etat.REFUSE,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
					nombres[7] = dc.getDossiers(Etat.TRAITE,
							new Date(datePicker.getDate().getTime()), null,
							null, null).size();
				} catch (ConnexionException | EchecSQLException
						| FichierConfigurationException e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							message.setText(e.getMessage());
							Toolkit.getDefaultToolkit().beep();
						}
					});
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						nbIncomplets.setText("" + nombres[0]);
						nbComplets.setText("" + nombres[1]);
						nbAControler.setText("" + nombres[2]);
						nbControles.setText("" + nombres[3]);
						nbATraiter.setText("" + nombres[4]);
						nbTraitement.setText("" + nombres[5]);
						nbRefuses.setText("" + nombres[6]);
						nbIndemnises.setText("" + nombres[7]);
					}
				});
			}
		}).start();
	}

	/**
	 * Listener du choix de la date
	 */
	public class DatePickerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			lblDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(
					datePicker.getDate()));
			actualiserEtats();
		}

	}

	/**
	 * Listener de l'affichage de l'onglet
	 */
	public class FenetreEtatListener extends ComponentAdapter {

		@Override
		public void componentShown(ComponentEvent arg0) {
			actualiserEtats();
		}

	}

	/**
	 * Listener du bouton de rafraîchissement
	 */
	public class BoutonActualiserEtatListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent arg0) {
			final JLabel label = (JLabel) arg0.getSource();
			label.setIcon(new ImageIcon(VisualisationEtatVue.class
					.getResource("/vue/images/iconeactualiserclick.png")));
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			final JLabel label = (JLabel) arg0.getSource();
			label.setIcon(new ImageIcon(VisualisationEtatVue.class
					.getResource("/vue/images/iconeactualiser.png")));
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			actualiserEtats();
		}

	}

}
