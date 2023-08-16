package com.ajaxjs.database_meta.tools.innodb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemaphoreEntry {
    String thread_id;
    String waited_at;
    String waited_time;
    String request_mode;
    String lock_type;
    String lock_name;
    String lock_loc;
    String lock_holder;
    String hold_mode;
    String lock_var;
    String lock_word;
    String readers;
    String waiters_flag;
    String last_read_locked;
    String last_write_locked;

    public SemaphoreEntry() {
    }

    public boolean parse(String line) {
        // --Thread 140455387199232 has waited at btr/btr0cur.c line 501 for 0.0000
        // seconds the semaphore:
        if (line == null || line.isEmpty())
            return false;
        if (this.thread_id == null && !line.startsWith("--Thread "))
            return false;

        try {
            if (line.startsWith("--Thread ")) {
                Pattern pt = Pattern.compile("\\-\\-Thread\\s+(\\d+)\\s+has\\s+waited\\s+at\\s+(.+)\\s+for\\s+(.+)\\s+seconds\\s+the\\s+semaphore:");
                Matcher mt = pt.matcher(line);

                if (mt.find()) {
                    this.thread_id = mt.group(1);
                    this.waited_at = mt.group(2);
                    this.waited_time = mt.group(3);
                }
//                else {
//                    logger.info("Failed to parse semaphore line: " + line);
//                }
            } else if (this.lock_name == null) {
                // S-lock on RW-latch at 0x7fc0e40a46f0 '&new_index->lock'
                // Mutex at 0x7fc43c785ee0 '&ibuf_mutex', lock var 1
                if (line.startsWith("Mutex ")) {
                    this.lock_type = "Mutex";
                    Pattern pt = Pattern.compile("Mutex\\s+at\\s+(0x[a-z0-9]+)\\s+'(.+)'\\s*,\\s*lock\\s+var\\s+(\\d+)");
                    Matcher mt = pt.matcher(line);
                    if (mt.find()) {
                        this.lock_loc = mt.group(1);
                        this.lock_name = mt.group(2);
                        this.lock_var = mt.group(3);
                    }
//                    else
//                        logger.info("Failed to parse semaphore Mutex line: " + line);

                } else {
                    Pattern pt = Pattern.compile("([A-Z]+)\\-lock\\s+on\\s+([a-zA-Z\\-]+)\\s+at\\s+(0x[a-z0-9]+)\\s+'(.+)'");
                    Matcher mt = pt.matcher(line);

                    if (mt.find()) {
                        this.request_mode = mt.group(1);
                        this.lock_type = mt.group(2);
                        this.lock_loc = mt.group(3);
                        this.lock_name = mt.group(4);
                    }
//                    else {
//                        logger.info("Failed to parse semaphore lock line: " + line);
//                    }
                }
            } else if (line.contains("thread id") && line.contains("mode")) {// a writer (thread id 139924314879744) has reserved it in mode exclusive
                Pattern pt = Pattern.compile("thread\\s+id\\s+(\\d+).+mode\\s+(.+)");
                Matcher mt = pt.matcher(line);

                if (mt.find()) {
                    this.lock_holder = mt.group(1);
                    this.hold_mode = mt.group(2);
                }
//                else
//                    logger.info("Failed to parse semaphore holder line: " + line);

            } else
                return false;
            // ignore other line for now
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;// we parsed it
    }
}
