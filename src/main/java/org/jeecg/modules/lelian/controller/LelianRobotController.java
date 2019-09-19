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
 * @Description: 机器人表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="机器人表")
@RestController
@RequestMapping("/lelian/lelianRobot")
public class LelianRobotController {
	@Autowired
	private ILelianRobotService lelianRobotService;
	
	@Autowired
	private IRongUserService rongUserService;

	
	/**
	  * 分页列表查询
	 * @param lelianRobot
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "机器人表-分页列表查询")
	@ApiOperation("机器人表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LelianRobot>> queryPageList(LelianRobot lelianRobot,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<LelianRobot>> result = new Result<IPage<LelianRobot>>();
		QueryWrapper<LelianRobot> queryWrapper = QueryGenerator.initQueryWrapper(lelianRobot, req.getParameterMap());
		Page<LelianRobot> page = new Page<LelianRobot>(pageNo, pageSize);
		IPage<LelianRobot> pageList = lelianRobotService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param lelianRobot
	 * @return
	 */
	@AutoLog(value = "机器人表-添加")
	@ApiOperation("机器人表-添加")
	@PostMapping(value = "/add")
	public Result<LelianRobot> add(@RequestBody LelianRobot lelianRobot) {
		Result<LelianRobot> result = new Result<LelianRobot>();
		try {
			lelianRobotService.saveRobot(lelianRobot, rongUserService);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	
	/**
	  *  编辑
	 * @param lelianRobot
	 * @return
	 */
	@AutoLog(value = "机器人表-编辑")
	@ApiOperation("机器人表-编辑")
	@PutMapping(value = "/edit")
	public Result<LelianRobot> edit(@RequestBody LelianRobot lelianRobot) {
		Result<LelianRobot> result = new Result<LelianRobot>();
		LelianRobot lelianRobotEntity = lelianRobotService.getById(lelianRobot.getId());
		if(lelianRobotEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = lelianRobotService.updateById(lelianRobot);
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
	@AutoLog(value = "机器人表-通过id删除")
	@ApiOperation("机器人表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			lelianRobotService.removeById(id);
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
	@AutoLog(value = "机器人表-批量删除")
	@ApiOperation("机器人表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<LelianRobot> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<LelianRobot> result = new Result<LelianRobot>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.lelianRobotService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "机器人表-通过id查询")
	@ApiOperation("机器人表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<LelianRobot> queryById(@RequestParam(name="id",required=true) String id) {
		Result<LelianRobot> result = new Result<LelianRobot>();
		LelianRobot lelianRobot = lelianRobotService.getById(id);
		if(lelianRobot==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(lelianRobot);
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
      QueryWrapper<LelianRobot> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              LelianRobot lelianRobot = JSON.parseObject(deString, LelianRobot.class);
              queryWrapper = QueryGenerator.initQueryWrapper(lelianRobot, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<LelianRobot> pageList = lelianRobotService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "机器人表列表");
      mv.addObject(NormalExcelConstants.CLASS, LelianRobot.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("机器人表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<LelianRobot> listLelianRobots = ExcelImportUtil.importExcel(file.getInputStream(), LelianRobot.class, params);
              lelianRobotService.saveBatch(listLelianRobots);
              return Result.ok("文件导入成功！数据行数:" + listLelianRobots.size());
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

}
