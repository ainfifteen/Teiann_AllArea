package Teian;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main{
	public static void main(String args[]) throws IOException{

		double simTime = 1;//シミュレーション間隔 0.1
		double endTime = 1200;//シミュレーション時間(招集しないなら406秒
		double lapseTime = 1;//経過時間
		//long sleepTime = (long) (simTime * 1000);
		
		int []normalPeaple = new int[9];
		
		for(int i = 0; i<9; i++) {
			normalPeaple[i] = 10;
		}

		int []falseNormalPeaple = new int[9];
		
		for(int i = 0; i<9; i++) {
			falseNormalPeaple[i] = normalPeaple[i] * 3;
		}

		Random rnd = new Random();
		int[][]iNormalValue = new int[9][10];
		int[][]jNormalValue = new int[9][10];

		int[][] field = new int[24][24];//データの格納
		for(int i = 0; i < 24; i++) {
			for(int j = 0; j < 24; j++) {
				field[i][j] = 0;
			}
		}

		for(int i = 0; i < 9; i++) {
			for(int k = 0; k < 10; k ++) {
		    	  iNormalValue[i][k] = rnd.nextInt(8) + (i % 3) * 8;
		    	  jNormalValue[i][k] = rnd.nextInt(8) + (i / 3) * 8;
		    	  boolean flag = false;
		    	  field[iNormalValue[i][k]][jNormalValue[i][k]] = 1;
	
		    	  do {
		              flag = false;
		              for (int j = k - 1; j >= 0; j--) {
		                  if (iNormalValue[i][k] == iNormalValue[i][j] && jNormalValue[i][k] == jNormalValue[i][j]) {
		                      flag = true;
		                      iNormalValue[i][k] = rnd.nextInt(8) + (i % 3) * 8;
		                	  jNormalValue[i][k] = rnd.nextInt(8) + (i / 3) * 8;
		                	  field[iNormalValue[i][k]][jNormalValue[i][k]] = 1;
		                  }
		              }
	
		          } while (flag == true);
	
		    	  field[iNormalValue[i][k]][jNormalValue[i][k]] = 1;
		    }
		
		}

		int convocationPeaple = 90;

		int falseConvocationPeaple = convocationPeaple * 2;

		int[]iConvocationValue = new int[90];
		int[]jConvocationValue = new int[90];

		int [][] divisionField = new int[36][36];//データの格納
		for(int i = 0; i < 36; i++) {
			for(int j = 0; j < 36; j++) {
				divisionField[i][j] = 0;
			}
		}

			for(int k = 0; k < 90; k ++) {
		    	  iConvocationValue[k] = rnd.nextInt(36);
		    	  jConvocationValue[k] = rnd.nextInt(36);
		    	  boolean flag = false;
		    	  divisionField[iConvocationValue[k]][jConvocationValue[k]] = 1;
	
		    	  do {
		              flag = false;
		              for (int j = k - 1; j >= 0; j--) {
		                  if (iConvocationValue[k] == iConvocationValue[j] && jConvocationValue[k] == jConvocationValue[j]) {
		                      flag = true;
		                      iConvocationValue[k] = rnd.nextInt(36);
		                	  jConvocationValue[k] = rnd.nextInt(36);
		                	  divisionField[iConvocationValue[k]][jConvocationValue[k]] = 1;
		                  }
		              }
	
		          } while (flag == true);
	
		    	  divisionField[iConvocationValue[k]][jConvocationValue[k]] = 1;
		    }
		



		int[][] area = new int[9][2];//エリア生成
		int x = 0, y = 0;
		for(int i = 0; i < 9; i++) {
			if(i == 0) {
				x = 15;
				y = 225;

			}
			if(i == 3 || i == 6) {
				x = 15;
				y += 240;

			}
			area[i][0] = x;
			area[i][1] = y;
			x += 240;

		}
		
		Drone[] drone = new Drone[9];//ドローン9台生成


		for(int i = 0; i < 9; i++) {//ドローンに値を割り当て
			drone[i] = new Drone(i + 50001, area[i][0], area[i][1]);
		}

		EdgeServer edgeServer = new EdgeServer();//インスタンス生成

		while(lapseTime < endTime) {

			for(int i = 0; i < 9; i++) {
				drone[i].move(simTime);
				drone[i].dataGet(field);
				drone[i].gDataGet(divisionField);

				System.out.println("ドローン"+(i+1)+":状態"+ drone[i].state+" x:"+ drone[i].x+"  y:"+drone[i].y+
						" 方向:"+drone[i].direction + " "+ drone[i].battery+" 招集状態"+drone[i].gatheringState +
						" " );
                falseNormalPeaple[i] -= drone[i].discover;
				falseConvocationPeaple -= drone[i].gDiscover;

				edgeServer.receiveData(area[i][0], area[i][1], simTime);
				
				System.out.println(falseConvocationPeaple);
				
				drone[i].discover = 0;
				drone[i].gDiscover = 0;

				/*if(drone[i].time.equals("sensingstart")) {
					String str =  String.valueOf(lapseTime) + " " + String.valueOf(drone[i].id);
					try {//ファイルへの書き込み
						FileWriter fw = new FileWriter("/Users/sachi/Desktop/sennsingStartTime.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(str);
						bw.newLine();//改行
						bw.flush();
						bw.close();//ファイル閉鎖
					}catch(IOException e) {
						System.out.println("エラー");
					}

				}

				if(drone[i].time.equals("sensingEnd")) {
					String str =  String.valueOf(lapseTime) + " " + String.valueOf(drone[i].id);
					try {//ファイルへの書き込み
						FileWriter fw = new FileWriter("/Users/sachi/Desktop/sennsingEndTime.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(str);
						bw.newLine();//改行
						bw.flush();
						bw.close();//ファイル閉鎖
					}catch(IOException e) {
						System.out.println("エラー");
					}

				}

				if(drone[i].time.equals("missionEnd")) {
					String str =  String.valueOf(lapseTime) + " " + String.valueOf(drone[i].id);
					try {//ファイルへの書き込み
						FileWriter fw = new FileWriter("/Users/sachi/Desktop/missionEndTime.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(str);
						bw.newLine();//改行
						bw.flush();
						bw.close();//ファイル閉鎖
					}catch(IOException e) {
						System.out.println("エラー");
					}

				}*/

				if(drone[i].time.equals("end")) {
					String str =  String.valueOf(lapseTime) + " " + String.valueOf(drone[i].id) + " " + String.valueOf(drone[i].state);
					try {//ファイルへの書き込み
						FileWriter fw = new FileWriter("/Users/sachi/Desktop/conventionSennsingEndTime.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(str);
						bw.newLine();//改行
						bw.flush();
						bw.close();//ファイル閉鎖
					}catch(IOException e) {
						System.out.println("エラー");
					}

				}
				System.out.println("");
			}

			//System.out.println(falsePeaple);
			for(int i = 0; i < 9; i++) {
				if(falseNormalPeaple[i] == 0) {
					String str =  String.valueOf(drone[i].id) + " " +String.valueOf(lapseTime) + " ";
					falseNormalPeaple[i] = 10000;
					try {//ファイルへの書き込み
						FileWriter fw = new FileWriter("/Users/sachi/Desktop/detect1PeapleTime.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(str);
						bw.newLine();//改行
						bw.flush();
						bw.close();//ファイル閉鎖
						//System.exit(0);
					}catch(IOException e) {
						System.out.println("エラー");
					}
				}
			}
			
			if(falseConvocationPeaple == 0) {
				String str =  String.valueOf(lapseTime) + " ";
				falseConvocationPeaple = 10000;
				try {//ファイルへの書き込み
					FileWriter fw = new FileWriter("/Users/sachi/Desktop/detect2PeapleTime.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(str);
					bw.newLine();//改行
					bw.flush();
					bw.close();//ファイル閉鎖
					//System.exit(0);
				}catch(IOException e) {
						System.out.println("エラー");
				}
			}
			


			lapseTime += simTime;
			System.out.println("経過時間："+lapseTime);

			if(lapseTime == endTime) {
				String str =  "　　";
				try {//ファイルへの書き込み
					FileWriter fw = new FileWriter("/Users/sachi/Desktop/sennsingEndTime.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(str);
					bw.newLine();//改行
					bw.flush();
					bw.close();//ファイル閉鎖
				}catch(IOException e) {
					System.out.println("エラー");
				}
				
				try {//ファイルへの書き込み
					FileWriter fw = new FileWriter("/Users/sachi/Desktop/conventionSennsingEndTime.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(str);
					bw.newLine();//改行
					bw.flush();
					bw.close();//ファイル閉鎖
				}catch(IOException e) {
					System.out.println("エラー");
				}
				
				try {//ファイルへの書き込み
					FileWriter fw = new FileWriter("/Users/sachi/Desktop/detect1PeapleTime.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(str);
					bw.newLine();//改行
					bw.flush();
					bw.close();//ファイル閉鎖
					//System.exit(0);
				}catch(IOException e) {
					System.out.println("エラー");
				}
				
				try {//ファイルへの書き込み
					FileWriter fw = new FileWriter("/Users/sachi/Desktop/detect2PeapleTime.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(str);
					bw.newLine();//改行
					bw.flush();
					bw.close();//ファイル閉鎖
					//System.exit(0);
				}catch(IOException e) {
					System.out.println("エラー");
				}
				System.exit(0);
			}

			/*try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}*/
		}


	}
}
