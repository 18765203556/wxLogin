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
import com.boot.security.server.utils.DateTimeUtil;
import com.boot.security.server.dao.TBannerDao;
import com.boot.security.server.model.TBanner;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tBanners")
public class TBannerController {

    @Autowired
    private TBannerDao tBannerDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TBanner save(@RequestBody TBanner tBanner) {
    	tBanner.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	tBanner.setCreateTime(DateTimeUtil.getCurrentDateTime());
    	tBanner.setLastModTime(DateTimeUtil.getCurrentDateTime());
        tBannerDao.save(tBanner);
        return tBanner;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TBanner get(@PathVariable String id) {
        return tBannerDao.getById(id);
    }
  
    @PutMapping
    @ApiOperation(value = "修改")
    public TBanner update(@RequestBody TBanner tBanner) {
    	tBanner.setLastModTime(DateTimeUtil.getCurrentDateTime());
        tBannerDao.update(tBanner);
        return tBanner;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tBannerDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TBanner> list(PageTableRequest request) {
                return tBannerDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tBannerDao.delete(id);
    }
}
