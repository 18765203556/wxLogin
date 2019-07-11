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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boot.security.server.dao.SysDictionariesDao;
import com.boot.security.server.model.SysDictionaries;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableResponse;

import io.swagger.annotations.ApiOperation;
/**
 * 字典表相关
 *  Class Name: SysDictionariesController.java
 *  Description: 
 *  @author xiaoding  DateTime 2019年7月4日 下午6:43:45 
 *  @company xiaoding 
 *  @version 1.0
 *  
 
 */
@RestController
@RequestMapping("/sysDictionariess")
public class SysDictionariesController {

    @Autowired
    private SysDictionariesDao sysDictionariesDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public SysDictionaries save(@RequestBody SysDictionaries sysDictionaries) {
    	 String dictionariesId= UUID.randomUUID().toString().replaceAll("-", "");;
    	 sysDictionaries.setDictionariesId(dictionariesId);
    	 sysDictionaries.setCreateTime(new Date());
    	 sysDictionariesDao.save(sysDictionaries);

        return sysDictionaries;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public SysDictionaries get(@PathVariable String id) {
        return sysDictionariesDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public SysDictionaries update(@RequestBody SysDictionaries sysDictionaries) {
    	sysDictionaries.setUpdateTime(new Date());
        sysDictionariesDao.update(sysDictionaries);

        return sysDictionaries;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return sysDictionariesDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<SysDictionaries> list(PageTableRequest request) {
                return sysDictionariesDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        sysDictionariesDao.delete(id);
    }
    /**
     * 根据父类id=0查询所有子类
     *  Description:
     *  @author xiaoding  DateTime 2019年7月4日 下午6:52:15
     *  @param request
     *  @return
     
     */
//    @GetMapping
//    @RequestMapping("listByParentId")
    @GetMapping("/parents")
    @ApiOperation(value = "一级")
//    @PreAuthorize("hasAuthority('sys:menu:query')")
    public List<SysDictionaries> listByParentId() {
    	List<SysDictionaries> sysDictionariesAll=sysDictionariesDao.listByParentId();
       return sysDictionariesAll;
    }
    /**
     * 查询所有
     *  Description:
     *  @author xiaoding  DateTime 2019年7月4日 下午8:02:57
     *  @return
     
     */
    @GetMapping("/all")
	@ApiOperation(value = "所有菜单")
//	@PreAuthorize("hasAuthority('sys:menu:query')")
	public JSONArray permissionsAll() {
		List<SysDictionaries> sysDictionariesAll = sysDictionariesDao.listAll();
		JSONArray array = new JSONArray();
		setSysDictionariesTree("0", sysDictionariesAll, array);
		return array;
	}
    
    
    /**
	 * 菜单树
	 * 
	 * @param pId
	 * @param permissionsAll
	 * @param array
	 */
	private void setSysDictionariesTree(String pId, List<SysDictionaries> sysDictionariesAll, JSONArray array) {
		for (SysDictionaries per : sysDictionariesAll) {
			if (per.getParentId().equals(pId)) {
				String string = JSONObject.toJSONString(per);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);

				if (sysDictionariesAll.stream().filter(p -> p.getParentId().equals(per.getDictionariesId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setSysDictionariesTree(per.getDictionariesId(), sysDictionariesAll, child);
				}
			}
		}
	}
	/**
	 * 根据父类id查询所有子类
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月4日 下午8:21:41
	 *  @param roleId
	 *  @return
	 
	 */
	@GetMapping(params = "parentId")
	@ApiOperation(value = "根据角色id获取权限")
//	@PreAuthorize("hasAnyAuthority('sys:menu:query','sys:role:query')")
	public List<SysDictionaries> listByRoleId(String parentId) {
		return sysDictionariesDao.listAllChilds(parentId);
	}
	/**
	 * 根据code查询顶级
	 *  Description:
	 *  @author xiaoding  DateTime 2019年7月4日 下午8:21:41
	 *  @param roleId
	 *  @return
	 
	 */
	@GetMapping("/getDataByCode/{bianma}")
	@ApiOperation(value = "根据角色id获取权限")
//	@PreAuthorize("hasAnyAuthority('sys:menu:query','sys:role:query')")
	public SysDictionaries getDataByCode(@PathVariable String bianma) {
		return sysDictionariesDao.getDataByCode(bianma);
	}
}
