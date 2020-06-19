package blue.sparse.jst.specification.materials.vtf.image;

import org.joml.Vector3f;
import xyz.eutaxy.util.data.RandomAccessReadableData;
import xyz.eutaxy.util.data.RandomAccessWritableData;
import xyz.eutaxy.util.memory.Bits;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteOrder;

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

						bufferedImage.setRGB(imageX, imageY, new Color(color.x, color.y, color.z).getRGB());
					}
				}
			}
		}

		data.byteOrder(originalOrder);
		return bufferedImage;
	}

	@Override
	public void write(BufferedImage image, RandomAccessWritableData data) {
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

		for (int blockY = 0; blockY < heightInBlocks; blockY++) {
			for (int blockX = 0; blockX < widthInBlocks; blockX++) {

				Vector3f minColor = new Vector3f(1);
				Vector3f maxColor = new Vector3f(0);

				for (int y = 0; y <= 3; y++) {
					for (int x = 0; x <= 3; x++) {

						var imageX = x + blockX * 4;
						var imageY = y + blockY * 4;

						if (imageX >= width || imageY >= height) {
							continue;
						}

						Vector3f rgb = decodeRGB888(image.getRGB(imageX, imageY));

						if (rgb.length() < minColor.length()) {
							minColor = rgb;
						}

						if (rgb.length() > maxColor.length()) {
							maxColor = rgb;
						}
					}
				}

				var color0 = minColor;
				var color1 = maxColor;

				var color0int = (int) encodeRGB565(color0) & 0xFFFF;
				var color1int = (int) encodeRGB565(color1) & 0xFFFF;

				data.writeShort((short) color0int);
				data.writeShort((short) color1int);

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

				int codes = 0;
				for (int y = 0; y <= 3; y++) {
					for (int x = 0; x <= 3; x++) {

						var imageX = x + blockX * 4;
						var imageY = y + blockY * 4;

						if (imageX >= width || imageY >= height) {
							continue;
						}

						var index = x + y * 4;
						var color = decodeRGB888(image.getRGB(imageX, imageY));
						int code = -1;
						float distance = Float.MAX_VALUE;

						for (int i = 0; i < array.length; i++) {
							Vector3f c = array[i];
							float d = c.distance(color);
							if (d < distance) {
								distance = d;
								code = i;
							}
						}

						codes = Bits.withBits32(codes, index * 2, 2, code);
					}

				}

				data.writeInt(codes);
			}
		}

		data.byteOrder(originalOrder);
	}
}
