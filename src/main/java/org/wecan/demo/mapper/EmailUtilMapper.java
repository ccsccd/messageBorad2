package org.wecan.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.wecan.demo.entity.EmailUtilEntity;

import java.util.List;

@Mapper
@Component(value = "emailUtilMapper")
public interface EmailUtilMapper {
    @Select("SELECT * FROM email_util")
    List<EmailUtilEntity> findUtileMail();
}
