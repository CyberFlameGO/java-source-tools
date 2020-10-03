import blue.sparse.jst.specification.materials.vtf.impl.*;
import xyz.eutaxy.util.data.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

public class ExportAll {

	public static void main(String[] args) {
		exportAll(new File("D:\\Steam Library\\steamapps\\common\\Source Unpack 2.4\\portal\\materials"), new File("materials"));
		exportAll(new File("D:\\Steam Library\\steamapps\\common\\Source Unpack 2.4\\hl2\\materials"), new File("materials"));
		exportAll(new File("C:\\Users\\kchri\\Desktop\\csgo-materials"), new File("M:\\source-resources\\csgo-materials"));
	}

	public static void exportAll(File inputFolder, File outputFolder) {
		var list = new ArrayList<Future<?>>();

		var files = inputFolder.listFiles();
		for (File file : files) {
			var name = file.getName();
			File output = new File(outputFolder, name);
			if (file.isDirectory()) {
				exportAll(file, output);
			} else if (name.endsWith(".vtf")) {
				ForkJoinTask<?> submit = ForkJoinPool.commonPool().submit(() -> {
					try {
						export(file, new File(outputFolder, name.substring(0, name.length() - 4) + ".png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				list.add(submit);
			} else if (name.endsWith(".vmt")) {
				try {
					if(!output.exists()) {
						output.getParentFile().mkdirs();
						Files.copy(file.toPath(), output.toPath());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		for (Future<?> future : list) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private static void export(File input, File output) throws IOException {
		if (output.exists()) {
			return;
		}

		output.getParentFile().mkdirs();

		try {
			RandomAccessReadableData data = Data.readFile(input);
			VTFInstance instance = VTFSpecification.INSTANCE.read(data);
			if (instance.getFrameCount() < 0) {
				System.out.println("Failed to export " + input.getPath() + " (Invalid format?)");
				return;
			}
			BufferedImage image = instance.getImage(0, 0);
			ImageIO.write(image, "PNG", output);
			System.out.println("Exporting " + input.getName());
		} catch (Throwable t) {

			System.out.println("Failed to export " + input.getPath());
//			t.printStackTrace();
		}

	}

}
