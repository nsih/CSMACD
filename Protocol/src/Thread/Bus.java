package Thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import socket.Server;

public class Bus extends Thread		//�� ��帶�� ������ ����.
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
			 * ���ð��� ������ ���� ��û
			 * �������� �ٸ� ��尡 ������ 5�и���ŭ �����ϰ� ���� ����.
			 * �������� �ٸ� ��尡 ������ ���� ���ϰ� ���ð� �ٽ� ����.
			*/
			/*
            while(true) 
            {
                
            }
           
		}
		*/
	}
	
	
	public void WaitTime() //���ð� ���
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
	
	
	
	
	public void Text()		//�α� �ؽ�Ʈ ����.
	{
		//����.
		//���� ���� �õ�
		// ���� ���� / ����
		//������ ���ۼ���
		//�ٸ� ��尡 ������ ���� ����/����
		//�ٸ� ��尡 ������ ������ ��
		//��
	}
	
	
	public void SystemTime()	//00:00:000 ���
	{
		
	}
	
	
}
