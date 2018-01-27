package 整理后的统计分析;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 匹配率分析
 * 需要计算：
 * 1，总的reads数目
 * 2，匹配的reads数目
 * 3，唯一匹配的reads数目
 * 4，所有reads长度和
 * 5，匹配的reads长度和
 * 6，匹配的reads bp数和
 * 7，加分割后匹配reads bp数和
 * 8，加分割以及未匹配reads分析后匹配的reads bp数和
 * @author wuxuehong
 * 2012-2-5
 */
public class MapPrecision {
	
	int totalReads = 0; //总的reads数目
	int totalMappedReads = 0; //匹配的reads数目
	int totalUniqueMappedReads = 0; //唯一匹配的reads数目
	int totalReadsLen = 0; //所有reads长度
	int totalMappedReadsLen = 0; //匹配的reads长度和
	int totalMappedReadsBPs = 0; //匹配的reads bp数和
	int totalMappedReadsBPs_divide = 0;//加分割后匹配reads bp数和
	int totalMappedReadsBPs_divide_unmap = 0; //加分割以及未匹配reads分析后匹配的reads bp数和

	Map<String,Reads> mapReads = new HashMap<String,Reads>();      //正常匹配结果
	Map<String,Integer> Divide_mapReads = new HashMap<String,Integer>(); //分割后匹配结果
	Map<String,Integer> Unmap_mapReads = new HashMap<String,Integer>();  //未匹配分段后匹配结果
	
	class Reads{
		int times; //出现次数
//		String readid; //reads ID
		int len; //reads 长度
		float matchics; //匹配率
	}
	/**
	 * 读取原始Reads数据
	 * @param filename
	 */
	public void readOriginalReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")){
					totalReads++;    //reads条数加1
					totalReadsLen+=(Integer.parseInt(str.substring(str.lastIndexOf('=')+1))); //reads长度叠加
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException E){
			E.printStackTrace();
		}
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
	 * 读取正常匹配结果  以及分割后匹配结果
	 * @param fileanem1
	 * @param filename2
	 */
	public void readMapedReadsAndDivideMappedReads(String fileanem1,String filename2,Map<String,Integer> map){
		try{
			BufferedReader br1 = FileStreamUtil.getBufferedReader(fileanem1);
			String str = br1.readLine();
			if(str.startsWith("ReadID"))str = br1.readLine();
			Scanner s = null;
			String readid;
			int len;
			float matchics;
			int i;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();  //read id
				for(i=0;i<4;i++) s.next();
				len = s.nextInt();  //reads length
				s.next();
				matchics = Float.parseFloat(s.next()); //匹配率
				if(map.get(readid)==null){
					map.put(readid, (int)(len*matchics));
				}
				str = br1.readLine();
			}
			br1.close();
			
			//读取分割后最佳匹配结果
			BufferedReader br2 = FileStreamUtil.getBufferedReader(filename2);
			str = br2.readLine();
			Integer temp ;
			while(str != null){
				s = new Scanner(str);
				readid = s.next().substring(0, 14);
				for(i=0;i<4;i++) s.next();
				len = s.nextInt();  //reads length
				s.next();
				matchics = Float.parseFloat(s.next()); //匹配率
				
				temp = map.get(readid);
				if(temp!=null){
					map.put(readid, temp.intValue()+(int)(len*matchics));
				}
				str = br2.readLine();
			}
			br2.close();
		}catch(IOException e){ 
			e.printStackTrace();
		}
	}
	/**
	 * 综合四种情况下最优匹配结果  从而计算 加上分割后最优匹配bp数
	 * @param map
	 */
	public void calMaxMappedBps(Map<String,Integer> map){
		String readid;
		Integer value;
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			readid = it.next();
			value =  Divide_mapReads.get(readid);
			if(value==null || (value!=null && map.get(readid).intValue()>value.intValue())){
				Divide_mapReads.put(readid, map.get(readid));
			}
		}
	}
	/**
	 * 读取分割出来的reads的最佳匹配结果
	 * @param filename
	 */
	public void readWithDivideMappedReads(){
		//CT与watson链匹配分析后  reads匹配bp数
		Map<String,Integer> watsonCTmap = new HashMap<String,Integer>();
		Map<String,Integer> watsonGAmap = new HashMap<String,Integer>();
		Map<String,Integer> crickCTmap = new HashMap<String,Integer>();
		Map<String,Integer> crickGAmap = new HashMap<String,Integer>();
		readMapedReadsAndDivideMappedReads("maxprecision/watson_CT.txt", 
				"maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt", watsonCTmap);
		readMapedReadsAndDivideMappedReads("maxprecision/watson_GA.txt", 
				"maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt", watsonGAmap);
		readMapedReadsAndDivideMappedReads("maxprecision/crick_CT.txt", 
				"maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt", crickCTmap);
		readMapedReadsAndDivideMappedReads("maxprecision/crick_GA.txt", 
				"maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt", crickGAmap);
		
		calMaxMappedBps(watsonCTmap);
		calMaxMappedBps(watsonGAmap);
		calMaxMappedBps(crickCTmap);
		calMaxMappedBps(crickGAmap);
		
		Iterator<Integer> it = Divide_mapReads.values().iterator();
		while(it.hasNext()){
			totalMappedReadsBPs_divide += it.next();
		}
	}
	
	
	/**
	 * 读取未匹配reads 分段分析后的结果
	 * @param filename
	 */
	public void readUnmappedReads(String filename){
		try{
			BufferedReader br1 = FileStreamUtil.getBufferedReader(filename);
			String str = br1.readLine();
			if(str.startsWith("ReadID"))str = br1.readLine();
			Scanner s = null;
			String readid;
			int len;
			float matchics;
			int i;
			Integer temp;
			int bps;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();  //read id
				for(i=0;i<4;i++) s.next();
				len = s.nextInt();  //reads length
				s.next();
				matchics = Float.parseFloat(s.next()); //匹配率
				bps = (int)(len*matchics); 
					
				temp = Unmap_mapReads.get(readid);
				if(temp == null || (temp != null && bps>temp.intValue())){
					Unmap_mapReads.put(readid, bps);
				}
				str = br1.readLine();
			}
			br1.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	
	
	public MapPrecision() {
		//第一步     读取原始数据计算
		readOriginalReads("CT.txt");
		System.out.println("总的reads数:"+totalReads);
		System.out.println("所有reads长度和:"+totalReadsLen);
		
		//第二步    读取bwa分析结果计算
		readMppedReads("maxprecision/crick_CT.txt");
		readMppedReads("maxprecision/crick_GA.txt");
		readMppedReads("maxprecision/watson_CT.txt");
		readMppedReads("maxprecision/watson_GA.txt");
		totalMappedReads = mapReads.size();
		Iterator<Reads> it = mapReads.values().iterator();
		Reads read;
		while(it.hasNext()){
			read = it.next();
			totalMappedReadsLen += read.len;
			totalMappedReadsBPs += (read.len*read.matchics);
			if(read.times==1)totalUniqueMappedReads++;
		}
		System.out.println("匹配的reads数:"+totalMappedReads+"\t"+(float)totalMappedReads/(float)totalReads);
		System.out.println("唯一匹配的reads数:"+totalUniqueMappedReads+"\t"+(float)totalUniqueMappedReads/(float)totalReads);
		System.out.println("匹配的reads长度和:"+totalMappedReadsLen+"\t"+(float)totalMappedReadsLen/(float)totalReadsLen);
		System.out.println("匹配的reads bp数和:"+totalMappedReadsBPs+"\t"+(float)totalMappedReadsBPs/(float)totalReadsLen);
		
		//第三步   在bwa分析结果基础上加上分割处理后的计算
//		readWithDivideMappedReads();
//		System.out.println("加上分割分析后匹配的reads bp数和:"+totalMappedReadsBPs_divide+"\t"+(float)totalMappedReadsBPs_divide/(float)totalReadsLen);
		
		//第四步  在以上基础上对unmappreads分段匹配处理后的计算
//		readUnmappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
//		readUnmappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");
//		readUnmappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
//		readUnmappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
//		Iterator<Integer> it2 = Unmap_mapReads.values().iterator();
//		while(it2.hasNext()){
//			totalMappedReadsBPs_divide_unmap += it2.next();
//		}
//		totalMappedReadsBPs_divide_unmap+=totalMappedReadsBPs_divide;
//		System.out.println("加上分割，以及未匹配Reads分段分析后匹配的reads bp数和："+totalMappedReadsBPs_divide_unmap+"\t"+(float)totalMappedReadsBPs_divide_unmap/(float)totalReadsLen);;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MapPrecision();
	}

}
