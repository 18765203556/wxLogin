package com.boot.security.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.server.annotation.LogAnnotation;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.dao.TCertificateDao;
import com.boot.security.server.dao.TServiceHisDao;
import com.boot.security.server.dao.TWorkHisDao;
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
	private TServiceHisDao tServiceHisDao;
	
	@Autowired
	private CusSelfInfoDao cusSelfInfoDao;
	
	@Autowired
	private TWorkHisDao tWorkHisDao;
	

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
	public String login(@RequestBody String json) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("code", jo.getString("code"));
		log.info("前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.wxLogin(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/getVerifyCode")
	@ApiOperation(value = "获取短信验证码接口",notes="获取短信验证码")
	public String getVerifyCode(@RequestBody String json) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("phone", jo.getString("mobile"));
		log.info("获取短信验证码接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.getVerifyCode(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/bindMobile")
	@ApiOperation(value = "用户手机号绑定接口",notes="用户手机号绑定接口")
	public String bindMobile(@RequestBody String json) {
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("iv",jo.getString("iv"));
		paramMap.put("sessionKey",jo.getString("sessionKey"));
		paramMap.put("encryptedData",jo.getString("encryptedData"));
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		log.info("用户手机号绑定接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.bindMobile(paramMap);
	}

	@LogAnnotation
	@ApiOperation(value = "上传文件接口",notes="上传文件接口")
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String openid,@RequestParam String token) throws Exception{
		//JSONObject jo=JSONObject.parseObject(json);
		return wxService.uploadFile(file,openid,token);
	}
	
	@LogAnnotation
	@PostMapping("/auditCheck")
	@ApiOperation(value = "大V认证接口",notes="大V认证接口")
	public String auditCheck(@RequestBody String json) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("picPath", jo.getString("picPath"));
		log.info("大V认证接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.auditCheck(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/getDictByType")
	@ApiOperation(value = "根据类型获取字典列表",notes="根据类型获取字典列表")
	public String getDictByType(@RequestBody String json) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("type", jo.getString("type"));
		log.info("获取字典列表接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return wxService.getDictByType(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/addCert")
	@ApiOperation(value = "添加证书接口",notes="添加证书接口")
	public String addCert(@RequestBody String json) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("certPath", jo.getString("certPath"));
		paramMap.put("certName", jo.getString("certName"));
		log.info("保存证书接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return cusService.addCert(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/delCert")
	@ApiOperation(value = "删除证书接口",notes="删除证书接口")
	public String delCert(@RequestBody String json) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("id", jo.getString("id"));
		log.info("删除证书接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		int delete = tCertificateDao.delete(jo.getString("id"));
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
	public String addSkill(@RequestBody String json) throws Exception{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("skillCodes", jo.getString("skillCodes"));
		log.info("保存技能接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		return cusService.addSkill(paramMap);
	}
	
	@LogAnnotation
	@PostMapping("/detail")
	@ApiOperation(value = "我的简历/预览简历接口",notes="我的简历/预览简历接口")
	public String detail(@RequestBody String json) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		log.info("我的简历/预览简历接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		CusSelfInfo byOpenId = cusSelfInfoDao.getByOpenId(jo.getString("openid"));
		if(byOpenId!=null) {
			return resumeService.getDetail(byOpenId.getId(),jo.getString("openid"));
		}else {
			resultMap.put("code", "1");
			resultMap.put("msg", "查询客户基本信息失败！");
			resultMap.put("data", "");
		}
		return JSON.toJSONString(resultMap);
	}
	
	@LogAnnotation
	@PostMapping("/addService")
	@ApiOperation(value = "添加服役履历接口",notes="添加服役履历接口")
	public String addService(@RequestBody String json) throws Exception{
		log.info("添加服役履历接口>>>>>>>>>>>"+json);
		return cusService.addService(json);
	}
	
	@LogAnnotation
	@PostMapping("/delService")
	@ApiOperation(value = "删除服役履历接口",notes="删除服役履历接口")
	public String delService(@RequestBody String json) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("id", jo.getString("id"));
		log.info("删除服役履历接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		int delete = tServiceHisDao.delete(jo.getString("id"));
		if(1==delete) {
			// 证书删除成功
			resultMap.put("data", "");
			resultMap.put("code", "0");
			resultMap.put("msg", "删除成功");
			log.info("服役履历删除成功－－－－－－－－－end－－－－－－－");
		}else {
			// 证书删除失败
			resultMap.put("data", "");
			resultMap.put("code", "1");
			resultMap.put("msg", "删除失败");
			log.info("服役履历删除失败-----end－－－－－－－");
		}
		return JSON.toJSONString(resultMap);
	}
	
	@LogAnnotation
	@PostMapping("/addWork")
	@ApiOperation(value = "添加社会工作经历接口",notes="添加社会工作经历接口")
	public String addWork(@RequestBody String json) throws Exception{
		log.info("添加社会工作经历接口>>>>>>>>>>>"+json);
		return cusService.addWork(json);
	}
	
	@LogAnnotation
	@PostMapping("/delWork")
	@ApiOperation(value = "删除社会工作经历接口",notes="删除社会工作经历接口")
	public String delWork(@RequestBody String json) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject jo=JSONObject.parseObject(json);
		paramMap.put("openId", jo.getString("openid"));
		paramMap.put("token", jo.getString("token"));
		paramMap.put("id", jo.getString("id"));
		log.info("删除社会工作经历接口--前台请求报文>>>>>>>>>>>"+JSON.toJSONString(paramMap));
		// 微信服务鉴权
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		if("1".equals((String)jsonObject.get("code"))) {
			return auth;
		}
		int delete = tWorkHisDao.delete(jo.getString("id"));
		if(1==delete) {
			// 证书删除成功
			resultMap.put("data", "");
			resultMap.put("code", "0");
			resultMap.put("msg", "删除成功");
			log.info("删除社会工作经历成功－－－－－－－－－end－－－－－－－");
		}else {
			// 证书删除失败
			resultMap.put("data", "");
			resultMap.put("code", "1");
			resultMap.put("msg", "删除失败");
			log.info("删除社会工作经历失败-----end－－－－－－－");
		}
		return JSON.toJSONString(resultMap);
	}
	
}
