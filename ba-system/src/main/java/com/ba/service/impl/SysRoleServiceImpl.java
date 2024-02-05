package com.ba.service.impl;

import com.ba.base.Page;
import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheManage;
import com.ba.dto.SysRoleDTO;
import com.ba.dto.SysUserRoleDTO;
import com.ba.exception.ServiceException;
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
import com.ba.util.StringUtils;
import com.ba.vo.SysMenuVO;
import com.ba.vo.SysUserRoleVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private CacheManage cacheManage;

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
        return sysRoleMenuMapper.findListBySQL(" and role_id = " + roleId);
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
    public List<SysRole> findList(Map<String, Object> map) {
        return sysRoleMapper.findList(map);
    }

    @Override
    public List<SysRole> findListBySQL(String condition) {
        return sysRoleMapper.findListBySQL(condition);
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
    public int add(SysRole model) {
        return sysRoleMapper.insert(model);
    }

    @Transactional
    @Override
    public int edit(SysRole model) {
        return sysRoleMapper.update(model);
    }

    public void refreshCache(SysRole role, List<SysMenuVO> permList) {
        if (CollectionUtils.isEmpty(permList)) {
            return;
        }
        // 1、删除角色菜单权限
        sysRoleMenuMapper.delByRoleId(role.getId());
        // 2、保存角色菜单权限
        List<SysRoleMenu> list = new ArrayList<>();
        // 处理权限数据
        List<String> perms = permList.stream().map(SysMenuVO::getTreeId).distinct().collect(Collectors.toList());
        Map<String, String> map = parseData(perms);
        map.forEach((k,v) -> {
            List<SysMenuVO> filter = permList.stream().filter(e -> k.equals(e.getId().toString())).collect(Collectors.toList());
            SysMenuVO vo = filter.get(0);
            // 封装参数
            SysRoleMenu menuRole = new SysRoleMenu();
            menuRole.setId(uidGenerator.getUID());
            menuRole.setRoleId(role.getId());
            menuRole.setMenuId(vo.getId());
            menuRole.setMenuCode(vo.getMenuCode());
            menuRole.setPermission(v);
            list.add(menuRole);
        });
        // 批量插入
        sysRoleMenuMapper.saveList(list);
        // 删除缓存
        cacheManage.delSysRoleMenuAll(role.getId().toString());
        // 新增缓存
        for (SysRoleMenu roleMenu : list) {
            cacheManage.setSysRoleMenu(role.getId(), roleMenu.getMenuCode(), Arrays.asList(roleMenu.getPermission().split(",")));
        }
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
