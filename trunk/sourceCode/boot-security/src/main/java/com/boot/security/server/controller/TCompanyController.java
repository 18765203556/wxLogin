package com.boot.security.server.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.dao.TCompanyDao;
import com.boot.security.server.model.TCompany;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.utils.Const;
import com.boot.security.server.utils.StrUtil;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tCompanys")
public class TCompanyController {
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");
    
	@Autowired
    private TCompanyDao tCompanyDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TCompany save(@RequestBody TCompany tCompany) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tCompany.setId(id);
    	try {
			tCompanyDao.save(tCompany);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        return tCompany;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TCompany get(@PathVariable String id) {
        return tCompanyDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TCompany update(@RequestBody TCompany tCompany) {
        tCompanyDao.update(tCompany);

        return tCompany;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tCompanyDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TCompany> list(PageTableRequest request) {
            	List<TCompany> dataList= tCompanyDao.list(request.getParams(), request.getOffset(), request.getLimit());
//            	String filePath = Const.PICTUREPATH ;
//            	for (TCompany tCompany : dataList) {
//            		if(StrUtil.isNotEmpty(tCompany.getLogoImgPath())) {
//        				log.info(filePath+tCompany.getLogoImgPath());
//        				tCompany.setLogoImgPath(filePath+tCompany.getLogoImgPath());
//        			}
//				}
                return dataList;
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        tCompanyDao.delete(id);
    }
}
