package com.boot.security.server.controller;

import java.util.List;


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
import com.boot.security.server.dao.CusAuditInfoDao;
import com.boot.security.server.model.CusAuditInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cusAuditInfos")
public class CusAuditInfoController {
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private CusAuditInfoDao cusAuditInfoDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public CusAuditInfo save(@RequestBody CusAuditInfo cusAuditInfo) {
        cusAuditInfoDao.save(cusAuditInfo);

        return cusAuditInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public CusAuditInfo get(@PathVariable Long id) {
        return cusAuditInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public CusAuditInfo update(@RequestBody CusAuditInfo cusAuditInfo) {
        cusAuditInfoDao.update(cusAuditInfo);

        return cusAuditInfo;
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
            	String filePath = "statics" ;
        		for(CusAuditInfo cusAuditInfo:list) {
        			if(StrUtil.isNotEmpty(cusAuditInfo.getCertifiedImg())) {
        				log.info(filePath+cusAuditInfo.getCertifiedImg());
        				cusAuditInfo.setCertifiedImg(filePath+cusAuditInfo.getCertifiedImg());
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
