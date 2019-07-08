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
import com.boot.security.server.dao.SmsVerifycodeDao;
import com.boot.security.server.model.SmsVerifycode;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/smsVerifycodes")
public class SmsVerifycodeController {

    @Autowired
    private SmsVerifycodeDao smsVerifycodeDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public SmsVerifycode save(@RequestBody SmsVerifycode smsVerifycode) {
        smsVerifycodeDao.save(smsVerifycode);

        return smsVerifycode;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public SmsVerifycode get(@PathVariable Long id) {
        return smsVerifycodeDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public SmsVerifycode update(@RequestBody SmsVerifycode smsVerifycode) {
        smsVerifycodeDao.update(smsVerifycode);

        return smsVerifycode;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return smsVerifycodeDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<SmsVerifycode> list(PageTableRequest request) {
                return smsVerifycodeDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        smsVerifycodeDao.delete(id);
    }
}
