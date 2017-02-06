package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import objet.ArretTravail;
import objet.Assure;
import objet.Attestation;
import objet.DossierIndemnisation;
import objet.Employeur;
import objet.Medecin;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controleur.DossiersControleur;
import exception.ConnexionException;
import exception.EchecSQLException;
import exception.FichierConfigurationException;

public class OngletsDossier extends JTabbedPane {

	private static final long serialVersionUID = -6815044816640493009L;
	private JLabel message;
	private int tailleLabel;
	private int tailleValeur;
	private boolean assure;
	private boolean medecin;
	private boolean employeur;
	private boolean arret;
	private boolean attestation;
	private boolean antecedents;
	private JLabel lblNumeroVal;
	private JLabel lblDateDeNaissanceVal;
	private JLabel lblNomVal;
	private JLabel lblLieuDeNaissanceVal;
	private JLabel lblPrenomVal;
	private JLabel lblAdresseVal;
	private JLabel lblNomValMed;
	private JLabel lblAdresseValMed;
	private JLabel lblSpecialiteVal;
	private JLabel lblPrenomValMed;
	private JLabel lblNomValEmp;
	private JLabel lblAdresseValEmp;
	private JLabel lblSecteurVal;
	private JLabel lblMedecinVal;
	private JLabel lblMotifVal;
	private JLabel lblEmployeurVal;
	private JLabel lblHeuresVal;
	private JLabel lblEmploiVal;
	private JLabel lblSalairesVal;
	private JLabel lblDateEmbaucheVal;
	private JLabel lblCasSpecialVal;
	private JTextField tfRecherche;
	private DefaultListModel<DossierIndemnisation> dlm;
	private JList<DossierIndemnisation> listeDossiersAnt;
	private JLabel lblNbDossierVal;
	private JLabel lblAssureVal;
	private JLabel lblIndemnitesVal;
	private JLabel lblDebutArretVal;
	private JLabel lblMontantVal;
	private JLabel lblFinArretVal;
	private JLabel lblRatioVal;
	private JLabel lblPersonneVal;
	private OngletsDossier onglets;
	private DossierIndemnisation dossier;

	/**
	 * Constructeur
	 */
	public OngletsDossier(JLabel message, int tailleLabel, int tailleValeur,
			boolean assure, boolean medecin, boolean employeur, boolean arret,
			boolean attestation, boolean antecedents) {
		super(JTabbedPane.TOP);
		this.message = message;
		this.tailleLabel = tailleLabel;
		this.tailleValeur = tailleValeur;
		this.assure = assure;
		this.medecin = medecin;
		this.employeur = employeur;
		this.arret = arret;
		this.attestation = attestation;
		this.antecedents = antecedents;
		this.initialize();
	}

	/**
	 * Initialisation du contenu
	 */
	public void initialize() {
		this.setBackground(Color.WHITE);
		this.setFont(new Font("Open Sans Condensed", Font.PLAIN, 12));

		if (assure == true) {

			JPanel panelAssure = new JPanel();
			panelAssure.setBackground(Color.WHITE);
			this.addTab("Assuré", null, panelAssure, null);
			panelAssure.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("15px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("6px"), }, new RowSpec[] {
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), }));

			JLabel lblNumero = new JLabel("Numéro");
			lblNumero.setForeground(Color.DARK_GRAY);
			lblNumero.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(lblNumero, "2, 2");

			JLabel label = new JLabel(":");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label, "4, 2");

			lblNumeroVal = new JLabel("");
			lblNumeroVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAssure.add(lblNumeroVal, "6, 2");

			JLabel lblDateDeNaissance = new JLabel("Date de naissance");
			lblDateDeNaissance.setForeground(Color.DARK_GRAY);
			lblDateDeNaissance.setFont(new Font("Open Sans Condensed",
					Font.PLAIN, tailleLabel));
			panelAssure.add(lblDateDeNaissance, "8, 2");

			JLabel label_1 = new JLabel(":");
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label_1, "10, 2");

			lblDateDeNaissanceVal = new JLabel("");
			lblDateDeNaissanceVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelAssure.add(lblDateDeNaissanceVal, "12, 2");

			JLabel lblNom = new JLabel("Nom");
			lblNom.setForeground(Color.DARK_GRAY);
			lblNom.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(lblNom, "2, 4");

			JLabel label_2 = new JLabel(":");
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label_2, "4, 4");

			lblNomVal = new JLabel("");
			lblNomVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAssure.add(lblNomVal, "6, 4");

			JLabel lblLieuDeNaissance = new JLabel("Lieu de naissance");
			lblLieuDeNaissance.setForeground(Color.DARK_GRAY);
			lblLieuDeNaissance.setFont(new Font("Open Sans Condensed",
					Font.PLAIN, tailleLabel));
			panelAssure.add(lblLieuDeNaissance, "8, 4");

			JLabel label_3 = new JLabel(":");
			label_3.setForeground(Color.DARK_GRAY);
			label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label_3, "10, 4");

			lblLieuDeNaissanceVal = new JLabel("");
			lblLieuDeNaissanceVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelAssure.add(lblLieuDeNaissanceVal, "12, 4");

			JLabel lblPrenom = new JLabel("Prénom");
			lblPrenom.setForeground(Color.DARK_GRAY);
			lblPrenom.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(lblPrenom, "2, 6");

			JLabel label_4 = new JLabel(":");
			label_4.setForeground(Color.DARK_GRAY);
			label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label_4, "4, 6");

			lblPrenomVal = new JLabel("");
			lblPrenomVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAssure.add(lblPrenomVal, "6, 6");

			JLabel lblAdresse = new JLabel("Adresse");
			lblAdresse.setForeground(Color.DARK_GRAY);
			lblAdresse.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(lblAdresse, "8, 6");

			JLabel label_5 = new JLabel(":");
			label_5.setForeground(Color.DARK_GRAY);
			label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAssure.add(label_5, "10, 6");

			lblAdresseVal = new JLabel("");
			lblAdresseVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAssure.add(lblAdresseVal, "12, 6");

		}

		if (medecin == true) {
			JPanel panelMedecin = new JPanel();
			panelMedecin.setBackground(Color.WHITE);
			addTab("Prescripteur", null, panelMedecin, null);
			panelMedecin.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("15px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("6px"), }, new RowSpec[] {
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), }));

			JLabel lblNomMed = new JLabel("Nom");
			lblNomMed.setForeground(Color.DARK_GRAY);
			lblNomMed.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(lblNomMed, "2, 2");

			JLabel label = new JLabel(":");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(label, "4, 2");

			lblNomValMed = new JLabel("");
			lblNomValMed.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelMedecin.add(lblNomValMed, "6, 2");

			JLabel lblAdresseMed = new JLabel("Adresse");
			lblAdresseMed.setForeground(Color.DARK_GRAY);
			lblAdresseMed.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(lblAdresseMed, "8, 2");

			JLabel label_1 = new JLabel(":");
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(label_1, "10, 2");

			lblAdresseValMed = new JLabel("");
			lblAdresseValMed.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelMedecin.add(lblAdresseValMed, "12, 2");

			JLabel lblPrenomMed = new JLabel("Prénom");
			lblPrenomMed.setForeground(Color.DARK_GRAY);
			lblPrenomMed.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(lblPrenomMed, "2, 4");

			JLabel label_2 = new JLabel(":");
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(label_2, "4, 4");

			lblPrenomValMed = new JLabel("");
			lblPrenomValMed.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelMedecin.add(lblPrenomValMed, "6, 4");

			JLabel lblSpecialite = new JLabel("Spécialité");
			lblSpecialite.setForeground(Color.DARK_GRAY);
			lblSpecialite.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(lblSpecialite, "8, 4");

			JLabel label_3 = new JLabel(":");
			label_3.setForeground(Color.DARK_GRAY);
			label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelMedecin.add(label_3, "10, 4");

			lblSpecialiteVal = new JLabel("");
			lblSpecialiteVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelMedecin.add(lblSpecialiteVal, "12, 4");
		}

		if (employeur == true) {
			JPanel panelEmployeur = new JPanel();
			panelEmployeur.setBackground(Color.WHITE);
			addTab("Employeur", null, panelEmployeur, null);
			panelEmployeur.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("6px"), }, new RowSpec[] {
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), }));

			JLabel lblNomEmp = new JLabel("Nom");
			lblNomEmp.setForeground(Color.DARK_GRAY);
			lblNomEmp.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(lblNomEmp, "2, 2");

			JLabel label = new JLabel(":");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(label, "4, 2");

			lblNomValEmp = new JLabel("");
			lblNomValEmp.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelEmployeur.add(lblNomValEmp, "6, 2");

			JLabel lblAdresseEmp = new JLabel("Adresse");
			lblAdresseEmp.setForeground(Color.DARK_GRAY);
			lblAdresseEmp.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(lblAdresseEmp, "2, 4");

			JLabel label_1 = new JLabel(":");
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(label_1, "4, 4");

			lblAdresseValEmp = new JLabel("");
			lblAdresseValEmp.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelEmployeur.add(lblAdresseValEmp, "6, 4");

			JLabel lblSecteur = new JLabel("Secteur d'activité");
			lblSecteur.setForeground(Color.DARK_GRAY);
			lblSecteur.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(lblSecteur, "2, 6");

			JLabel label_2 = new JLabel(":");
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelEmployeur.add(label_2, "4, 6");

			lblSecteurVal = new JLabel("");
			lblSecteurVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelEmployeur.add(lblSecteurVal, "6, 6");
		}

		if (arret == true) {

			JPanel panelArret = new JPanel();
			this.addTab("Arrêt de travail", null, panelArret, null);
			panelArret.setBackground(Color.WHITE);
			panelArret.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("6px"), }, new RowSpec[] {
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), }));

			JLabel lblMedecin = new JLabel("Prescripteur");
			lblMedecin.setForeground(Color.DARK_GRAY);
			lblMedecin.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelArret.add(lblMedecin, "2, 2");

			JLabel label_6 = new JLabel(":");
			label_6.setForeground(Color.DARK_GRAY);
			label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelArret.add(label_6, "4, 2");

			lblMedecinVal = new JLabel("");
			lblMedecinVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelArret.add(lblMedecinVal, "6, 2");

			JLabel lblMotif = new JLabel("Motif médical");
			lblMotif.setForeground(Color.DARK_GRAY);
			lblMotif.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelArret.add(lblMotif, "2, 4");

			JLabel label_7 = new JLabel(":");
			label_7.setForeground(Color.DARK_GRAY);
			label_7.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelArret.add(label_7, "4, 4");

			lblMotifVal = new JLabel("");
			lblMotifVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelArret.add(lblMotifVal, "6, 4");
		}

		if (attestation == true) {
			JPanel panelAttestation = new JPanel();
			this.addTab("Attestation", null, panelAttestation, null);
			panelAttestation.setBackground(Color.WHITE);
			panelAttestation.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("15px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"), FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("6px"),
					ColumnSpec.decode("max(75px;default)"),
					ColumnSpec.decode("6px"), }, new RowSpec[] {
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
					RowSpec.decode("6px"), }));

			JLabel lblEmployeur = new JLabel("Employeur");
			lblEmployeur.setForeground(Color.DARK_GRAY);
			lblEmployeur.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblEmployeur, "2, 2");

			JLabel label = new JLabel(":");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label, "4, 2");

			lblEmployeurVal = new JLabel("");
			lblEmployeurVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelAttestation.add(lblEmployeurVal, "6, 2");

			JLabel lblHeures = new JLabel("Heures travaillées");
			lblHeures.setForeground(Color.DARK_GRAY);
			lblHeures.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblHeures, "8, 2");

			JLabel label_1 = new JLabel(":");
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label_1, "10, 2");

			lblHeuresVal = new JLabel("");
			lblHeuresVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAttestation.add(lblHeuresVal, "12, 2");

			JLabel lblEmploi_1 = new JLabel("Emploi");
			lblEmploi_1.setForeground(Color.DARK_GRAY);
			lblEmploi_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblEmploi_1, "2, 4");

			JLabel label_2 = new JLabel(":");
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label_2, "4, 4");

			lblEmploiVal = new JLabel("");
			lblEmploiVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAttestation.add(lblEmploiVal, "6, 4");

			JLabel lblSalaires = new JLabel("Total salaires per\u00E7us");
			lblSalaires.setForeground(Color.DARK_GRAY);
			lblSalaires.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblSalaires, "8, 4");

			JLabel label_3 = new JLabel(":");
			label_3.setForeground(Color.DARK_GRAY);
			label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label_3, "10, 4");

			lblSalairesVal = new JLabel("");
			lblSalairesVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelAttestation.add(lblSalairesVal, "12, 4");

			JLabel lblDateEmbauche = new JLabel("Date d'embauche");
			lblDateEmbauche.setForeground(Color.DARK_GRAY);
			lblDateEmbauche.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblDateEmbauche, "2, 6");

			JLabel label_4 = new JLabel(":");
			label_4.setForeground(Color.DARK_GRAY);
			label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label_4, "4, 6");

			lblDateEmbaucheVal = new JLabel("");
			lblDateEmbaucheVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelAttestation.add(lblDateEmbaucheVal, "6, 6");

			JLabel lblCasSpecial = new JLabel("Cas spécial");
			lblCasSpecial.setForeground(Color.DARK_GRAY);
			lblCasSpecial.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(lblCasSpecial, "8, 6");

			JLabel label_5 = new JLabel(":");
			label_5.setForeground(Color.DARK_GRAY);
			label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel));
			panelAttestation.add(label_5, "10, 6");

			lblCasSpecialVal = new JLabel("");
			lblCasSpecialVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelAttestation.add(lblCasSpecialVal, "12, 6");

		}

		if (antecedents == true) {

			JPanel panelAntecedents = new JPanel();
			panelAntecedents.setBackground(Color.WHITE);
			this.addTab("Antécédents", null, panelAntecedents, null);
			panelAntecedents.setLayout(new BorderLayout(0, 0));

			dlm = new DefaultListModel<DossierIndemnisation>();

			JPanel panelSubAntecedents = new JPanel();
			panelSubAntecedents.setBackground(Color.WHITE);
			panelAntecedents.add(panelSubAntecedents, BorderLayout.CENTER);
			panelSubAntecedents
					.setLayout(new FormLayout(new ColumnSpec[] {
							ColumnSpec.decode("6px"),
							FormFactory.DEFAULT_COLSPEC,
							ColumnSpec.decode("6px"),
							FormFactory.DEFAULT_COLSPEC,
							ColumnSpec.decode("6px"),
							FormFactory.DEFAULT_COLSPEC,
							ColumnSpec.decode("6px"),
							ColumnSpec.decode("max(75px;default)"),
							ColumnSpec.decode("15px"),
							FormFactory.DEFAULT_COLSPEC,
							ColumnSpec.decode("6px"),
							FormFactory.DEFAULT_COLSPEC,
							ColumnSpec.decode("6px"),
							ColumnSpec.decode("max(75px;default)"),
							ColumnSpec.decode("default:grow"),
							ColumnSpec.decode("6px"), }, new RowSpec[] {
							RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
							RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
							RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
							RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
							RowSpec.decode("6px"), FormFactory.DEFAULT_ROWSPEC,
							RowSpec.decode("15px:grow"),
							FormFactory.DEFAULT_ROWSPEC, }));

			JLabel lblNbDossier = new JLabel("Dossier :");
			lblNbDossier.setForeground(Color.DARK_GRAY);
			lblNbDossier.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblNbDossier, "2, 2");

			lblNbDossierVal = new JLabel("");
			lblNbDossierVal.setForeground(Color.BLACK);
			lblNbDossierVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelSubAntecedents.add(lblNbDossierVal, "4, 2");

			JLabel lblAssure = new JLabel("Assuré");
			lblAssure.setForeground(Color.DARK_GRAY);
			lblAssure.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblAssure, "4, 4");

			JLabel label = new JLabel(":");
			label.setForeground(Color.DARK_GRAY);
			label.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label, "6, 4");

			lblAssureVal = new JLabel("");
			lblAssureVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelSubAntecedents.add(lblAssureVal, "8, 4");

			JLabel lblIndemnites = new JLabel(
					"Indemnités journalière");
			lblIndemnites.setForeground(Color.DARK_GRAY);
			lblIndemnites.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblIndemnites, "10, 4");

			JLabel label_1 = new JLabel(":");
			label_1.setForeground(Color.DARK_GRAY);
			label_1.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_1, "12, 4");

			lblIndemnitesVal = new JLabel("");
			lblIndemnitesVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelSubAntecedents.add(lblIndemnitesVal, "14, 4");

			JLabel lblDebutArret = new JLabel("Début de l'arrêt");
			lblDebutArret.setForeground(Color.DARK_GRAY);
			lblDebutArret.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblDebutArret, "4, 6");

			JLabel label_2 = new JLabel(":");
			label_2.setForeground(Color.DARK_GRAY);
			label_2.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_2, "6, 6");

			lblDebutArretVal = new JLabel("");
			lblDebutArretVal.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));
			panelSubAntecedents.add(lblDebutArretVal, "8, 6");

			JLabel lblMontant = new JLabel("Montant total");
			lblMontant.setForeground(Color.DARK_GRAY);
			lblMontant.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblMontant, "10, 6");

			JLabel label_3 = new JLabel(":");
			label_3.setForeground(Color.DARK_GRAY);
			label_3.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_3, "12, 6");

			lblMontantVal = new JLabel("");
			lblMontantVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelSubAntecedents.add(lblMontantVal, "14, 6");

			JLabel lblFinArret = new JLabel("Fin de l'arrêt");
			lblFinArret.setForeground(Color.DARK_GRAY);
			lblFinArret.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblFinArret, "4, 8");

			JLabel label_4 = new JLabel(":");
			label_4.setForeground(Color.DARK_GRAY);
			label_4.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_4, "6, 8");

			lblFinArretVal = new JLabel("");
			lblFinArretVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelSubAntecedents.add(lblFinArretVal, "8, 8");

			JLabel lblRatio = new JLabel("Ratio");
			lblRatio.setForeground(Color.DARK_GRAY);
			lblRatio.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblRatio, "10, 8");

			JLabel label_5 = new JLabel(":");
			label_5.setForeground(Color.DARK_GRAY);
			label_5.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_5, "12, 8");

			lblRatioVal = new JLabel("");
			lblRatioVal.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelSubAntecedents.add(lblRatioVal, "14, 8");

			JLabel lblPersonne = new JLabel("Personne en charge");
			lblPersonne.setForeground(Color.DARK_GRAY);
			lblPersonne.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(lblPersonne, "4, 10");

			JLabel label_6 = new JLabel(":");
			label_6.setForeground(Color.DARK_GRAY);
			label_6.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					tailleLabel - 1));
			panelSubAntecedents.add(label_6, "6, 10");

			lblPersonneVal = new JLabel("");
			lblPersonneVal
					.setFont(new Font("Verdana", Font.PLAIN, tailleValeur));
			panelSubAntecedents.add(lblPersonneVal, "8, 10");

			JPanel panel = new JPanel();
			panelAntecedents.add(panel, BorderLayout.WEST);
			panel.setLayout(new BorderLayout(0, 0));

			JScrollPane scrDossiersAnt = new JScrollPane();
			panel.add(scrDossiersAnt, BorderLayout.CENTER);
			scrDossiersAnt.setPreferredSize(new Dimension(175, 2));
			scrDossiersAnt.setBorder(new LineBorder(new Color(0, 0, 255)));
			listeDossiersAnt = new JList<DossierIndemnisation>(dlm);
			listeDossiersAnt.setFont(new Font("Verdana", Font.PLAIN,
					tailleValeur));

			listeDossiersAnt
					.addListSelectionListener(new SelectionListeListener());

			scrDossiersAnt.setViewportView(listeDossiersAnt);

			JPanel panelDossiersAnt = new JPanel();
			panel.add(panelDossiersAnt, BorderLayout.NORTH);
			panelDossiersAnt.setBackground(Color.BLUE);
			panelDossiersAnt.setLayout(new BorderLayout(0, 0));

			JLabel lblDossiersAnt = new JLabel("Dossiers");
			lblDossiersAnt.setHorizontalAlignment(SwingConstants.CENTER);
			lblDossiersAnt.setForeground(Color.WHITE);
			lblDossiersAnt.setFont(new Font("Open Sans Condensed", Font.PLAIN,
					15));
			panelDossiersAnt.add(lblDossiersAnt, BorderLayout.CENTER);

			tfRecherche = new JTextField();
			tfRecherche.setForeground(Color.DARK_GRAY);
			tfRecherche.setText("Rechercher");
			panelDossiersAnt.add(tfRecherche, BorderLayout.SOUTH);
			tfRecherche.setActionCommand("");
			tfRecherche.setBorder(new LineBorder(Color.BLUE));
			tfRecherche.setFont(new Font("Verdana", Font.PLAIN, 11));
			tfRecherche.setColumns(10);
			tfRecherche.addKeyListener(new RechercheListener());
			tfRecherche.addKeyListener(new RechercheListener());
			tfRecherche.addFocusListener(new FocusRechercheListener());

			onglets = new OngletsDossier(message, 13, 11, false, true, true,
					true, true, false);
			panelSubAntecedents.add(onglets, "2, 12, 14, 1, fill, fill");

		}
	}

	/**
	 * Vide le contenu de l'onglet
	 */
	public void vider() {
		if (assure) {
			lblNumeroVal.setText("");
			lblDateDeNaissanceVal.setText("");
			lblNomVal.setText("");
			lblLieuDeNaissanceVal.setText("");
			lblPrenomVal.setText("");
			lblAdresseVal.setText("");
		}
		if (medecin) {
			lblNomValMed.setText("");
			lblAdresseValMed.setText("");
			lblSpecialiteVal.setText("");
			lblPrenomValMed.setText("");
		}
		if (employeur) {
			lblNomValEmp.setText("");
			lblAdresseValEmp.setText("");
			lblSecteurVal.setText("");
		}
		if (arret) {
			lblMedecinVal.setText("");
			lblMotifVal.setText("");
		}
		if (attestation) {
			lblEmployeurVal.setText("");
			lblHeuresVal.setText("");
			lblEmploiVal.setText("");
			lblSalairesVal.setText("");
			lblDateEmbaucheVal.setText("");
			lblCasSpecialVal.setText("");
		}
		if (antecedents) {
			listeDossiersAnt.removeListSelectionListener(listeDossiersAnt
					.getListSelectionListeners()[0]);
			dlm.clear();
			listeDossiersAnt
					.addListSelectionListener(new SelectionListeListener());
			lblAssureVal.setText("");
			lblIndemnitesVal.setText("");
			lblDebutArretVal.setText("");
			lblMontantVal.setText("");
			lblFinArretVal.setText("");
			lblRatioVal.setText("");
			lblPersonneVal.setText("");
			onglets.vider();
		}
	}

	/**
	 * Remplit le contenu de l'onglet
	 */
	public void remplir(DossierIndemnisation dos) {
		this.dossier = dos;
		boolean arr = dos.getArretTravail() != null;
		boolean att = dos.getAttestation() != null;
		if (assure) {
			if (arr) {
				Assure ass = dos.getArretTravail().getAssure();
				lblNumeroVal.setText("" + ass.getId());
				lblDateDeNaissanceVal.setText("" + ass.getDateNaissance());
				lblNomVal.setText("" + ass.getNom());
				lblLieuDeNaissanceVal.setText("" + ass.getLieuNaissance());
				lblPrenomVal.setText("" + ass.getPrenom());
				lblAdresseVal.setText("" + ass.getAdresse());
			} else if (att) {
				Assure ass = dos.getAttestation().getAssure();
				lblNumeroVal.setText("" + ass.getId());
				lblDateDeNaissanceVal.setText("" + ass.getDateNaissance());
				lblNomVal.setText("" + ass.getNom());
				lblLieuDeNaissanceVal.setText("" + ass.getLieuNaissance());
				lblPrenomVal.setText("" + ass.getPrenom());
				lblAdresseVal.setText("" + ass.getAdresse());
			}
		}
		if (medecin) {
			if (arr) {
				Medecin med = dos.getArretTravail().getMedecin();
				lblNomValMed.setText("" + med.getNom());
				lblAdresseValMed.setText("" + med.getAdresse());
				lblSpecialiteVal.setText("" + med.getSpecialite());
				lblPrenomValMed.setText("" + med.getPrenom());
			}
		}
		if (employeur) {
			if (att) {
				Employeur emp = dos.getAttestation().getEmployeur();
				lblNomValEmp.setText("" + emp.getNom());
				lblAdresseValEmp.setText("" + emp.getAdresse());
				lblSecteurVal.setText("" + emp.getSecteurActivite());
			}
		}
		if (arret) {
			if (arr) {
				ArretTravail at = dos.getArretTravail();
				lblMedecinVal.setText("" + at.getMedecin().getPrenom() + " "
						+ at.getMedecin().getNom());
				lblMotifVal.setText("" + at.getMotifMedical());
			}
		}
		if (attestation) {
			if (att) {
				Attestation at = dos.getAttestation();
				lblEmployeurVal.setText("" + at.getEmployeur().getNom());
				lblHeuresVal.setText("" + at.getHeuresTravaillees());
				lblEmploiVal.setText("" + at.getEmploi());
				lblSalairesVal.setText("" + at.getSalairesPercus());
				lblDateEmbaucheVal.setText("" + at.getDateEmbauche());
				lblCasSpecialVal.setText(""
						+ (at.isCasSpecial() ? "Oui" : "Non"));
			}
		}
		if (antecedents) {
			Assure ass = getAssureFromDossier(dossier);
			if (ass != null)
				setDossiersAssure(ass);
		}
	}

	/**
	 * Affiche tous les dossiers de l'assuré
	 */
	private void setDossiersAssure(final Assure ass) {
		new Thread(new Runnable() {
			public void run() {
				DossiersControleur dc = DossiersControleur.getInstance();
				List<DossierIndemnisation> liste = new ArrayList<DossierIndemnisation>();
				try {
					liste.addAll(dc.getDossiers(null, null, null, ass, null));
				} catch (ConnexionException | FichierConfigurationException
						| EchecSQLException e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Toolkit.getDefaultToolkit().beep();
							message.setText(e.getMessage());
						}
					});
				}
				final List<DossierIndemnisation> liste2 = new ArrayList<DossierIndemnisation>();
				for (DossierIndemnisation d : liste) {
					if (d.getId() != dossier.getId()) {
						liste2.add(d);
					}
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						listeDossiersAnt
								.removeListSelectionListener(listeDossiersAnt
										.getListSelectionListeners()[0]);
						dlm.clear();
						listeDossiersAnt.setSelectedIndex(-1);
						Iterator<DossierIndemnisation> it = liste2.iterator();
						while (it.hasNext()) {
							dlm.addElement(it.next());
						}
						listeDossiersAnt
								.addListSelectionListener(new SelectionListeListener());
						if (dlm.getSize() > 0)
							listeDossiersAnt.setSelectedIndex(0);
					}
				});
			}
		}).start();

	}
	
	/**
	 * Permet d'obtenir les informations de l'assuré selon la disponibilité de
	 * l'arrêt ou de l'attestation
	 */
	private Assure getAssureFromDossier(DossierIndemnisation dossier) {

		if (dossier.getArretTravail() != null) {
			return dossier.getArretTravail().getAssure();
		} else if (dossier.getAttestation() != null) {
			return dossier.getAttestation().getAssure();
		}
		return null;
	}

	/**
	 * Listener de liste
	 */
	public class SelectionListeListener implements ListSelectionListener {

		@Override
		public void valueChanged(final ListSelectionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					final DossierIndemnisation dossier = (DossierIndemnisation) listeDossiersAnt
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
							onglets.remplir(dossier);
						}
					});
				}
			}).start();
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
						liste.addAll(dc.getDossiers(null, null, str,
								getAssureFromDossier(dossier), null));
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
							onglets.vider();
							listeDossiersAnt
									.removeListSelectionListener(listeDossiersAnt
											.getListSelectionListeners()[0]);
							dlm.clear();
							listeDossiersAnt.setSelectedIndex(-1);
							Iterator<DossierIndemnisation> it = liste
									.iterator();
							while (it.hasNext()) {
								dlm.addElement(it.next());
							}
							listeDossiersAnt
									.addListSelectionListener(new SelectionListeListener());
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
