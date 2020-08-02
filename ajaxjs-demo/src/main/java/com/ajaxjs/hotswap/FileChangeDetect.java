package com.ajaxjs.hotswap;

import java.io.File;

public class FileChangeDetect {
	private final File[] roots;
	private long lastModitySum = 0;

	public FileChangeDetect(File[] roots) {
		this.roots = roots;
		detectChange();
	}

	private long calculateModitySum(File file) {
		if (file.isDirectory()) {
			long sum = 0;
			for (File each : file.listFiles())
				sum += calculateModitySum(each);

			return sum;
		} else
			return file.lastModified();
	}

	public boolean detectChange() {
		System.out.println("::" + lastModitySum);

		long newModitySum = 0;

		for (File root : roots)
			newModitySum = calculateModitySum(root);

		if (newModitySum == lastModitySum)
			return false;
		else {
			lastModitySum = newModitySum;
			return true;
		}
	}
}