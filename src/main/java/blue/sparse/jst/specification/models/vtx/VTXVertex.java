package blue.sparse.jst.specification.models.vtx;

import blue.sparse.binary.annotation.LengthStatic;

public class VTXVertex {
	@LengthStatic(3)
	public byte[] boneWeightIndex;

	public byte numBones;
	public short origMeshVertID;

	@LengthStatic(3)
	public byte[] boneID;
}
