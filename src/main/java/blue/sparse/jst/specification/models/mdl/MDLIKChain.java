package blue.sparse.jst.specification.models.mdl;

public final class MDLIKChain {
	public int sznameindex;
	public int linktype;
	public int numlinks;
	public int linkindex;

	/*
	inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
	inline mstudioiklink_t *pLink( int i ) const { return (mstudioiklink_t *)(((byte *)this) + linkindex) + i; };
	// FIXME: add unused entries
	 */

}
