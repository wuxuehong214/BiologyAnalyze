package 序列比对处理一;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Future;

import util.LogServer;

public class Main {
	//存储ID到BeanVo对象的映射
	private Map<String,BeanVo> beanMap = new HashMap<String,BeanVo>();
	//存储所有BeanVo对象
	private List<BeanVo> beans = new ArrayList<BeanVo>();

	public Main() throws IOException {
		// TODO Auto-generated constructor stub
		String path = "e:/研究生工作/B1(14-1)/B1(14-1)/";
		readFiles(path, 23);
		LogServer.log("reads Num"+beans.size()+"\t\t"+beanMap.size());

		//output format 1
		//outputFiles(path+"output/simple.txt", path+"output/detail.txt");
		//output format 2    separate files
		//B1-chr23-out.bsmapper.best
		outputFiles(path+"output/B1-chr","-out.bsmapper.best.filter", 23);
	}

	/**
	 * 批量读取文件信息
	 * @param path 文件路径
	 * @param N  文件个数
	 * @throws IOException 
	 */
	public void readFiles(String path,int N) throws IOException{
		for(int i=1 ; i<=N; i++ ){
			System.out.println("Reading..."+i);
			readFile(path+"hsoc-B1-chr"+i+"(14-1)/hsoc-B1-chr"+i+"-out.bsmapper.best", i);
		}
	}
	/**
	 * 读取单个文件
	 * @param filename 文件名称
	 * @throws IOException 
	 */
	public void readFile(String filename,int index) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		Scanner s = null;
		String id = null;
		BeanVo bean = null;
		while(str != null){
			s = new Scanner(str);
			id = s.next();    //获取riseID 
			bean = beanMap.get(id);  //查看ID 对应的BeanVo对象是否存在
			if(bean == null){           //如果不存在
				bean  = new BeanVo();   //创建该对象
				bean.setId(id);         //设置ID属性
				bean.setIndex(index);   //设置染色体编号
				try{
				for(int i=0;i<4;i++)s.next();   //忽略四个值
				}catch (Exception e) {
					System.out.println(id);
				}
				int a = s.nextInt();           //获取匹配序列的起始位置
				int b = s.nextInt();           //获取序列结束位置
				bean.setNum(b-a+1);            //获得精确匹配的序列数
				bean.getList().add(str);       //将原始信息保存
				for(int i=0;i<3;i++)bean.getList().add(br.readLine()); //保存接下来三行对应的原始信息
				beans.add(bean);            //保存bean对象
				beanMap.put(id, bean);      //存储ID到bean对象的映射
			}else{                         //如果当前ID对应的bean 对象已经存在 
				for(int i=0;i<4;i++)s.next();  //忽略接下来的四个值
				int a = s.nextInt();          //获取匹配序列的起始位置
				int b = s.nextInt();           //获取匹配的结束位置
				b = b-a+1;                     //获取精确匹配的序列数
				if(b>bean.getNum()){           //如果当前匹配的序列数 更大 则替换之前的bean对象，
					bean.setIndex(index);  //设置染色体编号
					bean.setNum(b);            //设置最新精确匹配的序列数
					bean.getList().clear();    //将原来bean对象的原始信息清除
					bean.getList().add(str);    //存储最新原始信息
					for(int i=0;i<3;i++)bean.getList().add(br.readLine());// 存储最新接下来三行对应的原始信息
				}else{
					for(int i=0;i<3;i++)br.readLine();  //如果当前精确匹配数量不是最大 则忽略接下来三行
				}
			}
			str = br.readLine();
		}
	}
	/**
	 * 输出统计信息到文件
	 * @param simpleFile 输出简单信息   readsID num 染色体编号
	 * 
	 * @param detailFile 输出详细信息 包括原格式的数据信息
	 * @throws IOException 
	 */
	public void outputFiles(String simpleFile,String detailFile) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(simpleFile)));
		int num = beans.size();
		BeanVo bean = null;
		for(int i=0 ; i<num; i++){
			bean = beans.get(i);
			bw.write(bean.getId()+"\t"+bean.getNum()+"\thchr"+bean.getIndex()+".fa");
			bw.newLine();
		}
		bw.flush();
		bw.close();
		LogServer.log("Output simple file successfully!");
		
		bw = new BufferedWriter(new FileWriter(new File(detailFile)));
		for(int i=0; i<num; i++){
			bean = beans.get(i);
			bw.write(bean.getList().get(0)); //+"\t"+bean.getNum()+"\thchr"+bean.getIndex()+".fa"
			bw.newLine();
			for(int j=1; j<4; j++){
				bw.write(bean.getList().get(j));
				bw.newLine();
			}
		}
		bw.flush();
		bw.close();
		LogServer.log("Output detail file successfully!");
	}
	
	/**
	 * 将beans写到 num个文件中去，
	 * 每个 
	 * @param filename  文件名称 
	 * @param num  文件个数（染色体个数）
	 * @throws IOException 
	 * @hsoc-B1-chr23-out.bsmapper.best
	 */
	public void outputFiles(String postfix,String suffix,int num) throws IOException{
		BufferedWriter[] bw = new BufferedWriter[num];
		for(int i=0;i<num;i++){
			bw[i] = new BufferedWriter(new FileWriter(new File(postfix+(i+1)+suffix)));
		}
		int size = beans.size();
		int index = 0;
		BeanVo bean = null;
		for(int i=0; i<size; i++){
			bean = beans.get(i);
			index = bean.getIndex();
			bw[index-1].write(bean.getList().get(0));
			bw[index-1].newLine();
			for(int j=1; j<4; j++){
				bw[index-1].write(bean.getList().get(j));
				bw[index-1].newLine();
			}
		}
		for(int i=0;i<num;i++){
			bw[i].flush();
			bw[i].close();
		}
		LogServer.log("Out put files successfully!");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try {
				new Main();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
