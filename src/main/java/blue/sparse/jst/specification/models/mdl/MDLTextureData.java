package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.binary.annotation.NullTerminated;
import blue.sparse.binary.annotation.ValuePointer;

import java.util.Arrays;

public final class MDLTextureData {

	@ValuePointer()
	@NullTerminated
	public String name;
	public int flags;
	public int used;
	public int unused;
	public int material;
	public int clientMaterial;

	@LengthStatic(10)
	public int[] unused2;

	@Override
	public String toString() {
		return "MDLTextureData{" +
				"name='" + name + '\'' +
				", flags=" + flags +
				", used=" + used +
				", unused=" + unused +
				", material=" + material +
				", clientMaterial=" + clientMaterial +
				", unused2=" + Arrays.toString(unused2) +
				'}';
	}
}
