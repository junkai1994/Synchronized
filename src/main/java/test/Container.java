package test;

import java.util.List;

public interface Container<T> {
	public boolean add(T t) throws Exception;//向容器添加元素
	public T get() throws Exception;//获取容器元素
	public int getSize() throws Exception;//获取容器大小
	public List<T> getList() throws Exception;//获取容器的链表
}
