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
import com.boot.security.server.dao.TOrganDao;
import com.boot.security.server.model.TOrgan;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tOrgans")
public class TOrganController {

    @Autowired
    private TOrganDao tOrganDao;
//    @Autowired
//    private TTrainCourseDao tTrainCourseDao;
    @Autowired
    private CommonDao commonDao;
    @PostMapping
    @ApiOperation(value = "保存")
    public TOrgan save(@RequestBody TOrgan tOrgan) {
    	String id= UUID.randomUUID().toString().replaceAll("-", "");
    	tOrgan.setId(id);
    	tOrgan.setCreateTime(new Date());
        tOrganDao.save(tOrgan);
        return tOrgan;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TOrgan get(@PathVariable String id) {
        return tOrganDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TOrgan update(@RequestBody TOrgan tOrgan) {
    	tOrgan.setLastModTime(new Date());
        tOrganDao.update(tOrgan);
        return tOrgan;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tOrganDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TOrgan> list(PageTableRequest request) {
                return tOrganDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除")
    public void delete(PageTableRequest request,  String id) {
    	
    	try {
    		Map<String, Object> params=request.getParams();
        	if(id!=null&&!id.equals("")){
	    		//删除课程
//				tTrainCourseDao.deleteAllByOrganId(id);
	    		String sqlStrart="trainOrgan = ";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_train_course");
				commonDao.deleteAllByOtherId(params);
				//删除机构
				tOrganDao.delete(id);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 获取所有机构列表
     *  Description:
     *  @author xiaoding  DateTime 2019年7月20日 下午12:26:09
     *  @return
     
     */
    @GetMapping("/getAllOrgan")
    @ApiOperation(value = "获取所有机构列表")
    public  List<TOrgan> getAllOrgan(PageTableRequest request){
    	return tOrganDao.getAllOrgan(request.getParams());
    }
}
