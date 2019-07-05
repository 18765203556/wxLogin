package com.boot.security.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.dao.PlatformInfoDao;
import com.boot.security.server.model.PlatformInfo;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;
/**
 * 平台相关
 *  Class Name: PlatformInfoController.java
 *  Description: 
 *  @author xiaoding  DateTime 2019年7月5日 下午1:44:28 
 *  @company xiaoding 
 *  @version 1.0
 *  
 
 */
@RestController
@RequestMapping("/platformInfos")
public class PlatformInfoController {

    @Autowired
    private PlatformInfoDao platformInfoDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public PlatformInfo save(@RequestBody PlatformInfo platformInfo) {
    	
        platformInfoDao.save(platformInfo);

        return platformInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public PlatformInfo get(@PathVariable Long id) {
        return platformInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public PlatformInfo update(@RequestBody PlatformInfo platformInfo) {
        platformInfoDao.update(platformInfo);

        return platformInfo;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return platformInfoDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<PlatformInfo> list(PageTableRequest request) {
                return platformInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        platformInfoDao.delete(id);
    }
}
