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

//	static boolean receiverIsReady = false; // 수신자가 수신 준비를 완료했는지 나타내는 상태

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
			//wFile("ㅁㅁ통신하고 있는 소켓에게 네 번호 몇 번이라고 알려줌");
			//wFile("ㅁㅁ현재 스레드:" + Thread.currentThread().getId() + ", 현재 스레드의 input:" + in);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ㄴㄴ///

	}

	public void run() {
		try {
			// 4개 노드가 모두 연결될 때까지 대기
			while (clientList.size() != 4) {
				//wFile("ㅁㅁi am thread:" + Thread.currentThread().getId());
			}

			timer.wake();
			Thread.sleep(10);
			
			//System.out.println("??");
			while (now()<60000) {
				// 클라이언트가 뭔갈 보내길 기다림
				String inputMessage = in.readLine();
				
				//시간좀이라 옴
				//시간 줌
				out.write(now()+"\n");
				out.flush();
				
				inputMessage = in.readLine();
				//상태는? 이라고 옴
				if (inputMessage.contains("get status from Node:")) {
					int size=link.size();
					//비었으면 비었다 보냄
					if(size==0) {
						out.write("idle\n");
						out.flush();
						//시간좀이라고 옴
						inputMessage=in.readLine();
						//시간 줌
						//wFile("ㅁㅁ시간:"+fnow());
						out.write(now()+"\n");
						out.flush();
						//wFile("ㅁㅁ시간줬음");
						
						inputMessage=in.readLine();
						//전송하자고 옴
						if (inputMessage.contains("data send request:")) {
							//링크에 데이터 올림
							int sender = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf(":") + 1));
							int receiver = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf("-") + 1));
							link.add(sender+"-"+receiver);
							wFile(now() + " Node"+sender+" Data Send Request To Node"+receiver);
							//10 쉼
							Thread.sleep(10);
							
							//jam이냐? 라고 옴
							inputMessage=in.readLine();
							//jam 검사
							//jam임
							if(link.size()>=2) {
								//jam 신호 보냄
								link.add("jam");
								wFile(now() + " Jam: Node"+sender+" Data Send Request To Node"+receiver);
								//클라이언트에게 jam이라고 보냄
								out.write("jam:"+now()+"\n");
								out.flush();
								//50 쉼
								Thread.sleep(50);
								link.clear();
								//+1ㅁㅁㅁㅁㅁㅁㅁㅁㅁ
								//clear
								continue;
							//jam 아님
							}else { 
								//클라이언트에게 jam 아니며 송신 요청 수락한다고 보냄
								//"ㅁㅁjam 아니다");
								wFile(now() + " Accept: Node"+sender+" Data Send Request To Node"+receiver);
								out.write(now() +" Data Send Request Accept from Link\n");
								out.flush();
								// 수신자의 ok 들어올 때까지 계속 기다림
								while(true) {
									String temp="asdf";
									if(link.size()!=0) temp=link.get(0);
									if(temp.equals("ok!!")) {break;}
								}
								
								synchronized(this) {
								link.clear();

								link.add("start");
								}
								
								//시작이라 보내줌
								wFile(now()+" Node"+sender+" Data Send Start to Node"+receiver);
								out.write(now()+" Data Send Start to Node "+receiver+"\n");
								out.flush();
								//링크에도 start 추가

								//5초
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
								//끝보내줌
								synchronized(this) {
									link.clear();
									link.add("end");
								}
								
								wFile(now()+" Node"+sender+" Data Send Finished to Node"+receiver);
								out.write(now()+" Data Send Finished to Node "+receiver+"\n");
								out.flush();
								//링크에도 끝 추가

								continue;
							}
						 }
						 //끝내자고 옴
						 else {
							 continue;
						 }
					//안 비었으면 안 비었다 보냄
					}else {
						out.write("not idle\n");
						out.flush();
						//시간좀이라고 옴
						inputMessage=in.readLine();
						//시간 줌
						out.write(now()+"\n");
						out.flush();
						
						inputMessage=in.readLine();
						
						//클라이언트가 보낼 시간이 이미 지났다면, 지났긴 한데 전송 가능하니? 라고 옴
						if(inputMessage.contains("data send request:")) {
							out.write("you cannot\n");
							out.flush();
							int sender = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf(":") + 1));
							int receiver = Character.getNumericValue(inputMessage.charAt(inputMessage.indexOf("-") + 1));
							wFile(now()+" Reject: Node"+sender+" Data Send Request To Node"+receiver);
						}
						//클라이언트가 보낼 시간이 안 지났다면, 그냥 okay 하고 옴
						else {
							
						}
						

						
						
						
						//jam이냐?라고 옴
						inputMessage=in.readLine();
						//jam 검사
						//jam이나 뭐 다른 클라이언트가 통신중임
						if(link.size()>=2) {
							//wFile("ㅁㅁjam임");
							//클라이언트에게 jam이라고 보냄
							out.write("jam\n");
							out.flush();
							continue;
						//jam 아님
						}else {
							//다시 한 번 검사
							Thread.sleep(5);
						
							if(link.size()>=2) {
								out.write("jam\n");
								out.flush();
								continue;
							}
							
							//jam 아니라고 보냄
							//wFile("ㅁㅁ진짜 jam 아님");
							out.write("no\n");
							out.flush();
							
							//나한테 보내냐고 옴
							inputMessage=in.readLine();
							//wFile("ㅁㅁinputMessage:"+inputMessage+", 내 패킷이냐");
							//네 패킷인지 검사

							if(link.size()==0) {
								out.write("not yours\n");
								out.flush();
								continue;
							}
							
							
							String packet=link.get(0);
							//wFile("ㅁㅁ패킷:"+packet);
							int id = Character.getNumericValue(inputMessage.charAt(0));
							int sender = Character.getNumericValue(packet.charAt(0));
							int dest = Character.getNumericValue(packet.charAt(2));
							//너의 패킷이 o
							if(id==dest) {
								//wFile("ㅁㅁ 내 패킷이 맞으니 받자");
								out.write("yours\n");
								out.flush();
								//tell them accept라고 옴
								inputMessage=in.readLine();
								//링크에 ok 올리고 그랬다고 말함
								synchronized(this) {
									link.clear(); //ㅁㅁ
									link.add("ok!!");
								}
								out.write(now()+" Data Receive Start from Node "+sender+"\n"); 

								out.flush();
								//링크에 start 올 때까지 대기
								while(true) {
									String temp="asdf";
									try {
										if(link.size()!=0) temp=link.get(0);
									}catch(Exception e) {
										temp="asdf";
									}
									//wFile("ㅁㅁ수신자가받은것:"+temp);
									if(temp.equals("start")) {break;}
								}
								//wFile("ㅁㅁ수신자가 start를 받다");
								//start했다고 보내줌
								out.write("start\n");
								out.flush();
								//링크에 end 올 때까지 대기
								while(true) {
									String temp="asdf";
									try {
										if(link.size()!=0) temp=link.get(0);
									}catch(Exception e) {
										temp="asdf";
									}
									//wFile("ㅁㅁ수신자가받은것:"+temp);
									if(temp.equals("end")) {break;}
								}
								//wFile("ㅁㅁ수신자가 end를 받다");
								//end했다고 보내줌
								out.write(now()+" Data Receive Finished from Node"+sender+"\n");
								out.flush();
								// 링크 청소
								link.clear();
								//시간좀이라고 옴
								inputMessage=in.readLine();
								//시간 줌
								out.write(now()+"\n");
								out.flush();
								continue;
							}
							//너의 패킷이 x
							else {
								//x라고 보내줌
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
				wFile("소켓을 닫지 못함");
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
				wFile("소켓을 닫지 못함");
			}
		}
	}
}