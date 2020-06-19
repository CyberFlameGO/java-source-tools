package blue.sparse.jst.specification.models.vvd;

import blue.sparse.binary.annotation.*;

@LittleEndian
public final class VVDHeader {

	public int id;
	public int version;
	public int checksum;
	public int numLODs;

	@LengthStatic(8)
	public int[] numLODVertexes;

	@ArrayPointer
	public VVDFixup[] fixups;

	public int vertexTableStart;
	public int tangentDataStart;

	@LengthPointerField("numLODVertexes[0]")
	@ValuePointerField("vertexTableStart")
	public VVDVertexData[] vertices;

	@LengthPointerField("numLODVertexes[0]")
	@ValuePointerField("tangentDataStart")
	public VVDTangentData[] tangents;

}
