package HumanGenDeal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 * 将23条染色体的分析结果  融合到一个文件中
 * @author wuxuehong
 * 2011-12-27
 */
public class ResultFusion {

	List<ReadVo> reads = new ArrayList<ReadVo>();
	public ResultFusion() throws IOException {
		// TODO Auto-generated constructor stub
		String base = "D:/recover/研究生工作/personal_data/0106/06/FormatConvert/";
		reads.clear();
		for(int i=1;i<=22;i++){
//			System.out.println("C_Chr"+i+".txt");
			readMatchedReads(base+"C_Chr"+i+".txt");
		}
		readMatchedReads(base+"C_ChrX.txt");
//		System.out.println("watson_CT_ChrX.txt");
//		System.out.println("reads size:"+reads.size());
		writeToFile(base+"Fusion/crick_reads.txt");
		
//		reads.clear();
//		for(int i=1;i<=22;i++){
//			System.out.println("watson_GA_Chr:"+i);
//			readMatchedReads("watson_GA_Chr"+i+".txt");
//		}
//		readMatchedReads("watson_GA_ChrX.txt");
//		writeToFile("Fusion/watson_GA.txt");
//		
//		reads.clear();
//		for(int i=1;i<=22;i++){
//			System.out.println("crick_CT_Chr:"+i);
//			readMatchedReads("crick_CT_Chr"+i+".txt");
//		}
//		readMatchedReads("crick_CT_ChrX.txt");
//		writeToFile("Fusion/crick_CT.txt");
//		
//		reads.clear();
//		for(int i=1;i<=22;i++){
//			System.out.println("crick_GA_Chr:"+i);
//			readMatchedReads("crick_GA_Chr"+i+".txt");
//		}
//		readMatchedReads("crick_GA_ChrX.txt");
//		writeToFile("Fusion/crick_GA.txt");
	}
	
	//1-22 X, Y
//	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
//			159138663,146364022,141213431,135534747,135006516,133851895,
//			115169878,107349540,102531392,90354753,81195210,78077248,
//			59128983,63025520,48129895,51304566,155270560,59373566};
	
	@SuppressWarnings("unused")
	public void readMatchedReads(String inputFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		String str = br.readLine();
		str = br.readLine();
		Scanner s = null;
		String chr = null;
		String read,ref,seq,ciagr,reflen;
		float matchic;
		int start,end,len,match;
		while(str != null){
			s = new Scanner(str);
//			read = s.next();   //read ID
//			ref = s.next();   //ref ID 
//			chr = s.next(); //chr ID
//			start = s.nextInt(); //start position
//			end = s.nextInt(); //end position
//			len = s.nextInt(); //read length
//			reflen = s.next();  // ref length
			
			for(int i=0;i<7;i++) s.next();
			matchic = Float.parseFloat(s.next()); //
//			ciagr = s.next();
//			seq = str;
			
			ReadVo readvo = new ReadVo();
			reads.add(readvo);
//			readvo.setReadID(read);
//			readvo.setRefID(ref);
//			readvo.setStart(start);
//			readvo.setEnd(end);
//			readvo.setReadLength(len);
			readvo.setSeq(str);
//			readvo.setRefLength(Long.parseLong(reflen));
//			readvo.setCiagr(ciagr);
			readvo.setMathcs(matchic);
//			readvo.setChr(chr);
			
			str = br.readLine();
		}
		br.close();
	}
	
	
	public void writeToFile(String outFile) throws IOException{
//		Collections.sort(reads);
		System.out.println("reads size:"+reads.size());
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		bw.write("ReadID\tRefID\tChrID\tStarting position\tEnd position\tLength of read\tLength\tMatch/Reads\tCiagr\tMapping strand of ref");
		bw.newLine();
		int size = reads.size();
		ReadVo read;
		String seq;
		for(int i=0;i<size; i++){
			read = reads.get(i);
//			seq = read.getReadID();
//	System.out.println(i);
			bw.write(read.getSeq());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ResultFusion();
	}

}
