package org.jeecg.modules.lelian.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.lelian.LelianConstants;
import org.jeecg.modules.lelian.entity.RongUser;
import org.jeecg.modules.lelian.service.IRongUserService;
import org.jeecg.modules.system.entity.SysUser;

import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

import io.rong.RongCloud;
import io.rong.methods.user.onlinestatus.OnlineStatus;
import io.rong.models.response.CheckOnlineResult;
import io.rong.models.user.UserModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 融云用户表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="融云用户表")
@RestController
@RequestMapping("/lelian/rongUser")
public class RongUserController {
	@Autowired
	private IRongUserService rongUserService;
	
	/**
	  * 分页列表查询
	 * @param rongUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "融云用户表-分页列表查询")
	@ApiOperation(value="融云用户表-分页列表查询", notes="融云用户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<RongUser>> queryPageList(RongUser rongUser,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<RongUser>> result = new Result<IPage<RongUser>>();
		QueryWrapper<RongUser> queryWrapper = QueryGenerator.initQueryWrapper(rongUser, req.getParameterMap());
		Page<RongUser> page = new Page<RongUser>(pageNo, pageSize);
		IPage<RongUser> pageList = rongUserService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param rongUser
	 * @return
	 */
	@AutoLog(value = "融云用户表-添加")
	@ApiOperation(value="融云用户表-添加", notes="融云用户表-添加")
	@PostMapping(value = "/add")
	public Result<RongUser> add(@RequestBody RongUser rongUser) {
		Result<RongUser> result = new Result<RongUser>();
		try {
			rongUserService.save(rongUser);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param rongUser
	 * @return
	 */
	@AutoLog(value = "融云用户表-编辑")
	@ApiOperation(value="融云用户表-编辑", notes="融云用户表-编辑")
	@PutMapping(value = "/edit")
	public Result<RongUser> edit(@RequestBody RongUser rongUser) {
		Result<RongUser> result = new Result<RongUser>();
		RongUser rongUserEntity = rongUserService.getById(rongUser.getId());
		if(rongUserEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = rongUserService.updateById(rongUser);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云用户表-通过id删除")
	@ApiOperation(value="融云用户表-通过id删除", notes="融云用户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			rongUserService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "融云用户表-批量删除")
	@ApiOperation(value="融云用户表-批量删除", notes="融云用户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<RongUser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<RongUser> result = new Result<RongUser>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.rongUserService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云用户表-通过id查询")
	@ApiOperation(value="融云用户表-通过id查询", notes="融云用户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<RongUser> queryById(@RequestParam(name="id",required=true) String id) {
		Result<RongUser> result = new Result<RongUser>();
		RongUser rongUser = rongUserService.getById(id);
		if(rongUser==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(rongUser);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<RongUser> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              RongUser rongUser = JSON.parseObject(deString, RongUser.class);
              queryWrapper = QueryGenerator.initQueryWrapper(rongUser, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<RongUser> pageList = rongUserService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "融云用户表列表");
      mv.addObject(NormalExcelConstants.CLASS, RongUser.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("融云用户表列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<RongUser> listRongUsers = ExcelImportUtil.importExcel(file.getInputStream(), RongUser.class, params);
              rongUserService.saveBatch(listRongUsers);
              return Result.ok("文件导入成功！数据行数:" + listRongUsers.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }
  
  
  //================================== 自定义业务逻辑 ====================================//
  
	/**
	 * 通过id查询查询在线状态
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云用户表-通过id查询该用户在线状态")
	@ApiOperation(value="融云用户表-通过id查询该用户在线状态", notes="融云用户表-通过id查询该用户在线状态")
	@GetMapping(value = "/isOnline")
	public Result<CheckOnlineResult> isOnline(String id, String name, String avatar) {
		Result<CheckOnlineResult> result = new Result<CheckOnlineResult>();
		RongCloud rongCloud = RongCloud.getInstance(LelianConstants.RONG_APPKEY, LelianConstants.RONG_APPSECRET);
		OnlineStatus onlineStatus = new OnlineStatus(LelianConstants.RONG_APPKEY, LelianConstants.RONG_APPSECRET, rongCloud);
		UserModel userModel = new UserModel(id, name, avatar);
		CheckOnlineResult onlineResult;
		try {
			onlineResult = onlineStatus.check(userModel);
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("查询用户:"+id+" 在线状态失败.");
			result.setSuccess(false);
			return result;
		}
		
		result.setResult(onlineResult);
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * 通讯录
	 * 通过id查询所有好友
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云用户表-通过用户id查询所有好友")
	@ApiOperation(value="融云用户表-通过用户id查询所有好友", notes="融云用户表-通过用户id查询所有好友")
	@GetMapping(value = "/getAllFriends")
	public Result<List<RongUser>> getAllFriends(String userId) {
		Result<List<RongUser>> result = new Result<List<RongUser>>();
		List<RongUser> rongUserList = rongUserService.getAllFriendsByUserId(userId);
        result.setSuccess(true);
        result.setResult(rongUserList);
        return result;
	}
	
	/* 这个接口应该不能用分页
	@AutoLog(value = "融云用户表-通过用户id查询所有好友")
	@ApiOperation(value="融云用户表-通过用户id查询所有好友", notes="融云用户表-通过用户id查询所有好友")
	@GetMapping(value = "/getAllFriends")
	public Result<IPage<RongUser>> getAllFriends(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String userId) {
		Result<IPage<RongUser>> result = new Result<IPage<RongUser>>();
		Page<RongUser> page = new Page<RongUser>(pageNo, pageSize);
		IPage<RongUser> pageList = rongUserService.getAllFriendsByUserId(page, userId);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
	}
	*/
	
	


}
