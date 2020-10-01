import blue.sparse.jst.specification.materials.vtf.impl.*;
import xyz.eutaxy.util.data.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ExportAll {

	public static void main(String[] args) throws IOException {
		exportAll(new File("G:\\SourceUnpack\\portal\\materials"), new File("materials"));
		exportAll(new File("G:\\SourceUnpack\\hl2\\materials"), new File("materials"));
	}

	public static void exportAll(File inputFolder, File outputFolder) throws IOException {
		var files = inputFolder.listFiles();
		for (File file : files) {
			var name = file.getName();
			if(file.isDirectory()) {
				exportAll(file, new File(outputFolder, name));
			} else if(name.endsWith(".vtf")) {
				export(file, new File(outputFolder, name.substring(0, name.length() - 4)+".png"));
			}
		}
	}

	private static void export(File input, File output) throws IOException {
		if(output.exists())
			return;
		output.getParentFile().mkdirs();
		try {
			RandomAccessReadableData data = Data.readFile(input);
			VTFInstance instance = VTFSpecification.INSTANCE.read(data);
			if (instance.getFrameCount() < 0) {
				System.out.println("Failed to export "+input.getPath()+" (Invalid format?)");
				return;
			}
			BufferedImage image = instance.getImage(0, 0);
			ImageIO.write(image, "PNG", output);
			System.out.println("Exporting "+input.getName());
		} catch(Throwable t) {
			System.out.println("Failed to export "+input.getPath());
//			t.printStackTrace();
		}

	}

}
