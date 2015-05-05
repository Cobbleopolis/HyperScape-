#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec3 in_Position;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
	pass_Color = vec4(1, 1, 1, 1);
	//pass_Color = vec4(in_Position.x / 16, in_Position.y / 16, in_Position.z / 16, 1);
	pass_TextureCoord = in_TextureCoord;
}