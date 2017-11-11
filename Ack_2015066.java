import java.io.*;
import java.util.*;

public class Ack_2015066 implements Serializable
{
    private int seqNumber;

    Ack_2015066( int seqNumber )
    {
        this.seqNumber = seqNumber;
    }
    public int getSeqNumber()
    {
        return this.seqNumber;
    }
}