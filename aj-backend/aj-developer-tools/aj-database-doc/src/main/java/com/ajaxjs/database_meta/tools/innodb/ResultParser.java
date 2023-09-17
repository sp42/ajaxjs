package com.ajaxjs.database_meta.tools.innodb;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultParser {
    private static final LogHelper LOGGER = LogHelper.getLog(ResultParser.class);

    public String getStatus() {
        return CRUD.infoMap("SHOW ENGINE INNODB STATUS").get("Status").toString();
    }

    static final Pattern SPLIT_PATTERN = Pattern.compile("-{2,}\n(.+?)\n-{2,}(.+?)(?=-{2,}\n|$)", Pattern.DOTALL);

    /**
     * 先分组
     *
     * @param result MySQL 返回的结果
     * @return 分组 map
     */
    public Map<String, String> split(String result) {
        Matcher matcher = SPLIT_PATTERN.matcher(result);
        Map<String, String> segments = new HashMap<>();

        while (matcher.find()) {
            String header = matcher.group(1).trim();
            String content = matcher.group(2).trim();
            segments.put(header, content);
        }

        return segments;
    }

    public Map<String, String> parse(String id) {
        String str = getStatus();
        Map<String, String> segments = split(str);
        SortedMap<String, String> map = new TreeMap<>(String::compareTo);
        String segment = segments.get(id);

        try (StringReader reader = new StringReader(segment);
             BufferedReader bufReader = new BufferedReader(reader)) {
            String line;

            while ((line = bufReader.readLine()) != null) {
                if (!StringUtils.hasText(line))
                    continue;

                line = line.trim();

                switch (id) {
                    case "BACKGROUND THREAD":
                        String[] vals = line.split(":");

                        if (vals.length > 1)
                            map.put(vals[0], vals[1]);

                        map.put("Time", new Date().toString());

                        break;
                    case "FILE I/O":
                        parseFileIO(map, line);
                        break;
                    case "BUFFER POOL AND MEMORY":
                        parseBufferPool(map, line);
                        break;
                    case "ROW OPERATIONS":
                        parseRowOperation(map, line);
                        break;
                    case "SEMAPHORES":
                        parseSemaphore(map, line);
                        break;
                    case "LOG":
                        parseLog(map, line);
                        break;
                    case "INSERT BUFFER AND ADAPTIVE HASH INDEX":
                        parseIBuf(map, line);
                        break;
                }
            }

        } catch (Throwable e) {
            LOGGER.warning(e);
        }

        return map;
    }

    private static final String[] BUFFER_INTERNALS = new String[]{"Adaptive hash index", "Page hash", "Dictionary cache", "File system", "Lock system", "Recovery system"};

    private void parseFileIO(Map<String, String> valMap, String str) {
        if (str.startsWith("I/O thread ")) {
            String[] vals = str.split(":");
            if (vals.length > 1)
                valMap.put(vals[0], vals[1]);

        } else if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
            String[] vals = str.split(",");

            for (String v : vals) {
                String v2 = v.trim();
                int idx = v2.indexOf(' ');

                if (idx > 0)
                    valMap.put(v2.substring(idx + 1).trim(), v2.substring(0, idx).trim());
            }
        } else if (str.indexOf(',') >= 0) {
            String[] strs = str.split(",");

            for (String s : strs) {
                String[] res = splitSpacePair(s, true);

                if (res != null) {
                    String name = res[0];

                    if (name.contains(":"))
                        name = name.substring(0, name.indexOf(":"));

                    valMap.put(name, res[1]);
                }
            }
        }
    }

    /**
     * Split a space delimit name value pair, with name followed by value. The name could have space too.
     *
     * @return 2 element array. The first is name, the second is value
     */
    private static String[] splitSpacePair(String str2, boolean isNameValue) {
        if (str2 == null || str2.isEmpty())
            return null;
        str2 = str2.trim();

        int idx = isNameValue ? str2.lastIndexOf(' ') : str2.indexOf(' ');// since we trimmed, it cannot be the last one
        if (idx <= 0)
            return null;

        String name = str2.substring(0, idx), val = str2.substring(idx + 1);

        if (val.isEmpty())
            return null;

        // check if we can parse
        try {
            new BigDecimal(val);
            String[] res = new String[2];
            res[0] = name.trim();
            res[1] = val;

            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public void parseBufferPool(Map<String, String> valMap, String str) {
        String str2 = str.trim();

        if (str2.startsWith("Total memory")) {
            String[] vals = str2.split(";");

            for (String v : vals) {
                String v2 = v.trim();
                int idx = v2.lastIndexOf(' ');

                if (idx > 0)
                    valMap.put(v2.substring(0, idx), v2.substring(idx + 1));
            }
        } else if (str.startsWith("Internal hash")) {// Skip sub header
        } else if (str.startsWith("  ") || str.indexOf('(') > 0) {// indented
            for (String hts : BUFFER_INTERNALS) {
                if (str.contains(hts)) {
                    str2 = str.substring(str.indexOf(hts) + hts.length()).trim();
                    int idx = str2.lastIndexOf(')');

                    if (idx >= 0)
                        valMap.put("Internal hash tables - " + hts, str2.substring(0, idx + 1));
                }
            }
        } else if (str2.startsWith("Dictionary memory allocated") || str2.startsWith("Buffer pool size") || str2.startsWith("Free buffers")
                || str2.startsWith("Database pages") || str2.startsWith("Old database pages") || str2.startsWith("Modified db pages")
                || str2.startsWith("Pending reads")) {
            int idx = str2.lastIndexOf(' ');

            if (idx >= 0)
                valMap.put(str2.substring(0, idx), str2.substring(idx + 1));

        } else if (str2.startsWith("Pending writes")) {
            String str3 = str2.substring(str2.indexOf(':') + 1).trim();
            String[] vals = str3.split(",");

            for (String v : vals) {
                v = v.trim();

                if (v.contains("flush list") && v.contains("single page")) {
                    valMap.put("flush list", v.substring(v.indexOf("flush list") + 11, v.indexOf("single page") - 1));
                    valMap.put("single page", v.substring(v.lastIndexOf(' ') + 1));
                } else {
                    int idx = v.lastIndexOf(' ');
                    if (idx >= 0)
                        valMap.put("Pending writes: " + v.substring(0, idx), v.substring(idx + 1));
                }
            }
        } else if (str2.indexOf(',') >= 0) {
            String[] vals = str2.split(",");

            for (String v : vals) {
                v = v.trim();
                if (v.charAt(0) >= '0' && v.charAt(0) <= '9') {
                    int idx = v.lastIndexOf(' ');

                    if (idx >= 0) {
                        String k = v.substring(idx + 1);
                        k = "Pages " + k;
                        valMap.put(k, v.substring(0, idx));
                    }
                } else if (v.indexOf(':') >= 0) {
                    int idx = v.lastIndexOf(':');
                    if (idx >= 0)
                        valMap.put(v.substring(0, idx), v.substring(idx + 1));

                } else if (v.startsWith("Buffer pool hit rate")) {
                    valMap.put("Buffer pool hit rate", v.substring(21));
                } else if (v.startsWith("young-making rate")) {
                    int idx = v.lastIndexOf("not");
                    valMap.put("young-making rate", v.substring(18, idx));
                } else {
                    int idx = v.lastIndexOf(' ');

                    if (idx >= 0) {
                        String k = v.substring(0, idx);

                        if (str.startsWith("Pages") && !k.startsWith("Pages"))
                            k = "Pages " + k;

                        valMap.put(k, v.substring(idx + 1));
                    }
                }
            }
        } else
            LOGGER.info("Data not parsed: " + str2);
    }

    private void parseRowOperation(Map<String, String> valMap, String str) {
        if (str.contains("queries inside InnoDB") || str.contains("read views")) {
            String[] vals = str.split(",");

            for (String v : vals) {
                v = v.trim();
                int idx = v.indexOf(' ');
                if (idx > 0)
                    valMap.put(v.substring(idx + 1), v.substring(0, idx));
            }
        } else if (str.contains("Main thread")) {
            String[] vals = str.split(",");
            for (String v : vals) {
                String v2 = v.trim();
                int idx = v2.lastIndexOf(' ');
                if (idx > 0) {
                    String k = v2.substring(0, idx).trim();
                    if (!k.startsWith("Main thread"))
                        k = "Main thread " + k;
                    valMap.put(k, v2.substring(idx + 1).trim());
                }
            }
        } else if (str.contains("Number of rows")) {
            String[] vals = str.split(",");

            for (String v : vals) {
                String v2 = v.trim();
                int idx = v2.lastIndexOf(' ');
                if (idx > 0) {
                    String k = v2.substring(0, idx).trim();
                    if (!k.startsWith("Number of rows"))
                        k = "Number of rows " + k;
                    valMap.put(k, v2.substring(idx + 1).trim());
                }
            }
        } else if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
            String[] vals = str.split(",");

            for (String v : vals) {
                String v2 = v.trim();
                int idx = v2.indexOf(' ');

                if (idx > 0) {
                    String k = v2.substring(idx + 1).trim();
                    valMap.put(k, v2.substring(0, idx).trim());
                }
            }
        } else
            LOGGER.warning("Data not parsed: " + str);
    }

    private void parseSemaphore(Map<String, String> valMap, String str) {
        if (str.startsWith("OS WAIT ARRAY INFO: ")) {
            String str2 = str.substring(str.indexOf(':') + 1).trim();
            String[] vals = str2.split(",");

            for (String v : vals) {
                v = v.trim();
                int idx = v.lastIndexOf(' ');
                if (idx > 0)
                    valMap.put("OS WAIT ARRAY INFO: " + v.substring(0, idx), v.substring(idx + 1));
            }
        } else if (str.startsWith("Mutex")) {
            String str2 = str.substring(6).trim();
            String[] vals = str2.split(",");

            for (String v : vals) {
                v = v.trim();
                int idx = v.lastIndexOf(' ');
                if (idx > 0)
                    valMap.put("Mutex " + v.substring(0, idx), v.substring(idx + 1));
            }
        } else if (str.startsWith("Spin rounds per wait:")) {
            String str2 = str.substring(str.indexOf(':') + 1).trim();
            String[] vals = str2.split(",");

            for (String v : vals) {
                v = v.trim();
                int idx = v.indexOf(' ');
                if (idx > 0)
                    valMap.put("Spin rounds per wait: " + v.substring(idx + 1), v.substring(0, idx));
            }
        } else if (str.contains("RW-shared spins") || str.contains("RW-excl spins")) {
            String[] vals = str.split(";");

            for (String v : vals) {
                v = v.trim();
                String[] vals2 = v.split(",");

                for (String v2 : vals2) {
                    int idx = v2.lastIndexOf(' ');

                    if (idx > 0) {
                        String k = v2.substring(0, idx);
                        if (v.contains("RW-shared") && !k.contains("RW-shared"))
                            k = "RW-shared " + k;
                        else if (v.contains("RW-excl") && !k.contains("RW-excl"))
                            k = "RW-excl " + k;

                        valMap.put(k, v2.substring(idx + 1));
                    }
                }
            }
        } else if (str.startsWith("RW-sx")) {
            String str2 = str.substring(str.indexOf(' ') + 1).trim();
            String[] vals = str2.split(",");

            for (String v : vals) {
                v = v.trim();
                int idx = v.lastIndexOf(' ');

                if (idx > 0)
                    valMap.put("RW-sx " + v.substring(0, idx), v.substring(idx + 1));
            }
        } else
            LOGGER.warning("Data not parsed: " + str);
    }

    private void parseLog(Map<String, String> valMap, String str) {
        if (str.indexOf(',') < 0) {// not comma separated line, so name value pair
            String[] res = splitSpacePair(str, true);
            if (res != null)
                valMap.put(res[0], res[1]);
        } else {// comma separated, each substring has value name pair
            String[] strs = str.split(",");
            for (String s : strs) {
                String[] res = splitSpacePair(s, false);

                if (res != null)
                    valMap.put(res[0], res[1]);
            }
        }
    }

    private void parseIBuf(Map<String, String> valMap, String str) {
        if (str.startsWith("Ibuf: ")) {
            String str2 = str.substring(5).trim();
            String[] strs = str2.split(",");

            for (String s : strs) {
                String[] res = splitSpacePair(s, true);
                if (res != null)
                    valMap.put("Ibuf " + res[0], res[1]);
            }
        } else if (Character.isDigit(str.charAt(0))) {// value name pair
            String[] strs = str.split(",");
            for (String s : strs) {
                String[] res = splitSpacePair(s, false);
                if (res != null)
                    valMap.put(res[0], res[1]);
            }
        } else if (str.startsWith("Hash table")) {
            String[] strs = str.split(",");

            for (String s : strs) {
                if (s != null)
                    s = s.trim();
                else
                    continue;
                if (s.startsWith("Hash table")) {
                    String[] res = splitSpacePair(s, true);

                    if (res != null)
                        valMap.put(res[0], res[1]);
                } else if (s.startsWith("node heap")) {
                    String[] ss = s.split(" ");

                    if (ss.length > 3)
                        valMap.put("node heap buffer(s)", ss[3]);
                }
            }
        }

    }

}
