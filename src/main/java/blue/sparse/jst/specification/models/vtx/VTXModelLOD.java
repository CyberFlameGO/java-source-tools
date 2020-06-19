package blue.sparse.jst.specification.models.vtx;

import blue.sparse.binary.annotation.ArrayPointer;

public class VTXModelLOD {
	@ArrayPointer
	public VTXMesh[] meshes;
	public float switchPoint;
}
