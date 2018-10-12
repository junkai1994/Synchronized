package test;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 文件容器
 * 存放解析到的文件和文件夹，供线程写入和读取
 * @author HJK
 *
 */

public class FileContainer<E extends File> implements Container<E> {
	private volatile List<E> list = new LinkedList<>();
	private Lock lock = new ReentrantLock();
	private Condition productor = lock.newCondition();
	private Condition consumer = lock.newCondition();
	private volatile int count = 0;
	private final int MAX_SIZE = 15;
	
	public boolean add(E e) throws NotFileException{
		boolean flag = false;
		
		if(!(e instanceof File))
			throw new NotFileException("传入的对象不是文件或文件夹！");
		try{
			lock.lock();
			while(count == MAX_SIZE){
				try {
					productor.await();
					System.out.println("生产者：" + Thread.currentThread().getName() + "进入等待...");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			flag = list.add(e);
			count++;
			consumer.signalAll();
		}finally{
			lock.unlock();
		}
		return flag;
	}
	
	public E get(){
		LinkedList<E> linkedList = (LinkedList<E>) list;
		E file = null;
		
		try{
			lock.lock();
			while(count == 0){
				try {
					consumer.await();
					System.out.println("消费者：" + Thread.currentThread().getName() + "进入等待...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			file = linkedList.removeFirst();
			count--;
			System.out.println("消费者消费：" + file.getName());
			productor.signalAll();
		}finally{
			lock.unlock();
		}
		return file;
	}
	
	public int getSize(){
		return count;
	}

	@Override
	public List<E> getList() throws Exception {
		return list;
	}
	
}
