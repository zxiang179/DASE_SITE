package com.jeff.tianti.controller;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeff.tianti.common.dto.AjaxResult;

/**
 * 文件异步上传Controller
 * 
 * @author JeffXu
 * @since 2016-03-14
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

	public final static String ATTACH_SAVE_PATH = "attach";

	public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping("/uploadAttach")
	public void uploadAttach(HttpServletRequest request, PrintWriter out) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile multipartFile = null;
		String fileName = null;
		for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
			multipartFile = set.getValue();// 文件名
		}
		fileName = this.storeIOc(multipartRequest, multipartFile);

		out.print(fileName);
	}

	@RequestMapping("/ajax/upload_file")
	@ResponseBody
	public AjaxResult ajaxUploadFile(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(false);
		try {

			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			MultipartFile multipartFile = null;
			String fileName = null;
			for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
				multipartFile = set.getValue();// 文件名
			}
			fileName = this.storeIOc(multipartRequest, multipartFile);

			ajaxResult.setData(fileName);
			ajaxResult.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ajaxResult;
	}

	// 接受图片，返回文件地址
	private String storeIOc(HttpServletRequest request, MultipartFile file) {
		String result = "";
		String realPath = request.getSession().getServletContext().getRealPath("uploads");
		if (file == null) {
			return null;
		}
		String fileName = "";
		String logFileName = "";
		if (file.isEmpty()) {
			result = "文件未上传";
		} else {
			String _fileName = file.getOriginalFilename();
			String suffix = _fileName.substring(_fileName.lastIndexOf("."));
			if (StringUtils.isNotBlank(suffix)) {
				if (suffix.equalsIgnoreCase(".xls") || suffix.equalsIgnoreCase(".xlsx")
						|| suffix.equalsIgnoreCase(".txt") || suffix.equalsIgnoreCase(".png")
						|| suffix.equalsIgnoreCase(".doc") || suffix.equalsIgnoreCase(".docx")
						|| suffix.equalsIgnoreCase(".pdf") || suffix.equalsIgnoreCase(".ppt")
						|| suffix.equalsIgnoreCase(".pptx") || suffix.equalsIgnoreCase(".gif")
						|| suffix.equalsIgnoreCase(".rar") || suffix.equalsIgnoreCase(".zip")
						|| suffix.equalsIgnoreCase(".jpg") || suffix.equalsIgnoreCase(".jpeg")
						|| suffix.equalsIgnoreCase(".bmp")) {
					// 生成原文件名+uuid0-5位+文件类型
					logFileName = _fileName.substring(0, _fileName.lastIndexOf("."))
							+ UUID.randomUUID().toString().substring(0, 5) + suffix;
					String date = sdf.format(new Date());
					fileName = realPath + File.separator + ATTACH_SAVE_PATH + File.separator + date + File.separator
							+ logFileName;
					File restore = new File(fileName);
					try {
						file.transferTo(restore);
						result = "/uploads/attach/date/" + logFileName;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					result = "文件格式不对，只能上传ppt、ptx、doc、docx、xls、xlsx、pdf、png、jpg、jpeg、gif、bmp、rar、zip格式";
				}
			}
		}
		return result;
	}

}