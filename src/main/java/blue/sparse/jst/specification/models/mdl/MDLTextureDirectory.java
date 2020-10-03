package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.ArrayPointer;
import blue.sparse.binary.annotation.NullTerminated;
import blue.sparse.binary.annotation.ValuePointer;
import blue.sparse.binary.util.PointerOffset;

public class MDLTextureDirectory {
	@ValuePointer(offset = PointerOffset.ZERO)
	@NullTerminated
	public String path;

	@Override
	public String toString() {
		return "MDLTextureDirectory{" +
				"path='" + path + '\'' +
				'}';
	}
}
