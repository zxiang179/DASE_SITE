package com.jeff.tianti.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeff.tianti.org.entity.Teacher;
import com.jeff.tianti.org.service.TeacherService;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void save(HttpServletRequest request, @RequestBody String teacherJson) {
		teacherService.save(teacherJson, request.getSession());
	}

	@RequestMapping(value = "/teacher-list", method = RequestMethod.GET)
	@ResponseBody
	public Page<Teacher> searchTeacherList(@RequestParam int current, @RequestParam int size, @RequestParam int type) {
		return teacherService.searchTeacherList(current, size,type);
	}

	@RequestMapping(value = "/teacher/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Teacher searchTeacher(@PathVariable Long id) {
		return teacherService.searchTeacher(id);
	}

	@RequestMapping(value = "/upload-picture", method = RequestMethod.POST)
	@ResponseBody
	public void savePicture(MultipartHttpServletRequest request) {
		try {
			teacherService.savePicture(request.getFile("picture").getInputStream(), request.getSession());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/picture", method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
	public byte[] getPicture(HttpServletRequest request, @RequestParam String picturename) {
		return teacherService.getPicture(picturename, request);
	}

}
