package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthPointerField;
import blue.sparse.binary.annotation.ValuePointerField;

public final class MDLHitBoxSet {
	public int sznameindex;
	public int numhitboxes;
	public int hitboxindex;
	/*
	inline char * const	pszName( void ) const { return ((char *)this) + sznameindex; }
	 */

	@LengthPointerField("numhitboxes")
	@ValuePointerField("hitboxindex")
	public MDLBoundingBox[] boundingBoxes;
}