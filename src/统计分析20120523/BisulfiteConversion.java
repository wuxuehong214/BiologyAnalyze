package 统计分析20120523;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
2）Bisulfite conversion*:

* 只在ref的non-CG and non-CA 位点上统计

(#Cr1-#CGr1-#CAr1)/(#Cr2-#CGr2-#CAr2)=0.010988786
#Cr1	- Reads中C的个数:6426658 （过滤后的reads,转换前的，M部分）
#CGr1	- Reads中CG的个数:483107 (过滤后的reads, 转换前的, M部分)
#CAr1	- Reads中CA的个数:48107 (过滤后的reads, 转换前的, M部分)
#Cr2	- Ref中C的个数:579913581 （只取ref上的对应M的那些部分去数C）
#CGr2	- Ref中CG的个数:39039314 （只取ref上的对应M的那些部分去数CG）
#CAr2	- Ref中CA的个数:3039314 （只取ref上的对应M的那些部分去数CA）
 * @author wuxuehong
 * 2012-2-3
 */
public class BisulfiteConversion {
	
	private final static int offset = 1000;
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};

	//每条染色体上被匹配的长度
	long[] mapped = new long[23];
	int[] c = new int[23];
	int[] g = new int[23];
	int[] cg = new int[23];
	int[] gc = new int[23];
	
	
	/**
	 * reads信息
	 */
	private Map<String,String> readid2seq = new HashMap<String,String>();
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOriginreads(String filename){
		try{
			int count = 0;
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			String readid="",seq="";
			while(str != null){
				if(str.startsWith(">")){
					count++;
					if(seq.length()!=0){
						readid2seq.put(readid, seq);
					}
					readid = str.substring(1,str.indexOf("rank")-1);
					seq = "";
				}else{
					seq += str.trim();
				}
				str = br.readLine();
			}
			if(seq.length()!=0)
				readid2seq.put(readid, seq);
//		   System.out.println("Total reads size:"+count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取匹配后的reads信息
	 * 与watson链匹配的结果  直接计算
	 * @param filename   匹配结果
	 * @param b          标识情况
	 * @param length     染色体长度
	 */
	public void readMappedReads_Watson(String filename,byte[] b){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			str = br.readLine();
			int start,end,len;
			String cigra;
			char[] f;
			while(str != null){
				s = new Scanner(str);
				s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				len = s.nextInt();
				s.next();
				s.next();
				cigra = s.next();
				
				f = Util.getChars(cigra, len);
				for(int i=start;i<=end;i++){
					if(f[i-start]=='M')
						b[i]=1;
				}
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取与Crick匹配后的reads
	 * 注意：该匹配结果对应的ref位置信息，与原始ref位置信息互补  所以先转换成原始位置信息
	 * @param filename
	 * @param b
	 * @param length
	 */
	public void readMappedReads_Crick(String filename,byte[] b, long length){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			str = br.readLine();
			int start,end,temp,len;
			String cigra;
			char[] f;
			while(str != null){
				s = new Scanner(str);
				s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				len = s.nextInt();
				s.next();
				s.next();
				cigra = s.next();
				
				f = Util.getChars(cigra, len);
				for(int i=start;i<=end;i++){
					if(f[i-start]=='M'){
						temp = (int) (length+1-i);
						b[temp] = 1;
					}
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
	public void readChr(String chrFile, byte[] b, int index){
		 int length = 0;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(chrFile);
			br.readLine(); //忽略第一行
			String str = br.readLine();
			char[] ch;
			boolean furtherC = false; //上一行末尾是否为C
			boolean furtherG = false; //上一行末尾是否为G
			int position = 0;  //CG的位置
			while(str != null){
				if(furtherC) str = "C"+str;
				if(furtherG) str = "G"+str;
 				ch = str.toUpperCase().toCharArray();
				for(int i=0;i<ch.length-1;i++){
					if(ch[i]=='C'){
						position = length+(i+1); //获取到C的位置信息
						if(b[position]==1){   //如果被cover了
								c[index]++;
								if(ch[i+1]=='G' && b[position+1]==1){
									cg[index]++;
								}
						}
					}else if(ch[i]=='G'){
						position = length+(i+1);
						if(b[position]==1){
								g[index]++;
								if(ch[i+1]=='C' && b[position+1]==1){
									gc[index]++;
								}
						}
					}
				}
				
				if(ch[ch.length-1]=='C') { //如果字符串结尾是C  则长度--
					furtherC =  true;   
				    length--;
				}else
					furtherC = false;
				
				if(ch[ch.length-1]=='G'){
					furtherG = true;
					length--;
				}else
					furtherG = false;
				
				length+=str.length();
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public BisulfiteConversion(String args[]) {
		// TODO Auto-generated constructor stub
		for(int i=1;i<=22;i++){
	System.out.println(i);
			byte[] b = new byte[(int) (locat[i-1]+offset)];
			int count = 0;
			readMappedReads_Watson("../watson_CT_Chr"+i+".txt", b);
			readMappedReads_Watson("../watson_GA_Chr"+i+".txt", b);
			readMappedReads_Crick("../crick_CT_Chr"+i+".txt", b, locat[i-1]);
			readMappedReads_Crick("../crick_GA_Chr"+i+".txt", b, locat[i-1]);
			for(int j=0;j<=locat[i-1];j++)if(b[j]==1)count++;
			mapped[i-1]=count;
			readChr("HumanGeneRepeat/hg19_Chr"+i+".txt", b, i-1);
			b = null;
		}
		
		byte[] b = new byte[(int) (locat[22]+offset)];
		int count = 0;
		readMappedReads_Watson("../watson_CT_ChrX.txt", b);
		readMappedReads_Watson("../watson_GA_ChrX.txt", b);
		readMappedReads_Crick("../crick_CT_ChrX.txt", b, locat[22]);
		readMappedReads_Crick("../crick_GA_ChrX.txt", b, locat[22]);
		for(int j=0;j<=locat[22];j++)if(b[j]==1)count++;
		mapped[22]=count;
		readChr("HumanGeneRepeat/hg19_ChrX.txt", b, 22);
		b = null;
		
		int total = 0;
		int totalc = 0;
		int totalg = 0;
		int totalcg = 0;
		int totalgc = 0;
		
		for(int i=0;i<23;i++){
			total+=mapped[i];
			totalc+=c[i];
			totalg+=g[i];
			totalcg+=cg[i];
			totalgc+=gc[i];
		}
		
		System.out.println("total coverd ref length:"+total);
		System.out.println("total coverd ref c length:"+totalc);
		System.out.println("total coverd ref g length:"+totalg);
		System.out.println("total coverd ref cg length:"+totalcg);
		System.out.println("total coverd ref gc length:"+totalgc);
		
		System.out.println("c conversion:"+(float)(totalc-totalcg)/(float)total);
		System.out.println("g conversion:"+(float)(totalg-totalgc)/(float)total);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BisulfiteConversion c = 	new BisulfiteConversion(args);
//		System.out.println(Arrays.toString(c.getChars("6S104M1I16M315S",442)));
	}

}
