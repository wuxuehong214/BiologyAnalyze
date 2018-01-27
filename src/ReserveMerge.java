import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 * reads C->T 中每个readsid 后面加a
 * reads G->A 逆序 互补中 每个readsid 后面加b
 * @author wuxuehong
 * 2012-3-16
 */
public class ReserveMerge {

	String filename = "CTGAreservemerge/ab.txt";
	
	public void readReadsCT(){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader("CT.txt");
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(filename, true));
			String str = br.readLine();
			Scanner s = null;
			String readid ;
			while(str != null){
				if(str.startsWith(">")){
					s = new Scanner(str);
					readid = s.next();
					bw2.write(readid+"a"+" ");
					bw2.write(str.substring(str.indexOf("rank")));
					bw2.newLine();
				}else{
					bw2.write(str);
					bw2.newLine();
				}
				str = br.readLine();
			}
			bw2.flush();
			bw2.close();
		}catch (Exception e){
		}
	}
	
	public void readReadsGA(){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader("GA.txt");
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(filename, true));
			String str = br.readLine();
			Scanner s = null;
			String readid ;
			StringBuffer sb = null;
			String[] r;
			while(str != null){
				if(str.startsWith(">")){
					if(sb != null){
						sb.reverse();
						r = reserve(sb.toString());
						for(int i=0;i<r.length;i++){
							bw2.write(r[i]);
							bw2.newLine();
						}
					}
					sb = new StringBuffer();
					s = new Scanner(str);
					readid = s.next();
					bw2.write(readid+"b"+" ");
					bw2.write(str.substring(str.indexOf("rank")));
					bw2.newLine();
				}else{
					sb.append(str);
				}
				str = br.readLine();
			}
			sb.reverse();
			r = reserve(sb.toString());
			for(int i=0;i<r.length;i++){
				bw2.write(r[i]);
				bw2.newLine();
			}
			
			bw2.flush();
			bw2.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String[] reserve(String str){
		char[] ch = str.toCharArray();
		for(int i=0;i<ch.length;i++){
			ch[i] = getChar(ch[i]);
		}
		String ss = String.valueOf(ch);
		int len = 0;
		if(ss.length()%60==0)len = ss.length()/60;
		else len = ss.length()/60+1;
		String[] r = new String[len];
		for(int i=0;i<len;i++){
			r[i] = ss.substring(i*60,(i*60+60)<ss.length()?((i*60)+60):ss.length());
		}
		return r;
	}
	
	/**
	 * A-T C-G互补  将C换成T
	 * @param ch
	 * @return
	 */
	public char getChar(char ch){
		if(ch=='A') return 'T';
		if(ch=='T') return 'A';
		if(ch=='G') return 'C';
		if(ch=='C') return 'G';
		return ch;
	}
	
	public ReserveMerge(){
		readReadsCT();
		readReadsGA();
	}
	
	public static void main(String args[]){
		new ReserveMerge();
	}
}
