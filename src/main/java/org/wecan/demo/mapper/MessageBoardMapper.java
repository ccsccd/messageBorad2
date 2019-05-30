package org.wecan.demo.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.wecan.demo.entity.MessageEntity;
import org.wecan.demo.entity.UserEntity;

import java.util.List;

@Mapper
@Component(value = "messageBoardMapper")
public interface MessageBoardMapper {
    //#预编译，防sql注入

    @Select("SELECT * FROM message_board WHERE pid = #{pid}")
    List<MessageEntity> findMessagesByPid(@Param("pid")int pid);

    @Insert("INSERT INTO message_board(username,text,pid,create_time,user_id) VALUE(#{username},#{text},#{pid},#{createTime},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insertMessage(MessageEntity entity);

    @Delete("DELETE FROM message_board WHERE id = #{id}")
    void deleteMessage(@Param("id")int id);

    @Update("UPDATE message_board SET text = #{text},update_time=#{updateTime} WHERE id = #{id}")
    void updateMessage(MessageEntity message);

    @Select("SELECT * FROM message_board WHERE text LIKE #{content}")
    List<MessageEntity> findMessagesByContent(@Param("content")String content);

    @Select("SELECT count(id) FROM message_board WHERE id = #{id} AND user_id = #{userId}")
    int checkId(@Param("id")int id,@Param("userId")int userId);

    @Insert("INSERT INTO user(username,e_mail,password,create_time) VALUE(#{username},#{eMail},#{password},#{createTime})")
    void insertUser(UserEntity user);

    @Select("SELECT count(id) FROM `user` WHERE e_mail = #{eMail}")
    int checkUser(@Param("eMail")String eMail);

    @Select("SELECT * FROM `user` WHERE e_mail = #{eMail} and password = #{password}")
    UserEntity checkLogin(@Param("eMail")String eMail,@Param("password")String password);
}
