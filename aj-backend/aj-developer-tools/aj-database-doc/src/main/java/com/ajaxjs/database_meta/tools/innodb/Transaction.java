package com.ajaxjs.database_meta.tools.innodb;

/**
 * Used to parse transaction record
 */
public class Transaction {
    String id;// TX ID
    String state;// transaction status: ACTIVE or not started
    String txTime;
    String processNumber;
    String action;
    String lockStructs;// number of lock structs
    String lockStructsStatus;// number of lock structs
    String undoLogEntries;
    String heapSize;
    String threadId;
    String queryId;
    String host;
    String user;
    String cmd;
    String sql;
    String comments;

    static final int TX_BEFORE = -1;
    static final int TX_START = 0;
    static final int TX_ACTION = 1;
    static final int TX_THREAD = 2;
    static final int TX_SQL_START = 3;
    static final int TX_COMMENT = 4;
    private int txstate = -1;

    Transaction() {
    }

    void parseLine(String str) {
        if ((str.startsWith("------") && str.contains("TRX")) || str.startsWith("Trx read view will not see trx with id")
                || str.startsWith("TABLE LOCK table ") || str.startsWith("RECORD LOCK space "))
            txstate = TX_COMMENT;
        else if (str.startsWith("MySQL thread id"))
            txstate = TX_THREAD;
        try {
            switch (txstate) {
                case TX_BEFORE:
                    if (str.startsWith("---TRANSACTION ")) {
                        txstate = TX_START;
                        id = str.substring(str.indexOf(' ') + 1, str.indexOf(','));
                        String str2 = str.substring(str.indexOf(',') + 1).trim();

                        if (str2.startsWith("not started")) {
                            state = "not started";
                            if (str2.indexOf(',') >= 0)
                                str2 = str2.substring(str2.indexOf(',') + 1).trim();
                            else
                                str2 = null;
                        } else if (str2.startsWith("ACTIVE")) {
                            state = "ACTIVE";
                            if (str2.indexOf(',') > 0) {
                                txTime = str2.substring(str2.indexOf(' ') + 1, str2.indexOf(','));
                                str2 = str2.substring(str2.indexOf(',') + 1).trim();
                            } else {
                                int idx1 = str2.indexOf(' ');
                                int idx2 = str2.indexOf(' ', idx1 + 1);
                                if (idx2 > 0)
                                    idx2 = str2.indexOf(' ', idx2 + 1);
                                if (idx2 > 0) {
                                    txTime = str2.substring(idx1 + 1, idx2);
                                    str2 = str2.substring(idx2).trim();
                                } else {
                                    txTime = str2.substring(idx1 + 1);
                                    str2 = null;
                                }
                            }
                        } else {
                            if (str2.indexOf(',') >= 0) {
                                state = str2.substring(0, str2.indexOf(',')).trim();
                                str2.substring(str2.indexOf(',') + 1).trim();
                            } else {
                                state = str2;
                                str2 = null;
                            }
                        }

                        if (str2 != null) {
                            if (str2.startsWith("process ")) {
                                if (str2.indexOf(',') > 0)
                                    str2 = str2.substring(0, str2.indexOf(','));
                                this.processNumber = str2.substring(str2.lastIndexOf(' ') + 1);
                            } else
                                this.action = str2;
                        }
                    }

                    break;
                case TX_START:
                    this.action = str.trim();
                    txstate = TX_ACTION;
                    break;
                case TX_ACTION:
                    // TODO
                    txstate = TX_THREAD;
                    break;
                case TX_THREAD:
                    String str2 = str.substring(0, str.indexOf(','));
                    this.threadId = str2.substring(str2.lastIndexOf(' '));
                    str2 = str.substring(str.indexOf("query id")).trim();
                    this.queryId = str2.substring(8, str2.indexOf(' ', 10));
                    str2 = str2.substring(str2.indexOf(' ', 10) + 1).trim();

                    if (str2.startsWith("Slave ")) {
                        txstate = TX_SQL_START;
                        this.cmd = str2;
                        break;
                    } else if (str2.startsWith("update") || str2.startsWith("Updating") || str2.startsWith("init")) {
                        txstate = TX_SQL_START;
                        this.cmd = str2;
                        break;
                    }

                    try {
                        this.host = str2.substring(0, str2.indexOf(' '));
                        str2 = str2.substring(str2.indexOf(' ') + 1).trim();

                        if (str2.charAt(0) >= '0' && str2.charAt(0) <= '9' && str2.indexOf('.') >= 0) {
                            // ip address
                            this.host += " " + str2.substring(0, str2.indexOf(' '));
                            str2 = str2.substring(str2.indexOf(' ') + 1).trim();
                        }
                        if (str2.indexOf(' ') > 0) {
                            this.user = str2.substring(0, str2.indexOf(' '));
                            this.cmd = str2.substring(str2.indexOf(' ') + 1);
                        } else
                            this.user = str2;
                    } catch (Exception iex) {
                        iex.printStackTrace();
                    }

                    txstate = TX_SQL_START;
                    break;
                case TX_SQL_START:
                    if (this.sql == null)
                        this.sql = str;
                    else
                        this.sql += "\n" + str;
                    break;
                case TX_COMMENT:
                    if (this.comments == null)
                        this.comments = str;
                    else
                        this.comments += "\n" + str;
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
