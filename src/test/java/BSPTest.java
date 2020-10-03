import blue.sparse.jst.specification.map.bsp.BSPInstance;
import blue.sparse.jst.specification.map.bsp.BSPSpecification;
import blue.sparse.jst.specification.map.bsp.lumps.*;
import blue.sparse.jst.specification.map.bsp.lumps.impl.LumpTextureData;
import blue.sparse.jst.specification.map.bsp.lumps.impl.LumpTextureDataStringData;
import blue.sparse.jst.specification.map.bsp.lumps.impl.LumpTextureDataStringTable;
import xyz.eutaxy.util.data.Data;
import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class BSPTest {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("source-test/testchmb_a_01.bsp");
		RandomAccessReadableData data = Data.readFile(file);
		System.out.println("Starting...");
		BSPInstance bspInstance = BSPSpecification.INSTANCE.read(data);
		LumpData lumpData = bspInstance.getData();
		List<LumpTextureData> textureData = lumpData.get(LumpType.TEXDATA);
		List<LumpTextureDataStringTable> stringTable = lumpData.get(LumpType.TEXDATA_STRING_TABLE);
		List<LumpTextureDataStringData> stringData = lumpData.get(LumpType.TEXDATA_STRING_DATA);

		for (LumpTextureData texData : textureData) {
			int nameStringTableID = texData.nameStringTableIndex;
			LumpTextureDataStringTable stringTableDatum = stringTable.get(nameStringTableID);
			System.out.println("stringTable.size() = " + stringTable.size());
			System.out.println("stringData.size() = " + stringData.size());
			int byteIndex = stringTableDatum.index;

			for (LumpTextureDataStringData stringDatum : stringData) {
				if(stringDatum.started != byteIndex) {
					continue;
				}

				System.out.println("stringDatum = " + stringDatum.name);
			}
		}
	}

}
