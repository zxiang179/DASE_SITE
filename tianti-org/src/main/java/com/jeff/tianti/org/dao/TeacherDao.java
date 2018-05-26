package com.jeff.tianti.org.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jeff.tianti.org.entity.Teacher;

@Repository
public interface TeacherDao extends JpaRepository<Teacher,Long>,JpaSpecificationExecutor<Teacher>{
	
	@Query(value="select * from org_teacher where id = ?1",nativeQuery=true)
	public Teacher findById(long id);
	
}
