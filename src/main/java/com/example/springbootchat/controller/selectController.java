package com.example.springbootchat.controller;

import com.example.springbootchat.exception.PermissionIsAbnormal;
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
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
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
    /**
     * 存储加密cookie的密钥的id的延迟队列，延迟时间与cookie的过期时间保持一致
     */
    public static final DelayQueue<DelayedElement> COOKIE_OVERDUE_DELAY_QUEUE = new DelayQueue<>();
    /**
     * 存储加密cookie的密钥的并发哈希表
     */
    private static final ConcurrentHashMap<String, SecretKey> DATA_KEYS = new ConcurrentHashMap<>();
    /**
     * 存储用户登录凭证的HashMap
     */
    private static final HashMap<String, String> userMap = new HashMap<>();

    @Value("${cookie.expire.time}")
    private Long cookieExpireTime;
    @Resource
    private MessageSelectService messageSelectService;
    @Resource
    private FriendRequestSelectService friendRequestSelectService;
    @Value("${cookie.expire.util}")
    private TimeUnit cookieExpireTimeUnit;
    @Resource
    private UserSelectService userSelectService;
    @Resource
    private KeyGenerator keyGenerator;

    /**
     * 删除过期的cookie密钥
     */
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

    /**
     * 解密
     *
     * @param data --》 要解密的数据
     * @param kid  --》 密钥的id
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     */
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

    /**
     * 加密
     *
     * @param data --》 要加密的数据
     * @param uuid --》 密钥的id
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encrypt(String data, String uuid) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKey secretKey = DATA_KEYS.get(uuid);
        COOKIE_OVERDUE_DELAY_QUEUE.add(new DelayedElement(cookieExpireTimeUnit.toMillis(cookieExpireTime), uuid));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 创建一个新的密钥，存储密钥并返回密钥的id
     *
     * @return
     */

    public String getAndStoreKey() {
        SecretKey secretKey = keyGenerator.generateKey();
        String uuid = UUID.randomUUID().toString();
        DATA_KEYS.put(uuid, secretKey);
        return uuid;
    }

    /**
     * 用户登录
     *
     * @param un          --》cookie参数 用户名
     * @param pw          --》cookie参数 密码
     * @param kid         --》 cookie参数 密钥的id
     * @param autoLogin   --》请求头参数 是否自动登录
     * @param map         --》请求体参数 用户名 密码
     * @param response
     * @param httpSession
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     */
    @CrossOrigin(origins = {"https://lt.ximuliunian.top", "http://192.168.138.1:5500"}, allowCredentials = "true")
    @PostMapping("/users")
    public String selectUser(@CookieValue(value = "un", required = false) String un,
                             @CookieValue(value = "pw", required = false) String pw,
                             @CookieValue(value = "kId", required = false) String kid,
                             @RequestHeader(value = "autoLogin", required = false, defaultValue = "false") boolean autoLogin,
                             @RequestBody Map<String, String> map,
                             HttpServletResponse response,
                             HttpSession httpSession) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
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
        if (!"passwordError".equals(s)) {
            httpSession.setAttribute("user", userSelectService.selectUserObject(userName, password));
            if (autoLogin) {
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
        }

        return s;
    }

    /**
     * 查询id1 --》 id2的消息（只单向查询）
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/messages")
    public List<Message> selectMessage(@RequestBody Map<String, String> map, HttpSession session) {
        int id1 = Integer.parseInt(map.get("id1"));
        int id2 = Integer.parseInt(map.get("id2"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (id1 != user.getId() && id2 != user.getId()) {
                throw new PermissionIsAbnormal();
            }
        }
        return messageSelectService.selectMessage(id1, id2);
    }

    /**
     * 查询id1 --》 id2的消息 且发送时间在time以后（只单向查询）
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/messages/time")
    public List<Message> selectMessageByTime(@RequestBody Map<String, String> map, HttpSession session) {
        int id1 = Integer.parseInt(map.get("id1"));
        int id2 = Integer.parseInt(map.get("id2"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (id1 != user.getId() && id2 != user.getId()) {
                throw new PermissionIsAbnormal();
            }
            return messageSelectService.selectMessageByTime(Integer.valueOf(map.get("id1")), Integer.valueOf(map.get("id2")), map.get("time"));
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 返回UUID作为登录凭证
     *
     * @param map
     * @return
     */
    @PostMapping("/uuid")
    public HashMap<String, String> selectUuid(@RequestBody Map<String, String> map) {
        String uuid = map.get("uuid");
        return userSelectService.selectUuid(uuid);
    }

    /**
     * 根据id 查询该用户的好友
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/friends")
    public List<User> selectFriend(@RequestBody Map<String, String> map, HttpSession session) {
        int id = Integer.parseInt(map.get("id"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (id != user.getId()) {
                throw new PermissionIsAbnormal();
            }
            return userSelectService.selectFriend(id);
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 查询 其他用户基本信息
     * name--查询的用户名
     * inquirerID--查询者的id
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/queryUser")
    public User queryUser(@RequestBody Map<String, String> map, HttpSession session) {
        String name = map.get("name");
        int inquirerID = Integer.parseInt(map.get("inquirerID"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (inquirerID != user.getId()) {
                throw new PermissionIsAbnormal();
            }
            return userSelectService.queryUser(name, inquirerID);
        }
        throw new PermissionIsAbnormal();
    }

    /**
     * 查询当前用户收到的好友请求
     * id--查看者的id
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/queryFriendRequest")
    public List<friendRequest> queryFriendRequest(@RequestBody Map<String, String> map, HttpSession session) {
        int id = Integer.parseInt(map.get("id"));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (id != user.getId()) {
                throw new PermissionIsAbnormal();
            }
            return friendRequestSelectService.queryFriendRequest(id);
        }
        throw new PermissionIsAbnormal();
    }


}
