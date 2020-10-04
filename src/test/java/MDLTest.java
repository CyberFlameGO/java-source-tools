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

		for (VTXBodyPart bodyPart : vtxHeader.bodyParts) {
			for (VTXModel model : bodyPart.models) {
				for (VTXStripGroup stripGroup : model.modelLODs[0].meshes[0].stripGroups) {
					short[] indicesPool = stripGroup.indices;
					VTXVertex[] verticesPool = stripGroup.vertices;

					for (VTXStrip strip : stripGroup.strips) {

						for (int i = 0; i < strip.numIndices; i++) {
							short index = indicesPool[i + strip.indexOffset];

							VTXVertex vtxVertex = verticesPool[index];
							short id = vtxVertex.origMeshVertID;
							VVDVertexData vertex = vvdHeader.vertices[id];
							System.out.println("vertex = " + vertex);
							if (i % 3 == 0) {
								System.out.println();
							}
						}

//						for (int i = 0; i < strip.numVerts; i++) {
//							VTXVertex vtxVertex = verticesPool[i + strip.vertOffset];
//							short id = vtxVertex.origMeshVertID;
//							VVDVertexData vertex = vvdHeader.vertices[id];
//						}
					}
				}
			}
		}
	}
}
