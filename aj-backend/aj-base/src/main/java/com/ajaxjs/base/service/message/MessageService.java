package com.ajaxjs.base.service.message;

import com.ajaxjs.base.controller.MessageController;
import com.ajaxjs.base.model.MailVo;
import com.ajaxjs.base.service.message.email.Mail;
import com.ajaxjs.base.service.message.email.Sender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements MessageController {
    @Autowired
    Mail mailCfg;

    @Override
    public boolean email(MailVo mail) {
        System.out.println(mailCfg);
        BeanUtils.copyProperties(mail, mailCfg);

        return Sender.send(mailCfg);
    }
}
