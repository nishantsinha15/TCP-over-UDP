import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class Sender_2015066
{
    public static void main(String args[]) throws SocketException, UnknownHostException
    {
        Bob b = new Bob();
    }
}

class Bob
{
    private BufferedReader in;
    private DatagramSocket socket;
    private InetAddress ip;
    private byte sendMessage[];
    private byte recieveAck[];
    static int lastRecievedAck;
    static int windowSize = 3;

    Bob() throws UnknownHostException, SocketException
    {
        ip = InetAddress.getByName("localhost");
        socket = new DatagramSocket(9000);
        in = new BufferedReader( new InputStreamReader(System.in));
        SendMessage send = new SendMessage(in, socket, ip, sendMessage);
        RecieveAck recieve = new RecieveAck(in, socket, ip, recieveAck);
        lastRecievedAck = 0; // Can a static variable be directly accessed from it's class?
        send.start();
        recieve.start();
    }
}

class SendMessage extends Thread
{
    private BufferedReader in;
    private DatagramSocket socket;
    private InetAddress ip;
    private byte sendMessage[];

    SendMessage( BufferedReader in, DatagramSocket socket, InetAddress ip, byte sendMessage[] )
    {
        this.in = in;
        this.socket = socket;
        this.ip = ip;
        this.sendMessage = sendMessage;
    }
    public void run()
    {
        while( true )
        {
            try
            {
                for( int i = 0; i< Bob.windowSize; i++)
                    sendMessage = new byte[100000];
                String temp = in.readLine();
                Udp_2015066 pk = new Udp_2015066(temp, Bob.lastRecievedAck + 1 );
                sendMessage = Utility_2015066.convertToBytes(pk);
                DatagramPacket data = new DatagramPacket( sendMessage, sendMessage.length, ip, 5000);
                socket.send( data );
            }
            catch( IOException er )
            {
                er.printStackTrace();
            }
        }
    }
}

class RecieveAck extends Thread
{
    private BufferedReader in;
    private DatagramSocket socket;
    private InetAddress ip;
    private byte recieveAck[];

    RecieveAck( BufferedReader in, DatagramSocket socket, InetAddress ip, byte recieveAck[] )
    {
        this.in = in;
        this.socket = socket;
        this.ip = ip;
        this.recieveAck = recieveAck;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                recieveAck = new byte[100000];
                DatagramPacket packet = new DatagramPacket( recieveAck, recieveAck.length );
                socket.receive( packet );
                Ack_2015066 pk = (Ack_2015066)Utility_2015066.convertToUdp(packet.getData());
                int receivedAck = pk.getSeqNumber();
                System.out.println( "Got = " + receivedAck );
                if( receivedAck > Bob.lastRecievedAck )
                    Bob.lastRecievedAck = receivedAck;
            }
            catch( IOException e )
            {
                System.out.println("Error");
            }
            catch( ClassNotFoundException er)
            {
                System.out.println("Class not found");
            }
        }

    }

}
// class RecieveMessage extends Thread
// {
// 	private BufferedReader in;
// 	private DatagramSocket socket;
// 	private InetAddress ip;
// 	private byte recieveMessage[];

// 	RecieveMessage( BufferedReader in, DatagramSocket socket, InetAddress ip, byte recieveMessage[] )
// 	{
// 		this.in = in;
// 		this.socket = socket;
// 		this.ip = ip;
// 		this.recieveMessage = recieveMessage;
// 	}

// 	public void run()
// 	{
// 		System.out.println("Atleast here");
// 		while(true)
// 		{
// 			try
// 			{
// recieveMessage = new byte[100000];
// DatagramPacket packet = new DatagramPacket( recieveMessage, recieveMessage.length );
// System.out.println("Waiting here");
// socket.receive( packet );

// Udp_2015066 pk = (Udp_2015066)Utility_2015066.convertToUdp_2015066(packet.getData());
// String received = pk.getMessage();
// System.out.println( "Got = " + received +" Ack_2015066 is "+pk.getNextSeqNumber() );
// if( pk.getNextSeqNumber() == Alice.lastRecievedPacket + 1 )
// 	Alice.lastRecievedPacket++;
// System.out.println(pk.getNextSeqNumber()+", "+Alice.lastRecievedPacket +", "+Flag.value);
// System.out.println("Sending Ack_2015066");
// sendAck_2015066 = new byte[100000];
// Ack_2015066 pk = new Ack_2015066( Alice.lastRecievedPacket );
// sendAck_2015066 = Utility_2015066.convertToBytes(pk);
// DatagramPacket data = new DatagramPacket( sendAck_2015066, sendAck_2015066.length, ip, 9000);
// socket.send( data );
// System.out.println("Sent");
// 			}
// 			catch( IOException e )
// 			{
// 				System.out.println("Error");
// 			}
// 			catch( ClassNotFoundException er)
// 			{
// 				System.out.println("Class not found");
// 			}
// 		}

// 	}

// }

// class SendAck_2015066 extends Thread
// {
// 	private BufferedReader in;
// 	private DatagramSocket socket;
// 	private InetAddress ip;
// 	private byte sendAck_2015066[];

// 	SendAck_2015066( BufferedReader in, DatagramSocket socket, InetAddress ip, byte sendAck_2015066[] )
// 	{
// 		this.in = in;
// 		this.socket = socket;
// 		this.ip = ip;
// 		this.sendAck_2015066 = sendAck_2015066;
// 	}

// 	public void run()
// 	{
// 		while( true )
// 		{
// 			if( Flag.value == 1 )
// 			{
// 				try
// 				{
// 					System.out.println(Flag.value);
// 					System.out.println("Got here");
// 					sendAck_2015066 = new byte[100000];
// 					Ack_2015066 pk = new Ack_2015066( Alice.lastRecievedPacket );
// 					sendAck_2015066 = Utility_2015066.convertToBytes(pk);
// 					DatagramPacket data = new DatagramPacket( sendAck_2015066, sendAck_2015066.length, ip, 9000);
// 					socket.send( data );
// 					System.out.println("Sent");
// 					Flag.value = 0;
// 				}
// 				catch( IOException er )
// 				{
// 					er.printStackTrace();
// 				}
// 			}
// 		}
// 	}
// }
