package 数据处理以及比对;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import util.FileStreamUtil;

public class Main {
	
	/**
	 * 计算染色体的长度
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public long getLengthOfChr(String filename) throws IOException{
		long count = 0;
		BufferedReader br = FileStreamUtil.getBufferedReader(filename);
		String str = br.readLine();
		str = br.readLine();
		while(str != null){
			count += str.length();
			str = br.readLine();
		}
		br.close();
		return count;
	}
	
	/**
	 * 读取sam文件 并输出已经匹配read
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void readAndOutput(String inputFile,String outFile)throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		str = br.readLine();
		Scanner s = null;
		String read,ref,seq;
		int start,end,len;
		bw.write("ReadID\tRefID\tStarting position of ref in the alignment\tEnd position of ref in the alignment\tLength of read\tLength of ref\nMapping strand of ref");
		bw.newLine();
		while(str !=null ){
			s = new Scanner(str);
			read = s.next();
			s.next();
			ref = s.next();
			if(!"*".equals(ref)){
				start = s.nextInt();
				for(int i=0;i<5;i++)s.next();
				seq = s.next();
				len = seq.length();
				end = start+len-1;
				bw.write(read+"\t"+ref+"\t"+start+"\t"+end+"\t"+len+"\t"+51304566);
				bw.newLine();
				bw.write(seq);
				bw.newLine();
			}
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}
	
	/**
	 * 解析CIAGR 字符串  解析出 匹配的个数
	 * @param ciagr
	 * @return
	 */
	public int parseCiagr(String ciagr){
		int total = 0;
		int temp = 0;
		char[]  c = ciagr.toCharArray();
		int len = c.length;
		for(int i=0;i<len; i++){
			if(c[i]=='M'){
				total += temp;
			}
			if(c[i]>='0'&&c[i]<='9'){
				temp = temp*10+c[i]-'0';
			}else{
				temp = 0;
			}
		}
		return total;
	}
	
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	public void readMatchedReads(String inputFile,String outFile,int index) throws IOException{
		List<ReadVo> reads = new LinkedList<ReadVo>();
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		String str = br.readLine();
		str = br.readLine();
		Scanner s = null;
		String read,ref,seq,ciagr;
		int start,end,len,match;
		while(str != null){
			s = new Scanner(str);
			read = s.next();   //read ID
			s.next();         
			ref = s.next();   //ref ID 
			if(!"*".equals(ref)){
				ReadVo readvo = new ReadVo();
				reads.add(readvo);
				
				start = s.nextInt(); //start position
				s.next();  //ignore
				ciagr = s.next(); //CIAGR
				match = parseCiagr(ciagr); // number of match
				for(int i=0;i<3;i++)s.next();
				seq = s.next();
				len = seq.length();
				end = start+len-1;
				
				readvo.setReadID(read);
				readvo.setRefID(ref);
				readvo.setStart(start);
				readvo.setEnd(end);
				readvo.setReadLength(len);
				readvo.setSeq(seq);
				readvo.setRefLength(locat[index-1]);
				readvo.setCiagr(ciagr);
				readvo.setMathcs((float)(match)/(float)len);
			}
			str = br.readLine();
		}
		
		Collections.sort(reads);
		
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		bw.write("ReadID\tRefID\tStarting position of ref in the alignment\tEnd position of ref in the alignment\tLength of read\tLength of ref\tMatch/Reads\tCiagr\tMapping strand of ref");
		bw.newLine();
		int size = reads.size();
		for(int i=0;i<size; i++){
			bw.write(reads.get(i).toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	/**
	 * chr22 = 51304566
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException{
		Main main = new Main();
////		System.out.println(main.getLengthOfChr("E:\\研究生工作\\dataDeal\\20111031\\hchr22.fa"));
//		for(int i=50;i<=50;i++){
//System.out.println(i+"****");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_FC2T_C.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_C2T_Crick_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_FC2T_W.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_C2T_Watson_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_FG2A_C.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_G2A_Crick_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_FG2A_W.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-1_G2A_Watson_aln.txt");
//			
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_SC2T_C.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Crick_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_SC2T_W.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_C2T_Watson_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_SG2A_C.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Crick_aln.txt");
//			main.readMatchedReads("E:/研究生工作/mouse data/z=1-50/z"+i+"/f_SG2A_W.sam", "E:/研究生工作/mouse data/z=1-50/z"+i+"/f_rfsc-2_G2A_Watson_aln.txt");
//		}
		
		for(int i=1;i<=22;i++){
			main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/Chr"+i+"_crick_CT.fa",
					"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/Chr"+i+"_crick_CT.txt",i);
			main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/Chr"+i+"_crick_GA.fa",
					"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/Chr"+i+"_crick_GA.txt",i);
			main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/Chr"+i+"_watson_CT.fa",
					"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/Chr"+i+"_watson_CT.txt",i);
			main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/Chr"+i+"_watson_GA.fa",
					"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/Chr"+i+"_watson_GA.txt",i);
		}
		
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrX_crick_CT.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrX_crick_CT.txt",23);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrX_crick_GA.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrX_crick_GA.txt",23);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrX_watson_CT.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrX_watson_CT.txt",23);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrX_watson_GA.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrX_watson_GA.txt",23);
		
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrY_crick_CT.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrY_crick_CT.txt",24);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrY_crick_GA.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrY_crick_GA.txt",24);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrY_watson_CT.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrY_watson_CT.txt",24);
		main.readMatchedReads("E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/ChrY_watson_GA.fa",
				"E:/研究生工作/personal data/repeat chr1-22/GeneNoRepeat/result/f_result/ChrY_watson_GA.txt",24);
	}

}
