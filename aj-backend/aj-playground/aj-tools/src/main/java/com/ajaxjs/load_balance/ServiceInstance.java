package com.ajaxjs.load_balance;

import lombok.Data;

@Data
public class ServiceInstance {
    private String serverName;

    private Boolean isolated;

    public Boolean isIsolated() {
        return isolated;
    }
}
