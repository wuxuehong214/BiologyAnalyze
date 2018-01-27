package 整理后的统计分析;

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
 * 统计  有多少reads进行了分割 分割后reads匹配了
 * 以及对未匹配的分割 后 得到了多少条reads
 * @author wuxuehong
 * 2012-2-8
 */
public class Further {

	private Set<String> divideReads = new HashSet<String>(); // 被分割的reads
	private Set<String> allReads = new HashSet<String>(); // 分割后得到的reads
	private Set<String> mapReads = new HashSet<String>(); //匹配的reads
	private int mappedNum; //分割后reads 匹配的数目
	private int total = 0;
	
	private Set<String> unmapDividedReads = new HashSet<String>(); //未匹配的reads 分割后得到的reads条数
	private Set<String> mappedUnmapDividedReads = new HashSet<String>(); //匹配了的reads
	
	private Set<String> unmap = new HashSet<String>();
	
	/**
	 * 读取 bwa分析结果之后   进一步分割后的文件
	 * @param filename
	 */
	public void readDividedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			String readid;
			while(str != null){
				if(str.startsWith(">")){
					readid = str.substring(1, str.indexOf('_'));
					divideReads.add(readid);
					total++;
					str = br.readLine();
					allReads.add(readid+str);
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取分割后匹配结果文件
	 * @param filename
	 */
	public void readMappedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			String len;
			while(str != null){
				mappedNum++;
				s = new Scanner(str);
				readid = s.next();
				for(int i=0;i<4;i++)s.next();
				len = s.next();
				mapReads.add(len+readid);
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取未匹配reads分割后结果
	 * @param filename
	 */
	public void readUnmapDividedReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")){
					unmapDividedReads.add(str.substring(1));
					unmap.add(str.substring(1,str.indexOf('_')));
				}
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 未匹配reads分段分析后 匹配结果
	 * @param filename
	 */
	public void readMappedUnmappReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				mappedUnmapDividedReads.add(s.next().substring(0,14));
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private Map<String,Integer> mapped = new HashMap<String,Integer>();
	public void readMappedPrecisionReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				mapped.put(s.next(), 1);
				str = br.readLine();
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Further() {
		readDividedReads("maxprecision/divide/crick_CT.txt");
		readDividedReads("maxprecision/divide/crick_GA.txt");
		readDividedReads("maxprecision/divide/watson_CT.txt");
		readDividedReads("maxprecision/divide/watson_GA.txt");
		System.out.println("被分割的reads数目："+divideReads.size());
		System.out.println("分割后得到的reads数目:"+allReads.size());
		
		readMappedReads("maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
		readMappedReads("maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
		readMappedReads("maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
		readMappedReads("maxprecision/divide/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");
		System.out.println("分割后的reads中匹配的数目:"+mapReads.size());

		readUnmapDividedReads("unmappble/crick_CT.txt");
		readUnmapDividedReads("unmappble/crick_GA.txt");
		readUnmapDividedReads("unmappble/watson_CT.txt");
		readUnmapDividedReads("unmappble/watson_GA.txt");
		System.out.println("未匹配的reads数目:"+unmap.size());
		System.out.println("未匹配reads分割后得到的条数:"+unmapDividedReads.size());
		
		readMappedUnmappReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
		readMappedUnmappReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
		readMappedUnmappReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
		readMappedUnmappReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");
		
		readMappedPrecisionReads("maxprecision/crick_CT.txt");
		readMappedPrecisionReads("maxprecision/crick_GA.txt");
		readMappedPrecisionReads("maxprecision/watson_CT.txt");
		readMappedPrecisionReads("maxprecision/watson_GA.txt");
		int mapp = 0;
//		Iterator<String> it = mapped.iterator();
//		while(it.hasNext()){
//			if(mapped.get(it.next().substring(0, 14)) == null) mapp++;
//		}
		mappedUnmapDividedReads.removeAll(mapped.keySet());
		System.out.println("mapped reads:"+mapped.size());
		System.out.println("未匹配reads分割匹配分析后匹配的条数:"+mappedUnmapDividedReads.size()+"\t\t"+mapp);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Further();
	}

}
