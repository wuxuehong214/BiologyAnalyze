package 统计454中每条read中ACTG的比例;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import util.FileStreamUtil;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
		try {
			readAndOutput("E:\\研究生工作\\染色体数据以及测试数据\\hsoc-B1.GAC.454Reads.fa", "E:\\研究生工作\\统计454中每条read中ACTG比例\\out.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readAndOutput(String inputFile, String outFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		StringBuffer sb = null;
		Scanner s = null;
		String temp = null;
		bw.write("Read name\tA\tT\tC\tG");
		bw.newLine();
		while(str != null){
			if(str.startsWith(">")){
				if(sb!=null){
					temp = sb.toString();
					char[] ch = temp.toCharArray();
					float total = ch.length;
					float a=0,t=0,c=0,g=0;
					for(int i=0;i<total;i++){
						if(ch[i]=='A'){
							a++;
						}else if(ch[i]=='T'){
							t++;
						}else if(ch[i]=='C'){
							c++;
						}else if(ch[i]=='G')
							g++;
					}
					bw.write(a*100/total+"%\t"+t*100/total+"%\t"+c*100/total+"%\t"+g*100/total+"%");
//					System.out.println(a*100/total+"%\t"+t*100/total+"%\t"+c*100/total+"%\t"+g*100/total+"%");
					bw.newLine();
				}
				s = new Scanner(str);
				str = s.next();
//				System.out.println(str);
				bw.write(str.substring(1));
				bw.newLine();
				sb = new StringBuffer();
			}else{
				sb.append(str);
			}
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		new Main();
		Integer[] a = {1,2,};
		System.out.println(a.length);
		print("abc","def","d");
		print("abc","def","d","ff","a");
	}
	
	public static void print(Object...objects){
		for(Object o:objects){
			System.out.print(o+"   ");
		}
		System.out.println();
	}
	
	class Read{
		String read;
		String A;
		String C;
		String T;
		String G;
	}

}
