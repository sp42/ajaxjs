package com.ajaxjs.sql;

/**
 * https://www.cnblogs.com/yangzhilong/p/10290862.html
 * 
 * @author 
 *
 */
public class SimpleSnowflakeId {

	/**
	 * 时间起始标记点，作为基准，一般取系统的最近时间 此处以2018-01-01为基准时间
	 */
	private final long epoch = 1514736000000L;
	
	/**
	 * 机器标识位数
	 */
	private final long workerIdBits = 4L;
	
	/**
	 * 毫秒内自增位
	 */
	private final long sequenceBits = 12L;
	
	/**
	 * 机器ID最大值:16
	 */
	private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;

	private final long workerIdShift = this.sequenceBits;
	private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
	private final long sequenceMask = -1L ^ -1L << this.sequenceBits;

	private final long workerId;

	/**
	 * 并发控制
	 */
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	public SimpleSnowflakeId(long workerId) {
		if (workerId > this.maxWorkerId || workerId < 0) 
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
		
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = System.currentTimeMillis();
		if (lastTimestamp == timestamp) {
			// 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环);
			// 对新的timestamp，sequence从0开始
			this.sequence = this.sequence + 1 & this.sequenceMask;
			
			if (this.sequence == 0) {
				// 重新生成timestamp
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else 
			this.sequence = 0;
		

		if (timestamp < lastTimestamp) 
			throw new RuntimeException(
					String.format("clock moved backwards.Refusing to generate id for %d milliseconds",
							this.lastTimestamp - timestamp));

		lastTimestamp = timestamp;
		
		return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
	}

	/**
	 * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp)
			timestamp = System.currentTimeMillis();

		return timestamp;
	}
	
	/**
	 * 生成 id
	 * 
	 * @return
	 */
	public static long get() {
		return new SimpleSnowflakeId(1L).nextId();
	}

}
