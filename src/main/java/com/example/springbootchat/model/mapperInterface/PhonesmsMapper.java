package com.example.springbootchat.model.mapperInterface;

import com.example.springbootchat.model.entity.Phonesms;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author 86175
 * @description 针对表【phonesms】的数据库操作Mapper
 * @createDate 2024-02-24 10:24:31
 * @Entity com.example.springbootchat.model.entity.Phonesms
 */
public interface PhonesmsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Phonesms record);

    int insertSelective(Phonesms record);

    Phonesms selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Phonesms record);

    int updateByPrimaryKey(Phonesms record);

    int sendingATextMessage(Phonesms record);

    Date selectMostRecentTimeByPrimaryKey(Long id);

    int delCode(String phone);

    int setPhoneUser(@Param("phone") String phone, @Param("uid") Long id);
}
