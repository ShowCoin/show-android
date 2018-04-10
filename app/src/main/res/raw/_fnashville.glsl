     precision mediump float;

     varying mediump vec2 vTextureCoord;

     uniform sampler2D uTexture;
     uniform sampler2D uExtraTexture;

     void main()
     {
         vec3 texel = texture2D(uTexture, vTextureCoord).rgb;
         texel = vec3(
                      texture2D(uExtraTexture, vec2(texel.r, .16666)).r,
                      texture2D(uExtraTexture, vec2(texel.g, .5)).g,
                      texture2D(uExtraTexture, vec2(texel.b, .83333)).b);

//         texture2D(uExtraTexture, vTextureCoord);
 gl_FragColor = vec4(texel, 1.0);
     }
