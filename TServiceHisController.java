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
import com.boot.security.server.dao.TServiceHisDao;
import com.boot.security.server.model.TServiceHis;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tServiceHiss")
public class TServiceHisController {

    @Autowired
    private TServiceHisDao tServiceHisDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TServiceHis save(@RequestBody TServiceHis tServiceHis) {
        tServiceHisDao.save(tServiceHis);

        return tServiceHis;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TServiceHis get(@PathVariable String id) {
        return tServiceHisDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TServiceHis update(@RequestBody TServiceHis tServiceHis) {
        tServiceHisDao.update(tServiceHis);

        return tServiceHis;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tServiceHisDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TServiceHis> list(PageTableRequest request) {
                return tServiceHisDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tServiceHisDao.delete(id);
    }
}
