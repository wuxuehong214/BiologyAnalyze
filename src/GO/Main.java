package GO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	
	public void readAndOutput(String filename,String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			for(int i=0;i<8;i++) str = br.readLine();
			Scanner s = null;
			String syname,genname = null;
			String syg = "";
			String[] temp = null ;
			while(str != null){
				s = new Scanner(str);
				s.next();s.next();
				syname = s.next();
				if(!syg.equals(syname)){
//					while(s.hasNext()){
//						genname = s.next();
//						if(genname.equals("gene") && s.next().startsWith("taxon")){
//							genname = temp;
//							break;
//						}else
//							temp = genname;
//					}
					temp = str.split("\t");
					genname = temp[temp.length-5];
					bw.write(genname+"\t"+syname);
					bw.newLine();
				}
				syg = syname;
				str = br.readLine();
			}
			br.close();
			bw.flush();
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	Map<String,String> map = new HashMap<String,String>();
	Set<String> proteins = new HashSet<String>();
	public void readAndOutpu2(String inputFile,String filename, String outFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile)));
			String str = br.readLine();
			Scanner s = null;
			String[] genes;
			String sym;
			while(str != null){
				try{
				s = new Scanner(str);
				genes  = s.next().split("\\|");
				sym = s.next();
				for(int i=0;i<genes.length;i++){
					map.put(genes[i], sym);
				}
				}catch (Exception e) {
					System.err.println(str);
				}
				str = br.readLine();
			}
		br.close();
		
		br = new BufferedReader(new FileReader(new File(filename)));
		str = br.readLine();
		while(str != null){
			s = new Scanner(str);
			proteins.add(s.next());
			proteins.add(s.next());
			str = br.readLine();
		}
		br.close();
System.out.println("proteins:"+proteins.size());		
		Iterator<String> it = proteins.iterator();
		String key;
		while(it.hasNext()){
			key = it.next();
			if(map.get(key) != null){
				bw.write(key+"\t"+map.get(key));
				bw.newLine();
			}else{
				System.out.println("*****"+key);
			}
		}
		bw.flush();
		bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Main() {
		// TODO Auto-generated constructor stub
		String base = "G:/Program/ForPvalue/最新GO数据/";
//		readAndOutput("G:/Program/ForPvalue/最新GO数据/gene_association.sgd", "G:/Program/ForPvalue/最新GO数据/map.txt");
		readAndOutpu2(base+"map.txt", base+"PPI.txt", base+"ppi_map.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
//		String str = "YDR530C";
//		String[] s = str.split("\\|");
//		for(int i=0;i<s.length;i++){
//			System.out.println(s[i]);
//		}
	}

}
