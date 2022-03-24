package de.shifen.gloin.app;

import com.google.gson.Gson;
import com.usthe.sureness.util.JsonWebTokenUtil;
import de.shifen.gloin.misc.RtMethod;
import de.shifen.gloin.misc.ThirdPlatform;
import de.shifen.gloin.toolkit.CookieToolkit;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ms404
 */
public class Controller {
    protected String jwtIssue(String identifier, String nickname, List<String> roles){
        Map<String,Object> map = new HashMap<>(8);
        map.put("nickname",nickname);
        return JsonWebTokenUtil.issueJwt(identifier, identifier,
                "global-login-server", 1L,roles, map);
    }

    protected void handleLogin(String rtUrl,RtMethod rtMethod,HttpServletResponse response,
                               AuthRequest authRequest,
                               HttpSession session) throws IOException {
        session.setAttribute("rtUrl",rtUrl);
        session.setAttribute("rtMethod",rtMethod.toString());
        // 生成授权页面
        response.sendRedirect( authRequest.authorize(AuthStateUtils.createState()));
    }
    protected void handleCallback(
            ThirdPlatform platform,
            AuthRequest authRequest,
            HttpServletResponse response,
                                  AuthCallback callback,
                                  HttpSession session) throws IOException {
        AuthResponse resp =  authRequest.login(callback);
        if(resp.getCode()!=2000 || resp.getMsg()!=null){
            response.getWriter().write(new Gson().toJson(resp));
            return;
        }
        AuthUser authUser =(AuthUser) resp.getData();

        String identifier = platform.getByUserIdentifier(authUser.getUsername());
        String jwt = jwtIssue(identifier,authUser.getNickname(), Arrays.asList("guest"));
        String nickname = authUser.getNickname();
        String username = authUser.getUsername();
        RtMethod method = RtMethod.valueOf( session.getAttribute("rtMethod").toString());

        if(RtMethod.Cookie.equals(method)) {
            CookieToolkit.setCookie(response,CookieToolkit.IDENTIFIER_FIELD,identifier, 86400);
            CookieToolkit.setCookie(response,CookieToolkit.NAME_FIELD,nickname,86400);
            CookieToolkit.setCookie(response,CookieToolkit.TOKEN_FIELD,jwt,86400);
            CookieToolkit.setCookie(response,CookieToolkit.ID_FIELD,username,86400);
            response.sendRedirect(session.getAttribute("rtUrl").toString());
        }else{
            String url = session.getAttribute("rtUrl").toString();
            String extra = "_wjToken="+ URLEncoder.encode( jwt, StandardCharsets.UTF_8.toString())+"&_wjName="+
                    URLEncoder.encode(nickname,StandardCharsets.UTF_8.toString())+"&_wjId="+
                    URLEncoder.encode(username,StandardCharsets.UTF_8.toString())
                    +"&_wjIdentifier="+URLEncoder.encode(identifier,StandardCharsets.UTF_8.toString());;
            if(url.contains("?")){
                String target = url+"&"+extra;
                response.sendRedirect(target);
            }else{
                String target = url+"?"+extra;
                response.sendRedirect(target);
            }
        }

    }
}
