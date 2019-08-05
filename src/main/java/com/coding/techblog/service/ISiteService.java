package com.coding.techblog.service;



import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.modal.Bo.StatisticsBo;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.ContentVo;

import java.util.List;


public interface ISiteService {


    List<CommentVo> recentComments(int limit);

    List<ContentVo> recentContents(int limit);


    CommentVo getComment(Integer coid);


    StatisticsBo getStatistics();


    List<MetaDto> metas(String type, String orderBy, int limit);

}
