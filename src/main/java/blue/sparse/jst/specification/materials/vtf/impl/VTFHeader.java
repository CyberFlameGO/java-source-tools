package blue.sparse.jst.specification.materials.vtf.impl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.binary.annotation.LittleEndian;

import java.util.Arrays;

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
	public transient short depth;

	@LengthStatic(3)
	public transient byte[] padding2;
	public transient int numResources;

	@Override
	public String toString() {
		return "VTFHeader{" +
				"signature=" + Arrays.toString(signature) +
				", version=" + Arrays.toString(version) +
				", headerSize=" + headerSize +
				", width=" + width +
				", height=" + height +
				", flags=" + flags +
				", frames=" + frames +
				", firstFrame=" + firstFrame +
				", padding0=" + Arrays.toString(padding0) +
				", reflectivity=" + Arrays.toString(reflectivity) +
				", padding1=" + Arrays.toString(padding1) +
				", bumpmapScale=" + bumpmapScale +
				", highResImageFormat=" + highResImageFormat +
				", mipmapCount=" + mipmapCount +
				", lowResImageFormat=" + lowResImageFormat +
				", lowResImageWidth=" + lowResImageWidth +
				", lowResImageHeight=" + lowResImageHeight +
				", depth=" + depth +
				", padding2=" + Arrays.toString(padding2) +
				", numResources=" + numResources +
				'}';
	}
}
