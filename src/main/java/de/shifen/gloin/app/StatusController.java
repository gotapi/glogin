package de.shifen.gloin.app;

import de.shifen.gloin.toolkit.CookieToolkit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ms404
 */
@RestController
public class StatusController {
    @GetMapping("/spring/boot/status")
    public Object status(){
        return "OK";
    }


    @GetMapping("/spring/boot/cookies")
    public Object showCookie(HttpServletRequest request){
        return CookieToolkit.readCookieMap(request);
    }

}
