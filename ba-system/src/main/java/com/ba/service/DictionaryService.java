package com.ba.service;

import com.ba.base.BaseService;
import com.ba.model.system.Dictionary;
import com.ba.vo.OptionVO;

import java.util.List;

public interface DictionaryService extends BaseService<Dictionary> {

    List<OptionVO> optionList(String parentCode);

    List<Dictionary> checkNameSame(Dictionary dictionary);

    List<Dictionary> checkCodeSame(Dictionary dictionary);
}
