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

 float num = 0.99;

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

vec3 rgb2ypq(vec3 rgb)
{
 float y;
 float p;
 float q;

 y = 0.2989360212937753847527155*rgb.r+0.5870430744511212909351327*rgb.g+0.1140209042551033243121518*rgb.b;

 p = 0.5*rgb.r+0.5*rgb.g-1.0*rgb.b;
 q = rgb.r-1.0*rgb.g;

 return vec3(y,p,q);
}

vec3 ypq2rgb(vec3 ypq)
{
 float r;
 float g;
 float b;
 r = ypq.r+0.1140209042551033243121518*ypq.g+0.6440535265786729530912086*ypq.b;
 g = ypq.r+0.1140209042551033243121518*ypq.g-0.3559464734213270469087914*ypq.b;
 b = ypq.r-0.8859790957448966756878482*ypq.g+0.1440535265786729530912086*ypq.b;

 return vec3(r,g,b);
}

float my_sign(float num)
{
 if(num>0.0)
 {
     return 1.0;

 }

 if(num<0.0)
 {
     return -1.0;

 }

 return 0.0;
}

float get_contrast(vec3 rgb,vec3 rgb1,vec3 ypq,vec3 ypq1)
{
 float dr = rgb.r-rgb1.r;
 float dg = rgb.g-rgb1.g;
 float db = rgb.b-rgb1.b;

 float dy = ypq.r-ypq1.r;
 float dp = ypq.g-ypq1.g;
 float dq = ypq.b-ypq1.b;

 float ddist = sqrt(dr*dr+dg*dg+db*db);

 float c = 0.0;
 float eps = 0.001;
 if(abs(ddist)>eps)
 {
     c = (ddist-abs(dy)/0.6686)/ddist;

 }

 float osign = my_sign(dy);

 float delta_p = osign*c*dp;
 float delta_q = osign*c*dq;

 float k = ypq.g*delta_p+ypq.b*delta_q;

 float result = 0.03*k;//0.01*k;
 return result;



}

vec3 decolor(vec2 tc)
{
 vec3 rgb = mei_yan(tc);

 float tex_width = 768.0;
 float tex_height = 1334.0;

 float dist = 2.0;

 vec2 new_tc = tc+vec2(dist*1.0/tex_width,dist*1.0/tex_height);

 vec3 rgb1 = mei_yan(new_tc);

 vec3 ypq = rgb2ypq(rgb);
 vec3 ypq1 = rgb2ypq(rgb1);

 float delta_gray = get_contrast(rgb,rgb1,ypq,ypq1);

 ypq.r = ypq.r+delta_gray;
 ypq.g=0.0;
 ypq.b=0.0;
 rgb = ypq2rgb(ypq);

 return rgb;


}


void main()
{
 vec2 tc = vTextureCoord;
 //vec3 rgb = setUpColor(tc);

 //vec3 rgb = mei_yan(tc);
 vec3 rgb = decolor(tc);

 gl_FragColor = vec4(rgb, 1.0);
}