#extension GL_OES_EGL_image_external : require
precision mediump float;

varying vec2 vTextureCoord;
varying vec2 vExtraTextureCoord;

uniform samplerExternalOES uTexture; //相机图片
uniform sampler2D uExtraTexture;  //纹理图片


const vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);
const vec3 warmFilter = vec3(0.93, 0.54, 0.0);
const mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);
const mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);

void main() {

    vec4 color;
    float brightness = -0.1;
    float contract = 1.0;
    float saturation = 0.5;
    float temperature = (7000.0 - 5000.0) * 0.00006;
    float tint = 80.0/100.0;
    float sharpness = 1.0;
    vec2 Vtec = vec2(0.5,1.0);

    vec4 textureColor = texture2D(uTexture, vTextureCoord);
    float blueColor = textureColor.b * 63.0;

    vec2 quad1;
    quad1.y = floor(floor(blueColor) / 8.0);
    quad1.x = floor(blueColor) - (quad1.y * 8.0);

    vec2 quad2;
    quad2.y = floor(ceil(blueColor) / 8.0);
    quad2.x = ceil(blueColor) - (quad2.y * 8.0);

    lowp vec2 texPos1;
    texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);
    texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);

    lowp vec2 texPos2;
    texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);
    texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);

    vec4 newColor1 = texture2D(uExtraTexture, texPos1);
    vec4 newColor2 = texture2D(uExtraTexture, texPos2);
        float intensityss = 1.0;

        vec4 newColor = mix(newColor1, newColor2, fract(blueColor));
        color  = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensityss);
        //gl_FragColor = texture2D(uExtraTexture, textureCoordinate);
        /////////////////////
        color = vec4((color.rgb + vec3(brightness)), color.w);
        ////////////////////////
        color = vec4(((color.rgb - vec3(0.5)) * contract + vec3(0.5)), color.w);

        float luminance = dot(color.rgb, luminanceWeighting);
        vec3 greyScaleColor = vec3(luminance);

        color = vec4(mix(greyScaleColor, color.rgb, saturation), color.w);
     /////////////////////////////////////////////////////////////////////////////
        //vec4 source = texture2D(uTexture, textureCoordinate);

        vec3 yiq = RGBtoYIQ * color.rgb; //adjusting tint
        yiq.b = clamp(yiq.b + tint*0.5226*0.1, -0.5226, 0.5226);
        vec3 rgb = YIQtoRGB * yiq;

        vec3 processed = vec3(
                              (rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))), //adjusting temperature
                              (rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))),
                              (rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));

        gl_FragColor = vec4(mix(rgb, processed, temperature), color.a);
        ///////////////////////////////
        //锐化必须外围处理


        //gl_FragColor = mix(color, vec4(newColor.rgb, color.w), intensity);

}
