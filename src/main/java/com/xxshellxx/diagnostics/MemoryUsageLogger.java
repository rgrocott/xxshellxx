package com.xxshellxx.diagnostics;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryUsageLogger {

	private static final Logger logger = LoggerFactory.getLogger(MemoryUsageLogger.class);

	public static void logMemoryUsage() {
		if ("true".equals(System.getProperty("xxshellxx.debug.memory"))) {

			//ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
			logger.debug("Heap: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
//			logger.debug("NonHeap: " + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
//			List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
//			for (MemoryPoolMXBean bean : beans) {
//				logger.debug(String.format("%s %s", bean.getName(), bean.getUsage()));
//			}
//
//			for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
//				logger.debug(String.format("%s %s %s", bean.getName(), bean.getCollectionCount(), bean.getCollectionTime()));
//			}
		}
	}

}
