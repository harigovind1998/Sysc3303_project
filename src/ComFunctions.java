//import java.io.IOException;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//
//public class ComFunctions {
//	
//	public final int KNOWNLEN = 4;
//	public final int UNKNOWNLEN = 100;
//	
//	/**
//	 * Receives a packet on the specified socket 
//	 * @param socket Socket to listen to
//	 * @param len length of the packet, either 4 or 100 bytes long
//	 * @return received DatagramPakcet
//	 */
//	public DatagramPacket recievePacket(DatagramSocket socket, int len) {
//		
//		DatagramPacket tempPacket = createPacket(len);
//		try {
//			socket.receive(tempPacket);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
//		return tempPacket;
//	}
//	
//	
//	/**
//	 * Sends a packet through an initilized socket 
//	 * @param packet packet to send
//	 * @param socket socket to send the packet through
//	 */
//	public void sendPacket(DatagramPacket packet, DatagramSocket socket) {
//		try {
//			socket.send(packet);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
//	
//	/**
//	 * Creates a DatagramSocket on available port
//	 * @return DatagramSocket
//	 */
//	public DatagramSocket startSocket() {
//		DatagramSocket socket = null;
//		try {
//			socket = new DatagramSocket();
//		}catch (SocketException e){
//			e.printStackTrace();
//			System.exit(1);
//		}
//		return socket;
//	}
//	
//	/**
//	 * Creates a DatagramSocket on specified port
//	 * @param port port the Socket needs to created on
//	 * @return DatagramSocket linked to the specified port
//	 */
//	public DatagramSocket startSocket(int port) {
//		DatagramSocket socket = null;
//		try {
//			socket = new DatagramSocket(port);
//		}catch (SocketException e){
//			e.printStackTrace();
//			System.exit(1);
//		}
//		return socket;
//	}
//	
//	/**
//	 * Creates an DatagramPocket to be passed through a DatagramSocket.
//	 * @param msg Byte array message
//	 * @param address address of the destination socket
//	 * @param port port number of the destination socket
//	 * @return DatagramPacket that can be used for sending
//	 */
//	public DatagramPacket createPacket(byte[] msg, int port ) {
//		DatagramPacket sendPacket = null;
//		try {
//			sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), port);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		return sendPacket;		
//		
//	}
//	
//	/**
//	 * Creates an DatagramPocket used to handle DatagramSocket receive.
//	 * @param len desired length of the packet
//	 * @return new empty DatatgramPacket of desired length
//	 */
//	public DatagramPacket createPacket(int len) {
//		DatagramPacket sendPacket = null;
//		byte[] buff = new byte[len];
//		
//		try {
//			sendPacket = new DatagramPacket(buff, buff.length);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			System.exit(1);
//		}
//		return sendPacket;		
//	}
//	
//	
//	/**
//	 * Generates a byte message to be passed through the sockets. Creates an empty byte array of length equal
//	 * to the length of the file name, plus the length of the format, plus 4 to account for the 2 bytes used 
//	 * for specify if the message operation and 2 more for the 0 in the middle and end of the message.
//	 * @param type Message operation that is being sent, either a read or a write
//	 * @param file name of the file
//	 * @param format format of the file
//	 * @return byte array of specified message format
//	 */
//	public byte[] generateMessage(byte[] type, String file, String format) {
//		byte[] msg = new byte[file.length() + format.length() + 4];
//		msg[0] = type[0];
//		msg[1] = type[1];
//		int track = 2; 
//		
//		for(byte c : file.getBytes() ) {
//			msg[track] = c; 
//			track ++;
//		}
//		
//		msg[track] = 0;
//		track ++;
//		
//		for(byte b : format.getBytes()) {
//			msg[track] = b;
//			track ++;
//		}
//		
//		msg[msg.length - 1] = 0 ;
//		
//		return msg;
//	}
//	
//	/**
//	 * Generates a message for the Data Packet used to sends data trough the Data gram
//	 * @param blockNumber block number being sent through the Data gram Packet
//	 * @param block Data contained by the DatagramPacket with size <= 512
//	 * @return byte array of the message that can be used to create the DatagramPacket
//	 */
//	public byte[] generateDataPacket(byte[] blockNumber, byte[] block) {
//		byte[] msg = new byte[2 + blockNumber.length + block.length];
//		msg[0]= 0;
//		msg[1]= 3;
//		msg[2] = blockNumber[0];
//		msg[3]= blockNumber[1];
//		for(int i = 0; i < block.length ; i ++) {
//			msg[i + 4] = block[i];
//		}
//		return msg;
//	}
//	
//	/**
//	 * Generates an Acknowledge Response 
//	 * @param blockNumber Block number that has just been received
//	 * @return 
//	 */
//	public byte[] generateAckMessage(byte[] blockNumber) {
//		byte[] msg = new byte[] {0,4,blockNumber[0], blockNumber[1]};
//		return msg;
//	}
//	
//	/**
//	 * Generates a error response to be sent through the DatatgramPacket
//	 * @param errCode specified error Code
//	 * @param errMsg Contains more information about the error
//	 * @return generated byte array message
//	 */
//	public byte[] generateErrMessage(byte[] errCode, String errMsg) {
//		byte[] msg = new byte[5 + errMsg.length() ];
//		msg[0] = 0;
//		msg[1] = 5;
//		msg[2] = errCode[0];
//		msg[3] = errCode[1];
//		msg[msg.length -1 ] = 0;
//		int track = 4;
//		for(byte b : errMsg.getBytes()) {
//			msg[track] = b;
//			track ++;
//		}
//		
//		return msg;
//	}
//	
//	/**
//	 * Prints out the contents of a packet
//	 * @param init initial message 
//	 * @param msg message to print 
//	 */
//	public void printMessage(String init, byte[] msg) {
//		//prints the message in string format
//		System.out.println(init + " "+ new String(msg));
//		
//		System.out.print("In string format: "); //prints out the byte array
//		for(byte b: msg) {
//			System.out.print(b + " ");
//		}
//		System.out.print("\n");
//	}
//	
//	/**
//	 * Checks to see if a byte array is on the required format read/write,text,0,text,0...0
//	 * @param msg message that is to be checked
//	 * @return true if the format is correct and false if it is not
//	 */
//	public boolean checkMessage(byte[] msg) {
//
//		if (msg[0] == 0 && (msg [1] == 1 || msg[1] == 2)) { //Check to see if the first 2 bytes specify a read or a write;
//			int count = 0;
//			int i = 2;
//			//finds the first 2 0's
//			for (; i < msg.length; ++i) { 
//				if (msg[i] == 0 && (msg[i] != msg[i - 1])) {
//					++count;
//				}
//				if(count == 2) {
//					i++;
//					break;
//				}
//			}
//			
//			//checks to ensure the rest of the message are only 0's
//			for(;i<msg.length; i++) {
//				if(msg[i] != 0) {
//					return false;
//				}
//			}
//				return true;
//			
//		} else {
//			return false;
//		}
//		
//	}
//	
//	/**
//	 * Reads a file into a byte array
//	 * @param path path to the file
//	 * @return byte array of the file contents
//	 */
//	public byte[] readFileIntoArray(String path) {
//        byte[] bytesArray = null;
//
//        try {
//        	bytesArray = Files.readAllBytes(Paths.get(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Couldnt read properly");
//        } 
//        return bytesArray;
//	}
//	
//	/**
//	 * Get a block that is to be send through the socket
//	 * @param blockNumber block number that is needed
//	 * @param byteArray byteArray containing the file contents 
//	 * @return
//	 */
//	public byte[] getBlock(int blockNumber, byte[] byteArray) {
//		byte[] temp = new byte[512];
//		int len = byteArray.length;
//		int track = 0;
//		for(int i = ((blockNumber - 1) * 512); i < ((blockNumber ) * 512); i ++) {
//			if(track < len) {
//				temp[track] = byteArray[i];
//			}else {
//				temp[track] = 0;
//			}
//			track ++;
//		}
//		return temp;
//	}
//	
//	/**
//	 * Converts a number into a 2 bytes, has a range of 0 to 65535
//	 * @param num number to be converted
//	 * @return byte array
//	 */
//	public byte[] intToByte(int num) {
//		//Bit shifting operations so DW about it 
//		byte[] byteArr = new byte[2];
//		byteArr[0] = (byte) (num & 0xFF);
//		byteArr[1] = (byte) ((num>>>8)&0xFF);
//		return byteArr;
//		
//	}
//	
//	/**
//	 * Checks to see if the acknowledge packet is the one that is to be expected
//	 * @param packet Packet that has just come in
//	 * @param block block number that is expected
//	 * @return 
//	 */
//	public boolean CheckAck(DatagramPacket packet, int block) {
//		byte[] blockByte = intToByte(block);
//		if(packet.getData()[2] == blockByte[0] && packet.getData()[3] == blockByte[1]) {
//			return true;
//		}else {
//			return false;
//		}
//	}
//}

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ComFunctions {
	
	public final int KNOWNLEN = 4;
	public final int UNKNOWNLEN = 100;
	private final static char[] hexArr = "0123456789ABCDEF".toCharArray();
	public int fileLength;
	
	/**
	 * Recieves a packet on the specified socket 
	 * @param socket Socket to listen to
	 * @param len length of the packet, either 4 or 100 bytes long
	 * @return recieved DatagramPakcet
	 */
	public DatagramPacket recievePacket(DatagramSocket socket, int len) {
		
		DatagramPacket tempPacket = createPacket(len);
		try {
			socket.receive(tempPacket);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return tempPacket;
	}
	
	/**
	 * Sends a packet through an initilized socket 
	 * @param packet packet to send
	 * @param socket socket to send the packet through
	 */
	public void sendPacket(DatagramPacket packet, DatagramSocket socket) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Creates a DatagramSocket on available port
	 * @return DatagramSocket
	 */
	public DatagramSocket startSocket() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		}catch (SocketException e){
			e.printStackTrace();
			System.exit(1);
		}
		return socket;
	}
	
	/**
	 * Creates a DatagramSocket on specified port
	 * @param port port the Socket needs to created on
	 * @return DatagramSocket linked to the specified port
	 */
	public DatagramSocket startSocket(int port) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		}catch (SocketException e){
			e.printStackTrace();
			System.exit(1);
		}
		return socket;
	}
	
	/**
	 * Creates an DatagramPocket to be passed through a DatagramSocket.
	 * @param msg Byte array message
	 * @param address address of the destination socket
	 * @param port port number of the destination socket
	 * @return DatagramPacket that can be used for sending
	 */
	public DatagramPacket createPacket(byte[] msg, int port ) {
		DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return sendPacket;		
		
	}
	
	/**
	 * Creates an DatagramPocket used to handle DatagramSocket receive.
	 * @param len desired length of the packet
	 * @return new empty DatatgramPacket of desired length
	 */
	public DatagramPacket createPacket(int len) {
		DatagramPacket sendPacket = null;
		byte[] buff = new byte[len];
		
		try {
			sendPacket = new DatagramPacket(buff, buff.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		return sendPacket;		
	}
	
	
	/**
	 * Generates a byte message to be passed through the sockets. Creates an empty byte array of length equal
	 * to the length of the file name, plus the length of the format, plus 4 to account for the 2 bytes used 
	 * for specify if the message operation and 2 more for the 0 in the middle and end of the message.
	 * @param type Message operation that is being sent, either a read or a write
	 * @param file name of the file
	 * @param format format of the file
	 * @return byte array of specified message format
	 */
	public byte[] generateMessage(byte[] type, byte[] file, String format) {
		byte[] msg = new byte[file.length + format.length() + 4];
		msg[0] = type[0];
		msg[1] = type[1];
		int track = 2; 
		
		for(byte c : file) {
			msg[track] = c; 
			fileLength++;
			track ++;
		}
		
		msg[track] = 0;
		track ++;
		
		for(byte b : format.getBytes()) {
			msg[track] = b;
			track ++;
		}
		
		msg[msg.length - 1] = 0 ;
		
		return msg;
	}
	
	/**
	 * Generates a byte message to be passed through the sockets. Creates an empty byte array of length equal
	 * to the length of the file name, plus the length of the format, plus 4 to account for the 2 bytes used 
	 * for specify if the message operation and 2 more for the 0 in the middle and end of the message.
	 * @param type Message operation that is being sent, either a read or a write
	 * @param file name of the file
	 * @param format format of the file
	 * @return byte array of specified message format
	 */
	public byte[] generateMessage(byte[] type, String file, String format) {
		byte[] msg = new byte[file.length() + format.length() + 4];
		msg[0] = type[0];
		msg[1] = type[1];
		int track = 2; 
		
		for(byte c : file.getBytes() ) {
			msg[track] = c; 
			track ++;
		}
		
		msg[track] = 0;
		track ++;
		
		for(byte b : format.getBytes()) {
			msg[track] = b;
			track ++;
		}
		
		msg[msg.length - 1] = 0 ;
		
		return msg;
	}
	
	public byte[] generateDataPacket(byte[] blockNumber, byte[] block) {
		byte[] msg = new byte[2 + blockNumber.length + block.length];
		msg[0]= 0;
		msg[1]= 3;
		msg[2] = blockNumber[0];
		msg[3]= blockNumber[1];
		for(int i = 0; i < block.length ; i ++) {
			msg[i + 4] = block[i];
		}
		return msg;
	}
	
	/**
	 * Generates an Acknowledge Response 
	 * @param blockNumber Block number that has just been received
	 * @return 
	 */
	public byte[] generateAckMessage(byte[] blockNumber) {
		byte[] msg = new byte[] {0,4,blockNumber[0], blockNumber[1]};
		return msg;
	}
	
	/**
	 * Generates a error response to be sent through the DatatgramPacket
	 * @param errCode specified error Code
	 * @param errMsg Contains more information about the error
	 * @return generated byte array message
	 */
	public byte[] generateErrMessage(byte[] errCode, String errMsg) {
		byte[] msg = new byte[5 + errMsg.length() ];
		msg[0] = 0;
		msg[1] = 5;
		msg[2] = errCode[0];
		msg[3] = errCode[1];
		msg[msg.length -1 ] = 0;
		int track = 4;
		for(byte b : errMsg.getBytes()) {
			msg[track] = b;
			track ++;
		}
		
		return msg;
	}
	
	/**
	 * Prints out the contents of a packet
	 * @param init initial message 
	 * @param msg message to print 
	 */
	public void printMessage(String init, byte[] msg) {
		//prints the message in string format
		System.out.println(init + " "+ new String(msg));
		
		System.out.print("In string format: "); //prints out the byte array
		for(byte b: msg) {
			System.out.print(b + " ");
		}
		System.out.print("\n");
	}
	
	/**
	 * Checks to see if a byte array is on the required format read/write,text,0,text,0...0
	 * @param msg message that is to be checked
	 * @return true if the format is correct and false if it is not
	 */
	public boolean checkMessage(byte[] msg) {

		if (msg[0] == 0 && (msg [1] == 1 || msg[1] == 2)) { //Check to see if the first 2 bytes specify a read or a write;
			int count = 0;
			int i = 2;
			//finds the first 2 0's
			for (; i < msg.length; ++i) { 
				if (msg[i] == 0 && (msg[i] != msg[i - 1])) {
					++count;
				}
				if(count == 2) {
					i++;
					break;
				}
			}
			
			//checks to ensure the rest of the message are only 0's
			for(;i<msg.length; i++) {
				if(msg[i] != 0) {
					return false;
				}
			}
				return true;
			
		} else {
			return false;
		}
		
	}
	
	/**
	 * Reads a file into a byte array
	 * @param path path to the file
	 * @return byte array of the file contents
	 */
	public byte[] readFileIntoArray(String path) {
        byte[] bytesArray = null;

        try {
        	bytesArray = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldnt read properly");
        } 
        return bytesArray;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArr[v >>> 4];
	        hexChars[j * 2 + 1] = hexArr[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public void guiPrintArr(String init, byte[] msg, JTextArea a) {
		String msgAsString = init + " " + new String(msg) + bytesToHex(msg) + "\n";
		a.append(msgAsString);
	}	
	
	public void guiPrint(String msg, JTextArea a) {
		a.append(msg);
	}
	
	/**
	 * Get a block that is to be send through the socket
	 * @param blockNumber block number that is needed
	 * @param byteArray byteArray containing the file contents 
	 * @return temp which holds the byteArr of a blockNumber
	 */
	public byte[] getBlock(int blockNumber, byte[] byteArray) {
		byte[] temp = new byte[512];
		int len = byteArray.length;
		int track = 0;
		for(int i = ((blockNumber - 1) * 512); i < ((blockNumber ) * 512); i ++) {
			if(track < len) {
				temp[track] = byteArray[i];
			}else {
				temp[track] = 0;
			}
			track ++;
		}
		return temp;
	}
	
	/**
	 * Converts a number into a 2 bytes, has a range of 0 to 65535
	 * @param num number to be converted
	 * @return byte array
	 */
	public byte[] intToByte(int num) {
		//Bit shifting operations so DW about it 
		byte[] byteArr = new byte[2];
		byteArr[0] = (byte) (num & 0xFF);
		byteArr[1] = (byte) ((num>>>8)&0xFF);
		return byteArr;
	}
	
	/**
	 * Checks to see if the acknowledge packet is the one that is to be expected
	 * @param packet Packet that has just come in
	 * @param block block number that is expected
	 * @return 
	 */
	public boolean CheckAck(DatagramPacket packet, int block) {
		byte[] blockByte = intToByte(block);
		if(packet.getData()[2] == blockByte[0] && packet.getData()[3] == blockByte[1] && packet.getData()[0]==0 && packet.getData()[1]==4) {
			return true;
		}else {
			return false;
		}
	}
}