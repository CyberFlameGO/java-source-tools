package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.jst.specification.map.bsp.lumps.Lump;

import java.util.Arrays;

public final class LumpFace extends Lump {

	public short planeNumber;
	public byte side;
	public byte onNode;
	public int firstEdge;
	public short edgeCount;
	public short textureInfo;
	public short displacementInfo;
	public short surfaceFogVolumeID;
	@LengthStatic(4)
	public byte[] styles;
	public int lightOffset;
	public float area;
	@LengthStatic(2)
	public int[] lightmapTextureMinsInLuxels;
	@LengthStatic(2)
	public int[] lightmapTexturesSizeInLuxels;
	public int originalFace;
	public short primitiveCount;
	public short firstPrimitiveID;
	public int smoothingGroups;

	@Override
	public String toString() {
		return "LumpFace{" +
				"planeNumber=" + planeNumber +
				", side=" + side +
				", onNode=" + onNode +
				", firstEdge=" + firstEdge +
				", edgeCount=" + edgeCount +
				", textureInfo=" + textureInfo +
				", displacementInfo=" + displacementInfo +
				", surfaceFogVolumeID=" + surfaceFogVolumeID +
				", styles=" + Arrays.toString(styles) +
				", lightOffset=" + lightOffset +
				", area=" + area +
				", lightmapTextureMinsInLuxels=" + Arrays.toString(lightmapTextureMinsInLuxels) +
				", lightmapTexturesSizeInLuxels=" + Arrays.toString(lightmapTexturesSizeInLuxels) +
				", originalFace=" + originalFace +
				", primitiveCount=" + primitiveCount +
				", firstPrimitiveID=" + firstPrimitiveID +
				", smoothingGroups=" + smoothingGroups +
				'}';
	}
}
