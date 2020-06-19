package blue.sparse.jst.specification.models.mdl;

public final class MDLFlexRule {
	public int flex;
	public int numops;
	public int opindex;
	/*
	inline mstudioflexop_t *iFlexOp( int i ) const { return  (mstudioflexop_t *)(((byte *)this) + opindex) + i; };
	 */
}
