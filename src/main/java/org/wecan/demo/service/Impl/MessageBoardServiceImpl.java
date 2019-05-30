package org.wecan.demo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wecan.demo.entity.EmailUtilEntity;
import org.wecan.demo.entity.MessageEntity;
import org.wecan.demo.entity.UserEntity;
import org.wecan.demo.mapper.EmailUtilMapper;
import org.wecan.demo.mapper.MessageBoardMapper;
import org.wecan.demo.service.MessageBoardService;
import org.wecan.demo.util.EmailUtil;
import org.wecan.demo.util.Encrypt;

import java.util.List;

@Service
public class MessageBoardServiceImpl implements MessageBoardService {

    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @Autowired
    private EmailUtilMapper emailUtilMapper;

    private List<MessageEntity> findContentChild(MessageEntity content) {
        List<MessageEntity> list = messageBoardMapper.findMessagesByPid(content.getId());

        for (MessageEntity message : list) {
            List<MessageEntity> childList = findContentChild(message);
            message.setChildContent(childList);
        }

        return list;
    }

    @Override
    public List<MessageEntity> findAllMessages() {
        List<MessageEntity> list = messageBoardMapper.findMessagesByPid(0);

        for (MessageEntity message : list) {
            List<MessageEntity> childList = findContentChild(message);
            message.setChildContent(childList);
        }

        return list;
    }

    @Override
    public boolean insertMessage(MessageEntity message) {
        if (message.getUsername() != null && message.getText() != null) {
            messageBoardMapper.insertMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMessage(int id, int userId) {
        if (messageBoardMapper.checkId(id, userId) == 1) {
            messageBoardMapper.deleteMessage(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMessage(MessageEntity message) {
        if (messageBoardMapper.checkId(message.getId(), message.getUserId()) == 1) {
            messageBoardMapper.updateMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public List<MessageEntity> findNeededMessages(String key) {
        String content = "%" + key + "%";
        List<MessageEntity> list = messageBoardMapper.findMessagesByContent(content);

        for (MessageEntity message : list) {
            List<MessageEntity> childList = findContentChild(message);
            message.setChildContent(childList);
        }

        return list;
    }

    @Override
    public boolean insertUser(UserEntity user) {
        if (messageBoardMapper.checkUser(user.geteMail()) == 0) {
            String newPassword = Encrypt.md5Encode(user.getPassword());
            user.setPassword(newPassword);
            messageBoardMapper.insertUser(user);
            return true;
        }
        return false;
    }

    @Override
    public UserEntity checkLogin(String eMail, String password) {
        String newPassword = Encrypt.md5Encode(password);
        return messageBoardMapper.checkLogin(eMail, newPassword);
    }

    @Override
    public String sendEmail(String eMail) {
        String code = EmailUtil.achieveSimpleCode();
        List<EmailUtilEntity> list = emailUtilMapper.findUtileMail();
        EmailUtil.sendAuthCodeEmail(eMail, code, list.get(0).geteMail(), list.get(0).getPassword());
        return code;
    }
}
