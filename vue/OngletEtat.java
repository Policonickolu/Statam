package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objet.ArretTravail;
import objet.Attestation;
import objet.DossierIndemnisation;

import org.jdesktop.swingx.JXDatePicker;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.DossiersControleur;
import enumeration.Etat;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class OngletEtat extends JPanel {

	private static final long serialVersionUID = 1731357697915277126L;
	private Etat etat;
	private JLabel message;
	private JXDatePicker date;
	private JLabel lblNbDossierVal;
	private JLabel lblAssureVal;
	private JLabel lblDebutArretVal;
	private JLabel lblFinArretVal;
	private JLabel lblPersonneVal;
	private JLabel lblIndemnitesVal;
	private JLabel lblMontantVal;
	private JLabel lblRatioVal;
	private OngletsDossier ongletsDossier;
	private JTextField tfRecherche;
	private DefaultListModel<DossierIndemnisation> dlm;
	private JList<DossierIndemnisation> listeDossiers;

	/**
	 * Constructeur
	 */
	public OngletEtat(Etat etat, JLabel message, JXDatePicker date) {
		this.etat = etat;
		this.message = message;
		this.date = date;
		initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {

		this.setLayout(new BorderLayout(0, 0));

		dlm = new DefaultListModel<DossierIndemnisation>();

		this.addComponentListener(new FenetreListener());

		JPanel panelSub = new JPanel();
		this.add(panelSub, BorderLayout.CENTER);
		panelSub.setBackground(Color.WHITE);
		panelSub.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), ColumnSpec.decode("17px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				ColumnSpec.decode("max(75px;default)"),
				ColumnSpec.decode("15px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"),
				ColumnSpec.decode("max(75px;default)"),
				ColumnSpec.decode("15px:grow"), ColumnSpec.decode("17px"),
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("23px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("max(15px;default):grow"),
				FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblNbDossier = new JLabel("Dossier :");
		lblNbDossier.setForeground(Color.DARK_GRAY);
		lblNbDossier.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelSub.add(lblNbDossier, "3, 2");

		lblNbDossierVal = new JLabel("");
		lblNbDossierVal.setForeground(Color.BLACK);
		lblNbDossierVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblNbDossierVal, "5, 2");

		JLabel lblAssure = new JLabel("Assuré");
		lblAssure.setForeground(Color.DARK_GRAY);
		lblAssure.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblAssure, "5, 4");

		JLabel label = new JLabel(":");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label, "7, 4");

		lblAssureVal = new JLabel("");
		lblAssureVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblAssureVal, "9, 4");

		JLabel lblIndemnites = new JLabel("Indemnités journalière");
		lblIndemnites.setForeground(Color.DARK_GRAY);
		lblIndemnites.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblIndemnites, "11, 4");

		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_1, "13, 4, right, default");

		lblIndemnitesVal = new JLabel();
		lblIndemnitesVal.setPreferredSize(new Dimension(6, 18));
		lblIndemnitesVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblIndemnitesVal, "15, 4");

		JLabel lblDebutArret = new JLabel("Début de l'arrêt");
		lblDebutArret.setForeground(Color.DARK_GRAY);
		lblDebutArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblDebutArret, "5, 6");

		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_2, "7, 6");

		lblDebutArretVal = new JLabel("");
		lblDebutArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblDebutArretVal, "9, 6");

		JLabel lblMontant = new JLabel("Montant total");
		lblMontant.setForeground(Color.DARK_GRAY);
		lblMontant.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblMontant, "11, 6");

		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.DARK_GRAY);
		label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_3, "13, 6, right, default");

		lblMontantVal = new JLabel();
		lblMontantVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblMontantVal, "15, 6");

		JLabel lblFinArret = new JLabel("Fin de l'arrêt");
		lblFinArret.setForeground(Color.DARK_GRAY);
		lblFinArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblFinArret, "5, 8");

		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_4, "7, 8");

		lblFinArretVal = new JLabel("");
		lblFinArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblFinArretVal, "9, 8");

		JLabel lblRatio = new JLabel("Ratio");
		lblRatio.setForeground(Color.DARK_GRAY);
		lblRatio.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblRatio, "11, 8");

		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.DARK_GRAY);
		label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_5, "13, 8, right, default");

		lblRatioVal = new JLabel();
		lblRatioVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblRatioVal, "15, 8");

		JLabel lblPersonne = new JLabel("Personne en charge");
		lblPersonne.setForeground(Color.DARK_GRAY);
		lblPersonne.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(lblPersonne, "5, 10");

		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSub.add(label_6, "7, 10");

		lblPersonneVal = new JLabel("");
		lblPersonneVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSub.add(lblPersonneVal, "9, 10");

		ongletsDossier = new OngletsDossier(message, 14, 11, true, true, true,
				true, true, true);
		panelSub.add(ongletsDossier, "2, 12, 16, 1, fill, fill");

		JPanel panelColonne = new JPanel();
		add(panelColonne, BorderLayout.WEST);
		panelColonne.setLayout(new BorderLayout(0, 0));

		JScrollPane scrDossiers = new JScrollPane();
		panelColonne.add(scrDossiers);
		scrDossiers.setBorder(new LineBorder(new Color(0, 0, 255)));
		scrDossiers.setPreferredSize(new Dimension(175, 2));
		listeDossiers = new JList<DossierIndemnisation>(dlm);
		listeDossiers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listeDossiers.addListSelectionListener(new SelectionListeListener());

		listeDossiers.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrDossiers.setViewportView(listeDossiers);

		JPanel panelDossiers = new JPanel();
		panelColonne.add(panelDossiers, BorderLayout.NORTH);
		panelDossiers.setBackground(Color.BLUE);
		panelDossiers.setLayout(new BorderLayout(0, 0));

		JLabel lblDossiers = new JLabel("Dossiers");
		lblDossiers.setHorizontalAlignment(SwingConstants.CENTER);
		lblDossiers.setForeground(Color.WHITE);
		lblDossiers.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelDossiers.add(lblDossiers);

		JLabel lblRefresh = new JLabel("");
		lblRefresh.setIcon(new ImageIcon(VisualisationEtatVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		lblRefresh.addMouseListener(new BoutonActualiserListener());
		panelDossiers.add(lblRefresh, BorderLayout.WEST);

		tfRecherche = new JTextField();
		tfRecherche.setForeground(Color.DARK_GRAY);
		tfRecherche.setText("Rechercher");
		panelDossiers.add(tfRecherche, BorderLayout.SOUTH);
		tfRecherche.setActionCommand("");
		tfRecherche.setBorder(new LineBorder(Color.BLUE));
		tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRecherche.setColumns(10);
		tfRecherche.addKeyListener(new RechercheListener());
		tfRecherche.addFocusListener(new FocusRechercheListener());

	}

	/**
	 * Actualise les informations de l'onglet
	 */
	private void actualiser() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(etat, new Date(date.getDate()
							.getTime()), null, null, null));
				} catch (ConnexionException | FichierConfigurationException
						| EchecSQLException e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Toolkit.getDefaultToolkit().beep();
							message.setText(e.getMessage());
						}
					});
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						lblNbDossierVal.setText("");
						lblAssureVal.setText("");
						lblDebutArretVal.setText("");
						lblFinArretVal.setText("");
						lblPersonneVal.setText("");
						lblIndemnitesVal.setText("");
						lblMontantVal.setText("");
						lblRatioVal.setText("");
						ongletsDossier.vider();
						listeDossiers.removeListSelectionListener(listeDossiers
								.getListSelectionListeners()[0]);
						dlm.clear();
						listeDossiers.setSelectedIndex(-1);
						Iterator<DossierIndemnisation> it = liste.iterator();
						while (it.hasNext()) {
							dlm.addElement(it.next());
						}
						listeDossiers
								.addListSelectionListener(new SelectionListeListener());
						if (dlm.getSize() > 0)
							listeDossiers.setSelectedIndex(0);
					}
				});
			}
		}).start();
	}

	/**
	 * Listener de liste
	 */
	public class SelectionListeListener implements ListSelectionListener {

		@Override
		public void valueChanged(final ListSelectionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					final DossierIndemnisation dossier = (DossierIndemnisation) listeDossiers
							.getSelectedValue();
					final boolean arr = dossier.getArretTravail() != null;
					final boolean att = dossier.getAttestation() != null;
					final boolean per = dossier.getPersonneEnCharge() != null;
					final String[] str = { "", "", "", "" };
					if (arr) {
						ArretTravail at = dossier.getArretTravail();
						str[0] = "" + at.getAssure().getId();
						str[1] = "" + at.getDebutArret();
						str[2] = "" + at.getFinArret();
					} else if (att) {
						Attestation at = dossier.getAttestation();
						str[0] = "" + at.getAssure().getId();
						str[1] = "" + at.getDebutArret();
						str[2] = "" + at.getFinArret();
					}
					if (per) {
						str[3] = dossier.getPersonneEnCharge().getPrenom()
								+ " " + dossier.getPersonneEnCharge().getNom();
					} else {
						str[3] = "---";
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							lblNbDossierVal.setText("" + dossier.getId());
							lblAssureVal.setText(str[0]);
							lblDebutArretVal.setText(str[1]);
							lblFinArretVal.setText(str[2]);
							lblPersonneVal.setText(str[3]);
							lblIndemnitesVal.setText(""
									+ dossier.getIndemniteJournaliere()
									+ " €");
							lblMontantVal.setText(""
									+ dossier.getMontantTotal() + " €");
							lblRatioVal.setText("" + dossier.getRatio() + " %");
							ongletsDossier.remplir(dossier);
						}
					});
				}
			}).start();
		}

	}

	/**
	 * Listener de fenêtre
	 */
	public class FenetreListener extends ComponentAdapter {

		@Override
		public void componentShown(ComponentEvent arg0) {
			actualiser();
			message.setText("");
		}

	}

	/**
	 * Listener de bouton
	 */
	public class BoutonActualiserListener extends MouseAdapter {

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
			actualiser();
		}

	}

	/**
	 * Listener de champs de recherche
	 */
	public class RechercheListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					String str = tfRecherche.getText();
					DossiersControleur dc = DossiersControleur.getInstance();
					final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
					try {
						liste.addAll(dc.getDossiers(etat, new Date(date
								.getDate().getTime()), str, null, null));
					} catch (ConnexionException | FichierConfigurationException
							| EchecSQLException e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								Toolkit.getDefaultToolkit().beep();
								message.setText(e.getMessage());
							}
						});
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							lblNbDossierVal.setText("");
							lblAssureVal.setText("");
							lblDebutArretVal.setText("");
							lblFinArretVal.setText("");
							lblPersonneVal.setText("");
							lblIndemnitesVal.setText("");
							lblMontantVal.setText("");
							lblRatioVal.setText("");
							ongletsDossier.vider();
							listeDossiers
									.removeListSelectionListener(listeDossiers
											.getListSelectionListeners()[0]);
							dlm.clear();
							listeDossiers.setSelectedIndex(-1);
							Iterator<DossierIndemnisation> it = liste
									.iterator();
							while (it.hasNext()) {
								dlm.addElement(it.next());
							}
							listeDossiers
									.addListSelectionListener(new SelectionListeListener());
							if (dlm.getSize() > 0)
								listeDossiers.setSelectedIndex(0);
						}
					});
				}
			}).start();
		}

	}

	/**
	 * Listener de focus de champs de recherche
	 */
	public class FocusRechercheListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent arg0) {
			tfRecherche.setText("");
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			tfRecherche.setText("Recherche");
		}

	}

}
