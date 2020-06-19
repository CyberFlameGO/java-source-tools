package blue.sparse.jst.specification.materials.vtf.image;

import org.joml.Vector3f;
import xyz.eutaxy.util.data.*;
import xyz.eutaxy.util.memory.Bits;

import java.awt.image.BufferedImage;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.*;

public class DXT1Format extends ImageFormat {

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

				var array = new Vector3f[4];
				for (int i = 0; i < array.length; i++) {
					if (color0int > color1int) {
						switch (i) {
							case 0:
								array[i] = color0;
								break;
							case 1:
								array[i] = color1;
								break;
							case 2:
								array[i] = new Vector3f(color0).mul(2f).add(color1).div(3f);
								break;
							case 3:
								array[i] = new Vector3f(color0).add(new Vector3f(color1).mul(2f)).div(3f);
								break;
						}
					} else {
						switch (i) {
							case 0:
								array[i] = color0;
								break;
							case 1:
								array[i] = color1;
								break;
							case 2:
								array[i] = new Vector3f(color0).add(color1).div(2f);
								break;
							case 3:
								array[i] = new Vector3f(0f);
								break;
						}
					}
				}

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
			System.out.println("DXT1 encoding finished after "+diffTime+"ms");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Writing blocks to output...");
		long writeStart = System.currentTimeMillis();
		for (Block block : blocks) {
			data.writeShort(block.color0);
			data.writeShort(block.color1);
			data.writeInt(block.codes);
		}
		long writeDiff = System.currentTimeMillis() - writeStart;
		System.out.println("Done writing blocks after "+writeDiff+"ms");

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

			int min = -1;
			int max = 1;
			for (int r = min; r <= max; r++) {
				for (int g = min; g <= max; g++) {
					for (int b = min; b <= max; b++) {
						testColors.add(decodeRGB565(encodeRGB565(
								Math.max(Math.min(cr + r, 5), 0),
								Math.max(Math.min(cg + g, 6), 0),
								Math.max(Math.min(cb + b, 5), 0)
						)));
					}
				}
			}
		}

		float bestDistance = Float.MAX_VALUE;

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
					block.color0 = encodeRGB565(color0);
					block.color1 = encodeRGB565(color1);
//					outColor0.set(color0);
//					outColor1.set(color1);
				}
			}
		}
	}

	private float sumDistances(List<Vector3f> a, List<Vector3f> b) {
		float result = 0f;
		for (int i = 0; i < b.size(); i++) {
			result += a.get(i).distanceSquared(b.get(i));
		}

		return result;
	}

	private class Block {

		BufferedImage fullImage;

		short blockX;
		short blockY;

		short color0;
		short color1;
		int codes;

		public Block(BufferedImage fullImage, short blockX, short blockY) {
			this.fullImage = fullImage;
			this.blockX = blockX;
			this.blockY = blockY;
		}
	}
}
