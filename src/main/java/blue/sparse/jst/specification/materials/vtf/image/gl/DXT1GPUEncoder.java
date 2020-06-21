package blue.sparse.jst.specification.materials.vtf.image.gl;

import blue.sparse.jst.specification.materials.vtf.image.ImageFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import xyz.eutaxy.util.data.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.*;

import static blue.sparse.jst.util.gl.GLError.glCall;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43C.*;

public class DXT1GPUEncoder extends ImageFormat {
	private static int createVertexBuffer() {
		int bufferID = glCall(() -> glGenBuffers());
		glCall(() -> glBindBuffer(GL_ARRAY_BUFFER, bufferID));
		var data = ByteBuffer.allocateDirect(8 * 6).order(ByteOrder.nativeOrder());
		data.putFloat(-1f).putFloat(-1f);
		data.putFloat(-1f).putFloat(+1f);
		data.putFloat(+1f).putFloat(+1f);
		data.putFloat(+1f).putFloat(+1f);
		data.putFloat(+1f).putFloat(-1f);
		data.putFloat(-1f).putFloat(-1f);
		data.flip();

		glCall(() -> glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW));
		return bufferID;
	}

	private static int createVertexArray() {
		int vertexArrayID = glCall(() -> glGenVertexArrays());
		glCall(() -> glBindVertexArray(vertexArrayID));
		glCall(() -> glEnableVertexAttribArray(0));
		glCall(() -> glVertexAttribFormat(0, 2, GL_FLOAT, false, 0));
		glCall(() -> glVertexAttribBinding(0, 0));

		return vertexArrayID;
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

	private static int setupFrameBuffer(int targetWidth, int targetHeight, int outputTextureID) {
		int targetBufferID = glCall(() -> glGenFramebuffers());
		glCall(() -> glBindFramebuffer(GL_FRAMEBUFFER, targetBufferID));
		glCall(() -> glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, outputTextureID, 0));
		glCall(() -> glDrawBuffer(GL_COLOR_ATTACHMENT0));
		glCall(() -> glViewport(0, 0, targetWidth, targetHeight));
		glCall(() -> glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT));
		return targetBufferID;
	}

	private static int setupShader() {
		int vertexShaderID = compileShader("gl/screen.vs", GL_VERTEX_SHADER);
		int fragmentShaderID = compileShader("gl/dxt1.fs", GL_FRAGMENT_SHADER);
		int shaderProgramID = glCall(() -> glCreateProgram());
		glCall(() -> glAttachShader(shaderProgramID, vertexShaderID));
		glCall(() -> glAttachShader(shaderProgramID, fragmentShaderID));
		glCall(() -> glLinkProgram(shaderProgramID));

		if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == 0) {
			throw new IllegalStateException(String.format("Shader program linking error: %n%s",
					glCall(() -> glGetProgramInfoLog(shaderProgramID))));
		}

		glCall(() -> glValidateProgram(shaderProgramID));

		if (glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS) == 0) {
			throw new IllegalStateException(String.format("Shader program validation error: %n%s",
					glCall(() -> glGetProgramInfoLog(shaderProgramID))));
		}

		glCall(() -> glDeleteShader(vertexShaderID));
		glCall(() -> glDeleteShader(fragmentShaderID));
		glCall(() -> glUseProgram(shaderProgramID));
		return shaderProgramID;
	}

	private static int compileShader(String resourcePath, int shaderType) {
		int shaderID = glCall(() -> glCreateShader(shaderType));

		var resource = ClassLoader.getSystemResource(resourcePath);
		URLConnection connection;
		ReadableData dataIn;
		try {
			connection = resource.openConnection();
			dataIn = Data.readInputStream(connection.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		var content = dataIn.readBytes(connection.getContentLength());
		var sourceString = new String(content);

		glCall(() -> glShaderSource(shaderID, sourceString));
		glCall(() -> glCompileShader(shaderID));

		var log = glCall(() -> glGetShaderInfoLog(shaderID));

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
			throw new IllegalArgumentException(String.format("Shader compile error: (%s): %n%s", resourcePath, log));
		}

		if (!log.isBlank()) {
			System.out.printf("Shader info log (%s): %s", resourcePath, log);
		}

		return shaderID;
	}

	private static int setupOutputTexture(int targetWidth, int targetHeight) {
		int outputTextureID = glCall(() -> glGenTextures());
		glCall(() -> glBindTexture(GL_TEXTURE_2D, outputTextureID));
		glCall(() -> glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA16UI, targetWidth, targetHeight));
		return outputTextureID;
	}

	private static int setupInputTexture(BufferedImage image) {
		int inputTextureID = glCall(() -> glGenTextures());
		glCall(() -> glBindTexture(GL_TEXTURE_2D, inputTextureID));
		glCall(() -> glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGB8, image.getWidth(), image.getHeight()));
		glCall(() -> glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST));
		glCall(() -> glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST));
		return inputTextureID;
	}

	private static long initGLFW() {
		glfwInit();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

		var windowID = glfwCreateWindow(1, 1, "", 0, 0);
		glfwMakeContextCurrent(windowID);

		return windowID;
	}

	@Override
	public BufferedImage read(int width, int height, RandomAccessReadableData data) {
		throw new UnsupportedOperationException("DXT1GPUEncoder cannot read/decode.");
	}

	@Override
	public void write(BufferedImage image, WritableData data) {
		long initStartTime = System.currentTimeMillis();
		long windowID = initGLFW();
		glfwPollEvents();

		GL.createCapabilities();
		glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
			System.out.println(GLDebugMessageCallback.getMessage(length, message));
		}, 0L);

		var targetWidth = image.getWidth() / 4;
		var targetHeight = image.getHeight() / 4;
		int outputTextureID = setupOutputTexture(targetWidth, targetHeight);
		int targetBufferID = setupFrameBuffer(targetWidth, targetHeight, outputTextureID);

		int shaderProgramID = setupShader();

		var vertexArrayID = createVertexArray();
		var vertexBufferID = createVertexBuffer();
		long initEndTime = System.currentTimeMillis();
		long initDiffTime = initEndTime - initStartTime;
		int inputTextureID = setupInputTexture(image);
		System.out.println("GL initialization took " + initDiffTime + "ms");

		int sections = 8;
		ByteBuffer buffer = createRGBABuffer(image);
		glCall(() -> glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer));

		int uniformSizeLocation = glGetUniformLocation(shaderProgramID, "uInputTextureSize");
		int uniformScreenSectionsLocation = glGetUniformLocation(shaderProgramID, "uScreenSizeInSections");
		int uniformSectionLocation = glGetUniformLocation(shaderProgramID, "uSection");
		glUniform2ui(uniformSizeLocation, image.getWidth(), image.getHeight());
		glUniform2f(uniformScreenSectionsLocation, sections, sections);

		glCall(() -> glBindTexture(GL_TEXTURE_2D, inputTextureID));
		glCall(() -> glBindVertexBuffer(0, vertexBufferID, 0, 8));

		glCall(() -> glBindVertexArray(vertexArrayID));

		for (int y = 0; y < sections; y++) {
			System.out.println("Rendering section: " + y);
			for (int x = 0; x < sections; x++) {
				glUniform2f(uniformSectionLocation, x, y);
				glCall(() -> glDrawArrays(GL_TRIANGLES, 0, 6));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done rendering");
		glCall(() -> glBindFramebuffer(GL_FRAMEBUFFER, 0));
		glCall(() -> glBindTexture(GL_TEXTURE_2D, outputTextureID));

//		var sectionWidth = targetWidth / sections;
//		var sectionHeight = targetHeight / sections;
//		ByteBuffer targetBuffer = BufferUtils.createByteBuffer(targetWidth * sectionHeight * 4 * 2);
//		for (int y = 0; y < sections; y++) {
////			for (int x = 0; x < sections; x++) {
//				System.out.println("Downloading section "+y);
////				int finalX = x;
//				int finalY = y;
//				targetBuffer.clear();
//				glCall(() -> glGetTextureSubImage(
//						outputTextureID,
//						0,
//						0,
//						finalY * sectionHeight,
//						0,
//						targetWidth,
//						sectionHeight,
//						1,
//						GL_RGBA_INTEGER,
//						GL_UNSIGNED_SHORT,
//						targetBuffer
//				));
//				Thread.yield();
//				targetBuffer.clear();
//				data.write(Data.wrapByteBuffer(targetBuffer));
////			}
//		}

		ByteBuffer targetBuffer = BufferUtils.createByteBuffer(targetWidth * targetHeight * 4 * 2);
//		long capacity = targetWidth * targetHeight * 4 * 2;
//		int pixelBufferID = glCall(() -> glGenBuffers());
//		glCall(() -> glBindBuffer(GL_PIXEL_PACK_BUFFER, pixelBufferID));
//		glCall(() -> glBufferStorage(GL_PIXEL_PACK_BUFFER,capacity, GL_MAP_READ_BIT));
//		glCall(() -> glReadPixels(0, 0, targetWidth, targetHeight, GL_RGBA_INTEGER, GL_UNSIGNED_SHORT, 0));
//		ByteBuffer targetBuffer = glCall(() -> glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY));
		glCall(() -> glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA_INTEGER, GL_UNSIGNED_SHORT, targetBuffer));
		targetBuffer.clear();
		data.write(Data.wrapByteBuffer(targetBuffer));
		long renderEndTime = System.currentTimeMillis();
		long renderDiffTime = renderEndTime - initEndTime;
		System.out.println("Rendering took " + renderDiffTime + "ms");
//		glCall(() -> glDeleteBuffers(pixelBufferID));

		glCall(() -> glDeleteVertexArrays(vertexArrayID));
		glCall(() -> glDeleteBuffers(vertexBufferID));
		glCall(() -> glDeleteProgram(shaderProgramID));
		System.out.println("glDeleteFramebuffers");
		glCall(() -> glDeleteFramebuffers(targetBufferID));
		System.out.println("glDeleteTextures out");
		glCall(() -> glDeleteTextures(outputTextureID));
		System.out.println("glDeleteTextures in");
		glCall(() -> glDeleteTextures(inputTextureID));

		System.out.println("destroy window");
		glfwDestroyWindow(windowID);
		System.out.println("terminating");
		glfwTerminate();
		System.out.println("done terminating");
	}
}
