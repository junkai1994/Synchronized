package test;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class FileMove {
	@Test
	public void fun1(){
		File file = new File("");
		File desFile = new File("");
		
		if(!desFile.exists())
			desFile.mkdir();
		
		copyDir(file,desFile);
	}

	private void copyDir(File origin, File des) {
		if(origin.isFile()){
			String fileName = origin.getName();
			File copyFile = new File(des,fileName);
			origin.renameTo(copyFile);
		}else{
			File dir = new File(des,origin.getName());
			dir.mkdirs();
			
			File[] list = origin.listFiles();
			for(File file :list){
				copyDir(file,dir);
			}
		}
	}
}
