
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

import util.FileStreamUtil;
/**
 * 输出未匹配的reads
 * 分别根据Watson_CT, Watson_GA, Crick_CT, Crick_GA输出这四个文件
 * @author wuxuehong
 * 2012-3-12
 */
public class OutUnmappedReads {
	
	Map<String,Integer> id2reads  = new HashMap<String,Integer>();

	public void readMaxprecision(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid = null;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				id2reads.put(readid, 1);
				str = br.readLine();
			}
			br.close();
		}catch (Exception e) {
		}
	}
	
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			String str = br.readLine();
			boolean canwrite = false;
			Scanner s = null;
			String readid = null;
			while(str != null){
				if(str.startsWith(">")){
					s = new Scanner(str);
					readid = s.next().substring(1);
					if(id2reads.get(readid) == null){ //说明没有匹配  则需要输出
						bw.write(str);
						bw.newLine();
						canwrite = true;
					}else{
						canwrite = false;
					}
				}else{
					if(canwrite){
						bw.write(str);
						bw.newLine();
					}
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
		}
	}
	
	public OutUnmappedReads(){
		readMaxprecision("maxprecision/watson_CT.txt");
		readAndOutput("CT.txt", "watson_CT_unmapped.txt");
		id2reads.clear();
		readMaxprecision("maxprecision/crick_CT.txt");
		readAndOutput("CT.txt", "crick_CT_unmapped.txt");
		id2reads.clear();
		readMaxprecision("maxprecision/watson_GA.txt");
		readAndOutput("GA.txt", "watson_GA_unmapped.txt");
		id2reads.clear();
		readMaxprecision("maxprecision/crick_GA.txt");
		readAndOutput("GA.txt", "crick_GA_unmapped.txt");
	}
	
	public static void main(String args[]){
		new OutUnmappedReads();
	}
	
}
