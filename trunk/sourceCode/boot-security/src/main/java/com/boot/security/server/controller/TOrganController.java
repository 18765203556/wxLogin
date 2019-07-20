package com.boot.security.server.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import com.boot.security.server.dao.TOrganDao;
import com.boot.security.server.model.TOrgan;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tOrgans")
public class TOrganController {

    @Autowired
    private TOrganDao tOrganDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TOrgan save(@RequestBody TOrgan tOrgan) {
    	String id= UUID.randomUUID().toString().replaceAll("-", "");
    	tOrgan.setId(id);
    	tOrgan.setCreateTime(new Date());
        tOrganDao.save(tOrgan);
        return tOrgan;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TOrgan get(@PathVariable String id) {
        return tOrganDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TOrgan update(@RequestBody TOrgan tOrgan) {
    	tOrgan.setLastModTime(new Date());
        tOrganDao.update(tOrgan);
        return tOrgan;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tOrganDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TOrgan> list(PageTableRequest request) {
                return tOrganDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tOrganDao.delete(id);
    }
}
