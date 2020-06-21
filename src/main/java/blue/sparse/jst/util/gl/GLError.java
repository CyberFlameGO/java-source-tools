package blue.sparse.jst.util.gl;

import java.util.*;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.GL_INVALID_FRAMEBUFFER_OPERATION;

public enum GLError {

    INVALID_ENUM(GL_INVALID_ENUM),
    INVALID_VALUE(GL_INVALID_VALUE),
    INVALID_OPERATION(GL_INVALID_OPERATION),
    INVALID_FRAMEBUFFER_OPERATION(GL_INVALID_FRAMEBUFFER_OPERATION),
    OUT_OF_MEMORY(GL_OUT_OF_MEMORY);

    public final int id;

    GLError(int id) {
        this.id = id;
    }

    public static void clearErrors() {
        while (glGetError() != GL_NO_ERROR) {
            Thread.onSpinWait();
        }
    }

    public static GLError get(int id) {
        for (var glError : values()) {
            if (glError.id == id) {
                return glError;
            }
        }

        return null;
    }

    public static String getErrors() {
        int error = glGetError();

        if (error != GL_NO_ERROR) {
            return accumulateErrors(error);
        }

        return null;
    }

    public static void glCall(Runnable runnable) {
        clearErrors();

        runnable.run();

        var errors = getErrors();

        if (errors != null) {
            throw new IllegalStateException("OpenGL errors: " + errors);
        }
    }

    public static <T> T glCall(Supplier<T> supplier) {
        clearErrors();

        T result = supplier.get();

        var errors = getErrors();

        if (errors != null) {
            throw new IllegalStateException("OpenGL errors: " + errors);
        }

        return result;
    }

    private static String accumulateErrors(int initial) {
        List<GLError> result = new ArrayList<>();

        int errorID = initial;

        while (errorID != GL_NO_ERROR) {
            result.add(get(errorID));
            errorID = glGetError();
        }

        return result.toString();
    }
}
