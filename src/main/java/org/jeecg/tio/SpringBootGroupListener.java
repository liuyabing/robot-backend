package org.jeecg.tio;

import org.tio.core.ChannelContext;
import org.tio.core.intf.GroupListener;

/**
 * 绑定/解绑群组后会回调的接口
 * @author Administrator
 *
 */
public class SpringBootGroupListener implements GroupListener{
	@Override
    public void onAfterBind(ChannelContext channelContext, String s) throws Exception {

    }

    @Override
    public void onAfterUnbind(ChannelContext channelContext, String s) throws Exception {

    }
}
