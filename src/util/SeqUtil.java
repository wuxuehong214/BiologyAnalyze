package util;

/**
 * ÐòÁÐ´¦Àí
 * @author wuxuehong
 * 2011-11-3
 */
public class SeqUtil {
	
	/**
	 * C->T
	 * @param seq
	 * @return
	 */
	public static String C2T(String seq){
		return seq.replaceAll("C", "T");
	}
	/**
	 * G->A
	 * @param seq
	 * @return
	 */
	public static String G2A(String seq){
		return seq.replaceAll("G", "A");
	}
	
	/**
	 * ×Ö·û´®·´Ðò
	 * @param seq
	 * @return
	 */
	public static String reserve(String seq){
		char[] c = seq.toCharArray();
		int len = c.length;
		int first = 0;
		int last = len-1;
		char temp ;
		while(first<last){
			temp = c[first];
			c[first] = c[last];
			c[last] = temp;
			first++;
			last--;
		}
		return String.valueOf(c);
	}
	
	/**
	 * A-T C-G»¥²¹ 
	 * @param ch
	 * @return
	 */
	public static char getChar(char ch){
		if(ch=='A') return 'T';
		if(ch=='T') return 'A';
		if(ch=='G') return 'C';
		if(ch=='C') return 'G';
		return ch;
	}
	/**
	 * A-T C-G»¥²¹
	 * @param seq
	 * @return
	 */
	public static String complement(String seq){
		char[] c = seq.toUpperCase().toCharArray();
		int len = c.length;
		for(int i=0;i<len;i++){
			c[i] = getChar(c[i]);
		}
		return String.valueOf(c);
	}
	

}
