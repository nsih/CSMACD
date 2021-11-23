import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
	
	
	private static int transmissionTime = 0; // ���� ������ �ð�
	private static int transNum = 0; // ������ Ƚ��
	private static int noww=-1;
	static FileWriter fw=null;
	static BufferedWriter bw=null;
	static int myid=-1;
	
	private static boolean finished = false;

	/*
	 * Randomly selected back-off time, Calculated according to the retransmission
	 * number random multiples by k times for k-th retransmission
	 * 
	 * @param transNum : number of retransmission
	 * 
	 * @return Random multiples
	 */
	public static int BackoffTimer(int transNum) {
		int rndom;
		int temp;
		temp = Math.min(transNum, 10);
		rndom = (int) (Math.random() * (Math.pow(2, temp) - 1));
		return rndom;
	}

	public synchronized static void wFile(String str) {
		try {
			bw.write(str);
			bw.newLine();
			bw.flush(); 
		} catch (IOException e) {
			wFile(noww+" Node"+myid+" Finished");
			//e.printStackTrace();
		} 
		
	}
	
	public synchronized static int random0to3(int myid) {
		//wFile("����my id:" + myid);
		while (true) {
			int num = (int) (Math.random() * 4);
			if (num != myid)
				return num;
		}
	}

	public static void main(String[] args) {
		transmissionTime = BackoffTimer(transNum);
		
		try {
			//���� �õ�
			Socket socket = new Socket("localhost", 3112);
			//wFile("�������� ����");
			// �ʱ� ����: in, out, myid, receiver
			

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String inputMessage = in.readLine();
			myid = Integer.parseInt(inputMessage);
			//wFile("����id�� �޾���:" + myid);
			int receiver = random0to3(myid);
			fw= new FileWriter("Node"+myid+".txt"); 
			bw= new BufferedWriter(fw); 
			
			while(true) {
				//���� �����ϴ� �ٶ��� �� �ð��� �����°�?
				//�ð���
				out.write("give me time\n");
				out.flush();
				inputMessage=in.readLine();
				noww=Integer.parseInt(inputMessage);
				
				if (noww>=60000) break;
				
				if (noww>transmissionTime && finished==false) {
					//backoff �˰���
					transNum++;
					int tt=BackoffTimer(transNum);
					transmissionTime=noww+tt;
					//transmissionTime += tt;
					wFile(noww+" ������ ���� Ÿ�̹� ��ħ-> Exponential Back-off time: "+tt);
				}
				
				
				// link ���� ���

				out.write("get status from Node:" + myid + "\n");
				out.flush();
				inputMessage=in.readLine();
				//�����
				if (inputMessage.equals("idle")) {
					//�ð���
					out.write("give me time\n");
					out.flush();
					
					inputMessage=in.readLine();
					//wFile("�����ð�:"+inputMessage);
					//wFile("�������� ���� �ð�:"+transmissionTime);
					//wFile("����finished:"+finished);
					//�ð� �޾���
					noww=Integer.parseInt(inputMessage);
					//���� ���� �ð���
					if (noww==transmissionTime) {
						//��������
						wFile(noww+" Data send request to Node"+receiver);
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						Thread.sleep(10);
						//jam�̳�?
						//wFile("����jam�̳�");
						out.write("jam?\n");
						out.flush();
						inputMessage=in.readLine();
						
						//jam�̴�
						if (inputMessage.contains("jam:")) {
							//wFile("����jam�̷�");
							//backoff �˰���
							int nowww=Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1));
							transNum++;
							int tempInc=BackoffTimer(transNum);
							wFile(Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1))+" jam->Exponential Back-off Time: "+tempInc);
							//transmissionTime += tempInc;
							transmissionTime = nowww+tempInc;
							continue;
						//jam �ƴϴ�
						}else {
						    //accept��� ��
							wFile(inputMessage);
							//������ �� ������ ���
							inputMessage=in.readLine();
							wFile(inputMessage);
							//���� �� ������ ���
							inputMessage=in.readLine();
							wFile(inputMessage);
							finished=true;
							continue;
						}
					}
					//���� ���� �ð��� �ƴ�
					else {
						//wFile("�������� ���� �ð� �ƴ�");
						out.write("okay\n");
						out.flush();
					}
				}
				//�Ⱥ����
				else {
					//�ð���
					out.write("give me time\n");
					out.flush();
					inputMessage=in.readLine();
					//�ð� �޾���
					noww=Integer.parseInt(inputMessage);
					
					//���� ���� �ð��� �̹� �����ٸ� ������ �ѵ� ���� �����ϴ�? ��� ���
					if (noww>=transmissionTime && finished==false) {
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						//cannot�̶�� ���� ��
						inputMessage=in.readLine();
						wFile(noww+" Data Send Request Reject From Link");
						
						transNum++;
						int tempInc=BackoffTimer(transNum);
						wFile(noww+" Expotential Back-off Time: "+tempInc);
						
						//transmissionTime += tempInc;
						transmissionTime = noww+tempInc;
					}
					//���� ���� �ð��� ���� �� �����ٸ� okay �ϰ� �Ѿ
					else {
						out.write("okay\n");
						out.flush();
					}
					

					//jam�̳�?
					out.write("jam?\n");
					out.flush();
					inputMessage=in.readLine();
					//jam�̴�
					if (inputMessage.equals("jam")) {
						continue;
					//jam �ƴϴ�
					}else {
						
						
						
						
							
						//������ ������?
						out.write(myid+":my packet??\n");
						out.flush();
						inputMessage=in.readLine();
						//���Ŵ�
						if(inputMessage.equals("yours")) {
							//ok �÷���
							out.write("tell them accept\n");
							out.flush();
							
							//wFile("�����޴´�");
							
							inputMessage=in.readLine(); //ok �÷ȴٰ� �´�
							wFile(inputMessage);
							//wFile("ok �÷ȴ�");
							
							inputMessage=in.readLine(); //start �� ������ ���
							//wFile("start �Դ�");
							//wFile(inputMessage);
							inputMessage=in.readLine(); //end �� ������ ���
							//wFile("end �Դ�");
							wFile(inputMessage);
							
							//�ð��� ��
							out.write("give me time\n");
							out.flush();
							inputMessage=in.readLine();
							noww=Integer.parseInt(inputMessage);
							
							//���� ���� �ð� �������� �˻���
							if (noww>=transmissionTime && finished==false) {
								//backoff �˰���
								transNum++;
								int tt=BackoffTimer(transNum);
								transmissionTime = noww+tt;
								wFile(noww+" ������ ���� Ÿ�̹� ��ħ-> Exponential Back-off time: "+tt);
								//wFile("����transmission time: "+transmissionTime);
							}
						//�ƴϴ�
						}else {
							continue;
						}
					}
				}
			}
			wFile(noww+" Node"+myid+" Finished");
		} catch (Exception e) {
			wFile(noww+" Node"+myid+" Finished");
		}
	}
}
