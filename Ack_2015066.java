import java.io.*;
import java.util.*;

public class Ack_2015066 implements Serializable
{
    private int seqNumber;
    private int bufferWindow;
    Ack_2015066( int seqNumber, int bufferWindow )
    {
        this.seqNumber = seqNumber;
        this.bufferWindow = bufferWindow;
    }
    public int getSeqNumber()
    {
        return this.seqNumber;
    }
    public int getBufferWindow() { return this.bufferWindow; }
}