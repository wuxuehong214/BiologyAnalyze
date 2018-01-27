package 最新分析20120225;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 分割后 匹配的reads数 以及唯一匹配的reads数目
 * @author wuxuehong
 * 2012-2-28
 */
public class Validate {

	class ReadsVo{
		int count = 0;
	}
	Map<String,ReadsVo> map = new HashMap<String,ReadsVo>();
	
	public void readFile(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			ReadsVo reads;
			String readid;
			while(str != null){
				 s = new Scanner(str);
				 readid = s.next();
				 reads = map.get(readid);
				 if(reads == null){
					 reads = new ReadsVo();
					 reads.count = 1;
					 map.put(readid, reads);
				 }else{
					 reads.count++;
				 }
				 str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Validate(){
		readFile("unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_CT.txt");
		readFile("unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_GA.txt");
		readFile("unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_CT.txt");
		readFile("unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_GA.txt");
		
		int mapp = 0,umap = 0;
		Iterator<ReadsVo> it = map.values().iterator();
		ReadsVo reads;
		while(it.hasNext()){
			reads = it.next();
			mapp++;
			if(reads.count == 1) umap++;
		}
		System.out.println("mapped small reads:"+mapp);
		System.out.println("unique mapped small reads:"+umap);
	}
	
	public static void main(String ars[]){
		new Validate();
	}
}
