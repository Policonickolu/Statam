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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objet.ArretTravail;
import objet.Attestation;
import objet.DossierIndemnisation;

import org.jdesktop.swingx.JXDatePicker;

import systeme.DateActuelle;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.CreationDossierControleur;
import controleur.DossiersControleur;
import enumeration.Emploi;
import enumeration.Etat;
import enumeration.Motif;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class CreationDossierVue extends JPanel {

	private static final long serialVersionUID = 7015856396126938632L;
	private JLabel message;
	private Etat etat;
	private DefaultListModel<DossierIndemnisation> dlm;
	private JList<DossierIndemnisation> listeDossiers;
	private JFormattedTextField ftfMedecin;
	private JXDatePicker dateDebutArretArr;
	private JXDatePicker dateFinArretArr;
	private JComboBox<String> cbMotif;
	private JFormattedTextField ftfAssureAtt;
	private JTextField tfEmployeur;
	private JXDatePicker dateDebutArretAtt;
	private JXDatePicker dateFinArretAtt;
	private JComboBox<String> cbEmploi;
	private JXDatePicker dateDateEmbauche;
	private JFormattedTextField ftfHeuresTravaillees;
	private JFormattedTextField ftfSalaires;
	private JLabel lblMessage;
	private JList<String> listeSuggestions;
	private JScrollPane scrSuggestions;
	private JFormattedTextField ftfAssureArr;
	private JButton btnValiderArr;
	private JButton btnValiderAtt;
	private JTextField tfRecherche;
	private JLabel lblNbDossierVal;
	private JLabel lblAssureVal;
	private JLabel lblIndemnitesVal;
	private JLabel lblDebutArretVal;
	private JLabel lblMontantVal;
	private JLabel lblFinArretVal;
	private JLabel lblRatioVal;
	private JLabel lblPersonneVal;
	private OngletsDossier ongletsDossier;
	private JPanel panelCreationDossiers;
	private JCheckBox checkbCasSpecial;

	/**
	 * Constructeur
	 */
	public CreationDossierVue(JLabel message) {
		this.message = message;
		this.etat = Etat.INCOMPLET;
		this.initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {

		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout(0, 0));

		Emploi[] emploi = Emploi.values();
		String[] empstr = new String[emploi.length];
		for (int i = 0; i < emploi.length; i++) {
			empstr[i] = emploi[i].getNom();
		}

		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);

		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);

		Motif[] motif = Motif.values();
		String[] motstr = new String[motif.length];
		for (int i = 0; i < motif.length; i++) {
			motstr[i] = motif[i].getNom();
		}

		JTabbedPane tabpCreationDossiers = new JTabbedPane(JTabbedPane.TOP);
		tabpCreationDossiers.setBackground(Color.WHITE);
		tabpCreationDossiers.setFont(new Font("Open Sans Condensed",
				Font.PLAIN, 12));
		add(tabpCreationDossiers, BorderLayout.CENTER);

		panelCreationDossiers = new JPanel();
		tabpCreationDossiers.addTab("Entrer document", null,
				panelCreationDossiers, null);
		panelCreationDossiers.setBackground(Color.WHITE);
		panelCreationDossiers.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("23px:grow"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("23px:grow"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("10px"), ColumnSpec.decode("200px"),
				ColumnSpec.decode("23px:grow"), }, new RowSpec[] {
				RowSpec.decode("23px:grow"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"), RowSpec.decode("max(10px;default)"),
				RowSpec.decode("23px:grow"), }));

		JPanel panel_ArretDeTravail = new JPanel();
		panel_ArretDeTravail.setBorder(new TitledBorder(new LineBorder(
				new Color(0, 0, 255), 1, true), "Arrêt de travail",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0,
						255)));
		panel_ArretDeTravail.setBackground(Color.WHITE);
		panelCreationDossiers.add(panel_ArretDeTravail, "2, 2, fill, fill");
		panel_ArretDeTravail.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("15px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px:grow"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), }));

		JLabel lblAssureArr = new JLabel("Id de l'assuré");
		lblAssureArr.setForeground(Color.DARK_GRAY);
		lblAssureArr.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(lblAssureArr, "2, 2, right, default");

		ftfAssureArr = new JFormattedTextField(nf);

		panel_ArretDeTravail.add(ftfAssureArr, "4, 2, fill, default");
		ftfAssureArr.setFont(new Font("Verdana", Font.PLAIN, 11));
		ftfAssureArr.setColumns(10);

		JLabel lblMedecin = new JLabel("Id du prescripteur");
		lblMedecin.setForeground(Color.DARK_GRAY);
		lblMedecin.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(lblMedecin, "2, 4, right, default");

		ftfMedecin = new JFormattedTextField(nf);

		ftfMedecin.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_ArretDeTravail.add(ftfMedecin, "4, 4, fill, default");
		ftfMedecin.setColumns(10);

		JLabel lblDebutArretArr = new JLabel("Début de l'arrêt");
		lblDebutArretArr.setForeground(Color.DARK_GRAY);
		lblDebutArretArr
				.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(lblDebutArretArr, "2, 6, right, default");

		dateDebutArretArr = new JXDatePicker();
		dateDebutArretArr.setDate(DateActuelle.getDateActuelle());
		dateDebutArretArr.getEditor().setFont(
				new Font("Verdana", Font.PLAIN, 11));
		panel_ArretDeTravail.add(dateDebutArretArr, "4, 6");

		JLabel lblFinArretArr = new JLabel("Fin de l'arrêt");
		lblFinArretArr.setForeground(Color.DARK_GRAY);
		lblFinArretArr.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(lblFinArretArr, "2, 8, right, default");

		dateFinArretArr = new JXDatePicker();
		dateFinArretArr.setDate(DateActuelle.getDateActuelle());
		dateFinArretArr.getEditor()
				.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_ArretDeTravail.add(dateFinArretArr, "4, 8");

		JLabel lblMotif = new JLabel("Motif médical");
		lblMotif.setForeground(Color.DARK_GRAY);
		lblMotif.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(lblMotif, "2, 10, right, default");
		cbMotif = new JComboBox<String>();

		cbMotif.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbMotif.setModel(new DefaultComboBoxModel<String>(motstr));
		panel_ArretDeTravail.add(cbMotif, "4, 10, fill, default");

		btnValiderArr = new JButton("Valider");
		btnValiderArr.addActionListener(new ValiderListener());

		btnValiderArr.setForeground(Color.DARK_GRAY);
		btnValiderArr.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_ArretDeTravail.add(btnValiderArr, "2, 12");

		JPanel panel_Attestation = new JPanel();
		panel_Attestation.setBackground(Color.WHITE);
		panel_Attestation.setBorder(new TitledBorder(new LineBorder(new Color(
				0, 0, 255), 1, true), "Attestation de salaire",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0,
						255)));
		panelCreationDossiers.add(panel_Attestation, "4, 2, fill, fill");
		panel_Attestation.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("6px"), ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("6px"), }, new RowSpec[] {
				RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("9px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("30px"), FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("6px"), }));

		JLabel lblAssureAtt = new JLabel("Id de l'assuré");
		lblAssureAtt.setForeground(Color.DARK_GRAY);
		lblAssureAtt.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblAssureAtt, "2, 2, right, default");

		ftfAssureAtt = new JFormattedTextField(nf);
		ftfAssureAtt.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(ftfAssureAtt, "4, 2, fill, default");
		ftfAssureAtt.setColumns(10);

		JLabel lblEmployeur = new JLabel("Employeur");
		lblEmployeur.setForeground(Color.DARK_GRAY);
		lblEmployeur.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblEmployeur, "2, 4, right, default");

		scrSuggestions = new JScrollPane();
		scrSuggestions
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrSuggestions
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrSuggestions.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrSuggestions.setVisible(false);
		scrSuggestions.setBackground(Color.WHITE);
		scrSuggestions.setBorder(new TitledBorder(new LineBorder(new Color(128,
				128, 128), 1, true), "Suggestions", TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.GRAY));
		panelCreationDossiers.add(scrSuggestions, "6, 2, fill, fill");

		listeSuggestions = new JList<String>();
		listeSuggestions.setBorder(null);
		scrSuggestions.setViewportView(listeSuggestions);
		listeSuggestions.setBackground(Color.WHITE);
		listeSuggestions.setFont(new Font("Verdana", Font.PLAIN, 11));
		listeSuggestions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listeSuggestions.addListSelectionListener(new SuggestionsListener());

		tfEmployeur = new JTextField();
		tfEmployeur.addKeyListener(new RechercheListener());

		tfEmployeur.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(tfEmployeur, "4, 4, fill, default");
		tfEmployeur.setColumns(10);

		JLabel lblDebutArretAtt = new JLabel("Début de l'arrêt");
		lblDebutArretAtt.setForeground(Color.DARK_GRAY);
		lblDebutArretAtt
				.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblDebutArretAtt, "2, 6, right, default");

		dateDebutArretAtt = new JXDatePicker();
		dateDebutArretAtt.setDate(DateActuelle.getDateActuelle());
		dateDebutArretAtt.getEditor().setFont(
				new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(dateDebutArretAtt, "4, 6");

		JLabel lblFinArretAtt = new JLabel("Fin de l'arrêt");
		lblFinArretAtt.setForeground(Color.DARK_GRAY);
		lblFinArretAtt.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblFinArretAtt, "2, 8, right, default");

		dateFinArretAtt = new JXDatePicker();
		dateFinArretAtt.setDate(DateActuelle.getDateActuelle());
		dateFinArretAtt.getEditor()
				.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(dateFinArretAtt, "4, 8");

		JLabel lblEmploi = new JLabel("Emploi");
		lblEmploi.setForeground(Color.DARK_GRAY);
		lblEmploi.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblEmploi, "2, 10, right, default");

		cbEmploi = new JComboBox<String>();
		cbEmploi.setFont(new Font("Verdana", Font.PLAIN, 11));
		cbEmploi.setModel(new DefaultComboBoxModel<String>(empstr));
		panel_Attestation.add(cbEmploi, "4, 10, fill, default");

		JLabel lblDateEmbauche = new JLabel("Date d'embauche");
		lblDateEmbauche.setForeground(Color.DARK_GRAY);
		lblDateEmbauche
				.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblDateEmbauche, "2, 12, right, default");

		dateDateEmbauche = new JXDatePicker();
		dateDateEmbauche.setDate(DateActuelle.getDateActuelle());
		dateDateEmbauche.getEditor().setFont(
				new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(dateDateEmbauche, "4, 12");

		JLabel lblHeuresTravaillees = new JLabel("Heures travaillées");
		lblHeuresTravaillees.setForeground(Color.DARK_GRAY);
		lblHeuresTravaillees.setFont(new Font("Open Sans Condensed",
				Font.PLAIN, 14));
		panel_Attestation.add(lblHeuresTravaillees, "2, 14, right, default");

		ftfHeuresTravaillees = new JFormattedTextField(nf);
		ftfHeuresTravaillees.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(ftfHeuresTravaillees, "4, 14, fill, default");

		JLabel lblSalaires = new JLabel("Total salaires per\u00E7us");
		lblSalaires.setForeground(Color.DARK_GRAY);
		lblSalaires.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblSalaires, "2, 16, right, default");

		ftfSalaires = new JFormattedTextField(df);
		ftfSalaires.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(ftfSalaires, "4, 16, fill, default");

		JLabel lblCasSpecial = new JLabel("Cas spécial");
		lblCasSpecial.setForeground(Color.DARK_GRAY);
		lblCasSpecial.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(lblCasSpecial, "2, 18, right, default");

		checkbCasSpecial = new JCheckBox("");
		checkbCasSpecial.setBackground(Color.WHITE);
		checkbCasSpecial.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_Attestation.add(checkbCasSpecial, "4, 18, left, default");

		btnValiderAtt = new JButton("Valider");
		btnValiderAtt.setForeground(Color.DARK_GRAY);
		btnValiderAtt.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panel_Attestation.add(btnValiderAtt, "2, 20");
		btnValiderAtt.addActionListener(new ValiderListener());

		lblMessage = new JLabel("");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		panelCreationDossiers.add(lblMessage, "2, 4, 5, 1");
		lblMessage.setFont(new Font("Verdana", Font.PLAIN, 14));

		JPanel panelIncomplets = new JPanel();
		tabpCreationDossiers.addTab("Dossiers incomplets", null,
				panelIncomplets, null);
		panelIncomplets.setLayout(new BorderLayout(0, 0));
		panelIncomplets.addComponentListener(new FenetreListener());

		dlm = new DefaultListModel<DossierIndemnisation>();

		JPanel panelSubIncomplets = new JPanel();
		panelIncomplets.add(panelSubIncomplets, BorderLayout.CENTER);

		panelSubIncomplets.setBackground(Color.WHITE);
		panelSubIncomplets.setLayout(new FormLayout(new ColumnSpec[] {
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
		panelSubIncomplets.add(lblNbDossier, "3, 2");

		lblNbDossierVal = new JLabel("0000035");
		lblNbDossierVal.setForeground(Color.BLACK);
		lblNbDossierVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblNbDossierVal, "5, 2");

		JLabel lblAssure = new JLabel("Assuré");
		lblAssure.setForeground(Color.DARK_GRAY);
		lblAssure.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblAssure, "5, 4");

		JLabel label = new JLabel(":");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label, "7, 4");

		lblAssureVal = new JLabel("00000020");
		lblAssureVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblAssureVal, "9, 4");

		JLabel lblIndemnites = new JLabel("Indemnités journalière");
		lblIndemnites.setForeground(Color.DARK_GRAY);
		lblIndemnites.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblIndemnites, "11, 4");

		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_1, "13, 4, right, default");

		lblIndemnitesVal = new JLabel();
		lblIndemnitesVal.setText("0");
		lblIndemnitesVal.setPreferredSize(new Dimension(6, 18));
		lblIndemnitesVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblIndemnitesVal, "15, 4");

		JLabel lblDebutArret = new JLabel("Début de l'arrêt");
		lblDebutArret.setForeground(Color.DARK_GRAY);
		lblDebutArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblDebutArret, "5, 6");

		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_2, "7, 6");

		lblDebutArretVal = new JLabel("19-04-2013");
		lblDebutArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblDebutArretVal, "9, 6");

		JLabel lblMontant = new JLabel("Montant total");
		lblMontant.setForeground(Color.DARK_GRAY);
		lblMontant.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblMontant, "11, 6");

		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.DARK_GRAY);
		label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_3, "13, 6, right, default");

		lblMontantVal = new JLabel();
		lblMontantVal.setText("0");
		lblMontantVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblMontantVal, "15, 6");

		JLabel lblFinArret = new JLabel("Fin de l'arrêt");
		lblFinArret.setForeground(Color.DARK_GRAY);
		lblFinArret.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblFinArret, "5, 8");

		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.DARK_GRAY);
		label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_4, "7, 8");

		lblFinArretVal = new JLabel("26-04-2013");
		lblFinArretVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblFinArretVal, "9, 8");

		JLabel lblRatio = new JLabel("Ratio");
		lblRatio.setForeground(Color.DARK_GRAY);
		lblRatio.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblRatio, "11, 8");

		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.DARK_GRAY);
		label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_5, "13, 8, right, default");

		lblRatioVal = new JLabel();
		lblRatioVal.setText("0%");
		lblRatioVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblRatioVal, "15, 8");

		JLabel lblPersonne = new JLabel("Personne en charge");
		lblPersonne.setForeground(Color.DARK_GRAY);
		lblPersonne.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(lblPersonne, "5, 10");

		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.DARK_GRAY);
		label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN, 14));
		panelSubIncomplets.add(label_6, "7, 10");

		lblPersonneVal = new JLabel("Jean Sapin");
		lblPersonneVal.setFont(new Font("Verdana", Font.PLAIN, 11));
		panelSubIncomplets.add(lblPersonneVal, "9, 10");

		ongletsDossier = new OngletsDossier(message, 14, 11, true, true, true,
				true, true, false);
		panelSubIncomplets.add(ongletsDossier, "2, 12, 16, 1, fill, fill");

		JPanel panel = new JPanel();
		panelIncomplets.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrDossiers = new JScrollPane();
		panel.add(scrDossiers, BorderLayout.CENTER);
		scrDossiers.setBorder(new LineBorder(new Color(0, 0, 255)));
		scrDossiers.setPreferredSize(new Dimension(175, 2));
		listeDossiers = new JList<DossierIndemnisation>(dlm);
		listeDossiers.setFont(new Font("Verdana", Font.PLAIN, 11));
		scrDossiers.setViewportView(listeDossiers);

		JPanel panelDossiers = new JPanel();
		panel.add(panelDossiers, BorderLayout.NORTH);
		panelDossiers.setBackground(Color.BLUE);
		panelDossiers.setLayout(new BorderLayout(0, 0));

		JLabel lblRefresh = new JLabel("");
		lblRefresh.setIcon(new ImageIcon(CreationDossierVue.class
				.getResource("/vue/images/iconeactualiser.png")));
		panelDossiers.add(lblRefresh, BorderLayout.WEST);
		lblRefresh.addMouseListener(new BoutonActualiserListener());

		JLabel lblDossiers = new JLabel("Dossiers incomplets");
		lblDossiers.setHorizontalAlignment(SwingConstants.CENTER);
		lblDossiers.setForeground(Color.WHITE);
		lblDossiers.setFont(new Font("Open Sans Condensed", Font.PLAIN, 15));
		panelDossiers.add(lblDossiers);

		tfRecherche = new JTextField();
		tfRecherche.setForeground(Color.DARK_GRAY);
		tfRecherche.setText("Rechercher");
		panelDossiers.add(tfRecherche, BorderLayout.SOUTH);
		tfRecherche.setActionCommand("");
		tfRecherche.setBorder(new LineBorder(Color.BLUE));
		tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
		tfRecherche.setColumns(10);
		tfRecherche.addKeyListener(new RechercheIncompletsListener());
		tfRecherche.addFocusListener(new FocusRechercheListener());
		listeDossiers.addListSelectionListener(new SelectionListeListener());

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
					liste.addAll(dc.getDossiers(etat, null, null, null, null));
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
						listeDossiers.setSelectedIndex(0);
					}
				});
			}
		}).start();

	}

	/**
	 * Listener de champs de recherche
	 */
	public class RechercheListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					String str = tfEmployeur.getText();
					final boolean[] bool = { false };
					final Vector<String> listeVec = new Vector<String>();
					if (!str.equals("")) {
						CreationDossierControleur cdc = CreationDossierControleur
								.getInstance();
						List<String> liste = new ArrayList<String>();
						try {
							liste = cdc.rechercheNomEmployeur(str);
						} catch (ConnexionException
								| FichierConfigurationException e) {
							Toolkit.getDefaultToolkit().beep();
							message.setText(e.getMessage());
						}
						listeVec.addAll(liste);
						if (listeVec.size() > 0)
							bool[0] = true;
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							listeSuggestions.setListData(listeVec);
							scrSuggestions.setVisible(bool[0]);
						}
					});
				}
			}).start();
		}

	}

	/**
	 * Listener de champs de recherche
	 */
	public class SuggestionsListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					final String str = listeSuggestions.getSelectedValue()
							.toString();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							tfEmployeur.setText(str);
							scrSuggestions.setVisible(false);
						}
					});
				}
			}).start();
		}

	}

	/**
	 * Listener de bouton
	 */
	public class ValiderListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final ActionEvent ae = e;
			new Thread(new Runnable() {
				public void run() {
					if (ae.getSource() == btnValiderArr) {
						CreationDossierControleur cdc = new CreationDossierControleur();
						final String[] str = { "" };
						int idAss = (!ftfAssureArr.getText().equals("") ? Integer
								.parseInt(ftfAssureArr.getText()) : 0);
						int idMed = (!ftfMedecin.getText().equals("") ? Integer
								.parseInt(ftfMedecin.getText()) : 0);
						Date d1 = (dateDebutArretArr.getDate() != null ? new Date(
								dateDebutArretArr.getDate().getTime()) : null);
						Date d2 = (dateFinArretArr.getDate() != null ? new Date(
								dateFinArretArr.getDate().getTime()) : null);
						if (idAss != 0 && idMed != 0 && d1 != null
								&& d2 != null
								&& cbMotif.getSelectedIndex() >= 0) {
							try {
								str[0] = cdc.verifierArretTravail(idAss, idMed,
										d1, d2, Motif.getMotif((String) cbMotif
												.getSelectedItem()));
							} catch (final ConnexionException
									| FichierConfigurationException
									| EchecSQLException e) {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										Toolkit.getDefaultToolkit().beep();
										message.setText(e.getMessage());
									}
								});
								return;
							}
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									lblMessage.setText(str[0]);
									if (!str[0].equals(""))
										Toolkit.getDefaultToolkit().beep();
								}
							});
							if (str[0].equals("")) {
								int res = JOptionPane
										.showConfirmDialog(
												panelCreationDossiers,
												"Voulez vous vraiment enregistrer cet arrêt ?\nCette action est irréversible.",
												"Confirmation",
												JOptionPane.OK_CANCEL_OPTION);
								if (res == JOptionPane.OK_OPTION) {
									try {
										cdc.validerArretTravail(idAss, idMed,
												d1, d2,
												Motif.getMotif((String) cbMotif
														.getSelectedItem()));
										SwingUtilities
										.invokeLater(new Runnable() {
											public void run() {
												lblMessage
														.setText("Arrêt enregistré.");
												ftfAssureArr
														.setText("");
												ftfMedecin.setText("");
												dateDebutArretArr
														.setDate(DateActuelle
																.getDateActuelle());
												dateFinArretArr
														.setDate(DateActuelle
																.getDateActuelle());
												cbMotif.setSelectedIndex(0);
											}
										});
									} catch (final ConnexionException
											| FichierConfigurationException
											| EchecSQLException e) {
										SwingUtilities
												.invokeLater(new Runnable() {
													public void run() {
														Toolkit.getDefaultToolkit()
																.beep();
														message.setText(e
																.getMessage());
													}
												});
									}
								}
							}
						} else {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									lblMessage
											.setText("Remplissez tous les champs.");
									Toolkit.getDefaultToolkit().beep();
								}
							});
						}
					} else if (ae.getSource() == btnValiderAtt) {
						CreationDossierControleur cdc = new CreationDossierControleur();
						final String[] str = { "" };
						int idAss = (!ftfAssureAtt.getText().equals("") ? Integer
								.parseInt(ftfAssureAtt.getText()) : 0);
						Date d1 = (dateDebutArretAtt.getDate() != null ? new Date(
								dateDebutArretAtt.getDate().getTime()) : null);
						Date d2 = (dateFinArretAtt.getDate() != null ? new Date(
								dateFinArretAtt.getDate().getTime()) : null);
						Date d3 = (dateDateEmbauche.getDate() != null ? new Date(
								dateDateEmbauche.getDate().getTime()) : null);
						int heu = (!ftfHeuresTravaillees.getText().equals("") ? Integer
								.parseInt(ftfHeuresTravaillees.getText()) : 0);
						float sal = (!ftfSalaires.getText().equals("") ? Float
								.parseFloat(ftfSalaires.getText().replace(',',
										'.')) : 0);
						if (idAss != 0 && !tfEmployeur.getText().equals("")
								&& d1 != null && d2 != null && d3 != null
								&& cbEmploi.getSelectedIndex() >= 0) {
							try {
								str[0] = cdc.verifierAttestation(idAss,
										tfEmployeur.getText(), d1, d2, Emploi
												.getEmploi((String) cbEmploi
														.getSelectedItem()),
										d3, heu, sal, checkbCasSpecial
												.isSelected());
							} catch (final ConnexionException
									| FichierConfigurationException
									| EchecSQLException e) {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										Toolkit.getDefaultToolkit().beep();
										message.setText(e.getMessage());
									}
								});
								return;
							}
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									lblMessage.setText(str[0]);
									if (!str[0].equals(""))
										Toolkit.getDefaultToolkit().beep();
								}
							});
							if (str[0].equals("")) {
								int res = JOptionPane
										.showConfirmDialog(
												panelCreationDossiers,
												"Voulez-vous vraiment enregistrer cette attestation ?\nCette action est irréversible.",
												"Confirmation",
												JOptionPane.OK_CANCEL_OPTION);
								if (res == JOptionPane.OK_OPTION) {
									try {
										cdc.validerAttestation(
												idAss,
												tfEmployeur.getText(),
												d1,
												d2,
												Emploi.getEmploi((String) cbEmploi
														.getSelectedItem()),
												d3, heu, sal, checkbCasSpecial
														.isSelected());
										SwingUtilities
												.invokeLater(new Runnable() {
													public void run() {
														lblMessage
																.setText("Attestation enregistrée.");
														ftfAssureAtt
																.setText("");
														tfEmployeur.setText("");
														dateDebutArretAtt
																.setDate(DateActuelle
																		.getDateActuelle());
														dateFinArretAtt
																.setDate(DateActuelle
																		.getDateActuelle());
														cbEmploi.setSelectedIndex(0);
														dateDateEmbauche
																.setDate(DateActuelle
																		.getDateActuelle());
														ftfHeuresTravaillees
																.setText("");
														ftfSalaires.setText("");
														checkbCasSpecial
																.setSelected(false);
													}
												});
									} catch (final ConnexionException
											| FichierConfigurationException
											| EchecSQLException e) {
										SwingUtilities
												.invokeLater(new Runnable() {
													public void run() {
														Toolkit.getDefaultToolkit()
																.beep();
														message.setText(e
																.getMessage());
													}
												});
									}
								}
							}
						} else {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									lblMessage
											.setText("Remplissez tous les champs.");
									Toolkit.getDefaultToolkit().beep();
								}
							});
						}
					}
				}
			}).start();
		}

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
	public class RechercheIncompletsListener extends KeyAdapter {

		@Override
		public void keyTyped(KeyEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					String str = tfRecherche.getText();
					DossiersControleur dc = DossiersControleur.getInstance();
					final List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
					try {
						liste.addAll(dc
								.getDossiers(etat, null, str, null, null));
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
