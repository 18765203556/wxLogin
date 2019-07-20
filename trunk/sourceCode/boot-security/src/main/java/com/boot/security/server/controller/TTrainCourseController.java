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
import com.boot.security.server.dao.TTrainCourseDao;
import com.boot.security.server.model.TTrainCourse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tTrainCourses")
public class TTrainCourseController {

    @Autowired
    private TTrainCourseDao tTrainCourseDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TTrainCourse save(@RequestBody TTrainCourse tTrainCourse) {
    	String id= UUID.randomUUID().toString().replaceAll("-", "");
    	tTrainCourse.setId(id);
    	tTrainCourse.setCreateTime(new Date());
        tTrainCourseDao.save(tTrainCourse);

        return tTrainCourse;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TTrainCourse get(@PathVariable String id) {
        return tTrainCourseDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TTrainCourse update(@RequestBody TTrainCourse tTrainCourse) {
    	tTrainCourse.setLastModTime(new Date());
        tTrainCourseDao.update(tTrainCourse);

        return tTrainCourse;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tTrainCourseDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TTrainCourse> list(PageTableRequest request) {
                return tTrainCourseDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tTrainCourseDao.delete(id);
    }
}
