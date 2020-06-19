package blue.sparse.jst.specification.materials.vtf.impl;

import java.awt.image.BufferedImage;

public class VTFFace {

	private VTFFrame frame;
	private int index;

	private BufferedImage image;

	public VTFFace(VTFFrame frame, int index, BufferedImage image) {
		this.frame = frame;
		this.index = index;
		this.image = image;
	}

	public int getIndex() {
		return index;
	}

	public BufferedImage getImage() {
		return image;
	}

	public VTFFrame getFrame() {
		return frame;
	}
}
