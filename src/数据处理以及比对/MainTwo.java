package 数据处理以及比对;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 *  处理匹配的并且已经按照mismatch/match排序的reads信息
 *  
 *  给出满足以下条件的reads结果
   1.read>200bp
   2 non-mapped portion<50bp 
 * @author wuxuehong
 * 2011-11-8
 */
public class MainTwo {

	public MainTwo() throws IOException {
		// TODO Auto-generated constructor stub
		readAndOutput("E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/aln_GA_watson.txt", 
				"E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/进一步处理结果/aln_GA_watson.txt");
		
		readAndOutput("E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/aln_CT_watson.txt", 
		"E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/进一步处理结果/aln_CT_watson.txt");
		
		readAndOutput("E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/aln_CT_crick.txt", 
		"E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/进一步处理结果/aln_CT_crick.txt");
		
		readAndOutput("E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/aln_GA_crick.txt", 
		"E:/研究生工作/dataDeal/20111031/输出matched并且排序的read/进一步处理结果/aln_GA_crick.txt");
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
	
	public void readAndOutput(String inputFile, String outputFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outputFile);
		String str = br.readLine();  //读取第一行 列项
		bw.write(str);
		bw.newLine();
		str = br.readLine();
		Scanner s = null;
		String seq,ciagr;
		int match = 0, len = 0;
		while(str != null){
			s = new Scanner(str);
			for(int i=0;i<7;i++) s.next();
			ciagr = s.next();
			seq = s.next();
			len = seq.length();          //获取read length
			match = parseCiagr(ciagr);   //解析ciagr  获取match的个数
			if(len>200 && len-match<50){
				bw.write(str);
				bw.newLine();
			}
			str = br.readLine();
		}
		br.close();
		bw.flush();
		bw.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			MainTwo mt = new MainTwo();
//			System.out.println(mt.parseCiagr("29M1I11M1I15M1I5M1I38M1D12M1D50M1I16M1I11M1D18M31S"));
//			System.out.println("TATATATATATATATATATAATATATCTATATATCTATATATATATATCTATATATATTATATAATATATATATAATATCTATATATATATATATATATATAATATATCTATATTATATAATATATATATATATATAATATATATATATATATAATATATATATAATATATATATATATATAATATCTCTATATTTATATATATATATATAATATATATATATATATATAATATATATATAT".length());
	}

}
