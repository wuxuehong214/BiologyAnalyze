package HumanGeneDownload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2 {

	
	//1-22 X, Y
	long[] locat = {249250621,243199373,198022430,191154276,180915260,171115067,
			159138663,146364022,141213431,135534747,135006516,133851895,
			115169878,107349540,102531392,90354753,81195210,78077248,
			59128983,63025520,48129895,51304566,155270560,59373566}; 
	/**
	 * 读取url输出信息到文件
	 * @param urlStr
	 * @param outFile
	 * @throws IOException
	 */
	public void readAndOutput(String urlStr, String outFile,long start) throws IOException{
		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(0);
		connection.setConnectTimeout(0);
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile),true)); //叠加写入
		String str = br.readLine();  //回车 忽略
		if(start == 1){
			str = br.readLine();  //<PE>标签忽略
			str = br.readLine();   //chr信息 第一次读取需要 
		}else{
			str = br.readLine();  //<PE>标签忽略
			str = br.readLine();   //chr信息 忽略
			str = br.readLine();   //gene sequence 需要
		}
		while(str != null && !"</PRE>".equals(str)){
			bw.write(str);
			bw.newLine();
			try{
				str = br.readLine();
			}catch(IOException e){
				str = null;
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}
	
	public Main2(){
		String urlStr, outFile;
		for(int i=22;i>=1;i--){
			outFile = "E:/研究生工作/personal data/repeat chr1-22/mutithread/hg19_Chr"+i+".txt";
			new Thread(new LoadThread(outFile,locat[i-1],i+"")).start();
			}
		outFile = "E:/研究生工作/personal data/repeat chr1-22/mutithread/hg19_ChrX.txt";
		new Thread(new LoadThread(outFile,locat[22],"X")).start(); //x
		
		outFile = "E:/研究生工作/personal data/repeat chr1-22/mutithread/hg19_ChrY.txt";
		new Thread(new LoadThread(outFile,locat[23],"Y")).start(); //y
	}
	
	public static void main(String args[]) throws IOException{
		new Main2();
	}
	
	/**
	 * 线程方式
	 * @author wuxuehong
	 * 2011-12-23
	 */
	class LoadThread implements Runnable{
		String urlStr,outFile,index;
		long length;
		int hang = 200000;
		int each = 50;
		LoadThread(String outFile,long length,String index){
			this.outFile = outFile;
			this.length = length;
			this.index = index;
		}
		public void run() {
			try {
				System.out.println(outFile+"  is beginning....length:"+length);
				long start = 1;
				long tempEnd;
				while(start <= length){
					tempEnd =  start+hang*50-1;  //结束的位置
					tempEnd = tempEnd<length?tempEnd:length; //如果结束的位置大于长度的时候则 去长度
					
					urlStr = "http://genome.ucsc.edu/cgi-bin/hgc?hgsid=230365969&g=htcGetDna2&table=&i=mixed&o=0&l=0&r=155270560&getDnaPos=chr"+index+"%3A"+start+"-"+tempEnd+"&db=hg19&hgSeq.cdsExon=1&hgSeq.padding5=0&hgSeq.padding3=0&hgSeq.casing=upper&hgSeq.maskRepeats=on&boolshad.hgSeq.maskRepeats=0&hgSeq.repMasking=lower&boolshad.hgSeq.revComp=0&submit=get+DNA";
					readAndOutput(urlStr, outFile,start);
					start = tempEnd+1;
					System.out.println(outFile+"\tstart:"+start+"\tlength:"+length);
				}
				System.out.println(outFile+"  finished!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
