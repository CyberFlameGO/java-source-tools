package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLAttachment {
	public int sznameindex;
	public int flags;
	public int localbone;

	@LengthStatic(12)
	public float[] local;

	@LengthStatic(8)
	public int[] unused;

	/*
	inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
	 */
}
