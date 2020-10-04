package blue.sparse.jst.specification.map.bsp.lumps;

import blue.sparse.binary.Binary;
import blue.sparse.jst.specification.map.bsp.lumps.impl.*;
import org.jetbrains.annotations.Nullable;
import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.lang.reflect.Constructor;

public enum LumpType {
	ENTITIES,
	PLANES(LumpPlane.class),
	TEXDATA(LumpTextureData.class),
	VERTEXES(LumpVertex.class),
	VISIBILITY,
	NODES,
	TEXINFO(LumpTextureInfo.class),
	FACES(LumpFace.class),
	LIGHTING(LumpLighting.class),
	OCCLUSION,
	LEAFS,
	FACEIDS,
	EDGES(LumpEdge.class),
	SURFEDGES(LumpSurfaceEdge.class),
	MODELS,
	WORLDLIGHTS,
	LEAFFACES,
	LEAFBRUSHES,
	BRUSHES(LumpBrush.class),
	BRUSHSIDES(LumpBrushSide.class),
	AREAS,
	AREAPORTALS,
	PORTALS,
	//	UNUSED0,
//	PROPCOLLISION,
//	CLUSTERS,
	UNUSED1,
	//	PROPHULLS,
//	PORTALVERTS,
	UNUSED2,
	//	PROPHULLVERTS,
//	CLUSTERPORTALS,
	UNUSED3,
	//	PROPTRIS,
	DISPINFO,
	ORIGINALFACES(LumpFace.class),
	PHYSDISP,
	PHYSCOLLIDE,
	VERTNORMALS,
	VERTNORMALINDICES,
	DISP_LIGHTMAP_ALPHAS,
	DISP_VERTS,
	DISP_LIGHTMAP_SAMPLE_POSITIONS,
	GAME_LUMP,
	LEAFWATERDATA,
	PRIMITIVES,
	PRIMVERTS,
	PRIMINDICES,
	PAKFILE,
	CLIPPORTALVERTS,
	CUBEMAPS,
	TEXDATA_STRING_DATA(LumpTextureDataStringData.class),
	TEXDATA_STRING_TABLE(LumpTextureDataStringTable.class),
	OVERLAYS,
	LEAFMINDISTTOWATER,
	FACE_MACRO_TEXTURE_INFO,
	DISP_TRIS,
	PHYSCOLLIDESURFACE,
	//	PROP_BLOB,
	WATEROVERLAYS,
	//	LIGHTMAPPAGES,
	LEAF_AMBIENT_INDEX_HDR,
	//	LIGHTMAPPAGEINFOS,
	LEAF_AMBIENT_INDEX,
	LIGHTING_HDR,
	WORLDLIGHTS_HDR,
	LEAF_AMBIENT_LIGHTING_HDR,
	LEAF_AMBIENT_LIGHTING,
	XZIPPAKFILE,
	FACES_HDR,
	MAP_FLAGS,
	OVERLAY_FADES,
	OVERLAY_SYSTEM_LEVELS,
	PHYSLEVEL,
	DISP_MULTIBLEND;

	private final Class<? extends Lump> lumpClass;


	LumpType(Class<? extends Lump> clazz) {
		this.lumpClass = clazz;
	}

	LumpType() {
		this.lumpClass = null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Lump> T read(RandomAccessReadableData data, long info) throws ReflectiveOperationException {
		if (lumpClass == null) {
			return null;
		}

		try {
			Constructor<? extends Lump> constructor = lumpClass.getConstructor(long.class);
			Lump lump = constructor.newInstance(info);
			return (T) Binary.read(lump, data);
		} catch (NoSuchMethodException ignored) {
			return (T) Binary.read(lumpClass, data);
		}
	}

	@Nullable
	public Class<? extends Lump> getLumpClass() {
		return lumpClass;
	}
}
