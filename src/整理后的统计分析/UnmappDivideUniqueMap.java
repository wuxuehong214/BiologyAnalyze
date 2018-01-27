package 整理后的统计分析;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import util.FileStreamUtil;

/**
 * 统计未匹配reads 分割后  唯一匹配的条数
 * 只要分割中后的有一条reads唯一匹配 那么 该reads就是唯一匹配的
 * @author wuxuehong
 * 2012-2-24
 */
public class UnmappDivideUniqueMap {
	/**
	 * bwa 分析后最佳匹配的reads
	 */
	Set<String> mapped = new HashSet<String>();
	
	Map<String,Reads> map = new HashMap<String,Reads>();
	
	Set<String> unmap = new HashSet<String>();
	
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
				mapped.add(readid);
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readMappedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			Reads reads;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				reads = map.get(readid);
				if(reads == null){
					reads = new Reads();
					reads.readid = readid;
					reads.count = 1;
					map.put(readid, reads);
				}else{
					reads.count++;
				}
				str = br.readLine(); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造函数
	 */
	public UnmappDivideUniqueMap(){
		
		readMaxprecisionReads("maxprecision/crick_CT.txt");
		readMaxprecisionReads("maxprecision/crick_GA.txt");
		readMaxprecisionReads("maxprecision/watson_CT.txt");
		readMaxprecisionReads("maxprecision/watson_GA.txt");
System.out.println("bwa mapped reads:"+mapped.size());
		
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");

		Set<String> aa = new HashSet<String>();
		Iterator<Reads> it = map.values().iterator();
		Reads reads;
		while(it.hasNext()){
			reads = it.next();
			if(reads.count == 1){
				unmap.add(reads.readid.substring(0, reads.readid.indexOf('_')));
			}
			aa.add(reads.readid.substring(0, reads.readid.indexOf('_')));
		}
		aa.removeAll(mapped);
		System.out.println("unmap mapped size:"+aa.size());
		
		System.out.println("before unique reads:"+unmap.size());
		unmap.removeAll(mapped);
		System.out.println("after unique reads:"+unmap.size());
	}
	
	public static void main(String args[]){
		new UnmappDivideUniqueMap();
	}
	
	class Reads{
		String readid;
		int count = 0;
	}

}
