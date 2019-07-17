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

import com.boot.security.server.dao.TCommentDao;
import com.boot.security.server.dao.TDynamicDao;
import com.boot.security.server.model.TComment;
import com.boot.security.server.model.TDynamic;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tDynamics")
public class TDynamicController {

    @Autowired
    private TDynamicDao tDynamicDao;
    @Autowired
    private TCommentDao tCommentDao;
    
    @PostMapping
    @ApiOperation(value = "保存")
    public TDynamic save(@RequestBody TDynamic tDynamic) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tDynamic.setId(id);
    	tDynamic.setDynamicType("dynamic");
    	tDynamic.setCreateTime(new Date());
        tDynamicDao.save(tDynamic);

        return tDynamic;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TDynamic get(@PathVariable String id) {
        return tDynamicDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TDynamic update(@RequestBody TDynamic tDynamic) {
    	tDynamic.setUpdateTime(new Date());
        tDynamicDao.update(tDynamic);

        return tDynamic;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tDynamicDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TDynamic> list(PageTableRequest request) {
                return tDynamicDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tDynamicDao.delete(id);
        tCommentDao.deleteAllByDynamicId(id);
    }
    /**
     * 根据动态id获取动态信息
     *  Description:
     *  @author xiaoding  DateTime 2019年7月9日 上午11:00:35
     *  @param id
     *  @return
     
     */
    @GetMapping("/getUserDynamicById/{id}")
    @ApiOperation(value = "根据id获取")
    public TDynamic getUserDynamicById(@PathVariable String id) {
    	//查询标题
    	TDynamic tDynamic=tDynamicDao.getUserDynamicById(id,null);
        //查询评论相关
    	List<TComment> commentAll=tCommentDao.getAllCommentByDynamicId(id);
    	tDynamic.setCommentAll(commentAll);
    	return tDynamic;
    }
    
}
