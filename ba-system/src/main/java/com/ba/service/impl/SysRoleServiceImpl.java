package com.ba.service.impl;

import com.ba.base.Page;
import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.dto.SysRoleDTO;
import com.ba.dto.SysUserRoleDTO;
import com.ba.mapper.SysRoleMapper;
import com.ba.mapper.SysRoleMenuMapper;
import com.ba.mapper.SysUserRoleMapper;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysRoleMenu;
import com.ba.model.system.SysUserRole;
import com.ba.service.SysRoleService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.util.CommonUtils;
import com.ba.vo.SysUserRoleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private CachedUidGenerator uidGenerator;

//    @Override
//    public List<SysUserRole> findRoleUser(Long roleId) {
//        return sysUserRoleMapper.findList(" and role_id = " + roleId);
//    }
//
//    @Override
//    public int saveRoleUser(SysUserRoleDTO dto) {
//        List<SysUserRole> list = dto.getUserRoles();
//        UserInfo currentUser = dto.getCurrentUser();
//        String currentDate = dto.getCurrentDate();
//        // 1. 删除旧数据
//        sysUserRoleMapper.deleteByRoleId(dto.getRoleId());
//        if (CollectionUtils.isEmpty(list)) {
//            return 1;
//        }
//        // 2. 保存新数据
//        for (SysUserRole userRole : list) {
//            userRole.setId(uidGenerator.getUID());
//            userRole.setCreateBy(currentUser.getId());
//            userRole.setCreateTime(currentDate);
//        }
//        return sysUserRoleMapper.saveList(list);
//    }

    @Override
    public List<SysRoleMenu> findRoleMenu(Long roleId) {
        return sysRoleMenuMapper.findList(" and role_id = " + roleId);
    }

    @Transactional
    @Override
    public int insert(SysRole model) {
        return sysRoleMapper.insert(model);
    }

    @Transactional
    @Override
    public int update(SysRole model) {
        return sysRoleMapper.update(model);
    }

    @Override
    public SysRole findById(Long modelId) {
        return sysRoleMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel sm) {
        return sysRoleMapper.deleteBySm(sm);
    }

    @Override
    public List<SysRole> findList(String condition) {
        return sysRoleMapper.findList(condition);
    }

    @Override
    public PageView<SysRole> findPage(Map<String, Object> map) {
        int count = sysRoleMapper.count(map);
        PageView<SysRole> pageModel = new PageView<>();
        if (count > 0) {
            List<SysRole> list = sysRoleMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysRole> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Transactional
    @Override
    public int saveDTO(SysRoleDTO dto) {
        Long currentUserId = UserContext.getUserId();
        String currentDateStr = CommonUtils.getCurrentDate();

        // 角色ID
        Long id = dto.getId();
        // 菜单权限列表
        List<String> permList = dto.getPermList();

        // 1. 保存角色
        SysRole role = BeanUtils.convertTo(dto, SysRole::new);
        int result;
        if (role.getId() == null) {
            role.setId(uidGenerator.getUID());
            role.setCreateBy(currentUserId);
            role.setCreateTime(currentDateStr);
            result = sysRoleMapper.insert(role);
        } else {
            //编辑
            role.setUpdateBy(currentUserId);
            role.setUpdateTime(currentDateStr);
            result = sysRoleMapper.update(role);
        }
        if (result > 0) {
            // 2. 修改成功，删除角色菜单权限
            sysRoleMenuMapper.delByRoleId(role.getId());
        }

        // 3. 保存角色菜单权限
        if (!CollectionUtils.isEmpty(permList)) {
            List<SysRoleMenu> list = new ArrayList<>();
            // 处理权限数据
            Map<String, String> map = parseData(permList);
            // 封装参数
            map.forEach((k,v) -> {
                SysRoleMenu menuRole = new SysRoleMenu();
                menuRole.setId(uidGenerator.getUID());
                menuRole.setRoleId(id);
                menuRole.setMenuId(Long.parseLong(k));
                menuRole.setPermission(v);
                menuRole.setCreateBy(currentUserId);
                menuRole.setCreateTime(currentDateStr);
                list.add(menuRole);
            });
            // 批量插入
            sysRoleMenuMapper.saveList(list);
        }

        return result;
    }

    private Map<String, String> parseData(List<String> permList) {
        Map<String, String> map = new HashMap<>();
        for (String perm : permList) {
            // 判断是否存在（父节点id-字典code）
            if (perm.contains("-")) {
                String[] split = perm.split("-");
                // 判断map中是否包含key
                if (map.containsKey(split[0])) {
                    String value = "";
                    if (split.length > 1) {
                        // 判断key的value是否为空
                        String v = map.get(split[0]);
                        if (StringUtils.isNotEmpty(v)) {
                            // 不为空，拼接
                            value = v + "," + split[1];
                        } else {
                            // 为空，直接存
                            value = split[1];
                        }
                    }
                    map.put(split[0], value);
                } else {
                    // 直接插入map
                    map.put(split[0], split[1]);
                }
            } else {
                // 直接插入map
                map.put(perm, "");
            }
        }
        return map;
    }
}
