package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import util.FileStreamUtil;

public class ProblemOne_2 {
	
	
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
	
	//convert to watson unconverted
	public void convertToW(String inputFile,String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			bw.write(str); //第一行 
			bw.newLine();
			str = br.readLine();
			while(str != null){
				bw.write(str.toUpperCase());
				bw.newLine();
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//convert to crick unconverted
	public void convertToC(String inputFile, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			bw.write(str); //第一行 
			bw.newLine();
			str = br.readLine();
			while(str != null){
				char[] c = str.toUpperCase().toCharArray();
				for(int i=0;i<c.length;i++){
					c[i] = getChar(c[i]);
				}
				bw.write(String.valueOf(c));
				bw.newLine();
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ProblemOne_2(){
		String base = "D:/recover/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/";
		String rbase = "D:/recover/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat_reserve/";
		String des = "D:/recover/研究生工作/personal_data/repeatchr1-22/WC/";
		
		for(int i=1;i<=22;i++){
	System.out.println("current:"+i);
//			convertToW(base+"hg19_Chr"+i+".txt", des+"W_hg19_Chr"+i+".txt");
			convertToC(rbase+"hg19_Chr"+i+".txt", des+"C_hg19_Chr"+i+".txt");
		}
		
		System.out.println("current:X");
//		convertToW(base+"hg19_ChrX.txt", des+"W_hg19_ChrX.txt");
		convertToC(rbase+"hg19_ChrX.txt", des+"C_hg19_ChrX.txt");
	}
	
	public static void main(String args[]){
		new ProblemOne_2();
	}

}

