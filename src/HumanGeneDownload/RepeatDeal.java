package HumanGeneDownload;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import util.FileStreamUtil;

/**
 * repeat处理 将所有小写字母替换成N
 * @author wuxuehong
 * 2011-12-23
 */
public class RepeatDeal {
	
	public RepeatDeal(){
		for(int i=1;i<=22;i++){
			new Thread(new DealThread("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt",
					"E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeatMark/hg19_Chr"+i+".txt")).start();
		}
		new Thread(new DealThread("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrX.txt",
				"E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeatMark/hg19_ChrX.txt")).start();
		new Thread(new DealThread("E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeat/hg19_ChrY.txt",
		"E:/研究生工作/personal data/repeat chr1-22/HumanGeneRepeatMark/hg19_ChrY.txt")).start();
	}
	
	public static void main(String args[]){
		new RepeatDeal();
	}
	
	class DealThread implements Runnable{
		String inputFile,outFile;
		public DealThread(String inputFile,String outFile) {
			this.inputFile = inputFile;
			this.outFile = outFile;
		}
		public void run(){
			try{
				System.out.println(outFile+"\tis beginning...");
				BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
				BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
				String str = br.readLine();
				bw.write(str);  //第一行写出
				bw.newLine();
				str= br.readLine();
				while(str != null){
					str = str.replaceAll("a", "N").replaceAll("c", "N").replaceAll("g", "N").replaceAll("t", "N");
					bw.write(str);
					bw.newLine();
					str = br.readLine();
				}
				br.close();
				bw.flush();
				bw.close();
				System.out.println(outFile+"\tfinished!");
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

}
