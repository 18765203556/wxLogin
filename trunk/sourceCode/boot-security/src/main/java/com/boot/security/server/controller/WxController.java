package com.boot.security.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.server.annotation.LogAnnotation;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.dao.TCertificateDao;
import com.boot.security.server.model.CusSelfInfo;
import com.boot.security.server.service.CusService;
import com.boot.security.server.service.ResumeService;
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
	
	@Autowired
	private ResumeService resumeService;
	
	@Autowired
	private TCertificateDao tCertificateDao;
	
	@Autowired
	private CusSelfInfoDao cusSelfInfoDao;
	

	@LogAnnotation
	@PostMapping("/saveCusSelfInfo")
	@ApiOperation(value = "用户基本信息保存(修改)接口")
	public String saveCusSelfInfo(@RequestBody String json) {
		// 微信用户基本信息保存接口
		log.info("微信用户基本信息保存接口>>>>>>>>>>>"+json);
		return cusService.saveCus(json);
	}
	
	@LogAnnotation
	@PostMapping("/login")
	@ApiOperation(value = "微信用户登录",notes="前端通过临时登录凭证code获取session_key和openid等")
	public String login(String code) {
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
	
	@LogAnnotation
	@PostMapping("/getDictByType")
	@ApiOperation(value = "根据类型获取字典列表",notes="根据类型获取字典列表")
	public String getDictByType(String type,String openid,String token) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("type", type);
		log.info("获取字典列表接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.getDictByType(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/addCert")
	@ApiOperation(value = "添加证书接口",notes="添加证书接口")
	public String addCert(String certName,String certPath,String openid,String token) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("certPath", certPath);
		paramMap.put("certName", certName);
		log.info("保存证书接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return cusService.addCert(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/delCert")
	@ApiOperation(value = "删除证书接口",notes="删除证书接口")
	public String delCert(String id,String openid,String token) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("id", id);
		log.info("删除证书接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		int delete = tCertificateDao.delete(id);
		if(1==delete) {
			// 证书删除成功
			resultMap.put("data", "");
			resultMap.put("code", "0");
			resultMap.put("msg", "删除成功");
			log.info("证书删除成功－－－－－－－－－end－－－－－－－");
		}else {
			// 证书删除失败
			resultMap.put("data", "");
			resultMap.put("code", "1");
			resultMap.put("msg", "删除失败");
			log.info("证书删除失败-----end－－－－－－－");
		}
		return JSON.toJSONString(resultMap);
	}
	
	@LogAnnotation
	@PostMapping("/addSkill")
	@ApiOperation(value = "添加/修改技能接口",notes="添加/修改技能接口")
	public String addSkill(String skillCodes,String openid,String token) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		paramMap.put("skillCodes", skillCodes);
		log.info("保存技能接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return cusService.addSkill(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/detail")
	@ApiOperation(value = "我的简历/预览简历接口",notes="我的简历/预览简历接口")
	public String detail(String openid,String token) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("openId", openid);
		paramMap.put("token", token);
		log.info("我的简历/预览简历接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		CusSelfInfo byOpenId = cusSelfInfoDao.getByOpenId(openid);
		if(byOpenId!=null) {
			return resumeService.getDetail(byOpenId.getId());
		}else {
			resultMap.put("code", "1");
			resultMap.put("msg", "查询客户基本信息失败！");
			resultMap.put("data", "");
		}
		return JSON.toJSONString(resultMap);
	}
	
}
