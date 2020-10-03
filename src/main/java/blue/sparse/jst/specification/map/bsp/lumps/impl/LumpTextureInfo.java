package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.jst.specification.map.bsp.lumps.Lump;
import org.joml.Vector2f;
import org.joml.Vector4f;

public final class LumpTextureInfo extends Lump {

	@LengthStatic(2)
	public Vector4f[] textureVectors;

	@LengthStatic(2)
	public Vector4f[] lightmapVectors;

	public int flags;
	public int textureDataPointer;
}
