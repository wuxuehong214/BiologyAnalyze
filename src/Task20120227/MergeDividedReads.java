package Task20120227;
import java.awt.image.SampleModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

import util.FileStreamUtil;
/**
 * 分析对象： 分割后的小reads
 * 分割规则： 如果一条分割的reads 中 有连续两个小reads在同一染色体上有overlap 那么将其合并
 * @author wuxuehong
 * 2012-2-28
 */
public class MergeDividedReads {
	
	Map<String,List<SmallReads>>  map = new HashMap<String,List<SmallReads>>();
 	
	class SmallReads implements Comparable<SmallReads>{
		String readid;   //小reads的ID
		int index;       //小reads的索引编号
		int chr;        //染色体编号
		int start;
		int end;
		boolean isMerge = false;
		@Override
		public int compareTo(SmallReads sr) {
			// TODO Auto-generated method stub
			return index-sr.index;
		}
		public String toString(){
			return index+"";
		}
	}
	
	/**
	 * read files
	 * @param filename
	 */
	public void readFile(String filename){		
	try{
		BufferedReader br = FileStreamUtil.getBufferedReader(filename);
		String str = br.readLine();
		Scanner s = null;
		String readsid;
		String sid;
		String chrs;
		int chr;
		int start;
		int end;
		int index;
		while(str != null){
			s = new Scanner(str);
			sid = s.next();
			s.next();
			chrs = s.next();
			if(chrs.endsWith("X"))
				chr = 23;
			else
				chr = Integer.parseInt(chrs.substring(3));
			start = s.nextInt();
			end = s.nextInt();
			//deal
			index = Integer.parseInt(sid.substring(sid.indexOf('_')+1));
			readsid = sid.substring(0, sid.indexOf('_'));
			List<SmallReads> list = map.get(readsid);
			if(list == null){
				list = new ArrayList<SmallReads>();
				map.put(readsid, list);
			}
			SmallReads sr = new SmallReads();
			sr.readid = sid;
			sr.chr = chr;
			sr.start = start;
			sr.end = end;
			sr.index = index;
			list.add(sr);
			str = br.readLine();
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public MergeDividedReads(){
		String base = "D:/recover/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/";
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_CT.txt");
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/crick_GA.txt");
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_CT.txt");
		readFile(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/watson_GA.txt");

		try{
		BufferedWriter bw = FileStreamUtil.getBufferedWriter("d:/out.txt");
		Iterator<String> it = map.keySet().iterator();
		String readid;
		List<SmallReads> list;
		while(it.hasNext()){
			readid = it.next();
			list = map.get(readid);
			Collections.sort(list);
			bw.write(readid+":\t");
			bw.newLine();
			bw.write(list.toString());
			bw.newLine();
			int min = list.get(0).index;
			int max = list.get(list.size()-1).index;
			SmallReads sr = null;
			for(int k=min;k<=max;k++){
				int maxx = 1;
				boolean have = false;
				for(int a=0;a<list.size();a++){
					sr = list.get(a);
					if(sr.index == k){
						have = true;
						int b = dfs(sr,list);
	if(b>=2){
		System.out.println("**********************************************"+readid);
	}
						if(b>maxx)maxx = b;
					}
				}
				
				if(have){
					bw.write("("+k);
					for(int j=0;j<maxx-1;j++){
						bw.write(","+(k+j+1));
					}
					bw.write(")\t");
				if(maxx>1){
					k+=(maxx-1);
				}
				}
			}
			bw.newLine();
		}
		bw.flush();
		bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int dfs(SmallReads sr,List<SmallReads> list){
		for(int i=0;i<list.size();i++){
			SmallReads temp = list.get(i);
			if(temp.index == sr.index+1){  //连续
				if(temp.chr == sr.chr){
					if((temp.start>=sr.end && temp.start<=sr.end)||(temp.end>=sr.start && temp.end<=sr.end)){
						return 1+dfs(temp,list);
					}
				}
			}
		}
		return 1;
	}
	
	
	public static void main(String args[]){
		new MergeDividedReads();
	}

}
