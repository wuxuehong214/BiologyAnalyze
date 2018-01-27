package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * 将类型是L1HS的repeats 过滤
 * @author wuxuehong
 * 2012-4-25
 */
public class Main20120425 {
	
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
			String name;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				start = s.nextInt();
				end = s.nextInt();
				for(int i=0;i<2;i++)s.next();
				name = s.next();
				//如果是L1HS类型的repeats 去除
				if(name.equals("L1HS")){
		System.out.println(start+"\t\t"+end);
					for(int i=start;i<=end;i++){
						flag[i] = 1;
					}
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
			String name;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				startTemp = s.nextInt();
				endTemp = s.nextInt();
				start = length-endTemp+1;
				end = length-startTemp+1;
				for(int i=0;i<2;i++)s.next();
				name = s.next();
				//如果是L1HS类型的repeats 去除
				if(name.equals("L1HS")){
					for(int i=start;i<=end;i++){
						flag[i] = 1;
					}
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void filterReads(String filename, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Scanner s = null;
			int start, end;
			int maxLen = 20;
			int count = 0;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
				count = 0;
				for(int i=start;i<=end;i++){
					if(flag[i] == 0)count++; //如果是大写字母  则个数加1
				}
				if(count >= maxLen){
					bw.write(str);
					bw.newLine();
				}else{
					System.out.println("大写字母长度:"+count);
					System.out.println(start+"***"+end);
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main20120425(){
		String path = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/";
		String outPath = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/";
		readRepeatPos("K:/bwa/chr22-repeats.txt");
		filterReads(path+"Chr22_w+_filter-line.txt", outPath+"Chr22_w+_filter-line.txt");
		
//		readRepeatNeg("K:/bwa/chr22-repeats.txt");
//		filterReads(path+"Chr22_c+_filter.txt", outPath+"Chr22_c+_filter.txt");
	}
	
	
	
	public static void main(String args[]){
		new Main20120425();
	}
}
