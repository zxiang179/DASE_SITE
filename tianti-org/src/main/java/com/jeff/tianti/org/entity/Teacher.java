package com.jeff.tianti.org.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "org_teacher")
public class Teacher{
	
	private static final long serialVersionUID = 9063813830344067815L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	//姓名
	@Column(name = "name")
	private String name;
	
	//照片路径
	@Column(name = "pict_url")
	private String pict_url;
	
	//简介
	@Column(name="brief_info",length=5240)
	private String briefInfo;
	
	//详细信息
	@Column(name="spec_info",length=5240)
	private String specInfo;
	
	@Column(length=10240)
	private String papers;
	
	@Column(name="type")
	private int type;
	
	public Teacher(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPict_url() {
		return pict_url;
	}

	public void setPict_url(String pict_url) {
		this.pict_url = pict_url;
	}

	public String getBriefInfo() {
		return briefInfo;
	}

	public void setBriefInfo(String briefInfo) {
		this.briefInfo = briefInfo;
	}

	public String getSpecInfo() {
		return specInfo;
	}

	public void setSpecInfo(String specInfo) {
		this.specInfo = specInfo;
	}

	public String getPapers() {
		return papers;
	}

	public void setPapers(String papers) {
		this.papers = papers;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	
	
	
}
