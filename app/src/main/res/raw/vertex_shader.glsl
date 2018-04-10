attribute vec4 aPosition;
attribute vec4 aTexCoord;
varying vec2 vTexCoord;
uniform mat4 uMatrix;
uniform mat4 uSTMatrix;

void main() {
    vTexCoord = (uSTMatrix * aTexCoord).xy;
    gl_Position = uMatrix * aPosition;
}
