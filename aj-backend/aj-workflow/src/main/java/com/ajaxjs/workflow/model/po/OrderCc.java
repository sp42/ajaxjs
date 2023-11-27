package com.ajaxjs.workflow.model.po;


import com.ajaxjs.data.jdbc_helper.common.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 抄送实例实体
 */
@Data
@TableName("wf_order_cc")
@EqualsAndHashCode(callSuper = true)
public class OrderCc extends BasePersistantObject {
    private Long orderId;

    private Long actorId;

    private Long creator;

    private Date finishDate;
}
