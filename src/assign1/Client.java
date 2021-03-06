package assign1;

import java.io.*;
import java.net.*;

public class Client {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;
	
	public static final int read = 1;
	public static final int write = 2;

	public Client()
	{
		try {
			sendReceiveSocket = new DatagramSocket();
		} 
		catch (SocketException se) {   // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	public void setShutdown() {
		sendReceiveSocket.close();
	}

	/*
	 * sendAndReceive takes an opcode as an argument and sends a request of that type
	 * the hardcoded filename is test.txt and the format is specified to be octet.
	 * Once it has sent the request it waits for the server to respond.
	 */
	public void sendAndReceive(int opcode) {
		String s = "test.txt";
		String format = "ocTeT";
		byte msg[] = Message.formatRequest(s, format, opcode);

		try {
			sendPacket = new DatagramPacket(msg, msg.length,
					InetAddress.getLocalHost(), 6001);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Message.printOutgoing(sendPacket, "Client");

		// Send the datagram packet to the server via the send/receive socket. 
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long (the length of the byte array).
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.  
			sendReceiveSocket.receive(receivePacket);
		} 
		catch (SocketException e) {
			
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		Message.printIncoming(receivePacket, "Client");
		
	}

	public static void main(String args[])
	{
		Client c = new Client();
		new Message(c).start();
		c.sendAndReceive(read); //for the first 10 requests, send an opcode of either 0 or 1
		c.sendReceiveSocket.close();
	}
}
