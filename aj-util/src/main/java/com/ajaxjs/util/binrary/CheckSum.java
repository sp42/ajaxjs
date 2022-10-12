package com.ajaxjs.util.binrary;

import java.util.*;

/**
 * 海明校验码（HammingCode） & 奇偶校验码(Parity Code)
 * 
 * <code>
 * 公式：2^k-1≥n+k or 2^k≥n+1+k
 * <p>
 * 规则：
 * 设k个校验位为Pk,Pk-1,...,P1，N个数据位为Dn-1,Dn-2,...,D1,D0，对应的海明码为Hk+n,Hk+n-1,...,H1，那么
 * (1) Pi在海明码的第2^i-1位置，即Hj=Pi，且j=2^i-1，数据位一次从低到高占据海明码剩下的位置。
 * (2) 海明码中的任何一位都是由若干个校验位来进行校验。其对应关系如下：被校验的海明位的下标是所有参与该位的校验位的下标之和，而校验位由自身校验。
 * <p>
 * 对应位置参照：
 * H12 H11 H10 H9  H8  H7  H6  H5  H4  H3  H2  H1
 * D7  D6  D5  D4  P4  D3  D2  D1  P3  D0  P2  P1
 * 当前海明码下标=当前数据位下标+当前校验位数
 * 当前数据位下标
 * 计算当前海明码下标对应的校验位，并添加到table
 * 最后将校验位插入原始字节数组
 * 校验关系：
 * P1:P1,D6,D4,D3,D1,D0
 * P2:P2,D6,D5,D3,D2,D0
 * P3:P3,D7,D3,D2,D1
 * P4:P4,D7,D6,D5,D4
 * <p>
 * 8  4 12
 * 9  4 13
 * 10 4 14
 * 11 4 15
 * 12 5 17
 * <p>
 * 反校验，通过海明码的位数来获取最大的校验位，如12个比特位的海明码，12转二进制->1100，
 * 二进制位数即校验码的数量，按权展开获取10进制即海明码校验位的下标
 *
 * <p>
 * </code> User: Bai Yang DateTime:2021/2/15 3:26 下午
 * https://baiyang.blog.csdn.net/article/details/113814441
 */
public class CheckSum {
	public static final int BYTE_BIT_SIZE = 8;

	public static final int LONG_BIT_SIZE = 64;

	/**
	 * 对传入的字节数组按照海明码规则插入校验位。 海明码采用偶校验。
	 *
	 * @param bytes 编码字节数组。可以是任意对象转的字节数组，如字符串
	 * @return 按照海明码规则插入校验位之后的字节数组，长度=源数组长度+校验位所需字节长度
	 */
	public static byte[] encode(byte[] bytes) {
		// 构造table
		Map<Integer, List<Integer>> checkCodeTable = buildCheckBitTable(bytes);

		// 分配字节数组
		int allocateSize = (trimLengthOf(bytes) + checkCodeTable.size() + BYTE_BIT_SIZE - 1) / BYTE_BIT_SIZE;
		byte[] hmCodeBytes = new byte[allocateSize];

		// 将所有校验位暂时置为1，便于后面的数据位填充的判断
		for (int ckBitPositionOfHm : checkCodeTable.keySet()) {
			int byteIdx = allocateSize - (ckBitPositionOfHm + BYTE_BIT_SIZE - 1) / BYTE_BIT_SIZE;
			hmCodeBytes[byteIdx] = (byte) (hmCodeBytes[byteIdx] | (1 << ((ckBitPositionOfHm - 1) % 8)));
		}

		// 从右至左依次填充数据位
		int currByteIdx = hmCodeBytes.length - 1;
		int currBitOffset = -1;

		for (int i = bytes.length - 1; i >= 0; i--) {
			byte dataByte = bytes[i];

			for (int j = 0; j < BYTE_BIT_SIZE; j++) {
				int bitv = (dataByte >> j & 1);

				hmCodeByteLoopFlag: for (; currByteIdx >= 0; currByteIdx--) {
					byte hmCodeByte = hmCodeBytes[currByteIdx];

					for (currBitOffset++; currBitOffset < BYTE_BIT_SIZE; currBitOffset++) {
						if ((hmCodeByte >> currBitOffset & 1) == 0) {
							hmCodeBytes[currByteIdx] = (byte) (hmCodeByte | (bitv << currBitOffset));
							break hmCodeByteLoopFlag;
						}
					}

					currBitOffset = -1;
				}
			}
		}

		// 设置校验位值
		for (Map.Entry<Integer, List<Integer>> item : checkCodeTable.entrySet()) {
			int ckBitPositionOfHm = item.getKey();
			List<Integer> bitValues = item.getValue();
			int bitValue = bitValues.get(0);

			for (int i = 1; i < bitValues.size(); i++)
				bitValue = bitValue ^ bitValues.get(i);

			int byteIdx = allocateSize - (ckBitPositionOfHm + BYTE_BIT_SIZE - 1) / BYTE_BIT_SIZE;

			if ((hmCodeBytes[byteIdx] & (1 << ((ckBitPositionOfHm - 1) % 8))) > 0) {
				if (bitValue == 0)
					hmCodeBytes[byteIdx] = (byte) (hmCodeBytes[byteIdx] ^ 1 << ((ckBitPositionOfHm - 1) % 8));

			} else if (bitValue != 0)
				hmCodeBytes[byteIdx] = (byte) (hmCodeBytes[byteIdx] & bitValue << ((ckBitPositionOfHm - 1) % 8));
		}

		return hmCodeBytes;
	}

	/**
	 * 构建校验位Table key为校验位对应的海明码位置量，如:1,2,4,8...
	 * value为校验位校验的海明码数据位的位置量，如D0被P1(1)和P2(2)校验位校验，则P1(1)和P2(2)都有该数据位的值
	 *
	 * @param bytes 源字节数组
	 * @return table
	 */
	public static Map<Integer, List<Integer>> buildCheckBitTable(byte[] bytes) {
		Map<Integer, List<Integer>> checkBitTable = new HashMap<>();
		int currDataPosition = 1; // 当前数据位位置量(位置量从1算)
		int currCKBitIdx = -1;// 当前校验位下标
		int oneOfBitLen = trimLengthOf(bytes);

		for (byte data : bytes) {
			for (int j = 0; j < BYTE_BIT_SIZE && oneOfBitLen > 0; j++, oneOfBitLen--) {
				int ckBitCount = calculateCheckBitPosition(currDataPosition, 0);
				int newCKBitIdx = ckBitCount - 1;

				for (int k = currCKBitIdx + 1; k <= newCKBitIdx/* 转成bit位下标 */; k++) { // 新增最新计算出的校验码位置
					int ckBitPositionOfHm = 1 << k; // 获取位置量为k的校验码对应的海明码下标
					checkBitTable.putIfAbsent(ckBitPositionOfHm, new ArrayList<>());
				}

				currCKBitIdx = newCKBitIdx;

				int checkBitSize = checkBitTable.size(); // 当前校验码数量
				int hmIdx = checkBitSize + currDataPosition; // 海明码对应下标=校验码数量+当前数据位位置量(位置量从1算)
				// 根据海明码的规则2：被校验的海明位的下标是所有参与该位的校验位的下标之和，而校验位由自身校验。
				// 来获取当前数据位在海明码位置量下，参与校验的校验码位置量
				List<Integer> checkBitPositions = getOneOfBitPosition(hmIdx);

				for (Integer checkBitPosition : checkBitPositions)
					// 新增校验码对应的被校验位置的值(0/1),后续在计算校验位值的时候直接异或所有列表值即可
					checkBitTable.get(1 << (checkBitPosition - 1)).add((data >> j) & 1);

				currDataPosition++;
			}
		}

		return checkBitTable;
	}

	/**
	 * 计算数据位的offset所需的最大的校验位位置量,如 offset=1,k=2。 参考海明码公式：2^k-1≥n+k or 2^k≥n+1+k
	 *
	 * @param offset 数据位偏移量(也可以说需要计算的数据位的数量)
	 * @param k      当前计算出的校验位数量，递归传入
	 * @return offset所需的校验位数量
	 */
	private static int calculateCheckBitPosition(int offset, int k) {
		if (Math.pow(2, k) >= offset + 1 + k)
			return k;

		return calculateCheckBitPosition(offset, ++k);
	}

	/**
	 * 校验编码。 采用偶校验
	 *
	 * @param bytes 海明码字节数组
	 * @return 如果传入的字节数组无法通过海明码的校验，则返回false，否则返回true。
	 */
	public static boolean checkCode(byte[] bytes) {
		int bitLen = trimLengthOf(bytes); // 字节数组拼接后的有效位数（从右到左）
		Map<Integer, Integer> checkBitResultTable = new HashMap<>();
		int currOffset = 0;

		for (int i = bytes.length - 1; i >= 0; i--) {// 从字节数组右到左遍历
			byte b = bytes[i];

			for (int j = 0; j < BYTE_BIT_SIZE && currOffset < bitLen; j++) {
				currOffset++;
				List<Integer> oneOfBitPositions = getOneOfBitPosition(currOffset);

				for (Integer oneOfBitPosition : oneOfBitPositions) {
					int tableKey = 1 << oneOfBitPosition - 1;
					int bitValue = (b >> j) & 1;
					
					checkBitResultTable.merge(tableKey, bitValue, (a, b1) -> a ^ b1);
				}
			}
		}

		return !checkBitResultTable.containsValue(1);
	}

	/**
	 * 解码。 对海明码字节数组进行解码，去除校验位，获取原始数据字节数组。
	 *
	 * @param bytes 海明码字节数组
	 * @return 原始数据字节数组
	 */
	public static byte[] decode(byte[] bytes) {
		int bitLen = trimLengthOf(bytes); // 字节数组拼接后的有效位数（从右到左）
		List<Integer> oneOfBitPositions = getOneOfBitPosition(bitLen); // 根据当前海明码最高位的位置量，获取需要的校验位有哪些
		int ckBitCount = oneOfBitPositions.get(oneOfBitPositions.size() - 1);// 获取校验位的数量
		int dataBitLen = bitLen - ckBitCount;
		int allocateSize = (dataBitLen + BYTE_BIT_SIZE - 1) / BYTE_BIT_SIZE;
		byte[] dataBytes = new byte[allocateSize];

		int currDataIdx = dataBytes.length - 1;
		int currDataOffset = 0;
		int currOffset = 0;

		for (int i = bytes.length - 1; i >= 0; i--) {// 从字节数组右到左遍历
			byte b = bytes[i];

			for (int j = 0; j < BYTE_BIT_SIZE && currOffset < bitLen; j++) {
				currOffset++;
				oneOfBitPositions = getOneOfBitPosition(currOffset);

				if (oneOfBitPositions.size() == 1) // 如果只有一个1的bit，那么表明当前的下标对应的是校验位，跳过
					continue;

				byte dataByte = dataBytes[currDataIdx];
				int bitValue = (b >> j) & 1;
				dataBytes[currDataIdx] = (byte) (dataByte | bitValue << currDataOffset);
				currDataOffset++;

				if (currDataOffset > 7) {
					currDataOffset = 0;
					currDataIdx--;
				}
			}
		}

		return dataBytes;
	}

	/**
	 * 获取传入的数字对应的bit位为1的位置量。 位置量从1开始计数，从右往左依次获取。
	 * 例如：num=10，那么10的二进制是1010，则bit位为1的位置量是2,4(从右往左)
	 *
	 * @return bit位为1的位置量列表
	 */
	public static List<Integer> getOneOfBitPosition(long num) {
		List<Integer> list = new ArrayList<>();

		for (int i = 0; i < LONG_BIT_SIZE; i++) {
			if ((num >> i & 1) == 1)
				list.add(i + 1);
		}

		return list;
	}

	/**
	 * 获取字节数组的 bit 位长度。去除了最左字节的前导0的长度。
	 *
	 * @return 去除了最左字节的前导0的长度
	 */
	public static int trimLengthOf(byte[] bytes) {
		int bitLen = bytes.length * BYTE_BIT_SIZE;

		for (int i = BYTE_BIT_SIZE - 1; i >= 0; i--) {
			byte b = bytes[0];
			if ((b & (1 << i)) != 0)
				break;

			bitLen--;
		}

		return bitLen;
	}

	/**
	 * 返回字节数组所有bit位字符串
	 */
	public static String toBitStr(byte[] bytes) {
		StringBuilder strBuilder = new StringBuilder();

		for (byte b : bytes) {
			for (int i = BYTE_BIT_SIZE - 1; i >= 0; i--)
				strBuilder.append(b >> i & 1);
		}

		return strBuilder.toString();
	}

	/**
	 * 偶校验
	 *
	 * @param checkBytes 需要检查的字节数组
	 * @param full       checkBytes的所有字节累计进行奇偶校验
	 *                   。true-所有；false-只要有一个字节不通过奇偶校验就直接返回false。
	 * @return 是否校验通过
	 */
	public static boolean evenNumberCheck(byte[] checkBytes, boolean full) {
		return parityCheck(checkBytes, false, full);
	}

	/**
	 * 奇校验
	 *
	 * @param checkBytes 需要检查的字节数组
	 * @param full       checkBytes的所有字节累计进行奇偶校验
	 *                   。true-所有；false-只要有一个字节不通过奇偶校验就直接返回false。
	 * @return 是否校验通过
	 */
	public static boolean oddNumberCheck(byte[] checkBytes, boolean full) {
		return parityCheck(checkBytes, true, full);
	}

	/**
	 * 奇偶校验
	 * 
	 * https://baiyang.blog.csdn.net/article/details/113813493
	 *
	 * @param checkBytes 需要检查的字节数组
	 * @param odd        是否奇校验。true-校验，false-偶校验
	 * @param full       checkBytes的所有字节累计进行奇偶校验
	 *                   。true-所有；false-只要有一个字节不通过奇偶校验就直接返回false。
	 * @return 是否校验通过
	 */
	public static boolean parityCheck(byte[] checkBytes, boolean odd, boolean full) {
		byte checkMask = 0x1;
		int oddSum = 0;
		int byteSize = 8;

		for (byte checkByte : checkBytes) {
			for (int i = 0; i < byteSize; i++) {
				if ((checkByte >> i & checkMask) == 1)
					oddSum++;
			}

			if (!full) {
				if (!(odd == isOdd(oddSum)))
					return false;

				oddSum = 0;
			}
		}

		return !full || odd == isOdd(oddSum);
	}

	/**
	 * 判断传入的整数是否为奇数
	 *
	 * @param checkInt 校验整数
	 * @return true-奇数；false-偶数
	 */
	public static boolean isOdd(long checkInt) {
		boolean isOddNumber = false;
		byte checkMask = 0x1;

		if ((checkInt & checkMask) == 1) // 通过校验最后一位是否为1，来判断是否为奇数。
			// 为1的bit位数量为奇数
			isOddNumber = true;

		return isOddNumber;
	}
}
