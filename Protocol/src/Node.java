import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
	
	
	private static int transmissionTime = 0; // 鎧亜 穿勺拝 獣娃
	private static int transNum = 0; // 仙穿勺 判呪
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
		//wFile("けけmy id:" + myid);
		while (true) {
			int num = (int) (Math.random() * 4);
			if (num != myid)
				return num;
		}
	}

	public static void main(String[] args) {
		transmissionTime = BackoffTimer(transNum);
		
		try {
			//羨紗 獣亀
			Socket socket = new Socket("localhost", 3112);
			//wFile("けけ羨紗 失因");
			// 段奄 竺舛: in, out, myid, receiver
			

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String inputMessage = in.readLine();
			myid = Integer.parseInt(inputMessage);
			//wFile("けけid研 閤紹製:" + myid);
			int receiver = random0to3(myid);
			fw= new FileWriter("Node"+myid+".txt"); 
			bw= new BufferedWriter(fw); 
			
			while(true) {
				//刊亜 穿勺馬澗 郊寓拭 鎧 獣娃戚 走概澗亜?
				//獣娃岨
				out.write("give me time\n");
				out.flush();
				inputMessage=in.readLine();
				noww=Integer.parseInt(inputMessage);
				
				if (noww>=60000) break;
				
				if (noww>transmissionTime && finished==false) {
					//backoff 硝壱軒葬
					transNum++;
					int tt=BackoffTimer(transNum);
					transmissionTime=noww+tt;
					//transmissionTime += tt;
					wFile(noww+" 汽戚斗 左馨 展戚講 兜徴-> Exponential Back-off time: "+tt);
				}
				
				
				// link 雌殿 弘嬢砂

				out.write("get status from Node:" + myid + "\n");
				out.flush();
				inputMessage=in.readLine();
				//搾醸製
				if (inputMessage.equals("idle")) {
					//獣娃岨
					out.write("give me time\n");
					out.flush();
					
					inputMessage=in.readLine();
					//wFile("けけ獣娃:"+inputMessage);
					//wFile("けけ鎧亜 左馨 獣娃:"+transmissionTime);
					//wFile("けけfinished:"+finished);
					//獣娃 閤紹製
					noww=Integer.parseInt(inputMessage);
					//鎧亜 左馨 獣娃績
					if (noww==transmissionTime) {
						//穿勺馬切
						wFile(noww+" Data send request to Node"+receiver);
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						Thread.sleep(10);
						//jam戚劃?
						//wFile("けけjam戚劃");
						out.write("jam?\n");
						out.flush();
						inputMessage=in.readLine();
						
						//jam戚陥
						if (inputMessage.contains("jam:")) {
							//wFile("けけjam戚掘");
							//backoff 硝壱軒葬
							int nowww=Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1));
							transNum++;
							int tempInc=BackoffTimer(transNum);
							wFile(Integer.parseInt(inputMessage.substring(inputMessage.indexOf(":")+1))+" jam->Exponential Back-off Time: "+tempInc);
							//transmissionTime += tempInc;
							transmissionTime = nowww+tempInc;
							continue;
						//jam 焼艦陥
						}else {
						    //accept虞壱 身
							wFile(inputMessage);
							//獣拙戚 臣 凶猿走 企奄
							inputMessage=in.readLine();
							wFile(inputMessage);
							//魁戚 臣 凶猿走 企奄
							inputMessage=in.readLine();
							wFile(inputMessage);
							finished=true;
							continue;
						}
					}
					//鎧亜 左馨 獣娃戚 焼還
					else {
						//wFile("けけ鎧亜 左馨 獣娃 焼還");
						out.write("okay\n");
						out.flush();
					}
				}
				//照搾醸製
				else {
					//獣娃岨
					out.write("give me time\n");
					out.flush();
					inputMessage=in.readLine();
					//獣娃 閤紹製
					noww=Integer.parseInt(inputMessage);
					
					//鎧亜 左馨 獣娃戚 戚耕 走概陥檎 走概延 廃汽 穿勺 亜管馬艦? 虞壱 弘嬢砂
					if (noww>=transmissionTime && finished==false) {
						out.write("data send request:" + myid + "-" + receiver + "\n");
						out.flush();
						//cannot戚虞壱 岩舌 身
						inputMessage=in.readLine();
						wFile(noww+" Data Send Request Reject From Link");
						
						transNum++;
						int tempInc=BackoffTimer(transNum);
						wFile(noww+" Expotential Back-off Time: "+tempInc);
						
						//transmissionTime += tempInc;
						transmissionTime = noww+tempInc;
					}
					//鎧亜 左馨 獣娃戚 焼送 照 走概陥檎 okay 馬壱 角嬢姶
					else {
						out.write("okay\n");
						out.flush();
					}
					

					//jam戚劃?
					out.write("jam?\n");
					out.flush();
					inputMessage=in.readLine();
					//jam戚陥
					if (inputMessage.equals("jam")) {
						continue;
					//jam 焼艦陥
					}else {
						
						
						
						
							
						//蟹廃砺 左鎧劃?
						out.write(myid+":my packet??\n");
						out.flush();
						inputMessage=in.readLine();
						//鎧暗陥
						if(inputMessage.equals("yours")) {
							//ok 臣形操
							out.write("tell them accept\n");
							out.flush();
							
							//wFile("けけ閤澗陥");
							
							inputMessage=in.readLine(); //ok 臣携陥壱 紳陥
							wFile(inputMessage);
							//wFile("ok 臣携企");
							
							inputMessage=in.readLine(); //start 臣 凶猿走 企奄
							//wFile("start 尽陥");
							//wFile(inputMessage);
							inputMessage=in.readLine(); //end 臣 凶猿走 企奄
							//wFile("end 尽陥");
							wFile(inputMessage);
							
							//獣娃岨 操
							out.write("give me time\n");
							out.flush();
							inputMessage=in.readLine();
							noww=Integer.parseInt(inputMessage);
							
							//鎧亜 左馨 獣娃 走概澗走 伊紫敗
							if (noww>=transmissionTime && finished==false) {
								//backoff 硝壱軒葬
								transNum++;
								int tt=BackoffTimer(transNum);
								transmissionTime = noww+tt;
								wFile(noww+" 汽戚斗 左馨 展戚講 兜徴-> Exponential Back-off time: "+tt);
								//wFile("けけtransmission time: "+transmissionTime);
							}
						//焼艦陥
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
