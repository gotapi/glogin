package de.shifen.gloin.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ms404
 */
@RestController
public class StatusController {
    @GetMapping("/spring/boot/status")
    public Object status(){
        return "OK";
    }
}
