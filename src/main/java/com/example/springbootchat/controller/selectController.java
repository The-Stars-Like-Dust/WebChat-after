package com.example.springbootchat.controller;

import com.example.springbootchat.model.DelayedElement;
import com.example.springbootchat.model.entity.Message;
import com.example.springbootchat.model.entity.User;
import com.example.springbootchat.model.entity.friendRequest;
import com.example.springbootchat.service.friendRequestService.FriendRequestSelectService;
import com.example.springbootchat.service.messagesService.MessageSelectService;
import com.example.springbootchat.service.userService.UserSelectService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/sel")
@CrossOrigin()
public class selectController {
    public static final DelayQueue<DelayedElement> COOKIE_OVERDUE_DELAY_QUEUE = new DelayQueue<>();
    private static final ConcurrentHashMap<String, SecretKey> DATA_KEYS = new ConcurrentHashMap<>();
    private static HashMap<String, String> userMap = new HashMap<>();
    @Value("${cookie.expire.time}")
    private Long cookieExpireTime;
    @Resource
    private MessageSelectService messageSelectService;
    @Resource
    private FriendRequestSelectService friendRequestSelectService;
    @Value("${cookie.expire.time.util}")
    private TimeUnit cookieExpireTimeUnit;
    @Resource
    private UserSelectService userSelectService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private KeyGenerator keyGenerator;

    @PostConstruct
    public void deleteOverdueCookit() {
        new Thread(() -> {
            while (true) {
                try {
                    DelayedElement delayedElement = COOKIE_OVERDUE_DELAY_QUEUE.take();
                    DATA_KEYS.remove(delayedElement.getKey());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public selectController() {
    }

    public String decrypt(String data, String kid) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        SecretKey secretKey = DATA_KEYS.get(kid);
        if (secretKey == null) {
            return "null";
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(data);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String encrypt(String data, String uuid) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = DATA_KEYS.get(uuid);
        COOKIE_OVERDUE_DELAY_QUEUE.add(new DelayedElement(cookieExpireTimeUnit.toMillis(cookieExpireTime), uuid));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String getAndStoreKey() {
        SecretKey secretKey = keyGenerator.generateKey();
        String uuid = UUID.randomUUID().toString();
        DATA_KEYS.put(uuid, secretKey);
        return uuid;
    }

    @CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
    @PostMapping("/users")
    public String selectUser(@CookieValue(value = "un", required = false) String un,
                             @CookieValue(value = "pw", required = false) String pw,
                             @CookieValue(value = "kId", required = false) String kid,
                             @RequestHeader(value = "autoLogin", required = false, defaultValue = "false") boolean autoLogin,
                             @RequestBody Map<String, String> map,
                             HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        if (kid != null && un != null && pw != null) {
            un = decrypt(un, kid);
            if (!"null".equals(un)) {
                pw = decrypt(pw, kid);
                String s = userSelectService.selectUser(un, pw);
                if (!"passwordError".equals(s)) {
                    return s;
                }
            }
        }
        String userName = map.get("userName");
        String password = map.get("password");
        String s = userSelectService.selectUser(userName, password);
        if (!"passwordError".equals(s) && autoLogin) {
            kid = getAndStoreKey();
            un = encrypt(userName, kid);
            pw = encrypt(password, kid);
            ResponseCookie cookie = ResponseCookie.from("kId", kid).maxAge(cookieExpireTimeUnit.toSeconds(cookieExpireTime)).secure(true).sameSite("None").build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            ResponseCookie cookie1 = ResponseCookie.from("un", un).maxAge(cookieExpireTimeUnit.toSeconds(cookieExpireTime)).secure(true).sameSite("None").build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie1.toString());
            ResponseCookie cookie2 = ResponseCookie.from("pw", pw).maxAge(cookieExpireTimeUnit.toSeconds(cookieExpireTime)).secure(true).sameSite("None").build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie2.toString());
        }
        return s;
    }


    @PostMapping("/messages")
    public List<Message> selectMessage(@RequestBody Map<String, String> map) {
        return messageSelectService.selectMessage(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")));
    }


    @PostMapping("/messages/time")
    public List<Message> selectMessageByTime(@RequestBody Map<String, String> map) {
        return messageSelectService.selectMessageByTime(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")), map.get("time"));
    }

    @PostMapping("/uuid")
    public HashMap<String, String> selectUuid(@RequestBody Map<String, String> map) {
        String uuid = map.get("uuid");
        return userSelectService.selectUuid(uuid);
    }

    @PostMapping("/friends")
    public List<User> selectFriend(@RequestBody Map<String, String> map) {
        int id = Integer.parseInt(map.get("id"));
        return userSelectService.selectFriend(id);

    }


    @PostMapping("/queryUser")
    public User queryUser(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        Integer inquirerID = Integer.valueOf(map.get("inquirerID"));
        return userSelectService.queryUser(name, inquirerID);
    }


    @PostMapping("/queryFriendRequest")
    public List<friendRequest> queryFriendRequest(@RequestBody Map<String, String> map) {
        int id = Integer.parseInt(map.get("id"));
        return friendRequestSelectService.queryFriendRequest(id);

    }


}
