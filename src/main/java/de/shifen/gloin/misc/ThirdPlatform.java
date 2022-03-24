package de.shifen.gloin.misc;

import lombok.Getter;

/**
 * @author ms404
 */
public enum ThirdPlatform {
    /**
     * 钉钉
     */
    DingDing("dingding","dingoaf5gfoo97tqg4hm1m"),
    /**
     * 企业微信
     */
    WeWork("qw","wwb1cf38bb35fd125f"),
    /**
     * github
     */
    Github("github","183209");
    @Getter
    final String platformName;
    @Getter
    final String corpId;

    ThirdPlatform(String name, String corpId) {
        this.platformName = name;
        this.corpId = corpId;
    }

    public  String getByUserIdentifier(String name){
        return this.platformName+"://"+name+"@"+corpId;
    }
}
