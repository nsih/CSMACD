package Dt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	public static void main(String[] args) {
		try 
		{
			int socket = 1234;
			ServerSocket serverSocket = new ServerSocket(socket); 
			System.out.println("socket : " + socket + "으로 서버가 열렸습니다");
			
            while(true)
            {
            	Socket socketUser = serverSocket.accept();
            	System.out.println("Client가 접속함 : " + socketUser.getLocalAddress());
            }			
            
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}