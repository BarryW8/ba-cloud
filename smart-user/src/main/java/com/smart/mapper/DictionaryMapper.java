package com.smart.mapper;

import com.smart.base.BaseMapper;
import com.smart.model.user.Dictionary;
import com.smart.vo.DictionaryVO;
import com.smart.vo.OptionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    List<OptionVO> optionListByParentCode(@Param("parentCode")String parentCode);

    /**
     * 批量删除
     * @param del
     * @return
     */
    int batchDeleteBySm(@Param("delUser")Long delUser,
    		@Param("delUserName")String delUserName,
    		@Param("delDate")String delDate,
    		@Param("ids")List<Long> ids);
}
