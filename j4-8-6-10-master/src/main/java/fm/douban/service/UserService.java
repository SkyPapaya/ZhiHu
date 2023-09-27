package fm.douban.service;

import fm.douban.model.User;
import fm.douban.model.UserQueryParam;
import org.springframework.data.domain.Page;

public interface UserService {
    public User add(User user);
    public User get(String id);
    Page<User> list(UserQueryParam userQueryParam);
    boolean modify(User user);
    boolean delete(User user);



}
