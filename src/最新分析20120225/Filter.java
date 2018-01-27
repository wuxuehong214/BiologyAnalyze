package 最新分析20120225;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

public class Filter {


	/**
	 * bwa算法之后 最佳匹配结果
	 */
	Map<String,Integer> mapped = new HashMap<String,Integer>();
	
	/**
	 * 读取bwa算法匹配后的reads
	 * @param filename
	 */
	public void readMaxprecisionReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid ;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				mapped.put(readid, 0);
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param inputFile
	 * @param outFile
	 */
	public void readAndoutput(String inputFile, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			Scanner s = null;
			String readid = null;
			String readsid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				readsid = readid.substring(0, readid.indexOf('_'));
//System.out.println(readsid);
				if(mapped.get(readsid) == null){  //说明这是未匹配的reads
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Filter() {
		// TODO Auto-generated constructor stub
		readMaxprecisionReads("maxprecision/crick_CT.txt");
		readMaxprecisionReads("maxprecision/crick_GA.txt");
		readMaxprecisionReads("maxprecision/watson_CT.txt");
		readMaxprecisionReads("maxprecision/watson_GA.txt");
		
		readAndoutput("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt",
				"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_CT.txt");
		readAndoutput("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt",
		"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_GA.txt");
		readAndoutput("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt",
		"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_CT.txt");
		readAndoutput("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt",
		"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_GA.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Filter();
	}

}
