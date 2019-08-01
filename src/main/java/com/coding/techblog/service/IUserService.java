package com.coding.techblog.service;


import com.coding.techblog.modal.Vo.UserVo;

public interface IUserService {



    Integer insertUser(UserVo userVo);

    UserVo queryUserById(Integer uid);

    UserVo login(String username, String password);

    void updateByUid(UserVo userVo);
}
