package statics;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import util.FileStreamUtil;

/**
 * reads中C的个数：
   reads中CG的个数：
   Ref中C的个数：
   Ref中CG的个数：
 * @author wuxuehong
 * 2012-1-3
 */
public class Cnumber {
	
	int readc=0,readcg=0;
	int refc=0,refcg=0;
	
	/**
	 * 读取reads中C 以及 CG 的个数。
	 * @param filename
	 */
	public void readReads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			StringBuffer sb = new StringBuffer("");
			while(str != null){
				if(str.startsWith(">")){
					if(sb.toString().length()!=0){
						char[] ch = sb.toString().toCharArray();
						for(int i=0;i<ch.length;i++){
							if(ch[i]=='C'){
								readc++;
								if(i+1<ch.length&&ch[i+1]=='G')
									readcg++;
							}
						}
					}
					sb = new StringBuffer("");
				}else{
					sb.append(str.trim());
				}
				str = br.readLine();
			}
			
			if(sb.toString().length()!=0){
				char[] ch = sb.toString().toCharArray();
				for(int i=0;i<ch.length;i++){
					if(ch[i]=='C'){
						readc++;
						if(i+1<ch.length&&ch[i+1]=='G')
							readcg++;
					}
				}
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 计算ref中C以及CG的个数
	 * @param filename
	 */
	public void readRef(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			str = br.readLine();
			boolean headc = false;
			while(str != null){
				if(headc) {      //如果上一行结尾是c  则c个数减一
					refc--;
					str = "C"+str;
				}
				char[] c = str.toCharArray();
				for(int i=0;i<c.length;i++){
					if(c[i]=='C'||c[i]=='c'){
						refc++;
						if(i+1<c.length&&(c[i+1]=='G'||c[i+1]=='g'))
							refcg++;
					}
				}
				if(c[c.length-1]=='c'||c[c.length-1]=='C')
						headc = true;
				
				str = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Ref中C的个数:579913581
	   Ref中CG的个数:39039314
	   Ref中CA的个数：218299536
	 */
	public Cnumber() {
		// TODO Auto-generated constructor stub
		readReads("E:/研究生工作/personal_data/2010.GAC.454Reads/2010.GAC.454Reads.fa");
		System.out.println("Reads中C的个数:"+readc);
		System.out.println("Reads中CG的个数:"+readcg);
		
//		for(int i=1;i<=22;i++){
//			readRef("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_Chr"+i+".txt");
//			System.out.println(i+"\t\t\t"+refc);
//		}
//		readRef("E:/研究生工作/personal_data/repeatchr1-22/HumanGeneRepeat/hg19_ChrX.txt");
//		System.out.println("Ref中C的个数:"+refc);
//		System.out.println("Ref中CG的个数:"+refcg);
		System.out.println((float)(readc-readcg)/(float)(579913581-39039314));
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Cnumber();
	}

}
