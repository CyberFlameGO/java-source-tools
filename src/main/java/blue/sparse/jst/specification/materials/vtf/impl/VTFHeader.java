package blue.sparse.jst.specification.materials.vtf.impl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.binary.annotation.LittleEndian;

@LittleEndian
public final class VTFHeader {

	@LengthStatic(4)
	public byte[] signature;
	@LengthStatic(2)
	public int[] version;
	public int headerSize;
	public short width;
	public short height;
	public int flags;
	public short frames;
	public short firstFrame;
	@LengthStatic(4)
	public byte[] padding0;
	@LengthStatic(3)
	public float[] reflectivity;
	@LengthStatic(4)
	public byte[] padding1;
	public float bumpmapScale;
	public int highResImageFormat;
	public byte mipmapCount;
	public int lowResImageFormat;
	public byte lowResImageWidth;
	public byte lowResImageHeight;
	public short depth;

	@LengthStatic(3)
	public transient byte[] padding2;
	public transient int numResources;
//
//	public void postBinaryRead(RandomAccessReadableData data) {
//		VTFSpecification.INSTANCE.readHeader(data, this);
//	}
}
