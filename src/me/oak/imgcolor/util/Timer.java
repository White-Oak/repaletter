package me.oak.imgcolor.util;

import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 *
 * @author White Oak
 */
public class Timer {

    private long start;

    public static long time(Runnable runnable) {
	long start = System.currentTimeMillis();
	runnable.run();
	return System.currentTimeMillis() - start;
    }

    @SneakyThrows
    public static <T> Result<T> time(Callable<T> callable) {
	long start = System.currentTimeMillis();
	T result = callable.call();
	long total = System.currentTimeMillis() - start;
	return new Result<>(result, total);
    }

    public void start() {
	start = System.currentTimeMillis();
    }

    public long total() {
	return System.currentTimeMillis() - start;
    }

    @RequiredArgsConstructor
    public static class Result<T> {

	public final T result;
	public final long totalTime;
    }
}
