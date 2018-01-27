package statics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * Consisitent率
 * 计算REF上每个CG下匹配的reads 的数目中， 甲基化的比例
 * @author wuxuehong
 * 2012-1-10
 */
public class Consisitent {
	
	//CA consistent: 0.5961467
	
	private int totalCg = 0; //含有depth的CG的个数
	private int totalDepth = 0; //所有深度之和
	
	private float totalConsistent = 0; //总的consisitent率和
	
	//reads ID到seq映射
	private Map<String, String> id2seq = new HashMap<String, String>();
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOringinalReads(String filename){
		try{
			String readid="", seq="";
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			while(str != null){
				if(str.startsWith(">")){
					if(readid.length()!=0){
						id2seq.put(readid, seq);
						readid = "";
						seq = "";
					}
					readid = str.substring(1,str.indexOf("rank")-1); 
				}else{
					seq = seq+str;
				}
				str = br.readLine();
			}
			id2seq.put(readid, seq);
			br.close();
	System.out.println(id2seq.size()+"*******************");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 读取匹配后的reads信息
	 * 与watson链匹配的结果  直接计算
	 * @param filename   匹配结果
	 * @param cg          标识cg位置的深度
	 * @param total       表示所有位置的深度
	 * @param length     染色体长度
	 */
	public void readMappedReads_Watson(String filename,byte[] cg, byte[] total){
		String str = null;
		int len = cg.length;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			str = br.readLine();
			Scanner s = null;
			str = br.readLine();
			int start,end;
			String seq;  //序列
			String readid; 
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				seq = id2seq.get(readid);
				for(int i=start;i<=end&&i<len;i++){
					if( (i-start+1<seq.length()) && seq.charAt(i-start)=='C' && seq.charAt(i-start+1)=='G'){ //只计算CG的深度
									cg[i]++; //计算CG位置深度
									total[i]++;
					}
					if( (i-start+1<seq.length()) && seq.charAt(i-start)=='T' && seq.charAt(i-start+1)=='G') //只计算CG的深度
							total[i]++;   //计算所有位置深度
				}
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(str);
		}
	}
	
	/**
	 * 读取与Crick匹配后的reads
	 * 注意：该匹配结果对应的ref位置信息，与原始ref位置信息互补  所以先转换成原始位置信息
	 * @param filename
	 * @param b
	 * @param length
	 */
	public void readMappedReads_Crick(String filename,byte[] cg,byte[] total, long length){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			int len = cg.length;
			str = br.readLine();
			int start,end,temp;
			String seq,readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				temp = start;
				start = (int) (length+1-end);
				end = (int) (length+1-temp);
				seq = id2seq.get(readid);
				for(int i=start;i<=end&&i<len;i++){
					if( (i-start+1<seq.length()) && seq.charAt(i-start)=='G' && seq.charAt(i-start+1)=='C'){ //只计算CG的深度
						cg[i]++; //计算CG位置深度
						total[i]++;
					}
					if( (i-start+1<seq.length()) && seq.charAt(i-start)=='G' && seq.charAt(i-start+1)=='T') //只计算CG的深度
						total[i]++;   //计算TG位置深度  
					}
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 读取染色体数据信息
	 * 获取CG的位置
	 * @param chrFile
	 */
	public void readChr(String chrFile, String outFile,byte[] total, byte[] cg){
		 int length = 0;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(chrFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
			br.readLine(); //忽略第一行
			String str = br.readLine();
			char[] ch;
			boolean furtherC = false; //上一行末尾是否为C
			int position = 0;  //CG的位置
			while(str != null){
				if(furtherC) str = "C"+str;
				ch = str.toUpperCase().toCharArray();
				for(int i=0;i<ch.length-1;i++){
					if(ch[i]=='C'&&ch[i+1]=='G'){  //如果是 CG位点
						 position = length+(i+1); //获取到C的位置信息
						 if(total[position]>0){  //如果该CG位置上的深度大于0则输出到文件
							 totalCg++;  //CG个数就加1
							 totalDepth+=total[position]; //深度累加
							 
							 totalConsistent += (float)cg[position]/(float)total[position];
							 bw.write("CG\t"+position+"\t"+total[position]+"\t"+cg[position]+"\t"+(float)cg[position]/(float)total[position]);
							 bw.newLine();
						 }
					}
				}
				if(ch[ch.length-1]=='C') { //如果字符串结尾是C  则长度--
					furtherC =  true;   
				    length--;
				}else
					furtherC = false;
				length+=str.length();
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public Consisitent() {
		readOringinalReads("E:/研究生工作/personal_data/O6.GAC.454Reads/O6.GAC.454Reads.fa");
		
		for(int i=1;i<=22;i++){
			byte[] total = new byte[(int)locat[i-1]+1];
			byte[] cg = new byte[(int)locat[i-1]+1];
			readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_Chr"+i+".txt",
				 cg,total,locat[i-1]);
			readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_Chr"+i+".txt",
				 cg,total,locat[i-1]);
			
			readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_Chr"+i+".txt",
				 cg,total);
			readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_Chr"+i+".txt",
				 cg,total);
			
			readChr("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt", "e:/cg2/hg19_Chr"+i+".txt", total,cg);
			System.out.println(i+"\t\t"+totalCg+"\t"+totalDepth);
			total = null;
			cg = null;
		}
		byte[] total = new byte[(int)locat[22]+1];
		byte[] cg = new byte[(int)locat[22]+1];
		readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_ChrX.txt",
			 cg,total,locat[22]);
		readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_ChrX.txt",
			 cg,total,locat[22]);
		
		readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_ChrX.txt",
			 cg,total);
		readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_ChrX.txt",
			 cg,total);
		
		readChr("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_ChrX.txt", "e:/cg2/hg19_ChrX.txt", total,cg);
		System.out.println("X\t\t"+totalCg+"\t"+totalDepth);
		
		System.out.println("Total consistent:"+totalConsistent);
		System.out.println("Consistent:"+totalConsistent/totalCg);
		System.out.println("Total CG:"+totalCg);
		System.out.println("Total Depth:"+totalDepth);
		System.out.println("Average Depth:"+(float)totalDepth/(float)totalCg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Consisitent();
	}

}
