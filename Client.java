package tchat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.EventQueue;

//Action Listeners 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Fenetre Biblio Graphique
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
//Composants Biblio Graphique
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Client extends JFrame {

	// Interface Graphique
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnF;
	private JMenuItem mntmConnexion;
	private JMenuItem mntmDeconnexion;
	private JMenuItem mntmFermer;
	private JSeparator separator_1;
	private JTextField textField;
	private JTextArea textArea;
	private JButton btnEnvoiC;
	private JScrollPane scrollPane;
	private JButton btnReceptionC;
	
	// Communication
	private Reseau reseau;



	/**
	 * Lancement de l'app
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre
	 */
	public Client() {
		reseau = new Reseau();
		
		setTitle("SIO-Chat CLIENT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnF = new JMenu("Menu");
		menuBar.add(mnF);
		
		mntmConnexion = new JMenuItem("Connexion");
		mntmConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip = JOptionPane.showInputDialog(null,
						"Entrez l'adresse IP Serveur:\n",
						"Connexion",
						JOptionPane.OK_CANCEL_OPTION
						);
				try{
					String retour = reseau.connection_Client(ip);
					textArea.append(retour + "\n");
					mntmConnexion.setEnabled(false);
					mntmDeconnexion.setEnabled(true);
					btnEnvoiC.setEnabled(true);
					btnReceptionC.setEnabled(true);
				}catch(UnknownHostException e){
					textArea.append(e.getMessage());
				}catch(IOException e){
					textArea.append(e.getMessage());
				}
			}
		});
		mnF.add(mntmConnexion);
		
		mntmDeconnexion = new JMenuItem("Deconnexion");
		mntmDeconnexion.setEnabled(false);
		mntmDeconnexion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					String retour = reseau.deco_Client();
					textArea.append(retour+"\n");
					mntmConnexion.setEnabled(true);
					mntmDeconnexion.setEnabled(false);
					btnEnvoiC.setEnabled(false);
					btnReceptionC.setEnabled(false);
				}catch(IOException arg0){
					textArea.append(arg0.getMessage());
				}
					
			}
		});
		mnF.add(mntmDeconnexion);
		
		separator_1 = new JSeparator();
		mnF.add(separator_1);
		
		mntmFermer = new JMenuItem("Fermer");
		mntmFermer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					reseau.deco_Client();
					System.exit(0);
				}catch(IOException arg1){
					textArea.append(arg1.getMessage() + "\n");
				}
			}
		});
		mnF.add(mntmFermer);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(12, 12, 424, 161);
		
		textField = new JTextField();
		textField.setBounds(12, 392, 369, 58);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnEnvoiC = new JButton("Envoi");
		btnEnvoiC.setEnabled(false);
		btnEnvoiC.addActionListener(new ActionListener() {
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
		btnEnvoiC.setBounds(385, 392, 109, 58);
		contentPane.add(btnEnvoiC);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(12, 12, 397, 374);
		contentPane.add(scrollPane);
		
		btnReceptionC = new JButton("Reception");
		btnReceptionC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String reception = reseau.reception();
					if (reception != null){
						textArea.append("Professeur : " + reception + "\n");
					}
				}catch(IOException e){
					textArea.append(e.getMessage() + "\n");
				}
			}
		});
		btnReceptionC.setEnabled(false);
		btnReceptionC.setBounds(416, 272, 78, 114);
		contentPane.add(btnReceptionC);
	}	
}