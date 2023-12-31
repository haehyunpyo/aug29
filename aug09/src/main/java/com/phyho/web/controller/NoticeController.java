package com.phyho.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phyho.web.service.NoticeService;
import com.phyho.web.util.Util;



@Controller
public class NoticeController {
	// 필요한거? model + map + service + DAO + Mapper
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private Util util;
	
	@GetMapping("/notice")
	public String notice(Model model) {
		List<Map<String, Object>> list = noticeService.list();
		model.addAttribute("list", list);
		System.out.println(list);
		return "notice";
	}
	
	//2023-08-22
	//noticeDetail
	@GetMapping("/noticeDetail")
	public String noticeDetail(@RequestParam("nno") int nno, Model model) {
		System.out.println(nno);
		Map<String, Object> detail = noticeService.detail(nno);
		String nwriter = noticeService.nWriter(nno);
		model.addAttribute("detail", detail);
		model.addAttribute("nwriter", nwriter);
		return "noticeDetail";
	}
	
	// 다운로드 처리하기 /download@파일명
	// 20230822154251b33ae68f-af1d-47f3-aa8e-df990015d1ac메모장.txt
	@ResponseBody
	@GetMapping("/download@{fileName}")
	public void download(@PathVariable("fileName") String filename, HttpServletResponse response) {
		System.out.println(filename);
		String path = util.uploadPath();
		
		//System.out.println(path);
		String oriFileName = noticeService.getOriFileName(filename);
		File serverSideFile = new File(path, filename);
		try {
			byte[] fileByte = FileCopyUtils.copyToByteArray(serverSideFile);
			response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(oriFileName, "UTF-8")+"\";");
	        response.setHeader("Content-Transfer-Encoding", "binary");
	        response.getOutputStream().write(fileByte);
	        response.getOutputStream().flush();
	        response.getOutputStream().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	
	
}

