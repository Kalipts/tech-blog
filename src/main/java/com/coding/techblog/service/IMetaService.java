package com.coding.techblog.service;


import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.modal.Vo.MetaVo;

import java.util.List;


public interface IMetaService {

    MetaDto getMeta(String type, String name);


    Integer countMeta(Integer mid);


    List<MetaVo> getMetas(String types);


    void saveMetas(Integer cid, String names, String type);


    void saveMeta(String type, String name, Integer mid);


    List<MetaDto> getMetaList(String type, String orderby, int limit);

    void delete(int mid);


    void saveMeta(MetaVo metas);


    void update(MetaVo metas);
}
