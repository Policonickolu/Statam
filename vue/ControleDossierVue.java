package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objet.ArretTravail;
import objet.Attestation;
import objet.Compte;
import objet.DossierIndemnisation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.DossiersControleur;
import enumeration.Etat;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class ControleDossierVue extends JPanel {

	private static final long serialVersionUID = -4816087171406849713L;
	private JLabel message;
	private Compte compte;
	private OngletsDossier ongletsDossier;
	private JLabel lblAssureVal;
	private JLabel lblDebutArretVal;
	private JLabel lblFinArretVal;
	private JLabel lblPersonneVal;
	private JLabel lblRatioVal;
	private JLabel lblMontantVal;
	private JLabel lblIndemnitesVal;
	private JTextField tfRechercheRecup;
	private JTextField tfRecherche;
	private JLabel lblRefreshAControler;
	private JLabel lblRefreshRecuperes;
	private JLabel lblNbDossierVal;
	private DefaultListModel<DossierIndemnisation> dlmAControler;
	private DefaultListModel<DossierIndemnisation> dlmRecuperes;
	private JList<DossierIndemnisation> listeAControler;
	private JList<DossierIndemnisation> listeRecuperes;
	private JPanel panelSubControleDossier;
	private DossierIndemnisation selection = null;
	private JButton btnPrendreEnCharge;
	private JButton btnAccepter;
	private JButton btnRefuser;

	/**
	 * Constructeur
	 */
	public ControleDossierVue(JLabel message, Compte compte) {
		this.message = message;
		this.compte = compte;
		this.initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {

		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout(0, 0));

		dlmAControler = new DefaultListModel<DossierIndemnisation>();

		dlmRecuperes = new DefaultListModel<DossierIndemnisation>();

		JPanel panelDoubleListe = new JPanel();
		add(panelDoubleListe, BorderLayout.WEST);
		panelDoubleListe.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec
				.decode("175px"), },
				new RowSpec[] { RowSpec.decode("default:grow"),
						RowSpec.decode("default:grow"), }));

		JPanel panel1 = new JPanel();
		panelDoubleListe.add(panel1, "1, 1, fill, fill");
		panel1.setLayout(new BorderLayout(0, 0));

		JPanel panelAControler = new JPanel();
		panel1.add(panelAControler, BorderLayout.NORTH);
		panelAControler.setBackground(Color.BLUE);
		panelAControler.setLayout(new BorderLayout(0, 0));

		lblRefreshAControler = new JLabel("");
		lblRefreshAControler.setIcon(new ImageIcon(ControleDossierVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panelAControler.add(lblRefreshAControler, BorderLayout.WEST);
		lblRefreshAControler.addMouseListener(new BoutonActualiserListener());

		tfRecherche = new JTextField();
		tfRecherche.setForeground(Color.DARK_GRAY);
		tfRecherche.setText("Rechercher");
		panelAControler.add(tfRecherche, BorderLayout.SOUTH);
		tfRecherche.setActionCommand("");
		tfRecherche.setBorder(new LineBorder(Color.BLUE));
		tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRecherche.setColumns(10);

		JLabel lblAControler = new JLabel("Dossiers à contrôler");
		panelAControler.add(lblAControler, BorderLayout.CENTER);
		lblAControler.setHorizontalAlignment(SwingConstants.CENTER);
		lblAControler.setForeground(Color.WHITE);
		lblAControler.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		tfRecherche.addFocusListener(new FocusRechercheListener());
		tfRecherche.addKeyListener(new RechercheListener());

		JScrollPane scrAControler = new JScrollPane();
		panel1.add(scrAControler);
		scrAControler.setBorder(new LineBorder(new Color(0, 0, 255)));
		scrAControler.setPreferredSize(new Dimension(175, 2));
		listeAControler = new JList<DossierIndemnisation>(dlmAControler);
		listeAControler.setBackground(Color.WHITE);
		listeAControler.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrAControler.setViewportView(listeAControler);

		JPanel panel2 = new JPanel();
		panelDoubleListe.add(panel2, "1, 2, fill, fill");
		panel2.setLayout(new BorderLayout(0, 0));

		JPanel panelRecuperes = new JPanel();
		panel2.add(panelRecuperes, BorderLayout.NORTH);
		panelRecuperes.setBackground(Color.BLUE);
		panelRecuperes.setLayout(new BorderLayout(0, 0));

		JLabel lblRecuperes = new JLabel("Dossiers récupérés");
		lblRecuperes.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecuperes.setForeground(Color.WHITE);
		lblRecuperes.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelRecuperes.add(lblRecuperes);

		lblRefreshRecuperes = new JLabel("");
		lblRefreshRecuperes.setIcon(new ImageIcon(ControleDossierVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panelRecuperes.add(lblRefreshRecuperes, BorderLayout.WEST);
		lblRefreshRecuperes.addMouseListener(new BoutonActualiserListener());

		tfRechercheRecup = new JTextField();
		tfRechercheRecup.setForeground(Color.DARK_GRAY);
		tfRechercheRecup.setText("Rechercher");
		panelRecuperes.add(tfRechercheRecup, BorderLayout.SOUTH);
		tfRechercheRecup.setActionCommand("");
		tfRechercheRecup.setBorder(new LineBorder(Color.BLUE));
		tfRechercheRecup.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRechercheRecup.setColumns(10);
		tfRechercheRecup.addFocusListener(new FocusRechercheListener());
		tfRechercheRecup.addKeyListener(new RechercheListener());

		JScrollPane scrRecuperes = new JScrollPane();
		panel2.add(scrRecuperes, BorderLayout.CENTER);
		scrRecuperes.setBorder(new LineBorder(new Color(0, 0, 255)));
		scrRecuperes.setPreferredSize(new Dimension(175, 2));
		listeRecuperes = new JList<DossierIndemnisation>(dlmRecuperes);
		listeRecuperes.setFont(new Font("Verdana", Font.PLAIN, 11));
		listeRecuperes.setBackground(Color.WHITE);
		scrRecuperes.setViewportView(listeRecuperes);
		listeRecuperes.addListSelectionListener(new SelectionListeListener());
		listeRecuperes.addFocusListener(new FocusListeListener());
		listeAControler.addListSelectionListener(new SelectionListeListener());
		listeAControler.addFocusListener(new FocusListeListener());

		panelSubControleDossier = new JPanel();
		panelSubControleDossier.setBackground(Color.WHITE);
		this.add(panelSubControleDossier, BorderLayout.CENTER);
		panelSubControleDossier.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), ColumnSpec.decode("17px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				FormFactory.DEFAULT_COLSPEC, ColumnSpec.decode("6px"),
				ColumnSpec.decode("max(135px;default)"),
				ColumnSpec.decode("15px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"),
				ColumnSpec.decode("max(135px;default)"),
				ColumnSpec.decode("max(17px;default):grow"),
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("23px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), RowSpec.decode("20px"),
				RowSpec.decode("6px"), RowSpec.decode("20px"),
				RowSpec.decode("6px"), RowSpec.decode("20px"),
				RowSpec.decode("6px"), RowSpec.decode("20px"),
				RowSpec.decode("15px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("12px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("fill:max(15px;default):grow"),
				FormFactory.DEFAULT_ROWSPEC, }));

		this.addComponentListener(new FenetreListener());

		JLabel lblNbDossier = new JLabel("Dossier :");
		lblNbDossier.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		lblNbDossier.setForeground(Color.DARK_GRAY);
		panelSubControleDossier.add(lblNbDossier, "3, 2");

		lblNbDossierVal = new JLabel("0000001");
		lblNbDossierVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblNbDossierVal.setForeground(Color.BLACK);
		panelSubControleDossier.add(lblNbDossierVal, "5, 2");

		JLabel lblAssure = new JLabel("Assuré");
		lblAssure.setForeground(Color.DARK_GRAY);
		lblAssure.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblAssure, "5, 4");

		JLabel label = new JLabel(":");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label, "7, 4");

		lblAssureVal = new JLabel("000000025");
		lblAssureVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblAssureVal, "9, 4");

		JLabel lblIndemnites = new JLabel("Indemnités journalière");
		lblIndemnites.setForeground(Color.DARK_GRAY);
		lblIndemnites.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblIndemnites, "11, 4");

		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_1, "13, 4");

		lblIndemnitesVal = new JLabel("0");
		lblIndemnitesVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblIndemnitesVal, "15, 4");

		JLabel lblDebutArret = new JLabel("Début de l'arrêt");
		lblDebutArret.setForeground(Color.DARK_GRAY);
		lblDebutArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblDebutArret, "5, 6");

		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_2, "7, 6");

		lblDebutArretVal = new JLabel("20-04-2013");
		lblDebutArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblDebutArretVal, "9, 6");

		JLabel lblMontant = new JLabel("Montant total");
		lblMontant.setForeground(Color.DARK_GRAY);
		lblMontant.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblMontant, "11, 6");

		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.DARK_GRAY);
		label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_3, "13, 6");

		lblMontantVal = new JLabel("0");
		lblMontantVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblMontantVal, "15, 6");

		JLabel lblFinArret = new JLabel("Fin de l'arrêt");
		lblFinArret.setForeground(Color.DARK_GRAY);
		lblFinArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblFinArret, "5, 8");

		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_4, "7, 8");

		lblFinArretVal = new JLabel("25-04-2013");
		lblFinArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblFinArretVal, "9, 8");

		JLabel lblRatio = new JLabel("Ratio");
		lblRatio.setForeground(Color.DARK_GRAY);
		lblRatio.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblRatio, "11, 8");

		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.DARK_GRAY);
		label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_5, "13, 8");

		lblRatioVal = new JLabel("0%");
		lblRatioVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblRatioVal, "15, 8");

		JLabel lblPersonne = new JLabel("Personne en charge");
		lblPersonne.setForeground(Color.DARK_GRAY);
		lblPersonne.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(lblPersonne, "5, 10");

		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(label_6, "7, 10");

		lblPersonneVal = new JLabel("Jean Sapin");
		lblPersonneVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubControleDossier.add(lblPersonneVal, "9, 10");

		btnPrendreEnCharge = new JButton("Prendre en charge");
		btnPrendreEnCharge.setEnabled(false);
		btnPrendreEnCharge.setForeground(Color.DARK_GRAY);
		btnPrendreEnCharge.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				14));
		panelSubControleDossier.add(btnPrendreEnCharge, "9, 12");
		btnPrendreEnCharge.addActionListener(new BoutonCharge());

		btnAccepter = new JButton("Accepter");
		btnAccepter.setEnabled(false);
		btnAccepter.setForeground(Color.DARK_GRAY);
		btnAccepter.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(btnAccepter, "11, 12");
		btnAccepter.addActionListener(new BoutonChoix(this));

		btnRefuser = new JButton("Refuser");
		btnRefuser.setEnabled(false);
		btnRefuser.setForeground(Color.DARK_GRAY);
		btnRefuser.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubControleDossier.add(btnRefuser, "11, 14");
		btnRefuser.addActionListener(new BoutonChoix(this));

		ongletsDossier = new OngletsDossier(message, 14, 11, true, true, true,
				true, true, true);
		panelSubControleDossier.add(ongletsDossier,
				"2, 16, 15, 1, fill, default");
	}

	/**
	 * Actualise les informations de l'onglet
	 */
	private void actualiserAControler() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(Etat.A_CONTROLER, null, null,
							null, null));
				} catch (ConnexionException | EchecSQLException
						| FichierConfigurationException e) {
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
						listeAControler
								.removeListSelectionListener(listeAControler
										.getListSelectionListeners()[0]);
						dlmAControler.clear();
						listeAControler.setSelectedIndex(-1);
						Iterator<DossierIndemnisation> it = liste.iterator();
						while (it.hasNext()) {
							dlmAControler.addElement(it.next());
						}
						listeAControler
								.addListSelectionListener(new SelectionListeListener());
					}
				});
			}
		}).start();
	}

	/**
	 * Actualise les informations de l'onglet
	 */
	private void actualiserRecuperes() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(Etat.EN_CONTROLE, null, null,
							null, compte));
				} catch (ConnexionException | EchecSQLException
						| FichierConfigurationException e) {
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
						listeRecuperes
								.removeListSelectionListener(listeRecuperes
										.getListSelectionListeners()[0]);
						dlmRecuperes.clear();
						listeRecuperes.setSelectedIndex(-1);
						Iterator<DossierIndemnisation> it = liste.iterator();
						while (it.hasNext()) {
							dlmRecuperes.addElement(it.next());
						}
						listeRecuperes
								.addListSelectionListener(new SelectionListeListener());
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
					if (arg0.getSource() == listeAControler) {
						selection = listeAControler.getSelectedValue();
					} else if (arg0.getSource() == listeRecuperes) {
						selection = listeRecuperes.getSelectedValue();
					} else {
						selection = new DossierIndemnisation();
					}
					final boolean arr = selection.getArretTravail() != null;
					final boolean att = selection.getAttestation() != null;
					final boolean per = selection.getPersonneEnCharge() != null;
					final String[] str = { "", "", "", "" };
					if (arr) {
						ArretTravail at = selection.getArretTravail();
						str[0] = "" + at.getAssure().getId();
						str[1] = "" + at.getDebutArret();
						str[2] = "" + at.getFinArret();
					} else if (att) {
						Attestation at = selection.getAttestation();
						str[0] = "" + at.getAssure().getId();
						str[1] = "" + at.getDebutArret();
						str[2] = "" + at.getFinArret();
					}
					if (per) {
						str[3] = selection.getPersonneEnCharge().getPrenom()
								+ " "
								+ selection.getPersonneEnCharge().getNom();
					} else {
						str[3] = "---";
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							lblNbDossierVal.setText("" + selection.getId());
							lblAssureVal.setText(str[0]);
							lblDebutArretVal.setText(str[1]);
							lblFinArretVal.setText(str[2]);
							lblPersonneVal.setText(str[3]);
							if (arg0.getSource() == listeAControler) {
								lblIndemnitesVal.setText(""
										+ selection.getIndemniteJournaliere()
										+ " €");
								lblMontantVal.setText(""
										+ selection.getMontantTotal()
										+ " €");
								lblRatioVal.setText("" + selection.getRatio()
										+ " %");
								btnPrendreEnCharge.setText("Prendre en charge");
								btnAccepter.setEnabled(false);
								btnRefuser.setEnabled(false);
								btnPrendreEnCharge.setEnabled(true);
							} else if (arg0.getSource() == listeRecuperes) {
								lblIndemnitesVal.setText(""
										+ selection.getIndemniteJournaliere());
								lblMontantVal.setText(""
										+ selection.getMontantTotal());
								lblRatioVal.setText("" + selection.getRatio());
								btnPrendreEnCharge.setText("Abandonner");
								btnAccepter.setEnabled(true);
								btnRefuser.setEnabled(true);
								btnPrendreEnCharge.setEnabled(true);
							}
							ongletsDossier.remplir(selection);
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
			actualiserAControler();
			actualiserRecuperes();
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
			label.requestFocus();
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
			if (arg0.getSource() == lblRefreshAControler) {
				actualiserAControler();
			} else if (arg0.getSource() == lblRefreshRecuperes) {
				actualiserRecuperes();
			}
		}

	}

	/**
	 * Listener de champs de recherche
	 */
	public class RechercheListener extends KeyAdapter {

		@Override
		public void keyTyped(final KeyEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					String str = "";
					if (arg0.getSource() == tfRecherche) {
						str = tfRecherche.getText();
					} else if (arg0.getSource() == tfRechercheRecup) {
						str = tfRechercheRecup.getText();
					}
					DossiersControleur dc = DossiersControleur.getInstance();
					final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
					try {
						if (arg0.getSource() == tfRecherche) {
							liste.addAll(dc.getDossiers(Etat.A_CONTROLER, null,
									str, null, null));
						} else if (arg0.getSource() == tfRechercheRecup) {
							liste.addAll(dc.getDossiers(Etat.EN_CONTROLE, null,
									str, null, compte));
						}
					} catch (ConnexionException | EchecSQLException
							| FichierConfigurationException e) {
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
							if (arg0.getSource() == tfRecherche) {
								listeAControler
										.removeListSelectionListener(listeAControler
												.getListSelectionListeners()[0]);
								dlmAControler.clear();
								listeAControler.setSelectedIndex(-1);
								Iterator<DossierIndemnisation> it = liste
										.iterator();
								while (it.hasNext()) {
									dlmAControler.addElement(it.next());
								}
								listeAControler
										.addListSelectionListener(new SelectionListeListener());
							} else if (arg0.getSource() == tfRechercheRecup) {
								listeRecuperes
										.removeListSelectionListener(listeRecuperes
												.getListSelectionListeners()[0]);
								dlmRecuperes.clear();
								listeRecuperes.setSelectedIndex(-1);
								Iterator<DossierIndemnisation> it = liste
										.iterator();
								while (it.hasNext()) {
									dlmRecuperes.addElement(it.next());
								}
								listeRecuperes
										.addListSelectionListener(new SelectionListeListener());
							}
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
			if (arg0.getSource() == tfRecherche) {
				tfRecherche.setText("");
			} else if (arg0.getSource() == tfRechercheRecup) {
				tfRechercheRecup.setText("");
			}
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if (arg0.getSource() == tfRecherche) {
				tfRecherche.setText("Recherche");
			} else if (arg0.getSource() == tfRechercheRecup) {
				tfRechercheRecup.setText("Recherche");
			}
		}

	}

	/**
	 * Listener de focus de liste
	 */
	public class FocusListeListener extends FocusAdapter {

		@Override
		public void focusGained(FocusEvent arg0) {
			if (arg0.getSource() == listeAControler) {
				listeRecuperes.removeListSelectionListener(listeRecuperes
						.getListSelectionListeners()[0]);
				listeRecuperes.clearSelection();
				listeRecuperes
						.addListSelectionListener(new SelectionListeListener());
			} else if (arg0.getSource() == listeRecuperes) {
				listeAControler.removeListSelectionListener(listeAControler
						.getListSelectionListeners()[0]);
				listeAControler.clearSelection();
				listeAControler
						.addListSelectionListener(new SelectionListeListener());
			}
		}

	}

	/**
	 * Listener de bouton
	 */
	public class BoutonCharge implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					JButton btn = (JButton) arg0.getSource();
					if (btn.getText().equals("Prendre en charge")) {
						DossiersControleur dc = DossiersControleur
								.getInstance();
						try {
							if (listeAControler.getSelectedValue() != null)
								dc.prendreEnCharge(
										listeAControler.getSelectedValue(),
										Etat.EN_CONTROLE, compte);
						} catch (final ConnexionException
								| FichierConfigurationException
								| EchecSQLException e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Toolkit.getDefaultToolkit().beep();
									message.setText(e.getMessage());
								}
							});
						}
					} else if (btn.getText().equals("Abandonner")) {
						DossiersControleur dc = DossiersControleur
								.getInstance();
						try {
							if (listeRecuperes.getSelectedValue() != null)
								dc.prendreEnCharge(
										listeRecuperes.getSelectedValue(),
										Etat.A_CONTROLER, null);
						} catch (final ConnexionException
								| FichierConfigurationException
								| EchecSQLException e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Toolkit.getDefaultToolkit().beep();
									message.setText(e.getMessage());
								}
							});
						}
					}
					actualiserAControler();
					actualiserRecuperes();
				}
			}).start();
		}

	}

	/**
	 * Listener de bouton
	 */
	public class BoutonChoix implements ActionListener {

		JComponent compo;

		public BoutonChoix(JComponent c) {
			this.compo = c;
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					DossiersControleur dc = DossiersControleur.getInstance();
					try {
						if (arg0.getSource() == btnAccepter) {

							int res = JOptionPane
									.showConfirmDialog(
											compo,
											"Voulez-vous vraiment accepter l'indemnisation de ce dossier ?\nCette action est irréversible.",
											"Confirmation",
											JOptionPane.OK_CANCEL_OPTION);
							if (res == JOptionPane.OK_OPTION) {
								dc.choixControle(selection, true);
							}
						} else if (arg0.getSource() == btnRefuser) {
							int res = JOptionPane
									.showConfirmDialog(
											compo,
											"Voulez-vous vraiment refuser l'indemnisation de ce dossier ?\nCette action est irréversible.",
											"Confirmation",
											JOptionPane.OK_CANCEL_OPTION);
							if (res == JOptionPane.OK_OPTION) {
								dc.choixControle(selection, false);
							}
						}
					} catch (final ConnexionException
							| FichierConfigurationException | EchecSQLException e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								Toolkit.getDefaultToolkit().beep();
								message.setText(e.getMessage());
							}
						});
					}
					actualiserAControler();
					actualiserRecuperes();
				}
			}).start();
		}

	}

}
