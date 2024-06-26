package com.yami.shop.bean.enums;

/**
 * @author lanhai
 */
public enum PayType {

    /**
     * 微信支付
     */
    WECHATPAY(1, "微信支付"),

    /**
     * "支付宝
     */
    ALIPAY(2, "支付宝");

    private Integer num;

    private String payTypeName;

    public Integer value() {
        return num;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    PayType(Integer num, String payTypeName) {
        this.num = num;
        this.payTypeName = payTypeName;
    }

    public static PayType instance(Integer value) {
        PayType[] enums = values();
        for (PayType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
