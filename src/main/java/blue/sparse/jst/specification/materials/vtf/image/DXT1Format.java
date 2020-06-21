package blue.sparse.jst.specification.materials.vtf.image;

import org.joml.*;
import xyz.eutaxy.util.data.*;
import xyz.eutaxy.util.memory.Bits;

import java.awt.image.BufferedImage;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.*;

public class DXT1Format extends ImageFormat {

	private static final List<Vector3i> TEST_COLOR_OFFSETS = new ArrayList<>();
	private static final int MAX_BITS_5 = (1 << 5) - 1;
	private static final int MAX_BITS_6 = (1 << 6) - 1;
	private static final Vector3i INT_MAX_565 = new Vector3i(MAX_BITS_5, MAX_BITS_6, MAX_BITS_5);
	private static final Vector3f FLOAT_MAX_565 = new Vector3f(INT_MAX_565);
	public static int quality = 81;

	static {
		int range = 2;
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					var vector = new Vector3i(x, y, z);
					TEST_COLOR_OFFSETS.add(vector);
				}
			}
		}

		TEST_COLOR_OFFSETS.sort(Comparator.comparingDouble(Vector3i::length));
		System.out.println(TEST_COLOR_OFFSETS.size()+" test color offsets");
	}

	@Override
	public BufferedImage read(int width, int height, RandomAccessReadableData data) {
		var originalOrder = data.byteOrder();
		data.byteOrder(ByteOrder.LITTLE_ENDIAN);

		var widthInBlocks = width / 4;
		var heightInBlocks = height / 4;

		if (width % 4 != 0) {
			widthInBlocks++;
		}

		if (height % 4 != 0) {
			heightInBlocks++;
		}

		var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int blockY = 0; blockY < heightInBlocks; blockY++) {
			for (int blockX = 0; blockX < widthInBlocks; blockX++) {
				var color0int = (int) data.readShort() & 0xFFFF;
				var color1int = (int) data.readShort() & 0xFFFF;
				var color0 = decodeRGB565(color0int);
				var color1 = decodeRGB565(color1int);
				var codes = data.readInt();

				var array = interpolateColors(color0, color1);

				for (int y = 0; y <= 3; y++) {
					for (int x = 0; x <= 3; x++) {
						var imageX = x + blockX * 4;
						var imageY = y + blockY * 4;

						if (imageX >= width || imageY >= height) {
							continue;
						}

						var index = x + y * 4;
						var code = Bits.getBits32(codes, index * 2, 2);
						var color = array[code];

						bufferedImage.setRGB(imageX, imageY, encodeRGB888(color));
					}
				}
			}
		}

		data.byteOrder(originalOrder);
		return bufferedImage;
	}

	@Override
	public void write(BufferedImage image, WritableData data) {
		var originalOrder = data.byteOrder();
		data.byteOrder(ByteOrder.LITTLE_ENDIAN);

		var width = image.getWidth();
		var height = image.getHeight();

		var widthInBlocks = width / 4;
		var heightInBlocks = height / 4;

		if (width % 4 != 0) {
			widthInBlocks++;
		}

		if (height % 4 != 0) {
			heightInBlocks++;
		}

		List<Block> blocks = new ArrayList<>();
		var pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		var totalBlockCount = heightInBlocks * widthInBlocks;
		CountDownLatch latch = new CountDownLatch(totalBlockCount);

		for (short blockY = 0; blockY < heightInBlocks; blockY++) {
			for (short blockX = 0; blockX < widthInBlocks; blockX++) {
				var block = new Block(image, blockX, blockY);
				blocks.add(block);

				pool.submit(() -> {
					findBestColors(block);
					populateBlockCodes(block);
					latch.countDown();
				});
			}
		}

		try {
			long startTime = System.currentTimeMillis();
			while (!latch.await(500, TimeUnit.MILLISECONDS)) {
				var remaining = latch.getCount();
				var completed = totalBlockCount - remaining;
				double progress = completed / (double) totalBlockCount;
				System.out.printf("DXT1 encoding: (%,d/%,d) %.1f%%%n", completed, totalBlockCount, progress * 100.0);
			}
			long diffTime = System.currentTimeMillis() - startTime;
			System.out.println("DXT1 encoding finished after " + diffTime + "ms");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Writing blocks to output...");
		long writeStart = System.currentTimeMillis();
		float totalDistance = 0f;
		for (Block block : blocks) {
			data.writeShort(block.color0);
			data.writeShort(block.color1);
			data.writeInt(block.codes);

			totalDistance += block.distance;
		}
		System.out.println("Total block distance: " + totalDistance);
		long writeDiff = System.currentTimeMillis() - writeStart;
		System.out.println("Done writing blocks after " + writeDiff + "ms");

		pool.shutdownNow();

		data.byteOrder(originalOrder);
	}

	private void populateBlockCodes(Block block) {
		var color0 = decodeRGB565(block.color0);
		var color1 = decodeRGB565(block.color1);
		Vector3f[] array = interpolateColors(color0, color1);

		for (int y = 0; y <= 3; y++) {
			for (int x = 0; x <= 3; x++) {

				var imageX = x + block.blockX * 4;
				var imageY = y + block.blockY * 4;

				if (imageX >= block.fullImage.getWidth() || imageY >= block.fullImage.getHeight()) {
					continue;
				}

				var index = x + y * 4;
				var color = decodeRGB888(block.fullImage.getRGB(imageX, imageY));
				int code = getColorCode(array, color);

				block.codes = Bits.withBits32(block.codes, index * 2, 2, code);
			}

		}
	}

	private int getColorCode(Vector3f[] interpolated, Vector3f color) {
		int code = -1;
		float distance = Float.MAX_VALUE;

		for (int i = 0; i < interpolated.length; i++) {
			Vector3f c = interpolated[i];
			float d = c.distance(color);
			if (d < distance) {
				distance = d;
				code = i;
			}
		}
		return code;
	}

	private Vector3f[] interpolateColors(Vector3f color0, Vector3f color1) {
		var color0int = (int) encodeRGB565(color0) & 0xFFFF;
		var color1int = (int) encodeRGB565(color1) & 0xFFFF;

		var array = new Vector3f[4];
		for (int i = 0; i < array.length; i++) {
			if (color0int > color1int) {
				if (i == 0) {
					array[i] = color0;
				} else if (i == 1) {
					array[i] = color1;
				} else if (i == 2) {
					array[i] = new Vector3f(color0).mul(2f).add(color1).div(3f);
				} else {
					array[i] = new Vector3f(color0).add(new Vector3f(color1).mul(2f)).div(3f);
				}
			} else {
				if (i == 0) {
					array[i] = color0;
				} else if (i == 1) {
					array[i] = color1;
				} else if (i == 2) {
					array[i] = new Vector3f(color0).add(color1).div(2f);
				} else {
					array[i] = new Vector3f(0f);
				}
			}
		}
		return array;
	}

	private void findBestColors(Block block) {
		List<Vector3f> actualPixelColors = new ArrayList<>();

		for (int y = 0; y <= 3; y++) {
			for (int x = 0; x <= 3; x++) {
				var imageX = x + block.blockX * 4;
				var imageY = y + block.blockY * 4;

				if (imageX >= block.fullImage.getWidth() || imageY >= block.fullImage.getHeight()) {
					continue;
				}

				actualPixelColors.add(decodeRGB888(block.fullImage.getRGB(imageX, imageY)));
			}
		}

		Set<Vector3f> testColors = new HashSet<>();
		for (Vector3f actualPixelColor : actualPixelColors) {
			testColors.add(decodeRGB565(encodeRGB565(actualPixelColor)));
		}

		for (Vector3f color : actualPixelColors) {
			int color565 = encodeRGB565(color);
			int cr = Bits.getBits32(color565, 11, 5);
			int cg = Bits.getBits32(color565, 5, 6);
			int cb = Bits.getBits32(color565, 0, 5);

//			for (int i = 0; i < TEST_COLOR_OFFSETS.size(); i++) {
			for (int i = 0; i < quality; i++) {
				Vector3i offset = TEST_COLOR_OFFSETS.get(i);
				if (cr + offset.x > MAX_BITS_5)
					continue;
				if (cg + offset.y > MAX_BITS_6)
					continue;
				if (cb + offset.z > MAX_BITS_5)
					continue;

				testColors.add(decodeRGB565(encodeRGB565(cr + offset.x, cg + offset.y, cg + offset.z)));
			}
		}

		float bestDistance = Float.MAX_VALUE;

		Vector3f resultColor0 = new Vector3f();
		Vector3f resultColor1 = new Vector3f();
		for (Vector3f color0 : testColors) {
			for (Vector3f color1 : testColors) {
				Vector3f[] pInterpolated = interpolateColors(color0, color1);
				List<Vector3f> colors = new ArrayList<>(actualPixelColors.size());
				for (Vector3f actualPixelColor : actualPixelColors) {
					colors.add(pInterpolated[getColorCode(pInterpolated, actualPixelColor)]);
				}

				float distance = sumDistances(actualPixelColors, colors);
				if (distance < bestDistance) {
					bestDistance = distance;
					resultColor0 = color0;
					resultColor1 = color1;
				}
			}
		}

		block.distance = bestDistance;
		block.color0 = encodeRGB565(resultColor0);
		block.color1 = encodeRGB565(resultColor1);
	}

	private long sumDistances(short[] a, short[] b) {
		return 0;
	}

	private short distance(short a, short b) {
		int ra = a >> 11 & (1 << 5) - 1;
		int ga = a >> 5 & (1 << 6) - 1;
		int ba = a & (1 << 5) - 1;

		int rb = a >> 11 & (1 << 5) - 1;
		int gb = a >> 5 & (1 << 6) - 1;
		int bb = a & (1 << 5) - 1;

		int dr = ra - rb;
		int dg = ga - gb;
		int db = ba - bb;

		return (short) (dr * dr + dg * dg + db * db);
	}

	private float sumDistances(List<Vector3f> a, List<Vector3f> b) {
		float result = 0f;
		for (int i = 0; i < b.size(); i++) {
//			Vector3f va = a.get(i);
//			Vector3f vb = b.get(i);
//
//			float dr = va.x * 255f - vb.x * 255f;
//			float dg = va.y * 255f - vb.y * 255f;
//			float db = va.z * 255f - vb.z * 255f;
//
//			float ar = (va.x * 255f + vb.x * 255f) / 2f;
//			float dc = (float) Math.sqrt((2 + ar / 256f) * (dr * dr) + 4 * dg * dg + (2 + (255f - ar) / 256f) * db * db);
//
//			result += dc;

			result += a.get(i).distance(b.get(i));
		}

		return result;
	}

	private static class Block {

		BufferedImage fullImage;

		short blockX;
		short blockY;

		short color0;
		short color1;
		int codes;

		float distance;

		public Block(BufferedImage fullImage, short blockX, short blockY) {
			this.fullImage = fullImage;
			this.blockX = blockX;
			this.blockY = blockY;
		}
	}
}
