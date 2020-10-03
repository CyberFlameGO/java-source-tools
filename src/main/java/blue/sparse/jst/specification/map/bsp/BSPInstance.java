package blue.sparse.jst.specification.map.bsp;

import blue.sparse.jst.specification.Instance;
import blue.sparse.jst.specification.map.bsp.impl.BSPHeader;
import blue.sparse.jst.specification.map.bsp.lumps.LumpData;

public final class BSPInstance extends Instance<BSPInstance> {

	private final BSPHeader header;
	private final LumpData data;

	public BSPInstance(BSPHeader header, LumpData data) {
		super(BSPSpecification.INSTANCE);
		this.header = header;
		this.data = data;
	}

	public BSPHeader getHeader() {
		return header;
	}

	public LumpData getData() {
		return data;
	}
}
