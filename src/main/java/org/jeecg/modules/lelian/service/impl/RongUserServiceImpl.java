package org.jeecg.modules.lelian.service.impl;

import java.util.List;

import org.jeecg.modules.lelian.entity.RongUser;
import org.jeecg.modules.lelian.mapper.RongUserMapper;
import org.jeecg.modules.lelian.service.IRongUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 融云用户表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
@Service
public class RongUserServiceImpl extends ServiceImpl<RongUserMapper, RongUser> implements IRongUserService {
	@Autowired
	private RongUserMapper rongUserMapper;
	
//	public IPage<RongUser> getAllFriendsByUserId(Page<RongUser> page, String userId){
//		return rongUserMapper.queryAllFriendsByUserId(page, userId);
//	}
	
	public List<RongUser> getAllFriendsByUserId(String userId){
		return rongUserMapper.queryAllFriendsByUserId(userId);
	}
	

}
