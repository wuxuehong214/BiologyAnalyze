package 统计分析;

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
		readReads("E:/研究生工作/personal_data/1999.GAC.454Reads/1999.GAC.454Reads.fa");
		System.out.println("Total reads:"+totalCount);
		System.out.println("Total reads length:"+totalReadsLength);
		
		/*****************未过滤repeat区 mapped reads统计*********************/
		for(int i=1;i<=22;i++){
			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_Chr"+i+".txt");
			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_Chr"+i+".txt");
			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_Chr"+i+".txt");
			readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_Chr"+i+".txt");
			System.out.println(i+"\t\t"+totalMappedReadsLength+"\t\t"+totalMappedReadsBps);
		}
		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_ChrX.txt");
		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_ChrX.txt");
		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_ChrX.txt");
		readMappedReadsWithRepeat("E:/研究生工作/personal_data/1999.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_ChrX.txt");
		
		System.out.println("**********************With repeat********************");
		System.out.println("Multiple mapped reads:"+multiMapr.size());
		System.out.println("Uniquely Mapped reads :"+uniqueMapr.size()+"\t\tpercent:"+(float)uniqueMapr.size()/(float)totalCount);
		System.out.println("Mapped reads:"+uniqueMap.size());
		System.out.println("Mapped reads :"+sreadsr.size()+"\t\tpercent:"+(float)sreadsr.size()/(float)totalCount);
		System.out.println("Mapped reads length:"+totalMappedReadsLength+"\t\tpercent:"+(float)totalMappedReadsLength/(float)totalReadsLength);
		System.out.println("Mapped reads bp:"+totalMappedReadsBps+"\t\tpercent:"+(float)totalMappedReadsBps/(float)totalReadsLength);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MapPrecision(args);
	}

}
