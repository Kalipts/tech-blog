package com.coding.techblog.service;

import com.coding.techblog.modal.Vo.ContentVo;
import com.coding.techblog.modal.Vo.ContentVoExample;
import com.github.pagehelper.PageInfo;



public interface IContentService {


    void publish(ContentVo contents);


    PageInfo<ContentVo> getContents(Integer p, Integer limit);


    ContentVo getContents(String id);

    void updateContentByCid(ContentVo contentVo);

    PageInfo<ContentVo> getArticles(Integer mid, int page, int limit);

    PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit);


    PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit);

    void deleteByCid(Integer cid);

    void updateArticle(ContentVo contents);

    void updateCategory(String ordinal, String newCatefory);
}
