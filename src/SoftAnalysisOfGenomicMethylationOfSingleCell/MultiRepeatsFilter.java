package SoftAnalysisOfGenomicMethylationOfSingleCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * b 中M的部分为alu+L1M4c, 如果M的部分是2个或2个以上删除的repeat的组合，删除b。
 * @author wuxuehong
 * 2012-5-13
 */
public class MultiRepeatsFilter {
	
	private int chrLen;    //染色体长度
	private String readsFile;
	private String repeatFile;
	private String segmentFile;
	private String CNVFile;
	private String outFile;
	private short[] flag;
	private int style;
	
	public MultiRepeatsFilter(int chrLen,String readsFile, String repeatFile, String segmengFile, String CNVFile,String outFile,int style){
		this.chrLen = chrLen;
		this.readsFile = readsFile;
		this.repeatFile = repeatFile;
		this.segmentFile = segmengFile;
		this.CNVFile = CNVFile;
		this.outFile = outFile;
		this.style = style;
		flag = new short[chrLen];
		readRepeat();
		filterReads();
	}
	
	private void readRepeat(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(repeatFile)));
			String str = br.readLine();
			str = br.readLine();
			Scanner s = null;
			int start,end,tempStart,tempEnd;
			String family;
			String classr;
			String name;
			Map<String,Integer> map = new HashMap<String,Integer>();
			int index = 1;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				if(style == Main.CRICK){
					tempStart = s.nextInt();
					tempEnd = s.nextInt();
					start = chrLen-tempEnd+1;
					end = chrLen-tempStart+1;
				}else{
					start = s.nextInt();
					end = s.nextInt();
				}
				s.next();s.next();
				name = s.next();
				classr = s.next();
				family = s.next();
				
				//过滤一下四种repeats
				if(family.equals("Alu") || family.equals("Simple_repeat") || family.equals("ERVL-MaLR") || family.equals("Low_complexity") || name.equals("L1HS") || family.equals("ERV1") || family.equals("ERVL") || name.startsWith("L1M") || name.startsWith("L1P")){
					Integer intvalue = map.get(name);
					if(intvalue == null){
						index ++;
						intvalue = index;
						map.put(name, index);
					}
					for(int i=start;i<=end;i++){
						flag[i] = intvalue.shortValue();
					}
				}
				str = br.readLine();
			}
			
			//读取segmental duplicate repeats
			br = new BufferedReader(new FileReader(new File(segmentFile)));
			str = br.readLine(); //忽略第一行
			str = br.readLine();
			index++;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();
				if(style == Main.CRICK){
					tempStart = s.nextInt();
					tempEnd = s.nextInt();
					start = chrLen-tempEnd+1;
					end = chrLen-tempStart+1;
				}else{
					start = s.nextInt();
					end = s.nextInt();
				}
				for(int i=start;i<=end;i++)flag[i] = (short)index;
				str = br.readLine();
			}
			br.close();
			
			//读取CNV repeats
			br = new BufferedReader(new FileReader(new File(CNVFile)));
			str = br.readLine();
			str = br.readLine();
			index++;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();
				if(style == Main.CRICK){
					tempStart = s.nextInt();
					tempEnd = s.nextInt();
					start = chrLen-tempEnd+1;
					end = chrLen-tempStart+1;
				}else{
					start = s.nextInt();
					end = s.nextInt();
				}
				for(int i=start;i<=end;i++)flag[i] = (short)index;
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void filterReads(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(readsFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			bw.write(str);
			bw.newLine();
			str = br.readLine();
			Scanner s = null;
			int start,end,len;
			String cigra;
			while(str != null){
				s  = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
				len = s.nextInt();
				s.next();s.next();
				cigra = s.next();
				
				if(end >= chrLen) {
					System.err.println(str);
					str = br.readLine();
					continue;
				}
				boolean filter = false; //初始化
				char[] c = getChars(cigra, len);
				short cur = -1;
				int count = 0;
				for(int i=start;i<=end;i++){
					if(flag[i] == 0) continue;
					if(c[i-start] == 'M'){
						if(flag[i] != cur){
							count++;
							cur = flag[i];
						}
					}
				}
				if(count > 1){
					filter = true;
				}
				if(!filter){
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	                         
	/**
	 * 将reads匹配的详细信息转换成 字符数组  
	 * 其中D忽略
	 * I转换成M
	 * @param cigra
	 * @param readslen
	 * @return
	 */
	private char[] getChars(String cigra,int readslen){
		int totalD = 0;
	  try{
		char[] c = cigra.toCharArray();
		char[] r = new char[readslen];
		int index = 0;
		int count = 0;
 		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				count = count*10+c[i]-'0';
			}else if(c[i]=='M'){
				for(int j=index;j<index+count;j++)r[j]='M';
				index+=count;
				count=0;
			}else if(c[i]=='D'){
				//for(int j=index;j<index+count;j++)r[j]='D';
				//index+=count;
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='M';
				index+=count;
				count=0;
			}else if(c[i]=='S'){
				for(int j=index;j<index+count;j++)r[j]='S';
				index+=count;
				count=0;
			}else 
				System.out.println("exception !!!!");
		}
		return r;
	  }catch(Exception e){
		  System.out.println(cigra+"\t"+readslen);
		  System.out.println("Total D$$$$$$$$$$$$$$$:"+totalD);
		  return null;
	  }
	}

}
