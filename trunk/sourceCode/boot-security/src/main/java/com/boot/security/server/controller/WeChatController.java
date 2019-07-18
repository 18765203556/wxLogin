package com.boot.security.server.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.model.CusSelfInfo;
import com.boot.security.server.model.SysDictionaries;
import com.boot.security.server.model.TBanner;
import com.boot.security.server.model.TClassification;
import com.boot.security.server.model.TComment;
import com.boot.security.server.model.TCompany;
import com.boot.security.server.model.TDynamic;
import com.boot.security.server.model.TEmploy;
import com.boot.security.server.model.TEmployCollect;
import com.boot.security.server.model.TEmployComment;
import com.boot.security.server.model.TEmployDeliver;
import com.boot.security.server.model.TNews;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;
import com.boot.security.server.service.CusService;
import com.boot.security.server.service.WeChatService;
import com.boot.security.server.service.WxService;
import com.boot.security.server.utils.BaseController;
import com.boot.security.server.utils.Const;

import io.swagger.annotations.ApiOperation;
/**
 * 微信部分接口类
 *  Class Name: WeChatController.java
 *  Description: 
 *  @author xiaoding  DateTime 2019年7月9日 下午10:01:18 
 *  @company xiaoding 
 *  @version 1.0
 *  
 
 */
@RestController
@RequestMapping("/weChat")
public class WeChatController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");
	
	@Autowired
	private WeChatService weChatService;
	@Autowired
	private CusService cusService;
	@Autowired
	 private CusSelfInfoDao cusSelfInfoDao;
	 
	 @Autowired
	 private WxService wxService;
	/**
	 * 动态展示
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	/*@PostMapping("/listDynamic")
    @ApiOperation(value = "展示所有动态列表")
    public String listDynamic(PageTableRequest request,Integer pageSize,Integer page) {
		List<TDynamic> dataList=null;
		PageTableResponse response;
		try {
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			response = weChatService.listDynamic(request);
			String news=weChatService.listNewsByOne(request);
			dataList=(List<TDynamic>)response.getData();
			int totalCount=0;
			if(news!=null){
				totalCount=1;
			}
			if(dataList!=null){
				totalCount=totalCount+dataList.size();
			}
			resultMap.put("news", news);
			resultMap.put("totalCount", totalCount);
			resultMap.put("dynamic", dataList);
			log.info(JSON.toJSONString(resultMap));
			return success(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
			 return fail("系统异常请联系管理员！");
		}
    }*/
	/**
	 * 	动态详情
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月9日 下午10:03:19
	 *  @param request
	 *  @param id
	 *  @return
	 
	 */
	
	@GetMapping("/dynamicDetail")
    @ApiOperation(value = "动态详情")
    public String dynamicDetail(String id,String openid,String token,HttpServletRequest request) {
		TDynamic data;
		try {
			
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			data = weChatService.dynamicDetail(id,userId);
			log.info(JSON.toJSONString(data));
			 return success(data);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
			 return fail("系统异常请联系管理员！");
		}
       
    }
	/**
	 * 	新闻详情
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月17日 上午10:43:09
	 *  @param id
	 *  @param openid
	 *  @param token
	 *  @param request
	 *  @return
	 
	 */
	@GetMapping("/getNewsById")
    @ApiOperation(value = "评论保存方法")
	public String getNewsById(String id,String openid,String token,HttpServletRequest request){
		try {
			
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			 TNews result=weChatService.getNewsById(id, userId);
			 //处理富文本框
//			 String serverName=request.getServerName();
//			 int port=request.getServerPort();
			 String newsContent=result.getNewsContent();
			 newsContent=newsContent.replaceAll("/statics",getUrl(request)+"/statics");
			 result.setNewsContent(newsContent);
			 return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
	}
	/**
	 * 	动态保存
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月9日 下午10:03:19
	 *  @param request
	 *  @param id
	 *  @return
	 
	 */
	
	@PostMapping("/saveDynamic")
    @ApiOperation(value = "动态保存")
    public String saveDynamic(@RequestBody String json,HttpServletRequest request) {
		try {
			JSONObject data=JSONObject.parseObject(json);
			log.info("保存动态获取的json-----------------"+json);
			//获取用户信息
			String token=getToken(json);
			String openId=data.getString("openid");
			if(token==null||token==""||openId==null||openId==""){
				return login("需要登录");
			}
			JSONObject jsonObject=getUserId(openId, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			TDynamic entity=new TDynamic();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			entity.setContent(data.getString("content"));
			entity.setPictureIds(data.getString("pictureIds"));
			entity.setPicturePath(data.getString("picturePath"));
			entity.setStatus(data.getString("status"));
			//动态与新闻的区分
			entity.setDynamicType(data.getString("dynamicType"));
			entity.setCreateUser(userId);
			entity.setCreateTime(new Date());
			 weChatService.saveDynamic(entity);
			 return success("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	/**
	 * 	评论保存方法
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 上午9:07:23
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@PostMapping("/saveComment")
    @ApiOperation(value = "评论保存方法")
    public String saveComment(@RequestBody String json,HttpServletRequest request) {
		try {
			JSONObject data=JSONObject.parseObject(json);
			//获取用户信息
			String token=getToken(json);
			String openId=data.getString("openid");
			if(token==null||token==""||openId==null||openId==""){
				return login("需要登录");
			}
			JSONObject jsonObject=getUserId(openId, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			TComment entity=new TComment();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			//动态id
			entity.setDynamicId(data.getString("dynamicId"));
			//评论内容
			entity.setCommentContent(data.getString("commentContent"));
			//是否为评论0为评论1位点赞
			entity.setGiveUp(data.getString("giveUp"));
			//获取到的用户id
			//String userId=(String)request.getAttribute("userId");
			//评论用户
			entity.setCommentUser(userId);
			//创建人
			entity.setCreateUser(userId);
			//创建时间
			entity.setCreateTime(new Date());
			//状态字段
			entity.setStatus(data.getString("status"));
			String result= weChatService.saveComment(entity);
			 return success(entity);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	
	
	
	/**
	 * 	招聘类型分类接口
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午3:41:34
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@GetMapping("/listClassification")
    @ApiOperation(value = "招聘类型分类接口")
    public String listClassification(String openid,String token,HttpServletRequest request) {
		try {
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			List<TClassification> result=weChatService.listClassification();
			 return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	
	
	/**
	 * 区域列表
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listCity")
    @ApiOperation(value = " 区域列表")
    public String listCity(String id,String openid,String token,HttpServletRequest request) {
		try {
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			List<SysDictionaries> resultList=weChatService.listCity(Const.CHILDCITY);
			if(resultList!=null){
				return success(resultList);
			}else{
				return fail("城市加载失败！请联系管理员！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	/**
	 *  公司类型
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listCompanyType")
    @ApiOperation(value = "公司类型")
    public String listCompanyType(String openid,String token,HttpServletRequest request) {
		try {
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			List<SysDictionaries> resultList=weChatService.listCompanyType(Const.COMPANYTYPE);
			if(resultList!=null){
				return success(resultList);
			}else{
				return fail("城市加载失败！请联系管理员！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	
	
	/**
	 * 工作筛选
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listEmploy")
    @ApiOperation(value = "工作筛选")
    public String listEmploy(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TEmploy> dataList=null;
		PageTableResponse response;
		try {
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
//			String userId=(String)req.getAttribute("userId");
			Map<String, Object> params=request.getParams();
			if(params!=null){
				params.put("userId", userId);
			}
			//根据城市名称去字典表中查询城市id
			String cityName=(String)params.get("cityName");
			String cityId=weChatService.getCityIdByCityName(cityName);
			if(cityId!=null){
				params.put("companyCity",cityId);
			}else{
				return fail("根据城市名称查询城市id失败！请联系管理员 ");
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listEmploy(request);
				dataList=(List<TEmploy>)response.getData();
				
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				//获取当前服务的域名端口号
//				String serverName=req.getServerName();
//				int port=req.getServerPort();
				//处理富文本中的图片
				for (int i = 0; i < dataList.size(); i++) {
					TEmploy entity=dataList.get(i);
					String employIntroduc=entity.getEmployIntroduc();
					employIntroduc=employIntroduc.replaceAll("/statics", getUrl(req)+"/statics");
					entity.setEmployIntroduc(employIntroduc);
				}
				
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	/**
	 *  公司详情
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/companyDetail")
    @ApiOperation(value = "公司详情")
    public String companyDetail(String id,String openid,String token,HttpServletRequest request) {
		try {
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			TCompany resultList=weChatService.companyDetail(id);
			//出来富文本中图片的路径
			String companyPropaganda=resultList.getCompanyPropaganda();
			String militaryFeelings=resultList.getMilitaryFeelings();
//			String serverName=request.getServerName();
//			int port=request.getServerPort();
			companyPropaganda=companyPropaganda.replaceAll("/statics/",getUrl(request)+"/statics/");
			militaryFeelings=militaryFeelings.replaceAll("/statics/", getUrl(request)+"/statics/");
			resultList.setCompanyPropaganda(companyPropaganda);
			resultList.setMilitaryFeelings(militaryFeelings);
			if(resultList!=null){
				return success(resultList);
			}else{
				return success("查询不到公司信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
	/**
	 * 	收藏工作
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 上午9:07:23
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@PostMapping("/saveCollect")
    @ApiOperation(value = "收藏工作")
    public String saveCollect(@RequestBody String json,HttpServletRequest request) {
		try {
			JSONObject data=JSONObject.parseObject(json);
			TEmployCollect entity=new TEmployCollect();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			//获取到的用户id
			
			//获取用户信息
			String token=getToken(json);
			String openId=data.getString("openid");
			if(token==null||token==""||openId==null||openId==""){
				return login("需要登录");
			}
			JSONObject jsonObject=getUserId(openId, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			
//			String userId=(String)request.getAttribute("userId");
			String employId=data.getString("employId");
			entity.setEmployId(employId);
			//评论用户
			entity.setCusSelfId(userId);
			//创建人
			entity.setCreateUser(userId);
			//创建时间
			entity.setCreateTime(new Date());
			//状态字段
			String result= weChatService.saveCollect(entity);
			return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
    }
	/**
	 *  查看我的收藏
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listCollect")
    @ApiOperation(value = "查看我的收藏")
    public String listCollect(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TEmploy> dataList=null;
		PageTableResponse response;
		try {
//			String userId=(String)req.getAttribute("userId");
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			Map<String, Object> params=request.getParams();
			if(params!=null){
				params.put("cusSelfId", userId);
//				params.put("tableName", "t_employ_collect");
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listCollect(request);
				dataList=(List<TEmploy>)response.getData();
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	
	/**
	 * 	投递工作
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 上午9:07:23
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@PostMapping("/saveDeliver")
    @ApiOperation(value = "投递工作")
    public String saveDeliver(@RequestBody String json,HttpServletRequest request) {
		try {
			JSONObject data=JSONObject.parseObject(json);
			TEmployDeliver entity=new TEmployDeliver();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			//获取到的用户id
//			String userId=(String)request.getAttribute("userId");
			//获取用户信息
			String token=getToken(json);
			String openId=data.getString("openid");
			if(token==null||token==""||openId==null||openId==""){
				return login("需要登录");
			}
			JSONObject jsonObject=getUserId(openId, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			String employId=data.getString("employId");
			entity.setEmployId(employId);
			//评论用户
			entity.setCusSelfId(userId);
			//创建人
			entity.setCreateUser(userId);
			//创建时间
			entity.setCreateTime(new Date());
			//状态字段
			String result= weChatService.saveDeliver(entity);
			return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
    }
	/**
	 *  查看我的投递
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listDeliver")
    @ApiOperation(value = "查看我的投递")
    public String listDeliver(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TEmploy> dataList=null;
		PageTableResponse response;
		try {
//			String userId=(String)req.getAttribute("userId");
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			Map<String, Object> params=request.getParams();
			if(params!=null){
				params.put("cusSelfId", userId);
//				params.put("tableName", "t_employ_deliver");
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listCollect(request);
				dataList=(List<TEmploy>)response.getData();
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	/**
	 * 	取消收藏
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 上午9:07:23
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@GetMapping("/cancelCollect")
    @ApiOperation(value = "取消收藏")
    public String cancelCollect(String id,HttpServletRequest request,String openid,String token) {
		try {
//			String userId=(String)request.getAttribute("userId");
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			String result=weChatService.cancelCollect(id,userId);
			return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
    }
	/**
	 *  查看我的我点赞的动态
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listAllGiveUp")
    @ApiOperation(value = "查看我的我点赞的动态")
    public String listAllGiveUp(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TEmploy> dataList=null;
		PageTableResponse response;
		try {
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
//			String userId=(String)req.getAttribute("userId");
			Map<String, Object> params=request.getParams();
			if(params!=null){
				params.put("userId", userId);
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listAllGiveUp(request);
				dataList=(List<TEmploy>)response.getData();
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	/**
	 * 我发布的动态
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listDynamicByMe")
    @ApiOperation(value = "我发布的动态")
    public String listDynamicByMe(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TDynamic> dataList=null;
		PageTableResponse response;
		try {
//			String userId=(String)req.getAttribute("userId");
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			Map<String, Object> params=request.getParams();
			if(params!=null){
				params.put("userId", userId);
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			response = weChatService.listDynamic(request);
			dataList=(List<TDynamic>)response.getData();
			int totalCount=0;
			if(dataList!=null){
				totalCount=totalCount+dataList.size();
			}
			resultMap.put("totalCount", totalCount);
			resultMap.put("list", dataList);
			log.info(JSON.toJSONString(resultMap));
			return success(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
			 return fail("系统异常请联系管理员！");
		}
    }
	/**
	 * 	用户对职位进行评价
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 上午9:07:23
	 *  @param json
	 *  @param request
	 *  @return
	 
	 */
	
	@PostMapping("/saveCompanyComment")
    @ApiOperation(value = "用户点击猜你喜欢左右两边按钮")
    public String saveCompanyComment(@RequestBody String json,HttpServletRequest request) {
		try {
			JSONObject data=JSONObject.parseObject(json);
			TEmployComment entity=new TEmployComment();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			
			//获取用户信息
			String token=getToken(json);
			String openId=data.getString("openid");
			if(token==null||token==""||openId==null||openId==""){
				return login("需要登录");
			}
			
			JSONObject jsonObject=getUserId(openId, token,request);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			
			//获取到的用户id
//			String userId=(String)request.getAttribute("userId");
			String employId=data.getString("employId");
			entity.setEmployId(employId);
			//评论用户
			entity.setCreateUser(userId);
			//不喜欢
			entity.setGiveUp("0");
			//创建时间
			entity.setCreateTime(new Date());
			//状态字段
			String result= weChatService.saveCompanyComment(entity);
			return success(result);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
    }
	//Guess you like
	/**
	 * 	猜你喜欢列表分页
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月11日 下午8:22:40
	 *  @param req
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listYouLikeEmploy")
    @ApiOperation(value = "猜你喜欢列表分页")
    public String listYouLikeEmploy(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TEmploy> dataList=null;
		PageTableResponse response;
		try {
//			String userId=(String)req.getAttribute("userId");
			Map<String, Object> params=request.getParams();
			//猜你喜欢保存在用户信息里面的喜欢的职位类型多选，已分号分隔
//			String employTypes=(String)req.getAttribute("employTypes");
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			String userId=jsonObject.getString("userId");
			String employTypes=jsonObject.getString("employTypes");
			
			if(employTypes!=null){
				if(employTypes.indexOf(";")>-1){
					employTypes="('"+employTypes.replace(";", "';'")+"')";
				}else{
					employTypes="('"+employTypes+"')";
				}
				params.put("employTypes",employTypes);
			}
			
			if(params!=null){
				params.put("userId", userId);
				params.put("isLike", "isLike");
				
			}
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listEmploy(request);
				dataList=(List<TEmploy>)response.getData();
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	
	/**
	 * 	所有公司列表
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月11日 下午10:18:18
	 *  @param req
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listCompany")
    @ApiOperation(value = "所有公司列表")
    public String listCompany(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TCompany> dataList=null;
		PageTableResponse response;
		try {
			
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				response = weChatService.listCompany(request);
				dataList=(List<TCompany>)response.getData();
				int totalCount=0;
				if(dataList!=null){
					totalCount=totalCount+dataList.size();
				}
				resultMap.put("totalCount", totalCount);
				resultMap.put("list", dataList);
				log.info(JSON.toJSONString(resultMap));
				return success(resultMap);
			}else{
				return fail("分页参数不正确，请核实参数");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
		
    }
	/**
	 * 	招聘轮播
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月11日 下午10:18:18
	 *  @param req
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listBanner")
    @ApiOperation(value = "招聘轮播")
    public String listBanner(HttpServletRequest req,PageTableRequest request,Integer pageSize,Integer page,String openid,String token) {
		List<TBanner> dataList=null;
		PageTableResponse response;
		try {
			
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			//判断参数是否完整
			Map<String, Object> params=request.getParams();
			String bannerType=(String)params.get("bannerType");
			if(bannerType==null||bannerType==""){
				return fail("轮播类型字段未传递请校验参数！");
			}
//			//如果没有分页参数表示查询所有
//			if(pageSize!=null && page!=null){
//				Integer offset=(page-1)*pageSize;
//				Integer limit=page*pageSize;
//				request.setOffset(offset);
//				request.setLimit(limit);
//			}else{
//			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			response = weChatService.listBanner(request);
			dataList=(List<TBanner>)response.getData();
			int totalCount=0;
			if(dataList!=null){
				totalCount=totalCount+dataList.size();
			}
			resultMap.put("totalCount", totalCount);
			resultMap.put("list", dataList);
			log.info(JSON.toJSONString(resultMap));
			return success(resultMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail("系统出错了请联系管理员");
		}
    }
	/**
	 * 获取用户id
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月16日 下午3:43:41
	 *  @param openId
	 *  @param token
	 *  @return
	 
	 */
	private  JSONObject getUserId(String openId,String token,HttpServletRequest request){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("token", token);
		paramMap.put("openId", openId);
		String auth = wxService.commenAuth(paramMap);
		JSONObject jsonObject = JSON.parseObject(auth);
		
		if("0".equals((String)jsonObject.get("code"))) {
			jsonObject= new JSONObject();
			CusSelfInfo cusSelfInfo=cusSelfInfoDao.getByOpenId(openId);
			if(cusSelfInfo!=null){
				String userId=cusSelfInfo.getId();
				String employTypes=cusSelfInfo.getEmployTypes();
				jsonObject.put("userId", userId);
				jsonObject.put("employTypes", employTypes);
			}else{
				jsonObject.put("msg", "user is null");
				jsonObject.put("code", "1");
				jsonObject.put("data", "");
			}
			return jsonObject;
		}
		return jsonObject;
	}
	/**
	 * 动态展示
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月10日 下午2:28:01
	 *  @param request
	 *  @param pageSize
	 *  @param page
	 *  @return
	 
	 */
	@GetMapping("/listDynamic")
    @ApiOperation(value = "展示所有动态列表")
    public String listDynamic(PageTableRequest request,Integer pageSize,Integer page,String openid,String token,HttpServletRequest req) {
		List<TDynamic> dataList=null;
		PageTableResponse response;
		try {
			
			if(token==null||token==""||openid==null||openid==""){
				return login("需要登录");
			}
			
			//获取用户信息
			JSONObject jsonObject=getUserId(openid, token,req);
			if(jsonObject!=null &&jsonObject.containsKey("msg")){
				return JSON.toJSONString(jsonObject);
			}
			
			if(pageSize!=null && page!=null){
				Integer offset=(page-1)*pageSize;
				Integer limit=page*pageSize;
				request.setOffset(offset);
				request.setLimit(limit);
				//获取用户信息
//				jsonObject=getUserId(openid, token,req);
//				if(jsonObject.containsKey("userId")){
//					request.getParams().put("userId", jsonObject.get("userId"));
//				}
			}else{
				return fail("分页参数不正确，请核实参数");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			response = weChatService.listDynamic(request);
			String news=weChatService.listNewsByOne(request);
			dataList=(List<TDynamic>)response.getData();
			int totalCount=0;
			if(news!=null){
				totalCount=1;
			}
			if(dataList!=null){
				totalCount=totalCount+dataList.size();
			}
			resultMap.put("news", news);
			resultMap.put("totalCount", totalCount);
			resultMap.put("dynamic", dataList);
			log.info(JSON.toJSONString(resultMap));
			return success(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
			 return fail("系统异常请联系管理员！");
		}
    }
	@GetMapping("/test1")
    @ApiOperation(value = "展示所有动态列表")
	//测试页面-跳转到输入数据的form表单
	public String test1(HttpServletRequest request){
	
		String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
		log.info("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
		String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();
		log.info("协议名：//域名="+url2);
	
	
		log.info("获取项目名="+request.getContextPath());
		log.info("获取参数="+request.getQueryString());
		log.info("获取全路径="+request.getRequestURL());


	return "success";
	}
}
