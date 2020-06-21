import org.joml.*;

import java.util.*;

public class TestColorVectors {

	public static void main(String[] args) {
		List<Vector3i> vectors = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					var vector = new Vector3i(x, y, z);
					vectors.add(vector);
				}
			}
		}

		vectors.sort(Comparator.comparingDouble(Vector3i::length));
		for (Vector3i vector : vectors) {
			System.out.printf("vec3(%d, %d, %d)%n", vector.x, vector.y, vector.z);
		}
	}

}
