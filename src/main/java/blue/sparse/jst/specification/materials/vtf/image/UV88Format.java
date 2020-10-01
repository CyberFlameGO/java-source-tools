package blue.sparse.jst.specification.materials.vtf.image;

import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.awt.image.BufferedImage;

public final class UV88Format extends ImageFormat {

	@Override
	public BufferedImage read(int width, int height, RandomAccessReadableData data) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int u = data.readByte() & 0xFF;
				int v = data.readByte() & 0xFF;
				result.setRGB(x, y, encodeRGB888(u, v, 0));
			}
		}

		return result;
	}
}
