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
			System.out.println("socket : " + socket + "���� ������ ���Ƚ��ϴ�");
			
            while(true)
            {
            	Socket socketUser = serverSocket.accept();
            	System.out.println("Client�� ������ : " + socketUser.getLocalAddress());
            }			
            
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}