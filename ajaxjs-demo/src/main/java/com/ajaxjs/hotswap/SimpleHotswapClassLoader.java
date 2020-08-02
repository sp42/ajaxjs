package com.ajaxjs.hotswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHotswapClassLoader extends URLClassLoader {
	private static final ParalLock PARAL_LOCK = new ParalLock();
	private final File reloadPathFile;
	private ClassLoader parent;
	private ConcurrentHashMap<String, Class<?>> classMap = new ConcurrentHashMap<>();
	private String[] reloadPackages = new String[0];
	private String[] reloadPackageForClassFiless = new String[0];
	private String[] excludePackages = new String[0];

	public SimpleHotswapClassLoader(String reloadPath) {
		super(new URL[0]);
		parent = this.getClass().getClassLoader();
		reloadPathFile = new File(reloadPath);
	}

	public void setReloadPackages(String... reloadPackages) {
		this.reloadPackages = reloadPackages;
		reloadPackageForClassFiless = new String[reloadPackages.length];

		for (int i = 0; i < reloadPackages.length; i++)
			reloadPackageForClassFiless[i] = reloadPackages[i].replace(".", "/");
	}

	public void setExcludePackages(String... excludePackages) {
		this.excludePackages = excludePackages;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (classMap.containsKey(name))
			return classMap.get(name);

		synchronized (PARAL_LOCK.getLock(name)) {
			for (String reloadPackage : reloadPackages) {
				if (name.startsWith(reloadPackage)) {
					boolean canLoad = true;

					for (String excludePackage : excludePackages) {
						if (name.startsWith(excludePackage)) {
							canLoad = false;
							break;
						}
					}

					if (canLoad == false)
						continue;

					FileInputStream inputStream = null;
					
					try {
						File file = new File(reloadPathFile, name.replace(".", "/") + ".class");
						inputStream = new FileInputStream(file);
						byte[] src = new byte[inputStream.available()];
						inputStream.read(src);
						// inputStream.close();
						Class<?> c = defineClass(name, src, 0, src.length);
						classMap.put(name, c);
						
						return c;
					} catch (Exception e) {
						// ReflectUtil.throwException(e);
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}

			// First, check if the class has already been loaded
			Class<?> c = findLoadedClass(name);

			if (c == null) {
				try {
					c = parent.loadClass(name);
				} catch (ClassNotFoundException e) {
				}

				if (c == null)
					c = findClass(name);
			}
			
			classMap.put(name, c);

			return c;
		}
	}

	@Override
	public URL[] getURLs() {
		return ((URLClassLoader) parent).getURLs();
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return parent.getResources(name);
	}

	@Override
	public URL getResource(String name) {
		for (String reloadPackageForClass : reloadPackageForClassFiless) {
			if (name.startsWith(reloadPackageForClass) && name.endsWith(".class")) {
				File file = new File(reloadPathFile, name);
				
				try {
					return file.toURI().toURL();
				} catch (MalformedURLException e) {
					return parent.getResource(name);
				}
			}
		}
		
		return parent.getResource(name);
	}
}
