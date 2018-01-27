import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * merge CT and GA
 * 将C-T的Reads，每个后面加一个a 就是每个reads名字后面加个‘a’
 * 将G-A的Reads 每个后面加一个b就是reads名字后面加个'b'
 * @author wuxuehong
 * 2012-3-11
 */
public class Merge {

	String filename = "ab";

	public void readReadsCT(){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader("CT.txt");
			BufferedWriter bw = FileStreamUtil.getBufferedWriter("a.txt");
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(filename, true));
			String str = br.readLine();
			Scanner s = null;
			String readid ;
			while(str != null){
				if(str.startsWith(">")){
					s = new Scanner(str);
					readid = s.next();
					bw.write(readid+"a"+" ");
					bw2.write(readid+"a"+" ");
					bw.write(str.substring(str.indexOf("rank")));
					bw2.write(str.substring(str.indexOf("rank")));
					bw.newLine();
					bw2.newLine();
				}else{
					bw.write(str);
					bw2.write(str);
					bw.newLine();
					bw2.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			bw2.flush();
			bw2.close();
		}catch (Exception e){
		}
	}
	
	
		public void readReadsGA(){
			try{
				BufferedReader br = FileStreamUtil.getBufferedReader("GA.txt");
				BufferedWriter bw = FileStreamUtil.getBufferedWriter("b.txt");
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(filename, true));
				String str = br.readLine();
				Scanner s = null;
				String readid ;
				while(str != null){
					if(str.startsWith(">")){
						s = new Scanner(str);
						readid = s.next();
						bw.write(readid+"b"+" ");
						bw2.write(readid+"b"+" ");
						bw.write(str.substring(str.indexOf("rank")));
						bw2.write(str.substring(str.indexOf("rank")));
						bw.newLine();
						bw2.newLine();
					}else{
						bw.write(str);
						bw2.write(str);
						bw.newLine();
						bw2.newLine();
					}
					str = br.readLine();
				}
				bw.flush();
				bw.close();
				bw2.flush();
				bw2.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
	}

	public Merge(){
		readReadsCT();
		readReadsGA();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Merge();
	}

}
