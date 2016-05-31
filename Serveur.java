package tchat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;

public class Serveur extends JFrame {

	// Interface Graphique
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnRer;
	private JMenu mnActions;
	private JMenuItem mntmEnregistrer;
	private JMenuItem mntmFermer;
	private JMenuItem mntmDemmarerServeur;
	private JMenuItem mntmArreterServeur;
	private JMenuItem mntmRechercheClient;
	private JTextField textField;
	private JTextArea textArea;
	private JButton btnEnvoiS;
	private JButton btnReception;
	private JScrollPane scrollPane;
	private JSeparator separator;
	
	// Communication
	private Reseau reseau;
	
	// Fichiers
	private Historiques historiques;
	
	/**
	 * Lancement de l'app
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Serveur frame = new Serveur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la Fenetre
	 */
	public Serveur() {
		reseau = new Reseau();
		historiques = new Historiques();
		
		setTitle("SIO-Chat SERVEUR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnRer = new JMenu("Connexion");
		menuBar.add(mnRer);
		
		mntmDemmarerServeur = new JMenuItem("Demarrer Serveur");
		mntmDemmarerServeur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String addrLocal = reseau.fournitIP();
					textArea.append("L'adresse serveur est : " + addrLocal + "\n");
					String text= reseau.connection_Serveur();
					textArea.append(text + "\n");
					mntmArreterServeur.setEnabled(true);
					mntmDemmarerServeur.setEnabled(false);
					mntmRechercheClient.setEnabled(true);
				}
				catch (IOException arg0){
					textArea.append(arg0.getMessage() + "\n");
				}
			}
		});
		mnRer.add(mntmDemmarerServeur);
		
		mntmArreterServeur = new JMenuItem("Arret Serveur");
		mntmArreterServeur.setEnabled(false);
		mntmArreterServeur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					textArea.append("Deconnexion en cours\n");
					String text = reseau.deco_Serveur();
			        textArea.append(text + "\n");
			        mntmRechercheClient.setEnabled(false);
			        mntmArreterServeur.setEnabled(false);
					mntmDemmarerServeur.setEnabled(true);
					mntmEnregistrer.setEnabled(false);
					btnEnvoiS.setEnabled(false);
					btnReception.setEnabled(false);
				}
				catch (IOException arg0){
					textArea.append(arg0.getMessage() + "\n");
				}
			}
		});
		mnRer.add(mntmArreterServeur);
		
		separator = new JSeparator();
		mnRer.add(separator);
		
		mntmRechercheClient = new JMenuItem("Trouver Client");
		mntmRechercheClient.setEnabled(false);
		mntmRechercheClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String text = reseau.recherche();
					textArea.append(text + "\n");
					mntmRechercheClient.setEnabled(false);
					mntmEnregistrer.setEnabled(true);
					btnEnvoiS.setEnabled(true);
					btnReception.setEnabled(true);
				}catch(IOException arg1){
					textArea.append(arg1.getMessage() + "\n");
				}
			}
		});
		mnRer.add(mntmRechercheClient);
		
		mnActions = new JMenu("Menu");
		menuBar.add(mnActions);
		
		mntmEnregistrer = new JMenuItem("Enregistrer");
		mntmEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String note = JOptionPane.showInputDialog(null,
						"Entrez la note /20 :\n",
						"Note",
						JOptionPane.OK_CANCEL_OPTION
						);
				try{
					String addresse = historiques.enregistrer(textArea.getText(), note);
					textArea.append("Enregistrement dans " + addresse + "\n");
				}catch(IOException arg2){
					textArea.append(arg2.getMessage() + "\n");
				}
				
			}
		});
		mntmEnregistrer.setEnabled(false);
		mnActions.add(mntmEnregistrer);
		
		mntmFermer = new JMenuItem("Fermer");
		mntmFermer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					reseau.deco_Serveur();
					System.exit(0);
				}
				catch (IOException arg0){
					textArea.append(arg0.getMessage() + "\n");
				}
			}
		});
		mnActions.add(mntmFermer);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(12, 12, 424, 161);
		
		textField = new JTextField();
		textField.setBounds(12, 395, 368, 55);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnEnvoiS = new JButton("Envoi");
		btnEnvoiS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(textField.getText().length() != 0){
						reseau.envoi(textField.getText());
				        textArea.append(textField.getText() + "\n");
				        textField.setText("");
					}
				}catch(IOException e){
					textArea.append(e.getMessage() + "\n");
				}	
			}
		});
		btnEnvoiS.setEnabled(false);
		btnEnvoiS.setBounds(387, 395, 107, 55);
		contentPane.add(btnEnvoiS);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(12, 12, 400, 375);
		contentPane.add(scrollPane);
		
		btnReception = new JButton("Reception");
		btnReception.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String text = reseau.reception();
					if (text != null){
						textArea.append("Eleve : " + text + "\n");
					}
				}catch(IOException e){
					textArea.append(e.getMessage() + "\n");
				}
			}
		});
		btnReception.setEnabled(false);
		btnReception.setBounds(417, 276, 77, 111);
		contentPane.add(btnReception);
	}
}