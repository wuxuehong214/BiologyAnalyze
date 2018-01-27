package HummanGeneDeal_step1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

import util.FileStreamUtil;
public class ReadsFuision {

	Set<ReadVo> reads = new HashSet<ReadVo>();
	
	public ReadsFuision() {
		// TODO Auto-generated constructor stub
	}

	public void fuision(String inputFile1, String inputFile2,String outFile){
		try{
			BufferedReader br1 = FileStreamUtil.getBufferedReader(inputFile1);
			BufferedReader br2 = FileStreamUtil.getBufferedReader(inputFile2);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			
			
		}catch(Exception e){
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
