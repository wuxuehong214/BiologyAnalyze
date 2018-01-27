package 数据处理以及比对;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.FileStreamUtil;

public class ReferenceDeal {
	
	/**
	 * A-T C-G互补  将C换成T
	 * @param ch
	 * @return
	 */
	public char getChar(char ch){
		if(ch=='A') return 'T';
		if(ch=='T') return 'A';
		if(ch=='G') return 'T';
		if(ch=='C') return 'G';
		return ch;
	}
	
	/**
	 * 逆向读取，并取对应的值，A-T，G-C互补      将C转换成T
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
	 * Crick 链转换
	 * @param inputFile
	 * @param outFile
	 */
	public void Origin2Crick(String inputFile,String outFile){
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
	/**
	 * watson 链转换
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void origin2Watson(String inputFile,String outFile)throws IOException{
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		String str = br.readLine();
		while(str != null){
			str = str.toUpperCase();  //大写
			str = str.replaceAll("C", "T");
			bw.write(str);
			bw.newLine();
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
			ReferenceDeal rd = new ReferenceDeal();
	}
	
	public ReferenceDeal(){
		//1-22
		String inputFile,watsonFile,crickFile;
		for(int i=1;i<=22;i++){
			inputFile = "E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt";
			watsonFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/watson_Chr"+i+".txt";
			crickFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/crick_Chr"+i+".txt"; 
			
			try{
				System.out.println(crickFile+"\t is beginning....");
				Origin2Crick(inputFile, crickFile);
				System.out.println(crickFile+"\tfinished!");
				System.out.println(watsonFile+"\t is beginning....");
				origin2Watson(inputFile, watsonFile);
				System.out.println(watsonFile+"\tfinished!");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			inputFile = "E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrX.txt";
			watsonFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/watson_ChrX.txt";
			crickFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/crick_ChrX.txt";
			System.out.println(crickFile+"\t is beginning....");
			Origin2Crick(inputFile, crickFile);
			System.out.println(crickFile+"\tfinished!");
			System.out.println(watsonFile+"\t is beginning....");
			origin2Watson(inputFile, watsonFile);
			System.out.println(watsonFile+"\tfinished!");
			
			inputFile = "E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrY.txt";
			watsonFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/watson_ChrY.txt";
			crickFile = "E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/crick_ChrY.txt";
			System.out.println(crickFile+"\t is beginning....");
			Origin2Crick(inputFile, crickFile);
			System.out.println(crickFile+"\tfinished!");
			System.out.println(watsonFile+"\t is beginning....");
			origin2Watson(inputFile, watsonFile);
			System.out.println(watsonFile+"\tfinished!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
