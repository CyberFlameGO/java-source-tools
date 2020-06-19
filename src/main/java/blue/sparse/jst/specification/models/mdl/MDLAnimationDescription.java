package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLAnimationDescription {
	public int baseptr;
	public int sznameindex;
	public float fps;
	public int flags;
	public int numframes;
	public int nummovements;
	public int movementindex;
	@LengthStatic(6)
	public int[] unused1;
	public int animblock;
	public int animindex;
	public int numikrules;
	public int ikruleindex;
	public int animblockikruleindex;
	public int numlocalhierarchy;
	public int localhierarchyindex;
	public int sectionindex;
	public int sectionframes;
	public short zeroframespan;
	public short zeroframecount;
	public int zeroframeindex;
	public float zeroframestalltime;

	/*
inline studiohdr_t	*pStudiohdr( void ) const { return (studiohdr_t *)(((byte *)this) + baseptr); }
inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
inline mstudiomovement_t * const pMovement( int i ) const { return (mstudiomovement_t *)(((byte *)this) + movementindex) + i; };
mstudioikrule_t *pIKRule( int i ) const;
byte				*pZeroFrameData( ) const { if (zeroframeindex) return (((byte *)this) + zeroframeindex); else return NULL; };
mstudiolocalhierarchy_t *pHierarchy( int i ) const;
mstudioanim_t *pAnimBlock( int block, int index ) const;
mstudioanim_t *pAnim( int *piFrame, float &flStall ) const;
mstudioanim_t *pAnim( int *piFrame ) const;
inline mstudioanimsections_t * const pSection( int i ) const { return (mstudioanimsections_t *)(((byte *)this) + sectionindex) + i; }
	 */
}