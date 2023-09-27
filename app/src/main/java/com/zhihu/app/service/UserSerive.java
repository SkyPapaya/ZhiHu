package com.zhihu.app.service;

import com.zhihu.app.model.User;

public interface UserSerive {
    public User add(User user);
    public User get(String id);
    Page<User> list(UserQueryParam userQueryParam);
    boolean modify(User user);
    boolean delete(User user);

}
