package org.wecan.demo.service;

import org.springframework.stereotype.Service;
import org.wecan.demo.entity.MessageEntity;
import org.wecan.demo.entity.UserEntity;

import java.util.List;

@Service
public interface MessageBoardService {
    /**
     * 查找所有的留言
     * @return 留言的集合含（子内容，父节点）
     */
    List<MessageEntity> findAllMessages();

    /**
     * 插入一条留言
     * @param message 留言
     * @return 成功与否
     */
    boolean insertMessage(MessageEntity message);

    /**
     * 删除一条留言
     *
     * @param id,userId 留言id，用于确保用户本人的用户id
     * @return 成功与否
     */
    boolean deleteMessage(int id,int userId);

    /**
     * 修改一条留言
     *
     * @param message 留言（内含用于确保用户本人的用户id）
     * @return 成功与否
     */
    boolean updateMessage(MessageEntity message);

    /**
     * 搜索特定内容的留言
     *
     * @param content 留言内容
     * @return 留言的集合（含子内容，父节点）
     */
    List<MessageEntity> findNeededMessages(String content);

    /**
     * 插入用户
     *
     * @param user 用户
     * @return 成功与否
     */
    boolean insertUser(UserEntity user);

    /**
     * 登录
     *
     * @param eMail,password e-mail,密码
     * @return 成功返回User类
     */
    UserEntity checkLogin(String eMail,String password);

    /**
     * 发验证码邮件
     *
     * @param eMail e-mail
     * @return 验证码
     */
    String sendEmail(String eMail);
}
