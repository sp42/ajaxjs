package com.ajaxjs.database_meta.tools;

import com.ajaxjs.util.TestHelper;
import org.junit.Test;

import com.ajaxjs.database_meta.tools.innodb.ResultParser;

import java.util.HashMap;
import java.util.Map;

public class TestInnoDB {
    String str = "\n" +
            "=====================================\n" +
            "2023-07-23 11:04:55 140384808007424 INNODB MONITOR OUTPUT\n" +
            "=====================================\n" +
            "Per second averages calculated from the last 5 seconds\n" +
            "-----------------\n" +
            "BACKGROUND THREAD\n" +
            "-----------------\n" +
            "srv_master_thread loops: 877 srv_active, 0 srv_shutdown, 19928679 srv_idle\n" +
            "srv_master_thread log flush and writes: 0\n" +
            "----------\n" +
            "SEMAPHORES\n" +
            "----------\n" +
            "OS WAIT ARRAY INFO: reservation count 1945\n" +
            "OS WAIT ARRAY INFO: signal count 1902\n" +
            "RW-shared spins 0, rounds 0, OS waits 0\n" +
            "RW-excl spins 0, rounds 0, OS waits 0\n" +
            "RW-sx spins 0, rounds 0, OS waits 0\n" +
            "Spin rounds per wait: 0.00 RW-shared, 0.00 RW-excl, 0.00 RW-sx\n" +
            "------------------------\n" +
            "LATEST FOREIGN KEY ERROR\n" +
            "------------------------\n" +
            "2023-01-19 21:25:17 140384893351680 Transaction:\n" +
            "TRANSACTION 4417, ACTIVE 0 sec updating or deleting\n" +
            "mysql tables in use 1, locked 1\n" +
            "4 lock struct(s), heap size 1128, 2 row lock(s), undo log entries 1\n" +
            "MySQL thread id 4900, OS thread handle 140384893351680, query id 65616 119.34.178.220 root updating\n" +
            "UPDATE `auth`.`system` SET `id`='2' WHERE  `id`=7\n" +
            "Foreign key constraint fails for table `auth`.`system_app`:\n" +
            ",\n" +
            "  CONSTRAINT `sysem` FOREIGN KEY (`sys_id`) REFERENCES `system` (`id`)\n" +
            "Trying to update in parent table, in index PRIMARY tuple:\n" +
            "DATA TUPLE: 16 fields;\n" +
            " 0: len 4; hex 80000007; asc     ;;\n" +
            " 1: len 6; hex 000000001141; asc      A;;\n" +
            " 2: len 7; hex 010000015510d5; asc     U  ;;\n" +
            " 3: len 30; hex e6b4bbe698a0e9809ae794a8e59fbae7a180e8aebee696bde69c8de58aa1; asc                               ;;\n" +
            " 4: len 57; hex e58c85e68bace69687e4bbb6e4b88ae4bca0e38081e58f91e98081e982aee4bbb6e38081e79fade8aeafe7ad89e59fbae7a180e69c8de58aa1; asc                                                          ;;\n" +
            " 5: len 0; hex ; asc ;;\n" +
            " 6: len 0; hex ; asc ;;\n" +
            " 7: SQL NULL;\n" +
            " 8: SQL NULL;\n" +
            " 9: SQL NULL;\n" +
            " 10: SQL NULL;\n" +
            " 11: SQL NULL;\n" +
            " 12: len 5; hex 99af26c362; asc   & b;;\n" +
            " 13: SQL NULL;\n" +
            " 14: SQL NULL;\n" +
            " 15: len 5; hex 99af275633; asc   'V3;;\n" +
            "\n" +
            "But in child table `auth`.`system_app`, in index sysem_idx, there is a record:\n" +
            "PHYSICAL RECORD: n_fields 2; compact format; info bits 0\n" +
            " 0: len 4; hex 80000007; asc     ;;\n" +
            " 1: len 4; hex 80000001; asc     ;;\n" +
            "\n" +
            "------------\n" +
            "TRANSACTIONS\n" +
            "------------\n" +
            "Trx id counter 7452\n" +
            "Purge done for trx's n:o < 7452 undo n:o < 0 state: running but idle\n" +
            "History list length 3\n" +
            "LIST OF TRANSACTIONS FOR EACH SESSION:\n" +
            "---TRANSACTION 421860888969216, not started\n" +
            "0 lock struct(s), heap size 1128, 0 row lock(s)\n" +
            "---TRANSACTION 421860888968408, not started\n" +
            "0 lock struct(s), heap size 1128, 0 row lock(s)\n" +
            "---TRANSACTION 421860888967600, not started\n" +
            "0 lock struct(s), heap size 1128, 0 row lock(s)\n" +
            "---TRANSACTION 421860888966792, not started\n" +
            "0 lock struct(s), heap size 1128, 0 row lock(s)\n" +
            "--------\n" +
            "FILE I/O\n" +
            "--------\n" +
            "I/O thread 0 state: waiting for completed aio requests (insert buffer thread)\n" +
            "I/O thread 1 state: waiting for completed aio requests (log thread)\n" +
            "I/O thread 2 state: waiting for completed aio requests (read thread)\n" +
            "I/O thread 3 state: waiting for completed aio requests (read thread)\n" +
            "I/O thread 4 state: waiting for completed aio requests (read thread)\n" +
            "I/O thread 5 state: waiting for completed aio requests (read thread)\n" +
            "I/O thread 6 state: waiting for completed aio requests (write thread)\n" +
            "I/O thread 7 state: waiting for completed aio requests (write thread)\n" +
            "I/O thread 8 state: waiting for completed aio requests (write thread)\n" +
            "I/O thread 9 state: waiting for completed aio requests (write thread)\n" +
            "Pending normal aio reads: [0, 0, 0, 0] , aio writes: [0, 0, 0, 0] ,\n" +
            " ibuf aio reads:, log i/o's:\n" +
            "Pending flushes (fsync) log: 0; buffer pool: 0\n" +
            "1098 OS file reads, 39262 OS file writes, 25635 OS fsyncs\n" +
            "0.00 reads/s, 0 avg bytes/read, 1.20 writes/s, 0.80 fsyncs/s\n" +
            "-------------------------------------\n" +
            "INSERT BUFFER AND ADAPTIVE HASH INDEX\n" +
            "-------------------------------------\n" +
            "Ibuf: size 1, free list len 0, seg size 2, 0 merges\n" +
            "merged operations:\n" +
            " insert 0, delete mark 0, delete 0\n" +
            "discarded operations:\n" +
            " insert 0, delete mark 0, delete 0\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 9 buffer(s)\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 2 buffer(s)\n" +
            "Hash table size 34679, node heap has 1 buffer(s)\n" +
            "75.38 hash searches/s, 92.18 non-hash searches/s\n" +
            "---\n" +
            "LOG\n" +
            "---\n" +
            "Log sequence number          31720671\n" +
            "Log buffer assigned up to    31720671\n" +
            "Log buffer completed up to   31720671\n" +
            "Log written up to            31720671\n" +
            "Log flushed up to            31720671\n" +
            "Added dirty pages up to      31720671\n" +
            "Pages flushed up to          31719793\n" +
            "Last checkpoint at           31719793\n" +
            "Log minimum file id is       5\n" +
            "Log maximum file id is       9\n" +
            "10668 log i/o's done, 1.20 log i/o's/second\n" +
            "----------------------\n" +
            "BUFFER POOL AND MEMORY\n" +
            "----------------------\n" +
            "Total large memory allocated 0\n" +
            "Dictionary memory allocated 1680997\n" +
            "Buffer pool size   8192\n" +
            "Free buffers       5852\n" +
            "Database pages     2318\n" +
            "Old database pages 835\n" +
            "Modified db pages  8\n" +
            "Pending reads      0\n" +
            "Pending writes: LRU 0, flush list 0, single page 0\n" +
            "Pages made young 3152, not young 3037\n" +
            "0.40 youngs/s, 0.00 non-youngs/s\n" +
            "Pages read 1039, created 1467, written 20681\n" +
            "0.00 reads/s, 0.00 creates/s, 0.00 writes/s\n" +
            "Buffer pool hit rate 1000 / 1000, young-making rate 1 / 1000 not 0 / 1000\n" +
            "Pages read ahead 0.00/s, evicted without access 0.00/s, Random read ahead 0.00/s\n" +
            "LRU len: 2318, unzip_LRU len: 0\n" +
            "I/O sum[0]:cur[0], unzip sum[0]:cur[0]\n" +
            "--------------\n" +
            "ROW OPERATIONS\n" +
            "--------------\n" +
            "0 queries inside InnoDB, 0 queries in queue\n" +
            "0 read views open inside InnoDB\n" +
            "Process ID=2007, Main thread ID=140385342965504 , state=sleeping\n" +
            "Number of rows inserted 2046, updated 760, deleted 44, read 39097\n" +
            "0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 0.00 reads/s\n" +
            "Number of system rows inserted 9336, updated 6313, deleted 5972, read 160115\n" +
            "0.00 inserts/s, 0.60 updates/s, 0.00 deletes/s, 115.78 reads/s\n" +
            "----------------------------\n" +
            "END OF INNODB MONITOR OUTPUT\n" +
            "============================\n";

    ResultParser resultParser = new ResultParser();

    @Test
    public void testSplit() {
        Map<String, String> segments = resultParser.split(str);

        for (Map.Entry<String, String> entry : segments.entrySet()) {
            System.out.println("标题: " + entry.getKey());
            System.out.println("内容: " + entry.getValue());
            System.out.println();
        }
    }

    @Test
    public void testParseBufferPool() {
        Map<String, String> segments = resultParser.split(str);
        Map<String, String> map = new HashMap<>();
//        System.out.println(segments.get("BUFFER POOL AND MEMORY"));
        resultParser.parseBufferPool(map, segments.get("BUFFER POOL AND MEMORY"));
        TestHelper.printJson(map);
//        for (String key : map.keySet())
//            System.out.println(key);
    }
}
