package com.ba.mapper;

import com.ba.base.BaseMapper;
import com.ba.model.system.Dictionary;
import com.ba.vo.OptionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    List<OptionVO> optionList(String sql);

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
