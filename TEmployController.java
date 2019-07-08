package com.boot.security.server.controller;

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
import com.boot.security.server.dao.TEmployDao;
import com.boot.security.server.model.TEmploy;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tEmploys")
public class TEmployController {

    @Autowired
    private TEmployDao tEmployDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TEmploy save(@RequestBody TEmploy tEmploy) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tEmploy.setId(id);
        tEmployDao.save(tEmploy);

        return tEmploy;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TEmploy get(@PathVariable String id) {
        return tEmployDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TEmploy update(@RequestBody TEmploy tEmploy) {
        tEmployDao.update(tEmploy);

        return tEmploy;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tEmployDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TEmploy> list(PageTableRequest request) {
                return tEmployDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tEmployDao.delete(id);
    }
}
