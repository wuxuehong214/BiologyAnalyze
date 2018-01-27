package 最新分析20120225;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 * 将454的文件做出来的图如o1,o6-454.ppt
这个做背景为pdf 文件第3页中的最上面2个图。
前景是：map 的个数和唯一map的个数，背景和前景分别用红色和蓝色

我的理解是：横坐标是reads长度，每个坐标点对应有两列，一列是该reads长度下的reads总数用红色画，一列是该reaads长度下map的reads数用蓝色画  

 * @author wuxuehong
 * 2012-2-26
 */
public class MappedNumForEachLen {

	Map<String,Reads> mapReads = new HashMap<String,Reads>();      //正常匹配结果
	
	int[] uniqueMap = new int[1000];
	int[] map = new int[1000];
	
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
	
	
	
	public MappedNumForEachLen(String args[]){
		if(args.length!=1){
			System.out.println("wrong parameters!");
			return;
		}
		double total = Double.parseDouble(args[0]);
		//   读取bwa分析结果计算   未分割前reads
		readMppedReads("maxprecision/crick_CT.txt");
		readMppedReads("maxprecision/crick_GA.txt");
		readMppedReads("maxprecision/watson_CT.txt");
		readMppedReads("maxprecision/watson_GA.txt");

		Iterator<Reads> it = mapReads.values().iterator();
		int mapped=0,uniquemapped =0;
		int totalMappedbp = 0;
		while(it.hasNext()){
			Reads read = it.next();
			if(read.times == 1) {
				uniqueMap[read.len]++;
				uniquemapped++;
				totalMappedbp += (read.matchics*read.len);
			}
			map[read.len]++;
			mapped++;
		}
System.out.println("total mapped reads:"+mapReads.size());
System.out.println("total mapped reads:"+mapped);	
System.out.println("unique mapped reads:"+uniquemapped);
System.out.println("unique mapped reads bp:"+totalMappedbp);	
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("chart.txt")));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("chart_out.txt")));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("matched_chart_out.txt")));
			String str = br.readLine();
			Scanner s = null;
			int len;
			double mapd;
			double umapd;
			while(str != null){
				s = new Scanner(str);
				len = s.nextInt();
				bw.write(str+"\t"+map[len]+"\t"+uniqueMap[len]);
				bw.newLine();
				if(map[len]>0){
					mapd = (double)map[len]/total;
					umapd = (double)uniqueMap[len]/total;
					bw2.write(str+"\t"+map[len]+"\t"+uniqueMap[len]+"\t"+mapd+"\t"+umapd);
					bw2.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			bw2.flush();
			bw2.close();
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		new MappedNumForEachLen(args);
	}
	
	
}
