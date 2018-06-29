package com.jeff.tianti.org.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeff.tianti.org.dao.TeacherDao;
import com.jeff.tianti.org.entity.Teacher;

@Service
public class TeacherService {
	@Autowired
	private TeacherDao teacherDao;

	private final static String PICTURES = "pictures";

	private final static String ATTACH = "attach";

	private Gson gson = new GsonBuilder().create();

	private Gson gsonToHtml = new GsonBuilder().disableHtmlEscaping().create();

	// 为了防止教师的ID被用户直接使用，将每个ID映射成UUID返回到前端，使用keyMap来记录映射关系
	private Map<String, Long> keyMap;

	private Sort sortName = new Sort("name");

	/**
	 * 初始化keyMap
	 */
	@Autowired
	public Map<String, Object> initKeyMap() {
		Map<String, Object> res = new HashMap<>();
		List<Teacher> teachers = teacherDao.findAll(sortName);
		keyMap = new HashMap<>();
		String key;
		for (Teacher t : teachers) {
			key = UUID.randomUUID().toString().replaceAll("-", "");
			keyMap.put(key, t.getId());
			res.put(t.getName(), key);
		}
		return res;
	}

	public Map<String, Object> getMapping() {
		Map<String, Object> res = new HashMap<>();
		Teacher t;
		for (String key : keyMap.keySet()) {
			t = teacherDao.findById(keyMap.get(key));
			res.put(t.getName(), key);
		}
		return res;
	}

	/**
	 * 只有当id为xz时，才表示新增
	 * 
	 * @param teacherJson
	 *            前端传来的老师，为json格式
	 **/
	public void save(String teacherJson, HttpSession session) {
		// TODO: 根据是否有id来判断是添加还是修改
		Map<String, Object> teacherObj = (Map<String, Object>) gson.fromJson(teacherJson, Map.class).get("teacherJson");
		Teacher teacher = new Teacher();
		if (!teacherObj.get("id").toString().equals("xz") && keyMap.get(teacherObj.get("id").toString()) == null)
			return;
		else if (keyMap.get(teacherObj.get("id").toString()) != null) {
			long id = keyMap.get(teacherObj.get("id").toString());
			teacher.setId(id);
			teacher.setPict_url(teacherDao.findById(id).getPict_url());
		}

		if (session.getAttribute("pictureURL") != null) {
			teacher.setPict_url(session.getAttribute("pictureURL").toString());
			session.removeAttribute("pictureURL");
		}
		if (teacherObj.get("briefInfo") != null) {
			String briefInfo = gson.toJson(teacherObj.get("briefInfo"));
			teacher.setBriefInfo(briefInfo);
		}
		if (teacherObj.get("specInfo") != null) {
			String specInfo = gsonToHtml.toJson(teacherObj.get("specInfo").toString());
			teacher.setSpecInfo(specInfo);
		}
		if (teacherObj.get("papers") != null) {
			String papers = gsonToHtml.toJson(teacherObj.get("papers").toString());
			teacher.setPapers(papers);
		}
		if (teacherObj.get("name") != null) {
			teacher.setName(teacherObj.get("name").toString());
		}
		teacher.setType(1);
		teacher = teacherDao.save(teacher);
	}

	/**
	 * @param current
	 *            当前页
	 * @param size
	 *            每页多少条记录
	 **/
	public Page<Teacher> searchTeacherList(int current, int size, final int type) {
		Pageable pageable = new PageRequest(current, size, sortName);
		Page<Teacher> pages = teacherDao.findAll(new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate p = criteriaBuilder.equal(root.get("type").as(Integer.class), type);
				query.where(criteriaBuilder.and(p));
				return query.getRestriction();
			}
		}, pageable);
		return pages;
	}

	/**
	 * @param id
	 *            要查询导师的id
	 * @param size
	 *            每页多少条记录
	 **/
	public Teacher searchTeacher(long id) {
		Teacher teacher = teacherDao.findOne(id);
		return teacher;
	}

	/**
	 * 根据uuid查找老师
	 * 
	 * @param uuid
	 * @return
	 */
	public Teacher searchTeacher(String uuid) {
		if (keyMap.get(uuid) != null)
			return searchTeacher(keyMap.get(uuid));
		return null;
	}

	/**
	 * @param data
	 *            图片流
	 **/
	public void savePicture(InputStream picture, HttpSession session) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String realPath = session.getServletContext().getRealPath("uploads");
		OutputStream os = null;
		String filePath = realPath + File.separator + PICTURES + File.separator + uuid + ".jpg";
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			os = new FileOutputStream(file);
			byte[] bs = new byte[1024 * 2];
			int len;
			while ((len = picture.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			session.setAttribute("pictureURL", File.separator + uuid + ".jpg");
			os.close();
			picture.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getPicture(String pictureName, HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("uploads");
		String filePath = realPath + File.separator + PICTURES + File.separator + pictureName;
		File file = new File(filePath);
		FileInputStream fis = null;
		byte[] data = null;
		try {
			fis = new FileInputStream(file);
			long size = file.length();
			byte[] temp = new byte[(int) size];
			fis.read(temp, 0, (int) size);
			fis.close();
			data = temp;
			// 将IO流通过Base64
			data = Base64.encodeBase64(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public Map<String, Object> saveAttachFile(MultipartFile _file, HttpSession session) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String realPath = session.getServletContext().getRealPath("uploads");
		OutputStream os = null;
		String originalFileName = _file.getOriginalFilename();
		String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
		String filePath = realPath + File.separator + ATTACH + File.separator + fileName + uuid.substring(0, 5)
				+ suffix;
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		InputStream fileStream = null;
		try {
			fileStream = _file.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			os = new FileOutputStream(file);
			byte[] bs = new byte[1024 * 2];
			int len;
			while ((len = fileStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> res = new HashMap<>();
		res.put("data", "uploads/attach/" + fileName + uuid.substring(0, 5) + suffix);
		return res;
	}
}
