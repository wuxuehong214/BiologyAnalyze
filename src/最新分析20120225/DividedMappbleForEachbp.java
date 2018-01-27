package 最新分析20120225;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;
import 最新分析20120225.UniqueMapbps.Reads;

/**
 * 横坐标  match上的reads bp长度
 * 纵坐标match 率
 * @author wuxuehong
 * 2012-2-26
 */
public class DividedMappbleForEachbp {

	/**
	 * bwa算法之后 最佳匹配结果
	 */
	Map<String,Integer> mapped = new HashMap<String,Integer>();
	Map<String,Reads> divedeMap = new HashMap<String,Reads>(); //分割后匹配结果
	class Reads{
		int times = 0; //出现次数
		int len; //reads 长度
		float matchics; //匹配率
	}
	
	int[] total = new int[1000];
	int[] mapp = new int[1000];
	int[] unmapp = new int[1000];  //唯一map
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
				
				if(mapped.get(readid.substring(0, 14)) == null){  //说明这条分割的reads 是未map的 需要计算
					reads = divedeMap.get(readid);
					if(reads == null){
						reads = new Reads();
						reads.len = len;
						reads.matchics = matchics;
						reads.times = 1;
						divedeMap.put(readid, reads);
					}else{
						reads.times ++;
						if(matchics>reads.matchics){
							divedeMap.put(readid, reads);
						}
					}
				}
				str = br.readLine(); 
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public DividedMappbleForEachbp(String args[]){
		if(args.length != 1) {
			System.out.println("wrong paramaters!");
			return;
		}
		double total = Double.parseDouble(args[0]);
		readMaxprecisionReads("maxprecision/crick_CT.txt");
		readMaxprecisionReads("maxprecision/crick_GA.txt");
		readMaxprecisionReads("maxprecision/watson_CT.txt");
		readMaxprecisionReads("maxprecision/watson_GA.txt");
System.out.println("bwa mapped reads:"+mapped.size());

		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt");
		readMappedReads("unmappble/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt");
		
		Iterator<Reads> it = divedeMap.values().iterator();
		Reads read ;
		int bp;
		while(it.hasNext()){
			read = it.next();
			bp = (int) (read.len*read.matchics);
			mapp[bp]++;
			if(read.times == 1){
				unmapp[bp]++;
			}
		}
System.out.println("matched small reads:"+divedeMap.size());
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("divide_chart.txt")));
			bw.write("#bp\t#map\t#unique map\t#map/total\t#uniquemap/total");
			bw.newLine();
			double mapd;
			double umapd;
			for(int i=0;i<1000;i++){
				if(mapp[i]!=0){
					mapd = (double)mapp[i]/total;
					umapd = (double)unmapp[i]/total;
					bw.write(i+"\t"+mapp[i]+"\t"+unmapp[i]+"\t"+mapd+"\t"+umapd);
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		new DividedMappbleForEachbp(args);
	}
	
}
