package com.jeff.tianti.org.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jeff.tianti.org.entity.Teacher;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String>{
}
