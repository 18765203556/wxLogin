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
import com.boot.security.server.dao.TCommentDao;
import com.boot.security.server.model.TComment;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tComments")
public class TCommentController {

    @Autowired
    private TCommentDao tCommentDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TComment save(@RequestBody TComment tComment) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tComment.setId(id);
        tCommentDao.save(tComment);
        return tComment;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TComment get(@PathVariable Long id) {
        return tCommentDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TComment update(@RequestBody TComment tComment) {
        tCommentDao.update(tComment);

        return tComment;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tCommentDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TComment> list(PageTableRequest request) {
                return tCommentDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        tCommentDao.delete(id);
    }
}
