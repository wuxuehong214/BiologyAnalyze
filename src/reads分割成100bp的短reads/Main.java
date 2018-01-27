package reads分割成100bp的短reads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * reads分割
 * @author wuxuehong
 * 2011-11-25
 */
public class Main {
	
	private int eachCount = 50;
	
	/**
	 * 
	 * @param inputFile
	 * @param outFile
	 * @throws IOException 
	 */
	public void readAndOutput(String inputFile, String outFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		Scanner s = null;
		String readID = null;   //read id
		String readInfo = null ;  //
		StringBuffer sb = new StringBuffer(""); //存放完整的reads sequence
		while(str != null){
			if(str.startsWith(">")){
				if(sb.toString().length()!=0){
					List<String> subs = getSubString(100, sb.toString()); //切割序列  长度100bp
					for(int i=0;i<subs.size();i++){
						bw.write(readID+"-"+(i+1)+""+readInfo);
						bw.newLine();
						bw.write(subs.get(i));
						bw.newLine();
					}
				}
				s = new Scanner(str);
				readID = s.next(); //
				readInfo = str.substring(readID.length());
				sb = new StringBuffer("");
			}else{
				sb.append(str);
			}
			str = br.readLine();
		}
		//最后一个
		if(sb.toString().length()!=0){
			List<String> subs = getSubString(100, sb.toString());
			for(int i=0;i<subs.size();i++){
				bw.write(readID+"-"+(i+1)+""+readInfo);
				bw.newLine();
				bw.write(subs.get(i));
				bw.newLine();
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}
	
	/**
	 * 将字符串切割
	 * 切割后每个字符串之间都由overlap
	 * @param length  切割后每个字符串最大长度
	 * @param seq  字符串
	 * @return
	 */
	public List<String> getSubString(int length,String seq){
		List<String> subs = new ArrayList<String>();
		Random r = new Random();
		int start = 0;
		int ran = 0;
		String temp = "";
		while(seq.substring(start).length() >length){
			temp = seq.substring(start, start+length);
			subs.add(temp); 
			ran = r.nextInt(length);
			if(ran==0) ran++;
			start = start+ran;
		}
		subs.add(seq.substring(start));
		return subs;
	}
	
	public Main() throws IOException{
		readAndOutput("E:/研究生工作/mouse data/rfsc-1.GAC.454Reads_G2A.fa", "E:/研究生工作/mouse data/mouse分段/rfsc-1.GAC.454Reads_G2A.fa");
	}
	
	public static void main(String args[]) throws IOException{
		new Main();
	}

}
