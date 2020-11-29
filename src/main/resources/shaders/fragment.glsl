#version 150

in vec2 vTexPosition;

out vec4 pixelColor;

uniform sampler2D aTexture;

void main() {
    vec4 color = texture(aTexture, vTexPosition);
    if (color.a < 0.1) {
        discard;
    }
    pixelColor = color;
}