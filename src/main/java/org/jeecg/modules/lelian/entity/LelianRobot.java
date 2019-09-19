package org.jeecg.modules.lelian.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 机器人表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Data
@TableName("lelian_robot")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="lelian_robot对象", description="机器人表")
public class LelianRobot {
    
	/**ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**机器人ID*/
	@Excel(name = "机器人ID", width = 15)
    @ApiModelProperty(value = "机器人ID")
	private java.lang.String robotid;
	/**机器人名称*/
	@Excel(name = "机器人名称", width = 15)
    @ApiModelProperty(value = "机器人名称")
	private java.lang.String name;
	/**备注名*/
	@Excel(name = "备注名", width = 15)
    @ApiModelProperty(value = "备注名")
	private java.lang.String notename;
	/**管理员*/
	@Excel(name = "管理员", width = 15)
    @ApiModelProperty(value = "管理员")
	private java.lang.String manager;
	/**头像地址*/
	@Excel(name = "头像地址", width = 15)
    @ApiModelProperty(value = "头像地址")
	private java.lang.String avatar;
	/**二维码地址*/
	@Excel(name = "二维码地址", width = 15)
    @ApiModelProperty(value = "二维码地址")
	private java.lang.String qrcode;
	/**商家*/
	@Excel(name = "商家", width = 15)
    @ApiModelProperty(value = "商家")
	private java.lang.String provider;
	/**状态(1正常2占线)*/
	@Excel(name = "状态(1正常2占线)", width = 15)
    @ApiModelProperty(value = "状态(1正常2占线)")
	private java.lang.String status;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
