package com.ajaxjs.load_balance;

import java.util.List;

public interface DynamicUploadRule {
    List<ServiceInstance> getServiceInstanceRule();
}
