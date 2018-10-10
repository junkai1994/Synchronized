package cn.web.test;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Container02<T> {
	private volatile LinkedList<T> list = new LinkedList<T>();
	private Lock lock = new ReentrantLock();
	private Condition producter = lock.newCondition();
	private Condition consumer = lock.newCondition();
	
	private static final int MAX_SIZE = 10;
	private int count = 0;
	
	public void addElem(T t){
		try{
			lock.lock();
			while(count == MAX_SIZE){
				System.out.println("生产者" + Thread.currentThread().getName() + "进入等待");
				producter.await();
			}
			list.add(t);
			System.out.println("生产者" + Thread.currentThread().getName() + "生产" + t);
			count++;
			consumer.signalAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			try{
				lock.unlock();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public T getElem(){
		T t = null;
		try{
			lock.lock();
			while(count == 0){
				System.out.println("消费者" + Thread.currentThread().getName() + "进入等待");
				consumer.await();
			}
			t = (T) list.removeFirst();
			System.out.println(Thread.currentThread().getName() + "获取：" + t);
			count--;
			producter.signalAll();
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			try{
				lock.unlock();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return t;
	}
	
	public int getCount(){
		return count;
	}
}
