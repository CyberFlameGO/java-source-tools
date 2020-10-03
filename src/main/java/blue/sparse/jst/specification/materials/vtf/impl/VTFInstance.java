package blue.sparse.jst.specification.materials.vtf.impl;

import blue.sparse.jst.specification.Instance;
import blue.sparse.jst.specification.materials.vtf.VTFTextureFlags;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class VTFInstance extends Instance<VTFInstance> {

	private final VTFHeader header;
	private final List<VTFMipmap> mipmaps = new ArrayList<>();
	private final Set<VTFTextureFlags> flags = EnumSet.noneOf(VTFTextureFlags.class);

	public VTFInstance(VTFHeader header) {
		super(VTFSpecification.INSTANCE);
		this.header = header;
	}

	public int getVersionNumber() {
		return (header.version[0] * 10) + header.version[1];
	}

	public String getVersion() {
		return (header.version[0] * 10) + "." + header.version[1];
	}

	public int getWidth() {
		return header.width;
	}

	public int getHeight() {
		return header.height;
	}

	public VTFHeader getHeader() {
		return header;
	}

	public List<VTFMipmap> getMipmaps() {
		return mipmaps;
	}

	public Set<VTFTextureFlags> getFlags() {
		return flags;
	}

	public boolean isFlagSet(VTFTextureFlags flag) {
		return flags.contains(flag);
	}

	public boolean setFlag(VTFTextureFlags flag, boolean state) {
		if (state) {
			return flags.add(flag);
		} else {
			return flags.remove(flag);
		}
	}

	public int getMipmapCount() {
		return getMipmaps().size();
	}

	public int getFrameCount() {
		return getMipmaps().get(0).getFrames().size();
	}

	public BufferedImage getImage() {
		return getImage(0, 0, 0);
	}

	public BufferedImage getImage(int mipmap) {
		return getImage(mipmap, 0, 0);
	}

	public BufferedImage getImage(int mipmap, int frame) {
		return getImage(mipmap, frame, 0);
	}

	public BufferedImage getImage(int mipmap, int frame, int face) {
		return getMipmaps().get(mipmap).getFrames().get(frame).getFaces().get(face).getImage();
	}
}
