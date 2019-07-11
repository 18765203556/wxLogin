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
import com.boot.security.server.dao.TWorkHisDao;
import com.boot.security.server.model.TWorkHis;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tWorkHiss")
public class TWorkHisController {

    @Autowired
    private TWorkHisDao tWorkHisDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TWorkHis save(@RequestBody TWorkHis tWorkHis) {
        tWorkHisDao.save(tWorkHis);

        return tWorkHis;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TWorkHis get(@PathVariable String id) {
        return tWorkHisDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TWorkHis update(@RequestBody TWorkHis tWorkHis) {
        tWorkHisDao.update(tWorkHis);

        return tWorkHis;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tWorkHisDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TWorkHis> list(PageTableRequest request) {
                return tWorkHisDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tWorkHisDao.delete(id);
    }
}
