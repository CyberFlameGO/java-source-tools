package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;
import org.joml.Vector3f;

public final class LumpTextureData extends Lump {

	public Vector3f reflectivity;
	public int nameStringTableIndex;
	public int width;
	public int height;
	public int viewWidth;
	public int viewHeight;

	@Override
	public String toString() {
		return "LumpTextureData{" +
				"reflectivity=" + reflectivity +
				", nameStringTableID=" + nameStringTableIndex +
				", width=" + width +
				", height=" + height +
				", viewWidth=" + viewWidth +
				", viewHeight=" + viewHeight +
				'}';
	}
}


