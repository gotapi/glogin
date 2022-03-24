package de.shifen.gloin.toolkit;

import com.usthe.sureness.util.JsonWebTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ms404
 */
@Service
@Slf4j
public class CookieToolkit implements CommandLineRunner {
    public static final String IDENTIFIER_FIELD="_wjIdentifier";
    public static final String NAME_FIELD = "_wjName";
    public static final String TOKEN_FIELD="_wjToken";
    public static final String ID_FIELD="_wjId";

    static String domain = "404.ms";


    @Value("${app.secret}")
    String secret;


    @Override
    public void run(String... args) {
        JsonWebTokenUtil.setDefaultSecretKey(secret);
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name    cookie名字
     * @return Cookie
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return (Cookie) cookieMap.get(name);
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return Map<String, Cookie>
     */
    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 保存Cookies
     *
     * @param response response响应
     * @param name     cookie的名字
     * @param value    cookie的值
     * @param time     cookie的存在时间
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int time) {
        // new一个Cookie对象,键值对为参数
        Cookie cookie = new Cookie(name, value);
        // tomcat下多应用共享
        cookie.setPath("/");
        // 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
        try {
            URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 单位：秒
        cookie.setMaxAge(time);
        cookie.setDomain(domain);
        // 将Cookie添加到Response中,使之生效
        response.addCookie(cookie);
        // addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie

    }


    /**
     * <p>删除无效cookie</p>
     * <p>无效☞1.过时 2.未发布</p>
     *
     * @param request   请求
     * @param response  响应
     * @param deleteKey 需要删除cookie的名称
     */
    private void deleteCookieByName(HttpServletRequest request, HttpServletResponse response, String deleteKey) throws NullPointerException {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        for (String key : cookieMap.keySet()) {
            if (key.equalsIgnoreCase(deleteKey)) {
                Cookie cookie = cookieMap.get(key);
                /**
                 // 默认值是-1，即：关闭浏览器时就清除cookie;
                 // 当设置为0的时候：创建完cookie，使用后马上就删除;
                 // 因为时间到了，又因为，cookie没有清除方法，所以设置为 0，就相当于清除方法;
                 // 当设置时间大于0，当时间到达后就会自动删除
                 */
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

}
