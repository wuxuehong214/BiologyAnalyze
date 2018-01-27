package 数据处理以及比对;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import util.FileStreamUtil;

public class ReadDeal {

	public ReadDeal() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 454CT：将454reads中的C转化成T
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void C2T(String inputFile,String outFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		while(str != null){
			if(!str.startsWith(">")){
				str = str.replaceAll("C", "T");
			}
			bw.write(str);
			bw.newLine();
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}
	
	/**
	 * 454GA：将454reads中的G转化成A
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void G2A(String inputFile,String outFile)throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		while(str != null){
			if(!str.startsWith(">")){
				str = str.replaceAll("G", "A");
			}
			bw.write(str);
			bw.newLine();
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}
	
	public static void main(String args[]){
		ReadDeal rd = new ReadDeal();
		try {
			rd.C2T("E:/研究生工作/personal data/ocsc1.GAC.454Reads.fa", "E:/研究生工作/personal data/454ReadsC2T.fa");
			rd.G2A("E:/研究生工作/personal data/ocsc1.GAC.454Reads.fa", "E:/研究生工作/personal data/454ReadsG2A.fa");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
