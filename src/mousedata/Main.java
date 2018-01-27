package mousedata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 在ref里面有大写字母，也有小写字母，小写字母即是repeat区。
 * 他们现在还需要我们帮忙把reads匹配的结果再过滤一下，
 * 将全部都是repeat区的reads过滤掉。方法是，
 * 检查每条reads在ref里对应的是否存在小写，
 * 只要有大写字母即保留。z=1和z=100的结果都处理下。
 * @author wuxuehong
 * 2011-11-15
 */
public class Main {
	
	private static final int WATSON = 0x1 ;
	private static final int CRICK = 0x2;
	
	/**
	 * for watson
	 * @param leftmost
	 * @return
	 */
	public long getStartPointerWatson(long leftmost){
		long start = 0;
		leftmost--;
		start += 81; //第一行 数据过滤
		long hang = leftmost/50;   //50是每行的字符个数
		long mod = leftmost%50; //最后一行字符个数
		start += 52*hang;
		start = start+mod; 
		return start;
	}
	
	/**
	 * for crick
	 * @param leftmost
	 * @return
	 */
	public long getStartPointerCrick(long leftmost){
		long start = 0;
		leftmost--;
		start += 81; //第一行 数据过滤
		start += 18;  //第一行不足50字符串
		leftmost -= 16;
		long hang = leftmost/50;   //50是每行的字符个数
		long mod = leftmost%50; //最后一行字符个数
		start += 52*hang;
		start = start+mod; 
		return start;
	}
	
	/**
	 * 
	 * @param ref
	 * @param sam
	 * @param output
	 * @throws IOException
	 */
	public void refReadsAndFilterRuelst(int style, String ref, String sam, String output) throws IOException{
		 RandomAccessFile  rf  = null;
		 rf = FileStreamUtil.getRAF(ref);
//		 rf.seek(getStartPointerWatson(51));
//		 for(int i=0;i<10;){
//			 int a = rf.read();
//			 if (a=='\n'||a=='\r'){
//				 continue;
//			 }
//			 i++;
//			 System.out.print((char)a);
//		 }
//		 System.out.println();
//		 
//		 System.out.println("CTATTaaacgagttcaacataaaaagacattt".length());
//		 if(1==1)return;
		 
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
//	System.out.println(str);
			 s = new Scanner(str);
			 s.next();s.next();
			 leftmost = Long.parseLong(s.next()); //获取匹配ref中的位置
			 for(int i=0;i<5;i++)s.next();
			 length = s.next().length();  //获取seq长度
			 
			 switch(style){
			 case WATSON:  
				 rf.seek(getStartPointerWatson(leftmost));  //获取在ref文件中的位置
				 break;
			 case CRICK:
				 rf.seek(getStartPointerCrick(leftmost));  //获取在ref文件中的位置
				 break;
			 }
//			 rf.seek(getStartPointerWatson(leftmost));  //获取在ref文件中的位置
//			 rf.seek(getStartPointerCrick(leftmost)); //crick
			 c = -1;
			 avi = false;
			 for(int i=0; i<length; ){
				 c = rf.read();
				 if (c=='\n'||c=='\r'){
					 continue;
				 }
				 if(c>='A'&&c<='Z'){
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
	
	
	/**
	 * 字符串逆向
	 * @param str
	 * @return
	 */
	public String convert(String str){
		char[] c = str.toCharArray();
		int len = c.length;
		int first = 0;
		int last = len-1;
		char temp ;
		while(first<last){
			temp = c[first];
			c[first] = c[last];
			c[last] = temp;
			first++;
			last--;
		}
		return String.valueOf(c);
	}
	/**
	 * 将文件中内容反序输出
	 * @param inputFile
	 * @param outFile
	 */
	public void reserveFile(String inputFile,String outFile){
		 RandomAccessFile  rf  = null;
		 BufferedWriter bw = null; 
	    try {
	      rf = FileStreamUtil.getRAF(inputFile);
	      bw = FileStreamUtil.getBufferedWriter(outFile);
	      long len = rf.length();
	      long start = rf.getFilePointer();
	      System.out.println(start);
	      rf.seek(0);
	      bw.write(rf.readLine());
	      bw.newLine();
	      long nextend = start + len - 1;
	      String line;
	      rf.seek(nextend);
	      int c = -1;
	      while (nextend > start) {
	        c = rf.read();
	        if (c == '\n' || c == '\r') {
	          line = rf.readLine();
	        if(line == null)
	        {
	        	nextend -= 2;
	        	rf.seek(nextend);
	        	continue;
	        }
	          bw.write(convert(line)); 
	          bw.newLine();
	          nextend--;
	        }
	        nextend--;
	        rf.seek(nextend);
	        if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
	          System.out.println(rf.readLine());
	        }
	      }
	      bw.flush();
	      bw.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        if (rf != null)
	          rf.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	}
	
	
	//watson ----- rchr20_new.fa
	//crick ------ rchr20_new_reserve.fa
	
	public Main() throws IOException{
		
		for(int i=1;i<=1;i++){
			reserveFile("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt",
					"E:/研究生工作/personal data/repeat chr1-22/GeneWithRepeat_reserve/hg19_Chr"+i+".txt");
		}
//		reserveFile("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrX.txt",
//				"E:/研究生工作/personal data/repeat chr1-22/GeneWithRepeat/hg19_ChrX.txt");
//		reserveFile("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrY.txt",
//				"E:/研究生工作/personal data/repeat chr1-22/GeneWithRepeat/hg19_ChrY.txt");
//		refReadsAndFilterRuelst(WATSON, "E:/研究生工作/personal data/hg19_Chr22.txt", 
//				"E:/研究生工作/personal data/persion new data/z=1/readsC2T_watson.txt", 
//		"E:/研究生工作/personal data/persion new data/z=1/readsC2T_watson");
//		refReadsAndFilterRuelst(WATSON,"E:/研究生工作/personal data/hg19_Chr22.txt", 
//				"E:/研究生工作/personal data/persion new data/z=1/readsG2A_watson.txt", 
//		"E:/研究生工作/personal data/persion new data/z=1/readsG2A_watson");
//		
//		refReadsAndFilterRuelst(CRICK, "E:/研究生工作/personal data/hg19_Chr22_reserve.txt", 
//				"E:/研究生工作/personal data/persion new data/z=1/readsC2T_crick.txt", 
//		"E:/研究生工作/personal data/persion new data/z=1/readsC2T_crick");
//		refReadsAndFilterRuelst(CRICK,"E:/研究生工作/personal data/hg19_Chr22_reserve.txt", 
//				"E:/研究生工作/personal data/persion new data/z=1/readsG2A_crick.txt", 
//		"E:/研究生工作/personal data/persion new data/z=1/readsG2A_crick");
		
//		reserveFile("E:/研究生工作/personal data/hg19_Chr22.txt", "E:/研究生工作/personal data/hg19_Chr22_reserve.txt");
		//Watson
//		for(int i=50;i<=50;i++){
//		refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new.fa", 
//				"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Watson_aln.txt", 
//				"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Watson_aln");
//		refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new.fa", 
//				"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Watson_aln.txt", 
//				"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Watson_aln");
//		
//		}
		//or not and!!!
		//Crick
		
//			for(int i=50;i<=50;i++){
//		System.out.println("**********"+i);
//				refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new_reserve.fa", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_C2T_Crick_aln.txt", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_C2T_Crick_aln");
//				refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new_reserve.fa", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_G2A_Crick_aln.txt", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_G2A_Crick_aln");
//				refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new_reserve.fa", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Crick_aln.txt", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Crick_aln");
//				refReadsAndFilterRuelst("E:/研究生工作/mouse data/rchr20_new_reserve.fa", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Crick_aln.txt", 
//						"E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Crick_aln");
//			}
//		reserveFile("E:\\研究生工作\\mouse data\\rchr20_new.fa", "E:\\研究生工作\\mouse data\\rchr20_new_reserve.fa");
	}
	public static void main(String args[]) throws IOException{
		new Main();
	}

}
