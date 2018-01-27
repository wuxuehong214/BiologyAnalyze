package SoftAnalysisOfGenomicMethylationOfSingleCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * 将真个人类基因组相关数据: repeatmasker, CNV, SD分别按照每条染色体分割开来。 
 * @author wuxuehong
 * 2012-5-13
 */ 
public class SeparateChrs {
	
	public static void main(String args[]){
		String base = "E:/morehouse/download/";
		separateSD(base+"SD.txt", base+"SD/");
		separateCNV(base+"CNV.txt", base+"CNV/");
		separateRepeats(base+"repeatmasker.txt", base+"REPEAT/");
	}
	
	/**
	 * 分割segmental duplicate data,
	 */
	public static void separateSD(String inputFile, String outFilePath){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			Map<String,BufferedWriter> map = new HashMap<String,BufferedWriter>();
			String head = br.readLine();
			String str = br.readLine();
			Scanner s = null;
			String chr;
			BufferedWriter bw;
			while(str != null){
				s = new Scanner(str);
				s.next();
				chr = s.next();
				bw = map.get(chr);
				if(bw == null){
					bw = new BufferedWriter(new FileWriter(new File(outFilePath+chr+".txt")));
					bw.write(head);
					bw.newLine();
					map.put(chr, bw);
				}
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Iterator<BufferedWriter> it = map.values().iterator();
			while(it.hasNext()){
				bw = it.next();
				bw.flush();
				bw.close();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分割CNV
	 */
	public static void separateCNV(String inputFile, String outFilePath){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			Map<String,BufferedWriter> map = new HashMap<String,BufferedWriter>();
			String head = br.readLine();
			String str = br.readLine();
			Scanner s = null;
			String chr;
			BufferedWriter bw;
			while(str != null){
				s = new Scanner(str);
				s.next();
				chr = s.next();
				bw = map.get(chr);
				if(bw == null){
					bw = new BufferedWriter(new FileWriter(new File(outFilePath+chr+".txt")));
					bw.write(head);
					bw.newLine();
					map.put(chr, bw);
				}
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Iterator<BufferedWriter> it = map.values().iterator();
			while(it.hasNext()){
				bw = it.next();
				bw.flush();
				bw.close();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分割Repepats
	 */
	public static void separateRepeats(String inputFile, String outFilePath){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			Map<String,BufferedWriter> map = new HashMap<String,BufferedWriter>();
			String head = br.readLine();
			String str = br.readLine();
			Scanner s = null;
			String chr;
			BufferedWriter bw;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<5;i++)s.next();
				chr = s.next();
				bw = map.get(chr);
				if(bw == null){
					bw = new BufferedWriter(new FileWriter(new File(outFilePath+chr+".txt")));
					bw.write(head);
					bw.newLine();
					map.put(chr, bw);
				}
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Iterator<BufferedWriter> it = map.values().iterator();
			while(it.hasNext()){
				bw = it.next();
				bw.flush();
				bw.close();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
