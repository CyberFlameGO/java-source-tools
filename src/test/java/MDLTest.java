import blue.sparse.binary.Binary;
import blue.sparse.jst.specification.models.mdl.MDLHeader;
import blue.sparse.jst.specification.models.mdl.MDLTextureData;
import blue.sparse.jst.specification.models.mdl.MDLTextureDirectory;
import blue.sparse.jst.specification.models.vtx.*;
import blue.sparse.jst.specification.models.vvd.VVDHeader;
import blue.sparse.jst.specification.models.vvd.VVDVertexData;
import xyz.eutaxy.util.data.Data;
import xyz.eutaxy.util.data.RandomAccessReadableData;

import java.io.File;
import java.io.FileNotFoundException;

public final class MDLTest {

	private static final String TEST_NAME = "chell/chell";

	public static void main(String[] args) throws FileNotFoundException, ReflectiveOperationException {
		String prefix = "source-test/" + TEST_NAME;
		File mdlFile = new File(prefix + ".mdl");
		File vvdFile = new File(prefix + ".vvd");
		File vtxFile = new File(prefix + ".dx90.vtx");

		RandomAccessReadableData mdlData = Data.readFile(mdlFile);
		RandomAccessReadableData vvdData = Data.readFile(vvdFile);
		RandomAccessReadableData vtxData = Data.readFile(vtxFile);

		MDLHeader mdlHeader = Binary.read(MDLHeader.class, mdlData);
		System.out.println("mdlHeader.numtextures = " + mdlHeader.textureData.length);
		System.out.println("mdlHeader.numcdtextures = " + mdlHeader.textureDirectoryPointers.length);
		VVDHeader vvdHeader = Binary.read(VVDHeader.class, vvdData);
		System.out.println("vvdHeader.version = " + vvdHeader.version);
		VTXHeader vtxHeader = Binary.read(VTXHeader.class, vtxData);
		System.out.println("vtxHeader.numLODs = " + vtxHeader.numLODs);
		System.out.println("vtxHeader.version = " + vtxHeader.version);

		VVDVertexData[] vertices = vvdHeader.vertices;


		for (MDLTextureDirectory dir : mdlHeader.textureDirectoryPointers) {
			System.out.println("dir.path = " + dir.path);
		}

		for (MDLTextureData texture : mdlHeader.textureData) {
			System.out.println(texture);
		}
	}
}
