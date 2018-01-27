package HummanGeneDeal_step1;


/**
 * 匹配后的read 信息
 * @author wuxuehong
 * 2011-12-30
 */
public class ReadVo implements Cloneable,Comparable<ReadVo>{
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return seq.hashCode();
	}

	@Override
	protected Object clone(){
		// TODO Auto-generated method stub
		try{
		return super.clone();
		}catch (Exception e) {
			return null;
		}
	}
	
	public String toString(){
		return readid+"\t"+humgene+"\t"+chr+"\t"+start+"\t"+end+"\t"+length+"\t"+chrlen+"\t"+matchics+"\t"+cigar+"\t"+seq;
	}
	private String readid;  //read ID
	private String humgene;  //人类基因组编号
	private String chr; //所属染色体编号
	private long start;  //匹配的起始位置 
	private long end;   //匹配的结束位置
	private int length;  //序列长度
	private long chrlen ; //染色体长度
	private float matchics; //匹配比例
	private String cigar; //  CIGAR 序列匹配情况
	private String seq;  //序列
	public String getReadid() {
		return readid;
	}
	public void setReadid(String readid) {
		this.readid = readid;
	}
	public String getHumgene() {
		return humgene;
	}
	public void setHumgene(String humgene) {
		this.humgene = humgene;
	}
	public String getChr() {
		return chr;
	}
	public void setChr(String chr) {
		this.chr = chr;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getChrlen() {
		return chrlen;
	}
	public void setChrlen(long chrlen) {
		this.chrlen = chrlen;
	}
	public float getMatchics() {
		return matchics;
	}
	public void setMatchics(float matchics) {
		this.matchics = matchics;
	}
	public String getCigar() {
		return cigar;
	}
	public void setCigar(String cigar) {
		this.cigar = cigar;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}

	public boolean equals(Object obj) {
		ReadVo r = (ReadVo)obj;
		return seq.equals(r.getSeq());
	}

	@Override
	public int compareTo(ReadVo read) {
		// TODO Auto-generated method stub
		return (int)(read.getMatchics()*1000000-this.getMatchics()*1000000);
	}
}
