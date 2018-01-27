package repeat¥¶¿Ì;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main20120502 {
	
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str);
				bw.newLine();
				str = br.readLine();
			}
			Scanner s = null;
			String line;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<11;i++)s.next();
				line = s.next();
	System.out.println(s.next());
				if("L1PA3".equals(line) || "L1PA4".equals(line)|| "L1PA5".equals(line)|| "L1PA6".equals(line)|| "L1PA7".equals(line)|| "L1PA8".equals(line)){
					System.out.println(line);
				}else{
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main20120502() {
		// TODO Auto-generated constructor stub
		String base = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/L1PA3L1M4c_filter/";
		readAndOutput(base+"Chr22_w+_L1HS_dups_L1PA(3-8)_filter.txt", base+"Chr22_w+_L1HS_dups_L1PA(3-8)_filter(2).txt");
	}
	
	public static void main(String args[]){
		new Main20120502();
	}

}
