#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTexCoord;
uniform samplerExternalOES sTexture;
uniform int state;
uniform float color_R;
uniform float color_G;
uniform float color_B;

void main() {
    if(state == 0) {
        gl_FragColor = texture2D(sTexture,vTexCoord);

    } else {
      vec3 centralColor = texture2D(sTexture, vTexCoord).rgb;
       //gl_FragColor = vec4(0.299*centralColor.r+0.587*centralColor.g+0.114*centralColor.b);
      gl_FragColor = vec4(color_R*centralColor.r+color_G*centralColor.g+color_B*centralColor.b);
    }
}