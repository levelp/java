attribute float wave;

void main() {
	vec4 vertex = gl_Vertex;
	vertex.y = ( sin(wave + (vertex.x / 5.0) ) + sin(wave + (vertex.z / 4.0) ) ) * 2.5;
    gl_Position = gl_ModelViewProjectionMatrix * vertex;
    gl_FrontColor = gl_Color;
    gl_BackColor = gl_Color;
}