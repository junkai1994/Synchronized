package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {
	private static ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(1);
	private static Object o = new Object();
//	private static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		final Container<File> container = (Container<File>) new FileContainer<File>();
		long start = System.currentTimeMillis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					productor(container, new File("E:/Hearthstone"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					consumer(container, new File("E:/CopyFiles"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * 
	 * @param container 指定抽取元素的容器
	 * @param dir 指定目标文件夹
	 * @throws Exception
	 */
	private static void consumer(Container<File> container, File dir) throws Exception {
		while (container.getSize() != 0) {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				File file = container.get();
				String parentName = file.getParent();
				String subName = parentName.substring(parentName.indexOf("\\"));// 截取父文件名，拼接成新的路径
				File desFileParent = new File(dir.getAbsolutePath() + subName);
				if (!desFileParent.exists())
					desFileParent.mkdirs();
				// 创建文件
				File desFile = new File(desFileParent, file.getName());
				if (desFile.exists()) {
					continue;
				}
				// 创建流对象，读取源文件
				fis = new FileInputStream(file);
				fos = new FileOutputStream(desFile);
				byte[] data = new byte[1024 * 10];
				int length = 0;
				while ((length = fis.read(data)) != -1) {
					fos.write(data, 0, length);
					fos.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			}
		}
	}

	/**
	 * 
	 * @param container
	 *            指定元素要添加到哪个容器中
	 * @param file
	 *            要添加的元素
	 * @throws Exception
	 */
	private static void productor(Container<File> container, File dir) throws Exception {
		if (dir.isDirectory()) {
			File[] dirs = dir.listFiles();
			for (File file : dirs) {
				productor(container, file);
			}
		} else {
			container.add(dir);
		}
	}

}
