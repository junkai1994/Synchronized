package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class IOTest {
	@Test
	public void fun1() throws IOException{
		File file = new File("D:/Program Files");
		File[] list = file.listFiles();
		for(File f:list){
			String parentName = f.getParent();
			String subName = parentName.substring(parentName.lastIndexOf("\\"));
			System.out.println("父文件夹：" + subName +" "+ "文件名："+ f.getName() + "：" + f.getAbsolutePath());
		}
	}
	
	@Test
	public void fun2() throws IOException{
		FileInputStream fis = new FileInputStream("E:\\新建文本文档.txt");
		FileOutputStream fos = new FileOutputStream("E:\\copy.txt");
		byte[] data = new byte[1024 * 10];
		int length = 0;
		while((length=fis.read(data)) != -1){
			fos.write(data,0,length);
			fos.flush();
		}
		fos.close();
		fis.close();
		
	}
}
