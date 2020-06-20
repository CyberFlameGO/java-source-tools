import blue.sparse.jst.specification.materials.vtf.VTFImageDataFormat;
import blue.sparse.jst.specification.materials.vtf.image.*;
import blue.sparse.jst.specification.materials.vtf.impl.VTFInstance;
import blue.sparse.jst.specification.materials.vtf.impl.VTFSpecification;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;
import xyz.eutaxy.util.data.*;
import xyz.eutaxy.util.memory.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL41C.GL_RGB565;
import static org.lwjgl.opengl.GL42C.glTexStorage2D;

public final class Main {
	public static void main(String[] args) throws IOException {
//		File file = new File("source-test/v_portalgun.vtf");
//		RandomAccessReadableData data = Data.readFile(file);
//
//		VTFInstance instance = VTFSpecification.INSTANCE.read(data);
//		System.out.println();
//		BufferedImage image = instance.getImage(0, 0);
//
//		ImageIO.write(image, "PNG", new File("out.png"));
		/////////////////////////////////////////////////////////////

		BufferedImage image = ImageIO.read(new File("source-test/v_portalgun.png"));
		ImageFormat format = VTFImageDataFormat.DXT1.getFormat();

		var dxtOut = new ByteArrayOutputStream();
		format.write(image, Data.writeOutputStream(dxtOut));
		var bytes = dxtOut.toByteArray();
		System.out.println(MemoryFormatUnit.BYTE_DECIMAL.formatBytes(bytes.length));
		var dxtIn = Data.wrapByteArray(bytes);
		BufferedImage out = format.read(image.getWidth(), image.getHeight(), dxtIn);
		ImageIO.write(out, "PNG", new File("source-test/out.png"));

		/////////////////////////////////////////////////////////////

//		glfwInit();
//		glfwDefaultWindowHints();
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//
//		var id = glfwCreateWindow(1, 1, "", 0, 0);
//		glfwMakeContextCurrent(id);
//
//		GL.createCapabilities();
//		int tid = glGenTextures();
//		glBindTexture(GL_TEXTURE_2D, tid);
//		glTexStorage2D(GL_TEXTURE_2D, 1, GL_COMPRESSED_RGB_S3TC_DXT1_EXT, image.getWidth(), image.getHeight());
//		ByteBuffer buffer = createRGBABuffer(image);
//		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);
//		buffer.clear();
//		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
//		BufferedImage bufferedImage = createBufferedImage(image.getWidth(), image.getHeight(), buffer);
//		ImageIO.write(bufferedImage, "PNG", new File("opengl-out.png"));

		////////////////////////////////////////////////////////////////////

//		int[] colors = new int[8];
//		for (int i = 0; i < colors.length; i++) {
//			colors[i] = ThreadLocalRandom.current().nextInt(0xFFFFFF);
//		}
//
//		BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
//		for (int x = 0; x < image.getWidth(); x++) {
//			for (int y = 0; y < image.getHeight(); y++) {
//				double divX = (Math.sin(x * 0.01) * 0.5 + 0.5) * 6 + 3;
//				double divY = (Math.cos(y * 0.02) * 0.5 + 0.5) * 6 + 3;
//				int pos = (int) (x / divX) + (int) (y / divY);
//				int color = colors[pos % colors.length];
//				image.setRGB(x, y, color);
//			}
//		}
//
//		ImageIO.write(image, "PNG", new File("source-test/dxt-death-checker.png"));
	}

	public static ByteBuffer createRGBABuffer(BufferedImage image) {
		var result = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				var argb = image.getRGB(x, y);
				var rgba = (argb << 8) | ((argb >> 24) & 0xFF);

				result.putInt(rgba);
			}
		}

		return result.flip();
	}

	public static BufferedImage createBufferedImage(int width, int height, ByteBuffer buffer) {
		var result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgba = buffer.getInt();
				int argb = (rgba >> 8) | ((rgba & 0xFF) << 24);
				result.setRGB(x, y, argb);
			}
		}

		return result;
	}
}
