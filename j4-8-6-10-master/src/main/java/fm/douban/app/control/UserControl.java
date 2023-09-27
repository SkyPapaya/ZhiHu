package fm.douban.app.control;

import fm.douban.model.User;
import fm.douban.model.UserLoginInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fm.douban.model.UserQueryParam;
import fm.douban.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "app")
public class UserControl {

  private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

  @Autowired
  private UserService userService;

  @PostConstruct
  public void init() {
    LOG.info("UserControl 启动啦");
    LOG.info("userService 注入啦");
  }

  @GetMapping(path = "/login")
  public String loginPage(Model model) {
    return "login";
  }

  @PostMapping(path = "/authenticate")
  @ResponseBody
  public Map login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,
                   HttpServletResponse response) {
    Map returnData = new HashMap();
    // 根据登录名查询用户
    User regedUser = getUserByLoginName(name);

    // 找不到此登录用户
    if (regedUser == null) {
      returnData.put("result", false);
      returnData.put("message", "userName not correct");
      return returnData;
    }

    if (regedUser.getPassword().equals(password)) {
      UserLoginInfo userLoginInfo = new UserLoginInfo();
      userLoginInfo.setUserId("123456789abcd");
      userLoginInfo.setUserName(name);
      // 取得 HttpSession 对象
      HttpSession session = request.getSession();
      // 写入登录信息
      session.setAttribute("userLoginInfo", userLoginInfo);
      returnData.put("result", true);
      returnData.put("message", "login successfule");
    } else {
      returnData.put("result", false);
      returnData.put("message", "userName or password not correct");
    }

    return returnData;
  }

  @GetMapping(path = "/sign")
  public String signPage(Model model) {
    return "sign";
  }


  @PostMapping(path = "/register")
  @ResponseBody
  public Map registerAction(@RequestParam String name, @RequestParam String password, @RequestParam String mobile,
                            HttpServletRequest request, HttpServletResponse response) {
    Map returnData = new HashMap<>();
    User userTemp = userService.get(name);
    if(userTemp != null){
      System.out.println("login name is already exist");
      returnData.put("sign in result" , "failed");
      return null;
    }else{
      UserQueryParam userQueryParam = new UserQueryParam();
      userQueryParam.setUserName(name);
      userQueryParam.setPassword(password);
      userQueryParam.setMobile(mobile);
      userService.add(userQueryParam);
      //Page<User> userResult =userService.list(userQueryParam);
      //List<User> songs = userResult.getContent();
      returnData.put("sign in result" , "successful");
    }
return returnData;
  }

  private User getUserByLoginName(String loginName) {
    return userService.get(loginName);

  }
}
