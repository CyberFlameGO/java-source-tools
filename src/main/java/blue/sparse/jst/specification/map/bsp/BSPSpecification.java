package blue.sparse.jst.specification.map.bsp;

import blue.sparse.binary.Binary;
import blue.sparse.jst.specification.Specification;
import blue.sparse.jst.specification.map.bsp.impl.BSPHeader;
import blue.sparse.jst.specification.map.bsp.impl.BSPLump;
import blue.sparse.jst.specification.map.bsp.lumps.LumpData;
import blue.sparse.jst.specification.map.bsp.lumps.LumpType;
import xyz.eutaxy.util.data.RandomAccessReadableData;
import xyz.eutaxy.util.data.RandomAccessWritableData;

public class BSPSpecification extends Specification<BSPInstance> {

	public static final BSPSpecification INSTANCE = new BSPSpecification();

	@Override
	public BSPInstance read(RandomAccessReadableData data) {
		BSPHeader header = null;
		try {
			header = Binary.read(BSPHeader.class, data);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		if (header == null) {
			return null;
		}

		LumpData lumpData = new LumpData();
		BSPInstance instance = new BSPInstance(header, lumpData);

		BSPLump[] lumps = header.lumps;
		for (int lumpIndex = 0, lumpsLength = lumps.length; lumpIndex < lumpsLength; lumpIndex++) {
			BSPLump bspLump = lumps[lumpIndex];

			LumpType type = LumpType.values()[lumpIndex];

			int offset = bspLump.offset;
			int end = offset + bspLump.length;

			if (type.getLumpClass() == null) {
				continue;
			}

			data.position(offset);
			while (data.position() < end) {
				try {
					lumpData.add(type, type.read(data, data.position() - offset));
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		}

		return instance;
	}

	@Override
	public void write(RandomAccessWritableData data, BSPInstance instance) {
		//TODO: Not implemented.
	}

	@Override
	public String getFileExtension() {
		return "bsp";
	}
}
