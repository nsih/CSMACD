package Thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import socket.Server;

public class Bus extends Thread		//각 노드마다 스레드 돈다.
{
	Socket socket = null;
	
	public Bus(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		/*
		try 
		{
			/*
			 * 대기시간이 지나면 접속 신청
			 * 접속중인 다른 노드가 없으면 5밀리만큼 점거하고 접근 종료.
			 * 접속중인 다른 노드가 있으면 접속 못하고 대기시간 다시 받음.
			*/
			/*
            while(true) 
            {
                
            }
           
		}
		*/
	}
	
	
	public void WaitTime() //대기시간 계산
	{
		
	}
	
	public int BackoffTimer(int transNum) 
	{ 
		int rndom;
		int temp;
		temp=Math.min(transNum,10);
		rndom=(int)(Math.random()*(Math.pow(2,temp)-1));
		return rndom; 
	}
	
	
	
	
	public void Text()		//로그 텍스트 파일.
	{
		//시작.
		//버스 접근 시도
		// 접근 성공 / 실패
		//데이터 전송성공
		//다른 노드가 나한테 접근 성공/실패
		//다른 노드가 나한테 데이터 줌
		//끝
	}
	
	
	public void SystemTime()	//00:00:000 계산
	{
		
	}
	
	
}
