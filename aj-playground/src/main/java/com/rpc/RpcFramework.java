package com.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * RpcFramework
 * 
 * @author william.liangf https://www.iteye.com/blog/javatar-1123915
 */
public class RpcFramework {
	/**
	 * 暴露服务
	 * 
	 * @param service 服务实现
	 * @param port    服务端口
	 * @throws Exception
	 */
	public static void export(final Object service, int port) throws Exception {
		if (service == null)
			throw new IllegalArgumentException("service instance == null");
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port " + port);

		System.out.println("Export service " + service.getClass().getName() + " on port " + port);

		/*
		 * 服务器启动了一个线程监听 Socket 端口 当有 Socket 访问了，反序列化解析出调用哪个 Service 哪个方法, 以及传入的参数，再用
		 * Socket 写回去
		 */
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(port);

		for (;;) {
			final Socket socket = server.accept();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
						String methodName = input.readUTF();
						Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
						Object[] arguments = (Object[]) input.readObject();

						try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
							Method method = service.getClass().getMethod(methodName, parameterTypes);
							Object result = method.invoke(service, arguments);
							output.writeObject(result);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	/**
	 * 引用服务
	 * 
	 * @param <T>            接口泛型
	 * @param interfaceClass 接口类型
	 * @param host           服务器主机名
	 * @param port           服务器端口
	 * @return 远程服务
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T refer(Class<T> interfaceClass, final String host, final int port) {
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		if (host == null || host.length() == 0)
			throw new IllegalArgumentException("Host == null!");
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port " + port);

		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);

		/*
		 * 短连接，在调用方法时才创建连接
		 */
		InvocationHandler handler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
				/*
				 * 客户端利用 Jdk 的 Proxy 生成了一个代理类，在创建 Proxy 时建立与服务器的 Socket 连接。 调用 Proxy 的方法时，
				 * 向服务器发送数据， 等待结果返回。 客户端把行为和行为入参提供给服务端，然后服务端的接口实现执行这个行为，最后再把执行结果返回给客户端。
				 * 看起来是客户端执行了行为，但其实是通过动态代理交给服务端执行的。其中，行为和入参这些数据通过 Socket 由客户端传给了服务端
				 */
				try (Socket socket = new Socket(host, port); ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());) {
					output.writeUTF(method.getName());
					output.writeObject(method.getParameterTypes());// Socket 编程，传输 Object
					output.writeObject(arguments);

					try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
						Object result = input.readObject();
						if (result instanceof Throwable)
							throw (Throwable) result;

						return result;
					}
				}
			}
		};

		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass }, handler);
	}

}