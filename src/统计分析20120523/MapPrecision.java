package 统计分析20120523;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * 
 * 问题：
 * 所有reads的匹配部分bp数总和 and percentage:
 * 这个reads数对象: 是bwa分析后的包含Multiple 匹配的reads
 * 还是 ： 过滤后的
 * 如果一条reads在 chr1中匹配了30bp  在chr4中匹配了40bp  在chr14中匹配了60bp 那么 这条reads匹配了多少bp
 * @author wuxuehong
 *
 * 2012-5-23                                                                                       
 */
public class MapPrecision {
	
	private int totalReadsNum = 0 ; //总的reads数目
	private int totalMappedReadsNum = 0 ; //总的matched reads数目
	private int totalUnMappedReadsNum = 0; //总的未matched reads数目
	private int totalUniqueMappedReadsNum = 0; //总的唯一matched reads数目
	
	private int totalReadsLen = 0; //所有reads长度和
	private int totalMappedReadsLen = 0; //所有匹配reads长度和
	private int totalMappedReadsBp = 0 ;//所有匹配reads 匹配部分Bp数和。(包括切割后的)
	
	private Set<ReadsVo> reads = new HashSet<ReadsVo>();//bwa 后匹配reads
	private Set<ReadsVo> uniques = new HashSet<ReadsVo>();//唯一匹配的reads
	private Map<String,Integer> divides = new HashMap<String, Integer>(); //分割后reads匹配情况
	
	private Map<String,ReadsVo> map = new HashMap<String,ReadsVo>(); //过滤后的reads
	/**
	 * 读取原始reads数据信息
	 * @param filename
	 */
	public void readOriginalReads(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")){
					totalReadsNum++;
					totalReadsLen+=(Integer.parseInt(str.substring(str.lastIndexOf('=')+1))); //reads长度累加
				}
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取reads匹配结 果  bwa结果
	 * @param filename
	 */
	public void readMappedReads(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			if(str.startsWith("ReadID"))str = br.readLine();
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				ReadsVo r = new ReadsVo();
				r.readid = s.next().substring(0,14);
				for(int i=0;i<4;i++)s.next();
				r.len = s.nextInt();
				s.next();
				r.matchics = Float.parseFloat(s.next());
				r.cigra = s.next();
				reads.add(r);
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取bwa 匹配后  ，并且经过过滤后的reads
	 * @param filename
	 */
	public void readMpapedFilterReads(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			if(str.startsWith("ReadID"))str = br.readLine();
			Scanner s = null;
			String readid,cigra;
			int len;
			float matchics;
			while(str != null){
				s = new Scanner(str);
				readid = s.next().substring(0,14);
				for(int i=0;i<4;i++)s.next();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				cigra = s.next();
				ReadsVo r = map.get(readid);
				if(r == null){
					r = new ReadsVo();
					r.times = 1;
					r.readid = readid;
					r.len = len;
					s.next();
					r.matchics = matchics;
					r.cigra = cigra;
					byte[] flag = new byte[r.len];        //记录该reads中每个位置匹配情况
					char[] ch = Util.getChars(r.cigra,r.len);   //获取该reads每个位置匹配情况
					for(int i=0;i<ch.length;i++){         //一旦在特定位置匹配了 那么就将该位置计算加1
						if(ch[i] == 'M') flag[i]++;
					}
					r.flag = flag;
					map.put(readid, r);
				}else{
					byte[] flag = r.flag;
					char[] ch = Util.getChars(cigra,r.len);
					for(int i=0;i<ch.length;i++){
						if(ch[i] == 'M') flag[i]++;
					}
					r.times++;
				}
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算唯一匹配的reads数目
	 */
	public void calculate(){
		//计算唯一匹配的reads数目
		Iterator<ReadsVo> it = map.values().iterator();
		while(it.hasNext()){
			ReadsVo r = it.next();
			byte[] b = r.flag;
			boolean unique = true;
			for(int i=0;i<b.length;i++){   
				if(b[i]>1){        //一旦该reads的某一个位置 匹配了两次 或者两次以上 那么 该reads就不是唯一匹配的了
					unique = false;
					break;
				}
			}
			if(unique) {
				totalUniqueMappedReadsNum++;    //唯一匹配的reads数
				totalMappedReadsLen+=r.len;    //匹配的reads长度和
				uniques.add(r);
				
				int length = (int)(r.len*r.matchics);
				totalMappedReadsBp += length;
				if(divides.get(r.readid)!=null)totalMappedReadsBp+=divides.get(r.readid).intValue();
			}
		}
		
	}
	
	/**
	 * 读取分割后匹配的reads
	 * @param filename
	 */
	public void readsDivideMappedReads(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			str = br.readLine();
			int len;
			float matchics;
			String readid;
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				readid = s.next().substring(0,14);
				for(int i=0;i<4;i++)s.next();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				int value = (int)(len*matchics);
				Integer in = divides.get(readid);
				if(in == null){
					divides.put(readid, value);
				}else{
					divides.put(readid, in.intValue()+value);
				}
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public MapPrecision(){
		readOriginalReads("E:/morehouse/o1o6reads/o6-a.txt");
		String filename = null;
		for(int i=1;i<=23;i++){
System.out.println(i);
			filename = "Chr"+i+"_c+.txt";
			if(i==23)filename = "ChrX_c+.txt";
			readMappedReads("E:/morehouse/z=50/06/"+filename);
			filename = "Chr"+i+"_w+.txt";
			if(i==23)filename = "ChrX_w+.txt";
			readMappedReads("E:/morehouse/z=50/06/"+filename);
		}
		
		for(int i=1;i<=23;i++){
System.out.println(i);
			filename = "Chr"+i+"_c+.txt";
			if(i==23)filename = "ChrX_c+.txt";
			readMpapedFilterReads("E:/morehouse/z=50/06/MUTIMAP_FILTER/"+filename);
			filename = "Chr"+i+"_w+.txt";
			if(i==23)filename = "ChrX_w+.txt";
			readMpapedFilterReads("E:/morehouse/z=50/06/MUTIMAP_FILTER/"+filename);
		}
		
		for(int i=1;i<=23;i++){
System.out.println(i);
			filename = "Chr"+i+"_c+.txt";
			if(i==23)filename = "ChrX_c+.txt";
			readMpapedFilterReads("E:/morehouse/z=50/divide/06/FormatConvert/MUTIMAP_FILTER/"+filename);
			filename = "Chr"+i+"_w+.txt";
			if(i==23)filename = "ChrX_w+.txt";
			readMpapedFilterReads("E:/morehouse/z=50/divide/06/FormatConvert/MUTIMAP_FILTER/"+filename);
		}
		
		calculate();
		
		totalMappedReadsNum = reads.size();
		System.err.println("匹配的reads数目:"+totalMappedReadsNum);
		System.err.println("总的reads数目："+totalReadsNum);
		System.err.println("匹配的reads数目/总的reads数目:"+((float)totalMappedReadsNum/(float)totalReadsNum));
		totalUnMappedReadsNum  = totalReadsNum-totalMappedReadsNum;
		System.err.println("完全未匹配的reads数目:"+totalUnMappedReadsNum);
		System.err.println("未匹配的reads数目/总的reads数目:"+((float)totalUnMappedReadsNum/(float)totalReadsNum));
		System.err.println("唯一匹配的reads数目:"+totalUniqueMappedReadsNum);
		System.err.println("唯一匹配reads数目/总的reads数目"+((float)totalUniqueMappedReadsNum/(float)totalReadsNum));
		System.out.println();
		
		System.err.println("匹配的reads长度:"+totalMappedReadsLen);
		System.err.println("总dreads长度:"+totalReadsLen);
		System.err.println("匹配的reads长度/总的reads长度:"+((float)totalMappedReadsLen/(float)totalReadsLen));
		
		System.out.println();
		System.err.println("匹配的reads bp数总和:"+totalMappedReadsBp);
	}
	
	public static void main(String args[]){
		new MapPrecision();
	}
	

	
}
