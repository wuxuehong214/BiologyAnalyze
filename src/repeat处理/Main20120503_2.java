package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * 进一步过滤M位置上Alu  以及 simple repeat
 * @author wuxuehong
 * 2012-5-3
 */
public class Main20120503_2 {
	
	byte[] flag = new byte[51304567]; 
	byte[] flag2 = new byte[51304567];
	
	public void readCNV(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine();
			str = br.readLine();
			Scanner s = null;
			int start,end;
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
				//如果是 alu 或者simplerepeat 则表明为1
				if(family.equals("Alu") || family.equals("Simple_repeat")){
					for(int i=start;i<=end;i++)
						flag[i] = 1;
				}
				str = br.readLine();
			}
			br.close();
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
				for(int i=0;i<c.length;i++){
					if(c[i]=='M' ){
						flag2[start+i] = 'M';
					}
				}
				boolean filter = true;
				for(int i=start;i<=end;i++){
					if(flag2[i] =='M' && flag[i]!=1){
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
				
				for(int i=0;i<c.length;i++){
					if(c[i]=='M' ){
						flag2[start+i] = 0;
					}
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
	public Main20120503_2(){
		readCNV("K:/bwa/chr22-repeats.txt");
		String inputBase = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/LAPX_filter/L1PA3L1M4c_filter/CNV_filter/";
		filterReads(inputBase+"Chr22_w+_CNV_filter.txt", inputBase+"Chr22_w+_CNV_filter2.txt");
	}
	
	public static void main(String args[]){
		new Main20120503_2();
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
}
