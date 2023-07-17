/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import com.ajaxjs.mysql.common.KeyTool;
import lombok.Data;

/**
 * Database access username and password
 *
 * @author xrao
 */
@Data
public class DBCredential implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String appUser;// the user stores this db username/password pair
    private String username;// db username
    private String password;// db password
    private String dbGroupName;// the name of the database group
    private String encrypted; // encrypted password

    /**
     * Rerun a copy
     */
    public DBCredential copy() {
        DBCredential cred = new DBCredential();
        cred.setAppUser(this.appUser);
        cred.setDbGroupName(this.dbGroupName);
        cred.setUsername(this.username);
        cred.setPassword(this.password);
        cred.setEncrypted(encrypted);

        return cred;
    }

    /**
     * Invoke this method if password is encrypted
     */
    public void decryptPassword(KeyTool.DesEncrypter keyTool) {
        if (encrypted == null)
            return;

        String credString = keyTool.decrypt(encrypted);
        credString = credString.substring(0, credString.lastIndexOf("::"));
        credString = credString.substring(credString.lastIndexOf("::") + 2);

        password = credString;
    }
}
