precision highp float;
varying   highp vec2 vTextureCoord;
uniform   lowp  sampler2D uTexture;

uniform float aspectRatio;

vec3 setUpColor(vec2 currentTex)
{
 vec2 tc = currentTex;
 //tc.y = tc.y/aspectRatio;

 return  texture2D(uTexture,tc).rgb;
}

float check_value(float value)
{
 if(value>=1.0)
 {
     return 1.0;
 }
 if(value<=0.0)
 {
     return 0.0;
 }

 return value;
}

vec3 bright(vec3 rgb,float bright_value)
{
 float r;
 float g;
 float b;
 r = rgb.r+bright_value/255.0;
 r = check_value(r);

 g = rgb.g+bright_value/255.0;
 g = check_value(g);

 b = rgb.b+bright_value/255.0;
 b = check_value(b);

 return vec3(r,g,b);

}

vec3 contrast(vec3 rgb,float contrast_value)
{
 float r;
 float g;
 float b;

 float threshold = 0.5;

 if(contrast_value>=0.0)
 {
     r = rgb.r+(rgb.r-threshold)*(1.0/(1.0-contrast_value/255.0)-1.0);
     g = rgb.g+(rgb.g-threshold)*(1.0/(1.0-contrast_value/255.0)-1.0);
     b = rgb.b+(rgb.b-threshold)*(1.0/(1.0-contrast_value/255.0)-1.0);

 }

 if(contrast_value<0.0)
 {
     r = rgb.r+(rgb.r-threshold)*(contrast_value/255.0);
     g = rgb.g+(rgb.g-threshold)*(contrast_value/255.0);
     b = rgb.b+(rgb.b-threshold)*(contrast_value/255.0);

 }

 return vec3(r,g,b);
}


vec3 color_balance(vec3 rgb,float p1,float p2,float p3)
{
 /*float params[3];
 params[0] = 0.17;
 params[1] = 0.31;
 params[2] = 0.44;*/

 /*float params[3];
  params[0] = 0.15;
  params[1] = 0.0;
  params[2] = -0.45;*/

 /*float params[3];
 params[0] = 0.1;
 params[1] = 0.0;
 params[2] = 0.15;*/

 float r;
 float g;
 float b;

 /*r = rgb.r+rgb.r*params[0];
 g = rgb.g+rgb.g*params[1];
 b = rgb.b+rgb.b*params[2];*/

 r = rgb.r+rgb.r*p1;
 g = rgb.g+rgb.g*p2;
 b = rgb.b+rgb.b*p3;


 return vec3(r,g,b);

}


void main()
{
 vec2 tc = vTextureCoord;
 vec3 rgb = setUpColor(tc);

 float bright_value = 44.0;//29.0;//58.0;
 float contrast_value = 30.0;//45.0;
 if(contrast_value>0.0)
 {
     rgb = bright(rgb,bright_value);
     rgb = contrast(rgb,contrast_value);
 }
 else
 {
     rgb = contrast(rgb,contrast_value);
     rgb = bright(rgb,bright_value);

 }

 rgb = color_balance(rgb,0.075,0.0,0.0);
 //rgb = color_balance(rgb,0.1,0.0,0.0);

 gl_FragColor = vec4(rgb, 1.0);
}
