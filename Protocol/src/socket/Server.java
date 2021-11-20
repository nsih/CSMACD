package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Server extends Thread 
{
	static Socket socket = null;
	
	public Server(Socket socket) {
		this.socket = socket; // 유저 socket을 할당
	}
	
	
    public void run_CSMACD() 
    { //Thread 에서 start() 메소드 사용 시 자동으로 해당 메소드 시작
    	
		try 
		{
			
			while (true) //? 할때 한번씩 수행
			{
				System.out.println("test");
			}
		} 
		
		catch (Exception e) 
		{
		    e.printStackTrace(); // 예외처리
		}    		
    }	
	
    
	public static void main(String[] args) 
	{		
		try {
			int socketPort = 1234; // 소켓 포트 설정용
			ServerSocket serverSocket = new ServerSocket(socketPort); 
			//리스트에 추가.
			System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다");
			
            // 소켓 서버가 종료될 때까지 무한루프
            while(true) 
            {
                Socket socketUser = serverSocket.accept();
                Thread thd = new Server(socketUser); 
                
                thd.start(); // 리스트에서 불러서 Thread 4개 시작
            }                 
            
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace(); // 예외처리
		}

	}

}
