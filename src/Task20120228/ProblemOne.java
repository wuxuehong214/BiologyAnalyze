package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

import util.FileStreamUtil;

/**
 * 输出未匹配的reads
 * @author wuxuehong
 * 2012-3-2
 */
public class ProblemOne {

	private Map<String,Integer> map = new HashMap<String,Integer>();
	private Map<String,Integer> dmap = new HashMap<String,Integer>();
	/**
	 * 读取已经匹配的reads
	 * @param filename
	 */
	public void readMappedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				map.put(s.next(), 1);
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取分割后匹配的reads
	 * @param filename
	 */
	public void readDividedMappedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				readid = readid.substring(0, readid.indexOf('_'));
				map.put(readid, 1);
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			Scanner s = null;
			int total = 0;
			boolean isAvaliable = false;
			String str = br.readLine();
			String readid ;
			while(str != null){
				if(str.startsWith(">")){
					s = new Scanner(str);
					readid = s.next();
					readid = readid.substring(1);
					if(map.get(readid) == null){ //说明未匹配
						isAvaliable = true;
						total ++;
					}else{
						isAvaliable = false;
					}
				}
				if(isAvaliable){
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
			System.out.println("total:"+total);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProblemOne(){
		String base = "D:/recover/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/";
		//读取分割前匹配结果
		readMappedReads(base+"maxprecision/crick_CT.txt");
		readMappedReads(base+"maxprecision/crick_GA.txt");
		readMappedReads(base+"maxprecision/watson_CT.txt");
		readMappedReads(base+"maxprecision/watson_GA.txt");
System.out.println(map.size());
		//读取分割后匹配结果
		readDividedMappedReads(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_CT.txt");
		readDividedMappedReads(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_GA.txt");
		readDividedMappedReads(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_CT.txt");
		readDividedMappedReads(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_GA.txt");
System.out.println(map.size());
		//读取原始reads数据信息
		readAndOutput(base+"O6.GAC.454Reads.fa", base+"unmapped/O6.GAC.454Reads.unmap");
	}
	
	
	public static void main(String args[]){
		new ProblemOne();
	}
	
}
