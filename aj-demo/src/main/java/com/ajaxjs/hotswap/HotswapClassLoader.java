package com.ajaxjs.hotswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HotswapClassLoader extends ClassLoader {
	private static final Map<String, classInfo> classInfos = new HashMap<>();

	private static final ParalLock PARAL_LOCK = new ParalLock();

	private final ClassLoader parent;

	// 需要被重载的Class
	private final ConcurrentHashMap<String, Class<?>> reloadClassMap = new ConcurrentHashMap<>();

	// 不需要被重载的，不可变的 Class。无论是自定义的还是系统的，都在这个地方
	private final ConcurrentHashMap<String, Class<?>> immutableClassMap = new ConcurrentHashMap<>();

	// 排除在外的路径，该路径下的类不会进入自定义的加载流程
	private final Set<String> excludeClasses = new HashSet<>();
	private String[] reloadPackages = new String[0];
	private File[] reloadPaths = new File[0];

	public HotswapClassLoader() {
		parent = Thread.currentThread().getContextClassLoader();
	}

	public HotswapClassLoader(HotswapClassLoader classLoader) {
		parent = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
	}

	public static void addLibPath(String libPath) {
		File[] files = new File(libPath).listFiles();

		for (File file : files) {
			if (file.isDirectory() == false && file.getName().endsWith(".jar")) {
				JarFile jarFile = null;

				try {
					jarFile = new JarFile(file);
				} catch (IOException e) {
					// ReflectUtil.throwException(e);
					return;
				}

				Enumeration<JarEntry> entries = jarFile.entries();

				while (entries.hasMoreElements()) {
					JarEntry jarEntry = entries.nextElement();
					String entryName = jarEntry.getName();

					if (jarEntry.isDirectory() == false && entryName.endsWith(".class")) {
						String className = entryName.substring(0, entryName.length() - 6);
						className = className.replaceAll("/", ".");
						classInfos.put(className, new classInfo(jarEntry, file.getAbsolutePath()));
					}
				}

				try {
					jarFile.close();
				} catch (IOException e) {
					// ReflectUtil.throwException(e);
				}
			}
		}
	}

	public void setReloadPaths(String... paths) {
		List<File> files = new LinkedList<>();

		for (String each : paths)
			files.add(new File(each));

		reloadPaths = files.toArray(new File[files.size()]);
	}

	public void setExcludeClasses(String... excludeClasses) {
		for (String each : excludeClasses)
			this.excludeClasses.add(each);

	}

	public void setReloadPackages(String... packages) {
		reloadPackages = packages;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> c = null;

		c = immutableClassMap.get(name);
		if (c != null)
			return c;

		c = reloadClassMap.get(name);

		if (c != null)
			return c;

		synchronized (PARAL_LOCK.getLock(name)) {
			boolean exclude = false;

			if (excludeClasses.contains(name))
				exclude = true;

			if (exclude == false) {
				for (String each : reloadPackages) {
					if (name.startsWith(each)) {
						try {
							if (classInfos.containsKey(name)) {
								classInfo cInfo = classInfos.get(name);
								JarFile jarFile = null;
								InputStream inputStream = null;

								try {
									jarFile = new JarFile(cInfo.jarPath);
									inputStream = jarFile.getInputStream(cInfo.jarEntry);
									byte[] src = new byte[inputStream.available()];
									inputStream.read(src);
									// inputStream.close();
									// jarFile.close();
									c = defineClass(name, src, 0, src.length);
									reloadClassMap.put(name, c);
									return c;
								} finally {
									if (inputStream != null)
										inputStream.close();

									if (jarFile != null)
										jarFile.close();
								}
							}
							for (File pathFile : reloadPaths) {
								File file = new File(pathFile, name.replace(".", "/") + ".class");
								
								if (file.exists()) {
									FileInputStream inputStream = null;
									try {
										inputStream = new FileInputStream(file);
										byte[] src = new byte[inputStream.available()];
										inputStream.read(src);
										// inputStream.close();
										c = defineClass(name, src, 0, src.length);
										reloadClassMap.put(name, c);

										return c;
									} finally {
										if (inputStream != null)
											inputStream.close();

									}
								}
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
			if (c == null) {
				c = parent.loadClass(name);
				if (c == null)
					throw new ClassNotFoundException(name);

				resolveClass(c);
			}
			immutableClassMap.put(name, c);

			return c;
		}
	}

	static class classInfo {
		private final JarEntry jarEntry;
		private final String jarPath;

		public classInfo(JarEntry jarEntry, String jarPath) {
			this.jarEntry = jarEntry;
			this.jarPath = jarPath;
		}
	}
}
