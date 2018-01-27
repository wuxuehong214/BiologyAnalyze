package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * reads 过滤  
 * 过滤掉reads中的repeat区
 * @author wuxuehong
 * 2011-12-21
 */
public class ReadsDeal {

	byte[] flag = new byte[60000000];
	
	/**
	 * mark the flag
	 * @param inputFile
	 * @throws IOException
	 */
	public void readRepeat(String inputFile)throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		String str = br.readLine();
		Scanner s = null;
		int start,end;
		while(str != null)
		{
			s = new Scanner(str);
			s.next();
			start = s.nextInt();
			end = s.nextInt();
			for(int i=start; i<=end; i++) flag[i] = 1;   //将repeat区打上标识  1
			str = br.readLine();
		}
		br.close();
	}
	
	/**
	 * 
	 * @param inputFile
	 * @param outFile
	 */
	public void readReadsAndOutput(String inputFile, String outFile)throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine(); //忽略第一行
		bw.write(str);
		bw.newLine();
		Scanner s = null;
		str = br.readLine();
		int start,end;
		boolean avi = false;  //初始化为false  即该read不符合条件
		while(str != null){
			s = new Scanner(str);
			avi = false;
			s.next();s.next();
			start = s.nextInt();
			end = s.nextInt();
			for(int i=start;i<=end;i++){
				if(flag[i]==0){  //即包含了非repeat区的基因
					avi = true;  //该read需要输出
					break;
				}
			}
			if(avi){  //read 中有非repeat基因  则输出
				bw.write(str);
				bw.newLine();
			}
			str = br.readLine();
		}
		br.close();
		bw.flush();
		bw.close();
	}
	
	public ReadsDeal() throws IOException{
		// TODO Auto-generated constructor stub
		for(int i=0;i<60000000;i++)flag[i] = 0;
		readRepeat("E:/研究生工作/personal data/persion new data/repeat.txt");
System.out.println("1");
		readReadsAndOutput("E:/研究生工作/personal data/persion new data/z=1/readsC2T_crick.txt", 
				"E:/研究生工作/personal data/persion new data/z=1/f_readsC2T_crick.txt");
		System.out.println("2");
		readReadsAndOutput("E:/研究生工作/personal data/persion new data/z=1/readsC2T_watson.txt", 
				"E:/研究生工作/personal data/persion new data/z=1/f_readsC2T_watson.txt");
		System.out.println("3");
		readReadsAndOutput("E:/研究生工作/personal data/persion new data/z=1/readsG2A_crick.txt", 
				"E:/研究生工作/personal data/persion new data/z=1/f_readsG2A_crick.txt");
		readReadsAndOutput("E:/研究生工作/personal data/persion new data/z=1/readsG2A_watson.txt", 
				"E:/研究生工作/personal data/persion new data/z=1/f_readsG2A_watson.txt");
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
			try {
				new ReadsDeal();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
