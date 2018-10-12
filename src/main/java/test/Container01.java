package test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Container01<T> {
//	private  LinkedList<T> list = new LinkedList<T>();
	private Lock lock = new ReentrantLock();
//	private LinkedBlockingQueue<T> list = new LinkedBlockingQueue<T>();
	private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();
	private static final int MAX_SIZE = 10;
	
	public void add(T t){
		queue.offer(t);
	}
	
	public T get(){
		return queue.poll();
	}
	
	public int getSize(){
		return queue.size();
	}
}
