#version 330 core

uniform sampler2D texture_diffuse;
uniform vec4 chunkColor;
uniform vec3 sunDirection;
uniform vec4 sunColor;
uniform float sunIntensity;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in float fogPct;
in vec3 pass_Normal;

out vec4 out_Color;

const vec4 fogColor = vec4(0.67058823529411764705882352941176, 0.8078431372549019607843137254902, 1, 1);

void main(void) {
	out_Color *= sunIntensity;
	out_Color = pass_Color * chunkColor * texture(texture_diffuse, pass_TextureCoord) * sunColor;
	if (out_Color.a == 0.0) {
		discard;
	}
	float angle = dot(pass_Normal, normalize(sunDirection));
	float sunPct = cos(angle);
	if (angle > 0) { out_Color *= sunPct; }
//	out_Color = min(out_Color * (1.0 - sunPct) + (sunColor * sunPct), 1.0);
	if(fogPct > 0.0) {
        if(fogPct == 1.0) {
            discard;
        }
        out_Color = out_Color * (1.0 - fogPct) + fogColor * fogPct;
    }
    //gl_FragColor = outColor;
}