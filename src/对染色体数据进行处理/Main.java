package 对染色体数据进行处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import util.FileStreamUtil;

public class Main {

	public Main() throws IOException {
		// TODO Auto-generated constructor stub
		readAndOutput("E:\\研究生工作\\染色体数据以及测试数据\\hchr22.fa", "E:\\研究生工作\\染色体数据以及测试数据\\hchr22_filter.fa");
	}
	
	
	/**
	 * 转换字符串 将其中非CG连续的C 换成T    最后一个字符不处理
	 * @param str
	 * @return
	 */
	public String convert(String str){
		char[] c = str.toCharArray();
		int l = c.length;
		for(int i=0;i<l-1;i++){
			if(c[i]=='C' && c[i+1]!='G'){
				c[i] = 'T';
			}
		}
		return String.valueOf(c);
	}
	/**
	 * 将字符串中最后一个字符C换成T
	 * @param str
	 * @return
	 */
	public String convertLast(String str){
		char[] c = str.toCharArray();
		int l = c.length;
		c[l-1] = 'T';
		return String.valueOf(c);
	}
	
	public void readAndOutput(String inputFile,String outFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();   //读取第一行的以>开头的注释信息
		bw.write(str);       //将第一行数据写入到输出文件
		bw.newLine();       //换行
		str = br.readLine();  //读取第二行数据
		String prefix = null;  //用于存储前一行数据     当前一行数据中最后一个字符是C时 则用该变量存储字符串
		while(str != null){
			//如果上一行有数据 先处理上一行
			if(prefix != null){
				//如果前一行字符串中最后一个字符为C
				if(str.charAt(0)=='G'){  //如果当前行第一个字符是G则 直接将上一行数据输出到输出文件即可
					bw.write(prefix);
					bw.newLine();
				}else{   //如果第一个字符不是G 则将上一行最后一个字符C 换成T 并输出到输出文件
					prefix = convertLast(prefix);
					bw.write(prefix);
					bw.newLine();
				}
			}
			//处理当前行数据
			    str = convert(str); //
				if(str.charAt(str.length()-1)=='C'){ //如果最后一个字符是C
					prefix = str;         //将其存储到变量prefix中
				}else{                    //如果最后一个字符不是C
					prefix = null;        //将变量prefiz置为null
					bw.write(str);
					bw.newLine();
				}
			str = br.readLine();
		}
		br.close();
		bw.flush();
		bw.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try {
				new Main();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
