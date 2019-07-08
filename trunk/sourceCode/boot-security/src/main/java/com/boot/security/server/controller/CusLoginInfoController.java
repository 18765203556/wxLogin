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
import com.boot.security.server.dao.CusLoginInfoDao;
import com.boot.security.server.model.CusLoginInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cusLoginInfos")
public class CusLoginInfoController {

    @Autowired
    private CusLoginInfoDao cusLoginInfoDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public CusLoginInfo save(@RequestBody CusLoginInfo cusLoginInfo) {
        cusLoginInfoDao.save(cusLoginInfo);

        return cusLoginInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public CusLoginInfo get(@PathVariable Long id) {
        return cusLoginInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public CusLoginInfo update(@RequestBody CusLoginInfo cusLoginInfo) {
        cusLoginInfoDao.update(cusLoginInfo);

        return cusLoginInfo;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return cusLoginInfoDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<CusLoginInfo> list(PageTableRequest request) {
                return cusLoginInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        cusLoginInfoDao.delete(id);
    }
}
