package com.ajaxjs.database_meta.tools;

import java.util.HashMap;
import java.util.Map;

public class Explain {
    public static String show(String id, String key) {
        if (MAP.containsKey(id)) {
            Map<String, String> m = MAP.get(id);
            key = key.trim();
            return m.getOrDefault(key, "");
        } else return "";
    }

    private static final Map<String, Map<String, String>> MAP = new HashMap<String, Map<String, String>>() {{
        put("inno_status_file_io", new HashMap<String, String>() {{
            put("OS file reads", "操作系统文件读取次数");
            put("OS file writes", "操作系统文件写入次数");
            put("OS fsyncs", "操作系统fsync同步次数");
            put("avg bytes/read", "平均每次读取字节数");
            put("fsyncs/s", "每秒fsync同步次数");
            put("reads/s", "每秒读取次数");
            put("writes/s", "每秒写入次数");
        }});

        put("inno_status_buffer_pool", new HashMap<String, String>() {{
            put("Buffer pool size", "缓冲池大小：指定的InnoDB缓冲池的大小，以页面数为单位。");
            put("Database pages", "数据库页面：当前在缓冲池中的InnoDB数据库页面的数量。");
            put("Dictionary memory allocated", "分配给字典内存的大小：用于存储表和索引定义的内存大小。");
            put("Free buffers", "空闲缓冲区：缓冲池中当前可用的空闲缓冲区的数量。");
            put("I/O sum[0]", "I/O统计信息：表示磁盘I/O操作的总数。");
            put("LRU len", "LRU链表长度：表示LRU链表中的页面数量，该链表用于管理最近最少使用的页面。");
            put("Modified db pages", "修改过的数据库页面：在缓冲池中被修改但尚未写入磁盘的数据库页面数量。");
            put("Old database pages", "旧的数据库页面：在LRU链表中较旧的数据库页面的数量。");
            put("Pages Random read ahead", "页面预读取速率：每秒预读取的页面数量。");
            put("Pages created", "创建的页面数：从磁盘上创建的新页面的数量。");
            put("Pages evicted without access", "页面逐出但未访问：从缓冲池中逐出而未被访问的页面数量。");
            put("Pages made young", "年轻页面：用于管理页面的使用频率和淘汰策略。");
            put("Pages not young", "非年轻页面：用于管理页面的使用频率和淘汰策略。");
            put("Pages read", "读取的页面数：从磁盘读取到缓冲池中的页面数量。");
            put("Pages reads/s", "页面读取速率：每秒从磁盘读取到缓冲池中的页面数量。");
            put("Pages writes/s", "页面写入速率：每秒从缓冲池写入到磁盘的页面数量。");
            put("Pages written", "写入的页面数：从缓冲池写入到磁盘的页面数量。");
            put("Pending reads", "挂起的读取操作：正在等待完成的读取的数量。");
            put("Pending writes: flush list", "挂起的写入操作：正在等待完成写入操作的数量。");
            put("unzip sum[0]", "解压数据：与解压缓冲池中的压缩页相关的统计信息。");
            put("unzip_LRU len", "解压数据：与解压缓冲池中的压缩页相关的统计信息。");
        }});

        put("innodb_buffer_pool_status", new HashMap<String, String>() {{
            put("NUMBERPAGESGET", "获取的页面数量：从缓冲池中获取的页面数量。");
            put("OLDDATABASEPAGES", "旧的数据库页面：在LRU链表中较旧的数据库页面的数量。");
            put("YOUNGMAKEPERTHOUSANDGETS", "每千次读取年轻页面数：每进行1000次读取操作时，被标记为年轻页面的数量。");
            put("HITRATE", "命中率：从缓冲池中成功获取的页面比例，表示读取操作是否从内存中进行。");
            put("NUMBERREADAHEADEVICTED", "读取预读被驱逐的数量：由于预读操作导致其他页面被驱逐出缓冲池的次数。");
            put("POOLSIZE", "缓冲池大小：指定的InnoDB缓冲池的大小，以页面数为单位。");
            put("PENDINGFLUSHLRU", "待刷新的LRU数量：等待刷新到磁盘的LRU链表中页面的数量。");
            put("PAGESCREATERATE", "页面创建速率：新页面创建的速率，以页面数为单位。");
            put("NUMBERPAGESWRITTEN", "写入的页面数量：从缓冲池写入磁盘的页面数量。");
            put("READAHEADRATE", "预读速率：预读操作的速率，以页面数为单位。");
            put("PAGESMADEYOUNGRATE", "年轻页面创建速率：将页面标记为年轻页面的速率，以页面数为单位。");
            put("DATABASEPAGES", "数据库页面数量：当前在缓冲池中的InnoDB数据库页面的数量。");
            put("MODIFIEDDATABASEPAGES", "已修改的数据库页面数量：在缓冲池中被修改但尚未写入磁盘的数据库页面数量。");
            put("LRUIOCURRENT", "当前LRU I/O数量：当前正在进行的LRU I/O操作的数量。");
            put("PAGESMADEYOUNG", "年轻页面数量：通过将页面标记为年轻页面而创建的页面数量。");
            put("READAHEADEVICTEDRATE", "预读驱逐速率：由于预读操作导致其他页面被驱逐出缓冲池的速率。");
            put("NUMBERPAGESCREATED", "创建的页面数量：在缓冲池中创建的页面数量。");
            put("UNCOMPRESSCURRENT", "当前解压缩数量：当前正在进行的解压缩操作的数量。");
            put("PENDINGFLUSHLIST", "待刷新列表数量：等待刷新到磁盘的页面列表的数量。");
            put("PAGESREADRATE", "读取速率：从磁盘读取到缓冲池中的页面的速率。");
            put("PAGESWRITTENRATE", "写入速率：从缓冲池写入到磁盘的页面的速率。");
            put("PENDINGDECOMPRESS", "待解压缩数量：等待解压缩的页面的数量。");
            put("PAGESNOTMADEYOUNG", "未创建为年轻页面的数量：没有被标记为年轻页面的页面数量。");
            put("NUMBERPAGESREAD", "读取的页面数量：从磁盘读取到缓冲池中的页面数量。");
            put("FREEBUFFERS", "空闲缓冲区：缓冲池中当前可用的空闲缓冲区的数量。");
            put("NUMBERPAGESREADAHEAD", "预读取的页面数量：被预读取到缓冲池中的页面数量。");
            put("POOLID", "池ID：缓冲池的标识符。");
            put("PAGESMADENOTYOUNGRATE", "非年轻页面创建速率：将页面标记为非年轻页面的速率，以页面数为单位。");
            put("LRUIOTOTAL", "总LRU I/O数量：进行的总LRU I/O操作的数量。");
            put("UNCOMPRESSTOTAL", "总解压缩数量：进行的总解压缩操作的数量。");
            put("PENDINGREADS", "待读取页面数量：等待读取到缓冲池中的页面的数量。");
            put("NOTYOUNGMAKEPERTHOUSANDGETS", "每千次读取非年轻页面数：每进行1000次读取操作时，未被标记为年轻页面的数量。");
        }});

        put("inno_status_ibuf", new HashMap<String, String>() {{
            put("Hash table size", "哈希表大小：表示InnoDB存储引擎内部用于管理索引的哈希表的大小。哈希表用于加速索引查找操作。");
            put("Ibuf free list len", "插入缓冲空闲列表长度：表示插入缓冲（Insert Buffer）中的空闲页链表的长度。它表示了可以重新使用的空闲页数量。");
            put("Ibuf seg size", "插入缓冲段大小：表示每个插入缓冲段的大小。插入缓冲由多个段组成，每个段的大小由该参数指定。");
            put("Ibuf size", "插入缓冲大小：表示整个插入缓冲的大小。它决定了InnoDB在内存中为插入操作分配的空间大小。");
            put("node heap buffer(s)", "节点堆缓冲区：表示InnoDB存储引擎用于处理B+树中节点的内存缓冲区数量。这些缓冲区用于加速索引和数据页的读写操作。");
        }});

        put("inno_status_row_operations", new HashMap<String, String>() {{
            put("Main thread", "主线程ID：表示主线程的唯一标识符。");
            put("Main thread Process", "主线程进程ID：表示主线程所属进程的唯一标识符。");
            put("Number of rows deleted", "删除的行数：表示已经从数据库中删除的行数。");
            put("Number of rows inserted", "插入的行数：表示已经插入到数据库中的行数。");
            put("Number of rows read", "读取的行数：表示已经从数据库中读取的行数。");
            put("Number of rows updated", "更新的行数：表示已经更新到数据库中的行数。");
            put("deletes/s", "每秒钟的删除操作次数：表示每秒钟执行的删除操作次数。");
            put("inserts/s", "每秒钟的插入操作次数：表示每秒钟执行的插入操作次数。");
            put("queries in queue", "查询队列中的查询数：表示当前查询队列中的查询数量。");
            put("queries inside InnoDB", "InnoDB内部正在处理的查询数：表示InnoDB存储引擎内部正在处理的查询数量。");
            put("read views open inside InnoDB", "InnoDB内部打开的读视图数：表示InnoDB存储引擎内部打开的读视图数量。");
            put("reads/s", "每秒钟的读取操作次数：表示每秒钟执行的读取操作次数。");
            put("updates/s", "每秒钟的更新操作次数：表示每秒钟执行的更新操作次数。");
            put("Hash table size", "哈希表大小：表示InnoDB存储引擎内部用于管理索引的哈希表的大小。哈希表用于加速索引查找操作。");
        }});

        put("inno_status_semaphores", new HashMap<String, String>() {{
            put("OS WAIT ARRAY INFO: reservation count", "操作系统等待数组信息：请求保留资源的次数。");
            put("OS WAIT ARRAY INFO: signal count", "操作系统等待数组信息：信号量触发的次数。");
            put("RW-excl  OS waits", "针对独占读写锁的操作系统等待次数。");
            put("RW-excl  rounds", "针对独占读写锁的轮询次数。");
            put("RW-excl spins", "针对独占读写锁的自旋次数。");
            put("RW-shared  OS waits", "针对共享读写锁的操作系统等待次数。");
            put("RW-shared  rounds", "针对共享读写锁的轮询次数。");
            put("RW-shared spins", "针对共享读写锁的自旋次数。");
            put("RW-sx OS waits", "针对多版本并发控制的操作系统等待次数。");
            put("RW-sx rounds", "针对多版本并发控制的轮询次数。");
            put("RW-sx spins", "针对多版本并发控制的自旋次数。");
            put("Spin rounds per wait: RW-excl", "平均每次独占读写锁等待期间的自旋次数。");
            put("Spin rounds per wait: RW-shared", "平均每次共享读写锁等待期间的自旋次数。");
            put("Spin rounds per wait: RW-sx", "平均每次多版本并发控制等待期间的自旋次数。");
        }});

        put("inno_status_log", new HashMap<String, String>() {{
            put("Added dirty pages up to", "已添加的脏数量：表示数据库中尚未刷新到磁盘的修改数据页的数量。这表明有大量修改的数据仍在内存中等待刷新到磁盘。");
            put("Last checkpoint at", "最后一次检查点发生在哪一页：检查点是一个操作，将内存中的脏页刷新到磁盘，并更新相关的元数据信息。此处表示最后一次检查点发生时已经刷新到磁盘的页数。");
            put("Log buffer assigned up to", "日志缓冲区分配在哪一页：日志缓冲区用于暂存事务的变更情况，以便稍后写入重做日志文件。此处表示日志缓冲区已经分配的页数。");
            put("Log buffer completed up to", "日志缓冲区完成在哪一页：表示缓冲区中的所有日志都已成功写入重做日志文件。此处的指示已经完成的日志缓冲区页数。");
            put("Log flushed up to", "日志已刷新在哪一页：表示此页之前的日志已经持久化到磁盘。即这些页的日志已被写入磁盘。");
            put("Log maximum file id is", "日志文件的最大编号：表示数据库中使用的日志文件的最大编号。每个日志文件用于存储一定数量的日志记录。");
            put("Log minimum file id is", "日志文件的最小编号：表示数据库中使用的日志文件的最小编号。");
            put("Log sequence number", "日志序列号：表示当前正在处理的日志位置。它用于确保日志的顺序性和一致性。");
            put("Log written up to", "已写入的日志页数：表示从数据库启动以来已经写入了多少页的日志。");
        }});

    }};

    public final static Map<String, String> TH = new HashMap<String, String>() {{
        put("perf_table_lock_summary", "名称<div>NAME</div>,\n" +
                "发生的次数<div>COUNT_STAR</div>,\n" +
                "等待时间（总）<div>WAIT_MS</div>,\n" +
                "最小等待时间<div>MIN_WAIT_MS</div>,\n" +
                "最大等待时间<div>MAX_WAIT_MS</div>,\n" +
                "平均等待时间<div>AVG_WAIT_MS</div>,\n" +
                "读取操作次数<div>COUNT_READ</div>,\n" +
                "读取等待时间（总）<div>READ_MS</div>,\n" +
                "最小读取等待时间<div>MIN_READ_MS</div>,\n" +
                "最大读取等待时间<div>MAX_READ_MS</div>,\n" +
                "平均读取等待时间<div>AVG_READ_MS</div>,\n" +
                "写入操作次数<div>COUNT_WRITE</div>,\n" +
                "写入等待时间（总）<div>WRITE_MS</div>,\n" +
                "最小写入等待时间<div>MIN_WRITE_MS</div>,\n" +
                "最大写入等待时间<div>MAX_WRITE_MS</div>,\n" +
                "平均写入等待时间<div>AVG_WRITE_MS</div>,\n" +
                "普通读取操作次数<div>COUNT_READ_NORMAL</div>,\n" +
                "普通读取等待时间（总）<div>R_NORMAL_MS</div>,\n" +
                "最小普通读取等待时间<div>MIN_R_NORMAL_MS</div>,\n" +
                "最大普通读取等待时间<div>MAX_R_NORMAL_MS</div>,\n" +
                "平均普通读取等待时间<div>AVG_R_NORMAL_MS</div>,\n" +
                "带共享锁的读取操作次数<div>COUNT_READ_WITH_SHARED_LOCKS</div>,\n" +
                "带共享锁的读取等待时间（总）<div>R_SHARED_LOCKS_MS</div>,\n" +
                "最小带共享锁的读取等待时间<div>MIN_R_SHARED_LOCKS_MS</div>,\n" +
                "最大带共享锁的读取等待时间<div>MAX_R_SHARED_LOCKS_MS</div>,\n" +
                "平均带共享锁的读取等待时间<div>AVG_R_SHARED_LOCKS_MS</div>,\n" +
                "高优先级读取操作次数<div>COUNT_READ_HIGH_PRIORITY</div>,\n" +
                "高优先级读取等待时间（总）<div>R_HIGH_PRIORITY_MS</div>,\n" +
                "最小高优先级读取等待时间<div>MIN_R_HIGH_PRIORITY_MS</div>,\n" +
                "最大高优先级读取等待时间<div>MAX_R_HIGH_PRIORITY_MS</div>,\n" +
                "平均高优先级读取等待时间<div>AVG_R_HIGH_PRIORITY_MS</div>,\n" +
                "没有插入操作的读取操作次数<div>COUNT_READ_NO_INSERT</div>,\n" +
                "没有插入操作的读取等待时间（总）<div>R_NO_INSERT_MS</div>,\n" +
                "最小没有插入操作的读取等待时间<div>MIN_R_NO_INSERT_MS</div>,\n" +
                "最大没有插入操作的读取等待时间<div>MAX_R_NO_INSERT_MS</div>,\n" +
                "平均没有插入操作的读取等待时间<div>AVG_R_NO_INSERT_MS</div>,\n" +
                "外部读取操作次数<div>COUNT_READ_EXTERNAL</div>,\n" +
                "外部读取等待时间（总）<div>R_EXTERNAL_MS</div>,\n" +
                "最小外部读取等待时间<div>MIN_R_EXTERNAL_MS</div>,\n" +
                "最大外部读取等待时间<div>MAX_R_EXTERNAL_MS</div>,\n" +
                "平均外部读取等待时间<div>AVG_R_EXTERNAL_MS</div>,\n" +
                "允许写入操作次数<div>COUNT_WRITE_ALLOW_WRITE</div>,\n" +
                "允许写入等待时间（总）<div>W_ALLOW_W_MS</div>,\n" +
                "最小允许写入等待时间<div>MIN_W_ALLOW_W_MS</div>,\n" +
                "最大允许写入等待时间<div>MAX_W_ALLOW_W_MS</div>,\n" +
                "平均允许写入等待时间<div>AVG_W_ALLOW_W_MS</div>,\n" +
                "并发插入操作次数<div>COUNT_WRITE_CONCURRENT_INSERT</div>,\n" +
                "并发插入等待时间（总）<div>W_CONC_INSERT_MS</div>,\n" +
                "最小并发插入等待时间<div>MIN_W_CONC_INSERT_MS</div>,\n" +
                "最大并发插入等待时间<div>MAX_W_CONC_INSERT_MS</div>,\n" +
                "平均并发插入等待时间<div>AVG_W_CONC_INSERT_MS</div>,\n" +
                "低优先级写入操作次数<div>COUNT_WRITE_LOW_PRIORITY</div>,\n" +
                "低优先级写入等待时间（总）<div>W_LOW_PRIORITY_MS</div>,\n" +
                "最小低优先级写入等待时间<div>MIN_W_LOW_PRIORITY_MS</div>,\n" +
                "最大低优先级写入等待时间<div>MAX_W_LOW_PRIORITY_MS</div>,\n" +
                "平均低优先级写入等待时间<div>AVG_W_LOW_PRIORITY_MS</div>,\n" +
                "普通写入操作次数<div>COUNT_WRITE_NORMAL</div>,\n" +
                "普通写入等待时间（总）<div>W_NORMAL_MS</div>,\n" +
                "最小普通写入等待时间<div>MIN_W_NORMAL_MS</div>,\n" +
                "最大普通写入等待时间<div>MAX_W_NORMAL_MS</div>,\n" +
                "平均普通写入等待时间<div>AVG_W_NORMAL_MS</div>,\n" +
                "外部写入操作次数<div>COUNT_WRITE_EXTERNAL</div>,\n" +
                "外部写入等待时间（总）<div>W_EXTERNAL_MS</div>,\n" +
                "最小外部写入等待时间<div>MIN_W_EXTERNAL_MS</div>,\n" +
                "最大外部写入等待时间<div>MAX_W_EXTERNAL_MS</div>,\n" +
                "平均外部写入等待时间<div>AVG_W_EXTERNAL_MS</div>");

    }};
}
