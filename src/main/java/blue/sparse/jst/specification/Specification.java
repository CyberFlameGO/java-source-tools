package blue.sparse.jst.specification;

import xyz.eutaxy.util.data.RandomAccessReadableData;
import xyz.eutaxy.util.data.RandomAccessWritableData;

public abstract class Specification<T extends Instance<T>> {

	public abstract T read(RandomAccessReadableData file);
	public abstract void write(RandomAccessWritableData data, T instance);

	public abstract String getFileExtension();
}
