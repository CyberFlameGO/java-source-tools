package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLSecondaryHeader {
	public int srcBoneTransformCount;
	public int srcBoneTransformIndex;
	public int illumPositionAttachmentIndex;
	public float flMaxEyeDeflection;
	public int linearBoneIndex;

	@LengthStatic(64)
	public int[] unknown;
}
