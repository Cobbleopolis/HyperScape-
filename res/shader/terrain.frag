#version 330 core

uniform sampler2D texture_diffuse;
uniform vec4 chunkColor;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
	out_Color = chunkColor * texture(texture_diffuse, pass_TextureCoord);
}