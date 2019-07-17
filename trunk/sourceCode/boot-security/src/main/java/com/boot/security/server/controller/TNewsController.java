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
import com.boot.security.server.dao.TNewsDao;
import com.boot.security.server.model.TComment;
import com.boot.security.server.model.TNews;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tNewss")
public class TNewsController {

    @Autowired
    private TNewsDao tNewsDao;
    @Autowired
    private TCommentDao tCommentDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TNews save(@RequestBody TNews tNews) {
    	 String id= UUID.randomUUID().toString().replaceAll("-", "");;
    	 tNews.setId(id);
    	 tNews.setDynamicType("news");
    	 tNews.setCreateTime(new Date());
        tNewsDao.save(tNews);

        return tNews;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TNews get(@PathVariable String id) {
        return tNewsDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TNews update(@RequestBody TNews tNews) {
    	tNews.setUpdateTime(new Date());
        tNewsDao.update(tNews);

        return tNews;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tNewsDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TNews> list(PageTableRequest request) {
                return tNewsDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tNewsDao.delete(id);
        tCommentDao.deleteAllByDynamicId(id);
    }
    /**
     * 根据动态id获取动态信息
     *  Description:
     *  @author xiaoding  DateTime 2019年7月9日 上午11:00:35
     *  @param id
     *  @return
     
     */
    @GetMapping("/getNewsById/{id}")
    @ApiOperation(value = "根据id获取")
    public TNews getNewsById(@PathVariable String id) {
    	//查询标题
    	TNews tNews=tNewsDao.getNewsById(id,null);
    	if(tNews!=null){
    		 //查询评论相关
        	List<TComment> commentAll=tCommentDao.getAllCommentByDynamicId(id);
        	tNews.setCommentAll(commentAll);
    	}else new TNews();
    	return tNews;
    }
}
