package SoftAnalysisOfGenomicMethylationOfSingleCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.cert.CertPathValidatorException.Reason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * A和B是一个read的不同转换而成的，来源于A或B, A matched的长度大于B, A和B有overlap, B没有被overlap的部分<=15bp,请删除B
 * @author wuxuehong
 * 2012-5-13
 */
public class OverlapFilter {

	private int chrLen;    //染色体长度
	private String readsFile;
	private String outFile;
	private List<Read> reads = new ArrayList<Read>();
	private String head;
	
	public OverlapFilter(int chrLen,String readsFile, String outFile){
		this.chrLen = chrLen;
		this.readsFile = readsFile;
		this.outFile = outFile;
		sortReads();
		readsFilter();
	}
	
	private void sortReads(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(readsFile)));
			head = br.readLine();
			String str = br.readLine();
			Scanner s = null;
			String readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				Read r = new Read();
				r.readid = readid;
				r.str = str;
				reads.add(r);
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(reads);
	}
	
	private void readsFilter(){
		try{
			Iterator<Read> it = reads.iterator();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			bw.write(head);
			bw.newLine();
			Scanner s = null;
			Reads last;
			Reads cur;
			List<Reads> list = new ArrayList<Reads>();
			Read read;
			while(it.hasNext()){
				read = it.next();
				s = new Scanner(read.str);
				cur = new Reads();
				cur.readid = s.next();
				
				s.next();s.next();
				cur.start = s.nextInt();
				cur.end = s.nextInt();
				cur.len = s.nextInt();
				s.next();s.next();
				cur.cigra = s.next();
				cur.str = read.str;
				
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
						byte[] flag = new byte[last.len];
						//将上一条reads M的部分置为1
						  for(int j=0;j<last.len;j++){
							  if(lastc[j] == 'M'){
								  flag[j] = 1;
							  }
						  }
						  //将当前reads M的部分置为2
						  for(int j=0;j<last.len;j++){
							  if(curc[j] == 'M'){
								  flag[j] += 2;
							  }
						  }
						  
						  //分别计算上一条reads 以及当前reads M的并且没有被overlap部分   
						  int lastCount = 0, curCount = 0;
						  for(int j=0;j<last.len;j++){
							  if(flag[j] == 1) lastCount++;
						  }
						  for(int j=0;j<last.len;j++){
							  if(flag[j] == 2) curCount++;
						  }
						  
						  //初始化 是否要删除上一条reads 还是当前reads   默认为都不删除
						  boolean lastDel = false, curDel = false;
						  //找非overlap最小的   如果没有被overlapp的部分小于6了 那么删除   
						  if(lastCount<=curCount){
							  if(lastCount <=20) lastDel = true;
						  }else{
							  if(curCount <=20) curDel = true;
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
			}
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
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
	
	class Read implements Comparable<Read>{
		String readid;
		String str;
		@Override
		public int compareTo(Read o) {
			return this.readid.compareTo(o.readid);
		}
	}
	
	public static void main(String args[])
	{
		String last = "HILT7XA01A00AKb";
		last = last.substring(0, 14);
		System.out.println(last);
	}
	
	
}
