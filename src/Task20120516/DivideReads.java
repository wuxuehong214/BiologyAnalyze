package Task20120516;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import util.FileStreamUtil;

public class DivideReads {

	/**
	 * 
	 * 在repeat (alu, simple, CNV, low-complexity, LI...)flter的基础上
	 * 将reaads中大于等于31bp未匹配的部分拆分出来
	 * 
	 */
private Map<String,String> readid2seq = new HashMap<String,String>();
	
	
	public DivideReads(){
		readOriginreads("E:/morehouse/o1o6reads/o6-ab.txt");
		String path = "E:/morehouse/z=50/06/MUTIMAP_FILTER/";
		File file = new File(path);
		File[] files = file.listFiles();
		for(int i=0;i<files.length;i++){
			File p = files[i];
			System.out.println(p.getName());
			dividReads(p, "E:/morehouse/z=50/06/DIVIDE/"+p.getName());
		}
	}
	/**
	 * 读取原始reads信息
	 * @param filename
	 */
	public void readOriginreads(String filename){
		try{
			BufferedReader br = FileStreamUtil.getBufferedReader(filename);
			String str = br.readLine();
			String readid="",seq="";
			while(str != null){
				if(str.startsWith(">")){
					if(seq.length()!=0){
						readid2seq.put(readid, seq);
					}
					readid = str.substring(1,str.indexOf("rank")-1);
					seq = "";
				}else{
					seq += str.trim();
				}
				str = br.readLine();
			}
			if(seq.length()!=0)
				readid2seq.put(readid, seq);
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param inputFile 输入文件
	 * @param outFile  输出文件
	 */
	public void dividReads(File inputFile,String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			str = br.readLine();
			String readid;
			String cigra;
			String seq;
			Scanner s = null;
			int len;
			Map<String,Integer> map = new HashMap<String,Integer>();
			while(str != null){
				s = new Scanner(str);
				readid = s.next();
				for(int i=0;i<9;i++)s.next();
				seq = readid2seq.get(readid);
				cigra = s.next();
				len = seq.length();
				
				char[] c = getChars(cigra, len);
				char[] sc = seq.toCharArray();
				if(c.length != sc.length){
					System.err.println("wrong!");
					break;
				}
				StringBuffer sb = new StringBuffer();
				char last=0,cur=0;
				boolean isM = false;
				for(int i=0;i<c.length;i++){
					cur = c[i];
					if(cur == 'S'){
						sb.append(sc[i]);
						isM = false;
					}else{
						if(!isM)
							sb.append(",");
						isM = true;
					}
				}
				Integer index = map.get(readid);
				if(index == null){
					index = 1;
					map.put(readid, 1);
				}else{
					map.put(readid, index+1);
					index++;
				}
					
				String[] ss = sb.toString().split(",");
				for(int i=0;i<ss.length;i++){
					if(ss[i].length()<=34)continue;
					bw.write(">"+readid+index+"-"+(i+1));
					bw.newLine();
					bw.write(ss[i]);
					bw.newLine();
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
	
	/**
	 * 将reads匹配的详细信息转换成 字符数组  
	 * 其中D忽略
	 * I转换成M
	 * @param cigra
	 * @param readslen
	 * @return
	 */
	private char[] getChars(String cigra,int readslen){
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
	
	public static void main(String args[]){
		new DivideReads();
	}
	
}
