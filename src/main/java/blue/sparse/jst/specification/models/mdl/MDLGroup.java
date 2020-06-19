package blue.sparse.jst.specification.models.mdl;

public final class MDLGroup {
	public int szlabelindex;    // textual name
	public int sznameindex;    // file name

	/*
	inline char * const pszLabel( void ) const { return ((char *)this) + szlabelindex; }
	inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
	*/
}
