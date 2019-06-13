package org.wecan.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wecan.demo.entity.MessageEntity;
import org.wecan.demo.entity.UserEntity;
import org.wecan.demo.service.MessageBoardService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"user"})
@Api
public class MainController {
    @Autowired
    MessageBoardService messageBoardService;

    @ApiOperation(value="获取注册页面", notes="获取注册页面",produces = "text/html")
    @GetMapping("/view/register")
    public String view() {
        return "register";
    }

    @ApiOperation(value="获取登录页面", notes="获取登录页面",produces = "text/html")
    @GetMapping("/view/login")
    public String view2() {
        return "login";
    }

    @ApiOperation(value="获取主页面", notes="获取主页面",produces = "text/html")
    @GetMapping("/view/main")
    public String view3() {
        return "main";
    }

    @ApiOperation(value="注册", notes="注册",produces = "application/json")
    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam("username") final String username,
                                        @RequestParam("eMail") final String eMail,
                                        @RequestParam("password") final String password,
                                        @RequestParam("code") final String code,
                                        HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        //无特殊符号,1-35位数字字母汉字均可
        String regex1 = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,35}$";
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        //6-35位数字混字母
        String regex3 = "^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{6,35}$";
        if (username.matches(regex1) && eMail.matches(regex2) && password.matches(regex3)) {
            if (httpSession.getAttribute("mCode") != null) {
                String mCode = httpSession.getAttribute("mCode").toString();
                if (mCode.equals(code)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    UserEntity user = new UserEntity(username, eMail, password, sdf.format(new Date()));
                    if (messageBoardService.insertUser(user)) {
                        res.put("status", 200);
                        res.put("data", "注册成功");
                    } else {
                        //邮箱查重其实应单独做一个接口
                        res.put("status", 400);
                        res.put("data", "注册失败，邮箱已被注册");
                    }
                } else {
                    res.put("status", 800);
                    res.put("data", "注册失败，验证码有误");
                }
            } else {
                res.put("status", 1000);
                res.put("data", "注册失败，未获取验证码");
            }
        } else {
            res.put("status", 600);
            res.put("data", "注册失败，注册信息格式有误");
        }
        return res;
    }

    @ApiOperation(value="登录", notes="登录",produces = "application/json")
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam("eMail") final String eMail,
                                     @RequestParam("password") final String password,
                                     Model model,
                                     HttpServletRequest httpServletRequest) {
        Map<String, Object> res = new HashMap<>();
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        //6-35位数字混字母
        String regex3 = "^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{6,35}$";
        if (eMail.matches(regex2) && password.matches(regex3)) {
            UserEntity user = null;
            user = messageBoardService.checkLogin(eMail, password);
            if (user != null) {
                res.put("status", 200);
                res.put("data", "登录成功");
                model.addAttribute("user", user);
                httpServletRequest.getSession();
            } else {
                res.put("status", 400);
                res.put("data", "登录失败，邮箱或密码错误");
            }
        } else {
            res.put("status", 600);
            res.put("data", "登录失败，邮箱或密码格式有误");
        }
        return res;
    }

    @ApiOperation(value="获取所有留言", notes="获取所有留言",produces = "application/json")
    @GetMapping("/info")
    @ResponseBody
    public List info() {
        List<MessageEntity> messageList = messageBoardService.findAllMessages();
        return messageList;
    }

    @ApiOperation(value="添加一条留言", notes="添加一条留言",produces = "application/json")
    @PostMapping("/user/insert")
    @ResponseBody
    public Map<String, Object> insert(@RequestParam("text") final String text,
                                      @RequestParam("pid") final int pid,
                                      @ModelAttribute("user") final UserEntity user) {
        Map<String, Object> res = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String username = user.getUsername();
        int userId = user.getId();
        MessageEntity message = new MessageEntity(username, text, pid, sdf.format(new Date()), userId);
        if (messageBoardService.insertMessage(message)) {
            res.put("status", 200);
            res.put("data", "发送成功");
        } else {
            res.put("status", 400);
            res.put("data", "发送失败");
        }
        return res;
    }

    @ApiOperation(value="删除一条留言", notes="删除一条留言",produces = "application/json")
    @PostMapping("/user/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("mid") final int mid,
                                      @ModelAttribute("user") final UserEntity user) {
        //@ModelAttribute("user")这个注解真坑，参数中若存在id，user实例里的id注入会失败
        //还是原生session好用
        int userId = user.getId();
        Map<String, Object> res = new HashMap<>();
        if (messageBoardService.deleteMessage(mid, userId)) {
            res.put("status", 200);
            res.put("data", "删除成功");
        } else {
            res.put("status", 400);
            res.put("data", "删除失败");
        }
        return res;
    }


    @ApiOperation(value="更新一条留言", notes="更新一条留言",produces = "application/json")
    @PostMapping("/user/update")
    @ResponseBody
    public Map<String, Object> update(@RequestParam("text") final String text,
                                      @RequestParam("mid") final int mid,
                                      @ModelAttribute("user") final UserEntity user) {
        Map<String, Object> res = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int userId = user.getId();
        MessageEntity message = new MessageEntity(text, mid, sdf.format(new Date()), userId);
        if (messageBoardService.updateMessage(message)) {
            res.put("status", 200);
            res.put("data", "更新成功");
        } else {
            res.put("status", 400);
            res.put("data", "更新失败");
        }
        return res;
    }

    @ApiOperation(value="模糊搜索指定留言", notes="模糊搜索指定留言",produces = "application/json")
    @GetMapping("/search")
    @ResponseBody
    public List search(@RequestParam("content") final String content) {
        List<MessageEntity> messageList = messageBoardService.findNeededMessages(content);
        return messageList;
    }

    @ApiOperation(value="发送验证码", notes="发送验证码",produces = "application/json")
    @PostMapping("/email")
    @ResponseBody
    public Map<String, Object> email(@RequestParam("eMail") final String eMail,
                                     Model model,
                                     HttpServletRequest httpServletRequest) {
        //邮箱格式xxxxx@xxxxx.xxx
        String regex2 = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Map<String, Object> res = new HashMap<>();
        if (eMail.matches(regex2)) {
            try {
                HttpSession httpSession=httpServletRequest.getSession();
                httpSession.setAttribute("mCode", messageBoardService.sendEmail(eMail));
                httpSession.setMaxInactiveInterval(60);
                res.put("status", 200);
                res.put("data", "验证码发送成功，请注意查看邮箱，有效期一分钟");
                return res;
            } catch (Exception e) {
                res.put("status", 400);
                res.put("data", "验证码发送失败");
                return res;
            }
        } else {
            res.put("status", 600);
            res.put("data", "验证码发送失败，邮箱格式有误");
            return res;
        }
    }
}
