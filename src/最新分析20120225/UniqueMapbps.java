package 最新分析20120225;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import util.FileStreamUtil;


/**
 * 分割前唯一匹配的bp总长；分割后，在分割的小reads中，唯一匹配的bp总长
 * @author wuxuehong
 * 2012-2-25
 */
public class UniqueMapbps {
	
	Map<String,Reads> mapReads = new HashMap<String,Reads>();      //正常匹配结果
	
	Map<String,Reads> divedeMap = new HashMap<String,Reads>(); //分割后匹配结果
	/*****************************分割前唯一匹配的bp总长******************************************/
	class Reads{
		int times = 0; //出现次数
		int len; //reads 长度
		float matchics; //匹配率
	}
	
	/**
	 * 读取正常结果下最佳匹配结果
	 * @param filename
	 */
	public void readMppedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			if(str.startsWith("ReadID"))str = br.readLine();
			Scanner s = null;
			String readid; //id
			int len = 0;  //长度
			float matchics; //匹配率
			int i;
			Reads read;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				for(i=0;i<4;i++)s.next();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				read = mapReads.get(readid);
				if(read == null ){
					read = new Reads();
					read.times = 1;
					read.len = len;
					read.matchics = matchics;
					mapReads.put(readid, read);
				}else{
					read.times++;
					if(matchics > read.matchics)read.matchics = matchics;
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取分割后 匹配的reads
	 * @param filename
	 */
	public void readMappedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			Reads reads;
			int len = 0;  //长度
			float matchics; //匹配率
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				for(int i=0;i<4;i++)s.next();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				
				if(mapReads.get(readid.substring(0, 14)) == null){  //说明这条分割的reads 是未map的 需要计算
					reads = divedeMap.get(readid);
					if(reads == null){
						reads = new Reads();
						reads.len = len;
						reads.matchics = matchics;
						reads.times = 1;
						divedeMap.put(readid, reads);
					}else{
						reads.times ++;
					}
				}
				str = br.readLine(); 
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public UniqueMapbps(){
		System.out.println("*******************************分割前***************************************");
		//   读取bwa分析结果计算   未分割前reads
		readMppedReads("maxprecision/crick_CT.txt");
		readMppedReads("maxprecision/crick_GA.txt");
		readMppedReads("maxprecision/watson_CT.txt");
		readMppedReads("maxprecision/watson_GA.txt");
		
		int uniqueMap = 0, totalMaps = 0;  //唯一匹配的reads数目， 唯一匹配的bp总长
		Iterator<Reads> it = mapReads.values().iterator();
		while(it.hasNext()){
			Reads read = it.next();
			if(read.times == 1) {
				uniqueMap++;
				totalMaps += (read.len*read.matchics);
			}
		}
		System.out.println("匹配的reads总数:"+mapReads.size());
		System.out.println("未分割前唯一匹配的reads个数:"+uniqueMap);
		System.out.println("未分割前唯一匹配的reads bp总数:"+totalMaps);
		
		
		System.out.println("*******************************分割后***************************************");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");
		
		uniqueMap = 0;totalMaps = 0;
		it = divedeMap.values().iterator();
		while(it.hasNext()){
			Reads read = it.next();
			if(read.times == 1) {
				uniqueMap++;
				totalMaps += (read.len*read.matchics);
			}
		}
		System.out.println("匹配的reads总数:"+divedeMap.size());
		System.out.println("分割后唯一匹配的reads个数:"+uniqueMap);
		System.out.println("分割后唯一匹配的reads bp总数:"+totalMaps);
	}

	
	public static void main(String args[]){
		new UniqueMapbps();
	}
}
