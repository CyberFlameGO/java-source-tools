package blue.sparse.jst.specification.materials.vtf.impl;

import java.util.List;

public class VTFMipmap {

	private VTFInstance instance;
	private int index;
	private int width;
	private int height;

	private List<VTFFrame> frames;

	public VTFMipmap(VTFInstance instance, int index, int width, int height, List<VTFFrame> frames) {
		this.instance = instance;
		this.index = index;
		this.width = width;
		this.height = height;
		this.frames = frames;
	}

	public int getIndex() {
		return index;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<VTFFrame> getFrames() {
		return frames;
	}

	public VTFInstance getInstance() {
		return instance;
	}
}
