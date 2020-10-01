package blue.sparse.jst.specification.materials.vtf.impl;

import blue.sparse.binary.Binary;
import blue.sparse.jst.specification.Specification;
import blue.sparse.jst.specification.materials.vtf.VTFImageDataFormat;
import blue.sparse.jst.specification.materials.vtf.VTFResourceEntry;
import blue.sparse.jst.specification.materials.vtf.VTFTextureFlags;
import blue.sparse.jst.specification.materials.vtf.image.ImageFormat;
import xyz.eutaxy.util.data.RandomAccessReadableData;
import xyz.eutaxy.util.data.RandomAccessWritableData;
import xyz.eutaxy.util.data.WritableData;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class VTFSpecification extends Specification<VTFInstance> {

	public static final VTFSpecification INSTANCE = new VTFSpecification();

	private VTFSpecification() {
	}

	@Override
	public VTFInstance read(RandomAccessReadableData data) {
		try {
			VTFHeader vtfHeader = Binary.read(VTFHeader.class, data);
			VTFInstance vtfInstance = new VTFInstance(vtfHeader);
			var header = vtfInstance.getHeader();

			int flagBits = header.flags;

			for (VTFTextureFlags flag : VTFTextureFlags.values()) {
				int value = flag.getValue();
				if ((flagBits & value) != 0) {
					vtfInstance.setFlag(flag, true);
				}
			}

			int[] versionArray = header.version;

			int version = (versionArray[0] * 10) + versionArray[1];

			if (version == 71) {
				readVersion71(data, vtfInstance);
				return vtfInstance;
			} else {
				header.depth = data.readShort();

				if (version == 72) {
					readVersion72(data, vtfInstance);
				} else if (version == 73) {
					readVersion73(data, vtfInstance);
				}

				return vtfInstance;
			}

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void write(RandomAccessWritableData data, VTFInstance instance) {
		var header = instance.getHeader();

		data.writeBytes(header.signature);
		data.writeInts(header.version);
		data.writeInt(header.headerSize);
		data.writeShort(header.width);
		data.writeShort(header.height);
		data.writeInt(header.flags);
		data.writeShort(header.frames);
		data.writeShort(header.firstFrame);
		data.writeBytes(header.padding0);
		data.writeFloats(header.reflectivity);
		data.writeBytes(header.padding1);
		data.writeFloat(header.bumpmapScale);
		data.writeInt(header.highResImageFormat);
		data.writeByte(header.mipmapCount);
		data.writeInt(header.lowResImageFormat);
		data.writeByte(header.lowResImageWidth);
		data.writeByte(header.lowResImageHeight);
		data.writeShort(header.depth);

		if (instance.getVersionNumber() >= 73) {
			data.writeBytes(header.padding2);
			data.writeInt(header.numResources);
			writeVersion73(data, instance);
			return;
		}

		writeVersion72(data, instance);
	}

	private void writeVersion72(RandomAccessWritableData data, VTFInstance instance) {
		data.position(80);

	}

	private void writeVersion73(RandomAccessWritableData data, VTFInstance instance) {

	}

	private void readVersion71(RandomAccessReadableData data, VTFInstance instance) {
		var header = instance.getHeader();

		System.out.println("header = " + header);
		System.out.println(data.position());
		data.position(64);
		var lowResFormat = VTFImageDataFormat.values()[header.lowResImageFormat].getFormat();
		var highResFormat = VTFImageDataFormat.values()[header.highResImageFormat].getFormat();

		var lowResRead = lowResFormat.read(
				(int) header.lowResImageWidth & 0xFF,
				(int) header.lowResImageHeight & 0xFF,
				data
		);

		readMipmaps(data, instance, header, highResFormat);
	}

	private void readVersion72(RandomAccessReadableData data, VTFInstance instance) {
		var header = instance.getHeader();

		data.position(80);
		var lowResFormat = VTFImageDataFormat.values()[header.lowResImageFormat].getFormat();
		var highResFormat = VTFImageDataFormat.values()[header.highResImageFormat].getFormat();

		var lowResRead = lowResFormat.read(
				(int) header.lowResImageWidth & 0xFF,
				(int) header.lowResImageHeight & 0xFF,
				data
		);

		readMipmaps(data, instance, header, highResFormat);
	}

	private void readVersion73(RandomAccessReadableData data, VTFInstance instance) throws ReflectiveOperationException {
		var header = instance.getHeader();

		header.padding2 = new byte[3];
		data.readBytes(header.padding2);
		data.byteOrder(ByteOrder.LITTLE_ENDIAN);
		header.numResources = data.readInt();
		data.position(80);
		var highResFormat = VTFImageDataFormat.values()[header.highResImageFormat].getFormat();
		var lowResFormat = VTFImageDataFormat.values()[header.lowResImageFormat].getFormat();

		for (int i = 0; i < header.numResources; i++) {
			var startPosition = data.position();
			var resource = Binary.read(VTFResourceEntry.class, data);

			if (resource.flags == (byte) 0x2) {
				continue;
			}

			data.position(resource.offset);
			var b = resource.tag[0];
			if (b == (byte) 0x30) {
				readMipmaps(data, instance, header, highResFormat);
			}

			data.position(startPosition + 8);
		}

		var read = lowResFormat.read(
				(int) header.lowResImageWidth & 0xFF,
				(int) header.lowResImageHeight & 0xFF,
				data
		);
	}

	private void readMipmaps(RandomAccessReadableData data, VTFInstance instance, VTFHeader header, ImageFormat highResFormat) {
		int mipmapCount = header.mipmapCount;
		int frameCount = header.frames;
		int faceCount = 1;

		if (instance.isFlagSet(VTFTextureFlags.ENVMAP)) {
			faceCount = 6;
		}

		for (int mipmapIndex = mipmapCount - 1; mipmapIndex >= 0; mipmapIndex--) {
			var width = (int) header.width >> mipmapIndex;
			var height = (int) header.height >> mipmapIndex;
			VTFMipmap mipmap = new VTFMipmap(instance, mipmapIndex, width, height, new ArrayList<>());
			instance.getMipmaps().add(0, mipmap);

			for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
				VTFFrame frame = new VTFFrame(mipmap, frameIndex, new ArrayList<>());
				mipmap.getFrames().add(frame);

				for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
					var image = highResFormat.read(width, height, data);
					VTFFace face = new VTFFace(frame, faceIndex, image);
					frame.getFaces().add(face);
				}
			}
		}
	}

	@Override
	public String getFileExtension() {
		return "vtf";
	}
}
