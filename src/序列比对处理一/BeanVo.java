package 序列比对处理一;

import java.util.ArrayList;
import java.util.List;

public class BeanVo {
	
	private int index; //染色体编号
	
	private String id; //reads ID
	
	private int num;  //精确匹配序列数目
	
	private List<String> list; //原始信息
	
	public BeanVo(){
		list = new ArrayList<String>();
	}
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
	
	

}
