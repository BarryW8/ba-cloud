package com.smart.service;

import com.smart.base.BaseService;
import com.smart.model.user.Dictionary;
import com.smart.vo.DictionaryVO;
import com.smart.vo.OptionVO;

import java.util.List;

public interface DictionaryService extends BaseService<Dictionary> {

    List<OptionVO> optionListByParentCode(String parentCode);

    List<Dictionary> checkNameSame(Dictionary dictionary);

    List<Dictionary> checkCodeSame(Dictionary dictionary);
}
