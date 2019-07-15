package com.boot.security.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableResponse;
import com.boot.security.server.service.ResumeService;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.dao.CusSelfInfoDao;
import com.boot.security.server.dao.ResumeDao;
import com.boot.security.server.model.CusSelfInfo;
import com.boot.security.server.model.TEmploy;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @Autowired
    private ResumeDao resumeDao;
    
    @Autowired
    private ResumeService resumeService;
    
    @Autowired
	private CusSelfInfoDao cusSelfInfoDao;

   
    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return resumeDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TEmploy> list(PageTableRequest request) {
                return resumeDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }
    
    @GetMapping("/detailList")
    @ApiOperation(value = "根据职位id获取投递详情列表")
    public PageTableResponse get(PageTableRequest request) {
    	 return new PageTableHandler(new CountHandler() {

             @Override
             public int count(PageTableRequest request) {
                 return resumeDao.countByEmploy(request.getParams());
             }
         }, new ListHandler() {

             @Override
             public List<TEmploy> list(PageTableRequest request) {
                 return resumeDao.getByEmployId(request.getParams(), request.getOffset(), request.getLimit());
             }
         }).handle(request);
    }

    @GetMapping("/detail/{cusSelfId}")
    @ApiOperation(value = "根据客户基本信息id和职位id查询详情")
    public String getDetail(@PathVariable String cusSelfId) {
    	CusSelfInfo detail = cusSelfInfoDao.getById(cusSelfId);
    	return resumeService.getDetail(cusSelfId,detail.getOpenId());
    }
}
