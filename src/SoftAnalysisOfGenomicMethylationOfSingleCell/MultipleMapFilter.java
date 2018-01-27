package SoftAnalysisOfGenomicMethylationOfSingleCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * If multiple mapping, 则留与那个与repeatmasker（任何）/CNV/segmental dups中一个overlap少的那个reads
 * @author wuxuehong
 * 2012-5-18
 */
public class MultipleMapFilter {

	private int chrLen;    //染色体长度
	private String readsFile;
	private String repeatFile;
	private String segmentFile;
	private String CNVFile;
	private String outFile;
	private byte[] flag;
	private int style;
	
	public MultipleMapFilter(int chrLen, String readsFile, String repeatFile, String segmentFile, String CNVFile, String outFile, int style){
		this.chrLen = chrLen;
		this.repeatFile = repeatFile;
		this.readsFile = readsFile;
		this.outFile = outFile;
		this.CNVFile = CNVFile;
		this.segmentFile = segmentFile;
		flag = new byte[chrLen];
		this.style = style;
		
		readsRepeats();//读取reapeats
		readsFilter(); //过滤 
	}
	
	/**
	 * 读取repeats数据信息
	 * @param repeatFile
	 */
	private void readsRepeats(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(repeatFile)));
			String str = br.readLine(); //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int start ,end ,tempStart, tempEnd;
			String family;
			String classr;
			String name;
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
				for(int i=0;i<2;i++)s.next();
				name = s.next();
				classr = s.next();
				family = s.next();
				//过滤一下四种repeats
				if(family.equals("Alu") || family.equals("Simple_repeat") || family.equals("ERVL-MaLR") || family.equals("Low_complexity") || family.equals("ERV1") || family.equals("ERVL") || name.equals("L1HS") || name.startsWith("L1M") || name.startsWith("L1P")){
					for(int i=start;i<=end;i++) flag[i] = 1;
				}
				str = br.readLine();
			}
			
			//读取segmental duplicate repeats
			br = new BufferedReader(new FileReader(new File(segmentFile)));
			str = br.readLine(); //忽略第一行
			str = br.readLine();
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
				for(int i=start;i<=end;i++)flag[i] = 1;
				str = br.readLine();
			}
			br.close();
			
			//读取CNV repeats
			br = new BufferedReader(new FileReader(new File(CNVFile)));
			str = br.readLine();
			str = br.readLine();
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
				for(int i=start;i<=end;i++)flag[i] = 1;
				str = br.readLine();
			}
			br.close();       
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void readsFilter(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(readsFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			bw.write(str);
			bw.newLine();
			str = br.readLine();
			Scanner s = null;
			Reads last;
			Reads cur;
			List<Reads> list = new ArrayList<Reads>();
			while(str != null){
				s = new Scanner(str);
				cur = new Reads();
				cur.readid = s.next();
				
				s.next();s.next();
				cur.start = s.nextInt();
				cur.end = s.nextInt();
				cur.len = s.nextInt();
				s.next();s.next();
				cur.cigra = s.next();
				cur.str = str;
				
				if(list.size() == 0){//如果当前待比较队列为null 则直接将当前reads加入待分析队列 (一般仅用于初始化状态下 待分析队列为null)
					list.add(cur);
				}else if (list.size() != 0 && isSame(cur.readid, list)){ //如果当前待分析队列非null 并且当前reads与其相同 则分析之
					               
					boolean addCur = true; //是否要将当前reads 添加到待分析队列    当且仅当当前reads在与所有待分析reads比较后没被删除 才会添加到待分析队列
					for(int i=0;i<list.size();i++){
						last = list.get(i);
						//获取上一条reads详细匹配情况
						char[] lastc = getChars(last.cigra, last.len);
						char[] curc = getChars(cur.cigra, cur.len);
						
						if(last.len != cur.len){
							System.out.println(last.str);
							System.out.println(cur.str);
							System.err.println("warning.............");
						}
						
						int lastC=0,curC=0;
						for(int j=0;j<lastc.length;j++){
							if(lastc[j] == 'M' && flag[j+last.start]==1)lastC++;
						}
						for(int j=0;j<curc.length;j++){
							if(curc[j] == 'M' && flag[j+cur.start]==1)curC++;
						}
						  
					  //初始化 是否要删除上一条reads 还是当前reads   默认为都不删除
					  boolean lastDel = false, curDel = false;
					  
					  //与repeat overlap 多的 则删除
					  if(lastC<curC){
						    curDel = true;
					  }else{
						  lastDel = true;
					  }
					  if(lastDel) //删除上一reads
						  list.remove(last);
					  if(curDel){  //删除当前reads
						  addCur = false; //表示 无需将当前reads添加到待分析队列了
						  break;//一旦删除当前reads则跳出循环
					  }
					}
					if(addCur) list.add(cur);
				}else if (list.size() != 0 && !isSame(cur.readid, list)){  //如果当前待分析队列非null 并且当前reads与其不同  则输出待分析队列中数据 ，并且将当前reads加入分析队列
					for(int i=0;i<list.size();i++){
						bw.write(list.get(i).str);
						bw.newLine();
					}
					list.clear();
					//将当前reads加入待分析队列
					list.add(cur);
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 比较当前readsid是否与之前分析的readsid相同
	 * @param cur
	 * @param list
	 * @return
	 */
	private boolean isSame(String cur, List<Reads> list){
		String last = list.get(0).readid;
		last = last.substring(0, 14);
		cur = cur.substring(0, 14);
		return last.equals(cur);
	}
	
	class Reads{
		String readid = null;  //reads ID
		int start = 0;   //起始位置
		int end = 0;     //结束位置
		int len = 0;     //reads长度
		String cigra = null;  //匹配信息
		String str = null;   //reads信息
	}
	/**
	 * 获取每条reads的匹配详情
	 * @param cigra
	 * @param readslen
	 * @return
	 */
	public char[] getChars(String cigra,int readslen){
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
