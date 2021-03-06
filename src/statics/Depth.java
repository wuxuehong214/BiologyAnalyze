package statics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * ref上每个CG的depth:统计                                                                                                   
 * @author wuxuehong
 * 2012-1-8
 */
public class Depth {
	
	
	private int totalCg = 0; //含有depth的CG的个数
	private int totalDepth = 0; //所有深度之和
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	/**
	 * 读取匹配后的reads信息
	 * 与watson链匹配的结果  直接计算
	 * @param filename   匹配结果
	 * @param b          标识情况
	 * @param length     染色体长度
	 */
	public void readMappedReads_Watson(String filename,byte[] b){
		String str = null;
		int len = b.length;
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			str = br.readLine();
			Scanner s = null;
			str = br.readLine();
			int start,end;
			while(str != null){
				s = new Scanner(str);
				s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				for(int i=start;i<=end&&i<len;i++)b[i]++; //计算深度
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
	public void readMappedReads_Crick(String filename,byte[] b, long length){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			int len = b.length;
			str = br.readLine();
			int start,end,temp;
			while(str != null){
				s = new Scanner(str);
				s.next();
				s.next();
				s.next();
				start = s.nextInt();
				end = s.nextInt();
				temp = start;
				start = (int) (length+1-end);
				end = (int) (length+1-temp);
				for(int i=start;i<=end&&i<len;i++)b[i]++; //计算深度  
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
	public void readChr(String chrFile, String outFile,byte[] b){
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
						 if(b[position]>0){  //如果该CG位置上的深度大于0则输出到文件
							 totalCg++;  //CG个数就加1
							 totalDepth+=b[position]; //深度累加
							 bw.write("CG\t"+position+"\t"+b[position]);
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

	public Depth() {
		// TODO Auto-generated constructor stub
		for(int i=1;i<=22;i++){
			byte[] b = new byte[(int)locat[i-1]+1];
			readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_Chr"+i+".txt",
				 b,locat[i-1]);
			readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_Chr"+i+".txt",
				 b,locat[i-1]);
			
			readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_Chr"+i+".txt",
				 b);
			readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_Chr"+i+".txt",
				 b);
			
			readChr("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt", "e:/cg/hg19_Chr"+i+".txt", b);
			System.out.println(i+"\t\t"+totalCg+"\t"+totalDepth);
		}
		byte[] b = new byte[(int)locat[22]+1];
		readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_CT_ChrX.txt",
			 b,locat[22]);
		readMappedReads_Crick("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/crick_GA_ChrX.txt",
			 b,locat[22]);
		
		readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_CT_ChrX.txt",
			 b);
		readMappedReads_Watson("E:/研究生工作/personal_data/O6.GAC.454Reads/bwa匹配结果/FormatConvert/watson_GA_ChrX.txt",
			 b);
		
		readChr("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_ChrX.txt", "e:/cg/hg19_ChrX.txt", b);
		System.out.println("X\t\t"+totalCg+"\t"+totalDepth);
		
		System.out.println((float)totalDepth/(float)totalCg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Depth();
	}

}
