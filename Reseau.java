package tchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Reseau {
	
	// Constante
	final static int PORT = 2009;
	
	// Attributs
	private ServerSocket socketserver;
	private Socket socket;
	private String ip;
	private InputStreamReader streamIn;
	private BufferedReader in;
	private PrintWriter out;
	private String messRecu;
	
	// Constructeur
	public Reseau(){
		
	}
	
	// Accesseurs
	// Socket
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	// IP
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	// StreamIn
	public InputStreamReader getStreamIn() {
		return streamIn;
	}
	public void setStreamIn(InputStreamReader streamIn) {
		this.streamIn = streamIn;
	}
	
	// BufferReader
	public BufferedReader getIn() {
		return in;
	}
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	
	// PrintWriter
	public PrintWriter getOut() {
		return out;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	
	// Message
	public String getmessRecu() {
		return messRecu;
	}
	public void setmessRecu(String messRecu) {
		this.messRecu = messRecu;
	}
	
	// Methodes
	
	// Client 
	public String connection_Client(String IP) throws UnknownHostException, IOException{
		socket = new Socket(IP,PORT);
		ip = IP;
		return "Connection etablie";
	}
	
	public String deco_Client() throws IOException{
		if(socket != null){
			socket.close();
		}
		return "Deconnexion terminee";
	}
	
	// Serveur
	public String fournitIP() throws IOException{
		InetAddress net = InetAddress.getLocalHost();
		return net.toString();
	}
	
	public String connection_Serveur() throws IOException{
		socketserver = new ServerSocket(PORT);
		return "Serveur lancé.\n\nMerci de communiquer l'adresse IP au(x) Client(s),\nPuis rendez-vous dans \"Connexion\" -> \"Trouver Client\".";
	}
	
	public String deco_Serveur() throws IOException{
		if(socket != null){
			socket.close();
		}
		socketserver.close();
		return "Deconnexion effectuée";
	}
	
	public String recherche() throws IOException{
		socket = socketserver.accept();
		return "Client connecté\n";
	}
	
	// Communes
	public void envoi(String message) throws IOException{
		out = new PrintWriter(socket.getOutputStream());
		out.println(message);
		out.flush();
	}

	public String reception() throws IOException{
		streamIn = new InputStreamReader (socket.getInputStream());
		in = new BufferedReader (streamIn);
		if (in.ready()){
			messRecu = in.readLine();
			return messRecu;
		}else{
			return null;
		}
	}
}