package org.jeecg.modules.lelian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.lelian.entity.RongUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 融云用户表
 * @Author: jeecg-boot
 * @Date:   2019-09-17
 * @Version: V1.0
 */
public interface RongUserMapper extends BaseMapper<RongUser> {
	
	//IPage<RongUser> queryAllFriendsByUserId(Page page, @Param("userId") String userId);
	
	List<RongUser> queryAllFriendsByUserId(@Param("userId") String userId);
	

}
