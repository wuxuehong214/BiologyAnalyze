package WCNegative;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.FileStreamUtil;

public class Main {
	
	
	/**
	 * A-T C-G互补 
	 * @param ch
	 * @return
	 */
	public char getChar(char ch){
		if(ch=='A') return 'T';
		if(ch=='T') return 'A';
		if(ch=='G') return 'C';
		if(ch=='C') return 'G';
		return ch;
	}
	/**
	 * 逆向读取，并取对应的值，A-T，G-C互补   
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
			c[first]=getChar(c[first]);
			c[last]=getChar(c[last]);
			temp = c[first];
			c[first] = c[last];
			c[last] = temp;
			first++;
			last--;
		}
		return String.valueOf(c);
	}
	/**
	 * W+链的反链互补链则是W-
	 * W+--------逆序并且互补------------>W-
	 * 逆序:整条ref逆序读取
	 * 互补:A-T C-G互补
	 * 
	 * C+链的反链互补链则是C-
	 * C+--------逆序并且互补------------>C-
	 * 逆序:整条ref逆序读取
	 * 互补:A-T C-G互补
	 * @param inputFile
	 * @param outFile
	 */
	public void W2WorC2C(String inputFile,String outFile){
		 RandomAccessFile  rf  = null;
		 BufferedWriter bw = null; 
	    try {
	      //随机文件读取对象
	      rf = FileStreamUtil.getRAF(inputFile);
	      //文件输出
	      bw = FileStreamUtil.getBufferedWriter(outFile);
	      //文件长度
	      long len = rf.length();
	      //文件起始位置
	      long start = rf.getFilePointer();
	      //从文件头开始读取
	      rf.seek(0);
	      //读取第一行  忽略第一行
	      bw.write(rf.readLine());
	      bw.newLine();
	      //文件结束位置
	      long nextend = start + len - 1;
	      String line;
	      //指针指向文件结束位置
	      rf.seek(nextend);
	      int c = -1;
	      while (nextend > start) {
	    	rf.seek(nextend);
	        c = rf.read();
	        if (c == '\n' || c == '\r') {   //读取到回车或者换行的时候 将正行内容读取出来
	          line = rf.readLine();
			  if(line==null){
				  System.out.println(inputFile+"\t\t"+line+"\t\t"+rf.getFilePointer());
				  nextend-=2;
			      continue;
			  }
			  //将一行内容反序 并输出到文件
	          bw.write(convert(line)); 
	          bw.newLine();
	          nextend-=50;  //指针位置回退50(一行字符串的长度)
	        }
	        nextend--;
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
	
	public Main(){
		new Thread1().start();
		new Thread2().start();
	}
	
	class Thread1 extends Thread{
		public void run() {
			String basedir = "D:/recover/研究生工作/personal_data/repeatchr1-22/";
			for(int i=12;i>=1;i--){
				System.out.println("current:"+i);
				W2WorC2C(basedir+"WC+/crick_Chr"+i+".txt", basedir+"WC-/crick_Chr"+i+".txt");
						}
				System.out.println("current:X");
				W2WorC2C(basedir+"WC+/crick_ChrX.txt", basedir+"WC-/crick_ChrX.txt");
		}
	}
	
	class Thread2 extends Thread{
		public void run() {
			String basedir = "D:/recover/研究生工作/personal_data/repeatchr1-22/";
			for(int i=11;i>=1;i--){
				System.out.println("current:"+i);
				W2WorC2C(basedir+"WC+/watson_Chr"+i+".txt", basedir+"WC-/watson_Chr"+i+".txt");
						}
				System.out.println("current:X");
				W2WorC2C(basedir+"WC+/watson_ChrX.txt", basedir+"WC-/watson_ChrX.txt");
		}
	}
	
	public static void main(String args[]){
		new Main();
	}

}
