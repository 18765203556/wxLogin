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
import com.boot.security.server.dao.TTrainingClassificationDao;
import com.boot.security.server.model.TTrainingClassification;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tTrainingClassifications")
public class TTrainingClassificationController {

    @Autowired
    private TTrainingClassificationDao tTrainingClassificationDao;
//    @Autowired
//    private TOrganDao tOrganDao;
//    @Autowired
//    private TTrainCourseDao tTrainCourseDao;
    @Autowired
    private CommonDao commonDao;
    @PostMapping
    @ApiOperation(value = "保存")
    public TTrainingClassification save(@RequestBody TTrainingClassification tTrainingClassification) {
    	String id= UUID.randomUUID().toString().replaceAll("-", "");
    	tTrainingClassification.setId(id);
    	tTrainingClassification.setCreateTime(new Date());
    	
        tTrainingClassificationDao.save(tTrainingClassification);
        return tTrainingClassification;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TTrainingClassification get(@PathVariable String id) {
        return tTrainingClassificationDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TTrainingClassification update(@RequestBody TTrainingClassification tTrainingClassification) {
    	tTrainingClassification.setLastModTime(new Date());
        tTrainingClassificationDao.update(tTrainingClassification);

        return tTrainingClassification;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return tTrainingClassificationDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TTrainingClassification> list(PageTableRequest request) {
                return tTrainingClassificationDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除")
    public void delete(PageTableRequest request,  String id) {
        try {
        	Map<String, Object> params=request.getParams();
        	if(id!=null&&!id.equals("")){
	        	//删除分类
				tTrainingClassificationDao.delete(id);
				//tTrainCourseDao.deleteAllByTrainClassId(id);
				//tOrganDao.deleteAllByTrainClassId(id);
				//删除课程
				String sqlStrart="trainClass = ";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_train_course");
				commonDao.deleteAllByOtherId(params);
				//删除机构
				sqlStrart="train_class = ";
				params.put("sqlStrart", sqlStrart);
				params.put("id", id);
				params.put("tableName","t_organ");
				commonDao.deleteAllByOtherId(params);
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * 查询所有课程分类
     *  Description:
     *  @author xiaoding  DateTime 2019年7月20日 上午10:04:31
     *  @return
     
     */
    @GetMapping("/getAll")
    public List<TTrainingClassification> getAll(PageTableRequest request){
    	return tTrainingClassificationDao.getAll();
    	
    }
}
