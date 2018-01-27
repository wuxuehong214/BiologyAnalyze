package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 从所有repeat数据中 提取  simple_repeat microstelate Alu repeat区
 * @author wuxuehong
 * 2011-12-20
 */
public class Main {

	public Main() throws IOException {
		// TODO Auto-generated constructor stub
		readAndOutput("E:/研究生工作/personal data/persion new data/chr22_all", 
				"E:/研究生工作/personal data/persion new data/chr22_micsate",
				"E:/研究生工作/personal data/persion new data/chr22_simple repeat", 
				"E:/研究生工作/personal data/persion new data/repeat.txt");
	}
	
	/**
	 * all
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void readAndOutput(String inputFileAll,String inputMirc, String inputSim, String outFile)throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFileAll);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		str = br.readLine();
		Scanner s = null;
		String chr,start,end,fam;
		//all
		while(str != null){
			s = new Scanner(str);
			for(int i=0;i<5;i++) s.next();
			chr = s.next();
			start = s.next();
			end = s.next();
			for(int i=0;i<4;i++)s.next();
			fam = s.next();
			if("Alu".equals(fam)||"MIR".equals(fam)||"Simple_repeat".equals(fam)){ //只考虑三种repeat类型
				bw.write(chr+"\t"+start+"\t"+end+"\t"+fam);
				bw.newLine();
			}
			str = br.readLine();
		}
		br.close();
		
		//microsatelite
		br = FileStreamUtil.getBufferedReader(inputMirc);
		str = br.readLine();
		str = br.readLine();
		while(str != null){
			s = new Scanner(str);
			s.next();
			chr = s.next();
			start = s.next();
			end = s.next();
			bw.write(chr+"\t"+start+"\t"+end+"\tMIR");
			bw.newLine();
			str = br.readLine();
		}
		br.close();
		
		//simple repeat
		br = FileStreamUtil.getBufferedReader(inputSim);
		str = br.readLine();
		str = br.readLine();
		while(str != null){
			s = new Scanner(str);
			s.next();
			chr = s.next();
			start = s.next();
			end = s.next();
			bw.write(chr+"\t"+start+"\t"+end+"\tSimple_repeat");
			bw.newLine();
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
			new Main();
		byte[] flag = new byte[60000000];
	}

}
