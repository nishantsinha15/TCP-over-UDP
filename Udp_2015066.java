import java.io.*;
import java.util.*;

public class Udp_2015066 implements Serializable
{
    private String message;
    private int nextSeqNumber;

    Udp_2015066( String message, int nextSeqNumber )
    {
        this.message = message;
        this.nextSeqNumber = nextSeqNumber;
    }

    public String getMessage()
    {
        return this.message;
    }
    public int getNextSeqNumber()
    {
        return this.nextSeqNumber;
    }
}