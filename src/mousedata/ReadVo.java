package mousedata;

/**
 * read information
 * @author wuxuehong
 * 2011-11-3
 */
public class ReadVo implements Comparable<ReadVo>{
	
	private String readID;
	private String seq;
	private float mathcs;
	
	public String getReadID() {
		return readID;
	}
	public void setReadID(String readID) {
		this.readID = readID;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public float getMathcs() {
		return mathcs;
	}
	public void setMathcs(float mathcs) {
		this.mathcs = mathcs;
	}
	@Override
	public int compareTo(ReadVo read) {
		// TODO Auto-generated method stub
		return (int)(read.getMathcs()*1000000-this.getMathcs()*1000000);
	}
}
