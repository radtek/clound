

package com.cloud.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cloud.admin.api.annotation.LogField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author ygnet
 * @since 2017-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends Model<SysMenu> {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@TableId(value = "menu_id", type = IdType.UUID)
	@LogField(title = "主键")
	private String menuId;
	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	@LogField(title = "菜单名称")
	private String name;
	/**
	 * 菜单权限标识
	 */
	@LogField(title = "权限标识")
	private String permission;
	/**
	 * 父菜单ID
	 */
	@NotNull(message = "菜单父ID不能为空")
	@LogField(title = "父级ID")
	private String parentId;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * VUE页面
	 */
	private String component;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	@NotNull(message = "菜单类型不能为空")
	@LogField(title = "类型",dictType = "menu_type")
	private String type;
	/**
	 * 路由缓冲
	 */
	private String keepAlive;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0--正常 1--删除
	 */
	@TableLogic
	private String delFlag;
	/**
	 * 前端URL
	 */
	@LogField(title = "前端URL")
	private String path;
	private String parentIds;


}
