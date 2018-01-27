package ForDivide_SoftAnalysisOfGenomicMethylationOfSingleCell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
(1). 下载repeat masker类型，如果整个reads的位置落入了alu, simple repeats, ERVL-MaLR和low-complexity区域，去除该reads.
(2). 合并I和D: D不数，I数（比如34M1D66M=100M, 12M2I56M=70M）
(3).根据reads中M的位置去除纯alu, simple repeats，ERVL-MaLR和low-complexity, 并且向外延伸，两边相加小于20bp的非alu, simple repeats，ERVL-MaLR和low-complexity的reads也去除。
(4). 输出line类型（line是repeat的一种），在（1）过滤后文件Z=50文件中分别列出1列。根据M的位置filter L1HS, L1MXX and L1PXX 类型的reads，并且向外延伸，两边相加小于20bp的非L1HS, L1MXX and L1PXX的reads也去除。
(5). 下载segmental duplicate data, 根据M的位置过滤掉segmental duplicate，并且向外延伸，两边相加小于20bp的非segmental duplicate的reads也去除。
(6). 到ucsc中，tables下载CNV，根据M的位置过滤掉CNV，并且向外延伸，两边相加小于20bp的非CNV的reads也去除。（下载CNV: ucsc---tables: clade: mammal; genome: human; assembly: Feb. 2009 (GRCh37/hg19); group: variation and repeats; track: DGV struct Var; table: dgv; region：genome）
 * @author wuxuehong
 * 2012-5-13
 */
public class RepeatFilterForEachChr {
	
	private int chrLen;    //染色体长度
	private String readsFile;
	private String repeatFile;
	private String segmentFile;
	private String CNVFile;
	private String outFile;
	private byte[] flag;
	private byte[] flag2;
	private String[] line = new String[1000];
	private Map<String,Integer> map = new HashMap<String,Integer>();
	private int style;
	
	
	public RepeatFilterForEachChr(int chrLen, String readsFile, String repeatFile, String segmentFile, String CNVFile, String outFile, int style){
		this.chrLen = chrLen;
		this.repeatFile = repeatFile;
		this.readsFile = readsFile;
		this.outFile = outFile;
		this.CNVFile = CNVFile;
		this.segmentFile = segmentFile;
		this.style = style;
		flag = new byte[chrLen];
		flag2 = new byte[chrLen];
		readsRepeats();
		filterReads();
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
			int index = 5;
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
				if(family.equals("Alu") || family.equals("Simple_repeat") || family.equals("ERVL-MaLR") || family.equals("Low_complexity") || family.equals("ERV1") || family.equals("ERVL")){
					for(int i=start;i<=end;i++) flag[i] = 1;
				}
				
				//标记LINE类型
				//将L1HS  L1MXX  L1PXX 位置标记为1  (最终用于过滤)
				//其余的标记为5+  
				if(classr.equals("LINE")){
					if(name.equals("L1HS") || name.startsWith("L1M") || name.startsWith("L1P")){
						for(int i=start;i<=end;i++)flag[i] = 1;
					}else{
						Integer intvalue = map.get(name);
						if(intvalue != null){
							for(int i=start;i<=end;i++)
								flag2[i] = intvalue.byteValue();	
						}else{
							index++;
							for(int i=start;i<=end;i++){
								flag2[i] = (byte)index;
							}
							line[index] = name;
							map.put(name, index);
						}
					}
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
	
	private void filterReads(){
		String str = null;
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(readsFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String head = br.readLine();
			bw.write(head);
			bw.newLine();
			str = br.readLine();
			int start,end,len;
			String cigra;
			Scanner s = null;
			int maxLen = 20;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();  //匹配的开始位置
				end = s.nextInt();   //匹配的结束位置
				len = s.nextInt();   //reads长度
				s.next();s.next();
				cigra = s.next();  //匹配详细信息
				
				boolean filter = true; //初始化该reads是要被过滤的
				//查看整个reads是否落入了 repeat区域  如果落入了则过滤掉 否则暂时保留
				for(int i=start;i<=end;i++){
					if(flag[i] != 1) {
						filter = false;
						break;
					}
				}
				
				char[] c = getChars(cigra, len);
				//如果reads没有全部落入repeat区域  则查看 reads M 的部分是否落入了repeat区域
				
				try{
				if(!filter){
					filter = true; //依然将其初始为是要过滤掉的
					int count = 0; //计算M的部分没有落入
					for(int i=start;i<=end;i++){
						if(c[i-start] == 'M' && flag[i] != 1){ //统计reads中 M部分非 alu simple repeats,...四种repeats 的reads长度
							count++;
						}
					}
					if(count>=maxLen)filter = false;
				}
				}catch (Exception e) {
					System.err.println(str);
					str = br.readLine();
					continue;
				}
				
				//如果reads M部分依然没有落入四种repeats区间里面 则输出该reads  并且标记出LINE类型
				if(!filter){
					bw.write(str+"\t");
					bw.write(merge(c)+"\t");
					int cur = -1;
					boolean isline = true;  //默认为Line类型
					boolean first = true; //是否第一次遇到M
					for(int i=start;i<=end;i++){
						if(flag2[i] >= 5 && c[i-start] == 'M'){    //标志位大于等于5的才是line类型
							if(first){
								cur = flag2[i];
								first = false;
							}else if(flag2[i] != cur){
								isline = false;   //如果该reads中不是同一种类型
								break;
							}
						}
						if(flag2[i] < 5){
							isline = false;
							break;
						}
					}
					if(isline) bw.write(line[cur]);
					bw.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch (Exception e) {
			System.err.println(str);
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
	
	private String merge(char[] cigra){
		StringBuffer sb = new StringBuffer();
		char upchar = 'A';
		int count = 0;
		for(int i=0;i<cigra.length;i++){
			char cur = cigra[i];
			if(cur != upchar){
				if(count != 0){
					sb.append(count+""+upchar);
				}
				upchar = cur;
				count = 1;
			}else{
				count++;
			}
		}
		sb.append(count+""+upchar);
		return sb.toString();
	}

}
