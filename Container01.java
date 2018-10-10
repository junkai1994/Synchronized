package cn.web.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Container01<E> {
	private static Container01 container = new Container01();//同步容器设置成单例 
	private volatile LinkedList<E> list = new LinkedList<E>();
//	private Lock lock = new ReentrantLock();
	private static final int MAX_SIZE = 10;
	private static int count;
	
	private Container01() {
		super();
	}
	
	public static Container01 getContainer(){
		return container;
	}
	
	public void addElem(E elem){
		synchronized (this) {
			while(count == MAX_SIZE){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.add(elem);
			System.out.println("生产者" + Thread.currentThread().getName() + "生产" + elem);
			count++;
			this.notifyAll();
		}
		
	}
	
	public E getElem(int index){
		synchronized (this) {
			E elem = null;
			while(count == 0){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			elem = list.removeFirst();
			count--;
			this.notifyAll();
			return elem;
		}
	}
	
	public List<E> getList(){
		return list;
	}
}
