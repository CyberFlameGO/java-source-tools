package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class MDLBone {
	public int sznameindex;
	public int parent;

	@LengthStatic(6)
	public int[] bonecontroller;

	public Vector3f pos;
	public Quaternionf quat;

	public Vector3f rot;
	public Vector3f posscale;
	public Vector3f rotscale;
	@LengthStatic(12)
	public float[] poseToBone;
	public Quaternionf qAlignment;
	public int flags;
	public int proctype;
	public int procindex;
	public int physicsbone;
	public int surfacepropidx;
	public int contents;

	@LengthStatic(8)
	public int[] unused;
	/*
public inline char * const pszName( void ) const { return ((char *)this) + sznameindex; }
public inline void *pProcedure( ) const { if (procindex == 0) return NULL; else return  (void *)(((byte *)this) + procindex); };
public inline char * const pszSurfaceProp( void ) const { return ((char *)this) + surfacepropidx; }
	 */
}
