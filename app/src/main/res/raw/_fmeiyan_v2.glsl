//meiyan2
#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES uTexture;
varying lowp vec2 vTextureCoord;

void main(){

    vec3 centralColor;
    
    float mul_x = 1.6 / 720.0;
    float mul_y = 1.6 / 1280.0;
    
    vec2 blurCoordinates0 = vTextureCoord + vec2(0.0 * mul_x,-10.0 * mul_y);
    vec2 blurCoordinates1 = vTextureCoord + vec2(5.0 * mul_x,-8.0 * mul_y);
    vec2 blurCoordinates2 = vTextureCoord + vec2(8.0 * mul_x,-5.0 * mul_y);
    vec2 blurCoordinates3 = vTextureCoord + vec2(10.0 * mul_x,0.0 * mul_y);
    vec2 blurCoordinates4 = vTextureCoord + vec2(8.0 * mul_x,5.0 * mul_y);
    vec2 blurCoordinates5 = vTextureCoord + vec2(5.0 * mul_x,8.0 * mul_y);
    vec2 blurCoordinates6 = vTextureCoord + vec2(0.0 * mul_x,10.0 * mul_y);
    vec2 blurCoordinates7 = vTextureCoord + vec2(-5.0 * mul_x,8.0 * mul_y);
    vec2 blurCoordinates8 = vTextureCoord + vec2(-8.0 * mul_x,5.0 * mul_y);
    vec2 blurCoordinates9 = vTextureCoord + vec2(-10.0 * mul_x,0.0 * mul_y);
    vec2 blurCoordinates10 = vTextureCoord + vec2(-8.0 * mul_x,-5.0 * mul_y);
    vec2 blurCoordinates11 = vTextureCoord + vec2(-5.0 * mul_x,-8.0 * mul_y);
    
    
    float central;
    float gaussianWeightTotal;
    float sum;
    float sample1;
    float distanceFromCentralColor;
    float gaussianWeight;
    
    float distanceNormalizationFactor = 3.6;
    
    central = texture2D(uTexture, vTextureCoord).g;
    gaussianWeightTotal = 0.2;
    sum = central * 0.2;
    
    sample1 = texture2D(uTexture, blurCoordinates0).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates1).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates2).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates3).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates4).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates5).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates6).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates7).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates8).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates9).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates10).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    sample1 = texture2D(uTexture, blurCoordinates11).g;
    distanceFromCentralColor = min(abs(central - sample1) * distanceNormalizationFactor, 1.0);
    gaussianWeight = 0.08 * (1.0 - distanceFromCentralColor);
    gaussianWeightTotal += gaussianWeight;
    sum += sample1 * gaussianWeight;
    
    
    sum = sum/gaussianWeightTotal;
    
    
    
    centralColor = texture2D(uTexture, vTextureCoord).rgb;
    
    sample1 = centralColor.g - sum + 0.5;
    
    
    for(int i = 0; i < 5; ++i)
    {
    if(sample1 <= 0.5)
    {
    sample1 = sample1 * sample1 * 2.0;
    }
    else
    {
    sample1 = 1.0 - ((1.0 - sample1)*(1.0 - sample1) * 2.0);
    }
    }
    
    
    float aa = 1.0 + pow(sum, 0.3)*0.07;
    vec3 smoothColor = centralColor*aa - vec3(sample1)*(aa-1.0);// get smooth color
    smoothColor = clamp(smoothColor,vec3(0.0),vec3(1.0));//make smooth color right
    
    smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.33));
    smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.39));
    
    smoothColor = mix(centralColor, smoothColor, 0.69);
    
    gl_FragColor = vec4(pow(smoothColor, vec3(0.96)),1.0);

}
