package 统计分析;

import java.io.BufferedReader;
import java.util.Scanner;

import util.FileStreamUtil;


/**
 * 统计ref中所有CG 的甲基化率
 * 即： 甲基化的CG个数 除以ref中总的CG个数
 * @author wuxuehong
 * 2012-1-11
 */
public class CpG {
	
	//#CGr2-Ref中CG的个数:39039314
	//#CGr2-Ref中CA的个数:218299536
	private int totalCg = 39039314;
	private int totalCa = 218299536;
	private int mCg = 0;
	
	//consisitent率超过0.5则认为该位置上甲基化了
	private float door = 0.5f;

	//读取计算了consistent率的文件
	public void readCg(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			Scanner s = null;
			float m;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();s.next();
				m = Float.parseFloat(s.next());
				if(m>=door)
					mCg++;
				str = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public CpG() {
		for(int i=1;i<=22;i++){
			System.out.println("reading:"+i);
			readCg("CA_consistent/hg19_Chr"+i+".txt");
		}
		readCg("CA_consistent/hg19_ChrX.txt");
		
		System.out.println();
		System.out.println("Cm:"+mCg);
		System.out.println("Cm+Co:"+totalCg);
		System.out.println("CpA:"+(float)mCg/(float)totalCa);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CpG();
	}

}