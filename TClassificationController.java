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

import com.boot.security.server.dao.TClassificationDao;
import com.boot.security.server.model.TClassification;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tClassifications")
public class TClassificationController {

    @Autowired
    private TClassificationDao tClassificationDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TClassification save(@RequestBody TClassification tClassification) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tClassification.setId(id);
        tClassificationDao.save(tClassification);

        return tClassification;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TClassification get(@PathVariable String id) {
        return tClassificationDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TClassification update(@RequestBody TClassification tClassification) {
        tClassificationDao.update(tClassification);

        return tClassification;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tClassificationDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TClassification> list(PageTableRequest request) {
                return tClassificationDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tClassificationDao.delete(id);
    }
    /**
     * 展示所有
     *  Description:
     *  @author xiaoding  DateTime 2019年7月8日 下午2:16:05
     *  @return
     
     */
    @GetMapping("/all")
   	@ApiOperation(value = "所有岗位分类")
//   	@PreAuthorize("hasAuthority('sys:menu:query')")
   	public List<TClassification>  permissionsAll() {
   		List<TClassification> sysDictionariesAll = tClassificationDao.listAll();
   		return sysDictionariesAll;
   	}
}
