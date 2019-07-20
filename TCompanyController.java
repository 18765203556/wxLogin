package com.boot.security.server.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.dao.CommonDao;
import com.boot.security.server.dao.TCompanyDao;
import com.boot.security.server.model.TCompany;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tCompanys")
public class TCompanyController {
	
	private static final Logger log = LoggerFactory.getLogger("adminLogger");
    
	@Autowired
    private TCompanyDao tCompanyDao;
//	@Autowired
//    private TEmployDao tEmployDao;
//	@Autowired
//    private TEmployCommentDao tEmployCommentDao;
//	@Autowired
//	private TEmployCollectDao tEmployCollectDao;
//	@Autowired
//	private TEmployDeliverDao tEmployDeliverDao;
	@Autowired
    private CommonDao commonDao;
	
    @PostMapping
    @ApiOperation(value = "保存")
    public TCompany save(@RequestBody TCompany tCompany) {
    	String id=UUID.randomUUID().toString().toString().replaceAll("-", "");
    	tCompany.setId(id);
    	try {
    		tCompany.setCreateTime(new Date());
			tCompanyDao.save(tCompany);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.toString());
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
    	tCompany.setUpdateTime(new Date());
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

    @GetMapping("/delete")
    @ApiOperation(value = "删除")
    public void delete(PageTableRequest request, String id) {
        try {
        	Map<String, Object> params=request.getParams();
        	if(id!=null&&!id.equals("")){
				tCompanyDao.delete(id);
				//删除用户不喜欢表
//				tEmployCommentDao.deleteAllByCompanyId(id);
				//删除用户收藏
//				tEmployCollectDao.deleteAllByCompanyId(id);
				//删除用户投递
//				tEmployDeliverDao.deleteAllByCompanyId(id);
				//删除职位表
//				tEmployDao.deleteByCompanyId(id);
				
				//为修改做准备
				//删除用户不喜欢表
				String sqlStrart="employId in ( select id from t_employ t where t.companyId= ";
    			String sqlEnd=")";
    			params.put("sqlStrart", sqlStrart);
    			params.put("id", id);
    			params.put("sqlEnd", sqlEnd);
    			params.put("tableName","t_employ_comment");
    			commonDao.deleteAllByOtherId(params);
    			//删除用户收藏
    			sqlStrart="employ_id in (select id from t_employ t where t.companyId=  ";
    			params.put("sqlStrart", sqlStrart);
    			params.put("id", id);
    			params.put("tableName","t_employ_collect");
    			commonDao.deleteAllByOtherId(params);
    			//删除用户投递
    			sqlStrart="employ_id in ( select id from t_employ t where t.companyId= ";
    			params.put("sqlStrart", sqlStrart);
    			params.put("id", id);
    			params.put("tableName","t_employ_deliver");
    			commonDao.deleteAllByOtherId(params);
    			//删除职位表
    			sqlStrart=" companyId = ";
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
   	@ApiOperation(value = "所有公司")
//   	@PreAuthorize("hasAuthority('sys:menu:query')")
   	public List<TCompany>  permissionsAll() {
   		List<TCompany> sysDictionariesAll = tCompanyDao.listAll();
   		return sysDictionariesAll;
   	}
}
