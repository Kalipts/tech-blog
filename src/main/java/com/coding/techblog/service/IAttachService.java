package com.coding.techblog.service;

import com.coding.techblog.modal.Vo.AttachVo;
import com.github.pagehelper.PageInfo;



public interface IAttachService {

    PageInfo<AttachVo> getAttachs(Integer page, Integer limit);


    void save(String fname, String fkey, String ftype, Integer author);

    AttachVo selectById(Integer id);

    void deleteById(Integer id);
}
