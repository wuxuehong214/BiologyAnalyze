package HumanGenDeal;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import util.FileStreamUtil;

public class Main {

	Set<String> reads = new HashSet<String>();
	
	public void readFile(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			str = br.readLine();
			Scanner s = null;
			while(str != null){
				s = new Scanner(str);
				reads.add(s.next());
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Main() {
		// TODO Auto-generated constructor stub
		String base = "C:/Documents and Settings/Administrator/×ÀÃæ/more house/Feb28 Task_1/01/";
		readFile(base+"crick_reads.txt");
		System.out.println(reads.size());
		readFile(base+"watson_reads.txt");
		System.out.println(reads.size());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
