package PE2处理;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLClientInfoException;

import util.*;

public class PEDeal {
	
	
	/**
	 * 正序    序列处理  fastq格式
	 * @param inputFile
	 * @param outFile
	 * @throws IOException
	 */
	public void inputAndOutputByorder(String inputFile, String outFile) throws IOException{
		BufferedReader br = FileStreamUtil.getBufferedReader(inputFile);
		BufferedWriter bw = FileStreamUtil.getBufferedWriter(outFile);
		String str = br.readLine();
		while(str != null){
			if(str.startsWith("@")){
				bw.write(str);        //序列ID
				bw.newLine();
				//序列处理
				str = br.readLine();   //序列\
				
				//序列处理开始
//				str = SeqUtil.C2T(str);  //C-T
//				str = SeqUtil.G2A(str);  //G-A
//				str = SeqUtil.reserve(str);  str = SeqUtil.C2T(str); //反序 C->T
//				str = SeqUtil.reserve(str);  str = SeqUtil.G2A(str);  //反序 G->A
//				str = SeqUtil.complement(str);  str = SeqUtil.C2T(str);  //互补C->T
//				str = SeqUtil.complement(str);  str = SeqUtil.G2A(str);  //互补G->A
//				str = SeqUtil.reserve(str);  str = SeqUtil.complement(str); str = SeqUtil.C2T(str); //反序  互补 C->T
				str = SeqUtil.reserve(str);  str = SeqUtil.complement(str); str = SeqUtil.G2A(str); //反序 互补G->A
//				序列处理结束
				
				bw.write(str);
				bw.newLine();
				
				bw.write(br.readLine());  //序列的描述信息
				bw.newLine();
				bw.write(br.readLine());  //序列质量评价
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
			PEDeal pd = new PEDeal();
			pd.inputAndOutputByorder("E:/研究生工作/PE1PE2/Crick/PE_1.fastq",
					"E:/研究生工作/PE1PE2/Crick/PE_1_RCG2A.fastq");
			//test
//			pd.inputAndOutputByorder("d:/test.txt", "d:/reserve_com_C2T.TXT");
	}

}
