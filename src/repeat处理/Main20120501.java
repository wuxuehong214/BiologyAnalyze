package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main20120501 {

	byte[] flag = new byte[51304567]; 
	
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
				if(name.equals("L1PA3") || name.equals("L1M4c")){
					for(int i=start;i<=end;i++){
						flag[i] = 1;
					}
				}
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * filter L1PA4-8 
	 * @param inputFile
	 * @param outFile
	 */
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Scanner s = null;
			int start, end;
			int count = 0;
			int maxLen = 20;
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
	
	public Main20120501(){
		readRepeatPos("K:/bwa/chr22-repeats.txt");
		String inputPath = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/";
		String outPath = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/L1PA3L1M4c_filter/";
		readAndOutput(inputPath+"Chr22_w+_L1HS_dups_L1PA_filter.txt", outPath+"Chr22_w+_L1HS_dups_L1PA(3-8)_filter.txt");
	}
	
	public static void main(String args[]){
		new Main20120501();
	}
	
}
