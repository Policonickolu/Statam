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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.JTextComponent;

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

public class TraitementManuelVue extends JPanel {

	private static final long serialVersionUID = 4073919153631227007L;
	private JLabel message;
	private Compte compte;
	private JTextField tfRecherche;
	private JTextField tfRechercheRecup;
	private DefaultListModel<DossierIndemnisation> dlmATraiter;
	private DefaultListModel<DossierIndemnisation> dlmRecuperes;
	private JList<DossierIndemnisation> listeRecuperes;
	private JList<DossierIndemnisation> listeATraiter;
	private JLabel lblRefreshATraiter;
	private JLabel lblRefreshRecuperes;
	private JLabel lblNbDossierVal;
	private JLabel lblAssureVal;
	private JLabel lblDebutArretVal;
	private JLabel lblFinArretVal;
	private JLabel lblPersonneVal;
	private JFormattedTextField tfIndemnitesVal;
	private JFormattedTextField tfMontantVal;
	private JFormattedTextField tfRatioVal;
	private OngletsDossier ongletsDossier;
	private JButton btnPrendreEnCharge;
	private JButton btnValider;
	private JButton btnCalcul;
	private DossierIndemnisation selection = null;

	/**
	 * Constructeur
	 */
	public TraitementManuelVue(JLabel message, Compte compte) {
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

		JPanel panelDoubleListe = new JPanel();
		this.add(panelDoubleListe, BorderLayout.WEST);
		panelDoubleListe.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec
				.decode("175px:grow"), }, new RowSpec[] {
				RowSpec.decode("2px:grow"), RowSpec.decode("2px:grow"), }));

		dlmATraiter = new DefaultListModel<DossierIndemnisation>();
		dlmRecuperes = new DefaultListModel<DossierIndemnisation>();
		
		JPanel panel1 = new JPanel();
		panelDoubleListe.add(panel1, "1, 1, fill, fill");
		panel1.setLayout(new BorderLayout(0, 0));

		JPanel panelATraiter = new JPanel();
		panel1.add(panelATraiter, BorderLayout.NORTH);
		panelATraiter.setBackground(Color.BLUE);
		panelATraiter.setLayout(new BorderLayout(0, 0));

		JLabel lblATraiter = new JLabel("Dossiers à traiter");
		lblATraiter.setHorizontalAlignment(SwingConstants.CENTER);
		lblATraiter.setForeground(Color.WHITE);
		lblATraiter.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelATraiter.add(lblATraiter);

		lblRefreshATraiter = new JLabel("");
		lblRefreshATraiter.setIcon(new ImageIcon(TraitementManuelVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panelATraiter.add(lblRefreshATraiter, BorderLayout.WEST);
		lblRefreshATraiter.addMouseListener(new BoutonActualiserListener());

		tfRecherche = new JTextField();
		tfRecherche.setForeground(Color.DARK_GRAY);
		tfRecherche.setText("Rechercher");
		panelATraiter.add(tfRecherche, BorderLayout.SOUTH);
		tfRecherche.setActionCommand("");
		tfRecherche.setBorder(new LineBorder(Color.BLUE));
		tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRecherche.setColumns(10);

		JScrollPane scrATraiter = new JScrollPane();
		panel1.add(scrATraiter, BorderLayout.CENTER);
		scrATraiter.setPreferredSize(new Dimension(175, 2));
		scrATraiter.setBorder(new LineBorder(new Color(0, 0, 255)));
		listeATraiter = new JList<DossierIndemnisation>(dlmATraiter);
		listeATraiter.setBackground(Color.WHITE);
		listeATraiter.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrATraiter.setViewportView(listeATraiter);
		listeATraiter.addFocusListener(new FocusListeListener());
		listeATraiter.addListSelectionListener(new SelectionListeListener());
		tfRecherche.addFocusListener(new FocusRechercheListener());
		tfRecherche.addKeyListener(new RechercheListener());

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
		lblRefreshRecuperes.setIcon(new ImageIcon(TraitementManuelVue.class
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

		JScrollPane scrRecuperes = new JScrollPane();
		panel2.add(scrRecuperes, BorderLayout.CENTER);
		scrRecuperes.setPreferredSize(new Dimension(175, 2));
		scrRecuperes.setBorder(new LineBorder(new Color(0, 0, 255)));
		listeRecuperes = new JList<DossierIndemnisation>(dlmRecuperes);
		listeRecuperes.setBackground(Color.WHITE);
		listeRecuperes.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrRecuperes.setViewportView(listeRecuperes);
		listeRecuperes.addFocusListener(new FocusListeListener());
		listeRecuperes.addListSelectionListener(new SelectionListeListener());
		tfRechercheRecup.addFocusListener(new FocusRechercheListener());
		tfRechercheRecup.addKeyListener(new RechercheListener());

		JPanel panelSubTraitementManuel = new JPanel();
		panelSubTraitementManuel.setBackground(Color.WHITE);
		this.add(panelSubTraitementManuel, BorderLayout.CENTER);
		panelSubTraitementManuel.setLayout(new FormLayout(new ColumnSpec[] {
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

		JLabel lblNbDossier = new JLabel("Dossier :");
		lblNbDossier.setForeground(Color.DARK_GRAY);
		lblNbDossier.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelSubTraitementManuel.add(lblNbDossier, "3, 2");

		lblNbDossierVal = new JLabel("0000030");
		lblNbDossierVal.setForeground(Color.BLACK);
		lblNbDossierVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(lblNbDossierVal, "5, 2");

		JLabel lblAssure = new JLabel("Assuré");
		lblAssure.setForeground(Color.DARK_GRAY);
		lblAssure.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblAssure, "5, 4");

		JLabel label = new JLabel(":");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label, "7, 4");

		lblAssureVal = new JLabel("000000020");
		lblAssureVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(lblAssureVal, "9, 4");

		JLabel lblIndemnites = new JLabel("Indemnités journalière");
		lblIndemnites.setForeground(Color.DARK_GRAY);
		lblIndemnites.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblIndemnites, "11, 4");

		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_1, "13, 4, right, default");

		tfIndemnitesVal = new JFormattedTextField();
		tfIndemnitesVal.setEditable(false);
		tfIndemnitesVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(tfIndemnitesVal, "15, 4");
		tfIndemnitesVal.setColumns(10);
		tfIndemnitesVal.addKeyListener(new ChangementChampsListener());
		tfIndemnitesVal.addFocusListener(new IndemnitesFocusListener());

		JLabel lblDebutArret = new JLabel("Début de l'arrêt");
		lblDebutArret.setForeground(Color.DARK_GRAY);
		lblDebutArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblDebutArret, "5, 6");

		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_2, "7, 6");

		lblDebutArretVal = new JLabel("20-04-2013");
		lblDebutArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(lblDebutArretVal, "9, 6");

		JLabel lblMontant = new JLabel("Montant total");
		lblMontant.setForeground(Color.DARK_GRAY);
		lblMontant.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblMontant, "11, 6");

		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.DARK_GRAY);
		label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_3, "13, 6, right, default");

		tfMontantVal = new JFormattedTextField();
		tfMontantVal.setEditable(false);
		tfMontantVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(tfMontantVal, "15, 6");
		tfMontantVal.setColumns(10);
		tfMontantVal.addKeyListener(new ChangementChampsListener());
		tfMontantVal.addFocusListener(new IndemnitesFocusListener());

		JLabel lblFinArret = new JLabel("Fin de l'arrêt");
		lblFinArret.setForeground(Color.DARK_GRAY);
		lblFinArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblFinArret, "5, 8");

		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_4, "7, 8");

		lblFinArretVal = new JLabel("25-04-2013");
		lblFinArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(lblFinArretVal, "9, 8");

		JLabel lblRatio = new JLabel("Ratio");
		lblRatio.setForeground(Color.DARK_GRAY);
		lblRatio.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblRatio, "11, 8");

		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.DARK_GRAY);
		label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_5, "13, 8, right, default");

		tfRatioVal = new JFormattedTextField();
		tfRatioVal.setEditable(false);
		tfRatioVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(tfRatioVal, "15, 8");
		tfRatioVal.setColumns(10);
		tfRatioVal.addFocusListener(new IndemnitesFocusListener());

		JLabel lblPersonne = new JLabel("Personne en charge");
		lblPersonne.setForeground(Color.DARK_GRAY);
		lblPersonne.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(lblPersonne, "5, 10");

		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(label_6, "7, 10");

		lblPersonneVal = new JLabel("Jean Sapin");
		lblPersonneVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubTraitementManuel.add(lblPersonneVal, "9, 10");

		btnPrendreEnCharge = new JButton("Prendre en charge");
		btnPrendreEnCharge.setEnabled(false);
		btnPrendreEnCharge.setForeground(Color.DARK_GRAY);
		btnPrendreEnCharge.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				14));
		btnPrendreEnCharge.addActionListener(new BoutonCharge());
		panelSubTraitementManuel.add(btnPrendreEnCharge, "9, 12");

		btnCalcul = new JButton("Calculer");
		btnCalcul.setEnabled(false);
		btnCalcul.setForeground(Color.DARK_GRAY);
		btnCalcul.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(btnCalcul, "11, 12");
		btnCalcul.addActionListener(new CalculListener());

		btnValider = new JButton("Valider");
		btnValider.setEnabled(false);
		btnValider.setForeground(Color.DARK_GRAY);
		btnValider.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubTraitementManuel.add(btnValider, "11, 14");
		btnValider.addActionListener(new BoutonValider(this));

		ongletsDossier = new OngletsDossier(message, 14, 11, true, true, true,
				true, true, true);
		panelSubTraitementManuel.add(ongletsDossier,
				"2, 16, 15, 1, fill, default");

		this.addComponentListener(new FenetreListener());

	}

	/**
	 * Actualiser la liste des dossier à traiter
	 */
	private void actualiserATraiter() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(Etat.A_TRAITER_MANUELLEMENT,
							null, null, null, null));
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
						tfIndemnitesVal.setText("");
						tfMontantVal.setText("");
						tfRatioVal.setText("");
						ongletsDossier.vider();
						listeATraiter.removeListSelectionListener(listeATraiter
								.getListSelectionListeners()[0]);
						dlmATraiter.clear();
						listeATraiter.setSelectedIndex(-1);
						Iterator<DossierIndemnisation> it = liste.iterator();
						while (it.hasNext()) {
							dlmATraiter.addElement(it.next());
						}
						listeATraiter
								.addListSelectionListener(new SelectionListeListener());
					}
				});
			}
		}).start();
	}

	/**
	 * Actualise la liste des dossiers récupérés
	 */
	private void actualiserRecuperes() {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(Etat.EN_TRAITEMENT_MANUEL,
							null, null, null, compte));
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
						tfIndemnitesVal.setText("");
						tfMontantVal.setText("");
						tfRatioVal.setText("");
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
					if (arg0.getSource() == listeATraiter) {
						selection = listeATraiter.getSelectedValue();
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
							if (arg0.getSource() == listeATraiter) {
								tfIndemnitesVal.setText(""
										+ selection.getIndemniteJournaliere()
										+ " €");
								tfMontantVal.setText(""
										+ selection.getMontantTotal()
										+ " €");
								tfRatioVal.setText("" + selection.getRatio()
										+ " %");
								btnPrendreEnCharge.setText("Prendre en charge");
								tfIndemnitesVal.setEditable(false);
								tfMontantVal.setEditable(false);
								tfRatioVal.setEditable(false);
								btnValider.setEnabled(false);
								btnCalcul.setEnabled(false);
								btnPrendreEnCharge.setEnabled(true);
							} else if (arg0.getSource() == listeRecuperes) {
								tfIndemnitesVal.setText(""
										+ selection.getIndemniteJournaliere());
								tfMontantVal.setText(""
										+ selection.getMontantTotal());
								tfRatioVal.setText("" + selection.getRatio());
								btnPrendreEnCharge.setText("Abandonner");
								tfIndemnitesVal.setEditable(true);
								tfMontantVal.setEditable(true);
								tfRatioVal.setEditable(true);
								btnValider.setEnabled(true);
								btnCalcul.setEnabled(true);
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
			actualiserATraiter();
			actualiserRecuperes();
			message.setText("");
		}

	}

	/**
	 * Listener du bouton de rafraîchissement
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
			if (arg0.getSource() == lblRefreshATraiter) {
				actualiserATraiter();
			} else if (arg0.getSource() == lblRefreshRecuperes) {
				actualiserRecuperes();
			}
		}

	}

	/**
	 * Listener du champs de recherche
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
							liste.addAll(dc.getDossiers(
									Etat.A_TRAITER_MANUELLEMENT, null, str,
									null, null));
						} else if (arg0.getSource() == tfRechercheRecup) {
							liste.addAll(dc.getDossiers(
									Etat.EN_TRAITEMENT_MANUEL, null, str, null,
									compte));
						}
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
							lblNbDossierVal.setText("");
							lblAssureVal.setText("");
							lblDebutArretVal.setText("");
							lblFinArretVal.setText("");
							lblPersonneVal.setText("");
							tfIndemnitesVal.setText("");
							tfMontantVal.setText("");
							tfRatioVal.setText("");
							ongletsDossier.vider();
							if (arg0.getSource() == tfRecherche) {
								listeATraiter
										.removeListSelectionListener(listeATraiter
												.getListSelectionListeners()[0]);
								dlmATraiter.clear();
								listeATraiter.setSelectedIndex(-1);
								Iterator<DossierIndemnisation> it = liste
										.iterator();
								while (it.hasNext()) {
									dlmATraiter.addElement(it.next());
								}
								listeATraiter
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
	 * Listener du champs de focus de recherche
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
			if (arg0.getSource() == listeATraiter) {
				listeRecuperes.removeListSelectionListener(listeRecuperes
						.getListSelectionListeners()[0]);
				listeRecuperes.clearSelection();
				listeRecuperes
						.addListSelectionListener(new SelectionListeListener());
			} else if (arg0.getSource() == listeRecuperes) {
				listeATraiter.removeListSelectionListener(listeATraiter
						.getListSelectionListeners()[0]);
				listeATraiter.clearSelection();
				listeATraiter
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
							if (listeATraiter.getSelectedValue() != null)
								dc.prendreEnCharge(
										listeATraiter.getSelectedValue(),
										Etat.EN_TRAITEMENT_MANUEL, compte);
						} catch (final ConnexionException | EchecSQLException
								| FichierConfigurationException e) {
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
										Etat.A_TRAITER_MANUELLEMENT, null);
						} catch (final ConnexionException | EchecSQLException
								| FichierConfigurationException e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Toolkit.getDefaultToolkit().beep();
									message.setText(e.getMessage());
								}
							});
						}
					}
					actualiserATraiter();
					actualiserRecuperes();
				}
			}).start();
		}

	}

	/**
	 * Listener de bouton
	 */
	public class BoutonValider implements ActionListener {

		JComponent compo;

		public BoutonValider(JComponent c) {
			this.compo = c;
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					DossiersControleur dc = DossiersControleur.getInstance();
					double indemnites = Double.parseDouble(tfIndemnitesVal
							.getText().replace(',', '.'));
					double montant = Double.parseDouble(tfMontantVal.getText()
							.replace(',', '.'));
					double ratio = Double.parseDouble(tfRatioVal.getText()
							.replace(',', '.'));
					try {
						int res = JOptionPane
								.showConfirmDialog(
										compo,
										"Voulez-vous vraiment valider le traitement de ce dossier ?\nCette action est irréversible.",
										"Confirmation",
										JOptionPane.OK_CANCEL_OPTION);
						if (res == JOptionPane.OK_OPTION) {
							dc.traiterManuellement(selection, indemnites,
									montant, ratio);
						}
					} catch (final ConnexionException | EchecSQLException
							| FichierConfigurationException e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								Toolkit.getDefaultToolkit().beep();
								message.setText(e.getMessage());
							}
						});
					}
					actualiserATraiter();
					actualiserRecuperes();
				}
			}).start();

		}
	}

	/**
	 * Listener de champs de texte
	 */
	public class ChangementChampsListener extends KeyAdapter {

		@Override
		public void keyTyped(final KeyEvent arg0) {
			if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
				JComponent compo = (JComponent) arg0.getSource();
				if (compo == tfIndemnitesVal) {
					tfMontantVal.requestFocus();
				} else if (compo == tfMontantVal) {
					tfRatioVal.requestFocus();
				}
			}
		}

	}

	/**
	 * Listener de focus de champs de texte
	 */
	public class IndemnitesFocusListener extends FocusAdapter {

		@Override
		public void focusLost(final FocusEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					DecimalFormat f = new DecimalFormat();
					DecimalFormatSymbols dfs = f.getDecimalFormatSymbols();
					dfs.setGroupingSeparator(' ');
					f.setDecimalFormatSymbols(dfs);
					f.setMaximumFractionDigits(2);
					f.setMinimumFractionDigits(2);
					f.setGroupingUsed(false);
					JTextComponent tf = (JTextComponent) arg0.getSource();
					String[] str = ((!tf.getText().isEmpty() ? tf.getText()
							: "0") + ".00").replace(" ", "").replace("-", "")
							.split("\\D+");
					String s1 = str[0];
					double i = Double.parseDouble(s1);
					double d = Double.parseDouble("0." + str[1]);
					if (tf == tfRatioVal) {
						if (i + 1 > 100.00) {
							s1 = "100";
							d = 0.00d;
						} else if (i < 0) {
							s1 = "0";
							d = 0.00d;
						}
					}
					String s2 = f.format(d).substring(2);
					while (!s1.isEmpty() && s1.toCharArray()[0] == '0') {
						s1 = s1.substring(1);
					}
					tf.setText((!s1.isEmpty() ? s1 : "0") + "," + s2);
					s1 = tfIndemnitesVal.getText();
					s2 = tfMontantVal.getText();
					if (!s1.isEmpty())
						s1 = (s1.toCharArray()[0] == '-' ? s1.substring(1) : s1);
					if (!s2.isEmpty())
						s2 = (s2.toCharArray()[0] == '-' ? s2.substring(1) : s2);
					String[] strTabIndemnites = ("0" + s1 + ".00").replace(" ",
							"").split("\\D+");
					String[] strTabMontant = ("0" + s2 + ".00")
							.replace(" ", "").split("\\D+");
					String strIndemnites = strTabIndemnites[0] + "."
							+ strTabIndemnites[1];
					String strMontant = strTabMontant[0] + "."
							+ strTabMontant[1];
					final double indemnites = (!strIndemnites.isEmpty() ? Double
							.parseDouble(strIndemnites) : 0);
					final double montant = (!strMontant.isEmpty() ? Double
							.parseDouble(strMontant) : 0);
					final DossierIndemnisation dossier = listeRecuperes
							.getSelectedValue();
					if (arg0.getSource() == tfIndemnitesVal) {
						double d1 = indemnites
								* (((dossier.getArretTravail().getFinArret()
										.getTime() - dossier.getArretTravail()
										.getDebutArret().getTime()) / 1000.00 / 60.00 / 60.00 / 24.00) + 1);
						final String s = f.format(d1);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tfMontantVal.setText(s.replace(" ", "")
										.replace('.', ','));
							}
						});
					} else if (arg0.getSource() == tfMontantVal) {
						double d1 = montant
								/ (((dossier.getArretTravail().getFinArret()
										.getTime() - dossier.getArretTravail()
										.getDebutArret().getTime()) / 1000.00 / 60.00 / 60.00 / 24.00) + 1);
						final String s = f.format(d1);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tfIndemnitesVal.setText(s.replace(" ", "")
										.replace('.', ','));
							}
						});
					}
				}
			}).start();
		}

	}

	/**
	 * Listener de bouton
	 */
	public class CalculListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (selection != null) {
				new Thread(new Runnable() {
					public void run() {
						String s = tfRatioVal.getText();
						double ratio = (!s.isEmpty() ? Double.parseDouble(s
								.replace(',', '.')) : 0);
						double indemnites = (selection.getAttestation()
								.getSalairesPercus() / 180) * (ratio / 100);
						double montant = indemnites
								* (((selection.getAttestation().getFinArret()
										.getTime() - selection.getAttestation()
										.getDebutArret().getTime()) / 1000 / 60 / 60 / 24) + 1);
						final DecimalFormat f = new DecimalFormat();
						DecimalFormatSymbols dfs = f.getDecimalFormatSymbols();
						dfs.setGroupingSeparator(' ');
						f.setDecimalFormatSymbols(dfs);
						f.setMaximumFractionDigits(2);
						f.setMinimumFractionDigits(2);
						f.setGroupingUsed(false);
						final String smon = f.format(indemnites);
						final String sind = f.format(montant);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tfIndemnitesVal.setText(sind.replace(" ", "")
										.replace('.', ','));
								tfMontantVal.setText(smon.replace(" ", "")
										.replace('.', ','));
							}
						});
					}
				}).start();
			}
		}

	}
	
}
