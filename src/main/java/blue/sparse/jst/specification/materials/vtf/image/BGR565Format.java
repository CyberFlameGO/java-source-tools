package blue.sparse.jst.specification.materials.vtf.image;

import org.joml.Vector3f;
import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.awt.image.BufferedImage;

public final class BGR565Format extends ImageFormat {

	@Override
	public BufferedImage read(int width, int height, RandomAccessReadableData data) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				result.setRGB(x, y, encodeRGB888(decodeBGR565(data.readShort())));
			}
		}

		return result;
	}
}
