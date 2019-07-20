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
	@Autowired
    private CommonDao commonDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public TClassification save(@RequestBody TClassification tClassification) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tClassification.setId(id);
    	tClassification.setCreateTime(new Date());
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
    	tClassification.setUpdateTime(new  Date());
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

    @GetMapping("/deleteAll")
    @ApiOperation(value = "删除")
    public void deleteAll(PageTableRequest request,String id) {
        try {
        	Map<String, Object> params=request.getParams();
        	if(id!=null&&!id.equals("")){
        		//拼接update参数
            	//无序拼接
    			tClassificationDao.delete(id);
    			//拼接需要删除用户不喜欢的sql
    			String sqlStrart="employId in ( select id from t_employ t where t.employType= ";
    			String sqlEnd=")";
    			params.put("sqlStrart", sqlStrart);
    			params.put("id", id);
    			params.put("sqlEnd", sqlEnd);
    			params.put("tableName","t_employ_comment");
    			commonDao.deleteAllByOtherId(params);
    			//拼接善用用户收藏的sql
    			sqlStrart="employ_id in ( select id from t_employ t where t.employType= ";
    			params.put("sqlStrart", sqlStrart);
    			params.put("id", id);
    			params.put("sqlEnd", sqlEnd);
    			params.put("tableName","t_employ_collect");
    			commonDao.deleteAllByOtherId(params);
    			//删除用户投递
    			sqlStrart="employ_id in ( select id from t_employ t where t.employType= ";
    			params.put("sqlStrart", sqlStrart);
    			params.put("tableName","t_employ_deliver");
    			commonDao.deleteAllByOtherId(params);
//    			拼接删除职位的sql
    			sqlStrart=" employType = ";
    			params.put("sqlStrart", sqlStrart);
    			params.put("sqlEnd", "");
    			params.put("tableName","t_employ");
    			commonDao.deleteAllByOtherId(params);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
