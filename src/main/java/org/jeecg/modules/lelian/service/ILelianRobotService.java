package org.jeecg.modules.lelian.service;

import org.jeecg.modules.lelian.entity.LelianRobot;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 机器人表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
public interface ILelianRobotService extends IService<LelianRobot> {

	public void saveRobot(LelianRobot robot,  IService rongUserService) throws Exception;
}
