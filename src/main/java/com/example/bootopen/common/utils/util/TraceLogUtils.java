package com.example.bootopen.common.utils.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;


/**
 * 日志跟踪 【slf4j】
 * @author chaijd
 */
public class TraceLogUtils {
	private static final String TRACE_KEY = "TraceID";
	private static final int TRACE_LENGTH = 20;

	public static void beginTrace() {
		String traceId = RandomStringUtils.randomAlphanumeric(TRACE_LENGTH);
		MDC.put(TRACE_KEY, traceId);
	}

	public static void beginTrace(String traceId) {
		MDC.put(TRACE_KEY, traceId);
	}

	public static void endTrace() {
		MDC.remove(TRACE_KEY);
	}

	public static String getTraceId() {
		return ((String) MDC.get(TRACE_KEY));
	}
}