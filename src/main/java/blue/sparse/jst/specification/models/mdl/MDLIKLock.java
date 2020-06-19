package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLIKLock {
	public int chain;
	public float flPosWeight;
	public float flLocalQWeight;
	public int flags;
	@LengthStatic(4)
	public int[] unused;
}
