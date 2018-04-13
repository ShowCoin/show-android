    #extension GL_OES_EGL_image_external : require
    precision highp float;
                         varying highp vec2 vTextureCoord;
                         uniform samplerExternalOES uTexture;
                         uniform sampler2D uTexture;
                       

                         vec3 AdjustColor(vec3 inputColor,ivec3 chooseColor,vec4 adjustAmount)
    {
        vec3 outputColor = inputColor;

        float max_c = max(inputColor.r, inputColor.g);
        float min_c = min(inputColor.r, inputColor.g);
        float mid_c = inputColor.b;
        if(inputColor.b < min_c)
        {
            mid_c = min_c;
            min_c = inputColor.b;
        }
        if(inputColor.b > max_c)
        {
            mid_c = max_c;
            max_c = inputColor.b;
        }

        float interval_up = max_c - mid_c;
        float interval_down = mid_c - min_c;


        float redUpmax = inputColor.r;
        float redDownmax = -1.0 * (1.0-redUpmax);
        float greenUpmax = inputColor.g;
        float greenDownmax = -1.0 * (1.0-greenUpmax);
        float blueUpmax = inputColor.b;
        float blueDownmax = -1.0 * (1.0-blueUpmax);
        float blackUpmax = dot(inputColor, vec3(0.3,0.6,0.1));
        float blackDownmax = -1.0 * (1.0-blackUpmax);

        vec4 changeColor = adjustAmount;
        if(changeColor.r > redUpmax)
            changeColor.r = redUpmax;
        if(changeColor.r < redDownmax)
            changeColor.r = redDownmax;

        if(changeColor.g > greenUpmax)
            changeColor.g = greenUpmax;
        if(changeColor.g < greenDownmax)
            changeColor.g = greenDownmax;

        if(changeColor.b > blueUpmax)
            changeColor.b = blueUpmax;
        if(changeColor.b < blueDownmax)
            changeColor.b = blueDownmax;

        if(changeColor.a > blackUpmax)
            changeColor.a = blackUpmax;
        if(changeColor.a < blackDownmax)
            changeColor.a = blackDownmax;



        if(chooseColor.r == 1 && chooseColor.g == 0 && chooseColor.b == 0 )//选中红色
        {
            if(max_c == inputColor.r)
            {
                outputColor.r = outputColor.r - changeColor.r * interval_up;
                outputColor.g = outputColor.g - changeColor.g * interval_up;
                outputColor.b = outputColor.b - changeColor.b * interval_up;

                outputColor.r = outputColor.r - changeColor.a * interval_up;
                outputColor.g = outputColor.g - changeColor.a * interval_up;
                outputColor.b = outputColor.b - changeColor.a * interval_up;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中红色

        if(chooseColor.r == 0 && chooseColor.g == 1 && chooseColor.b == 0 )//选中绿色
        {
            if(max_c == inputColor.g)
            {
                outputColor.r = outputColor.r - changeColor.r * interval_up;
                outputColor.g = outputColor.g - changeColor.g * interval_up;
                outputColor.b = outputColor.b - changeColor.b * interval_up;

                outputColor.r = outputColor.r - changeColor.a * interval_up;
                outputColor.g = outputColor.g - changeColor.a * interval_up;
                outputColor.b = outputColor.b - changeColor.a * interval_up;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中绿色

        if(chooseColor.r == 0 && chooseColor.g == 0 && chooseColor.b == 1 )//选中蓝色
        {
            if(max_c == inputColor.b)
            {
                outputColor.r = outputColor.r - changeColor.r * interval_up;
                outputColor.g = outputColor.g - changeColor.g * interval_up;
                outputColor.b = outputColor.b - changeColor.b * interval_up;

                outputColor.r = outputColor.r - changeColor.a * interval_up;
                outputColor.g = outputColor.g - changeColor.a * interval_up;
                outputColor.b = outputColor.b - changeColor.a * interval_up;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中蓝色

        if(chooseColor.r == 1 && chooseColor.g == 1 && chooseColor.b == 0 )//选中黄色
        {
            if((max_c == inputColor.r&&mid_c == inputColor.g)||(max_c == inputColor.g&&mid_c == inputColor.r))
            {
                outputColor.r = outputColor.r - changeColor.r * interval_down;
                outputColor.g = outputColor.g - changeColor.g * interval_down;
                outputColor.b = outputColor.b - changeColor.b * interval_down;

                outputColor.r = outputColor.r - changeColor.a * interval_down;
                outputColor.g = outputColor.g - changeColor.a * interval_down;
                outputColor.b = outputColor.b - changeColor.a * interval_down;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中黄色

        if(chooseColor.r == 0 && chooseColor.g == 1 && chooseColor.b == 1 )//选中青色
        {
            if((max_c == inputColor.g&&mid_c == inputColor.b)||(max_c == inputColor.b&&mid_c == inputColor.g))
            {
                outputColor.r = outputColor.r - changeColor.r * interval_down;
                outputColor.g = outputColor.g - changeColor.g * interval_down;
                outputColor.b = outputColor.b - changeColor.b * interval_down;

                outputColor.r = outputColor.r - changeColor.a * interval_down;
                outputColor.g = outputColor.g - changeColor.a * interval_down;
                outputColor.b = outputColor.b - changeColor.a * interval_down;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中青色

        if(chooseColor.r == 1 && chooseColor.g == 0 && chooseColor.b == 1 )//选中洋红
        {
            if((max_c == inputColor.r&&mid_c == inputColor.b)||(max_c == inputColor.b&&mid_c == inputColor.r))
            {
                outputColor.r = outputColor.r - changeColor.r * interval_down;
                outputColor.g = outputColor.g - changeColor.g * interval_down;
                outputColor.b = outputColor.b - changeColor.b * interval_down;

                outputColor.r = outputColor.r - changeColor.a * interval_down;
                outputColor.g = outputColor.g - changeColor.a * interval_down;
                outputColor.b = outputColor.b - changeColor.a * interval_down;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中洋红


        if(chooseColor.r == 2 && chooseColor.g == 2 && chooseColor.b == 2 )//选中白色
        {
            if(min_c > 0.667)
            {
                outputColor.r = outputColor.r - changeColor.r * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.g * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.b * (interval_down+interval_up) * 0.5;

                outputColor.r = outputColor.r - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.a * (interval_down+interval_up) * 0.5;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中白色

        if(chooseColor.r == 0 && chooseColor.g == 0 && chooseColor.b == 0 )//选中黑色
        {
            if(min_c < 0.333)
            {
                outputColor.r = outputColor.r - changeColor.r * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.g * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.b * (interval_down+interval_up) * 0.5;

                outputColor.r = outputColor.r - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.a * (interval_down+interval_up) * 0.5;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中黑色

        if(chooseColor.r == 1 && chooseColor.g == 1 && chooseColor.b == 1 )//选中灰色
        {
            if(min_c >= 0.333 && min_c <= 0.667)
            {
                outputColor.r = outputColor.r - changeColor.r * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.g * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.b * (interval_down+interval_up) * 0.5;

                outputColor.r = outputColor.r - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.g = outputColor.g - changeColor.a * (interval_down+interval_up) * 0.5;
                outputColor.b = outputColor.b - changeColor.a * (interval_down+interval_up) * 0.5;
            }
            else
            {
                outputColor = inputColor;
            }

        }//选中灰色



        outputColor.r = clamp(outputColor.r, 0.0, 1.0);
        outputColor.g = clamp(outputColor.g, 0.0, 1.0);
        outputColor.b = clamp(outputColor.b, 0.0, 1.0);

        return outputColor;

    }


                         lowp vec3 RGBToHSL(lowp vec3 color)

    {
        lowp vec3 hsl; // init to 0 to avoid warnings ? (and reverse if + remove first part)

        lowp float fmin = min(min(color.r, color.g), color.b);    //Min. value of RGB
        lowp float fmax = max(max(color.r, color.g), color.b);    //Max. value of RGB
        lowp float delta = fmax - fmin;             //Delta RGB value

        hsl.z = (fmax + fmin) / 2.0; // Luminance

        if (delta == 0.0)		//This is a gray, no chroma...
        {
            hsl.x = 0.0;	// Hue
            hsl.y = 0.0;	// Saturation
        }
        else                                    //Chromatic data...
        {
            if (hsl.z < 0.5)
                hsl.y = delta / (fmax + fmin); // Saturation
            else
                hsl.y = delta / (2.0 - fmax - fmin); // Saturation

            lowp float deltaR = (((fmax - color.r) / 6.0) + (delta / 2.0)) / delta;
            lowp float deltaG = (((fmax - color.g) / 6.0) + (delta / 2.0)) / delta;
            lowp float deltaB = (((fmax - color.b) / 6.0) + (delta / 2.0)) / delta;

            if (color.r == fmax )
                hsl.x = deltaB - deltaG; // Hue
            else if (color.g == fmax)
                hsl.x = (1.0 / 3.0) + deltaR - deltaB; // Hue
            else if (color.b == fmax)
                hsl.x = (2.0 / 3.0) + deltaG - deltaR; // Hue

            if (hsl.x < 0.0)
                hsl.x += 1.0; // Hue
            else if (hsl.x > 1.0)
                hsl.x -= 1.0; // Hue
        }

        return hsl;
    }


                         lowp float HueToRGB(lowp float f1, lowp float f2, lowp float hue)
    {
        if (hue < 0.0)
            hue += 1.0;
        else if (hue > 1.0)
            hue -= 1.0;
        lowp float res;
        if ((6.0 * hue) < 1.0)
            res = f1 + (f2 - f1) * 6.0 * hue;
        else if ((2.0 * hue) < 1.0)
            res = f2;
        else if ((3.0 * hue) < 2.0)
            res = f1 + (f2 - f1) * ((2.0 / 3.0) - hue) * 6.0;
        else
            res = f1;
        return res;
    }


                         lowp vec3 HSLToRGB(lowp vec3 hsl)
    {
        lowp vec3 rgb;

        if (hsl.y == 0.0)
            rgb = vec3(hsl.z); // Luminance
        else
        {
            lowp float f2;

            if (hsl.z < 0.5)
                f2 = hsl.z * (1.0 + hsl.y);
            else
                f2 = (hsl.z + hsl.y) - (hsl.y * hsl.z);

            lowp float f1 = 2.0 * hsl.z - f2;

            rgb.r = HueToRGB(f1, f2, hsl.x + (1.0/3.0));
            rgb.g = HueToRGB(f1, f2, hsl.x);
            rgb.b= HueToRGB(f1, f2, hsl.x - (1.0/3.0));
        }

        return rgb;
    }


                         lowp float RGBToL(lowp vec3 color)
    {
        lowp float fmin = min(min(color.r, color.g), color.b);    //Min. value of RGB
        lowp float fmax = max(max(color.r, color.g), color.b);    //Max. value of RGB

        return (fmax + fmin) / 2.0; // Luminance
    }



                         void main()
    {
    
    //    gl_FragColor = vec4(1.0,0.0,0.0,1.0);
    //    return;
        mat3 saturateMatrix = mat3(
                                   1.1102,
                                   -0.0598,
                                   -0.061,
                                   -0.0774,
                                   1.0826,
                                   -0.1186,
                                   -0.0228,
                                   -0.0228,
                                   1.1772);

        mat3 brightMatrix = mat3(
                                 1.1,
                                 0.0,
                                 0.0,
                                 0.0,
                                 1.1,
                                 0.0,
                                 0.0,
                                 0.0,
                                 1.1);

        gl_FragColor = texture2D(uTexture,vTextureCoord);


        float dist = distance(vTextureCoord, vec2(0.5,0.5));
        dist = smoothstep(0.0,1.0,dist);
        dist = pow(dist, 1.5);
        vec4 darkenColor = gl_FragColor;
        darkenColor.r = pow(darkenColor.r, 3.0);
        darkenColor.g = pow(darkenColor.g, 3.0);
        darkenColor.b = pow(darkenColor.b, 3.0);

        gl_FragColor = mix(gl_FragColor, darkenColor, dist);

        //L1



        gl_FragColor.r = pow(gl_FragColor.r, 0.8);
        gl_FragColor.g = pow(gl_FragColor.g, 0.8);
        gl_FragColor.b = pow(gl_FragColor.b, 0.8);



        //gl_FragColor.rgb = AdjustColor(gl_FragColor.rgb, ivec3(0,0,0), vec4(0.0,-0.4,0.0,0.0));


        vec3 shadowsShift = vec3(0.0, 0.0, 0.2);
        vec3 midtonesShift = vec3(0.13, 0.0, 0.17);
        vec3 highlightsShift = vec3(0.0, 0.0, 0.0);
        int preserveLuminosity = 0;

        // Alternative way:
        //lowp vec3 lightness = RGBToL(gl_FragColor.rgb);
        lowp vec3 lightness = gl_FragColor.rgb;

        const lowp float a = 0.25;
        const lowp float b = 0.333;
        const lowp float scale = 0.7;

        lowp vec3 shadows = shadowsShift * (clamp((lightness - b) / -a + 0.5, 0.0, 1.0) * scale);
        lowp vec3 midtones = midtonesShift * (clamp((lightness - b) / a + 0.5, 0.0, 1.0) * clamp((lightness + b - 1.0) / -a + 0.5, 0.0, 1.0) * scale);
        lowp vec3 highlights = highlightsShift * (clamp((lightness + b - 1.0) / a + 0.5, 0.0, 1.0) * scale);

        mediump vec3 newColor = gl_FragColor.rgb + shadows + midtones + highlights;
        newColor = clamp(newColor, 0.0, 1.0);

        if (preserveLuminosity != 0) {
            lowp vec3 newHSL = RGBToHSL(newColor);
            lowp float oldLum = RGBToL(gl_FragColor.rgb);
            gl_FragColor.rgb = HSLToRGB(vec3(newHSL.x, newHSL.y, oldLum));
        } else {
            gl_FragColor = vec4(newColor.rgb, gl_FragColor.w);
        }


        float contrast = 0.9;
        gl_FragColor = vec4(((gl_FragColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), gl_FragColor.w);

        gl_FragColor.r = pow(gl_FragColor.r, 0.87);
        gl_FragColor.g = pow(gl_FragColor.g, 0.87);
        gl_FragColor.b = pow(gl_FragColor.b, 0.87);



    }
