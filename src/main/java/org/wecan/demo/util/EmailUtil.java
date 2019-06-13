package org.wecan.demo.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmailUtil {

    public static String achieveCode() {
        //复杂随机验证码，网上的，暂时不用
        //由于数字1 和0 和字母 O,l 有时分不清，所有，没有字母1 、0
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        //将数组转换为集合
        List list = Arrays.asList(beforeShuffle);
        //打乱集合顺序
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            //将集合转化为字符串
            sb.append(list.get(i));
        }
        //截取字符串第4到8
        return sb.toString().substring(3, 8);
    }

    public static String achieveSimpleCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public static void sendAuthCodeEmail(String email, String authCode,String UtilEmail,String UtilPassword) {
        //不要把邮箱及密码嵌在代码里还上传到github
        try {
            SimpleEmail mail = new SimpleEmail();
            //发送邮件的服务器
            mail.setHostName("smtp.qq.com");
            //登录邮箱的密码，是开启SMTP的密码
            mail.setAuthentication(UtilEmail, UtilPassword);
            //发送邮件的邮箱和发件人
            mail.setFrom(UtilEmail, "邮箱验证机器人");
            //使用安全链接
            mail.setSSLOnConnect(true);
            //utf-8编码
            mail.setCharset("UTF-8");
            //接收的邮箱
            mail.addTo(email);
            //设置邮件的主题
            mail.setSubject("注册验证码");
            //设置邮件的内容
            mail.setMsg("尊敬的用户：您好！您的注册验证码为：" + authCode + "，有效期为一分钟，请尽快验证。");
            //发送
            mail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}

