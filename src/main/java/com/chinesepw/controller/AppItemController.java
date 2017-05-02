package com.chinesepw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinesepw.po.Appitem;
import com.chinesepw.po.AppitemCustom;
import com.chinesepw.service.IAppItemService;
import com.chinesepw.service.IApptypelistService;
import com.chinesepw.service.ITypeService;
import com.chinesepw.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author HJW
 * @date 2017年4月23日
 * App的管理
 */

@Controller
@RequestMapping(value="appItem")
public class AppItemController {
	
	@Autowired
	IAppItemService iAppItemService;
	@Autowired
	ITypeService iTypeService;
	@Autowired
	IApptypelistService iApptypelistService;
	@Autowired
	IUserService iUserService;
	
	
	@RequestMapping(value="/queryAll",method=RequestMethod.GET)
	public String queryAll(Model model, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize, HttpServletRequest req, HttpServletResponse resp) {
		PageHelper.startPage(pageNum, pageSize);
		List<AppitemCustom> appitemList = iAppItemService.queryAll();
		for (AppitemCustom ac : appitemList) {
			ac.setUserName(iUserService.selectByPrimaryKey(ac.getUserId()).getUsername());
			ac.setTypeName(this.getTypeName(ac.getAppId()));
			if (ac.getState()) {
				ac.setStateStr("已通过");
			}else{
				ac.setStateStr("未通过");
			}
		} 
		PageInfo<AppitemCustom> pageInfo = new PageInfo<AppitemCustom>(appitemList);
		model.addAttribute("appList", appitemList);
		model.addAttribute("page", pageInfo);
		return "/WEB-INF/manager/appList";
	}
	
	@RequestMapping(value="/queryPending",method =RequestMethod.GET)
	public String queryPending(Model model, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize, HttpServletRequest req, HttpServletResponse resp) {
		PageHelper.startPage(pageNum, pageSize);
		List<AppitemCustom> appitemPendingList = iAppItemService.queryPending();
		for (AppitemCustom ac : appitemPendingList) {
			ac.setUserName(iUserService.selectByPrimaryKey(ac.getUserId()).getUsername());
			ac.setTypeName(this.getTypeName(ac.getAppId()));
			ac.setStateStr("未通过");
		} 
		PageInfo<AppitemCustom> pageInfo = new PageInfo<AppitemCustom>(appitemPendingList);
		model.addAttribute("appList", appitemPendingList);
		model.addAttribute("page", pageInfo);
		return "/WEB-INF/manager/pendingItem";
	}
	
	@RequestMapping(value="/del/{id}",method=RequestMethod.GET)
	public String deleteByPrimaryKey(@PathVariable("id") Integer appId,HttpServletRequest req, HttpServletResponse resp) {
		/*关联删除*/
		iApptypelistService.deleteByappId(appId);
		iAppItemService.deleteByPrimaryKey(appId);
		if (req.getParameter("p").equals("pending")) {
			return "redirect: ../queryPending";
		}else{
			return "redirect: ../queryAll";
		}
	}
	
	@RequestMapping(value="/selectDel",method=RequestMethod.POST)
	public String selectDeleteByPrimaryKey(HttpServletRequest req, HttpServletResponse resp) {
		String[] appinfos = req.getParameterValues("appinfo");
		for (String appId : appinfos) {
			if (appId != null) {
				iApptypelistService.deleteByappId(Integer.parseInt(appId));
				iAppItemService.deleteByPrimaryKey(Integer.parseInt(appId));			
			}
		}
		if (req.getParameter("p").equals("pending")) {
			return "redirect: queryPending";
		}else{
			return "redirect: queryAll";
		}
	}

	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/WEB-INF/manager/addApp";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String insertSelective(Appitem record) {
		iAppItemService.insertSelective(record);
		return "redirect: queryAll";
	}
	@ResponseBody
	@RequestMapping(value="select/{id}",method=RequestMethod.GET)
	public Appitem selectByPrimaryKey(@PathVariable("id") Integer appId) {
		return iAppItemService.selectByPrimaryKey(appId);
	}

	@RequestMapping(value="up/{id}",method=RequestMethod.GET)
	public String updateBefore(Model model, @PathVariable("id") Integer appId) {
		Appitem appitem = iAppItemService.selectByPrimaryKey(appId);
		model.addAttribute("appItem", appitem);
		return "/WEB-INF/manager/addApp";
	}
	
	@RequestMapping(value="update/{id}",method=RequestMethod.POST)
	public String updateByPrimaryKeyWithBLOBs(Appitem record) {
		iAppItemService.updateByPrimaryKeyWithBLOBs(record);
		return "redirect: ../queryAll";
	}
	
	@RequestMapping(value="/changeState",method=RequestMethod.POST)
	public String changeState(HttpServletRequest req, HttpServletResponse resp) {
		String[] appinfos = req.getParameterValues("appinfo");
		for (String appId : appinfos) {
			if (appId !=null) {
				System.out.println(iAppItemService.selectByPrimaryKey(Integer.parseInt(appId)).getState());
				Appitem appitem = iAppItemService.selectByPrimaryKey(Integer.parseInt(appId));
				if (appitem.getState()) {
					appitem.setState(false);
				}else {
					appitem.setState(true);
				}
				updateByPrimaryKeyWithBLOBs(appitem);
			}
		}
		if (req.getParameter("p").equals("pending")) {
			return "redirect: queryPending";
		}else{
			return "redirect: queryAll";
		}
	}

	@RequestMapping(value="/changeStateById/{id}",method=RequestMethod.GET)
	public String changeStateById(@PathVariable("id") Integer appId,HttpServletRequest req, HttpServletResponse resp) {
		if (appId !=null) {
			Appitem appitem = iAppItemService.selectByPrimaryKey(appId);
			if (appitem.getState()) {
				appitem.setState(false);
			}else {
				appitem.setState(true);
			}
			updateByPrimaryKeyWithBLOBs(appitem);
		}
		System.out.println("P:" + req.getParameter("p"));
		if (req.getParameter("p").equals("pending")) {
			return "redirect: ../queryPending";
		}else{
			return "redirect: ../queryAll";
		}
		
	}	
	
	
	/**
	 * 关联apptypelist 与 apptype表找出APP的所有类别的名称
	 * @param appId
	 * @return 
	 */
	public String[] getTypeName(Integer appId) {
		List<Integer> typeIdList = iApptypelistService.getTypeListByAppId(appId);
		System.out.println(typeIdList);
		List<String> typeNameList = new ArrayList<String>();
		for (Integer typeId : typeIdList) {
			typeNameList.add(iTypeService.selectByPrimaryKey(typeId).getName());
		}
		String[] typeNameStr = new String[typeNameList.size()];
		typeNameList.toArray(typeNameStr);
		return typeNameStr;
	}
	
	/**
	 * 关联apptypelist 与 apptype,appitem表找出这个类别下的的所有app
	 * @param typeId
	 * @return
	 */
	public List<Appitem> getAppName(Integer typeId) {
		List<Integer> appIdList = iApptypelistService.getAppListByTypeId(typeId);
		List<Appitem> appitemList = new ArrayList<Appitem>();
		for (Integer appId : appIdList) {
			appitemList.add(iAppItemService.selectByPrimaryKey(appId));
		}
		return appitemList;
	}

}
