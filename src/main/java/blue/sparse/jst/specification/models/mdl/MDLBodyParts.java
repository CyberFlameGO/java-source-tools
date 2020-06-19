package blue.sparse.jst.specification.models.mdl;

public final class MDLBodyParts {
	public int sznameindex;
	public int nummodels;
	public int base;
	public int modelindex; // index into models array

	/*
	inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
	inline mstudiomodel_t *pModel( int i ) const { return (mstudiomodel_t *)(((byte *)this) + modelindex) + i; };
	 */
}
