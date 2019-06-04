package com.ajaxjs.javatools.task.timeout;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DelayItem<T> implements Delayed {
	private static final long NANO_ORIGIN = System.nanoTime(); // 基准时间
	private static final AtomicLong sequencer = new AtomicLong(0); //
	private final long sequenceNumber; // FIFO, 保证不会有两个以上的延迟项同时被执行, 先开始延迟的优先执行
	private long time;
	private final T item;
	private final long timeout;

	public DelayItem(T submit, int minutes) {
		this(submit, TimeUnit.NANOSECONDS.convert(minutes, TimeUnit.MINUTES));
	}

	public DelayItem(T submit, long timeout) {
		this.timeout = timeout;
		this.time = now() + timeout;
		this.item = submit;
		this.sequenceNumber = sequencer.getAndIncrement();
	}

	public void resetTime() {
		this.time = now() + timeout;
	}

	final static long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	public T getItem() {
		return this.item;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(time - now(), TimeUnit.NANOSECONDS);
	}

	@Override
	public int compareTo(Delayed other) {
		if (other == this) // compare zero ONLY if same object
			return 0;
		if (other instanceof DelayItem) {
			DelayItem<?> x = (DelayItem<?>) other;
			long diff = time - x.time;
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else if (sequenceNumber < x.sequenceNumber)
				return -1;
			else
				return 1;
		}
		long d = (getDelay(TimeUnit.NANOSECONDS) - other
				.getDelay(TimeUnit.NANOSECONDS));

		return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
	}
}