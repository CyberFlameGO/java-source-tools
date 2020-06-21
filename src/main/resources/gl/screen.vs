#version 330 core

in vec2 aPosition;

uniform vec2 uScreenSizeInSections; // vec2(8, 8)
uniform vec2 uSection; // vec2(1, 1)

void main() {
    vec2 wPos = aPosition * 0.5 + 0.5;
    vec2 pos = wPos * (1.0/uScreenSizeInSections) + uSection / uScreenSizeInSections;
    gl_Position = vec4(pos * 2.0 - 1.0, 0, 1);
}