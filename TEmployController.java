package com.boot.security.server.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.dao.CommonDao;
import com.boot.security.server.dao.TEmployDao;
import com.boot.security.server.model.TEmploy;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tEmploys")
public class TEmployController {

    @Autowired
    private TEmployDao tEmployDao;
//    @Autowired
//    private TEmployCommentDao tEmployCommentDao;
//	@Autowired
//	private TEmployCollectDao tEmployCollectDao;
//	@Autowired
//	private TEmployDeliverDao tEmployDeliverDao;
	@Autowired
    private CommonDao commonDao;
    @PostMapping
    @ApiOperation(value = "保存")
    public TEmploy save(@RequestBody TEmploy tEmploy) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tEmploy.setId(id);
    	tEmploy.setCreateTime(new Date());
        tEmployDao.save(tEmploy);

        return tEmploy;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TEmploy get(@PathVariable String id) {
        return tEmployDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TEmploy update(@RequestBody TEmploy tEmploy) {
    	tEmploy.setUpdateTime(new Date());
        tEmployDao.update(tEmploy);

        return tEmploy;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tEmployDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TEmploy> list(PageTableRequest request) {
                return tEmployDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除")
    public void delete(PageTableRequest request, String id) {
        try {
        	Map<String, Object> params=request.getParams();
        	if(id!=null&&!id.equals("")){
				tEmployDao.delete(id);
				//删除用户不喜欢表
//				tEmployCommentDao.deleteAllByEmployId(id);
				//删除用户收藏
//				tEmployCollectDao.deleteAllByEmployId(id);
				//删除用户投递
//				tEmployDeliverDao.deleteAllByEmployId(id);
				//删除用户不喜欢表
				String sqlStrart="employId = ";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_employ_comment");
				commonDao.deleteAllByOtherId(params);
				//删除用户收藏
				sqlStrart="employ_id =  ";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_employ_collect");
				commonDao.deleteAllByOtherId(params);
				//删除用户投递
				sqlStrart="employ_id =";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_employ_deliver");
				commonDao.deleteAllByOtherId(params);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
