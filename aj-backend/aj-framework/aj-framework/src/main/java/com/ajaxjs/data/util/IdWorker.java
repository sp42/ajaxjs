package com.ajaxjs.data.util;

/**
 * 雪花算法
 */
public class IdWorker {
    // 下面两个每个5位，加起来就是10位的工作机器id
    private final long workerId;    //工作id
    private final long datacenterId;   //数据id
    //12位的序列号
    private long sequence;

    public IdWorker(long workerId, long datacenterId, long sequence) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0)
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));

        if (datacenterId > maxDatacenterId || datacenterId < 0)
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    //初始时间戳
    private final long twepoch = 1288834974657L;

    //长度为5位
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    //最大值
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final  long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    //序列号id长度
    private final long sequenceBits = 12L;
    //序列号最大值
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    //工作id需要左移的位数，12位
    private final long workerIdShift = sequenceBits;
    //数据id需要左移位数 12+5=17位
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    //时间戳需要左移位数 12+5+5=22位
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    //上次时间戳，初始值为负数
    private long lastTimestamp = -1L;

    //下一个ID生成算法
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;

            if (sequence == 0)
                timestamp = SnowflakeId.tilNextMillis(lastTimestamp);
        } else
            sequence = 0;

        // 将上次时间戳值刷新
        lastTimestamp = timestamp;

        /*
         * 返回结果：
         * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
         * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
         * (workerId << workerIdShift) 表示将工作id左移相应位数
         * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
         * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
         */
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }


    private final static IdWorker INSTANCE = new IdWorker(1, 1, 1);

    /**
     * 生成 id
     *
     * @return 雪花 id
     */
    public static synchronized long get() {
        return INSTANCE.nextId();
    }
}