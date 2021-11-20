package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Server extends Thread 
{
	static Socket socket = null;
	
	public Server(Socket socket) {
		this.socket = socket; // ���� socket�� �Ҵ�
	}
	
	
    public void run_CSMACD() 
    { //Thread ���� start() �޼ҵ� ��� �� �ڵ����� �ش� �޼ҵ� ����
    	
		try 
		{
			
			while (true) //? �Ҷ� �ѹ��� ����
			{
				System.out.println("test");
			}
		} 
		
		catch (Exception e) 
		{
		    e.printStackTrace(); // ����ó��
		}    		
    }	
	
    
	public static void main(String[] args) 
	{		
		try {
			int socketPort = 1234; // ���� ��Ʈ ������
			ServerSocket serverSocket = new ServerSocket(socketPort); 
			//����Ʈ�� �߰�.
			System.out.println("socket : " + socketPort + "���� ������ ���Ƚ��ϴ�");
			
            // ���� ������ ����� ������ ���ѷ���
            while(true) 
            {
                Socket socketUser = serverSocket.accept();
                Thread thd = new Server(socketUser); 
                
                thd.start(); // ����Ʈ���� �ҷ��� Thread 4�� ����
            }                 
            
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace(); // ����ó��
		}

	}

}
