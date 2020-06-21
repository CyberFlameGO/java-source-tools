import blue.sparse.jst.specification.materials.vtf.VTFImageDataFormat;
import blue.sparse.jst.specification.materials.vtf.image.ImageFormat;
import blue.sparse.jst.specification.materials.vtf.image.gl.DXT1GPUEncoder;
import xyz.eutaxy.util.data.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class GPUEncodingTest {

	public static void main(String[] args) throws IOException {
		long fullStartTime = System.currentTimeMillis();
		BufferedImage image = ImageIO.read(new File("source-test/triangles-small.png"));
		var encoder = new DXT1GPUEncoder();
		var dxtOut = new ByteArrayOutputStream();
		encoder.write(image, Data.writeOutputStream(dxtOut));
		long gpuDiffTime = System.currentTimeMillis() - fullStartTime;
		System.out.println("GPU done in "+gpuDiffTime+"ms");

		var gpuOut = Data.writeFile(new File("gpu-test.bin"));
		var dxtBytes = dxtOut.toByteArray();
		gpuOut.writeBytes(dxtBytes);

		var format = VTFImageDataFormat.DXT1.getFormat();
		var outputImage = format.read(image.getWidth(), image.getHeight(), Data.wrapByteArray(dxtBytes));
		ImageIO.write(outputImage, "PNG", new File("source-test/out.png"));

		long fullDiffTime = System.currentTimeMillis() - fullStartTime;
		System.out.println("Done in "+fullDiffTime+"ms");
	}

}
