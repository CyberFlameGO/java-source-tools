package blue.sparse.jst.specification.models.vtx;

import blue.sparse.binary.annotation.ArrayPointer;

public class VTXMesh {
	@ArrayPointer
	public VTXStripGroup[] stripGroups;

	public byte flags;

}
