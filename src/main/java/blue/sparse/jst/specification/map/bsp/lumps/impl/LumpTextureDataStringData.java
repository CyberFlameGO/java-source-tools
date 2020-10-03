package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.binary.annotation.NullTerminated;
import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public class LumpTextureDataStringData extends Lump {
	public transient long started;

	public LumpTextureDataStringData(long started) {
		this.started = started;
	}

	@NullTerminated
	public String name;
}
