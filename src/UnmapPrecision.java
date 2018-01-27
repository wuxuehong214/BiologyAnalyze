import java.io.BufferedReader;

import util.FileStreamUtil;
import java.util.*;

/**
 * 分析 为匹配reads分段匹配后  匹配的reads数目
 * @author wuxuehong
 * 2012-3-13
 */
public class UnmapPrecision {
	
	Set<String> reads = new HashSet<String>();

	public void readUnmapp(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				readid = readid.substring(0,readid.indexOf('_'));
				reads.add(readid);
				str = br.readLine();
			}
		}catch (Exception e) {
		}
	}
	
	public UnmapPrecision() {
		// TODO Auto-generated constructor stub
		String base = "E:/morehouse/o6/FormatConvert/Fusion/unmappble/bwa/FormatConvert/Fusion/maxprecision/filter/";
		readUnmapp(base+"crick_CT.txt");
		readUnmapp(base+"watson_CT.txt");
		readUnmapp(base+"watson_GA.txt");
		readUnmapp(base+"crick_GA.txt");
		System.out.println(reads.size());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UnmapPrecision();
	}

}
