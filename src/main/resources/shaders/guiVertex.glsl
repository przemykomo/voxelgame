#version 150

in vec2 position;
in vec2 texPosition;

uniform float aspectRatio;

out vec2 vTexPosition;

void main() {
    gl_Position = vec4(position.x / aspectRatio, position.y, 0.0, 1.0);
    vTexPosition = texPosition;
}