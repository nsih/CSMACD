package Dt;

import java.io.IOException;
import java.net.Socket;

//192.168.56.1

public class Client 
{
	public static void main(String[] args)
	{
		try
		{
			Socket socket = new Socket("192.168.56.1",1234);
			System.out.println("Á¢¼Ó");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
}