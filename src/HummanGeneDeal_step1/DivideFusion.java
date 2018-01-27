package HummanGeneDeal_step1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 背景： bwa匹配后的结果  根据匹配情况 将匹配的 和未匹配的部分拆分开来
 *将未匹配的部分重新用bwa算法 进行匹配
 *匹配结束后 需要将匹配信息融合到原来的匹配结果中 
 *
 * @author wuxuehong
 * 2012-1-3
 */
public class DivideFusion {
	
	//同一个seq对应可能多种不同匹配结果
	public Map<String, List<String>> seq2reads = new HashMap<String, List<String>>();

	public DivideFusion() {
		// TODO Auto-generated constructor stub
		
		readMisMatchFile("E:/研究生工作/personal data/ocsc1.GAC.454Reads/divided/mismatch/crick_CT.txt");
		readAndOutput("E:/研究生工作/personal data/ocsc1.GAC.454Reads/divided/divided/crick_CT_divide.txt",
				         "E:/研究生工作/personal data/ocsc1.GAC.454Reads/divided/crick_CT_divide.txt");
	}
	
	/**
wu	 * 分割出来匹配的reads重新匹配后的信息
	 * @param mismatchFile
	 */
	public void readMisMatchFile(String mismatchFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(mismatchFile);
			String str = br.readLine(); //忽略第一行
			Scanner s = null;
			str = br.readLine();
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<9;i++) s.next();
				String seq = s.next();
				List<String> reads = seq2reads.get(seq);
				if(reads == null){
					reads = new ArrayList<String>();
					reads.add(str);
					seq2reads.put(seq, reads);
				}else{
					reads.add(str);
				}
				str = br.readLine();
			}
			br.close();
		System.out.println("seq2reads size:"+seq2reads.size());
		}catch(IOException e){}
	}

	public void readAndOutput(String originMapFile, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(originMapFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			Scanner s = null;
			String readid = null;
			String seq;
			while(str != null){
				s = new Scanner(str);
				if(s.hasNext()){   
					readid = s.next();
					if(readid.contains("_")){  //如果包含这个符合 则说明是拆分后的
						s.next();
						seq = s.next(); 
						if(!s.hasNext()){  //如果没有接下来的元素  则说明该reads是mismatch的
							List<String> readss = seq2reads.get(seq);
							if(readss==null || readss.size()==0){
								bw.write(str+"\tunmappable");
								bw.newLine();
							}else{
								for(int i=0;i<readss.size();i++){
									bw.write(readid+"\t"+readss.get(i).substring(17));
									bw.newLine();
								}
							}
							str = br.readLine();
							continue;
						}
					}
				}
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch(IOException e){
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new DivideFusion();
	}

}
