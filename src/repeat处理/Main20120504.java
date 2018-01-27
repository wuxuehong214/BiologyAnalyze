package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A和B是一个reads不同转换而成的 来源于a或b， A matched的长度大于B， A和B有overlap， B没有被overlap的部分<=6bp.请删除B
 * @author wuxuehong
 * 2012-5-4
 */
public class Main20120504 {
	
	byte[] flag = new byte[51304567]; 
	
	public void readsFilter(String inputFile, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine(); //忽视第一行表头
			str = br.readLine();
			Scanner s = null;
			Reads last;
			Reads cur;
			List<Reads> list = new ArrayList<Reads>();
			while(str != null){
				s = new Scanner(str);
				cur = new Reads();
				cur.readid = s.next();
				
				if(cur.readid.equals("HILT7XA01A02BKa")){
					System.out.println("abc");
				}
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
						
						//将上一条reads M的部分置为1
						  for(int j=0;j<lastc.length;j++){
							  if(lastc[j] == 'M'){
								  flag[j+last.start] = 1;
							  }
						  }
						  //将当前reads M的部分置为2
						  for(int j=0;j<curc.length;j++){
							  if(curc[j] == 'M'){
								  flag[j+cur.start] += 2;
							  }
						  }
						  
						  //分别计算上一条reads 以及当前reads M的并且没有被overlap部分   
						  int lastCount = 0, curCount = 0;
						  for(int j=last.start;j<=last.end;j++){
							  if(flag[j] == 1) lastCount++;
						  }
						  for(int j=cur.start;j<=cur.end;j++){
							  if(flag[j] == 2) curCount++;
						  }
						  
						  //初始化 是否要删除上一条reads 还是当前reads   默认为都不删除
						  boolean lastDel = false, curDel = false;
						  //找非overlap最小的   如果没有被overlapp的部分小于6了 那么删除   
						  if(lastCount<=curCount){
							  if(lastCount <=6) lastDel = true;
						  }else{
							  if(curCount <=6) curDel = true;
						  }
						  //将flag标志位 置为0 初始值
						  for(int j=last.start;j<=last.end;j++){
							  flag[j] = 0;
						  }
						  for(int j=cur.start;j<=cur.end;j++){
							  flag[j] = 0;
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
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main20120504(){
		String base = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/L1PA3L1M4c_filter/CNV_filter/";
		readsFilter(base+"Chr22_w+_CNV_filter.txt", base+"ABfilter/Chr22_w+_AB_filter.txt");
	}
	
	public static void main(String args[]){
		new Main20120504();
	}
	
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
		last = last.substring(0, last.length()-1);
		cur = cur.substring(0, cur.length()-1);
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
}
