package com.boot.security.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableResponse;
import com.boot.security.server.utils.StrUtil;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.alibaba.fastjson.JSON;
import com.boot.security.server.dao.CusAuditInfoDao;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.dao.DictDao;
import com.boot.security.server.model.CusAuditInfo;
import com.boot.security.server.model.CusSelfInfo;
import com.boot.security.server.model.Dict;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cusAuditInfos")
public class CusAuditInfoController {
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private CusAuditInfoDao cusAuditInfoDao;
    
    @Autowired
    private CusSelfInfoDao cusSelfInfoDao;
    
    @Autowired
    private DictDao dictDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public CusAuditInfo save(@RequestBody CusAuditInfo cusAuditInfo) {
        cusAuditInfoDao.save(cusAuditInfo);

        return cusAuditInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public CusAuditInfo get(@PathVariable String id) {
        return cusAuditInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public CusAuditInfo update(@RequestBody CusAuditInfo cusAuditInfo) {
        cusAuditInfoDao.update(cusAuditInfo);
        return cusAuditInfo;
    }
    
    @PostMapping("/auditCheck")
    @ApiOperation(value = "大V审核")
    public String auditCheck(String id,String status) {
        int check = cusAuditInfoDao.auditCheck(id,status);
        // 修改基本信息表用户身份
        CusAuditInfo auditInfo = cusAuditInfoDao.getById(id);
        if(StrUtil.isNotEmpty(auditInfo)) {
        	String auditStatus = auditInfo.getAuditStatus();
        	String openid = auditInfo.getOpenid();
        	CusSelfInfo cusSelfInfo = new CusSelfInfo();
        	if("1".equals(auditStatus)) {
        		cusSelfInfo.setUserType("2");
        	}else {
        		cusSelfInfo.setUserType("1");
        	}
        	cusSelfInfo.setOpenId(openid);
        	cusSelfInfoDao.update(cusSelfInfo);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(check==1) {
        	resultMap.put("data", "");
			resultMap.put("code", "0");
			resultMap.put("msg", "修改成功");
        }else {
        	resultMap.put("data", "");
			resultMap.put("code", "1");
			resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return cusAuditInfoDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<CusAuditInfo> list(PageTableRequest request) {
            	List<CusAuditInfo> list = cusAuditInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
                // 拼接图片访问地址
            	String filePath = "/statics" ;
        		for(CusAuditInfo cusAuditInfo:list) {
        			if(StrUtil.isNotEmpty(cusAuditInfo.getCertifiedImg())) {
        				log.info(filePath+cusAuditInfo.getCertifiedImg());
        				cusAuditInfo.setCertifiedImg(filePath+cusAuditInfo.getCertifiedImg());
        				Dict dict = dictDao.getByTypeAndK("auditStatus", cusAuditInfo.getAuditStatus());
        				if(StrUtil.isNotEmpty(dict)) {
        					cusAuditInfo.setAuditStatus(dict.getVal());
        				}
        			}
        		}
            	return list;
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        cusAuditInfoDao.delete(id);
    }
}
