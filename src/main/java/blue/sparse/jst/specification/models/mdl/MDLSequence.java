package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;
import org.joml.Vector3f;

public final class MDLSequence {
	public int baseptr;
	public int szlabelindex;
	public int szactivitynameindex;
	public int flags;		// looping/non-looping flags
	public int activity;	// initialized at loadtime to game DLL values
	public int actweight;
	public int numevents;
	public int eventindex;
	public Vector3f bbmin;		// per sequence bounding box
	public Vector3f bbmax;
	public int numblends;
	public int animindexindex;
	public int movementindex;	// [blend] float array for blended movement

	@LengthStatic(2)
	public int[] groupsize;
	@LengthStatic(2)
	public int[] paramindex;	// X, Y, Z, XR, YR, ZR
	@LengthStatic(2)
	public float[] paramstart;	// local (0..1) starting value
	@LengthStatic(2)
	public float[] paramend;	// local (0..1) ending value

	public int paramparent;
	public float fadeintime;		// ideal cross fate in time (0.2 default)
	public float fadeouttime;	// ideal cross fade out time (0.2 default)
	public int localentrynode;		// transition node at entry
	public int localexitnode;		// transition node at exit
	public int nodeflags;		// transition rules
	public float entryphase;		// used to match entry gait
	public float exitphase;		// used to match exit gait
	public float lastframe;		// frame that should generation EndOfSequence
	public int nextseq;		// auto advancing sequences
	public int pose;			// index of delta animation between end and nextseq
	public int numikrules;
	public int numautolayers;	//
	public int autolayerindex;
	public int weightlistindex;
	public int posekeyindex;
	public int numiklocks;
	public int iklockindex;
	public int keyvalueindex;
	public int keyvaluesize;
	public int cycleposeindex;		// index of pose parameter to use as cycle index
	public int activitymodifierindex;
	public int numactivitymodifiers;

	@LengthStatic(5)
	public int[] unused;		// remove/add as appropriate (grow back to 8 ints on version change!)

	/*
	float				*pPoseKey( int iParam, int iAnim ) const { return (float *)(((byte *)this) + posekeyindex) + iParam * groupsize[0] + iAnim; }
	float				poseKey( int iParam, int iAnim ) const { return *(pPoseKey( iParam, iAnim )); }
	inline mstudioevent_t *pEvent( int i ) const { Assert( i >= 0 && i < numevents); return (mstudioevent_t *)(((byte *)this) + eventindex) + i; };
	inline char * const pszLabel( void ) const { return ((char *)this) + szlabelindex; }
	inline studiohdr_t	*pStudiohdr( void ) const { return (studiohdr_t *)(((byte *)this) + baseptr); }
	inline char * const pszActivityName( void ) const { return ((char *)this) + szactivitynameindex; }
	inline int			anim( int x, int y ) const
	{
		if ( x >= groupsize[0] )
		{
			x = groupsize[0] - 1;
		}

		if ( y >= groupsize[1] )
		{
			y = groupsize[ 1 ] - 1;
		}

		int offset = y * groupsize[0] + x;
		short *blends = (short *)(((byte *)this) + animindexindex);
		int value = (int)blends[ offset ];
		return value;
	}
	inline mstudioautolayer_t *pAutolayer( int i ) const { Assert( i >= 0 && i < numautolayers); return (mstudioautolayer_t *)(((byte *)this) + autolayerindex) + i; };
	inline float		*pBoneweight( int i ) const { return ((float *)(((byte *)this) + weightlistindex) + i); };
	inline float		weight( int i ) const { return *(pBoneweight( i)); };
	inline mstudioiklock_t *pIKLock( int i ) const { Assert( i >= 0 && i < numiklocks); return (mstudioiklock_t *)(((byte *)this) + iklockindex) + i; };
	inline const char * KeyValueText( void ) const { return keyvaluesize != 0 ? ((char *)this) + keyvalueindex : NULL; }
	inline mstudioactivitymodifier_t *pActivityModifier( int i ) const { Assert( i >= 0 && i < numactivitymodifiers); return activitymodifierindex != 0 ? (mstudioactivitymodifier_t *)(((byte *)this) + activitymodifierindex) + i : NULL; };
	 */
}
