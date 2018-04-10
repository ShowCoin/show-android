precision highp float;
varying   highp vec2 vTextureCoord;
uniform   lowp  sampler2D uTexture;

uniform float aspectRatio;

vec3 setUpColor(vec2 currentTex)
{
 vec2 tc = currentTex;
 //        tc.y = tc.y/aspectRatio;

 return  texture2D(uTexture,tc).rgb;
}


float get_gray(vec3 rgb)
{
 float gray = 0.299*rgb.r+0.587*rgb.g+0.114*rgb.b;
 return gray;

}

vec3  get_max_value(vec2 tc)
{
 float r_max_value = 0.0;
 float g_max_value = 0.0;
 float b_max_value = 0.0;

 float tex_width = 768.0;
 float tex_height = 1334.0;

 float i;
 float j;
 for(i=-2.0;i<=2.0;i+=1.0)
     for(j=-2.0;j<=2.0;j+=1.0)
     {
         float offsetx = j/tex_width;
         float offsety = i/tex_height;

         vec2 offset_tc = tc+vec2(offsetx, offsety);

         vec3 temp_rgb = setUpColor(offset_tc);

         if(temp_rgb.r > r_max_value)
         {
             r_max_value = temp_rgb.r;
         }

         if(temp_rgb.g > g_max_value)
         {
             g_max_value = temp_rgb.g;
         }

         if(temp_rgb.b > b_max_value)
         {
             b_max_value = temp_rgb.b;
         }

     }

 return vec3(r_max_value,g_max_value,b_max_value);
}

vec3 mei_bai_map(vec3 rgb)
{
 float r;
 float g;
 float b;

 float num = 0.98;

 r=pow(rgb.r,num);
 g=pow(rgb.g,num);
 b=pow(rgb.b,num);

 return vec3(r,g,b);

}

vec3 gauss_blur(vec2 tc)
{
 float tex_width = 768.0;
 float tex_height = 1334.0;

 vec3 ret = vec3(0.0,0.0,0.0);

 vec2 tc_offset = vec2(-1.0/tex_width,-1.0/tex_height);
 vec2 new_tc = tc+tc_offset;

 vec3 rgb = setUpColor(new_tc);


 ret =ret + 0.0947416*rgb;

 tc_offset = vec2(-1.0/tex_width,1.0/tex_height);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.0947416*rgb;

 tc_offset = vec2(1.0/tex_width,-1.0/tex_height);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.0947416*rgb;

 tc_offset = vec2(1.0/tex_width,1.0/tex_height);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.0947416*rgb;

 tc_offset = vec2(1.0/tex_width,0.0);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.118318*rgb;

 tc_offset = vec2(-1.0/tex_width,0.0);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.118318*rgb;

 tc_offset = vec2(0.0,-1.0/tex_height);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.118318*rgb;

 tc_offset = vec2(0.0,1.0/tex_height);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.118318*rgb;

 tc_offset = vec2(0.0,0.0);
 new_tc = tc+tc_offset;

 rgb = setUpColor(new_tc);


 ret =ret + 0.147761*rgb;

 return ret;


}

vec3 high_sub(vec3 rgb,vec2 tc)
{
 vec3 gauss_blur_rgb = gauss_blur(tc);
 vec3 diff = rgb - gauss_blur_rgb;
 vec3 result = diff + vec3(0.5,0.5,0.5);
 return result;

}

vec3 additive_blend(vec3 base_color,vec3 blend_color)
{
 vec3 ret;
 if(base_color.r<=0.5)
 {
     ret.r = base_color.r*blend_color.r/0.5;

 }
 else
 {
     ret.r = 1.0-(1.0-base_color.r)*(1.0-blend_color.r)/0.5;

 }

 if(base_color.g<=0.5)
 {
     ret.g = base_color.g*blend_color.g/0.5;

 }
 else
 {
     ret.g = 1.0-(1.0-base_color.g)*(1.0-blend_color.g)/0.5;

 }

 if(base_color.b<=0.5)
 {
     ret.b = base_color.b*blend_color.b/0.5;

 }
 else
 {
     ret.b = 1.0-(1.0-base_color.b)*(1.0-blend_color.b)/0.5;

 }

 return  ret;


}


vec3 mei_yan(vec2 tc)
{
 vec3 rgb = setUpColor(tc);

 vec3 mei_bai_rgb = mei_bai_map(rgb);

 float gray = get_gray(rgb);

 vec3 high_sub_rgb = high_sub(rgb,tc);

 vec3 result = high_sub_rgb;


 result = additive_blend(result,rgb);
 result = additive_blend(result,rgb);

 float alpha = gray;
 result = alpha*mei_bai_rgb + (1.0-alpha)*result;

 return result;
}

vec3 curve_one(vec3 rgb)
{
 float r = rgb.r;
 float g = rgb.g;
 float b = rgb.b;

 float new_r;
 float new_g;
 float new_b;


 float fit_value_red = 0.1;
 float fit_value_green = 0.01;
 float fit_value_blue = 0.03;


 new_r = pow(r,1.0-fit_value_red);


 if(g<0.25)
 {
     new_g = pow(g,1.0+fit_value_green);
 }
 else
 {
     new_g = pow(g,1.0-fit_value_green);

 }


 new_b = pow(b,1.0+fit_value_blue);


 return vec3(new_r,new_g,new_b);
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

vec3 rgb2hsv(vec3 c)
{
 vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
 vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
 vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

 float d = q.x - min(q.w, q.y);
 float e = 1.0e-10;
 return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c)
{
 vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
 vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
 return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);

}


vec3 curve_two(vec3 rgb)
{
 float new_r;
 float new_g;
 float new_b;

 float fit_value = 0.05;

 new_r = pow(rgb.r,1.0-fit_value);
 new_g = pow(rgb.g,1.0-fit_value);
 new_b = pow(rgb.b,1.0-fit_value);

 return vec3(new_r,new_g,new_b);


}

float soft_blend(float b)
{
 float c;

 float a = 0.5;

 float coef = 1.35;
 if(b<0.5)
 {
     c = a*b/0.5+coef*pow(a,2.0)*(1.0-2.0*b);
     //c = a*b/0.5+coef*(1.0-2.0*b);
 }
 else
 {
     c = a*(1.0-b)/0.5+coef*sqrt(a)*(2.0*b-1.0);
     //c = a*(1.0-b)/0.5+coef*(2.0*b-1.0);

 }

 return c;
}

vec3 mid_gray(vec3 rgb)
{
 //float r = soft_blend(rgb.r);
 //float g = soft_blend(rgb.g);
 //float b = soft_blend(rgb.b);

 //vec3 rgb1 =  vec3(r,g,b);
 //return 0.93*rgb1;
 vec3 gray = vec3(0.5,0.5,0.5);
 float alpha = 0.20;
 vec3 ret = alpha*gray+(1.0-alpha)*rgb;
 return ret;

}

void main()
{
 vec2 tc = vTextureCoord;
 //vec3 rgb = setUpColor(tc);

 vec3 rgb = mei_yan(tc);

 rgb = mid_gray(rgb);

 rgb = curve_one(rgb);
 rgb = color_balance(rgb,-0.09,0.01,0.0);
 rgb = curve_two(rgb);

 gl_FragColor = vec4(rgb, 1.0);
}
