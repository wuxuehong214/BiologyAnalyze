package HummanGeneDeal_step1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import util.FileStreamUtil;
import util.LogServer;

/**
 * 拆分匹配的reads
 * @author wuxuehong
 * 2011-12-30
 */
public class Main {
	
	private List<ReadVo> readList = new ArrayList<ReadVo>();
	private List<ReadVo> matchlist = new ArrayList<ReadVo>();
	private Set<ReadVo> mismatchlist = new HashSet<ReadVo>(); //防止重复
	private String title;
	
	private Map<String,String> readid2seq = new HashMap<String,String>();
	
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOriginreads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			String readid="",seq="";
			while(str != null){
				if(str.startsWith(">")){
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
			br.close();
//	System.out.println("original reads size:"+readid2seq.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取已经匹配的read信息
	 * @param inputFile
	 */
	public void readReads(String inputFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			String str = br.readLine(); //忽略第一行
			title = str;
			Scanner s = null;
			str = br.readLine();
			ReadVo read = null;
			int count=0;
			while(str != null){
				s = new Scanner(str);
				read = new ReadVo();
				readList.add(read);
				read.setReadid(s.next()); //readid
//System.out.println(count++);
				read.setHumgene(s.next()); //
				read.setChr(s.next()); //
				read.setStart(Long.parseLong(s.next())) ; //start position
				read.setEnd(Long.parseLong(s.next())); //end position
				read.setLength(s.nextInt()); //length
				read.setChrlen(Long.parseLong(s.next())); //chr length
				read.setMatchics(Float.parseFloat(s.next())) ; //match/all
				read.setCigar(s.next()); //cigra
				read.setSeq(readid2seq.get(read.getReadid()));  //sequence
				str = br.readLine();
			}
			br.close();
//			LogServer.log("Total mapped reads:"+readList.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 分析read  并对read进行分割
	 * @param  matchFile
	 * @param  misMatchFile
	 * 
	 */
	public void divideReadsAndoutput(String matchFile, String misMatchFile,String cigar, String divideFile){
		ReadVo read = null;
		String str[];
		for(int i=0;i<readList.size();i++){
			read = readList.get(i);
			str = divideCigra(read.getCigar()); // Cigar分割
			try {
				dealReadwithCigar(read, str, divideFile);  //分解reads
			} catch (IOException e) {
			}
//			showStr(divideCigra(read.getCigar()),cigar);
		}
		
		try{
			BufferedWriter bw2 = FileStreamUtil.getBufferedWriter(misMatchFile);
//			Collections.sort(matchlist);
//			BufferedWriter bw1 = FileStreamUtil.getBufferedWriter(matchFile);
//			bw1.write(title);
//			bw1.newLine();
//			for(int i=0;i<matchlist.size();i++){
//				bw1.write(matchlist.get(i).toString());
//				bw1.newLine();
//			}
//			bw1.flush();
//			bw1.close();
			
			Iterator<ReadVo> it = mismatchlist.iterator();
			while(it.hasNext()){
				ReadVo r = it.next();
				bw2.write(">"+r.getReadid()+"\t"+r.getLength());
				bw2.newLine();
				bw2.write(r.getSeq());
				bw2.newLine();
			}
			bw2.flush();
			bw2.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将read 按照分解后的cigra进行分割
	 * @param read
	 * @param str
	 * @throws IOException 
	 */
	public void dealReadwithCigar(ReadVo read, String[] str, String divideFile) throws IOException{
//		BufferedWriter bw = null;
//		bw = new BufferedWriter(new FileWriter(new File(divideFile), true));
		
		if(str.length==1){
			matchlist.add(read);
//			bw.write(read.toString());
//			bw.newLine();
		}else{
//			bw.newLine();
//			bw.write(read.toString());
//			bw.newLine();
//			bw.write("||");
//			bw.newLine();
			for(int i=0;i<str.length;i++){
				int[] a = getMatchAndMismatch(str[i]);
				String cigra = str[i];
				int subindex = 0;
				if(a[0]>a[1]){  //这种情况下是match    如果解析出来的字符串中 匹配的大于未匹配的
					ReadVo r = (ReadVo) read.clone();
					r.setReadid(r.getReadid()+"_"+(i+1));
					long start = r.getStart();
					for(int j=0;j<i;j++){
						int b[] = getMatchAndMismatch(str[j]);
						start+=(b[0]+b[1]);
						subindex+=(b[0]+b[1]);
					}
					r.setStart(start);
					r.setEnd(r.getStart()+a[0]+a[1]-1);
					r.setLength(a[0]+a[1]);
					float matchics = (float)(parseCiagr(cigra))/(float)r.getLength();
					r.setMatchics(matchics);
					r.setCigar(cigra);
					r.setSeq(r.getSeq().substring(subindex, r.getLength()+subindex));
					matchlist.add(r);
					
//					bw.write(r.toString());
//					bw.newLine();
//					if(i!=str.length-1){
//						bw.write("+");
//						bw.newLine();
//					}
				}else{  //这种情况需要单独输出重新匹配
					ReadVo r = new ReadVo();
					r.setReadid(read.getReadid()+"_"+(i+1));
					r.setLength(a[0]+a[1]);
					for(int j=0;j<i;j++){
						int b[] = getMatchAndMismatch(str[j]);
						subindex+=(b[0]+b[1]);
					}
					r.setSeq(read.getSeq().substring(subindex,subindex+r.getLength()));
					mismatchlist.add(r);
					
//					bw.write(r.getReadid()+"\t"+r.getLength()+"\t"+r.getSeq());
//					bw.newLine();
//					if(i!=str.length-1){
//						bw.write("+");
//						bw.newLine();
//					}
				}
			}
//			bw.newLine();
		}
//		bw.flush();
//		bw.close();
	}
	
	/**
	 * a[0]--number of match
	 * a[1]--number of mismatch
	 * @param cigra
	 * @return
	 */
	public int[] getMatchAndMismatch(String cigra){
		int[] a = new int[2];
		char[] c = cigra.toCharArray();
		int count = 0;
		for(int i=0;i<c.length;i++){
			if(c[i]=='D'){
				count=0;
				continue;
			}
			if(c[i]=='M'){
				a[0]+=count;
				count=0;
			}else if(c[i]=='S'||c[i]=='I'){
				a[1]+=count;
				count=0;
			}else if(c[i]>='0'&&c[i]<='9'){
				count = count*10+c[i]-'0';
			}else{
				LogServer.log("exception happens in getMatchAndMismatch!");
			}
		}
		return a;
	}
	
	/**
	 * M D I S
	 * 
	 * *******如果mis的数量大于等于M 则将其分离********
	 * etc:241S31M18S
	 * @param cigra
	 * @return
	 */
	public String[] divideCigra(String cigar){
		int match = parseCiagr(cigar); //首先解析M的数量
		int count = 0; //计数器对于 M/D的计数
		StringBuffer sb = new StringBuffer("");
		char[]  c = cigar.toCharArray();
		String temp = null;
		for(int i=0;i<c.length;i++){
			temp =  sb.toString();
			
			if(c[i]=='M' || c[i]=='D'){
				sb.append(""+count+c[i]);
				count = 0;   //MD计数清零
			}else if(c[i]=='I' || c[i]=='S'){
				if(count >= 35){               //切割判定规则
					sb.append(","+count+c[i]+",");
				}else
					sb.append(""+count+c[i]);
				count = 0;  //MD 计数清零
			}else if(c[i]>='0'&&c[i]<='9'){
				count = count*10+c[i]-'0';
			}else{
				LogServer.log("My god! exception! not M?D?I?S?? it is:"+c[i]);
			}
		}
		temp  = sb.toString();
		if(temp.startsWith(","))temp = temp.substring(1);
		if(temp.endsWith(","))temp = temp.substring(0,temp.length()-1);
		return temp.split(",");
	}
	
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
 
	public Main() {
		// TODO Auto-generated constructor stub
		readOriginreads("GA.txt");
		readReads("maxprecision/watson_GA.txt");
		divideReadsAndoutput("","maxprecision/divide/watson_GA.txt","","");
		
		readList.clear();matchlist.clear();mismatchlist.clear();
		readReads("maxprecision/crick_GA.txt");
		divideReadsAndoutput("","maxprecision/divide/crick_GA.txt","","");
		
		readList.clear();matchlist.clear();mismatchlist.clear();readid2seq.clear();
		
		
		readOriginreads("CT.txt");
		readReads("maxprecision/watson_CT.txt");
		divideReadsAndoutput("","maxprecision/divide/watson_CT.txt","","");
		
		readList.clear();matchlist.clear();mismatchlist.clear();
		readReads("maxprecision/crick_CT.txt");
		divideReadsAndoutput("","maxprecision/divide/crick_CT.txt","","");
	}
	
	public void showStr(String[] s, String filename){
		if(s.length>3)
			System.out.println("********************************************");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename),true));
		for(int i=0;i<s.length;i++){
			bw.write(s[i]+"\t");
		}
		bw.newLine();
		bw.flush();
		bw.close();
		}catch(Exception e){
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
