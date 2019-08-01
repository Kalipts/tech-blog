package com.coding.techblog.service;



import com.coding.techblog.dto.MetaDto;
import com.coding.techblog.modal.Bo.ArchiveBo;
import com.coding.techblog.modal.Bo.BackResponseBo;
import com.coding.techblog.modal.Bo.StatisticsBo;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.ContentVo;

import java.util.List;


public interface ISiteService {


    List<CommentVo> recentComments(int limit);

    List<ContentVo> recentContents(int limit);


    CommentVo getComment(Integer coid);


    BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception;


    StatisticsBo getStatistics();

    List<ArchiveBo> getArchives();

    List<MetaDto> metas(String type, String orderBy, int limit);

}
