package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.mapper.SysMenuMapper;
import com.ba.model.system.SysMenu;
import com.ba.response.ResData;
import com.ba.service.SysMenuService;
import com.ba.vo.SysMenuVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insert(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(SysMenu sysMenu) {
        return sysMenuMapper.update(sysMenu);
    }

    @Override
    public SysMenu findById(Long modelId) {
        return sysMenuMapper.findById(modelId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysMenuMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysMenu> findList(Map<String, Object> map) {
        return sysMenuMapper.findList(map);
    }

    @Override
    public List<SysMenu> findListBySQL(String sql) {
        return sysMenuMapper.findListBySQL(sql);
    }

    @Override
    public PageView<SysMenu> findPage(Map<String, Object> map) {
        return null;
    }

    @Override
    public Map<String, Object> findTree(Map<String, Object> queryMap) {
        if (MapUtils.isEmpty(queryMap)) {
            queryMap = new HashMap<>();
        }
        // 查询一级菜单
        queryMap.put("menuType", "M");
        List<SysMenu> parentMenus = sysMenuMapper.findList(queryMap);
        if (CollectionUtils.isEmpty(parentMenus)) {
            return null;
        }
        // 查询二级菜单
        queryMap.put("menuType", "C");
        List<SysMenu> subMenus = sysMenuMapper.findList(queryMap);
        // 封装数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("levelOneList", parentMenus);
        resultMap.put("levelTwoList", subMenus);
        return resultMap;
    }
}
