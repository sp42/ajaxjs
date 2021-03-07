package com.demo.base.di;

public class MyInterface {
	public interface PCI {
		void start();

		void stop();
	}

	/**
	 * 显卡实现PCI接口规范
	 */
	public static class GraphicCard implements PCI {
		@Override
		public void start() {
			System.out.println("Display Graphic...");
		}

		@Override
		public void stop() {
			System.out.println("Display Graphic stop!");
		}
	}

	/**
	 * 网卡实现PCI接口规范
	 */
	public static class NetworkCard implements PCI {
		@Override
		public void start() {
			System.out.println("Send...");
		}

		@Override
		public void stop() {
			System.out.println("Network stop!");
		}

	}

	/**
	 * 声卡实现PCI接口规范
	 *
	 */
	public static class SoundCard implements PCI {
		@Override
		public void start() {
			System.out.println("Du du...");
		}

		@Override
		public void stop() {
			System.out.println("Sound stop!");
		}
	}


	/**
	 * 主板类
	 */
	public static class MainBoard {
		/**
		 * 通过这个方法,主板上可以插入任何实现PCI接口规范的卡
		 * 
		 * @param pci 参数类型为接口,任何实现接口的类都可以传进来.
		 */
		public void usePCICard(PCI pci) {
			pci.start();
			pci.stop();
		}

	}

	public static void main(String[] args) {
		MainBoard mb = new MainBoard();

		PCI gc = new GraphicCard();// 在主板上插入显卡
		mb.usePCICard(gc);

		PCI nc = new NetworkCard();// 在主板上插入网卡
		mb.usePCICard(nc);

		PCI sc = new SoundCard();// 在主板上插入声卡
		mb.usePCICard(sc);
	}
}
