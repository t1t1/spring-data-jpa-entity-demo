package com.example;

import java.util.ArrayList;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.model.Addr;
import com.example.model.Dept;
import com.example.model.Emp;
import com.example.model.Hobby;
import com.example.repository.AddrRepository;
import com.example.repository.DeptRepository;
import com.example.repository.EmpRepository;

@SpringBootApplication
public class ManyToOneApplication implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(ManyToOneApplication.class);
	
	@Autowired
	private DeptRepository deptRepository;
	
	@Autowired
	private AddrRepository addrRepository;
	
	@Autowired
	private EmpRepository empRepository;
	
	@Override
	@Transactional
	public void run(String... arg0) throws Exception {
		testLog();
		testInsert();
		testFindAll();
//		testDelete();
//		testFindAll();
		
		testOneToOne();
		
		testManyToMany();
	}

	private void testManyToMany() {
		
		Emp e1 = empRepository.findOne(1L);
		e1.setEname("hasHobby");
//		e1.getHobbies().add(new Hobby("hobby1"));
//		e1.getHobbies().add(new Hobby("hobby2"));
		e1.setHobbies(new ArrayList<Hobby>() {
			{
				add(new Hobby("hobby1"));
				add(new Hobby("hobby2"));
			}
		});
		empRepository.save(new ArrayList<Emp>() {
			{
				add(e1);
			}
		});
		empRepository.flush();
	}

	private void testOneToOne() {
		Dept d1 = deptRepository.findOne(1L);
//		HashSet<Emp> emps = (HashSet<Emp>) d1.getEmps();
//		for (Emp emp : emps) {
		for (Emp emp : d1.getEmps()) {
			Addr addr = new Addr("juso");
			addr.setEmp(emp);
			addrRepository.save(addr);
		}
	}

	private void testDelete() {
		deptRepository.delete(1L);
	}

	private void testFindAll() {
		for (Dept dept : deptRepository.findAll()) {
			logger.info(dept.toString());
		}
	}

	private void testInsert() {
		Dept d1 = new Dept("develop");
		Emp e1 = new Emp("kim");
		Emp e2 = new Emp("cha");
		
		// 순서 보장 x
		d1.setEmps(new HashSet<Emp>(){
			{
				add(e1);
				add(e2);
			}
		});
		
		// 순서 보장 x
		Dept d2 = new Dept("manage", new HashSet<Emp>(){
			{
				add(new Emp("lee"));
				add(new Emp("park"));
			}
		});
		
		// 
		deptRepository.save(new ArrayList<Dept>(){
			{
				add(d1);
				add(d2);
			}
		});
	}

	private void testLog() {
		logger.warn("ManyToOneApplication: 1");
		System.out.println("ManyToOneApplication: 1");
	}

}
