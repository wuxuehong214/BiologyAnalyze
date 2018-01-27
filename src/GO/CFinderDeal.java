package GO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class CFinderDeal {
	
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			Scanner s = null;
			List<String> proteins = new ArrayList<String>();
			int index = 1;
			while(str != null){
				s = new Scanner(str);
				s.next();
				while(s.hasNext())proteins.add(s.next());
				bw.write("Complex\t"+index+"\t"+proteins.size());
				bw.newLine();
				for(int i=0;i<proteins.size();i++){
					bw.write(proteins.get(i));
					bw.newLine();
				}
				proteins.clear();
				str = br.readLine();
				index++;
			}
			bw.flush();
			bw.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public CFinderDeal(){
		String base = "G:/personal/我的文章/BMC/BMC实验补充/BMC实验结果/其他五个算法/";
		readAndOutput(base+"cf.txt", base+"cpm(k=3).txt");
	}
	
	public static void main(String args[]){
		new CFinderDeal();
	}

}
