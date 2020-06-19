package blue.sparse.jst.specification.models.vtx;

import blue.sparse.binary.annotation.ArrayPointer;

public class VTXStripGroup {
	@ArrayPointer
	public VTXVertex[] vertices;

	@ArrayPointer
	public short[] indices;

	@ArrayPointer
	public VTXStrip[] strips;
	public byte flags;
}
