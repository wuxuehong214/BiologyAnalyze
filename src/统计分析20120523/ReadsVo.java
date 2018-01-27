package Í³¼Æ·ÖÎö20120523;

public class ReadsVo {
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		ReadsVo r = (ReadsVo)obj;
		return r.readid.equals(readid);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return readid.hashCode();
	}
	String readid;
	int len;
	float matchics;
	String cigra;
	int times = 0;
	int start,end;
	String chr;
	byte[] flag;

}
