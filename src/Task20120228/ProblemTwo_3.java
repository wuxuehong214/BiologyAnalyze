package Task20120228;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * Waston	W+	Non-CG之C个数
		CG之C个0数
	W-	Non-CG之G个数
		CG之G个数
Crick	C+	Non-CG之C个数
		CG之C个数
	C-	Non-CG之G个数
		CG之G个数
 * @author wuxuehong
 * 2012-3-3
 */
public class ProblemTwo_3 {
	
	//watson_ct w+
	//watson_ga w-
	//crick_ct c+
	//crick_ga c-
	
	int wncgc = 0; // Non-CG之C个数     W+
	int wcgc = 0;  // CG之C个数      W+
	
	int wncgg = 0; // Non-CG之G个数  W-
	int wcgg = 0; // CG之G个数    W-
	
	private Map<String, Integer>  map2 = new HashMap<String,Integer>();
	public void readBwaMappedResults(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			str = br.readLine();
			Scanner s = null;
			String readid;
			String start;
			int flag;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				flag = s.nextInt();
				s.next();
				start = s.next();
				if(flag == 16){ //reserved by bwa
					map2.put(readid+start, 1);
				}
				str = br.readLine();
			}
			br.close();
			System.out.println("map size;"+map2.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Map<String,ReadsVo> map = new HashMap<String,ReadsVo>();
	class ReadsVo{
		String readid;
		int start;
		int end;
		int len;
		String chr;
		String cigra;
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
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='I';
				index+=count;
				totalD+=count;
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
	 * non cg 之c
	 * cg 之c
	 * @param filename
	 */
	public void readUniqueMappedReadsC(String filename){
		char[]c=null,flag = null;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String seq,cigra;
			int len;
			int start;
			String readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				start = s.nextInt();
		if(map2.get(readid+start) != null){ //说明这个是bwa逆转后的  假match
			str = br.readLine();
			continue;
		}
				s.next();
				len = s.nextInt();
				s.next();
				cigra = s.next();
				seq = s.next();
				flag = getChars(cigra, len);
				c = seq.toCharArray();
				for(int i=0;i<c.length;i++){
					if(c[i] == 'C' && flag[i]=='M'){
						if(i+1<c.length && c[i+1]=='G'&&flag[i+1]=='M'){
							wcgc++;
						}else{
							wncgc++;
						}
					}
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * non cg 之g
	 * cg 之g
	 * @param filename
	 */
	public void readUniqueMappedReadsG(String filename){
		char[]c=null,flag = null;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String seq,cigra;
			int len;
			int start;
			String readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				start = s.nextInt();
				if(map2.get(readid+start) != null){ //说明这个是bwa逆转后的  假match
					str = br.readLine();
					continue;
				}
				s.next();
				len = s.nextInt();
				s.next();
				cigra = s.next();
				seq = s.next();
				flag = getChars(cigra, len);
				c = seq.toCharArray();
				for(int i=0;i<c.length;i++){
					if(c[i] == 'G' && flag[i]=='M'){
						if(i-1>=0 && c[i-1]=='C' && flag[i-1]=='M'){
							wcgg++;
						}else
							wncgg++;
					}
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProblemTwo_3(){
		String readf = "o1";
		//首先标志所有 被bwa逆转的reads
		String bb = "D:/recover/研究生工作/personal_data/"+readf+".GAC.454Reads/bwa匹配结果/";
		String base = "D:/recover/研究生工作/personal_data/"+readf+".GAC.454Reads/bwa匹配结果/FormatConvert/Fusion/";
		for(int i=1;i<=22;i++){
			readBwaMappedResults(bb+"Chr"+i+"_watson_CT");
		}
		readBwaMappedResults(bb+"ChrX_watson_CT");
		
		//W+
		//non cg c 
		// cg c
		readUniqueMappedReadsC(base+"maxprecision/unique/watson_CT.txt");
		readUniqueMappedReadsC(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/watson_CT.txt");
		System.out.println("W+/C+:non cg c:"+wncgc);
		System.out.println("W+/C+:cg c:"+wcgc);
		
		//W-  /C-
		//non cg g
		//cg g
//		readUniqueMappedReadsG(base+"maxprecision/unique/crick_CT.txt");
//		readUniqueMappedReadsG(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/crick_CT.txt");
//		System.out.println("W-/C-:non cg G:"+wncgg);
//		System.out.println("W-/C-:cg G:"+wcgg);
		
		
	}
	
	public static void main(String args[]){
		new ProblemTwo_3();
	}

}
