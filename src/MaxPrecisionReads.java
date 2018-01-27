import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 * 将分割前的分析结果  与分割后的分析结果  合并 计算出每个 reads的最佳匹配长度
 * @author wuxuehong
 * 2012-2-2
 */
public class MaxPrecisionReads {
	
	/**
	 * 
	 * @param inputFile1   bwa分析后的各个reads最佳匹配结果
	 * @param inputFile2   bwa分析后reads进行进一步分割后  各个分割reads最佳匹配结果
	 * @param outFile      输出每个reads最佳匹配长度 bp数
	 */
	public void readAndOutput(String inputFile1,String inputFile2, String outFile){
		try{
			BufferedReader br1 = FileStreamUtil.getBufferedReader(inputFile1);
//			BufferedReader br2 = FileStreamUtil.getBufferedReader(inputFile2);
			BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
			
			Map<String,Reads> map = new HashMap<String,Reads>();
			String str = br1.readLine();
			Scanner s = null;
			String readid;
			int length;
			float matchics;
			int i = 0;
			Reads temp;
			while(str != null){
				s = new Scanner(str);
				readid = s.next();  //read id
				for(i=0;i<4;i++) s.next();
				length = s.nextInt();  //reads length
				s.next();
				matchics = Float.parseFloat(s.next()); //匹配率
				
				temp = map.get(readid);
				if(temp == null){
					temp = new Reads();
					temp.readid = readid;
					temp.length = length;
					temp.machLen = (int)(length*matchics);
					map.put(readid, temp);
				}
				
				str = br1.readLine();
			}
			br1.close();
			
			//读取分割后结果
//			str = br2.readLine();
//			while(str != null){
//				s = new Scanner(str);
//				readid = s.next().substring(0, 14);  //获取原始readsID
//				for(i=0;i<4;i++) s.next();
//				length = s.nextInt();  //reads length
//				s.next();
//				matchics = Float.parseFloat(s.next()); //匹配率
//				
//				temp = map.get(readid);
//				if( temp != null){
//					temp.machLen = temp.machLen+(int)(matchics*length);
//				}
//				
//				str = br2.readLine();
//			}
//			br2.close();
			
			//输出
			Iterator<Reads> it = map.values().iterator();
			while(it.hasNext()){
				temp = it.next();
				if(temp.machLen > temp.length)
					System.out.println("Exception .................................happens!!!");
				bw.write(temp.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class Reads{
		String readid;
		int length;
		int machLen;
		
		public String toString(){
			return readid+"\t"+length+"\t"+machLen;
		}
	}
	
	public MaxPrecisionReads(){
		readAndOutput("crick_CT.txt", "divide/bwa/FormatConvert/Fusion/maxprecision/crick_CT.txt", "maxprecisionreads/crick_CT.txt");
		readAndOutput("crick_GA.txt", "divide/bwa/FormatConvert/Fusion/maxprecision/crick_GA.txt", "maxprecisionreads/crick_GA.txt");
		readAndOutput("watson_CT.txt", "divide/bwa/FormatConvert/Fusion/maxprecision/watson_CT.txt", "maxprecisionreads/watson_CT.txt");
		readAndOutput("watson_GA.txt", "divide/bwa/FormatConvert/Fusion/maxprecision/watson_GA.txt", "maxprecisionreads/watson_GA.txt");
	}

	public static void main(String args[]){
		new MaxPrecisionReads();
	}
}
