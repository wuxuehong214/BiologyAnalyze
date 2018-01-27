package 根据给定的起始位置获取ref中对应的seq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import mousedata.Main;
import util.FileStreamUtil;

public class RepeatDeal {
	public static final int WATSON = 0x1 ;
	public static final int CRICK = 0x2;
	
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	/**
	 * for watson
	 * @param leftmost
	 * @return
	 */
	public static long getStartPointerWatson(long leftmost, int firstLine, int eachLine){
		long start = 0;
		leftmost--;
		start += (firstLine+2); //第一行 数据过滤
		long hang = leftmost/eachLine;   //50是每行的字符个数
		long mod = leftmost%eachLine; //最后一行字符个数
		start += (eachLine+2)*hang;
		start = start+mod; 
		return start;
	}
	
	/**
	 * for crick
	 * @param leftmost
	 * @return
	 */
	public static long getStartPointerCrick(long leftmost, int firstLine, int eachLine, int offset){
		long start = 0;
		leftmost--;
		start += (firstLine+2); //第一行 数据过滤
		start += (offset+2);  //第一行不足50字符串
		leftmost -= offset;
		long hang = leftmost/eachLine;   //50是每行的字符个数
		long mod = leftmost%eachLine; //最后一行字符个数
		start += (eachLine+2)*hang;
		start = start+mod; 
		return start;
	}
	
	/**
	 * 
	 * @param style  WATSON OR CRICK deal?
	 * @param firstLine 第一行字符串的长度
	 * @param eachLine  每行字符串的长度
	 * @param offset  CRICK时 第一行不足50个字符的字符长度
	 * @param ref  reference
	 * @param sam   待分析文件
	 * @param output   输出文件
	 * @throws IOException 
	 */
	public static String refReadsAndFilterRuelst(int style,int eachLine, String ref) throws IOException{
		 RandomAccessFile  rf  = null;
		 rf = FileStreamUtil.getRAF(ref);
		 int firstLine = rf.readLine().length();
		 int offset = rf.readLine().length();
		 rf.seek(0);
		 
		 if(style == WATSON)
			 rf.seek(getStartPointerWatson(10776501,firstLine,eachLine));
		 else if(style == CRICK)
			 rf.seek(getStartPointerCrick(37353346, firstLine, eachLine, offset));
		 
		 StringBuffer sb = new StringBuffer();
		 for(int i=0;i<50;){
			 int a = rf.read();
			 if (a=='\n'||a=='\r'){
				 continue;
			 }
			 i++;
			 sb.append((char)a);
		 }
		 return sb.toString();
	}
	
	
	public RepeatDeal(String args[]) throws IOException{
		refReadsAndFilterRuelst(CRICK,50,"d:/recover/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat_reserve/hg19_Chr21.txt");
	}
	
	public static void main(String args[]) throws IOException{
		new RepeatDeal(args);
	}

	
	//10776501 watson gacagaatggaattgaacaaagtggagtgtagtggagtggagcagagtgg 
	
	//10776550 crick  ggtgagacgaggtgaggtgatgtgaggtgaaacaagttaaggtaagacag
	//                AGAAATTTTTTGGGTTGTATTTTTTAATTGTTAAAGTTGTTTAATTATGA
}
