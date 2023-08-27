const data = {
    subMenu: [],
    menu: [
        {
            name: '实时信息',
            children: [{
                name: '进程 Process'
            }
                , {
                name: '全局状态 Global Status'
            }
                , {
                name: '全局变量 Global Variables'
            }
            ]
        }
        , {
            name: '集群信息',
            children: [{
                name: ''
            }, {
                name: ''
            }]
        }
        , {
            name: 'InnoDB 信息',
            children: [{
                name: '摘要',
                id: 'inno_status_summary',
                type: 2
            }
                , {
                name: '文件 IO',
                id: 'inno_status_file_io',
                type: 2,
                note: 'InnoDB 引擎进行的文件 IO 操作的统计信息，这些统计信息是瞬时值。'
            }
                , {
                name: '缓冲池及内存',
                id: 'inno_status_buffer_pool',
                type: 2,
                note: 'InnoDB 缓冲池是 InnoDB 引擎使用的一个重要的内存区域，用于缓存数据库表中的数据和索引。它允许快速读取和写入数据，提高数据库的性能。'
            }
                , {
                name: '缓冲池统计',
                id: 'innodb_buffer_pool_status',
                type: 2,
                th: 'Name,Count,Time Elapsed',
                note: '缓冲池是 InnoDB 存储引擎中的一个关键组件，用于缓存表和索引的数据页。Buffer Pool Statistics 通过将热门的数据页放在内存中，可以提高查询性能和响应速度。'
            }
                , {
                name: '插入缓冲',
                id: 'inno_status_ibuf',
                type: 2,
                note: '插入缓冲（Insert Buffer）是一个特殊的内存区域，用于临时存储待插入的数据页。'
            }

                , {
                name: '行操作',
                id: 'inno_status_row_operations',
                type: 2,
                note: 'InnoDB 执行行的增删改等操作（Row Operations）的情况。'
            }
                , {
                name: '信号量',
                id: 'inno_status_semaphores',
                type: 2,
                note: '信号量 Semaphores 用来同步数据库操作，控制并发访问和保护共享资源的机制，确保数据的一致性和完整性。这些统计数据可以帮助评估系统中的资源竞争情况和并发访问的效率，以便进行性能优化和调整。'
            }
                , {
                name: '事务 Tx',
                id: 'inno_status_txs',
                type: 1
            }
                , {
                name: '死锁 Deadlocks',
                id: 'inno_status_deadlocks',
                type: 1
            }, {
                name: '互斥锁',
                id: 'mysql_innodb_mutex',
                type: 1,
                note: '互斥锁（Mutex）是 InnoDB 存储引擎中用于实现并发控制的一种机制。Mutex 是一种同步原语，用于确保多个线程对共享资源的访问不会发生冲突。'
                    + '这些是关于InnoDB引擎的行级锁（row-level lock）的等待统计信息。每一行表示不同的锁类型和对应的等待次数。',
                th: '类型,名称,状态'
            }

                , {
                name: '日志',
                id: 'inno_status_log',
                type: 2,
                note: '关于数据库事务日志的详细信息，包括脏页数量、日志缓冲区情况、日志刷新状态和日志文件信息。它们对于监测和调整数据库的性能和可靠性非常有用。'
            }
                , {
                name: 'InnoDB 指标',
                id: 'innodb_metrics',
                type: 1,
                note: 'InnoDB 所有指标（Metrics）一览。',
                th: 'Name,Count,Time Elapsed'
            }
            ]
        }

        , {
            name: '性能分析',
            children: [
                {
                    name: '等待的事件',
                    id: 'perf_events_waits_current',
                    type: 1,
                    note: '在MySQL中不同的等待事件上花费的时间，以便进一步优化和调整系统性能。',
                    th: `<span title="">进程 ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">线程ID<div>THREAD_ID</div></span>,
                    <span title="正在执行操作的数据库">数据库<div>PROCESSLIST_DB</div></span>,
                    <span title="正在执行的命令">执行的命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="进程已经运行的时间">运行时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="最后一次等待的事件名称">事件名称<div>EVENT_NAME</div></span>,
                    <span title="最后一次等待的事件持续时间（毫秒）">事件等待时间<div>LAST_WAIT_MS</div></span>,
                    <span title="引起等待的资源类型">资源类型<div>SOURCE</div></span>,
                    <span title="正在访问的对象的名称和索引">访问对象<div>OBJECT</div></span>,
                    <span title="正在进行的操作">进行的操作<div>OPERATION</div></span>,
                    <span title="">涉及的字节数<div>NUMBER_OF_BYTES</div></span>,
                    <span title="">已检查的行数<div>ROWS_EXAMINED</div></span>,
                    <span title="">发送的行数<div>ROWS_SENT</div></span>,
                    <span title="">影响的行数<div>ROWS_AFFECTED</div></span>,
                    <span title="创建的临时表数量">临时表数<div>CREATED_TMP_TABLES</div></span>,
                    <span title="在磁盘上创建的临时表数量">磁盘临时表数<div>CREATED_TMP_DISK_TABLES</div></span>,
                    <span title="是否执行了全表扫描">全表扫描？<div>FULL_SCAN</div></span>,
                    <span title="最后一次语句的延迟时间（毫秒）">最后延迟时间<div>LAST_STATEMENT_LATENCY</div></span>,
                    <span title="最后一次语句的锁定时间（毫秒）">最后锁定时间<div>LOCK_LATENCY</div></span>,
                    <span title="">用户信息<div>USER</div></span>,
                    <span title="正在执行的SQL语句">SQL语句<div>SQL_TEXT</div></span>`
                }
                , {
                    name: '等待的事件统计',
                    id: 'perf_events_waits_summary',
                    type: 1,
                    note: '在MySQL中不同的等待事件上花费的时间，以便进一步优化和调整系统性能。',
                    th: '事件名称,事件发生次数,等待毫秒数'
                }

                , {
                    name: 'mutex',
                    id: 'perf_mutex',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
                , {
                    name: 'RW Locks',
                    id: 'perf_rwlock',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
                , {
                    name: '元数据锁',
                    id: 'metadata_lock',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
                , {
                    name: '内存情况',
                    id: 'perf_memory_summary_global_by_event_name',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
                , {
                    name: '根据线程分析内存',
                    id: 'perf_memory_summary_by_thread_by_event_name',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
                , {
                    name: 'Top Queries',
                    id: 'digests_wait_time',
                    type: 1,
                    note: 'MySQL 每执行一条 DML、DDL 语句时都会申请元数据锁，DML 操作需要 Metadata 读锁，DDL 操作需要 Metadata 写锁。当前提供正在运行的线程和元数据锁的详细信息。',
                    th: `
                    <span title="">进程列表ID<div>PROCESSLIST_ID</div></span>,
                    <span title="">进程列表用户<div>PROCESSLIST_USER</div></span>,
                    <span title="">进程列表主机<div>PROCESSLIST_HOST</div></span>,
                    <span title="">进程列表命令<div>PROCESSLIST_COMMAND</div></span>,
                    <span title="">进程列表状态<div>PROCESSLIST_STATE</div></span>,
                    <span title="">进程列表时间<div>PROCESSLIST_TIME</div></span>,
                    <span title="元数据锁对象类型">锁对象类型<div>object_type</div></span>,
                    <span title="元数据锁对象模式">锁对象模式<div>object_schema</div></span>,
                    <span title="元数据锁对象名称">锁对象名称<div>object_name</div></span>,
                    <span title="">元数据锁类型<div>LOCK_TYPE</div></span>,
                    <span title="">元数据锁状态<div>LOCK_STATUS</div></span>,
                    <span title="">元数据锁持续时间<div>LOCK_DURATION</div></span>,
                    <span title="">进程列表信息<div>PROCESSLIST_INFO</div></span>`
                }
            ]
        }
        
        , {
            name: 'Table/File IO',
            children: [
                {
                    name: '表/索引访问情况',
                    id: 'perf_objects_summary',
                    type: 1,
                    note: '提供全局对象的汇总统计信息。它可以帮助用户了解数据库中不同类型对象的使用情况和性能状况。',
                    th: `<span title="通过查看对象的类型，可以了解数据库中不同类型对象的分布情况，例如表、索引、存储过程等">对 象<div>NAME</div></span>,
                    <span title="表示每种对象类型在性能统计中出现的次数，即被访问或操作的频率。可以用于确定哪些对象更常被使用">记录数<div>COUNT_STAR</div></span>,
                    <span title="表示每种对象类型在所有访问或操作过程中发生的总等待时间。可以反映对象的使用繁忙程度和可能存在的性能瓶颈">等待时间<div>WAIT_MS</div></span>,
                    <span title="">最小等待时间<div>WAIT_MS</div></span>,
                    <span title="">最大等待时间<div>MIN_WAIT_MS</div></span>,
                    <span title="">平均等待时间<div>AVG_WAIT_MS</div></span>`
                }
                , {
                    name: '表 I/O',
                    id: 'perf_table_io_summary',
                    type: 1,
                    note: '提供基于表的访问情况。通过分析该表的数据，可以得出哪些表在执行期间经历了较长的 I/O 等待时间，识别出引发等待的具体表。',
                    th: `名称<div>NAME</div>,
                    总事件数<div>COUNT_STAR</div>,
                    等待总时间<div>WAIT_MS</div>,
                    最小等待时间<div>MIN_WAIT_MS</div>,
                    最大等待时间<div>MAX_WAIT_MS</div>,
                    平均等待时间<div>AVG_WAIT_MS</div>,
                    读取次数<div>COUNT_READ</div>,
                    读取总时间<div>READ_MS</div>,
                    最小读取时间<div>MIN_READ_MS</div>,
                    平均读取时间<div>AVG_READ_MS</div>,
                    最大读取时间<div>MAX_READ_MS</div>,
                    写入次数<div>COUNT_WRITE</div>,
                    写入总时间<div>WRITE_MS</div>,
                    最小写入时间<div>MIN_WRITE_MS</div>,
                    平均写入时间<div>AVG_WRITE_MS</div>,
                    最大写入时间<div>MAX_WRITE_MS</div>,
                    获取次数<div>COUNT_FETCH</div>,
                    获取总时间<div>FETCH_MS</div>,
                    最小获取时间<div>MIN_FETCH_MS</div>,
                    平均获取时间<div>AVG_FETCH_MS</div>,
                    最大获取时间<div>MAX_FETCH_MS</div>,
                    插入次数<div>COUNT_INSERT</div>,
                    插入总时间<div>INSERT_MS</div>,
                    最小插入时间<div>MIN_INSERT_MS</div>,
                    平均插入时间<div>AVG_INSERT_MS</div>,
                    最大插入时间<div>MAX_INSERT_MS</div>,
                    更新次数<div>COUNT_UPDATE</div>,
                    更新总时间<div>UPDATE_MS</div>,
                    最小更新时间<div>MIN_UPDATE_MS</div>,
                    平均更新时间<div>AVG_UPDATE_MS</div>,
                    最大更新时间<div>MAX_UPDATE_MS</div>,
                    删除次数<div>COUNT_DELETE</div>,
                    删除总时间<div>DELETE_MS</div>,
                    最小删除时间<div>MIN_DELETE_MS</div>,
                    平均删除时间<div>AVG_DELETE_MS</div>,
                    最大删除时间<div>MAX_DELETE_MS</div>`
                }
                , {
                    name: '文件 I/O',
                    id: 'perf_filesum_inst',
                    type: 1,
                    note: '提供文件访问的情况，包括读取和写入的数量、耗时以及字节数等。',
                    th: `
                        <span title="">文件名<div>FILE_NAME</div></span>,
                        <span title="">事件名<div>EVENT_NAME</div></span>,
                        <span title="OBJECT_INSTANCE_BEGIN">对象实例开始<div>INSTANCE_BEGIN</div></span>,
                        <span title="">计数总数<div>COUNT</div></span>,
                        <span title="">等待时间（毫秒）<div>TIMER_WAIT_MS</div></span>,
                        <span title="">读取计数<div>COUNT_READ</div></span>,
                        <span title="">读取时间（毫秒）<div>TIMER_READ_MS</div></span>,
                        <span title="">读取字节数<div>BYTES_READ</div></span>,
                        <span title="">写入计数<div>COUNT_WRITE</div></span>,
                        <span title="">写入时间（毫秒）<div>TIMER_WRITE_MS</div></span>,
                        <span title="">写入字节数<div>BYTES_WRITE</div></span>,
                        <span title="">其他操作计数<div>COUNT_MISC</div></span>,
                        <span title="">其他操作时间（毫秒）<div>TIMER_MISC_MS</div></span>`
                }
                , {
                    name: '索引 I/O',
                    id: 'perf_index_io_summary',
                    type: 1,
                    note: '用于统计数据库表在索引使用上的 I/O 等待情况的摘要信息。通过分析该表的数据，可以得出哪些表的查询语句在执行时因为索引使用不当导致了较长的 I/O 等待时间。'
                        + '这可以帮助开发人员或数据库管理员进行性能优化，例如优化查询语句、创建合适的索引或重新设计数据库结构，以提升数据库的性能和响应速度',
                    th: `
                    名称<div>NAME</div>,
                   总事件数<div>COUNT_STAR</div>,
                   等待总时间<div>WAIT_MS</div>,
                   最小等待时间<div>MIN_WAIT_MS</div>,
                   最大等待时间<div>MAX_WAIT_MS</div>,
                   平均等待时间<div>AVG_WAIT_MS</div>,
                   读取次数<div>COUNT_READ</div>,
                   读取总时间<div>READ_MS</div>,
                   最小读取时间<div>MIN_READ_MS</div>,
                   平均读取时间<div>AVG_READ_MS</div>,
                   最大读取时间<div>MAX_READ_MS</div>,
                   写入次数<div>COUNT_WRITE</div>,
                   写入总时间<div>WRITE_MS</div>,
                   最小写入时间<div>MIN_WRITE_MS</div>,
                   平均写入时间<div>AVG_WRITE_MS</div>,
                   最大写入时间<div>MAX_WRITE_MS</div>,
                   获取次数<div>COUNT_FETCH</div>,
                   获取总时间<div>FETCH_MS</div>,
                   最小获取时间<div>MIN_FETCH_MS</div>,
                   平均获取时间<div>AVG_FETCH_MS</div>,
                   最大获取时间<div>MAX_FETCH_MS</div>,
                   插入次数<div>COUNT_INSERT</div>,
                   插入总时间<div>INSERT_MS</div>,
                   最小插入时间<div>MIN_INSERT_MS</div>,
                   平均插入时间<div>AVG_INSERT_MS</div>,
                   最大插入时间<div>MAX_INSERT_MS</div>,
                   更新次数<div>COUNT_UPDATE</div>,
                   更新总时间<div>UPDATE_MS</div>,
                   最小更新时间<div>MIN_UPDATE_MS</div>,
                   平均更新时间<div>AVG_UPDATE_MS</div>,
                   最大更新时间<div>MAX_UPDATE_MS</div>,
                   删除次数<div>COUNT_DELETE</div>,
                   删除总时间<div>DELETE_MS</div>,
                   最小删除时间<div>MIN_DELETE_MS</div>,
                   平均删除时间<div>AVG_DELETE_MS</div>,
                   最大删除时间<div>MAX_DELETE_MS</div>`
                }
                , {
                    name: '锁表情况',
                    id: 'perf_table_lock_summary',
                    type: 1,
                    note: '提供了关于表锁等待的汇总信息，包括锁等待次数、等待时间以及各种类型的锁等待计数和持续时间。可以了解哪些表发生了锁等待的信息，以及锁等待的持续时间和频率等指标。这对于识别和优化数据库中出现的锁竞争问题非常有用',
                    th: ``
                }
            ]
        }
   
        , {
            name: '用户信息',
            children: [{
                name: ''
            }, {
                name: ''
            }]
        }
     
        , {
            name: 'SQL 调优',
            children: [{
                name: ''
            }, {
                name: ''
            }]
        }
    ]
};