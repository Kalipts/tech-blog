package com.coding.techblog.service;


import com.coding.techblog.modal.Vo.OptionVo;

import java.util.List;
import java.util.Map;


public interface IOptionService {

    void insertOption(OptionVo optionVo);

    void insertOption(String name, String value);

    List<OptionVo> getOptions();



    void saveOptions(Map<String, String> options);
}
