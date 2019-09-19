package org.jeecg.modules.lelian.service;

import java.util.List;

import org.jeecg.modules.lelian.entity.RongUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 融云用户表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
public interface IRongUserService extends IService<RongUser> {
	
	//public IPage<RongUser> getAllFriendsByUserId(Page<RongUser> page, String userId);
	
	public List<RongUser> getAllFriendsByUserId(String userId);
	

}
