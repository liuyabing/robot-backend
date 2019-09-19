package org.jeecg.modules.lelian.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.jeecg.modules.lelian.entity.RongFriendship;
import org.jeecg.modules.lelian.service.IRongFriendshipService;
import org.jeecg.modules.lelian.LelianConstants;
import org.jeecg.modules.lelian.entity.LelianRobot;
import org.jeecg.modules.lelian.entity.RongUser;
import org.jeecg.modules.lelian.service.ILelianRobotService;
import org.jeecg.modules.lelian.service.IRongUserService;

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
import io.rong.methods.user.User;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 融云好友表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="融云好友表")
@RestController
@RequestMapping("/lelian/rongFriendship")
public class RongFriendshipController {
	@Autowired
	private IRongFriendshipService rongFriendshipService;
	
	@Autowired
	private ILelianRobotService lelianRobotService;
	
	@Autowired
	private IRongUserService rongUserService;

	/**
	  * 分页列表查询
	 * @param rongFriendship
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "融云好友表-分页列表查询")
	@ApiOperation(value="融云好友表-分页列表查询", notes="融云好友表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<RongFriendship>> queryPageList(RongFriendship rongFriendship,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<RongFriendship>> result = new Result<IPage<RongFriendship>>();
		QueryWrapper<RongFriendship> queryWrapper = QueryGenerator.initQueryWrapper(rongFriendship, req.getParameterMap());
		Page<RongFriendship> page = new Page<RongFriendship>(pageNo, pageSize);
		IPage<RongFriendship> pageList = rongFriendshipService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param rongFriendship
	 * @return
	 */
	@AutoLog(value = "融云好友表-添加")
	@ApiOperation(value="融云好友表-添加", notes="融云好友表-添加")
	@PostMapping(value = "/add")
	public Result<RongFriendship> add(@RequestBody RongFriendship rongFriendship) {
		Result<RongFriendship> result = new Result<RongFriendship>();
		try {
			rongFriendshipService.save(rongFriendship);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param rongFriendship
	 * @return
	 */
	@AutoLog(value = "融云好友表-编辑")
	@ApiOperation(value="融云好友表-编辑", notes="融云好友表-编辑")
	@PutMapping(value = "/edit")
	public Result<RongFriendship> edit(@RequestBody RongFriendship rongFriendship) {
		Result<RongFriendship> result = new Result<RongFriendship>();
		RongFriendship rongFriendshipEntity = rongFriendshipService.getById(rongFriendship.getId());
		if(rongFriendshipEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = rongFriendshipService.updateById(rongFriendship);
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
	@AutoLog(value = "融云好友表-通过id删除")
	@ApiOperation(value="融云好友表-通过id删除", notes="融云好友表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			rongFriendshipService.removeById(id);
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
	@AutoLog(value = "融云好友表-批量删除")
	@ApiOperation(value="融云好友表-批量删除", notes="融云好友表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<RongFriendship> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<RongFriendship> result = new Result<RongFriendship>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.rongFriendshipService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云好友表-通过id查询")
	@ApiOperation(value="融云好友表-通过id查询", notes="融云好友表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<RongFriendship> queryById(@RequestParam(name="id",required=true) String id) {
		Result<RongFriendship> result = new Result<RongFriendship>();
		RongFriendship rongFriendship = rongFriendshipService.getById(id);
		if(rongFriendship==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(rongFriendship);
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
      QueryWrapper<RongFriendship> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              RongFriendship rongFriendship = JSON.parseObject(deString, RongFriendship.class);
              queryWrapper = QueryGenerator.initQueryWrapper(rongFriendship, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<RongFriendship> pageList = rongFriendshipService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "融云好友表列表");
      mv.addObject(NormalExcelConstants.CLASS, RongFriendship.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("融云好友表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<RongFriendship> listRongFriendships = ExcelImportUtil.importExcel(file.getInputStream(), RongFriendship.class, params);
              rongFriendshipService.saveBatch(listRongFriendships);
              return Result.ok("文件导入成功！数据行数:" + listRongFriendships.size());
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
	 * 通讯录
	 * 通过id查询所有申请加他的用户
	 * @param id
	 * @return
	 */
	@AutoLog(value = "融云好友表-通过用户id查询所有申请加他的用户")
	@ApiOperation(value="融云好友表-通过用户id查询所有申请加他的用户", notes="融云好友表-通过用户id查询所有申请加他的用户")
	@GetMapping(value = "/getAskFriends")
	public Result<IPage<RongFriendship>> getAskFriends(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, String userId) {
		Result<IPage<RongFriendship>> result = new Result<IPage<RongFriendship>>();
		// 组装查询条件
		QueryWrapper<RongFriendship> queryWrapper = new QueryWrapper<RongFriendship>();
		queryWrapper.eq("status", "11");//表示被请求
		queryWrapper.eq("friend_id", userId);
		//List<RongFriendship> list = rongFriendshipService.list(queryWrapper);
		Page<RongFriendship> page = new Page<RongFriendship>(pageNo, pageSize);
		IPage<RongFriendship> pageData = rongFriendshipService.page(page, queryWrapper);

		result.setSuccess(true);
		result.setResult(pageData);
		return result;
	}
	
	/**
	 * 
	 * 用户a申请加用户b   a -> b  10  and b -> a  11
	 *  
	 * 通过/拒绝/删除好友
	 */
	@AutoLog(value = "融云好友表-申请/通过/拒绝/删除好友")
	@ApiOperation(value="融云好友表-申请/通过/拒绝/删除好友", notes="code(10表示申请20表示同意21表示拒绝30表示删除)")
	@GetMapping(value = "/operateFriendship")
	public Result<?> operateFriendship(String userId, String friendId, String code) {
		if(code == null)
			return Result.error("请指定操作码");
		
		List<RongFriendship> entityList = new ArrayList<RongFriendship>();
		if(code.equals("10")) {//申请操作
			RongFriendship userShip = null;
			//要先判断这两个用户是否存在关系,如果存在就直接修改状态,否则就新增
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", friendId);
			userShip = rongFriendshipService.getOne(queryWrapper1);
			if(userShip == null) {
				userShip = new RongFriendship();
				userShip.setUserId(userId);
				userShip.setFriendId(friendId);
				userShip.setDisplayName(friendId);
				entityList.add(userShip);
			}
			userShip.setMessage("申请");
			userShip.setStatus(10);
			userShip.setCreateTime(new Date());

			RongFriendship friendShip = null;
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", friendId);
			queryWrapper2.eq("friend_id", userId);
			friendShip = rongFriendshipService.getOne(queryWrapper2);
			if(friendShip == null) {
				friendShip = new RongFriendship();
				friendShip.setUserId(friendId);
				friendShip.setFriendId(userId);
				friendShip.setDisplayName(userId);
				entityList.add(friendShip);
			}
			friendShip.setMessage("被申请");
			friendShip.setStatus(11);
			friendShip.setCreateTime(new Date());
			
			boolean askResult = false;
			if(entityList.size() == 0)
				askResult = rongFriendshipService.updateBatchById(entityList, entityList.size());
			else if(entityList.size() == 2)
				askResult = rongFriendshipService.saveBatch(entityList, entityList.size());
			if(askResult)
				return Result.ok("申请成功");
			else
				return Result.error("申请失败");
		}else if(code.equals("20")) {//同意操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", friendId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("申请通过");
			userShip.setStatus(20);   //更新11状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
			
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", friendId);
			queryWrapper2.eq("friend_id", userId);
			RongFriendship friendShip = rongFriendshipService.getOne(queryWrapper2);
			friendShip.setMessage("申请通过");
			friendShip.setStatus(20); //更新10状态这条
			friendShip.setUpdateTime(new Date());
			entityList.add(friendShip);
		}else if(code.equals("21")) {//忽略操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", friendId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("申请被拒绝");
			userShip.setStatus(21);   //更新11状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
		}else if(code.equals("30")) {//删除操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", friendId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("删除好友");
			userShip.setStatus(30);   //更新20状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
			
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", friendId);
			queryWrapper2.eq("friend_id", userId);
			RongFriendship friendShip = rongFriendshipService.getOne(queryWrapper2);
			friendShip.setMessage("删除好友");
			friendShip.setStatus(30); //更新20状态这条
			friendShip.setUpdateTime(new Date());
			entityList.add(friendShip);
		}else {
			return Result.error("无效操作码"+code);
		}

		boolean updateResult = rongFriendshipService.updateBatchById(entityList, entityList.size());
		if(updateResult)
			return Result.ok("操作成功");
		else
			return Result.error("操作失败");
	}
	
	/**
	 * 
	 * 用户a申请加机器人b   a -> b  10  and b -> a  11
	 *  
	 * 通过/拒绝/删除机器人
	 */
	@AutoLog(value = "融云好友表-申请/通过/拒绝/删除机器人")
	@ApiOperation(value="融云好友表-申请/通过/拒绝/删除机器人", notes="code(10表示申请20表示同意21表示拒绝30表示删除)")
	@GetMapping(value = "/robotFriendship")
	public Result<?> robotFriendship(String userId, String robotId, String robotName, String code) {
		if(code == null)
			return Result.error("请指定操作码");
		
		List<RongFriendship> entityList = new ArrayList<RongFriendship>();
		if(code.equals("10")) {//申请操作
			//先判断该机器人是否已经存在于机器人表
			QueryWrapper<LelianRobot> robotWrapper = new QueryWrapper<LelianRobot>();
			robotWrapper.eq("robotId", robotId);
			LelianRobot robot = lelianRobotService.getOne(robotWrapper);
			if(robot == null) {
				robot = new LelianRobot();
				robot.setRobotid(robotId);
				robot.setName(robotName);
				robot.setNotename(robotName);
				robot.setManager(userId);  //第一个加机器人的用户就是这个机器人的管理员
				robot.setAvatar(LelianConstants.AVATAR_ROBOT);
				robot.setQrcode("");//TODO 是否需要后台生成二维码
				robot.setProvider("");
				robot.setStatus(LelianConstants.ROBOT_STATUS_NORMAL);
				lelianRobotService.save(robot);
				//判断融云表里是否有该机器人
				QueryWrapper<RongUser> ronguserWrapper = new QueryWrapper<RongUser>();
				ronguserWrapper.eq("user_id", robotId);
				RongUser ronguser = rongUserService.getOne(ronguserWrapper);
				if(ronguser == null) {	
					//并需要注册融云
					RongCloud rongCloud = RongCloud.getInstance(LelianConstants.RONG_APPKEY, LelianConstants.RONG_APPSECRET);
					User ruser = rongCloud.user;
			        UserModel userModel = new UserModel()
			                .setId(robotId)
			                .setName(robotName)
			                .setPortrait(LelianConstants.AVATAR_ROBOT);
			        //注册用户，生成用户在融云的唯一身份标识 Token
			        TokenResult rongResult = null;
					try {
						rongResult = ruser.register(userModel);
					} catch (Exception e) {
						e.printStackTrace();
						return Result.error("注册融云失败");
					}
			        //写到融云用户表中
			        if(rongResult.getCode() == 200) {
			        	RongUser rongUser = new RongUser();
			        	rongUser.setUserId(userModel.getId());
			        	rongUser.setUserName(userModel.getName());
			        	rongUser.setUserType(LelianConstants.LELIAN_USER_TYPE_ROBOT);
			        	rongUser.setToken(rongResult.getToken());
			        	rongUser.setPortrait(userModel.getPortrait());
			        	rongUser.setCreateBy(userModel.getId());
			        	rongUser.setCreateTime(new Date());
			        	rongUserService.save(rongUser);
			        }
				}
			}
			
			RongFriendship userShip = null;
			//要先判断这两个用户是否存在关系,如果存在就直接修改状态,否则就新增
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", robotId);
			userShip = rongFriendshipService.getOne(queryWrapper1);
			if(userShip == null) {
				userShip = new RongFriendship();
				userShip.setUserId(userId);
				userShip.setFriendId(robotId);
				userShip.setDisplayName(robotName);
				entityList.add(userShip);
			}
			userShip.setMessage("申请");
			userShip.setStatus(10);
			userShip.setCreateTime(new Date());

			RongFriendship friendShip = null;
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", robotId);
			queryWrapper2.eq("friend_id", userId);
			friendShip = rongFriendshipService.getOne(queryWrapper2);
			if(friendShip == null) {
				friendShip = new RongFriendship();
				friendShip.setUserId(robotId);
				friendShip.setFriendId(userId);
				friendShip.setDisplayName(userId);
				entityList.add(friendShip);
			}
			friendShip.setMessage("被申请");
			friendShip.setStatus(11);
			friendShip.setCreateTime(new Date());
			
			boolean askResult = false;
			if(entityList.size() == 0)
				askResult = rongFriendshipService.updateBatchById(entityList, entityList.size());
			else if(entityList.size() == 2)
				askResult = rongFriendshipService.saveBatch(entityList, entityList.size());
			if(askResult)
				return Result.ok("申请成功");
			else
				return Result.error("申请失败");
		}else if(code.equals("20")) {//同意操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", robotId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("申请通过");
			userShip.setStatus(20);   //更新11状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
			
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", robotId);
			queryWrapper2.eq("friend_id", userId);
			RongFriendship friendShip = rongFriendshipService.getOne(queryWrapper2);
			friendShip.setMessage("申请通过");
			friendShip.setStatus(20); //更新10状态这条
			friendShip.setUpdateTime(new Date());
			entityList.add(friendShip);
		}else if(code.equals("21")) {//忽略操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", robotId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("申请被拒绝");
			userShip.setStatus(21);   //更新11状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
		}else if(code.equals("30")) {//删除操作
			QueryWrapper<RongFriendship> queryWrapper1 = new QueryWrapper<RongFriendship>();
			queryWrapper1.eq("user_id", userId);
			queryWrapper1.eq("friend_id", robotId);
			RongFriendship userShip = rongFriendshipService.getOne(queryWrapper1);
			userShip.setMessage("删除好友");
			userShip.setStatus(30);   //更新20状态这条
			userShip.setUpdateTime(new Date());
			entityList.add(userShip);
			
			QueryWrapper<RongFriendship> queryWrapper2 = new QueryWrapper<RongFriendship>();
			queryWrapper2.eq("user_id", robotId);
			queryWrapper2.eq("friend_id", userId);
			RongFriendship friendShip = rongFriendshipService.getOne(queryWrapper2);
			friendShip.setMessage("删除好友");
			friendShip.setStatus(30); //更新20状态这条
			friendShip.setUpdateTime(new Date());
			entityList.add(friendShip);
		}else {
			return Result.error("无效操作码"+code);
		}

		boolean updateResult = rongFriendshipService.updateBatchById(entityList, entityList.size());
		if(updateResult)
			return Result.ok("操作成功");
		else
			return Result.error("操作失败");
	}
	



}
