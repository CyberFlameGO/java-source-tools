package blue.sparse.jst.specification.map.bsp.lumps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LumpData {

	private final Map<LumpType, List<Lump>> data = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <L extends Lump> List<L> get(LumpType type) {
		if (!data.containsKey(type)) {
			return null;
		}

		return (List<L>) data.get(type);
	}

	@SuppressWarnings("unchecked")
	public <L extends Lump> List<L> add(LumpType type, Lump lump) {
		List<Lump> lumps = data.get(type);
		if (lumps == null) {
			lumps = new ArrayList<>();
		}

		lumps.add(lump);
		data.put(type, lumps);
		return (List<L>) lumps;
	}

	public Map<LumpType, List<Lump>> getData() {
		return data;
	}
}
