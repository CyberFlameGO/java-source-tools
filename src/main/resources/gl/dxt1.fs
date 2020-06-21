#version 430 core

layout(location = 0) out uvec4 fColor;

const uint MAX_BITS_5 = (1 << 5) - 1;
const uint MAX_BITS_6 = (1 << 6) - 1;
const uvec3 INT_MAX_565 = uvec3(MAX_BITS_5, MAX_BITS_6, MAX_BITS_5);
const vec3 FLOAT_MAX_565 = vec3(INT_MAX_565);

const ivec3[] TEST_COLOR_OFFSETS = {
    ivec3(0, 0, 0), ivec3(-1, 0, 0), ivec3(0, -1, 0),
    ivec3(0, 0, -1), ivec3(0, 0, 1), ivec3(0, 1, 0),
    ivec3(1, 0, 0), ivec3(-1, -1, 0), ivec3(-1, 0, -1),
    ivec3(-1, 0, 1), ivec3(-1, 1, 0), ivec3(0, -1, -1),
    ivec3(0, -1, 1), ivec3(0, 1, -1), ivec3(0, 1, 1),
    ivec3(1, -1, 0), ivec3(1, 0, -1), ivec3(1, 0, 1),
    ivec3(1, 1, 0), ivec3(-1, -1, -1), ivec3(-1, -1, 1),
    ivec3(-1, 1, -1), ivec3(-1, 1, 1), ivec3(1, -1, -1),
    ivec3(1, -1, 1), ivec3(1, 1, -1), ivec3(1, 1, 1)
};

const int TEST_COLOR_OFFSET_COUNT = 7;
const int TEST_COLOR_COUNT = 16 * TEST_COLOR_OFFSET_COUNT;

uniform sampler2D uInputTexture;
uniform uvec2 uInputTextureSize;

uint packRGB565(uvec3 color) {
    uint result = 0;
    result = bitfieldInsert(result, color.r, 11, 5);
    result = bitfieldInsert(result, color.g, 5, 6);
    result = bitfieldInsert(result, color.b, 0, 5);
    return result;
}

uint encodeRGB565(vec3 color) {
    return packRGB565(uvec3(round(color * FLOAT_MAX_565)));
//    uint result = 0;
//    result = bitfieldInsert(result, uint(round(color.r * float(MAX_BITS_5))), 11, 5);
//    result = bitfieldInsert(result, uint(round(color.g * float(MAX_BITS_6))), 5, 6);
//    result = bitfieldInsert(result, uint(round(color.b * float(MAX_BITS_5))), 0, 5);
//    return result;
}

uvec3 extractRGB565(uint color) {
    return uvec3(
        bitfieldExtract(color, 11, 5),
        bitfieldExtract(color, 5, 6),
        bitfieldExtract(color, 0, 5)
    );
}

vec3 decodeRGB565(uint color) {
    return vec3(extractRGB565(color)) / FLOAT_MAX_565;
}

vec3[4] interpolateColors(uint icolor0, uint icolor1) {
    vec3[4] result;
    vec3 color0 = result[0] = decodeRGB565(icolor0);
    vec3 color1 = result[1] = decodeRGB565(icolor1);

    if(icolor0 > icolor1) {
        result[2] = (color0 * 2 + color1) / 3f;
        result[3] = (color0 + color1 * 2) / 3f;
    } else {
        result[2] = (color0 + color1) / 2f;
        result[3] = vec3(0);
    }

    return result;
}

uint getColorCode(vec3[4] interpolated, vec3 color) {
    uint code = -1;
    float distance = 100;

    for(uint i = 0u; i < 4u; i++) {
        vec3 c = interpolated[i];
        float d = length(color - c);

        if(d < distance) {
            distance = d;
            code = i;
        }
    }

    return code;
}

float sumDistances(vec3[16] a, vec3[16] b) {
    float result = 0f;
    for(int i = 0; i < 16; i++) {
        result += length(a[i] - b[i]);
    }

    return result;
}

void main() {
    uint blockX = uint(gl_FragCoord.x);
    uint blockY = uint(gl_FragCoord.y);

    vec3[16] actualPixelColors;
    for(int y = 0; y < 4; y ++) {
        for(int x = 0; x < 4; x++) {
            vec2 pos = vec2(
                float(blockX) * 4.0 + x + 0.5,
                float(blockY) * 4.0 + y + 0.5
            ) / vec2(uInputTextureSize);
            actualPixelColors[x + y * 4] = texture(uInputTexture, pos).rgb;
        }
    }

    uint[TEST_COLOR_COUNT] testColors;
    int index = 16;
    for(int i = 0; i < 16; i++) {
        uint encoded565 = encodeRGB565(actualPixelColors[i]);
        uvec3 color565 = extractRGB565(encoded565);

        uvec3 dmin = max(color565 - 1, uvec3(0u));
        uvec3 dmax = min(color565 + 1, INT_MAX_565);

        for(int i = 0; i < TEST_COLOR_OFFSET_COUNT; i++) {
            testColors[index++] = packRGB565(color565 + TEST_COLOR_OFFSETS[i]);
        }
    }

    float resultDistance = 99999999;
    uint resultColor0 = 0;
    uint resultColor1 = 0;
    uint resultCode = 0;
    for(int i = 0; i < TEST_COLOR_COUNT; i++) {
        for(int j = 0; j < TEST_COLOR_COUNT; j++) {
            uint color0 = testColors[i];
            uint color1 = testColors[j];

            vec3[4] interpolated = interpolateColors(color0, color1);
            vec3[16] colors;

            uint code = 0;
            for(int k = 0; k < 16; k++) {
                uint pixelCode = getColorCode(interpolated, actualPixelColors[k]);
                code = bitfieldInsert(code, pixelCode, k * 2, 2);
                colors[k] = interpolated[pixelCode];
            }

            float distance = sumDistances(actualPixelColors, colors);
            if(distance < resultDistance) {
                resultDistance = distance;
                resultColor0 = color0;
                resultColor1 = color1;
                resultCode = code;
            }
        }
    }

    fColor = uvec4(
        resultColor0,
        resultColor1,
        bitfieldExtract(resultCode, 0, 16),
        bitfieldExtract(resultCode, 16, 16)
    );
}