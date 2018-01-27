package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

import Task20120228.ProblemTwo.ReadsVo;

import util.FileStreamUtil;
/**
 *  计算每种匹配情况(分割后的)之下  唯一匹配的reads
 * @author wuxuehong
 * 2012-3-3
 */
public class ProblemTwo_2 {
	
	Set<ReadsVo> set1 = new HashSet<ReadsVo>();
	Set<ReadsVo> set2 = new HashSet<ReadsVo>();
	Set<ReadsVo> set3 = new HashSet<ReadsVo>();
	Set<ReadsVo> set4 = new HashSet<ReadsVo>();
	
	class ReadsVo{
		int times = 0;
		String readid;
		String chr;
		int start;
		int end;
		int len;
		float matchics;
		String cigra;
		public String toString(){
			String s = ""+readid+"\t"+chr+"\t"+start+"\t"+end+"\t"+len+"\t"+matchics+"\t"+cigra+"\t";
			return s;
		}
	}
	//每条reads 对应的 匹配次数
	Map<String,ReadsVo> map = new HashMap<String, ReadsVo>();
	
	Map<String, String> id2seq = new HashMap<String,String>();
	
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOringinalReads(String filename){
		try{
			String readid="", seq="";
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")){
					readid = str.substring(1);
					seq = br.readLine();
					id2seq.put(readid, seq);
				}
				str = br.readLine();
			}
			br.close();
	System.out.println("Reads size:"+id2seq.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * read File
	 * @param filename
	 * @param set
	 */
	public void readFile(String filename,Set<ReadsVo> set){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid,chr,cigra;
			int start,end,len;
			float matchics;
			while(str != null){
				s = new Scanner(str);
				//readid
				readid = s.next();
				s.next();
				//chr
				chr = s.next();
				start = s.nextInt();
				end = s.nextInt();
				len = s.nextInt();
				s.next();
				matchics = Float.parseFloat(s.next());
				cigra = s.next();
				
				ReadsVo reads = map.get(readid);
				if(reads == null){
					reads = new ReadsVo();
					reads.times = 1;
					reads.readid = readid;
					reads.chr = chr;
					reads.start = start;
					reads.end = end;
					reads.len = len;
					reads.matchics = matchics;
					reads.cigra = cigra;
					set.add(reads);
					map.put(readid, reads);
				}else{
					reads.times++;
				}
				str = br.readLine();	
			}
			br.close();
			System.out.println("map size:"+map.size());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 写文件
	 * @param filename
	 * @param set
	 */
	int total = 0;
	public void writeFile(String filename, Set<ReadsVo> set){
		try{
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(filename);
			Iterator it = set.iterator();
			ReadsVo reads;
			while(it.hasNext()){
				reads = (ReadsVo) it.next();
				String readid = reads.readid;
				if(reads.times == 1){
						total++;
					 bw.write(reads.toString()+id2seq.get(readid));
					 bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			System.out.println("unique reads:"+total);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProblemTwo_2(){
		String base = "E:/morehouse/o6/FormatConvert/Fusion/";
		readOringinalReads(base+"unmapped/crick_CT.txt");
		readOringinalReads(base+"unmapped/crick_GA.txt");
		readOringinalReads(base+"unmapped/watson_CT.txt");
		readOringinalReads(base+"unmapped/watson_GA.txt");
		
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_CT.txt", set1);
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_GA.txt", set2);
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_CT.txt", set3);
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_GA.txt", set4);
		
		writeFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/crick_CT.txt", set1);
		writeFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/crick_GA.txt", set2);
		writeFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/watson_CT.txt", set3);
		writeFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/watson_GA.txt", set4);
	}
	
	public static void main(String args[]){
		new ProblemTwo_2();
	}

}
