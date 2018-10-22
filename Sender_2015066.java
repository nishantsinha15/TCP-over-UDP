import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    static int cwnd;
    static int ssthresh;
    static int aliceBufferWindow = Integer.MAX_VALUE;
    static int base = 0;
    Bob() throws UnknownHostException, SocketException
    {
        ip = InetAddress.getByName("localhost");
        socket = new DatagramSocket(9000);
        in = new BufferedReader( new InputStreamReader(System.in));
        Bob.lastRecievedAck = 0; // Can a static variable be directly accessed from it's class?
        Bob.cwnd = 1;
        Bob.ssthresh = Integer.MAX_VALUE;
        SendMessage send = new SendMessage(in, socket, ip, sendMessage);
        RecieveAck recieve = new RecieveAck(in, socket, ip, recieveAck);
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
                sendMessage = new byte[100000];
                String temp = "Hello";
                int temp1;
                System.out.println("S : Buffer = "+ Bob.aliceBufferWindow + " Cwnd = " + Bob.cwnd + " Ssthresh = "+Bob.ssthresh);
                if( Bob.cwnd > Bob.aliceBufferWindow )
                {
                    temp1 = Bob.cwnd - Bob.aliceBufferWindow;
                    System.out.println("S : Flow Control - Sending only "+Bob.aliceBufferWindow+" packets and dropping "+temp1+" packets");
                    temp1 = 1;
                    Bob.ssthresh = Bob.cwnd/2;
                    Bob.cwnd = 1;
                    System.out.println("S : SSthresh = "+Bob.ssthresh + " Cwnd = 1");
                }
                else
                    temp1 = Bob.cwnd;

                int temp2 = Bob.lastRecievedAck;
                int tempBase = Bob.base;
                System.out.println("S : Cwnd =" + Bob.cwnd +" Base ="+ tempBase +" temp1 ="+ temp1 );
                for( int i = tempBase + 1; i <= tempBase + temp1 ; i++ )
                {
                    Udp_2015066 pk = new Udp_2015066(temp, i);
                    sendMessage = Utility_2015066.convertToBytes(pk);
                    DatagramPacket data = new DatagramPacket(sendMessage, sendMessage.length, ip, 5000);
                    socket.send(data);
                    System.out.println("S : Sent with seq number "+i);
                    TimeUnit.SECONDS.sleep(1);
                    temp2++;
                }
            }
            catch( IOException er )
            {
                er.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
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
    private int duplicateAck;
    private float increase;
    RecieveAck( BufferedReader in, DatagramSocket socket, InetAddress ip, byte recieveAck[] )
    {
        this.in = in;
        this.socket = socket;
        this.ip = ip;
        this.recieveAck = recieveAck;
        duplicateAck = 0;
        increase =  0;
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
                int x = pk.getBufferWindow();
                System.out.println( "	 	R : Ack Recieved = " + receivedAck );
                System.out.println("	 	R : Previous Max Ack=" + Bob.lastRecievedAck + " Cwnd="+Bob.cwnd);
                if( receivedAck > Bob.lastRecievedAck )
                {
                    if( Bob.ssthresh > Bob.cwnd ) //Slow start
                    {
                        Bob.cwnd += (receivedAck - Bob.lastRecievedAck);
                    }
                    else //Regular increase
                    {
                        increase += 0.2;
                        if( increase > 1)
                        {
                            Bob.cwnd += 1;
                            increase = 0;
                        }
                    }
                    Bob.lastRecievedAck = receivedAck;
                    Bob.aliceBufferWindow = x;
                    duplicateAck = 0;
                    Bob.base = receivedAck;
                }
                else
                {
                    System.out.println("	 	R : Duplicate Ack. Discarding");
                    duplicateAck++;
                    if( duplicateAck >= 3 )
                    {
                        Bob.ssthresh = Bob.cwnd/2;
                        Bob.cwnd /= 2;
                        System.out.println("	 	R : CONGESTION CONTROL. 3 duplicate Acks.\nCwnd = "+Bob.cwnd+" and ssthresh = "+Bob.ssthresh);
                    }
                }
            }
            catch( IOException e )
            {
                System.out.println("	 	R : Error");
            }
            catch( ClassNotFoundException er)
            {
                System.out.println("	 	R : Class not found");
            }
        }

    }

}
