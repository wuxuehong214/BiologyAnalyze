package HumanGenDeal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 将BWA算法分析结果 转换成 特地的格式
 * @author wuxuehong
 * 2011-12-26
 */
public class FormatConvert {
	
	/**
	 * 解析CIAGR 字符串  解析出 匹配的个数
	 * @param ciagr
	 * @return
	 */
	public int parseCiagr(String ciagr){
		int total = 0;
		int temp = 0;
		char[]  c = ciagr.toCharArray();
		int len = c.length;
		for(int i=0;i<len; i++){
			if(c[i]=='M'){
				total += temp;
			}
			if(c[i]>='0'&&c[i]<='9'){
				temp = temp*10+c[i]-'0';
			}else{
				temp = 0;
			}
		}
		return total;
	}
	
	
	
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566};
	
	public void readMatchedReads(String inputFile,String outFile,int index) throws IOException{
		List<ReadVo> reads = new LinkedList<ReadVo>();
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		String str = br.readLine();  //第一行忽略
		String chr = "Chr"+index;
		if(index == 23){
			chr = "ChrX";
		}
		Scanner s = null;
		String read,ref,seq,ciagr;
		str = br.readLine();
		int start,end,len,match;
		int flag ;
		while(str != null){
			s = new Scanner(str);
			read = s.next();   //read ID
			flag = s.nextInt();
			if(flag == 16){
				str = br.readLine();
				continue;
			}
			ref = s.next();   //ref ID 
			if(!"*".equals(ref)){
				ReadVo readvo = new ReadVo();
				reads.add(readvo);
				
				start = s.nextInt(); //start position
				s.next();  //ignore
				ciagr = s.next(); //CIAGR
				match = parseCiagr(ciagr); // number of match
				for(int i=0;i<3;i++)s.next();
				seq = s.next();
				len = seq.length();
				end = start+len-1;
				
				readvo.setReadID(read);
				readvo.setRefID(ref);
				readvo.setStart(start);
				readvo.setEnd(end);
				readvo.setReadLength(len);
				readvo.setSeq(seq);
				readvo.setRefLength(locat[index-1]);
				readvo.setCiagr(ciagr);
				readvo.setChr(chr);
				readvo.setMathcs((float)(match)/(float)len);
			}
			str = br.readLine();
		}
		
		Collections.sort(reads);
		
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		bw.write("ReadID\tRefID\tChrID\tStarting position\tEnd position\tLength of read\tLength\tMatch/Reads\tCiagr\tMapping strand of ref");
		bw.newLine();
		int size = reads.size();
		for(int i=0;i<size; i++){
			bw.write(reads.get(i).toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	/**
	 * chr22 = 51304566
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException{
		FormatConvert main = new FormatConvert();
		String base = "E:/morehouse/z=50/divide/01/";
		for(int i=1;i<=23;i++){
			String filename = "Chr"+i+"_w+.txt";
			if(i==23)filename = "ChrX_w+.txt";
			main.readMatchedReads(base+filename, base+"FormatConvert/"+filename, i);
		}
	}

}
