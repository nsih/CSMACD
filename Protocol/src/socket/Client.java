package socket;

import java.io.IOException;
import java.net.Socket;

public class Client 
{
	public float waitingTime;
	
	
	public static void main(String[] args) {
		try 
		{
			Socket socket1 = null;
			Socket socket2 = null;
			Socket socket3 = null;
			Socket socket4 = null;
			
			socket1 = new Socket("192.168.56.1", 1234);
			System.out.println("1");
			socket2 = new Socket("192.168.56.1", 1234);
			System.out.println("2");
			socket3 = new Socket("192.168.56.1", 1234);
			System.out.println("3");
			socket4 = new Socket("192.168.56.1", 1234);
			System.out.println("4");
			
			
			//소켓 종료까지 무한루프
            while(true) 
            {
                //Bus node1 = new Bus(socket1);
            }  
		} 
		
		
		catch (IOException e) 
		{
			e.printStackTrace(); // 예외처리
		}
	}
}