package com.boot.security.server.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.boot.security.server.annotation.LogAnnotation;
import com.boot.security.server.model.CusSelfInfo;
import com.boot.security.server.service.CusService;
import com.boot.security.server.service.WxService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
   * 微信小程序相关接口
 * 
 * @author mdd
 *
 */
@Api(tags = "微信")

@RestController
@RequestMapping("/wx")
public class WxController {
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");
	
	@Autowired
	private WxService wxService;
	
	@Autowired
	private CusService cusService;
	

	@LogAnnotation
	@PostMapping("/saveCusSelfInfo")
	@ApiOperation(value = "用户基本信息保存接口")
	public String saveCusSelfInfo(@RequestBody CusSelfInfo cusSelfInfo) {
		// 微信用户基本信息保存接口
		log.info("微信用户基本信息保存接口>>>>>>>>>>>"+JSON.toJSONString(cusSelfInfo));
		return cusService.saveCus(cusSelfInfo);
	}
	
	@LogAnnotation
	@PutMapping("login/{code}")
	@ApiOperation(value = "微信用户登录",notes="前端通过临时登录凭证code获取session_key和openid等")
	public String login(@PathVariable String code) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("code", code);
		log.info("前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.wxLogin(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/getVerifyCode")
	@ApiOperation(value = "获取短信验证码接口",notes="获取短信验证码")
	public String getVerifyCode(String openid,String mobile) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("phone", mobile);
		log.info("获取短信验证码接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.getVerifyCode(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/bindMobile")
	@ApiOperation(value = "用户手机号绑定接口",notes="用户手机号绑定接口")
	public String bindMobile(String openid,String mobile,String token) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("mobile", mobile);
		log.info("用户手机号绑定接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.bindMobile(paramMap);
	}

	@LogAnnotation
	@PostMapping("/uploadFile")
	@ApiOperation(value = "上传文件接口",notes="上传文件接口")
	public String uploadFile(MultipartFile file,String openid,String token) throws Exception{
		return wxService.uploadFile(file,openid,token);
	}
	
	@LogAnnotation
	@PostMapping("/auditCheck")
	@ApiOperation(value = "大V认证接口",notes="大V认证接口")
	public String auditCheck(String picPath,String openid,String token) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("picPath", picPath);
		log.info("大V认证接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.auditCheck(paramMap);
	}
}
