import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
	
	
	private static int transmissionTime = 0; // 내가 전송할 시간
	private static int transNum = 0; // 재전송 횟수
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
		//wFile("ㅁㅁmy id:" + myid);
		while (true) {
			int num = (int) (Math.random() * 4);
			if (num != myid)
				return num;
		}
	}

	public static void main(String[] args) {
		transmissionTime = BackoffTimer(transNum);
		
		try {
			//접속 시도
			Socket socket = new Socket("localhost", 3112);
			//wFile("ㅁㅁ접속 성공");
			// 초기 설정: in, out, myid, receiver
			

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String inputMessage = in.readLine();
			myid = Integer.parseInt(inputMessage);
			//wFile("ㅁㅁid를 받았음:" + myid);
			int receiver = random0to3(myid);
			fw= new FileWriter("Node"+myid+".txt"); 
			bw= new BufferedWriter(fw); 
			
			while(true) {
				//누가 전송하는 바람에 내 시간이 지났는가?
				//시간좀
				out.write("give me time\n");
				out.flush();
				inputMessage=in.readLine();
				noww=Integer.parseInt(inputMessage);
				
				if (noww>=60000) break;
				
				if (noww>transmissionTime && finished==false) {
					//backoff 알고리즘
					transNum++;
					int tt=BackoffTimer(transNum);
					transmissionTime=noww+tt;
					//transmissionTime += tt;
					wFile(noww+" 데이터 보낼 타이밍 놓침-> Exponential Back-off time: "+tt);
				}
				
				
				// link 상태 물어봄

				out.write("get status from Node:" + myid + "\n");
				out.flush();
				inputMessage=in.readLine();
				//비었음
				if (inputMessage.equals("idle")) {
					//시간좀
					out.write("give me time\n");
					out.flush();
					
					inputMessage=in.readLine();
					//wFile("ㅁㅁ시간:"+inputMessage);
					//wFile("ㅁㅁ내가 보낼 시간:"+transmissionTime);
					//wFile("ㅁㅁfinished:"+finished);
					//시간 받았음
					noww=Integer.parseInt(inputMessage);
					//내가 보낼 시간임
					if (noww==transmissionTime) {
						//전송하자
						wFile(noww+" Data send request to Node"+receiver);
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						Thread.sleep(10);
						//jam이냐?
						//wFile("ㅁㅁjam이냐");
						out.write("jam?\n");
						out.flush();
						inputMessage=in.readLine();
						
						//jam이다
						if (inputMessage.contains("jam:")) {
							//wFile("ㅁㅁjam이래");
							//backoff 알고리즘
							int nowww=Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1));
							transNum++;
							int tempInc=BackoffTimer(transNum);
							wFile(Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1))+" jam->Exponential Back-off Time: "+tempInc);
							//transmissionTime += tempInc;
							transmissionTime = nowww+tempInc;
							continue;
						//jam 아니다
						}else {
						    //accept라고 옴
							wFile(inputMessage);
							//시작이 올 때까지 대기
							inputMessage=in.readLine();
							wFile(inputMessage);
							//끝이 올 때까지 대기
							inputMessage=in.readLine();
							wFile(inputMessage);
							finished=true;
							continue;
						}
					}
					//내가 보낼 시간이 아님
					else {
						//wFile("ㅁㅁ내가 보낼 시간 아님");
						out.write("okay\n");
						out.flush();
					}
				}
				//안비었음
				else {
					//시간좀
					out.write("give me time\n");
					out.flush();
					inputMessage=in.readLine();
					//시간 받았음
					noww=Integer.parseInt(inputMessage);
					
					//내가 보낼 시간이 이미 지났다면 지났긴 한데 전송 가능하니? 라고 물어봄
					if (noww>=transmissionTime && finished==false) {
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						//cannot이라고 답장 옴
						inputMessage=in.readLine();
						wFile(noww+" Data Send Request Reject From Link");
						
						transNum++;
						int tempInc=BackoffTimer(transNum);
						wFile(noww+" Expotential Back-off Time: "+tempInc);
						
						//transmissionTime += tempInc;
						transmissionTime = noww+tempInc;
					}
					//내가 보낼 시간이 아직 안 지났다면 okay 하고 넘어감
					else {
						out.write("okay\n");
						out.flush();
					}
					

					//jam이냐?
					out.write("jam?\n");
					out.flush();
					inputMessage=in.readLine();
					//jam이다
					if (inputMessage.equals("jam")) {
						continue;
					//jam 아니다
					}else {
						
						
						
						
							
						//나한테 보내냐?
						out.write(myid+":my packet??\n");
						out.flush();
						inputMessage=in.readLine();
						//내거다
						if(inputMessage.equals("yours")) {
							//ok 올려줘
							out.write("tell them accept\n");
							out.flush();
							
							//wFile("ㅁㅁ받는다");
							
							inputMessage=in.readLine(); //ok 올렸다고 온다
							wFile(inputMessage);
							//wFile("ok 올렸대");
							
							inputMessage=in.readLine(); //start 올 때까지 대기
							//wFile("start 왔다");
							//wFile(inputMessage);
							inputMessage=in.readLine(); //end 올 때까지 대기
							//wFile("end 왔다");
							wFile(inputMessage);
							
							//시간좀 줘
							out.write("give me time\n");
							out.flush();
							inputMessage=in.readLine();
							noww=Integer.parseInt(inputMessage);
							
							//내가 보낼 시간 지났는지 검사함
							if (noww>=transmissionTime && finished==false) {
								//backoff 알고리즘
								transNum++;
								int tt=BackoffTimer(transNum);
								transmissionTime = noww+tt;
								wFile(noww+" 데이터 보낼 타이밍 놓침-> Exponential Back-off time: "+tt);
								//wFile("ㅁㅁtransmission time: "+transmissionTime);
							}
						//아니다
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
