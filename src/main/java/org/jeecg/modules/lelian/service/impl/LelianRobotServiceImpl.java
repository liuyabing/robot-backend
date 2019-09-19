package org.jeecg.modules.lelian.service.impl;

import java.util.Date;

import org.jeecg.modules.lelian.LelianConstants;
import org.jeecg.modules.lelian.entity.LelianRobot;
import org.jeecg.modules.lelian.entity.RongUser;
import org.jeecg.modules.lelian.mapper.LelianRobotMapper;
import org.jeecg.modules.lelian.service.ILelianRobotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.rong.RongCloud;
import io.rong.methods.user.User;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

/**
 * @Description: 机器人表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Service
public class LelianRobotServiceImpl extends ServiceImpl<LelianRobotMapper, LelianRobot> implements ILelianRobotService {

	@Override
	@Transactional
	public void saveRobot(LelianRobot robot, IService rongUserService) throws Exception {
//		if(null == robot.getCreateBy() || robot.getCreateBy().equals(""))
//			robot.setCreateBy();
		super.save(robot);
		//注册融云
		RongCloud rongCloud = RongCloud.getInstance(LelianConstants.RONG_APPKEY, LelianConstants.RONG_APPSECRET);
		User ruser = rongCloud.user;
        UserModel userModel = new UserModel()
                .setId(robot.getRobotid())
                .setName(robot.getName())
                .setPortrait(robot.getAvatar());
        //注册用户，生成用户在融云的唯一身份标识 Token
        TokenResult rongResult = ruser.register(userModel);
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
