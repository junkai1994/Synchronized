package cn.web.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.web.domain.Department;
import cn.web.domain.Employee;
import cn.web.mapper.DepartmentMapper;
import cn.web.mapper.EmployeeMapper;

public class MybatisTest {
	private SqlSessionFactory sqlSessionFactory;
	
	public static void main(String[] args) {
		final Container01 container = Container01.getContainer();
		final Container02 c2 = new Container02();
		
		for(int i=0;i<5;i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					for(int j=0;j<4;j++){
						c2.getElem();
					}
				}
			}).start();;
		}
		
		for(int i=0;i<2;i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					for(int i=0;i<11;i++){
						c2.addElem(Thread.currentThread().getName() + " " + i);
						System.out.println("当前容量：" + c2.getCount());
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();;
		}
	}
	@Before
	public void bef() throws IOException
	{
		String resource = "mybatis/sqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}
	
	@Test
	public void fun1() throws Exception
	{
		SqlSession session = sqlSessionFactory.openSession();
		try{
			DepartmentMapper deptMapper = session.getMapper(DepartmentMapper.class);
			Department dept = deptMapper.findDeptById(20);
			System.out.println(dept);
		}finally{
			session.close();
		}
	}
	@Test
	public void fun2() throws Exception
	{
		SqlSession session = sqlSessionFactory.openSession();
		try{
			EmployeeMapper empMapper = session.getMapper(EmployeeMapper.class);
			Employee emp = empMapper.findEmpById(1002);
			Department dept = emp.getDept();
			System.out.println(emp);
			System.out.println(dept);
		}finally{
			session.close();
		}
	}
	
	@Test
	public void fun3() throws Exception
	{
		SqlSession session = sqlSessionFactory.openSession();
		EmployeeMapper empMapper = session.getMapper(EmployeeMapper.class);
		try {
			Employee emp = new Employee();
			emp.setEname("天");
			List<Employee> list = empMapper.findEmpToMap(emp);
			System.out.println(list);
		} finally{
			session.close();
		}
	}
	

	@Test
	public void fun4() throws Exception
	{
		SqlSession session = sqlSessionFactory.openSession();
		EmployeeMapper empMapper = session.getMapper(EmployeeMapper.class);
		try{
			PageHelper.startPage(3, 3);
			List<Employee> empList = empMapper.findAllEmp();
			PageInfo<Employee> info = new PageInfo<Employee>(empList, 3);
			
			int[] nums = info.getNavigatepageNums();
			System.out.println(Arrays.toString(nums));
			System.out.println(info.getNavigatePages());
			System.out.println(info.getNextPage());
			System.out.println(info.getPageNum());
			System.out.println(info.getPageSize());
			System.out.println(info.getPrePage());
			
			for(Employee emp : empList)
			{
				System.out.println(emp);
			}
		} finally{
			session.close();
		}
	}
	@Test
	public void fun5(){
		CountDownLatch countDownLatch = new CountDownLatch(3);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
