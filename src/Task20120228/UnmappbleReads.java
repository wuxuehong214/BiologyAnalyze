package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 输出unmappblereads信息
 * @author wuxuehong
 * 2012-1-13
 */
public class UnmappbleReads {

	private Map<String,Integer> mappble = new HashMap<String, Integer>(); 
	
	/**
	 * reads信息
	 */
	private Map<String,String> readid2seq = new HashMap<String,String>();
	/**
	 * 读取已经map的reads信息
	 * @param filename
	 */
	public void readMappbleReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid = null;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				mappble.put(readid, 0);
				str = br.readLine();
			}
			br.close();
//			System.out.println("mapped reads size;"+mappble.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOriginreads(String filename){
		try{
			int count = 0;
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			String readid="",seq="";
			while(str != null){
				if(str.startsWith(">")){
					count++;
					if(seq.length()!=0){
						readid2seq.put(readid, seq);
					}
					readid = str.substring(1,str.indexOf("rank")-1);
					seq = "";
				}else{
					seq += str.trim();
				}
				str = br.readLine();
			}
			if(seq.length()!=0)
				readid2seq.put(readid, seq);
//		   System.out.println("Total reads size:"+count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取原始 CT/GA转换后的reads信息
	 * @param filename
	 */
	public void outputReads(String outFile){
		int count = 0;
		try{
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			Iterator<String> it = readid2seq.keySet().iterator();
			String readid = null;
			String seq = null;
			while(it.hasNext())
			{
				readid = it.next();
				if(mappble.get(readid)==null){
					count++;
					seq = readid2seq.get(readid);
					List<String> list = divideReads(seq, 20, 50);
					for(int i=0;i<list.size();i++){
						bw.write(">"+readid+"_"+(i+1));
						bw.newLine();
						bw.write(list.get(i));
						bw.newLine();
					}
				}
			}
			bw.flush();
			bw.close();
//System.out.println("unmappble reads size:"+count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public List<String> divideReads(String seq, int overlap, int eachLen){
		List<String> list = new ArrayList<String>();
		while(seq.length()>=eachLen*2-overlap){
			list.add(seq.substring(0,eachLen));
			seq = seq.substring(eachLen-overlap);
		}
		list.add(seq);
		return list;
	}
	
	public UnmappbleReads() {
		
		String base = "E:/morehouse/o1/FormatConvert/Fusion/";
		
		readOriginreads(base+"o1.GAC.454Reads.fa");
		
		readMappbleReads(base+"maxprecision/watson_CT.txt");
		outputReads(base+"unmapped/watson_CT.txt");
		
		mappble.clear();
		readMappbleReads(base+"maxprecision/crick_CT.txt");
		outputReads(base+"unmapped/crick_CT.txt");
		
		readMappbleReads(base+"maxprecision/watson_GA.txt");
		outputReads(base+"unmapped/watson_GA.txt");
		
		mappble.clear();
		readMappbleReads(base+"maxprecision/crick_GA.txt");
		outputReads(base+"unmapped/crick_GA.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UnmappbleReads();
	}

}
