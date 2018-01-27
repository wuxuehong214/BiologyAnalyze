package statics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import util.FileStreamUtil;

/**
 * 匹配的reads数/总得reads数目
 * @author wuxuehong
 * 2012-1-3
 */
public class MapPrecision {
	
	//总得reads数目
	int totalCount = 0;
	//总的reads长度
	int totalReadsLength = 0;
	//匹配的reads长度                             //直接求
	int totalMappedReadsLength = 0;
	//匹配的reads bp长度                 //reads长度乘以匹配率
	int totalMappedReadsBps = 0;
	
	List<String> treads = new ArrayList<String>();
	
	/***************************未过滤repeat区的统计**********************************/
	//用于统计唯一map的reads数目     就是map到ref上一个位置的
	Map<String,ReadVo> uniqueMapr = new HashMap<String,ReadVo>(); 
	//用于统计多个map的reads数目
	Map<String,Integer> multiMapr = new HashMap<String,Integer>();
	//用于统计总得map的reads数目   
	Set<String> sreadsr = new HashSet<String>();
	
	//用于统计唯一map的reads数目     就是map到ref上一个位置的
	Map<String,Integer> uniqueMap = new HashMap<String,Integer>(); 
	
	//匹配的reads总数
	Map<String,Integer> uniquereads = new HashMap<String,Integer>();
	
	
	class ReadVo{
		String readid;
		String chrid;
		String start;
	}
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")) {
					totalCount++;
					totalReadsLength+=(Integer.parseInt(str.substring(str.lastIndexOf('=')+1))); //reads长度累加
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){e.printStackTrace();}
	}
	
	/**
	 * 读取map后reads信息(未过滤repeat区)
	 * 计算map数目
	 * @param filename
	 */
	public void readMappedReadsWithRepeat(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine(); //忽略第一行
			Scanner s = null;
			str = br.readLine();
			String readid,chrid,start;
			float matchics;
			int length;
			ReadVo r;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				chrid = s.next();
				start = s.next();
				r = uniqueMapr.get(readid); 
				if(r!=null){
					if(!r.chrid.equals(chrid) || !r.start.equals(start)){  //readid相同 如果匹配的ref上位置不一样则不是位一匹配
						uniqueMapr.remove(readid);
						multiMapr.put(readid, 1);
						}
				}else{
					r = new ReadVo();
					r.readid = readid;
					r.chrid = chrid;
					r.start = start;
					if(multiMapr.get(readid)==null)
						uniqueMapr.put(readid, r);
				}
				sreadsr.add(readid);
				
				if(uniqueMap.get(readid) == null){
					s.next();
					length = s.nextInt();
					totalMappedReadsLength += length;  //累计mapped reads的长度
					s.next();
					matchics = Float.parseFloat(s.next());
					totalMappedReadsBps += (length*matchics); //累计mapped reads bp数量
					uniqueMap.put(readid, 1);
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
				e.printStackTrace();
		}
	}

	public MapPrecision(String args[]) {
//		readReads("E:/研究生工作/personal_data/1999.GAC.454Reads/1999.GAC.454Reads.fa");
//		System.out.println("Total reads:"+totalCount);
//		System.out.println("Total reads length:"+totalReadsLength);
//		
//		/*****************未过滤repeat区 mapped reads统计*********************/
//		for(int i=1;i<=22;i++){
//			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_Chr"+i+".txt");
//			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_Chr"+i+".txt");
//			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_Chr"+i+".txt");
//			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_Chr"+i+".txt");
//			System.out.println(i+"\t\t"+totalMappedReadsLength+"\t\t"+totalMappedReadsBps);
//		}
//		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_ChrX.txt");
//		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_ChrX.txt");
//		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_ChrX.txt");
//		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_ChrX.txt");
//		
//		System.out.println("**********************With repeat********************");
//		System.out.println("Multiple mapped reads:"+multiMapr.size());
//		System.out.println("Uniquely Mapped reads :"+uniqueMapr.size()+"\t\tpercent:"+(float)uniqueMapr.size()/(float)totalCount);
//		System.out.println("Mapped reads:"+uniqueMap.size());
//		System.out.println("Mapped reads :"+sreadsr.size()+"\t\tpercent:"+(float)sreadsr.size()/(float)totalCount);
//		System.out.println("Mapped reads length:"+totalMappedReadsLength+"\t\tpercent:"+(float)totalMappedReadsLength/(float)totalReadsLength);
//		System.out.println("Mapped reads bp:"+totalMappedReadsBps+"\t\tpercent:"+(float)totalMappedReadsBps/(float)totalReadsLength);
	
		readMappedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/crick_CT.txt",uniqueMap1);
		readDividedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/divide/bwa/FormatConvert/Fusion/crick_CT.txt",uniqueMap1);
System.out.println(uniqueMap1.size());
		readMappedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/crick_GA.txt",uniqueMap2);
		readDividedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/divide/bwa/FormatConvert/Fusion/crick_GA.txt",uniqueMap2);
System.out.println(uniqueMap2.size());
		readMappedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/watson_CT.txt",uniqueMap3);
		readDividedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/divide/bwa/FormatConvert/Fusion/watson_CT.txt",uniqueMap3);
System.out.println(uniqueMap3.size());
		readMappedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/watson_GA.txt",uniqueMap4);
		readDividedBPs("j:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/divide/bwa/FormatConvert/Fusion/watson_GA.txt",uniqueMap4);
System.out.println(uniqueMap4.size());
		Iterator<String> it = uniqueMap1.keySet().iterator();
		String readid = null;
		while(it.hasNext()){
			readid = it.next();
			if(uniqueMap.get(readid)==null || uniqueMap1.get(readid)>uniqueMap.get(readid)){
				uniqueMap.put(readid, uniqueMap1.get(readid));
			}
		}
		
		it = uniqueMap2.keySet().iterator();
		while(it.hasNext()){
			readid = it.next();
			if(uniqueMap.get(readid)==null || uniqueMap2.get(readid)>uniqueMap.get(readid)){
				uniqueMap.put(readid, uniqueMap2.get(readid));
			}
		}
		
		it = uniqueMap3.keySet().iterator();
		while(it.hasNext()){
			readid = it.next();
			if(uniqueMap.get(readid)==null || uniqueMap3.get(readid)>uniqueMap.get(readid)){
				uniqueMap.put(readid, uniqueMap3.get(readid));
			}
		}
		
		it = uniqueMap4.keySet().iterator();
		while(it.hasNext()){
			readid = it.next();
			if(uniqueMap.get(readid)==null || uniqueMap4.get(readid)>uniqueMap.get(readid)){
				uniqueMap.put(readid, uniqueMap4.get(readid));
			}
		}
		
		Iterator<Integer> it2 = uniqueMap.values().iterator();
		while(it2.hasNext()){
			totalMappedReadsBps += it2.next();
		}
		System.out.println(totalMappedReadsBps);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MapPrecision(args);
	}

	private Map<String,Integer> divideMap = new HashMap<String,Integer>();
	public void readDividedBPs(String filename, Map<String,Integer> map){
		  divideMap.clear();
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();  //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int len = 0;
			float matchics = 0;
			String readid = null;
			int addition=0;
			Integer value;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				if(divideMap.get(readid)==null){
					for(int i=0;i<4;i++) s.next();
					len = s.nextInt();
					s.next();
					matchics = Float.parseFloat(s.next());
					addition = (int) (len*matchics);
					divideMap.put(readid, addition);
					
					readid = readid.substring(0,14); //get real reads ID
					value = map.get(readid);
					if(value != null){
						map.put(readid, value+addition);
					}else
						System.out.println("***************************88");
				}
				str = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取已经mapped的reads中map的bp数目
	 * @param filename
	 */
	
	private Map<String,Integer> uniqueMap1 = new HashMap<String,Integer>();
	private Map<String,Integer> uniqueMap2 = new HashMap<String,Integer>();
	private Map<String,Integer> uniqueMap3 = new HashMap<String,Integer>();
	private Map<String,Integer> uniqueMap4 = new HashMap<String,Integer>();
	public void readMappedBPs(String filename, Map<String,Integer> map){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();  //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int len = 0;
			float matchics = 0;
			String readid = null;
			int addition=0;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				for(int i=0;i<4;i++) s.next();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				addition = (int) (len*matchics);
				if(map.get(readid)==null || addition>map.get(readid)){
					map.put(readid, addition);
				}
				str = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
