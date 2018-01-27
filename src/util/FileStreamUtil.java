package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileStreamUtil {
	
	/**
	 * 获取文件 缓冲型字符输入流
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getBufferedReader(String filename) throws FileNotFoundException{
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(new File(filename)));
		return br;
	}
	
	/**
	 * 获取文件缓冲型字符输出流
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static BufferedWriter getBufferedWriter(String filename) throws IOException{
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(new File(filename)));
		return bw;
	}
	
	/**
	 * 获取随机文件输入流
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static RandomAccessFile getRAF(String filename)throws IOException{
		RandomAccessFile rf = null;
		rf = new RandomAccessFile(filename, "r");
		return rf;
	}

}
