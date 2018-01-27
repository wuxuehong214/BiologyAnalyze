package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * 
 * 只将repeat中alu simple repeat去掉
 * @author wuxuehong
 * 2012-4-17
 */
public class Main20120417 {
 
	byte[] flag = new byte[51304567]; 
	private int length = 51304566;
	
	/**
	 * W+
	 * @param filename
	 */
	public void readRepeatPos(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine(); //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int start ,end;
			String family;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				start = s.nextInt();
				end = s.nextInt();
				for(int i=0;i<4;i++)s.next();
				family = s.next();
				//如果是 alu 或者simplerepeat 则表明为1
				if(family.equals("Alu") || family.equals("Simple_repeat")){
					for(int i=start;i<=end;i++)
						flag[i] = 1;
				}else{//如果是其他reapeat则表明为2
					for(int i=start;i<=end;i++)
						flag[i] = 2;
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * C+
	 * @param filename
	 */
	public void readRepeatNeg(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine(); //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int start ,end, startTemp, endTemp;
			String family;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				startTemp = s.nextInt();
				endTemp = s.nextInt();
				
				start = length-endTemp+1;
				end = length-startTemp+1;
				
				for(int i=0;i<4;i++)s.next();
				family = s.next();
				//如果是 alu 或者simplerepeat 则表明为1
				if(family.equals("Alu") || family.equals("Simple_repeat")){
					for(int i=start;i<=end;i++)
						flag[i]= 1;
				}else{ //如果是其他reapeat则表明为2
					for(int i=start;i<=end;i++)
						flag[i] = 2;
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * filter the reads
	 * @param filename
	 * @param outFile
	 */
	public void filterReads(String filename,String outFile){
		try{
			int maxLen = 20;
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Scanner s = null;
			int start,end,count;
			boolean filter = true;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
				count = 0;
				
				//看当前reads中 是不是都是alu 或者simple repeat   如果是 则过滤掉
				filter = true;
				for(int i=start;i<=end;i++){
					//如果是大写字母则计算 大写字母个数 并且表明当前reads不是alu或者simple repeat
					if(flag[i] == 0){
						count++;
					}
					//表明当前reads不是alu或者simple repeat
					if(flag[i] != 1)
						filter = false;
				}
				
				//如果没有过滤 则说明是其他repeat或者不全是 alu simple repeat
				if(!filter){
					//要么大写字母大于等于20  要么就全是其他ｒｅｐｅａｔ
					if(count>=maxLen || count == 0){
						bw.write(str);
						bw.newLine();
					}else{
						System.out.println("大写字母长度:"+count);
					}
				}else{
					System.out.println("I am alu or simple repeat reads!");
				}
				
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main20120417(){
		String path = "K:/bwa/z=50/01/";
		readRepeatPos("H:/bwa/chr22-repeats.txt");
		filterReads(path+"Chr22_w+.txt", path+"Chr22_w+_filter.txt");
		
//		readRepeatNeg("K:/bwa/chr22-repeats.txt");
//		filterReads(path+"Chr22_c+.txt", path+"Chr22_c+_filter.txt");
	}
	
	public static void main(String args[]){
		new Main20120417();
	}
}
