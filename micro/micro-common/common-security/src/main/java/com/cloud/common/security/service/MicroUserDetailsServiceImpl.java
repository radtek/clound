

package com.cloud.common.security.service;

import cn.hutool.core.util.StrUtil;
import com.cloud.admin.api.dto.UserInfo;
import com.cloud.admin.api.entity.SysUser;
import com.cloud.admin.api.feign.RemoteUserService;
import com.cloud.common.core.constant.CommonConstants;
import com.cloud.common.core.constant.SecurityConstants;
import com.cloud.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详细信息
 *
 * @author ygnet
 */
@Slf4j
@Service
@AllArgsConstructor
public class MicroUserDetailsServiceImpl implements MicroUserDetailsService {
    private final RemoteUserService remoteUserService;
    private final CacheManager cacheManager;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache("user_details");
        if (cache != null && cache.get(username) != null) {
            return (MicroUser) cache.get(username).get();
        }

        R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result);
        cache.put(username, userDetails);
        return userDetails;
    }


    /**
     * 根据社交登录code 登录
     *
     * @param inStr TYPE@CODE
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserBySocial(String inStr) {
        return getUserDetails(remoteUserService.social(inStr, SecurityConstants.FROM_IN));
    }

    @Override
    public UserDetails loadUserByIdCard(String idCard) throws UsernameNotFoundException {
        return getUserDetails(remoteUserService.pkiUser(idCard, SecurityConstants.FROM_IN));
    }

    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return
     */
    private UserDetails getUserDetails(R<UserInfo> result) {
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserInfo info = result.getData();
        Set<String> dbAuthsSet = new HashSet<>();
        if (null != info.getRoles()) {
            // 获取角色
            dbAuthsSet.add(SecurityConstants.ROLE + info.getRoles());
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();
        boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
        // 构造security用户

        return new MicroUser(user.getUserId(), user.getDeptId(), user.getUsername(), info.getSysDept(), info.getSysRole(), SecurityConstants.BCRYPT + user.getPassword(), enabled,
                true, true, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
    }
}
