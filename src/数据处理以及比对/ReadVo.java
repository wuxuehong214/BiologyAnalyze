package 数据处理以及比对;

/**
 * read information
 * @author wuxuehong
 * 2011-11-3
 */
public class ReadVo implements Comparable<ReadVo>{
	
	private String readID;
	private String refID;
	private int start;
	private int end;
	private int readLength;
	private long refLength;
	private String seq;
	private float mathcs;
	private String ciagr;
	
	public String getCiagr() {
		return ciagr;
	}
	public void setCiagr(String ciagr) {
		this.ciagr = ciagr;
	}
	public String getReadID() {
		return readID;
	}
	public void setReadID(String readID) {
		this.readID = readID;
	}
	public String getRefID() {
		return refID;
	}
	public void setRefID(String refID) {
		this.refID = refID;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getReadLength() {
		return readLength;
	}
	public void setReadLength(int readLength) {
		this.readLength = readLength;
	}
	public long getRefLength() {
		return refLength;
	}
	public void setRefLength(long refLength) {
		this.refLength = refLength;
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
	public int compareTo(ReadVo read) {
		// TODO Auto-generated method stub
		return (int)(read.getMathcs()*1000000-this.getMathcs()*1000000);
	}
	
	public String toString(){
		return readID+"\t"+refID+"\t"+start+"\t"+end+"\t"+readLength+"\t"+refLength+"\t"+mathcs+"\t"+ciagr+"\t"+seq;
	}
}
