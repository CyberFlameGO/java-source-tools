import blue.sparse.jst.specification.materials.vtf.impl.VTFInstance;
import blue.sparse.jst.specification.materials.vtf.impl.VTFSpecification;
import xyz.eutaxy.util.data.Data;
import xyz.eutaxy.util.data.RandomAccessReadableData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VTFTest {

	private static final File FOLDER = new File("D:\\Steam Library\\steamapps\\common\\Source Unpack 2.4\\hl2\\materials");

	public static void main(String[] args) throws IOException {

		File file = new File("source-vtf-test/7-0.vtf");
		RandomAccessReadableData data = Data.readFile(file);

		VTFInstance instance = VTFSpecification.INSTANCE.read(data);
		BufferedImage image = instance.getImage(0, 0);

		ImageIO.write(image, "PNG", new File("out.png"));

	}

}
