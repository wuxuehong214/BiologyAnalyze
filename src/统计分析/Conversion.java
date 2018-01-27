package 统计分析;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 分别计算C链 G链的Conversion
 * 
 * 计算方式: C链  (#C-#CpG)/(reads所Cover的ref长度)
 *           G链  (#G-#GpC)/(reads所Cover的ref长度)
 * 分别计算 Cover 长度  以及Cover bp数两种情况下
 * @author wuxuehong
 * 2012-2-3
 */
public class Conversion {
	
	private final static int offset = 1000;
	private final static int LEN = 1; //reads length cover
	private final static int BP = 0;  //reads bp cover
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
	 * 读取匹配后的reads信息
	 * 与watson链匹配的结果  直接计算
	 * @param filename   匹配结果
	 * @param b          标识情况
	 * @param length     染色体长度
	 */
	public void readMappedReads_Watson(String filename,byte[] b,int type){
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
				
				switch(type){
					case LEN:  //cover length
						for(int i=start;i<=end;i++)b[i]=1;
						break; 
					case BP:  //cover bp
						f = getChars(cigra, len*2);
						for(int i=start;i<=end;i++){
							if(f[i-start]=='M')
								b[i]=1;
						}
				}
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
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
				totalD+=count;
				count=0;
			}else if(c[i]=='I'){
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
	 * 读取与Crick匹配后的reads
	 * 注意：该匹配结果对应的ref位置信息，与原始ref位置信息互补  所以先转换成原始位置信息
	 * @param filename
	 * @param b
	 * @param length
	 */
	public void readMappedReads_Crick(String filename,byte[] b, long length,int type){
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
				
				switch(type){
					case LEN:
						for(int i=start;i<=end;i++)b[(int) (length+1-i)]=1;
						break;
					case BP:
						f = getChars(cigra, len*2);
						for(int i=start;i<=end;i++){
							if(f[i-start]=='M'){
								temp = (int) (length+1-i);
								b[temp] = 1;
							}
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

	public Conversion(String args[]) {
		// TODO Auto-generated constructor stub
		if(args.length!=1){
			System.out.println("参数错误，请输入reads cover类型0-length,1-bp");
			return;
		}
		int type = 0;
		try{
			type = Integer.parseInt(args[0]);
			if(!(type==1||type==0)){
				throw new Exception();
			}
		}catch(Exception e){
			System.out.println("参数错误，请输入reads cover类型0-length,1-bp");
			return;
		}
		for(int i=1;i<=22;i++){
	System.out.println(i);
			byte[] b = new byte[(int) (locat[i-1]+offset)];
			int count = 0;
			readMappedReads_Watson("../watson_CT_Chr"+i+".txt", b,type);
			readMappedReads_Watson("../watson_GA_Chr"+i+".txt", b,type);
			readMappedReads_Crick("../crick_CT_Chr"+i+".txt", b, locat[i-1],type);
			readMappedReads_Crick("../crick_GA_Chr"+i+".txt", b, locat[i-1],type);
			for(int j=0;j<=locat[i-1];j++)if(b[j]==1)count++;
			mapped[i-1]=count;
			readChr("HumanGeneRepeat/hg19_Chr"+i+".txt", b, i-1);
			b = null;
		}
		
		byte[] b = new byte[(int) (locat[22]+offset)];
		int count = 0;
		readMappedReads_Watson("../watson_CT_ChrX.txt", b,type);
		readMappedReads_Watson("../watson_GA_ChrX.txt", b,type);
		readMappedReads_Crick("../crick_CT_ChrX.txt", b, locat[22],type);
		readMappedReads_Crick("../crick_GA_ChrX.txt", b, locat[22],type);
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
		
		if(type==LEN){
			System.out.println("按照匹配reads中所有bp cover ref计算:");
		}else
			System.out.println("按照匹配reads中匹配了的bp cover ref计算:");
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
		Conversion c = 	new Conversion(args);
//		System.out.println(Arrays.toString(c.getChars("6S104M1I16M315S",442)));
	}

}
