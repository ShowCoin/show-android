precision lowp float;

varying highp vec2 vTextureCoord;
// varying vec2 vTextureCoord;

uniform sampler2D uTexture;
uniform sampler2D uExtraTexture;

void main()
{

     highp vec4 textureColor = texture2D(uTexture, vTextureCoord);

     highp float blueColor = textureColor.b * 63.0;

     highp vec2 quad1;
     quad1.y = floor(floor(blueColor) / 8.0);
     quad1.x = floor(blueColor) - (quad1.y * 8.0);

     highp vec2 quad2;
     quad2.y = floor(ceil(blueColor) /8.0);
     quad2.x = ceil(blueColor) - (quad2.y * 8.0);

     highp vec2 texPos1;
     texPos1.x = (quad1.x * 1.0/8.0) + 0.5/512.0 + ((1.0/8.0 - 1.0/512.0) * textureColor.r);
     texPos1.y = (quad1.y * 1.0/8.0) + 0.5/512.0 + ((1.0/8.0 - 1.0/512.0) * textureColor.g);

     highp vec2 texPos2;
     texPos2.x = (quad2.x * 1.0/8.0) + 0.5/512.0 + ((1.0/8.0 - 1.0/512.0) * textureColor.r);
     texPos2.y = (quad2.y * 1.0/8.0) + 0.5/512.0 + ((1.0/8.0 - 1.0/512.0) * textureColor.g);

     lowp vec4 newColor1 = texture2D(uExtraTexture, texPos1);
     lowp vec4 newColor2 = texture2D(uExtraTexture, texPos2);

     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));
     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), 1.0);

     // vec4 textureColor = texture2D(uTexture, vTextureCoord);
     
     // float blueColor = textureColor.b * 63.0;
     
     // vec2 quad1;
     // quad1.y = floor(floor(blueColor) / 8.0);
     // quad1.x = floor(blueColor) - (quad1.y * 8.0);
     
     // vec2 quad2;
     // quad2.y = floor(ceil(blueColor) / 8.0);
     // quad2.x = ceil(blueColor) - (quad2.y * 8.0);
     
     // vec2 texPos1;
     // texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);
     // texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);
     
     // vec2 texPos2;
     // texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);
     // texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);
     
     // vec4 newColor1 = texture2D(uExtraTexture, texPos1);
     // vec4 newColor2 = texture2D(uExtraTexture, texPos2);
     
     // vec4 newColor = mix(newColor1, newColor2, fract(blueColor));
     // gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), 1.0);
}