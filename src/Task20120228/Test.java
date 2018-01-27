package Task20120228;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;

public class Test {

	String base2 = "D:/recover/研究生工作/personal_data/repeatchr1-22/WC/";
	String reads = "GTGTTGGGTATGGTTTTTAGTGTGTATGTTGATGATATTGTTGTGTAGATTTGTTAGTATGGTGGTTTGGTTGGTGTGATGTTATTGATGTTGGGTTGGGTTGTAGTGATTGGTATGTAGGGTTTGTTGGATTGGGTGTATTTGTTGTTGTTGGTGTTGATTATTTTTATTTGGATGTTGTTGTATTTTTGGGGGAGATTTGTGTTGAATTAGTTTAGTTGTGTTTGGGGGTAGGGAGAGTATAGGTTTTTTTTAATTTAAAAATAATCGTTATGTGTTAAAATTTTTGTAAAATGTGTTGAGTTGTAGATTATAGTAGTGAATTTGTTTGTTTTTGAAGTTAATTATTTTTTGTTTAGTGATAGATGGTTAGTAAAAATGGTCGTGGAGTTGTTTGTTTTTGGAAGTTGGGTAAGGTGGTTTTTTTTTAATTGTTATTTAAATGCTTTTTATATTAAGGTAGTTTTCGTTTTTTAGGTTGGTTGTTGAGTAGTGTTTTTTAGGTAGTTGTGTTGATTG";
	String seq = "TTTTAGGGGAGATTTGTGCTGAATCAGCTCAGCTGTGTTTGGGGGTAGGGAGAGGACAGGCTTTTTTTCCCACTTTAAAAATAACCCTTATGTGCCAAAAAACCCTATAAAATGTGCTGAATTGCAAATCTTCCAACTTCCTATACCTCCTGGTTTACAGAGAATAGATTCTTTTGAGCTTGGGCTTGATGATACTGTGCAGAATATCTTATGTTGTAGACGAGTGTGAAGTGAGGCTGGATGCAGATGTGCTCATTTGGAAAGGAAACCTGACTATTGCTATCTTTTTTCAGTATTTTTTTCTCCAGCATACGATCAGTTTTGTCCACTTACAAGGATCTATGTAGGGATACTAGATATAAATTTTCTAATGAGAAAAACGTTTTCTTGTGTATCTGCAAAATAACCCATATCATTCAGAAAGAGTCCAATTGGAAAGCACCAACATTCCAGGGTTTCTATGTAAGTGGATCTCTTTTTGTTATGCTCTGTAAAGTTGGGCTAATTGTTTAGGTTGCTTGAATATTTCTTTCCCAAGG";
	String cigra = "186S60M3D33M1D29M211S";
	
	String str = "HEZ68ZB01AH1BSa	0	HG19_DNA	11620037	0	65S34M14S	*	0	0	GTTTTTGTGTATGTGTGTGTATGGTGTATTGTGTGTGTATGTGTGTATGTATGTGTATGTATGTATATGTGTATGTATGTGTATGTGTGTATGTGTGTATGTGTTTGTGTTTG	*	AS:i:30	XS:i:0	XF:i:2	XE:i:0	XN:i:0";
	int offset = 20;
	public  Test(){
//		try {
//			String seq = RefUtil.refReadsAndFilterRuelst(base2+"W_hg19_Chr2.txt", 22902933, 22902982, RefUtil.NEG);
//			System.out.println(seq);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		int of = getOffset(cigra);
		
		char[] cr = reads.toCharArray();
		char[] cseq = seq.toCharArray();
		char[] cig = getChars(cigra, 519+offset);
		int index = 0;
		for(int i=0;i<of;i++){
			System.out.print('*');
		}
		
		for(int i=of;i<cig.length;i++){
			if(cig[i]==0)break;
			if(cig[i] == 'I'){
				System.out.print('*');
			}else{
				System.out.print(cseq[index++]);
			}
		}
		System.out.println();
		System.out.println(String.valueOf(cig));
		index = 0;
		for(int i=0;i<cig.length;i++){
			if(cig[i] == 'D'){
				System.out.print('*');
			}else{
				if(index<cr.length)
				System.out.print(cr[index++]);
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t = new Test();
//		StringBuffer sb = new StringBuffer();
//		sb.append("TTATATATATAATTATATATATATATATAATTTATATATAA");
//		String str = sb.reverse().toString();
//		char[] c = str.toCharArray();
//		for(int i=0;i<c.length;i++){
//			c[i] = t.getChar(c[i]);
//		}
//		System.out.println(String.valueOf(c));
	}
	
	/**
	 * A-T C-G互补  将C换成T
	 * @param ch
	 * @return
	 */
	public char getChar(char ch){
		if(ch=='A') return 'T';
		if(ch=='T') return 'A';
		if(ch=='G') return 'C';
		if(ch=='C') return 'G';
		return ch;
	}
	
	public int getOffset(String cigra){
		int total = 0;
		char flag = 0;
		char[] c = cigra.toCharArray();
		for(int i=0;i<c.length;i++){
			if(c[i]>='0'&&c[i]<='9'){
				total = total*10+(c[i]-'0');
			}else{
				flag = c[i];
				break;
			}
		}
		if(flag =='M')return 0;
		else return total;
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
				for(int j=index;j<index+count;j++)r[j]='D';
				index+=count;
				totalD+=count;
				count=0;
			}else if(c[i]=='I'){
				for(int j=index;j<index+count;j++)r[j]='I';
				index+=count;
				totalD+=count;
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
		  e.printStackTrace();
		  System.out.println(cigra+"\t"+readslen);
		  System.out.println("Total D$$$$$$$$$$$$$$$:"+totalD);
		  return null;
	  }
	}
}
