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

import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableResponse;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.model.CusSelfInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cusSelfInfos")
public class CusSelfInfoController {

    @Autowired
    private CusSelfInfoDao cusSelfInfoDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public CusSelfInfo save(@RequestBody CusSelfInfo cusSelfInfo) {
        cusSelfInfoDao.save(cusSelfInfo);

        return cusSelfInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public CusSelfInfo get(@PathVariable String id) {
        return cusSelfInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public CusSelfInfo update(@RequestBody CusSelfInfo cusSelfInfo) {
        cusSelfInfoDao.update(cusSelfInfo);

        return cusSelfInfo;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return cusSelfInfoDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<CusSelfInfo> list(PageTableRequest request) {
                return cusSelfInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        cusSelfInfoDao.delete(id);
    }
}
