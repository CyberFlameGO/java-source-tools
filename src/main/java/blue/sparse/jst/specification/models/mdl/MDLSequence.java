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

	/**
	 * struct mstudioseqdesc_t {
	 *   DECLARE_BYTESWAP_DATADESC();
	 *   int baseptr;
	 *   inline studiohdr_t *pStudiohdr() const {
	 *     return (studiohdr_t *)(((u8 *)this) + baseptr);
	 *   }
	 *
	 *   int szlabelindex;
	 *   inline char *const pszLabel() const { return ((char *)this) + szlabelindex; }
	 *
	 *   int szactivitynameindex;
	 *   inline char *const pszActivityName() const {
	 *     return ((char *)this) + szactivitynameindex;
	 *   }
	 *
	 *   int flags;  // looping/non-looping flags
	 *
	 *   int activity;  // initialized at loadtime to game DLL values
	 *   int actweight;
	 *
	 *   int numevents;
	 *   int eventindex;
	 *   inline mstudioevent_t *pEvent(int i) const {
	 *     Assert(i >= 0 && i < numevents);
	 *     return (mstudioevent_t *)(((u8 *)this) + eventindex) + i;
	 *   };
	 *
	 *   Vector bbmin;  // per sequence bounding box
	 *   Vector bbmax;
	 *
	 *   int numblends;
	 *
	 *   // Index into array of shorts which is groupsize[0] x groupsize[1] in length
	 *   int animindexindex;
	 *
	 *   inline int anim(int x, int y) const {
	 *     if (x >= groupsize[0]) {
	 *       x = groupsize[0] - 1;
	 *     }
	 *
	 *     if (y >= groupsize[1]) {
	 *       y = groupsize[1] - 1;
	 *     }
	 *
	 *     int offset = y * groupsize[0] + x;
	 *     short *blends = (short *)(((u8 *)this) + animindexindex);
	 *     int value = (int)blends[offset];
	 *     return value;
	 *   }
	 *
	 *   int movementindex;  // [blend] f32 array for blended movement
	 *   int groupsize[2];
	 *   int paramindex[2];  // X, Y, Z, XR, YR, ZR
	 *   f32 paramstart[2];  // local (0..1) starting value
	 *   f32 paramend[2];    // local (0..1) ending value
	 *   int paramparent;
	 *
	 *   f32 fadeintime;   // ideal cross fate in time (0.2 default)
	 *   f32 fadeouttime;  // ideal cross fade out time (0.2 default)
	 *
	 *   int localentrynode;  // transition node at entry
	 *   int localexitnode;   // transition node at exit
	 *   int nodeflags;       // transition rules
	 *
	 *   f32 entryphase;  // used to match entry gait
	 *   f32 exitphase;   // used to match exit gait
	 *
	 *   f32 lastframe;  // frame that should generation EndOfSequence
	 *
	 *   int nextseq;  // auto advancing sequences
	 *   int pose;     // index of delta animation between end and nextseq
	 *
	 *   int numikrules;
	 *
	 *   int numautolayers;  //
	 *   int autolayerindex;
	 *   inline mstudioautolayer_t *pAutolayer(int i) const {
	 *     Assert(i >= 0 && i < numautolayers);
	 *     return (mstudioautolayer_t *)(((u8 *)this) + autolayerindex) + i;
	 *   };
	 *
	 *   int weightlistindex;
	 *   inline f32 *pBoneweight(int i) const {
	 *     return ((f32 *)(((u8 *)this) + weightlistindex) + i);
	 *   };
	 *   inline f32 weight(int i) const { return *(pBoneweight(i)); };
	 *
	 *   // TODO(d.rattman): make this 2D instead of 2x1D arrays
	 *   int posekeyindex;
	 *   f32 *pPoseKey(int iParam, int iAnim) const {
	 *     return (f32 *)(((u8 *)this) + posekeyindex) + iParam * groupsize[0] + iAnim;
	 *   }
	 *   f32 poseKey(int iParam, int iAnim) const {
	 *     return *(pPoseKey(iParam, iAnim));
	 *   }
	 *
	 *   int numiklocks;
	 *   int iklockindex;
	 *   inline mstudioiklock_t *pIKLock(int i) const {
	 *     Assert(i >= 0 && i < numiklocks);
	 *     return (mstudioiklock_t *)(((u8 *)this) + iklockindex) + i;
	 *   };
	 *
	 *   // Key values
	 *   int keyvalueindex;
	 *   int keyvaluesize;
	 *   inline const char *KeyValueText() const {
	 *     return keyvaluesize != 0 ? ((char *)this) + keyvalueindex : nullptr;
	 *   }
	 *
	 *   int cycleposeindex;  // index of pose parameter to use as cycle index
	 *
	 *   int unused[7];  // remove/add as appropriate (grow back to 8 ints on version
	 *                   // change!)
	 *
	 *   mstudioseqdesc_t() {}
	 *
	 *  private:
	 *   // No copy constructors allowed
	 *   mstudioseqdesc_t(const mstudioseqdesc_t &vOther) = delete;
	 * };
	 */
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
	 */
}
