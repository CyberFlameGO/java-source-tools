package blue.sparse.jst.specification.materials.vtf;

import blue.sparse.jst.specification.materials.vtf.image.*;

public enum VTFImageDataFormat {
	RGBA8888,
	ABGR8888,
	RGB888,
	BGR888(new BGR888Format()),
	RGB565,
	I8,
	IA88,
	P8,
	A8,
	RGB888_BLUESCREEN,
	BGR888_BLUESCREEN,
	ARGB8888,
	BGRA8888(new BGRA8888Format()),
	DXT1(new DXT1Format()),
	DXT3(new DXT3Format()),
	DXT5(new DXT5Format()),
	BGRX8888,
	BGR565(new BGR565Format()),
	BGRX5551,
	BGRA4444,
	DXT1_ONEBITALPHA,
	BGRA5551,
	UV88(new UV88Format()),
	UVWQ8888,
	RGBA16161616F,
	RGBA16161616,
	UVLX8888;

	private ImageFormat format;

	VTFImageDataFormat(ImageFormat format) {
		this.format = format;
	}

	VTFImageDataFormat() {
		this.format = null;
	}

	public ImageFormat getFormat() {
		return format;
	}
}
