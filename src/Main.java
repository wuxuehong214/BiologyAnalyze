import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Main {

	public Main() throws IOException {
		// TODO Auto-generated constructor stubC:\Users\Administrator\Desktop\IPCA-Windows\IPCA-Windows\Debug
		BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/Administrator/Desktop/IPCA-Windows/IPCA-Windows/Debug/MIPS.txt")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("C:/Users/Administrator/Desktop/IPCA-Windows/IPCA-Windows/Debug/MIPS3.txt")));
		String str;
		str = br.readLine();
		Random r = new Random();
		while(str != null){
			bw.write(str+"\t"+1);
			bw.newLine();
			str = br.readLine();
		}
		bw.flush();
		bw.close();
		br.close();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Main();
	}

}
