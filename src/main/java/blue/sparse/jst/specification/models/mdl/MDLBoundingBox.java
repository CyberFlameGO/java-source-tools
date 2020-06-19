package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;
import org.joml.Vector3f;

public final class MDLBoundingBox {
	public int bone;
	public int group;
	public Vector3f bbmin;
	public Vector3f bbmax;
	public int szhitboxnameindex;

	@LengthStatic(8)
	public int[] unused;
}
