package com.coding.techblog.service;

import com.coding.techblog.modal.Bo.CommentBo;
import com.coding.techblog.modal.Vo.CommentVo;
import com.coding.techblog.modal.Vo.CommentVoExample;
import com.github.pagehelper.PageInfo;



public interface ICommentService {


    void insertComment(CommentVo commentVo);


    PageInfo<CommentBo> getComments(Integer cid, int page, int limit);

    PageInfo<CommentVo> getCommentsWithPage(CommentVoExample commentVoExample, int page, int limit);

    CommentVo getCommentById(Integer coid);

    void delete(Integer coid, Integer cid);

    void update(CommentVo comments);

}
