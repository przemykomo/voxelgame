#version 150

in vec2 vTexPosition;

out vec4 pixelColor;

uniform sampler2D aTexture;

void main() {
//    pixelColor = vec4(1.0, 1.0, 0.0, 1.0);
    pixelColor = texture(aTexture, vTexPosition);
//    pixelColor = vec4(vTexPosition, 0.0, 1.0);
}