package com.coding.techblog.service;

import com.coding.techblog.modal.Vo.RelationshipVoKey;


import java.util.List;

public interface IRelationshipService {

    void deleteById(Integer cid, Integer mid);


    Long countById(Integer cid, Integer mid);


    void insertVo(RelationshipVoKey relationshipVoKey);

    List<RelationshipVoKey> getRelationshipById(Integer cid, Integer mid);
}
