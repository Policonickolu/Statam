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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objet.CritereAge;
import objet.CritereEmploi;
import objet.CritereInterface;
import objet.CritereJoursArret;
import objet.CritereMotifMedical;
import objet.CritereSecteurActivite;
import objet.Filtre;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.FiltresControleur;
import enumeration.Comparateur;
import enumeration.Emploi;
import enumeration.Motif;
import enumeration.SecteurActivite;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class GestionSystemeControleVue extends JPanel {

	private static final long serialVersionUID = -1873436222718439474L;
	private JLabel message;
	private JTextField tfNomFiltre;
	private JTextField tfValeur;
	private JLabel lblRefresh;
	private JTextField tfRecherche;
	private JButton btnValiderFiltre;
	private JButton btnSupprimerCritere;
	private JButton btnNouveauFiltre;
	private JButton btnSupprimerFiltre;
	private JLabel lblMessageFiltre;
	private JButton btnValiderCritere;
	private JComboBox<String> cbType;
	private JComboBox<String> cbComparateur;
	private DefaultListModel<Filtre> dlmFiltres;
	private DefaultListModel<CritereInterface> dlmCriteres;
	private JList<Filtre> listeFiltres;
	private JList<CritereInterface> listeCriteres;
	private JCheckBox checkbActive;
	private JComboBox<String> cbEmploi;
	private JComboBox<String> cbMotif;
	private JComboBox<String> cbSecteur;
	private Filtre filtreSelection = new Filtre();
	private CritereInterface critereSelection;
	private JPanel panel;

	/**
	 * Constructeur
	 */
	public GestionSystemeControleVue(JLabel message) {
		this.message = message;
		this.initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {

		this.setBackground(Color.WHITE);

		this.setLayout(new BorderLayout(0, 0));

		Comparateur[] comparateur = Comparateur.values();
		String[] comstr = new String[comparateur.length];
		for (int i = 0; i < comparateur.length; i++) {
			comstr[i] = comparateur[i].getNom();
		}

		dlmFiltres = new DefaultListModel<Filtre>();
		dlmCriteres = new DefaultListModel<CritereInterface>();

		panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panelFiltres = new JPanel();
		panel.add(panelFiltres, BorderLayout.NORTH);
		panelFiltres.setBackground(Color.BLUE);
		panelFiltres.setLayout(new BorderLayout(0, 0));

		JLabel lblFiltres = new JLabel("Filtres");
		lblFiltres.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiltres.setForeground(Color.WHITE);
		lblFiltres.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelFiltres.add(lblFiltres);

		lblRefresh = new JLabel("");
		lblRefresh.setIcon(new ImageIcon(GestionSystemeControleVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panelFiltres.add(lblRefresh, BorderLayout.WEST);
		lblRefresh.addMouseListener(new BoutonActualiserListener());

		tfRecherche = new JTextField();
		tfRecherche.setForeground(Color.DARK_GRAY);
		tfRecherche.setText("Rechercher");
		panelFiltres.add(tfRecherche, BorderLayout.SOUTH);
		tfRecherche.setActionCommand("");
		tfRecherche.setBorder(new LineBorder(Color.BLUE));
		tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRecherche.setColumns(10);

		JScrollPane scrFiltres = new JScrollPane();
		panel.add(scrFiltres, BorderLayout.CENTER);
		scrFiltres.setBorder(new LineBorder(new Color(0, 0, 255)));
		scrFiltres.setPreferredSize(new Dimension(175, 2));
		listeFiltres = new JList<Filtre>(dlmFiltres);
		listeFiltres.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrFiltres.setViewportView(listeFiltres);
		listeFiltres.addListSelectionListener(new SelectionListeListener());
		tfRecherche.addFocusListener(new FocusRechercheListener());
		tfRecherche.addKeyListener(new RechercheListener());

		JPanel panelGestionSystemeControle = new JPanel();
		panelGestionSystemeControle.setBorder(new LineBorder(Color.CYAN, 1,
				true));
		panelGestionSystemeControle.setBackground(Color.WHITE);
		this.add(panelGestionSystemeControle, BorderLayout.CENTER);
		panelGestionSystemeControle
				.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("23px"),
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"),
				ColumnSpec.decode("120px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("240px:grow"),
				ColumnSpec.decode("120px"),
				ColumnSpec.decode("15px"),
				ColumnSpec.decode("120px"),
				ColumnSpec.decode("23px"),},
			new RowSpec[] {
				RowSpec.decode("23px"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("15px"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("23px"),}));

		JLabel lblNomFiltre = new JLabel("Filtre :");
		lblNomFiltre.setForeground(Color.DARK_GRAY);
		lblNomFiltre.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelGestionSystemeControle.add(lblNomFiltre, "2, 2, right, default");

		tfNomFiltre = new JTextField();
		tfNomFiltre.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelGestionSystemeControle.add(tfNomFiltre, "4, 2");
		tfNomFiltre.setColumns(10);
		tfNomFiltre.addKeyListener(new ChangementNomListener());

		lblMessageFiltre = new JLabel("");
		lblMessageFiltre.setFont(new Font("Verdana", Font.PLAIN, 14));
		panelGestionSystemeControle.add(lblMessageFiltre, "6, 2, 4, 1");

		checkbActive = new JCheckBox("Activé");
		checkbActive.setBackground(Color.WHITE);
		checkbActive.setFont(new Font("Open Sans Condensed", Font.PLAIN, 13));
		panelGestionSystemeControle.add(checkbActive, "4, 4");

		JPanel panel_Criteres = new JPanel();
		panel_Criteres.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 255), 1, true), "Critères", TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.BLUE));
		panel_Criteres.setBackground(Color.WHITE);
		panelGestionSystemeControle.add(panel_Criteres,
				"2, 6, 8, 1, fill, fill");
		panel_Criteres.setLayout(new BorderLayout(0, 0));

		JScrollPane scrCriteres = new JScrollPane();
		scrCriteres.setBorder(new LineBorder(new Color(0, 204, 255), 1, true));
		scrCriteres.setBackground(Color.WHITE);
		scrCriteres.setPreferredSize(new Dimension(175, 2));
		panel_Criteres.add(scrCriteres, BorderLayout.WEST);

		listeCriteres = new JList<CritereInterface>(dlmCriteres);
		listeCriteres.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrCriteres.setViewportView(listeCriteres);
		listeCriteres.addListSelectionListener(new SelectionListeListener());

		JPanel panel_Critere = new JPanel();
		panel_Critere.setBackground(Color.WHITE);
		panel_Criteres.add(panel_Critere, BorderLayout.CENTER);
		panel_Critere.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("23px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("23px:grow"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("23px"), }, new RowSpec[] {
				RowSpec.decode("12px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px:grow"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("23px"), }));

		JPanel panel_Nouveau = new JPanel();
		panel_Nouveau.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
				255), 1, true), "Nouveau", TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.BLUE));
		panel_Nouveau.setBackground(Color.WHITE);
		panel_Critere.add(panel_Nouveau, "4, 2, fill, fill");
		panel_Nouveau.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6dlu"), FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), }));

		JLabel lblType = new JLabel("Type :");
		panel_Nouveau.add(lblType, "2, 2, right, default");
		lblType.setForeground(Color.DARK_GRAY);
		lblType.setFont(new Font("Open Sans Condensed", Font.PLAIN, 13));

		cbType = new JComboBox<String>();
		panel_Nouveau.add(cbType, "4, 2");
		cbType.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbType.setModel(new DefaultComboBoxModel<String>(new String[] { "Type",
				"Âge", "Emploi", "Jours d'arrêt",
				"Motif médical", "Secteur d'activité" }));
		cbType.addItemListener(new NouveauCritereListener());

		JLabel lblComparateur = new JLabel("Comparateur :");
		panel_Nouveau.add(lblComparateur, "2, 4, right, default");
		lblComparateur.setForeground(Color.DARK_GRAY);
		lblComparateur.setFont(new Font("Open Sans Condensed", Font.PLAIN, 13));

		cbComparateur = new JComboBox<String>();
		cbComparateur.setForeground(Color.BLACK);
		cbComparateur.setEnabled(false);
		cbComparateur.setModel(new DefaultComboBoxModel<String>(comstr));
		panel_Nouveau.add(cbComparateur, "4, 4");
		cbComparateur.setFont(new Font("Verdana", Font.BOLD, 14));

		JLabel val = new JLabel("Valeur :");
		panel_Nouveau.add(val, "2, 6, right, default");
		val.setForeground(Color.DARK_GRAY);
		val.setFont(new Font("Open Sans Condensed", Font.PLAIN, 13));

		tfValeur = new JTextField();
		tfValeur.setDisabledTextColor(Color.BLACK);
		tfValeur.setEnabled(false);
		tfValeur.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Nouveau.add(tfValeur, "4, 6, fill, default");
		tfValeur.setColumns(10);
		tfValeur.addFocusListener(new NouveauCritereListener());
		tfValeur.addKeyListener(new ChangementChampsListener());

		Emploi[] emploi = Emploi.values();
		String[] empstr = new String[emploi.length + 1];
		empstr[0] = "Emploi";
		for (int i = 0; i < emploi.length; i++) {
			empstr[i + 1] = emploi[i].getNom();
		}

		Motif[] motif = Motif.values();
		String[] motstr = new String[motif.length + 1];
		motstr[0] = "Motif médical";
		for (int i = 0; i < motif.length; i++) {
			motstr[i + 1] = motif[i].getNom();
		}

		SecteurActivite[] secteur = SecteurActivite.values();
		String[] secstr = new String[secteur.length + 1];
		secstr[0] = "Secteur d'activité";
		for (int i = 0; i < secteur.length; i++) {
			secstr[i + 1] = secteur[i].getNom();
		}

		cbSecteur = new JComboBox<String>();
		cbSecteur.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbSecteur.setVisible(false);

		cbMotif = new JComboBox<String>();
		cbMotif.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbMotif.setVisible(false);

		cbEmploi = new JComboBox<String>();
		cbEmploi.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbEmploi.setVisible(false);
		panel_Nouveau.add(cbEmploi, "4, 8");
		cbEmploi.setModel(new DefaultComboBoxModel<String>(empstr));
		cbEmploi.addItemListener(new SuggestionsListener());
		panel_Nouveau.add(cbMotif, "4, 9");
		cbMotif.setModel(new DefaultComboBoxModel<String>(motstr));
		cbMotif.addItemListener(new SuggestionsListener());
		panel_Nouveau.add(cbSecteur, "4, 10");
		cbSecteur.setModel(new DefaultComboBoxModel<String>(secstr));
		cbSecteur.addItemListener(new SuggestionsListener());

		btnValiderCritere = new JButton("Valider");
		btnValiderCritere.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				14));
		btnValiderCritere.setForeground(Color.DARK_GRAY);
		panel_Nouveau.add(btnValiderCritere, "2, 12");
		btnValiderCritere.addActionListener(new ValiderListener());

		JButton button_1 = new JButton("Valider");
		button_1.setForeground(Color.DARK_GRAY);

		button_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));

		btnSupprimerCritere = new JButton("Supprimer");
		btnSupprimerCritere.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				14));
		panel_Critere.add(btnSupprimerCritere, "2, 6");
		btnSupprimerCritere.addActionListener(new SupprimerListener(this));

		btnValiderFiltre = new JButton("Valider");
		btnValiderFiltre
				.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		btnValiderFiltre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panelGestionSystemeControle.add(btnValiderFiltre, "4, 8");
		btnValiderFiltre.addActionListener(new ValiderListener());

		btnSupprimerFiltre = new JButton("Supprimer");
		btnSupprimerFiltre.addActionListener(new SupprimerListener(this));

		btnNouveauFiltre = new JButton("Nouveau");
		btnNouveauFiltre
				.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelGestionSystemeControle.add(btnNouveauFiltre, "7, 8");
		btnNouveauFiltre.addActionListener(new ValiderListener());

		btnSupprimerFiltre.setFont(new Font("Open Sans Condensed", Font.PLAIN,
				14));
		panelGestionSystemeControle.add(btnSupprimerFiltre, "9, 8");

		this.addComponentListener(new FenetreListener());

	}

	/**
	 * Actualise les informations de l'onglet
	 */
	private void actualiser() {
		new Thread(new Runnable() {
			public void run() {
				FiltresControleur dc = FiltresControleur.getInstance();
				final List<Filtre> liste = new ArrayList<Filtre>();
				try {
					liste.addAll(dc.getFiltres(null));
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
						tfNomFiltre.setText("");
						checkbActive.setSelected(false);
						dlmCriteres.clear();
						listeFiltres.removeListSelectionListener(listeFiltres
								.getListSelectionListeners()[0]);
						dlmFiltres.clear();
						listeFiltres.setSelectedIndex(-1);
						Iterator<Filtre> it = liste.iterator();
						while (it.hasNext()) {
							dlmFiltres.addElement(it.next());
						}
						listeFiltres
								.addListSelectionListener(new SelectionListeListener());
						if (filtreSelection != null) {
							for (int i = 0; i < dlmFiltres.size(); i++) {
								Filtre f = dlmFiltres.get(i);
								if (f.getNom().equals(filtreSelection.getNom())) {
									listeFiltres.setSelectedValue(f, true);
								}
							}
						}
					}
				});
			}
		}).start();
	}

	/**
	 * Listener de liste
	 */
	public class SelectionListeListener implements ListSelectionListener {

		public void valueChanged(final ListSelectionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					if (arg0.getSource() == listeFiltres) {
						filtreSelection = (Filtre) listeFiltres
								.getSelectedValue();
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tfNomFiltre.setText(filtreSelection.getNom());
								checkbActive.setSelected(filtreSelection
										.isActive());
								listeCriteres
										.removeListSelectionListener(listeCriteres
												.getListSelectionListeners()[0]);
								dlmCriteres.clear();
								listeCriteres.clearSelection();
								Iterator<CritereInterface> it = filtreSelection
										.getCriteres().iterator();
								while (it.hasNext()) {
									dlmCriteres.addElement(it.next());
								}
								listeCriteres
										.addListSelectionListener(new SelectionListeListener());
							}
						});
					} else if (arg0.getSource() == listeCriteres) {
						critereSelection = (CritereInterface) listeCriteres
								.getSelectedValue();
					}
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
					FiltresControleur dc = FiltresControleur.getInstance();
					final List<Filtre> liste = new ArrayList<Filtre>();
					try {
						liste.addAll(dc.getFiltres(str));
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
							tfNomFiltre.setText("");
							checkbActive.setSelected(false);
							dlmCriteres.clear();
							listeFiltres
									.removeListSelectionListener(listeFiltres
											.getListSelectionListeners()[0]);
							dlmFiltres.clear();
							listeFiltres.setSelectedIndex(-1);
							Iterator<Filtre> it = liste.iterator();
							while (it.hasNext()) {
								dlmFiltres.addElement(it.next());
							}
							listeFiltres
									.addListSelectionListener(new SelectionListeListener());
							if (filtreSelection != null) {
								for (int i = 0; i < dlmFiltres.size(); i++) {
									Filtre f = dlmFiltres.get(i);
									if (f.getNom().equals(
											filtreSelection.getNom())) {
										listeFiltres.setSelectedValue(f, true);
									}
								}
							}
						}
					});
				}
			}).start();
		}

	}

	/*
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

	/**
	 * Listener de bouton
	 */
	public class ValiderListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					if (arg0.getSource() == btnValiderFiltre
							&& !tfNomFiltre.getText().isEmpty()) {
						if (filtreSelection.getId() != 0) {
							FiltresControleur fc = new FiltresControleur();
							filtreSelection.setNom(tfNomFiltre.getText());
							filtreSelection.setActive(checkbActive.isSelected());
							ArrayList<CritereInterface> criteres = new ArrayList<CritereInterface>();
							try {
								for (int i = 0; i < dlmCriteres.size(); i++) {
									fc.nouveauCritere(dlmCriteres.get(i));
									criteres.add(dlmCriteres.get(i));
								}
								filtreSelection.setCriteres(criteres);
								List<Filtre> liste = fc
										.getFiltresParNom(tfNomFiltre.getText());
								if (liste.size() == 0
										|| liste.get(0)
												.getNom()
												.equals(filtreSelection
														.getNom()))
									fc.validerFiltre(filtreSelection);
							} catch (final ConnexionException
									| FichierConfigurationException
									| EchecSQLException e) {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										message.setText(e.getMessage());
										Toolkit.getDefaultToolkit().beep();
									}
								});
							}
						} else {
							FiltresControleur fc = new FiltresControleur();
							filtreSelection.setNom(tfNomFiltre.getText());
							filtreSelection.setActive(checkbActive.isSelected());
							ArrayList<CritereInterface> criteres = new ArrayList<CritereInterface>();
							try {
								for (int i = 0; i < dlmCriteres.size(); i++) {
									fc.nouveauCritere(dlmCriteres.get(i));
									criteres.add(dlmCriteres.get(i));
								}
								filtreSelection.setCriteres(criteres);
								List<Filtre> liste = fc
										.getFiltresParNom(tfNomFiltre.getText());
								if (liste.size() == 0
										|| liste.get(0)
												.getNom()
												.equals(filtreSelection
														.getNom()))
									fc.nouveauFiltre(filtreSelection);
							} catch (final ConnexionException
									| FichierConfigurationException
									| EchecSQLException e) {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										message.setText(e.getMessage());
										Toolkit.getDefaultToolkit().beep();
									}
								});
							}
						}
						actualiser();
					} else if (arg0.getSource() == btnValiderCritere) {
						final String[] s = { "" };
						if (cbType.getSelectedItem().equals("Âge")
								|| cbType.getSelectedItem().equals(
										"Jours d'arrêt")) {
							s[0] = tfValeur.getText();
							final String[] tabs = (!s[0].isEmpty() ? s[0] : "")
									.split("\\D+");
							if (tabs.length > 0) {
								while (!tabs[0].isEmpty()
										&& tabs[0].toCharArray()[0] == '0') {
									tabs[0] = tabs[0].substring(1);
								}
								s[0] = tabs[0];
							} else {
								s[0] = "";
							}
						}
						if (!s[0].isEmpty()
								|| (!cbType.getSelectedItem()
										.equals("Âge") && !cbType
										.getSelectedItem().equals(
												"Jours d'arrêt"))) {
							final CritereInterface cri;
							switch ((String) cbType.getSelectedItem()) {
							case "Âge":
								cri = new CritereAge();
								cri.setComparateur(Comparateur
										.getComparateur((String) cbComparateur
												.getSelectedItem()));
								cri.setValeurComparee(s[0]);
								filtreSelection.addCritere(cri);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										dlmCriteres.addElement(cri);
									}
								});
								break;
							case "Emploi":
								cri = new CritereEmploi();
								cri.setComparateur(Comparateur
										.getComparateur((String) cbComparateur
												.getSelectedItem()));
								cri.setValeurComparee(Emploi.getEmploi(tfValeur
										.getText()) + "");
								filtreSelection.addCritere(cri);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										dlmCriteres.addElement(cri);
									}
								});
								break;
							case "Jours d'arrêt":
								cri = new CritereJoursArret();
								cri.setComparateur(Comparateur
										.getComparateur((String) cbComparateur
												.getSelectedItem()));
								cri.setValeurComparee(s[0]);
								filtreSelection.addCritere(cri);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										dlmCriteres.addElement(cri);
									}
								});
								break;
							case "Motif médical":
								cri = new CritereMotifMedical();
								cri.setComparateur(Comparateur
										.getComparateur((String) cbComparateur
												.getSelectedItem()));
								cri.setValeurComparee(Motif.getMotif(tfValeur
										.getText()) + "");
								filtreSelection.addCritere(cri);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										dlmCriteres.addElement(cri);
									}
								});
								break;
							case "Secteur d'activité":
								cri = new CritereSecteurActivite();
								cri.setComparateur(Comparateur
										.getComparateur((String) cbComparateur
												.getSelectedItem()));
								cri.setValeurComparee(SecteurActivite
										.getSecteurActivite(tfValeur.getText())
										+ "");
								filtreSelection.addCritere(cri);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										dlmCriteres.addElement(cri);
									}
								});
								break;
							}
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									tfValeur.setText("");
									cbEmploi.setSelectedIndex(0);
									cbMotif.setSelectedIndex(0);
									cbSecteur.setSelectedIndex(0);
								}
							});
						}
					} else if (arg0.getSource() == btnNouveauFiltre) {
						filtreSelection = new Filtre();
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								tfNomFiltre.setText("");
								checkbActive.setSelected(false);
								dlmCriteres.clear();
								listeFiltres
										.removeListSelectionListener(listeFiltres
												.getListSelectionListeners()[0]);
								listeFiltres.clearSelection();
								listeFiltres
										.addListSelectionListener(new SelectionListeListener());
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
	public class SupprimerListener implements ActionListener {

		JComponent compo;

		public SupprimerListener(JComponent c) {
			this.compo = c;
		}

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					if (arg0.getSource() == btnSupprimerFiltre) {
						FiltresControleur fc = new FiltresControleur();
						try {
							int res = JOptionPane
									.showConfirmDialog(
											compo,
											"Voulez-vous vraiment supprimer ce filtre ?\nCette action est irréversible.",
											"Confirmation",
											JOptionPane.OK_CANCEL_OPTION);
							if (res == JOptionPane.OK_OPTION) {
								fc.supprimer(filtreSelection);
							}
						} catch (final ConnexionException
								| FichierConfigurationException
								| EchecSQLException e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									message.setText(e.getMessage());
									Toolkit.getDefaultToolkit().beep();
								}
							});
						}
						actualiser();
					} else if (arg0.getSource() == btnSupprimerCritere) {
						filtreSelection.getCriteres().remove(critereSelection);
						dlmCriteres.removeElement(critereSelection);
						critereSelection = null;
					}
				}
			}).start();
		}

	}

	/**
	 * Listener de champs de recherche
	 */
	public class ChangementNomListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					String str = tfNomFiltre.getText();
					FiltresControleur dc = FiltresControleur.getInstance();
					final List<Filtre> liste = new ArrayList<Filtre>();
					try {
						liste.addAll(dc.getFiltresParNom(str));
					} catch (ConnexionException | FichierConfigurationException
							| EchecSQLException e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								Toolkit.getDefaultToolkit().beep();
								message.setText(e.getMessage());
							}
						});
					}
					if (liste.size() > 0
							&& !liste.get(0).getNom()
									.equals(filtreSelection.getNom())) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								lblMessageFiltre
										.setText("Ce nom est déjà pris.");
								btnValiderFiltre.setEnabled(false);
							}
						});
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								lblMessageFiltre.setText("");
								btnValiderFiltre.setEnabled(true);
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
	public class NouveauCritereListener extends FocusAdapter implements
			ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			if (arg0.getSource() == cbType) {
				switch ((String) cbType.getSelectedItem()) {
				case "Type":
					cbComparateur.setSelectedIndex(0);
					cbComparateur.setEnabled(false);
					tfValeur.setText("");
					tfValeur.setEnabled(true);
					cbEmploi.setVisible(false);
					cbMotif.setVisible(false);
					cbSecteur.setVisible(false);
					break;
				case "Âge":
					cbComparateur.setEnabled(true);
					tfValeur.setText("");
					tfValeur.setEnabled(true);
					cbEmploi.setVisible(false);
					cbMotif.setVisible(false);
					cbSecteur.setVisible(false);
					break;
				case "Emploi":
					cbComparateur.setSelectedIndex(0);
					cbComparateur.setEnabled(false);
					tfValeur.setText("");
					tfValeur.setEnabled(false);
					cbEmploi.setVisible(true);
					cbMotif.setVisible(false);
					cbSecteur.setVisible(false);
					break;
				case "Jours d'arrêt":
					cbComparateur.setEnabled(true);
					tfValeur.setText("");
					tfValeur.setEnabled(true);
					cbEmploi.setVisible(false);
					cbMotif.setVisible(false);
					cbSecteur.setVisible(false);
					break;
				case "Motif médical":
					cbComparateur.setSelectedIndex(0);
					cbComparateur.setEnabled(false);
					tfValeur.setText("");
					tfValeur.setEnabled(false);
					cbEmploi.setVisible(false);
					cbMotif.setVisible(true);
					cbSecteur.setVisible(false);
					break;
				case "Secteur d'activité":
					cbComparateur.setSelectedIndex(0);
					cbComparateur.setEnabled(false);
					tfValeur.setText("");
					tfValeur.setEnabled(false);
					cbEmploi.setVisible(false);
					cbMotif.setVisible(false);
					cbSecteur.setVisible(true);
					break;
				}
			}
		}

	}

	/**
	 * Listener de champs de texte
	 */
	public class SuggestionsListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					final String[] str = { "" };
					if (cbType.getSelectedItem().equals("Emploi")) {
						str[0] = (String) cbEmploi.getSelectedItem();
					} else if (cbType.getSelectedItem().equals(
							"Motif médical")) {
						str[0] = (String) cbMotif.getSelectedItem();
					} else if (cbType.getSelectedItem().equals(
							"Secteur d'activité")) {
						str[0] = (String) cbSecteur.getSelectedItem();
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (!str[0].equals("Emploi")
									&& !str[0].equals("Motif médical")
									&& !str[0]
											.equals("Secteur d'activité"))
								tfValeur.setText(str[0]);
						}
					});
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
			new Thread(new Runnable() {
				public void run() {
					if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
						JComponent compo = (JComponent) arg0.getSource();
						if (compo == tfValeur) {
							String s = tfValeur.getText();
							final String[] tabs = (!s.isEmpty() ? s : "")
									.split("\\D+");
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (tabs.length > 0) {
										while (!tabs[0].isEmpty()
												&& tabs[0].toCharArray()[0] == '0') {
											tabs[0] = tabs[0].substring(1);
										}
										tfValeur.setText(tabs[0]);
									} else {
										tfValeur.setText("");
									}
								}
							});
						}
					}
				}
			}).start();
		}
	}

}
