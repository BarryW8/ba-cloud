package com.smart.service.impl;

import cn.hutool.core.date.DatePattern;
import com.smart.base.Page;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.dto.SysRoleDTO;
import com.smart.dto.SysUserRoleDTO;
import com.smart.mapper.SysRoleMapper;
import com.smart.mapper.SysRoleMenuMapper;
import com.smart.mapper.SysUserRoleMapper;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRole;
import com.smart.model.user.SysRoleMenu;
import com.smart.model.user.SysUserRole;
import com.smart.service.SysRoleService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.vo.SysUserRoleVO;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Override
    public List<SysUserRoleVO> findRoleUser(Long roleId) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append(" and ur.role_id = ").append(roleId);
        return sysUserRoleMapper.findList(sqlBd.toString());
    }

    @Override
    public int saveRoleUser(SysUserRoleDTO dto) {
        List<SysUserRole> list = dto.getUserRoles();
        LoginUser currentUser = dto.getCurrentUser();
        String currentDate = dto.getCurrentDate();
        // 1. 删除旧数据
        sysUserRoleMapper.deleteByRoleId(dto.getRoleId());
        if (CollectionUtils.isEmpty(list)) {
            return 1;
        }
        // 2. 保存新数据
        for (SysUserRole userRole : list) {
            userRole.setId(uidGenerator.getUID());
            userRole.setCreateUserId(currentUser.getUserId());
            userRole.setCreateUserName(currentUser.getRealName());
            userRole.setCreateTime(currentDate);
        }
        return sysUserRoleMapper.saveList(list);
    }

    @Transactional
    @Override
    public int save(SysRole sysRole) {
        return sysRoleMapper.save(sysRole);
    }

    @Transactional
    @Override
    public int update(SysRole sysRole) {
        return sysRoleMapper.update(sysRole);
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
    public PageView<SysRole> findPage(int page, int pageSize, String sql, String params) {
        Page pg = new Page();
        pg.setPage((page - 1) * pageSize);
        pg.setPageSize(pageSize);
        pg.setSql(sql);
        int count = sysRoleMapper.count(sql);
        PageView<SysRole> pageModel = new PageView<>();
        if (count > 0) {
            List<SysRole> list = sysRoleMapper.findPage(pg);
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
    public int saveRole(SysRoleDTO dto) {
        LoginUser currentUser = dto.getCurrentUser();
        String currentDate = dto.getCurrentDate();
        //-----------a、保存角色
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto,role);
        int result;
        if (role.getId() == null) {
            role.setId(uidGenerator.getUID());
            role.setCreateUserId(currentUser.getUserId());
            role.setCreateUserName(currentUser.getRealName());
            role.setCreateTime(currentDate);
            result = sysRoleMapper.save(role);
        } else {
            //编辑
            role.setUpdateUserId(currentUser.getUserId());
            role.setUpdateUserName(currentUser.getRealName());
            role.setUpdateTime(currentDate);
            result = sysRoleMapper.update(role);
            if (result > 0) {
                // 修改成功，删除角色的所有菜单权限
                sysRoleMenuMapper.delByRoleId(role.getId());
            }
        }
        if (result > 0) {
            //-----------b、保存角色菜单权限
            // 角色id
            Long roleId = role.getId();
            // 菜单权限列表
            List<String> permList = dto.getPermList();
            // 1. 对前端返回集合进行处理
            if (!CollectionUtils.isEmpty(permList)) {
                List<SysRoleMenu> list = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                for (String perm : permList) {
                    // 判断是否存在（父节点id-字典code）
                    if (perm.contains("-")) {
                        String[] split = perm.split("-");
                        // 判断map中是否包含key
                        if (map.containsKey(split[0])) {
                            String value = "";
                            if(split.length > 1){
                                // 判断key的value是否为空
                                String v = map.get(split[0]);
                                if (StringUtils.isNotEmpty(v)){
                                    // 不为空，拼接
                                    value =  v + "," + split[1];
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
                // 2. 封装数据
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(Long.parseLong(entry.getKey()));
                    roleMenu.setId(uidGenerator.getUID());
                    roleMenu.setMenuAction(entry.getValue());
                    roleMenu.setCreateUserId(currentUser.getUserId());
                    roleMenu.setCreateUserName(currentUser.getRealName());
                    roleMenu.setCreateTime(currentDate);
                    list.add(roleMenu);
                }
                sysRoleMenuMapper.saveList(list);
            }
        }
        return result;
    }
}
