import java.io.*;
import java.util.*;
import java.net.*;

public class Reciever_2015066
{
    public static void main(String args[]) throws SocketException, UnknownHostException
    {
        Alice a = new Alice();
    }
}

class Alice
{
    private BufferedReader in;
    private DatagramSocket socket;
    private InetAddress ip;
    private byte sendAck_2015066[];
    private byte recieveMessage[];
    static int lastRecievedPacket;

    Alice() throws UnknownHostException, SocketException
    {
        socket = new DatagramSocket(5000);
        ip = InetAddress.getByName("localhost");
        in = new BufferedReader( new InputStreamReader(System.in));
        lastRecievedPacket = 0;
        while(true)
        {
            try
            {
                recieveMessage = new byte[100000];
                DatagramPacket packet = new DatagramPacket( recieveMessage, recieveMessage.length );
                socket.receive( packet );
                Udp_2015066 pk1 = (Udp_2015066)Utility_2015066.convertToUdp(packet.getData());
                String received = pk1.getMessage();
                if( pk1.getNextSeqNumber() == Alice.lastRecievedPacket + 1 )
                {
                    Alice.lastRecievedPacket++;
                    System.out.println( "Recieved = " + received +"\nAck_2015066 is "+pk1.getNextSeqNumber() );
                }
                else
                {
                    if( pk1.getNextSeqNumber() <= Alice.lastRecievedPacket )
                        System.out.println( "Recieved duplicate packet. Discarding!" );
                    else
                        System.out.println( "Missed "+ (pk1.getNextSeqNumber() -Alice.lastRecievedPacket - 1) +" packets inbetween. Discarding!" );
                }
                System.out.println("Sending Ack_2015066 for "+Alice.lastRecievedPacket );
                sendAck_2015066 = new byte[100000];
                Ack_2015066 pk = new Ack_2015066( Alice.lastRecievedPacket );
                sendAck_2015066 = Utility_2015066.convertToBytes(pk);
                DatagramPacket data = new DatagramPacket( sendAck_2015066, sendAck_2015066.length, ip, 9000);
                socket.send( data );
                System.out.println("Sent");
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