package HumanGenDeal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import mousedata.Main;
import util.FileStreamUtil;

public class RepeatDeal {
	private static final int WATSON = 0x1 ;
	private static final int CRICK = 0x2;
	/**
	 * for watson
	 * @param leftmost
	 * @return
	 */
	public long getStartPointerWatson(long leftmost, int firstLine, int eachLine){
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
	public long getStartPointerCrick(long leftmost, int firstLine, int eachLine, int offset){
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
	public void refReadsAndFilterRuelst(int style,int eachLine, String ref, String sam, String output) throws IOException{
		 RandomAccessFile  rf  = null;
		 rf = FileStreamUtil.getRAF(ref);
		 int firstLine = rf.readLine().length();
		 int offset = rf.readLine().length();
		 rf.seek(0);
		 rf.seek(getStartPointerWatson(135859926,firstLine,50));
		 for(int i=0;i<50;){
			 int a = rf.read();
			 if (a=='\n'||a=='\r'){
				 continue;
			 }
			 i++;
			 System.out.print((char)a);
		 }
		 System.out.println();
		 
		 if(1==1)return;
		 
		 BufferedReader br = FileStreamUtil.getBufferedReader(sam);
		 BufferedWriter bw = FileStreamUtil.getBufferedWriter(output);
		 
		 String str = br.readLine(); //ignorance the first line
		 bw.write(str);
		 bw.newLine();
		 
		 Scanner s = null;
		 str = br.readLine();
		 long leftmost = 0;
		 long length = 0;
		 int c = -1;
		 boolean avi = false;
		 while(str != null){
			 s = new Scanner(str);
			 s.next();s.next();s.next();
			 leftmost = Long.parseLong(s.next()); //获取匹配ref中的位置
			 for(int i=0;i<5;i++)s.next();
			 length = s.next().length();  //获取seq长度
			 
			 switch(style){
			 case WATSON:  
				 rf.seek(getStartPointerWatson(leftmost,firstLine,eachLine));  //获取在ref文件中的位置
				 break;
			 case CRICK:
				 rf.seek(getStartPointerCrick(leftmost,firstLine,eachLine,offset));  //获取在ref文件中的位置
				 break;
			 }
			 c = -1;
			 avi = false;
			 for(int i=0; i<length; ){
				 c = rf.read();
				 if (c=='\n'||c=='\r'){
					 continue;
				 }
				 if(c>='A'&&c<='Z'&&c!='N'){
					 avi = true;
					 break;
				 }
				 i++;
			 }
			 if(avi){  //如果匹配的seq  strand有大写字母则输出
				 bw.write(str);
				 bw.newLine();
			 }
			str = br.readLine();
		 }
		 rf.close();
		 br.close();
		 bw.flush();
		 bw.close();
	}
	
	
	public RepeatDeal(String args[]) throws IOException{
		refReadsAndFilterRuelst(WATSON,50,"d:/recover/研究生工作/personal_data/repeatchr1-22/GeneNoRepeat/watson_Chr2.txt","","");
		
		
//		if(args.length != 2){
//			System.out.println("Bad paramater!");
//			return;
//		}
//		String watsonDir = args[0];
//		String crickDir = args[1];
//		
//		for(int i=1;i<=22;i++){
//			
//			System.out.println("reading........."+i);
//			//WATSON
//			refReadsAndFilterRuelst(WATSON,50, watsonDir+"/hg19_Chr"+i+".txt", 
//					"watson_CT_Chr"+i+".txt", 
//					"RepeatFilter/watson_CT_Chr"+i+".txt");
//			refReadsAndFilterRuelst(WATSON,50, watsonDir+"/hg19_Chr"+i+".txt", 
//					"watson_GA_Chr"+i+".txt", 
//					"RepeatFilter/watson_GA_Chr"+i+".txt");
//		
//			refReadsAndFilterRuelst(CRICK,50, crickDir+"/hg19_Chr"+i+".txt", 
//					"crick_CT_Chr"+i+".txt", 
//					"RepeatFilter/crick_CT_Chr"+i+".txt");
//			refReadsAndFilterRuelst(CRICK,50, crickDir+"/hg19_Chr"+i+".txt", 
//					"crick_GA_Chr"+i+".txt", 
//					"RepeatFilter/crick_GA_Chr"+i+".txt");
//		}
//		
//		refReadsAndFilterRuelst(WATSON,50, watsonDir+"/hg19_ChrX.txt", 
//				"watson_CT_ChrX.txt", 
//				"RepeatFilter/watson_CT_ChrX.txt");
//		refReadsAndFilterRuelst(WATSON,50, watsonDir+"/hg19_ChrX.txt", 
//				"watson_GA_ChrX.txt", 
//				"RepeatFilter/watson_GA_ChrX.txt");
//	
//		refReadsAndFilterRuelst(CRICK,50, crickDir+"/hg19_ChrX.txt", 
//				"crick_CT_ChrX.txt", 
//				"RepeatFilter/crick_CT_ChrX.txt");
//		refReadsAndFilterRuelst(CRICK,50, crickDir+"/hg19_ChrX.txt", 
//				"crick_GA_ChrX.txt", 
//				"RepeatFilter/crick_GA_ChrX.txt");
		
	}
	public static void main(String args[]) throws IOException{
		new RepeatDeal(args);
	}

}
