package blue.sparse.jst.specification.materials.vtf.image;

import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.awt.image.BufferedImage;

public final class BGRA8888Format extends ImageFormat {

	@Override
	public BufferedImage read(int width, int height, RandomAccessReadableData data) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int b = data.readByte() & 0xFF;
				int g = data.readByte() & 0xFF;
				int r = data.readByte() & 0xFF;
				int a = data.readByte() & 0xFF;

				result.setRGB(x, y, encodeARGB8888(a, r, g, b));
			}
		}

		return result;
	}

}
