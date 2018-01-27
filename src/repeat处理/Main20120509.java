package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * 这次任务  删除 ERVL-MaLR  L1MXX and L1PXX
 * @author wuxuehong
 * 2012-5-9
 */
public class Main20120509 {

	byte[] flag = new byte[51304567]; 
	private int length = 51304566;
	/**
	 * W+
	 * @param filename
	 */
	public void readRepeatPos(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine(); //忽略第一行
			str = br.readLine();
			Scanner s = null;
			int start ,end;
			String family;
			String classr;
			String name;
			while(str != null){
				s = new Scanner(str);
				for(int i=0;i<6;i++)s.next();
				start = s.nextInt();
				end = s.nextInt();
				for(int i=0;i<2;i++)s.next();
				name = s.next();
				classr = s.next();
				family = s.next();
				if(name.startsWith("L1M") || name.startsWith("L1P") || family.equals("ERVL-MaLR")){
	System.out.println(name+"\t"+family);
					for(int i=start;i<=end;i++)
						flag[i] = 1;
				}
				str = br.readLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void filterReads(String inputFile, String outFile){
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
			int start, end, len;
			String cigra;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
				len = s.nextInt();
				s.next();s.next();
				cigra = s.next();
				char[] c = getChars(cigra,  len);
				
				//默认该reads是要被删除 过滤掉的
				boolean filter = true;
				for(int i=0;i<c.length;i++){
					//如果该reads中油一个字母不是要删除的repeats 那么 该reads就不需要被过滤
					if(c[i]=='M' && flag[start+i] == 0){
						filter = false;
						break;
					}
				}
				if(!filter){
					bw.write(str);
					bw.newLine();
				}else{
					System.out.println("filter");
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
	
	public char[] getChars(String cigra,int readslen){
		int totalD = 0;
	  try{
		char[] c = cigra.toCharArray();
		char[] r = new char[readslen];
		int index = 0;
		int count = 0;
 		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				count = count*10+c[i]-'0';
			}else if(c[i]=='M'){
				for(int j=index;j<index+count;j++)r[j]='M';
				index+=count;
				count=0;
			}else if(c[i]=='D'){
				//for(int j=index;j<index+count;j++)r[j]='D';
				//index+=count;
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='M';
				index+=count;
				count=0;
			}else if(c[i]=='S'){
				for(int j=index;j<index+count;j++)r[j]='S';
				index+=count;
				count=0;
			}else 
				System.out.println("exception !!!!");
		}
		return r;
	  }catch(Exception e){
		  System.out.println(cigra+"\t"+readslen);
		  System.out.println("Total D$$$$$$$$$$$$$$$:"+totalD);
		  return null;
	  }
	}
	
	public Main20120509(){
		String path = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/L1PA3L1M4c_filter/CNV_filter/ABfilter/";
		readRepeatPos("K:/bwa/chr22-repeats.txt");
		filterReads(path+"Chr22_w+_AB_filter.txt", path+"L1ML1P_Filter/Chr22_w+_L1ML1P_filter.txt");
	}
	
	public static void main(String args[]){
		new Main20120509();
	}
	
}
