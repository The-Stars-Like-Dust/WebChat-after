package com.example.springbootchat.controller;

import com.example.springbootchat.model.entity.Phonesms;
import com.example.springbootchat.service.smsService.SMSService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Pattern;

/**
 * 这是一个处理电话相关操作的控制器类。
 * 它允许客户端发送短信和检查电话号码的有效性。
 */
@CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
@Controller
public class PhoneController {
    @Resource
    private Pattern pattern;
    @Resource
    private SMSService smsService;

    /**
     * 此方法用于发送短信。
     * 它首先检查电话号码的格式，然后检查短信发送频率是否超过限制。
     * 如果所有检查都通过，它会发送短信。
     *
     * @param phonesms 要发送短信的电话号码。
     * @return 表示操作结果的字符串。"-1"表示电话号码格式无效，"-2"表示短信发送过于频繁，"-3"表示超过短信发送限制，或者短信发送操作的结果。
     */
    @ResponseBody
    @RequestMapping("/sendSMS")
    public String sendSMS(@RequestBody Phonesms phonesms) {
        Phonesms phdata;
        String phone = phonesms.getPhone();
        if (!checkPhone(phone)) {
            return "-1";    //手机号格式错误码
        } else if ((phdata = smsService.selectByPrimaryKey(phonesms.getPhone())) != null) {
            long time = phdata.getMostRecentTime().getTime();
            long now = System.currentTimeMillis();
            if (now - time < 120000) {
                return "-2";    //发送过于频繁错误码
            }
            if (phdata.getEveryDayCount() >= 2 || phdata.getEveryWeekCount() >= 5 || phdata.getEveryMonthCount() >= 10) {
                return "-3";    //发送达到上限错误码
            }
        }
        try {
            return smsService.sendSMS(phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法用于检查电话号码是否有效。
     * 它使用正则表达式来检查电话号码的格式。
     *
     * @param phone 要检查的电话号码。
     * @return 如果电话号码有效，则返回true，否则返回false。
     */
    private boolean checkPhone(String phone) {
        return pattern.matcher(phone).matches();
    }
}