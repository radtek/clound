

package com.cloud.admin.api.dto;

import com.cloud.admin.api.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ygnet
 * @date 2017/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {
	/**
	 * 角色ID
	 */
	private Integer role;

	private String deptId;

	/**
	 * 新密码
	 */
	private String newpassword1;
}
