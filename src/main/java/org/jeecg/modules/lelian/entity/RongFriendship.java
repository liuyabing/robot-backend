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
 * @Description: 融云好友表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Data
@TableName("rong_friendship")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="rong_friendship对象", description="融云好友表")
public class RongFriendship {
    
	/**uuid*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "uuid")
	private java.lang.String id;
	/**当前用户id*/
	@Excel(name = "当前用户id", width = 15)
    @ApiModelProperty(value = "当前用户id")
	private java.lang.String userId;
	/**好友id*/
	@Excel(name = "好友id", width = 15)
    @ApiModelProperty(value = "好友id")
	private java.lang.String friendId;
	/**备注名*/
	@Excel(name = "备注名", width = 15)
    @ApiModelProperty(value = "备注名")
	private java.lang.String displayName;
	/**加好友时的“请求信息”*/
	@Excel(name = "加好友时的“请求信息”", width = 15)
    @ApiModelProperty(value = "加好友时的“请求信息”")
	private java.lang.String message;
	/**10: 请求, 11: 被请求, 20: 同意, 21: 忽略, 30: 被删除*/
	@Excel(name = "10: 请求, 11: 被请求, 20: 同意, 21: 忽略, 30: 被删除", width = 15)
    @ApiModelProperty(value = "10: 请求, 11: 被请求, 20: 同意, 21: 忽略, 30: 被删除")
	private java.lang.Integer status;
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
