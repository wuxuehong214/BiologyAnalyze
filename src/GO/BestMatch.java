package GO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 根据known.txt中的已知蛋白质复合物 
 * 去 静态蛋白质复合物 以及动态蛋白质复合物中分别寻找一个最佳匹配结果并且输出
 * @author wuxuehong
 *
 * 2012-3-27
 */
public class BestMatch {
	
	public void readAndOutput(String inputFile, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str =  br.readLine();
			int index = 1;
			while(str != null){
				if(str.startsWith("Complex")){
					bw.write("Complex\t"+(index++));
					bw.newLine();
				}else{
					bw.write(str);
					bw.newLine();
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	String base = "G:/personal/我的文章/BMC/BMC实验补充/BMC实验结果/Response2Nature/";
	public BestMatch() {
		// TODO Auto-generated constructor stub
		readAndOutput(base+"nature.txt", base+"out.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BestMatch();
	}

}
