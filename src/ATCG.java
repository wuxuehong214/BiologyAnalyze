
public class ATCG {
	
	/**
	 * A-T C-G»¥²¹  ½«C»»³ÉT
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
	
	public ATCG(){
		String str = "ACACAAACACATAAACACACAAATACACACACAAACTCACATAAACACACACTCACAAAAACACACACAAACAACACAAACATACACAAACACAAACACAAACACACAAACACACAAATACACACACAAACACAAACACACACAAAACACACAAAAAATA";
		StringBuffer sb = new StringBuffer(str);
		str = sb.reverse().toString();
		String[] s = reserve(str);
		for(int i=0;i<s.length;i++){
			System.out.println(s[i]);
		}
	}
	
	public static void main(String args[]){
		new ATCG();
	}

	public String[] reserve(String str){
		char[] ch = str.toCharArray();
		for(int i=0;i<ch.length;i++){
			ch[i] = getChar(ch[i]);
		}
		String ss = String.valueOf(ch);
		int len = 0;
		if(ss.length()%60==0)len = ss.length()/60;
		else len = ss.length()/60+1;
		String[] r = new String[len];
		for(int i=0;i<len;i++){
			r[i] = ss.substring(i*60,(i*60+60)<ss.length()?((i*60)+60):ss.length());
		}
		return r;
	}
	
	
}
