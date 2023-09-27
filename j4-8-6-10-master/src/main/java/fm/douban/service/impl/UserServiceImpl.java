package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.User;
import fm.douban.model.UserQueryParam;
import fm.douban.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
  @Autowired
  MongoTemplate mongoTemplate ;


  @Override
  public User add(User user) {
    User newUser = user;
    newUser.setId(user.getId());
    mongoTemplate.insert(newUser);
    return null;
  }

  @Override
  public User get(String name) {
    User user=mongoTemplate.findById(name , User.class);
    return user;
  }
  @Override
  public boolean modify(User user) {
//    User newUser = new User();
//    Query query = new Query(Criteria.where().is());
//    Update update = new Update("");
//    UpdateResult updateResult = mongoTemplate.updateFirst(query , update);
    return true;
  }

  @Override
  public boolean delete(User user) {
    User newUser = new User();
    newUser.setId(user.getId());
    DeleteResult deleteResult = mongoTemplate.remove(newUser);
    return true;
  }

  @Override
  public Page<User> list(UserQueryParam userQueryParam) {

    List<Criteria> criterias= new ArrayList<>();
    if (StringUtils.hasText(userQueryParam.getUserName())){
      criterias.add(Criteria.where("userName").is(userQueryParam.getUserName()));
    }

    if (StringUtils.hasText(userQueryParam.getMobile())){
      criterias.add(Criteria.where("mobile").is(userQueryParam.getMobile()));
    }

    if (StringUtils.hasText(userQueryParam.getPassword())){
      criterias.add(Criteria.where("password").is(userQueryParam.getPassword()));
    }

    if (criterias.isEmpty()){
      System.out.println("register failed");
      return null;
    }
    Criteria criteria = new Criteria();
    criteria.andOperator(criterias.toArray(new Criteria[]{}));
    Query query = new Query(criteria);
    List<User> users = mongoTemplate.find(query , User.class);
    long count = mongoTemplate.count(query , User.class);
    Pageable pageable = PageRequest.of(userQueryParam.getPageNum() - 1, userQueryParam.getPageSize());
    Page <User> page = PageableExecutionUtils.getPage(users, pageable, new LongSupplier() {
      @Override
      public long getAsLong() {
        return  count;
      }
    }) ;
    return page;
  }


}
