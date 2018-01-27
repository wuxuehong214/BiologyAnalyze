package repeat处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 如果一个reasd 落入了seq-dups-chr22.txt 文件给予的区域里面 就再Chr22_w+_filter-line.txt
 * 文件中 响应的reads后面加入4列
 * @author wuxuehong
 * 2012-4-25
 */
public class Main20120425_2 {
	
	byte[] flag = new byte[51304567]; 
	private int length = 51304566;
	
	String[] value = new String[4000];
	
	class Hang{
		int start;
		int end;
		String value;
	}
	
	List<Hang> hangs = new ArrayList<Hang>();
	/**
	 * W+
	 * @param filename
	 */
	public void readDups(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String str = br.readLine(); //忽略第一行
			str = br.readLine();
			Scanner s = null;
			String ab, mb, msb, fm;
			while(str != null){
				s = new Scanner(str);
				Hang hang = new Hang();
				s.next();s.next();
				hang.start = s.nextInt();
				hang.end = s.nextInt();
				for(int i=0;i<17;i++)s.next();
				ab = s.next();
				mb = s.next();
				msb = s.next();
				s.next();s.next();
				fm = s.next();
				
				hang.value=ab+"\t"+mb+"\t"+msb+"\t"+fm;
				hangs.add(hang);
				str = br.readLine();
			}
System.out.println(hangs.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void filterReads(String inputFile, String outFile, String outFile2){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(outFile2)));
			String str = br.readLine();
			if(str.startsWith("ReadID")){
				bw.write(str+"\t");
				bw.write("alignB\tmatchB\tmismatchB\tfracMatch");
				bw.newLine();
				
				bw2.write(str);
				bw2.newLine();
				str = br.readLine();
			}
			int start,end;
			Scanner s = null;
			Hang hang;
			boolean t = false;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();s.next();
				start = s.nextInt();
				end = s.nextInt();
				t = false;
				for(int i=0;i<hangs.size();i++){
					hang = hangs.get(i);
//					if(start>hang.end)continue;
//					if(hang.end-start < (end-start)) continue;
//					if(end<hang.start)break;
//					if(end-hang.start < (end-start) )  break;
					if(start>=hang.start && end<=hang.end){
						t = true;
						bw.write(str+"\t"+hang.value);
						bw.newLine();
					}
				}
				if(!t){
						bw2.write(str);
						bw2.newLine();
				}
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
			
			bw2.flush();
			bw2.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main20120425_2(){
		String path = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/";
		String outPath = "K:/bwa/z=50/01/z=50alusimpleFilter_line01/L1HS_filter/dupsfilter/";
		
		readDups("K:/bwa/seq-dups-chr22.txt");
		filterReads(path+"Chr22_w+_filter-line.txt", outPath+"Chr22_w+_filter-line.txt",outPath+"Chr22_w+_filter-line_filter.txt");
	}
	
	public static void main(String args[]){
		new Main20120425_2();
	}

}
