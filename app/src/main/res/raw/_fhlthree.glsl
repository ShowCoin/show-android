precision highp float;
varying   highp vec2 vTextureCoord;
uniform   highp  sampler2D uTexture;

uniform float aspectRatio;

const float eps = 0.00001;

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

vec3 adjust_hue_saturation(vec3 rgb)
{
float hue_range[6];
hue_range[0] = 0.0;
hue_range[1] = 60.0;
hue_range[2] = 120.0;
hue_range[3] = 180.0;
hue_range[4] = -120.0;
hue_range[5] = -60.0;

float hue_value[6];
hue_value[0] = 0.0;
hue_value[1] = 0.0;
hue_value[2] = 0.0;
hue_value[3] = -3.0/100.0;
hue_value[4] = -5.0/100.0;
hue_value[5] = 0.0;

float saturation_value[6];
saturation_value[0] = 0.0;
saturation_value[1] = 0.0;
saturation_value[2] = 0.0;
saturation_value[3] = 45.0/100.0;
saturation_value[4] = 10.0/100.0;
saturation_value[5] = 0.0;

float lightness_value[6];
lightness_value[0] = 0.0;
lightness_value[1] = 0.0;
lightness_value[2] = 0.0;
lightness_value[3] = 0.0;
lightness_value[4] = 0.0;
lightness_value[5] = 0.0;

vec3 hsv = rgb2hsv(rgb);

float hue = hsv.x;
float saturation = hsv.y;
float lightness = hsv.z;

float new_hue;

float range = 30.0;

if(hue> hue_range[0]-range && hue < hue_range[0] + range)
{
 new_hue = hue+hue*hue_value[0];
 saturation = saturation+saturation*saturation_value[0];
 lightness = lightness + lightness*lightness_value[0];

}

if(hue> hue_range[1]-range && hue < hue_range[1] + range)
{
 new_hue = hue+hue*hue_value[1];
 saturation = saturation+saturation*saturation_value[1];
 lightness = lightness + lightness*lightness_value[1];

}

if(hue> hue_range[2]-range && hue < hue_range[2] + range)
{
 new_hue = hue+hue*hue_value[2];
 saturation = saturation+saturation*saturation_value[2];
 lightness = lightness + lightness*lightness_value[2];

}

if(hue> hue_range[3]-range || hue < -hue_range[3] + range)
{
 new_hue = hue+hue*hue_value[3];
 saturation = saturation+saturation*saturation_value[3];
 lightness = lightness + lightness*lightness_value[3];

}

if(hue> hue_range[4]-range && hue < hue_range[4] + range)
{
 new_hue = hue+hue*hue_value[4];
 saturation = saturation+saturation*saturation_value[4];
 lightness = lightness + lightness*lightness_value[4];

}

if(hue> hue_range[5]-range && hue < hue_range[5] + range)
{
 new_hue = hue+hue*hue_value[5];
 saturation = saturation+saturation*saturation_value[5];
 lightness = lightness + lightness*lightness_value[5];

}

vec3 new_hsv = vec3(new_hue,saturation,lightness);

vec3 new_rgb = hsv2rgb(new_hsv);
return new_rgb;
}

vec3 curve_one(vec3 rgb)
{
float r = rgb.r;
float g = rgb.g;
float b = rgb.b;

float new_r;
float new_g;
float new_b;

if(r<0.25)
{
 new_r = pow(r,1.01);
}
else
{
 new_r = pow(r,0.99);
}

if(g<0.25)
{
 new_g = pow(g,1.01);
}
else
{
 new_g = pow(g,0.99);
}

if(b<0.25)
{
 new_b = pow(b,1.01);
}
else
{
 new_b = pow(b,0.99);
}

return vec3(new_r,new_g,new_b);
}

vec3 curve_two(vec3 rgb)
{
float r = rgb.r;
float g = rgb.g;
float b = rgb.b;

float new_r;
float new_g;
float new_b;

float fit_value = 0.02;

if(r<0.5)
{
 new_r = pow(r,1.0+fit_value);
}
else
{
 new_r = pow(r,1.0-fit_value);
}

if(g<0.5)
{
 new_g = pow(g,1.0+fit_value);
}
else
{
 new_g = pow(g,1.0-fit_value);
}

if(b<0.5)
{
 new_b = pow(b,1.0+fit_value);
}
else
{
 new_b = pow(b,1.0-fit_value);
}

return vec3(new_r,new_g,new_b);

}

float max_value(vec3 rgb)
{
if(rgb.r>rgb.g&& rgb.r>rgb.b)
{
 return rgb.r;
}

if(rgb.g>rgb.r&& rgb.g>rgb.b)
{
 return rgb.g;
}

if(rgb.b>rgb.r&& rgb.b>rgb.g)
{
 return rgb.b;
}

return 0.0;

}

float mid_value(vec3 rgb)
{
if(rgb.r>rgb.g&& rgb.r<rgb.b)
{
 return rgb.r;
}

if(rgb.r>rgb.b&& rgb.r<rgb.g)
{
 return rgb.r;
}

if(rgb.g>rgb.r&& rgb.g<rgb.b)
{
 return rgb.g;
}

if(rgb.g>rgb.b&& rgb.g<rgb.r)
{
 return rgb.g;
}

if(rgb.b>rgb.r&& rgb.b<rgb.g)
{
 return rgb.b;
}

if(rgb.b>rgb.g&& rgb.b<rgb.r)
{
 return rgb.b;
}

return 0.0;

}

float min_value(vec3 rgb)
{
if(rgb.r<rgb.g&& rgb.r<rgb.b)
{
 return rgb.r;
}

if(rgb.g<rgb.r&& rgb.g<rgb.b)
{
 return rgb.g;
}

if(rgb.b<rgb.r&& rgb.b<rgb.g)
{
 return rgb.b;
}

return 0.0;

}

int select_index(vec3 rgb, int index)
{
int ret = 0;

if(index == 1)
{
 if(abs(rgb.r-max_value(rgb))<eps)
 {
     return 1;
 }

}

if(index == 2)
{
 if(abs(rgb.b-min_value(rgb))<eps)
 {
     return 1;
 }

}

if(index == 3)
{
 if(abs(rgb.g-max_value(rgb))<eps)
 {
     return 1;
 }

}

if(index == 4)
{
 if(abs(rgb.r-min_value(rgb))<eps)
 {
     return 1;
 }


}

if(index == 5)
{
 if(abs(rgb.b-max_value(rgb))<eps)
 {
     return 1;
 }


}

if(index == 6)
{
 if(abs(rgb.g-min_value(rgb))<eps)
 {
     return 1;
 }

}

if(index == 7)
{
 if(rgb.r<0.5&&rgb.g<0.5&&rgb.b<0.5)
 {
     return 1;
 }

}

if(index == 8)
{
 if(abs(rgb.r)<eps&& abs(rgb.g)<eps&& abs(rgb.r)<eps)
 {
     return 0;
 }

 if(abs(rgb.r-1.0)<eps&& abs(rgb.g-1.0)<eps&& abs(rgb.r-1.0)<eps)
 {
     return 0;
 }

 return 1;

}

if(index == 9)
{
 if(rgb.r>0.5&&rgb.g>0.5&&rgb.b>0.5)
 {
     return 1;
 }

}

return ret;
}

float select_range(vec3 rgb,int index)
{
float ret = 0.0;
if(index == 1)
{
 ret = max_value(rgb)-min_value(rgb);
}

if(index == 2)
{
 ret = mid_value(rgb)-min_value(rgb);
}

if(index == 3)
{
 ret = max_value(rgb)-min_value(rgb);
}

if(index == 4)
{
 ret = mid_value(rgb)-min_value(rgb);
}

if(index == 5)
{
 ret = max_value(rgb)-min_value(rgb);
}

if(index == 6)
{
 ret = mid_value(rgb)-min_value(rgb);
}

if(index == 7)
{
 ret = 2.0*(min_value(rgb)-0.5);
}

if(index == 8)
{
 ret = 1.0-(abs(max_value(rgb)-0.5)+abs(min_value(rgb)-0.5));
}

if(index == 9)
{
 ret = 2.0*(0.5-max_value(rgb));
}

return ret;
}

vec3 selective_color2(vec3 rgb)
{
float select_red[9];
select_red[0] = -0.02;
select_red[1] = 0.0;
select_red[2] = 0.0;
select_red[3] = 0.0;
select_red[4] = 0.0;
select_red[5] = 0.0;
select_red[6] = 0.0;
select_red[7] = 0.0;
select_red[8] = 0.03;

float select_green[9];
select_green[0]=0.13;
select_green[1]=0.0;
select_green[2]=0.0;
select_green[3]=0.0;
select_green[4]=0.0;
select_green[5]=0.0;
select_green[6]=0.0;
select_green[7]=0.0;
select_green[8]=-0.01;

float select_blue[9];
select_blue[0]=0.05;
select_blue[1]=0.0;
select_blue[2]=0.0;
select_blue[3]=0.0;
select_blue[4]=0.0;
select_blue[5]=0.0;
select_blue[6]=0.0;
select_blue[7]=0.0;
select_blue[8]=-0.04;

float select_all[9];
select_all[0]=0.05;
select_all[1]=0.0;
select_all[2]=0.0;
select_all[3]=0.0;
select_all[4]=0.0;
select_all[5]=0.0;
select_all[6]=0.0;
select_all[7]=0.0;
select_all[8]=0.0;

vec3 rgb1=rgb;
int i;
for(i=0;i<9;i++)
{
 int index = i+1;
 int sel_index = select_index(rgb,index);

 if(sel_index == 1)
 {
     float sel_range = select_range(rgb,index);

     if(abs(select_red[i])>eps)
     {

         if(select_red[i] > 0.0)
         {
             rgb1.r = rgb1.r-rgb1.r*sel_range*select_red[i];
         }
         else
         {
             rgb1.r = rgb1.r-(1.0-rgb1.r)*sel_range*select_red[i];
         }
     }

     if(abs(select_green[i])>eps)
     {
         if(select_green[i] > 0.0)
         {
             rgb1.g = rgb1.g-rgb1.g*sel_range*select_green[i];
         }
         else
         {
             rgb1.g = rgb1.g-(1.0-rgb1.g)*sel_range*select_green[i];
         }
     }

     if(abs(select_blue[i])>eps)
     {
         if(select_blue[i] > 0.0)
         {
             rgb1.b = rgb1.b-rgb1.b*sel_range*select_blue[i];
         }
         else
         {
             rgb1.b = rgb1.b-(1.0-rgb1.b)*sel_range*select_blue[i];
         }
     }

     if(abs(select_all[i])>eps)
     {

         if(select_all[i] > 0.0)
         {
             rgb1.r = rgb1.r-rgb1.r*sel_range*select_all[i];
             rgb1.g = rgb1.g-rgb1.g*sel_range*select_all[i];
             rgb1.b = rgb1.b-rgb1.b*sel_range*select_all[i];
         }
         else
         {
             rgb1.r = rgb1.r-(1.0-rgb1.r)*sel_range*select_all[i];
             rgb1.g = rgb1.g-(1.0-rgb1.g)*sel_range*select_all[i];
             rgb1.b = rgb1.b-(1.0-rgb1.b)*sel_range*select_all[i];
         }
     }


 }
}

return rgb1;
}

vec3 selective_color1(vec3 rgb)
{
float select_red[9];
select_red[0] = 0.0;
select_red[1] = 0.0;
select_red[2] = 0.0;
select_red[3] = 0.0;
select_red[4] = 0.0;
select_red[5] = 0.0;
select_red[6] = 0.0;
select_red[7] = 0.0;
select_red[8] = 0.0;

float select_green[9];
select_green[0]=0.0;
select_green[1]=0.0;
select_green[2]=0.0;
select_green[3]=0.0;
select_green[4]=0.0;
select_green[5]=0.0;
select_green[6]=0.0;
select_green[7]=0.0;
select_green[8]=0.0;

float select_blue[9];
select_blue[0]=0.0;
select_blue[1]=0.0;
select_blue[2]=0.0;
select_blue[3]=0.0;
select_blue[4]=0.0;
select_blue[5]=0.0;
select_blue[6]=0.0;
select_blue[7]=0.0;
select_blue[8]=0.0;

float select_all[9];
select_all[0]=-0.43;
select_all[1]=-0.37;
select_all[2]=0.0;
select_all[3]=0.0;
select_all[4]=0.0;
select_all[5]=0.0;
select_all[6]=0.0;
select_all[7]=0.0;
select_all[8]=0.0;

vec3 rgb1=rgb;
int i;
for(i=0;i<9;i++)
{
 int index = i+1;
 int sel_index = select_index(rgb,index);

 if(sel_index == 1)
 {
     float sel_range = select_range(rgb,index);

     if(abs(select_red[i])>eps)
     {

         if(select_red[i] > 0.0)
         {
             rgb1.r = rgb1.r-rgb1.r*sel_range*select_red[i];
         }
         else
         {
             rgb1.r = rgb1.r-(1.0-rgb1.r)*sel_range*select_red[i];
         }
     }

     if(abs(select_green[i])>eps)
     {
         if(select_green[i] > 0.0)
         {
             rgb1.g = rgb1.g-rgb1.g*sel_range*select_green[i];
         }
         else
         {
             rgb1.g = rgb1.g-(1.0-rgb1.g)*sel_range*select_green[i];
         }
     }

     if(abs(select_blue[i])>eps)
     {

         if(select_blue[i] > 0.0)
         {
             rgb1.b = rgb1.b-rgb1.b*sel_range*select_blue[i];
         }
         else
         {
             rgb1.b = rgb1.b-(1.0-rgb1.b)*sel_range*select_blue[i];
         }
     }

     if(abs(select_all[i])>eps)
     {
         if(select_all[i] > 0.0)
         {
             rgb1.r = rgb1.r-rgb1.r*sel_range*select_all[i];
             rgb1.g = rgb1.g-rgb1.g*sel_range*select_all[i];
             rgb1.b = rgb1.b-rgb1.b*sel_range*select_all[i];
         }
         else
         {
             rgb1.r = rgb1.r-(1.0-rgb1.r)*sel_range*select_all[i];
             rgb1.g = rgb1.g-(1.0-rgb1.g)*sel_range*select_all[i];
             rgb1.b = rgb1.b-(1.0-rgb1.b)*sel_range*select_all[i];
         }
     }


 }
}

return rgb1;
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
//vec3 rgb = setUpColor(tc);

vec3 rgb = mei_yan(tc);

//rgb = selective_color2(rgb);
//rgb = selective_color1(rgb);

rgb = adjust_hue_saturation(rgb);
rgb = curve_one(rgb);
rgb = curve_two(rgb);


rgb = color_balance(rgb,-0.02,0.02,0.05);

float bright_value = 13.0;
float contrast_value = 0.0;
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
gl_FragColor = vec4(rgb, 1.0);
}