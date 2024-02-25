package com.example.springbootchat.service.smsService;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.example.springbootchat.configuration.PhoneConfig;
import com.example.springbootchat.model.entity.Phonesms;
import com.example.springbootchat.model.mapperInterface.PhonesmsMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

/**
 * 这是一个处理短信服务的类。
 * 它允许客户端发送短信，检查验证码，查询最近的时间和设置电话用户。
 */
@Transactional
@Service
public class SMSService {
    @Resource
    private PhonesmsMapper phonesmsMapper;

    @Autowired
    private com.aliyun.dysmsapi20170525.Client client;
    @Resource
    private PhoneConfig phoneConfig;
    @Resource
    private Random random;

    /**
     * 此方法用于发送短信。
     * 它首先生成一个随机的验证码，然后发送短信。
     *
     * @param phone 手机号码。
     * @return 发送结果，总是返回"1"。
     */
    public String sendSMS(String phone) throws Exception {
        SendSmsRequest SendSmsRequest = phoneConfig.getSendSmsRequest();
        SendSmsRequest.phoneNumbers = phone;
        Integer codeInt = random.nextInt(10000);
        String code = codeInt.toString();
        if (codeInt < 1000) {
            StringBuilder stringBuilder = new StringBuilder(codeInt.toString());
            while (stringBuilder.length() < 4) {
                stringBuilder.insert(0, "0");
            }
            code = stringBuilder.toString();
        }
        SendSmsRequest.templateParam = "{\"code\":\"" + code + "\"}";
        Phonesms phonesms = phonesmsMapper.selectByPrimaryKey(phone);
        Phonesms build = Phonesms.builder().phone(phone).mostRecentCode(code).mostRecentTime(new Date()).build();
        if (phonesms == null) {
            phonesmsMapper.insertSelective(build);
        } else {
            phonesmsMapper.sendingATextMessage(build);
        }
        SendSmsResponse sendSmsResponse = client.sendSms(SendSmsRequest);
        return "1";
    }

    /**
     * 此方法用于检查验证码。
     * 它首先查询数据库中的验证码，然后与用户输入的验证码进行比较。
     *
     * @param phone 手机号码。
     * @param code  用户输入的验证码。
     * @return 检查结果，如果验证码正确，返回true，否则返回false。
     */
    public Boolean checkCode(String phone, String code) {
        Phonesms phonesms = phonesmsMapper.selectByPrimaryKey(phone);
        if (phonesms == null || phonesms.getMostRecentCode() == null) {
            return false;
        } else if (phonesms.getMostRecentCode().equals(code)) {
            phonesmsMapper.delCode(phone);
            return true;
        }
        return false;
    }

    /**
     * 此方法用于查询最近的时间。
     * 它返回数据库中存储的最近的时间。
     *
     * @param id 用户的ID。
     * @return 最近的时间。
     */
    public Date selectMostRecentTimeByPrimaryKey(Long id) {
        return phonesmsMapper.selectMostRecentTimeByPrimaryKey(id);
    }

    /**
     * 此方法用于查询电话号码。
     * 它返回数据库中存储的电话号码。
     *
     * @param id 用户的ID。
     * @return 电话号码。
     */
    public Phonesms selectByPrimaryKey(String id) {
        return phonesmsMapper.selectByPrimaryKey(id);
    }

    /**
     * 此方法用于设置电话用户。
     * 它在数据库中更新电话用户的ID。
     *
     * @param phone 手机号码。
     * @param id    用户的ID。
     */
    public void setPhoneUser(String phone, int id) {
        phonesmsMapper.setPhoneUser(phone, (long) id);
    }
}