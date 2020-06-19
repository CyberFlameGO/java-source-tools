package blue.sparse.jst.specification.models.vtx;

import blue.sparse.binary.annotation.*;

@LittleEndian
public final class VTXHeader {
	public int version;
	public int vertCacheSize;
	public short maxBonesPerStrip;
	public short maxBonesPerTri;
	public int maxBonesPerVert;
	public int checkSum;
	public int numLODs;
	public int materialReplacementListOffset;

	@ArrayPointer
	public VTXBodyPart[] bodyParts;

}
