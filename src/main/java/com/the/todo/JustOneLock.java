package com.the.todo;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class JustOneLock {
	private String appName;

	FileLock lock;

	FileChannel channel;

	public JustOneLock(String appName) {
		this.appName = appName;
	}

	public boolean isAppActive() throws Exception {
		File file = new File(System.getProperty("user.home"), appName + ".tmp");
		channel = new RandomAccessFile(file, "rw").getChannel();

		lock = channel.tryLock();
		if (lock == null) {
			channel.close();
			return true;
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					lock.release();
					channel.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return false;
	}
}
