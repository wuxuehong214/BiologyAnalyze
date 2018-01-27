import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

/**
 * 统计 分割情况后    所有reads匹配上的Bp总数
 * @author wuxuehong
 * 2012-2-2
 */
public class ReadsBP {
	
	Map<String, Integer> map = new HashMap<String, Integer>();
	int total = 0;
	
	public void readMappedReads(String inputFile){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
			String str = br.readLine();
			Scanner s = null;
			String readid;
			int matchlen;
			while(str!=null){
				s = new Scanner(str);
				readid = s.next();
				s.next();
				matchlen = s.nextInt();
				if(map.get(readid)==null || map.get(readid).intValue()<matchlen)
					map.put(readid, matchlen);
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ReadsBP() {
		// TODO Auto-generated constructor stub
		readMappedReads("crick_CT.txt");
		readMappedReads("crick_GA.txt");
		readMappedReads("watson_CT.txt");
		readMappedReads("watson_GA.txt");
		Iterator<Integer> it = map.values().iterator();
		while(it.hasNext()){
			total+=it.next().intValue();
		}
		System.out.println("total mapped bps:"+total);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ReadsBP();
	}

}
