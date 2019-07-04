package com.boot.security.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.annotation.LogAnnotation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 微信相关接口
 * 
 * @author mdd
 *
 */
@Api(tags = "微信")

@RestController
@RequestMapping("/wx")
public class WxController {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@LogAnnotation
	@PutMapping("register/{username}/{mobile}")
	@ApiOperation(value = "用户注册")
	public void register(@PathVariable String username,@PathVariable String mobile) {
		// 微信用户注册接口，不需token鉴权
		log.info("微信用户注册>>>>>>>>>>>"+username);
	}
	
	@LogAnnotation
	@PutMapping("getVerifyCode/{openId}")
	@ApiOperation(value = "获取短信验证码接口")
	public void getVerifyCode(@PathVariable String openId) {
		// 获取短信验证码
		log.info("微信用户openId>>>>>>>>>>>"+openId);
	}

	
}
