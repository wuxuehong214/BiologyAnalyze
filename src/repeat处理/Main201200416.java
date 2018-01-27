package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;

import Task20120228.RefUtil;

import util.FileStreamUtil;

public class Main201200416 {
	
	String readBase = "K:/bwa/z=50/01/";
	String refBase = "D:/recover/研究生工作/personal_data/repeatchr1-22/网上下下来数据反序/";
	
	public void readReads(String readsf, String ref, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(readsf);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Scanner s = null;
			int start,end;
			String reads,seq;
			int count = 0;
			boolean isAvi = false;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
//				for(int i=0;i<4;i++)s.next();
//				reads = s.next();
				
				isAvi = false;
				seq = RefUtil.refReadsAndFilterRuelst(ref, start, end, RefUtil.NEG);
				char[] ch = seq.toCharArray();
				for(int i=0;i<ch.length;i++){
					if(ch[i] =='A' || ch[i] == 'G' || ch[i] == 'C' || ch[i] == 'T'){
						isAvi = true;
						break;
					}
				}
				if(isAvi){
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main201200416(){
		readReads(readBase+"Chr22_c+.txt", refBase+"hg19_Chr22.txt", readBase+"Chr22_c+_repeatFilter.txt");
	}

	public static void main(String args[]){
		new Main201200416();
	}
}
