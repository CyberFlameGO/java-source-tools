package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLBoneController {
	public int bone;
	public int type;
	public float start;
	public float end;
	public int rest;
	public int inputField;

	@LengthStatic(8)
	public int[] unused;
}
