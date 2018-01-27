package Task20120228;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import util.FileStreamUtil;

/**
 * 未converted的唯一matched的reads（分割前+分割后）所对应的区间的reference
W+	Non-CG之C个数 (分母)
	CG之C个数
 * @author wuxuehong
 * 2012-3-3
 */
public class ProblemThree_2 {
	//W+
	//non-cg  c
	//cg  c
	int ncg=0;
	int cg=0;
	int rncg=0;
	int rcg=0;
	int totalreads = 0;
	int avireads = 0;
	
	private Map<String, Integer>  map = new HashMap<String,Integer>();
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
					map.put(readid+start, 1);
				}
				str = br.readLine();
			}
			br.close();
			System.out.println("map size;"+map.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 读取匹配后的reads信息
	 * 与watson链匹配的结果  直接计算
	 * @param filename   匹配结果
	 * @param b          标识情况
	 * @param length     染色体长度
	 */
	public void readMappedReads(String filename,String refBase,String outFile,int type){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile), true));
			String str = br.readLine();
			Scanner s = null;
			String readid;
			char[] f;
			char[] c;
			String chrFile,seq,cigra;
			String uniquereads;
			String chr;
			int chrid;
			int start,end,len;
			int offset = 50;
			while(str != null){
				s = new Scanner(str);
				//readid
				readid = s.next();
				//chr
				chr = s.next();
				if(type == RefUtil.NEG)
					chrFile = refBase+"C_hg19_"+chr+".txt";
				else 
					chrFile = refBase+"W_hg19_"+chr+".txt";
				//start
				start = s.nextInt();
				//end
				end = s.nextInt();
				//len
				len = s.nextInt();
				s.next();
				cigra = s.next();
				uniquereads = s.next();
	if(map.get(readid+start) != null){  //说明当前reads是逆转后的那么  不考虑之
//System.out.println("***************"+readid);
		  str = br.readLine();
		  continue;
	}
				//get seq from reference
				seq = RefUtil.refReadsAndFilterRuelst(chrFile, start, end+offset,type);
				
				char[] readsar = uniquereads.toCharArray();
				char[] seqar = seq.toCharArray();
				char[] cig = getChars(cigra, len+offset);
				int of = getOffset(cigra);
				int index = 0;
				
				//reference
				StringBuffer sb1 = new StringBuffer("");
				for(int i=0;i<of;i++)sb1.append("*");
				for(int i=of;i<cig.length;i++){
					if(cig[i]==0)break;
					if(cig[i] == 'I'){
						sb1.append("*");
					}else{
						if(cig[i]=='M' || cig[i]=='D')
							sb1.append(seqar[index++]);
						else
							sb1.append("*");
					}
				}

				//mapping
				StringBuffer sb3 = new StringBuffer("");
				for(int i=0;i<cig.length;i++){
					if(cig[i] == 0) break;
					sb3.append(cig[i]);
				}
				
                //reads
				StringBuffer sb2 = new StringBuffer("");
				index = 0;
				for(int i=0;i<cig.length;i++){
					if(cig[i] == 'D'){
						sb2.append("*");
					}else{
						if(index<readsar.length)
							sb2.append(readsar[index++]);
					}
				}

				
				
				int temprefcg = 0;
				int temprefncg = 0;
                //count reference
				c =  sb1.toString().toCharArray();
				for(int i=0;i<c.length;i++){
					if(c[i] == 'G' ){
						if(i-1>=0 && c[i-1]=='C'){
							temprefcg++;
						}else{
							temprefncg++;
						}
					}
				}
				
				int temprcg = 0;
				int temprncg = 0;
				//count reads
				for(int i=0;i<readsar.length;i++){
					if(cig[i] == 'D') continue;
					if(readsar[i] == 'G' && cig[i] == 'M'){
						 if(i-1>=0 && readsar[i-1]=='C' && cig[i-1]=='M'){
							 temprcg++;
						 }else{
							 temprncg++;
						 }
					}
				}
				for(int i=0;i<cig.length;i++){
					
				}
				
				totalreads++;
				if(temprefncg>=temprncg){
					cg+=temprefcg;
					rcg+=temprcg;
					ncg+=temprefncg;
					rncg+=temprncg;
					
					avireads ++;
					bw.write(readid);
					bw.newLine();
					bw.write(" seq :"+sb1.toString());
					bw.newLine();
					bw.write("match:"+sb3.toString());
					bw.newLine();
					bw.write("reads:"+sb2.toString());
					bw.newLine();
				}
				
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getOffset(String cigra){
		int total = 0;
		char flag = 0;
		char[] c = cigra.toCharArray();
		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				total = total*10+(c[i]-'0');
			}else{
				flag = c[i];
				break;
			}
		}
		if(flag =='M')return 0;
		else return total;
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
				for(int j=index;j<index+count;j++)r[j]='D';
				index+=count;
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='I';
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
	
	
	public ProblemThree_2() {
		// TODO Auto-generated constructor stub
		String readf = "o6";
		String base = "E:/morehouse/"+readf+"/FormatConvert/Fusion/";
		String base2 = "D:/recover/研究生工作/personal_data/repeatchr1-22/WC/";
		//首先标志所有 被bwa逆转的reads
		String bb = "E:/morehouse/"+readf+"/";
		for(int i=1;i<=22;i++){
			readBwaMappedResults(bb+"Chr"+i+"_crick_GA");
		}
		readBwaMappedResults(bb+"ChrX_crick_GA");
		readMappedReads(base+"maxprecision/unique/crick_GA.txt",base2, base+"maxprecision/unique/watson_CT_mapping.txt",RefUtil.POS);
		
		map.clear();
		bb = "E:/morehouse/"+readf+"/FormatConvert/Fusion/unmappble/bwa/";
		for(int i=1;i<=22;i++){
			readBwaMappedResults(bb+"Chr"+i+"_crick_GA");
		}
		readBwaMappedResults(bb+"ChrX_crick_GA");
		readMappedReads(base+"unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/unique/crick_GA.txt", base2,base+"maxprecision/unique/watson_CT_mapping.txt",RefUtil.POS);
		
		System.out.println("Non-cg  c:"+ncg);
		System.out.println("Cg c:"+cg);
		System.out.println("Non-cg  c:"+rncg);
		System.out.println("Cg c:"+rcg);
		System.out.println("total:"+totalreads);
		System.out.println("avi:"+avireads);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ProblemThree_2();
	}

}
