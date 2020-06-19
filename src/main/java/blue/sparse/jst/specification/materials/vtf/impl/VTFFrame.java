package blue.sparse.jst.specification.materials.vtf.impl;

import java.util.List;

public class VTFFrame {

	private VTFMipmap mipmap;
	private int index;

	private List<VTFFace> faces;

	public VTFFrame(VTFMipmap mipmap, int index, List<VTFFace> faces) {
		this.mipmap = mipmap;
		this.index = index;
		this.faces = faces;
	}

	public int getIndex() {
		return index;
	}

	public List<VTFFace> getFaces() {
		return faces;
	}

	public VTFMipmap getMipmap() {
		return mipmap;
	}
}
