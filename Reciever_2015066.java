import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

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
    static int bufferWindow = 20;
    static int flag = 0;

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
                TimeOut t = new TimeOut(in, socket, ip, lastRecievedPacket);
                t.start();
                flag = 0;
                System.out.println("here");
                socket.receive( packet );
                System.out.println("Caught");
                flag = 1;
                Udp_2015066 pk1 = (Udp_2015066)Utility_2015066.convertToUdp(packet.getData());
                String received = pk1.getMessage();
                if( pk1.getNextSeqNumber() == lastRecievedPacket + 1 )
                {
                    lastRecievedPacket++;
                    System.out.println( "Recieved = " + received +"\nAck_2015066 is "+pk1.getNextSeqNumber() );
//                    System.out.println("  ");
                    String ch = "a";
                    if( ch.equalsIgnoreCase("b") )
                    {
                        Alice.bufferWindow--;
                    }
                    System.out.println("Buffer Window = " + Alice.bufferWindow);
                }
                else
                {
                    if( pk1.getNextSeqNumber() <= lastRecievedPacket )
                        System.out.println( "Recieved duplicate packet = "+( pk1.getNextSeqNumber()-1 )+". Discarding!" );
                    else
                        System.out.println( "Missed "+ (pk1.getNextSeqNumber() - lastRecievedPacket - 1) +" packets inbetween. Discarding!" );
                }
                System.out.println("Sending Ack_2015066 for "+ lastRecievedPacket );
                sendAck_2015066 = new byte[100000];
                Ack_2015066 pk = new Ack_2015066( lastRecievedPacket, bufferWindow );
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

class TimeOut extends Thread
{
    private BufferedReader in;
    private DatagramSocket socket;
    private InetAddress ip;
    private byte sendMessage[];
    private int pk1;
    private byte sendAck_2015066[];

    TimeOut(BufferedReader in, DatagramSocket socket, InetAddress ip, int pk)
    {
        this.in = in;
        this.socket = socket;
        this.ip = ip;
        this.pk1 = pk;
    }
    public void run()
    {
        while(true)
        {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Alice.flag == 0 && pk1 == Alice.lastRecievedPacket) {
                System.out.println("TIME OUT!Sending again");

                try {
                    System.out.println("\tSending Ack_2015066 for " + pk1);
                    sendAck_2015066 = new byte[100000];
                    Ack_2015066 pk = new Ack_2015066(pk1, Alice.bufferWindow);
                    sendAck_2015066 = Utility_2015066.convertToBytes(pk);
                    DatagramPacket data = new DatagramPacket(sendAck_2015066, sendAck_2015066.length, ip, 9000);
                    socket.send(data);
                    System.out.println("\tSent");
                } catch (IOException er) {
                    er.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

}