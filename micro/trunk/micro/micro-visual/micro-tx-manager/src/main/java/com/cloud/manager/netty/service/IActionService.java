

package com.cloud.manager.netty.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author LCN on 2017/11/11
 */
public interface IActionService {


	String execute(String channelAddress, String key, JSONObject params);

}
