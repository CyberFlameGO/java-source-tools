package blue.sparse.jst.specification.materials.vtf.image;

import org.joml.Vector3f;
import xyz.eutaxy.util.data.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteOrder;

public class DXT5Format extends ImageFormat {

	private int getBits(int i, int position, int count) {
		return (i >> position) & ((1 << count) - 1);
	}

	private long getBits(long l, int position, int count) {
		return (l >> position) & ((1 << count) - 1);
	}

	private Vector3f getRgb565(int i) {
		var red = getBits(i, 11, 5) / (float) (1 << 5);
		var green = getBits(i, 5, 6) / (float) (1 << 6);
		var blue = getBits(i, 0, 5) / (float) (1 << 5);
		return new Vector3f(red, green, blue);
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
				var alphaLong = data.readLong();
				var color0int = (int) data.readShort() & 0xFFFF;
				var color1int = (int) data.readShort() & 0xFFFF;
				var colorCodes = data.readInt();

				var color0 = getRgb565(color0int);
				var color1 = getRgb565(color1int);

				var alpha0 = getBits(alphaLong, 0, 8) / (float) (1 << 8);
				var alpha1 = getBits(alphaLong, 8, 8) / (float) (1 << 8);
				var alphaCodes = getBits(alphaLong, 16, 48);

				var alphaArray = new float[8];

				for (int i = 0; i < alphaArray.length; i++) {
					if (alpha0 > alpha1) {
						switch (i) {
							case 0:
								alphaArray[i] = alpha0;
								break;
							case 1:
								alphaArray[i] = alpha1;
								break;
							case 2:
								alphaArray[i] = (6f * alpha0 + 1f * alpha1) / 7f;
								break;
							case 3:
								alphaArray[i] = (5f * alpha0 + 2f * alpha1) / 7f;
								break;
							case 4:
								alphaArray[i] = (4f * alpha0 + 3f * alpha1) / 7f;
								break;
							case 5:
								alphaArray[i] = (3f * alpha0 + 4f * alpha1) / 7f;
								break;
							case 6:
								alphaArray[i] = (2f * alpha0 + 5f * alpha1) / 7f;
								break;
							case 7:
								alphaArray[i] = (1f * alpha0 + 6f * alpha1) / 7f;
								break;
						}
					} else {
						switch (i) {
							case 0:
								alphaArray[i] = alpha0;
								break;
							case 1:
								alphaArray[i] = alpha1;
								break;
							case 2:
								alphaArray[i] = (4f * alpha0 + 1f * alpha1) / 5f;
								break;
							case 3:
								alphaArray[i] = (3f * alpha0 + 2f * alpha1) / 5f;
								break;
							case 4:
								alphaArray[i] = (2f * alpha0 + 3f * alpha1) / 5f;
								break;
							case 5:
								alphaArray[i] = (1f * alpha0 + 4f * alpha1) / 5f;
								break;
							case 6:
								alphaArray[i] = 0.0f;
								break;
							case 7:
								alphaArray[i] = 1.0f;
						}
					}

				}

				var colorArray = new Vector3f[4];
				for (int i = 0; i < colorArray.length; i++) {
					switch (i) {
						case 0:
							colorArray[i] = color0;
							break;
						case 1:
							colorArray[i] = color1;
							break;
						case 2:
							colorArray[i] = (new Vector3f(color0).mul(2f).add(color1)).div(3f);
							break;
						case 3:
							colorArray[i] = (new Vector3f(color0).add(new Vector3f(color1).mul(2f))).div(3f);
							break;
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
						var colorCode = getBits(colorCodes, index * 2, 2);
						var alphaCode = (int) getBits(alphaCodes, index * 3, 3);
						var color = colorArray[colorCode];
						var alpha = alphaArray[alphaCode];

						bufferedImage.setRGB(imageX, imageY, new Color(color.x, color.y, color.z, alpha).getRGB());
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

		for (int blockY = 0; blockY < heightInBlocks; blockY++) {
			for (int blockX = 0; blockX < widthInBlocks; blockX++) {


			}
		}
	}
}
