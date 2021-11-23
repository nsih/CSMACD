import java.io.*;
import java.net.*;
import java.util.*;

public class Link implements Runnable {
	static Vector<Socket> clientList = new Vector<Socket>();
	static Vector<String> link=new Vector<String>();

	private Socket socket = null;

	private static Timer timer = null;
	int sender = -1;
	int receiver = -1;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	
	private static FileWriter fw = null;
	private static BufferedWriter bw= null;

//	static boolean receiverIsReady = false; // �����ڰ� ���� �غ� �Ϸ��ߴ��� ��Ÿ���� ����

	synchronized public static int now() {
		return timer.now();
	}


	public static String fnow() {
		int time = now();
		int min = (int) (time / 60000);
		int sec = (int) (time / 1000);
		int msec = (int) (time % 1000);
		return String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%03d", msec);
	}

	public synchronized static void wFile(String str) {
		try {
			bw.write(str);
			bw.newLine();
			bw.flush(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	

	public Link(Socket socket, Timer timer) {
		try {

			this.socket = socket;
			Link.timer=timer;
			clientList.add(socket);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			int socketNumber = clientList.indexOf(socket);
			out.write(socketNumber + "\n");
			out.flush();
			//wFile("��������ϰ� �ִ� ���Ͽ��� �� ��ȣ �� ���̶�� �˷���");
			//wFile("�������� ������:" + Thread.currentThread().getId() + ", ���� �������� input:" + in);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ����///

	}

	public void run() {
		try {
			// 4�� ��尡 ��� ����� ������ ���
			while (clientList.size() != 4) {
				//wFile("����i am thread:" + Thread.currentThread().getId());
			}

			timer.wake();
			Thread.sleep(10);
			
			//System.out.println("??");
			while (now()<60000) {
				// Ŭ���̾�Ʈ�� ���� ������ ��ٸ�
				String inputMessage = in.readLine();
				
				//�ð����̶� ��
				//�ð� ��
				out.write(now()+"\n");
				out.flush();
				
				inputMessage = in.readLine();
				//���´�? �̶�� ��
				if (inputMessage.contains("get status from Node:")) {
					int size=link.size();
					//������� ����� ����
					if(size==0) {
						out.write("idle\n");
						out.flush();
						//�ð����̶�� ��
						inputMessage=in.readLine();
						//�ð� ��
						//wFile("�����ð�:"+fnow());
						out.write(now()+"\n");
						out.flush();
						//wFile("�����ð�����");
						
						inputMessage=in.readLine();
						//�������ڰ� ��
						if (inputMessage.contains("data send request:")) {
							//��ũ�� ������ �ø�
							int sender = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf(":") + 1));
							int receiver = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf("-") + 1));
							link.add(sender+"-"+receiver);
							wFile(now() + " Node"+sender+" Data Send Request To Node"+receiver);
							//10 ��
							Thread.sleep(10);
							
							//jam�̳�? ��� ��
							inputMessage=in.readLine();
							//jam �˻�
							//jam��
							if(link.size()>=2) {
								//jam ��ȣ ����
								link.add("jam");
								wFile(now() + " Jam: Node"+sender+" Data Send Request To Node"+receiver);
								//Ŭ���̾�Ʈ���� jam�̶�� ����
								out.write("jam:"+now()+"\n");
								out.flush();
								//50 ��
								Thread.sleep(50);
								link.clear();
								//+1������������������
								//clear
								continue;
							//jam �ƴ�
							}else { 
								//Ŭ���̾�Ʈ���� jam �ƴϸ� �۽� ��û �����Ѵٰ� ����
								//"����jam �ƴϴ�");
								wFile(now() + " Accept: Node"+sender+" Data Send Request To Node"+receiver);
								out.write(now() +" Data Send Request Accept from Link\n");
								out.flush();
								// �������� ok ���� ������ ��� ��ٸ�
								while(true) {
									String temp="asdf";
									if(link.size()!=0) temp=link.get(0);
									if(temp.equals("ok!!")) {break;}
								}
								
								synchronized(this) {
								link.clear();

								link.add("start");
								}
								
								//�����̶� ������
								wFile(now()+" Node"+sender+" Data Send Start to Node"+receiver);
								out.write(now()+" Data Send Start to Node "+receiver+"\n");
								out.flush();
								//��ũ���� start �߰�

								//5��
								timer.inc();
								Thread.sleep(1);
								timer.inc();
								Thread.sleep(1);
								timer.inc();
								Thread.sleep(1);
								timer.inc();
								Thread.sleep(1);
								timer.inc();
								Thread.sleep(1);
								//��������
								synchronized(this) {
									link.clear();
									link.add("end");
								}
								
								wFile(now()+" Node"+sender+" Data Send Finished to Node"+receiver);
								out.write(now()+" Data Send Finished to Node "+receiver+"\n");
								out.flush();
								//��ũ���� �� �߰�

								continue;
							}
						 }
						 //�����ڰ� ��
						 else {
							 continue;
						 }
					//�� ������� �� ����� ����
					}else {
						out.write("not idle\n");
						out.flush();
						//�ð����̶�� ��
						inputMessage=in.readLine();
						//�ð� ��
						out.write(now()+"\n");
						out.flush();
						
						inputMessage=in.readLine();
						
						//Ŭ���̾�Ʈ�� ���� �ð��� �̹� �����ٸ�, ������ �ѵ� ���� �����ϴ�? ��� ��
						if(inputMessage.contains("data send request:")) {
							out.write("you cannot\n");
							out.flush();
							int sender = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf(":") + 1));
							int receiver = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf("-") + 1));
							wFile(now()+" Reject: Node"+sender+" Data Send Request To Node"+receiver);
						}
						//Ŭ���̾�Ʈ�� ���� �ð��� �� �����ٸ�, �׳� okay �ϰ� ��
						else {
							
						}
						

						
						
						
						//jam�̳�?��� ��
						inputMessage=in.readLine();
						//jam �˻�
						//jam�̳� �� �ٸ� Ŭ���̾�Ʈ�� �������
						if(link.size()>=2) {
							//wFile("����jam��");
							//Ŭ���̾�Ʈ���� jam�̶�� ����
							out.write("jam\n");
							out.flush();
							continue;
						//jam �ƴ�
						}else {
							//�ٽ� �� �� �˻�
							Thread.sleep(5);
						
							if(link.size()>=2) {
								out.write("jam\n");
								out.flush();
								continue;
							}
							
							//jam �ƴ϶�� ����
							//wFile("������¥ jam �ƴ�");
							out.write("no\n");
							out.flush();
							
							//������ �����İ� ��
							inputMessage=in.readLine();
							//wFile("����inputMessage:"+inputMessage+", �� ��Ŷ�̳�");
							//�� ��Ŷ���� �˻�

							if(link.size()==0) {
								out.write("not yours\n");
								out.flush();
								continue;
							}
							
							
							String packet=link.get(0);
							//wFile("������Ŷ:"+packet);
							int id = Character.getNumericValue(inputMessage.charAt(0));
							int sender = Character.getNumericValue(packet.charAt(0));
							int dest = Character.getNumericValue(packet.charAt(2));
							//���� ��Ŷ�� o
							if(id==dest) {
								//wFile("���� �� ��Ŷ�� ������ ����");
								out.write("yours\n");
								out.flush();
								//tell them accept��� ��
								inputMessage=in.readLine();
								//��ũ�� ok �ø��� �׷��ٰ� ����
								synchronized(this) {
									link.clear(); //����
									link.add("ok!!");
								}
								out.write(now()+" Data Receive Start from Node "+sender+"\n"); 

								out.flush();
								//��ũ�� start �� ������ ���
								while(true) {
									String temp="asdf";
									try {
										if(link.size()!=0) temp=link.get(0);
									}catch(Exception e) {
										temp="asdf";
									}
									//wFile("���������ڰ�������:"+temp);
									if(temp.equals("start")) {break;}
								}
								//wFile("���������ڰ� start�� �޴�");
								//start�ߴٰ� ������
								out.write("start\n");
								out.flush();
								//��ũ�� end �� ������ ���
								while(true) {
									String temp="asdf";
									try {
										if(link.size()!=0) temp=link.get(0);
									}catch(Exception e) {
										temp="asdf";
									}
									//wFile("���������ڰ�������:"+temp);
									if(temp.equals("end")) {break;}
								}
								//wFile("���������ڰ� end�� �޴�");
								//end�ߴٰ� ������
								out.write(now()+" Data Receive Finished from Node"+sender+"\n");
								out.flush();
								// ��ũ û��
								link.clear();
								//�ð����̶�� ��
								inputMessage=in.readLine();
								//�ð� ��
								out.write(now()+"\n");
								out.flush();
								continue;
							}
							//���� ��Ŷ�� x
							else {
								//x��� ������
								out.write("not yours\n");
								out.flush();
								continue;
							}
						}
						
					}
				}
				Thread.sleep(1);
			}
		

		} catch (Exception e) {
			
			//e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				//bw.close(); 
				//fw.close();
//				senderOut.close();
//				receiverOut.close();
			} catch (IOException e) {
				wFile("������ ���� ����");
			}
		}
	}

	public static void main(String[] args) {
		ServerSocket server = null;
		Socket client1 = null;
		Socket client2 = null;
		Socket client3 = null;
		Socket client4 = null;

		try {
			int socketPort = 3112;
			server = new ServerSocket(socketPort);

			
			Timer timer = new Timer();
			Thread t5 = new Thread(timer);
			t5.start();
			
			fw= new FileWriter("Link.txt"); 
			bw= new BufferedWriter(fw); 
			
			client1 = server.accept();
			Link link1 = new Link(client1, timer);
			Thread t1 = new Thread(link1);
			t1.start();

			client2 = server.accept();
			Link link2 = new Link(client2, timer);
			Thread t2 = new Thread(link2);
			t2.start();

			client3 = server.accept();
			Link link3 = new Link(client3, timer);
			Thread t3 = new Thread(link3);
			t3.start();

			client4 = server.accept();
			Link link4 = new Link(client4, timer);
			Thread t4 = new Thread(link4);
			t4.start();

			wFile(now() + " Link Start //00min 00sec 000msec");
			wFile(now() + " System Clock Start //00min 00sec 000msec");
	
			

			while (now()<60000) {
				;
			}
			t1.interrupt();
			t2.interrupt();
			t3.interrupt();
			t4.interrupt();
			
			
			
			wFile(now()+" Link Finished");
			bw.close();
			t5.interrupt();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
				client1.close();
				client2.close();
				client3.close();
				client4.close();
				
			} catch (IOException e) {
				wFile("������ ���� ����");
			}
		}
	}
}