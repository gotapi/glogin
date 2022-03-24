package de.shifen.gloin.app;

import com.xkcoding.http.config.HttpConfig;
import de.shifen.gloin.misc.RtMethod;
import de.shifen.gloin.misc.ThirdPlatform;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ms404
 */
@RestController
@RequestMapping("/wework")
public class WeworkController extends Controller{
    @Value("app.wework.app_key")
    String appKey;
    @Value("app.wework.app_secret")
    String appSecret;
    @Value("app.wework.callback")
    String callbackUri;
    protected AuthRequest getAuthRequest(){
        return  new AuthWeChatEnterpriseQrcodeRequest(AuthConfig.builder()
                .clientId(appKey)
                .clientSecret(appSecret)
                .redirectUri(callbackUri)
                .httpConfig(HttpConfig.builder().timeout(15000). build())
                .build());
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response, @RequestParam("_returnUrl") String rtUrl,
                      @RequestParam(value = "_returnMethod",defaultValue = "Cookie") RtMethod rtMethod,
                      HttpSession session) throws IOException {
        handleLogin(rtUrl,rtMethod,response,getAuthRequest(),session);
    }

    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response,
                         AuthCallback callback,
                         HttpSession session) throws IOException {
        handleCallback(ThirdPlatform.DingDing,getAuthRequest(),response,callback,session);
    }
}
