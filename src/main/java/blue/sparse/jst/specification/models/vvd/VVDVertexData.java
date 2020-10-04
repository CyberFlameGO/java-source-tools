package blue.sparse.jst.specification.models.vvd;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class VVDVertexData {
	public VVDBoneWeight boneWeight;
	public Vector3f vecPosition;
	public Vector3f vecNormal;
	public Vector2f vecTexCoord;

	@Override
	public String toString() {
		return "VVDVertexData{" +
				"boneWeight=" + boneWeight +
				", vecPosition=" + vecPosition +
				", vecNormal=" + vecNormal +
				", vecTexCoord=" + vecTexCoord +
				'}';
	}
}
