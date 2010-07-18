package es.softwareprocess.AndroidTableEditor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Harbinger {
	private boolean inited = false;
	String host = null;
	int port = -1;
	InetAddress hostAddress = null;
	DatagramSocket socket = null;
	DatagramPacket packet = null;
	String destination = null;
	String id = null;
	String program = null;
	static String blankIfNull( String str ) {		
		return (str == null)?"":str;
	}
	public Harbinger( String ihost, int iport, String senderName, String id, String destination) {
		this.host = ihost;
		this.port = iport;
		this.program = blankIfNull(senderName);
		this.id = blankIfNull(id);
		this.destination = blankIfNull(destination);
		inited = false;
	}
	void init() throws IOException {
		if (!inited) {
			hostAddress = InetAddress.getByName( host );
			socket = new DatagramSocket();
			inited = true;
			packet = new DatagramPacket(
						"".getBytes(),
						0,
						hostAddress, 
						port);
		}
	}
	public void sendMessage(String message) throws IOException {
		String sendMsg = program + "|" + id + "|" + destination + "|" + message;
		sendPacket( sendMsg );
	}
	void sendPacket(String value) throws IOException {
		init();
		byte []  buffer = value.getBytes();
		packet.setData( buffer );		
		socket.send(packet);
	}
		

}
