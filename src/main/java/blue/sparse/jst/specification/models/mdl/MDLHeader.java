package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.*;
import org.joml.Vector3f;

@LittleEndian
public final class MDLHeader {
	public int id;
	public int version;
	public int checksum;
	@LengthStatic(64)
	public String name;
	public int dataLength;
	public Vector3f eyePosition;
	public Vector3f illuminationPosition;
	public Vector3f hullMin;
	public Vector3f hullMax;
	public Vector3f viewBbmin;
	public Vector3f viewBbmax;
	public int flags;

	@ArrayPointer
	public MDLBone[] bones;

	@ArrayPointer
	public MDLBoneController[] boneControllers;

	@ArrayPointer
	public MDLHitBoxSet[] hitBoxSets;

	@ArrayPointer
	public MDLAnimationDescription[] animationDescriptions;

	@ArrayPointer
	public MDLSequence[] sequences;

	public int activitylistversion;
	public int eventsindexed;

	@ArrayPointer
	public MDLTextureData[] textureData;

	@ArrayPointer
	public int[] textureDirectoryPointers;

	//TODO
	public int skinreferenceCount;
	public int skinrfamilyCount;
	public int skinreferenceIndex;
	//

	@ArrayPointer
	public MDLBodyParts[] bodyParts;

	@ArrayPointer
	public MDLAttachment[] attachments;

	public int localNodeCount;
	public int localNodeIndex;
	@LengthPointerField("localNodeCount")
	@ValuePointerField("localNodeIndex")
	public byte[] localNodes;
	//TODO: name
	public int localNodeNameIndex;

	@ArrayPointer
	public MDLFlexDescription[] flexDescriptions;

	@ArrayPointer
	public MDLFlexController[] flexControllers;

	@ArrayPointer
	public MDLFlexRule[] flexRules;

	@ArrayPointer
	public MDLIKChain[] ikChains;

	@ArrayPointer
	public MDLMouth[] mouths;

	@ArrayPointer
	public MDLPoseParameterDescription[] poseParameterDescriptions;

	@ValuePointer
	public String surfaceProp;

	//TODO
	public int keyvalueIndex;
	public int keyvalueCount;
	//

	@ArrayPointer
	public MDLIKLock[] ikLocks;

	public float mass;
	public int contents;

	@ArrayPointer
	public MDLGroup[] includedModels;

	public int virtualModel;

	public int animBlocksNameIndex;

	@ArrayPointer
	public MDLAnimationBlock[] animationBlocks;

	public int animBlockModel;
	public int boneTableNameIndex;


	public int vertexBase;
	public int offsetBase;
	public byte directionalDotProduct;
	public byte rootLod;
	public byte numAllowedRootLODs;
	public byte unused0;
	public int unused1;

	@ArrayPointer
	public MDLFlexControllerUI[] flexControllerUIS;

	public int studiohdr2index;
	public int unused2;
	@LengthStatic(1)
	@ValuePointerField("studiohdr2index")
	public MDLSecondaryHeader[] secondaryHeader;
}
