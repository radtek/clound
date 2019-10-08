

package com.cloud.manager.manager.service.impl;

import com.cloud.manager.config.ConfigReader;
import com.cloud.manager.manager.service.LoadBalanceService;
import com.cloud.manager.model.LoadBalanceInfo;
import com.cloud.manager.redis.RedisServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LCN on 2017/12/5
 */
@Service
public class LoadBalanceServiceImpl implements LoadBalanceService {

	@Autowired
	private RedisServerService redisServerService;

	@Autowired
	private ConfigReader configReader;

	@Override
	public boolean put(LoadBalanceInfo loadBalanceInfo) {
		String groupName = getLoadBalanceGroupName(loadBalanceInfo.getGroupId());
		redisServerService.saveLoadBalance(groupName, loadBalanceInfo.getKey(), loadBalanceInfo.getData());
		return true;
	}

	@Override
	public LoadBalanceInfo get(String groupId, String key) {
		String groupName = getLoadBalanceGroupName(groupId);
		String bytes = redisServerService.getLoadBalance(groupName, key);
		if (bytes == null) {
			return null;
		}

		LoadBalanceInfo loadBalanceInfo = new LoadBalanceInfo();
		loadBalanceInfo.setGroupId(groupId);
		loadBalanceInfo.setKey(key);
		loadBalanceInfo.setData(bytes);
		return loadBalanceInfo;
	}

	@Override
	public boolean remove(String groupId) {
		redisServerService.deleteKey(getLoadBalanceGroupName(groupId));
		return true;
	}

	@Override
	public String getLoadBalanceGroupName(String groupId) {
		return configReader.getKeyPrefixLoadbalance() + groupId;
	}
}
