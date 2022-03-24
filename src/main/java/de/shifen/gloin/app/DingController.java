package de.shifen.gloin.app;

import com.xkcoding.http.config.HttpConfig;
import de.shifen.gloin.misc.RtMethod;
import de.shifen.gloin.misc.ThirdPlatform;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author ms404
 */
@Slf4j
@RestController
public class DingController extends Controller{
    @Value("app.dingding.app_key")
    String appKey;
    @Value("app.dingding.app_secret")
    String appSecret;
    @Value("app.dingding.callback")
    String callbackUri;
    protected AuthRequest getAuthRequest(){
        return  new AuthGithubRequest(AuthConfig.builder()
                .clientId(appKey)
                .clientSecret(appSecret)
                .redirectUri(callbackUri)
                .httpConfig(
                        HttpConfig.builder().timeout(15000)
                                .proxy(new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("127.0.0.1", 1080)))
                                . build())
                .build());
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response, @RequestParam("_returnUrl") String rtUrl,
                      @RequestParam(value = "_returnMethod",defaultValue = "Cookie") RtMethod rtMethod,
                      HttpSession session) throws IOException {
        handleLogin(rtUrl,rtMethod,response,getAuthRequest(),session);
    }

    @GetMapping("/callback")
    public void callback(HttpServletRequest request,HttpServletResponse response,
                         AuthCallback callback,
                         HttpSession session) throws IOException {
        handleCallback(ThirdPlatform.DingDing,getAuthRequest(),response,callback,session);
    }
}
