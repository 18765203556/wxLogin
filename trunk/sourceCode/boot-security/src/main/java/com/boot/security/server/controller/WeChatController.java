package com.boot.security.server.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.server.model.TComment;
import com.boot.security.server.model.TDynamic;
import com.boot.security.server.service.WeChatService;
import com.boot.security.server.utils.BaseController;

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
	/**
	 * 获取用户id
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月9日 下午10:02:56
	 *  @param request
	 *  @param id
	 *  @return
	 
	 */
	@GetMapping("/exampale")
    @ApiOperation(value = "根据id获取")
    public String get(HttpServletRequest request, String id) {
		System.out.println("fangwenkongzhiWXSaveController!!!!!!!!!!!");
		String userId=(String)request.getAttribute("userId");
		String openId=request.getParameter("openId");
		System.out.println(openId);
        return userId;
    }
	/**
	 * 	展示所有动态列表
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月9日 下午10:03:19
	 *  @param request
	 *  @param id
	 *  @return
	 
	 */
	
	@GetMapping("/listDynamic")
    @ApiOperation(value = "展示所有动态列表")
    public String listDynamic() {
		List<TDynamic> dataList;
		try {
			dataList = weChatService.listDynamic();
			log.info(JSON.toJSONString(dataList));
			return success(dataList);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
			 return fail("系统异常请联系管理员！");
		}
    }
	/**
	 * 	动态详情
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月9日 下午10:03:19
	 *  @param request
	 *  @param id
	 *  @return
	 
	 */
	
	@GetMapping("/dynamicDetail")
    @ApiOperation(value = "展示所有动态列表")
    public String dynamicDetail(String id) {
		TDynamic data;
		try {
			data = weChatService.dynamicDetail(id);
			log.info(JSON.toJSONString(data));
			 return success(data);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(JSON.toJSONString(e));
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
			TDynamic entity=new TDynamic();
			//获取id
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			entity.setContent(data.getString("content"));
			entity.setPictureIds(data.getString("pictureIds"));
			entity.setPicturePath(data.getString("picturePath"));
			entity.setStatus(data.getString("status"));
			//获取到的用户id
			String userId=(String)request.getAttribute("userId");
			entity.setCreateUser(userId);
			 weChatService.saveDynamic(entity);
			 return success(null);
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
			String userId=(String)request.getAttribute("userId");
			//评论用户
			entity.setCommentUser(userId);
			//创建人
			entity.setCreateUser(userId);
			//状态字段
			entity.setStatus(data.getString("status"));
			 weChatService.saveComment(entity);
			 return success(null);
		} catch (Exception e) {
			e.printStackTrace();
			 return fail("系统异常请联系管理员！");
		}
       
    }
}
