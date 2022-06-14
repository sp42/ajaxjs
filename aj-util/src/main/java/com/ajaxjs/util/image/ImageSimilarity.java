package com.ajaxjs.util.image;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片相似性
 * 
 * 判断图像的相似性主要用于图像的去重，一种验证相似性的思路是先将图像进行缩放至指定尺寸，然后进行灰度处理，去掉颜色特征，最后对处理后的图像计算哈希值，通过比对不同图像的哈希值的汉明距离来判断图像是否相似
 * 
 * @author https://blog.csdn.net/jianggujin/article/details/80205459
 *
 */
public class ImageSimilarity {
	public static int size = 32;
	public static int smallerSize = 8;
	// DCT function stolen from
	// http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java
	private static double[] c;

	static {
		c = new double[size];

		for (int i = 1; i < size; i++)
			c[i] = 1;

		c[0] = 1 / Math.sqrt(2.0);
	}

	/**
	 * 通过汉明距离计算相似度
	 * 
	 * @param hash1
	 * @param hash2
	 * @return
	 */
	public static double calSimilarity(String hash1, String hash2) {
		return calSimilarity(getHammingDistance(hash1, hash2));
	}

	/**
	 * 通过汉明距离计算相似度
	 * 
	 * @param hammingDistance
	 * @return
	 */
	public static double calSimilarity(int hammingDistance) {
		int length = size * size;
		double similarity = (length - hammingDistance) / (double) length;

		similarity = Math.pow(similarity, 2);// 使用指数曲线调整相似度结果
		return similarity;
	}

	/**
	 * 通过汉明距离计算相似度
	 * 
	 * @param image1
	 * @param image2
	 * @return
	 * @throws IOException
	 */
	public static double calSimilarity(File image1, File image2) throws IOException {
		return calSimilarity(getHammingDistance(image1, image2));
	}

	/**
	 * 获得汉明距离
	 * 
	 * @param hash1
	 * @param hash2
	 * @return
	 */
	public static int getHammingDistance(String hash1, String hash2) {
		int counter = 0;

		for (int k = 0; k < hash1.length(); k++) {
			if (hash1.charAt(k) != hash2.charAt(k))
				counter++;
		}

		return counter;
	}

	/**
	 * 获得汉明距离
	 * 
	 * @param image1
	 * @param image2
	 * @return
	 * @throws IOException
	 */
	public static int getHammingDistance(File image1, File image2) throws IOException {
		return getHammingDistance(getHash(image1), getHash(image2));
	}

	/**
	 * 返回二进制字符串，类似“001010111011100010”，可用于计算汉明距离
	 * 
	 * @param imageFile
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getHash(File imageFile) throws IOException {
		BufferedImage img = ImageIO.read(imageFile);

		/*
		 * 1. Reduce size. Like Average Hash, pHash starts with a small image. However,
		 * the image is larger than 8x8; 32x32 is a good size. This is really done to
		 * simplify the DCT computation and not because it is needed to reduce the high
		 * frequencies.
		 */
		img = resize(img, size, size);

		/*
		 * 2. Reduce color. The image is reduced to a grayscale just to further simplify
		 * the number of computations.
		 */
		img = grayscale(img);

		double[][] vals = new double[size][size];

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++)
				vals[x][y] = getBlue(img, x, y);

		}

		/*
		 * 3. Compute the DCT. The DCT separates the image into a collection of
		 * frequencies and scalars. While JPEG uses an 8x8 DCT, this algorithm uses a
		 * 32x32 DCT.
		 */
		// long start = System.currentTimeMillis();
		double[][] dctVals = applyDCT(vals);
		// System.out.println("DCT: " + (System.currentTimeMillis() - start));

		/*
		 * 4. Reduce the DCT. This is the magic step. While the DCT is 32x32, just keep
		 * the top-left 8x8. Those represent the lowest frequencies in the picture.
		 */
		/*
		 * 5. Compute the average value. Like the Average Hash, compute the mean DCT
		 * value (using only the 8x8 DCT low-frequency values and excluding the first
		 * term since the DC coefficient can be significantly different from the other
		 * values and will throw off the average).
		 */
		double total = 0;

		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++)
				total += dctVals[x][y];

		}
		total -= dctVals[0][0];

		double avg = total / (double) ((smallerSize * smallerSize) - 1);

		/*
		 * 6. Further reduce the DCT. This is the magic step. Set the 64 hash bits to 0
		 * or 1 depending on whether each of the 64 DCT values is above or below the
		 * average value. The result doesn't tell us the actual low frequencies; it just
		 * tells us the very-rough relative scale of the frequencies to the mean. The
		 * result will not vary as long as the overall structure of the image remains
		 * the same; this can survive gamma and color histogram adjustments without a
		 * problem.
		 */
		StringBuilder hash = new StringBuilder();

		for (int x = 0; x < smallerSize; x++) {
			for (int y = 0; y < smallerSize; y++) {
				if (x != 0 && y != 0)
					hash.append((dctVals[x][y] > avg ? "1" : "0"));

			}
		}

		return hash.toString();
	}

	private static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}

	private static BufferedImage grayscale(BufferedImage img) {
		new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(img, img);

		return img;
	}

	private static int getBlue(BufferedImage img, int x, int y) {
		return (img.getRGB(x, y)) & 0xff;
	}

	private static double[][] applyDCT(double[][] f) {
		int N = size;
		double[][] F = new double[N][N];

		for (int u = 0; u < N; u++) {
			for (int v = 0; v < N; v++) {
				double sum = 0.0;

				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++)
						sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (f[i][j]);
				}

				sum *= ((c[u] * c[v]) / 4.0);
				F[u][v] = sum;
			}
		}

		return F;
	}

	public static void main(String[] args) throws IOException {
		// 获取图像
		File imageFile1 = new File("1.jpg");
		File imageFile2 = new File("2.jpg");
		System.err.println(ImageSimilarity.calSimilarity(imageFile1, imageFile2));
		System.err.println(ImageSimilarity.calSimilarity(imageFile1, imageFile1));
	}
}