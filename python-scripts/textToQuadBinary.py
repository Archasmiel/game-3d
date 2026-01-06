import struct
import zlib
import re

def parse_quad_file(input_file: str):
    faces = []
    vertices = []
    current_color = None

    with open(input_file, 'r') as file:
        for line in file:
            line = line.strip()
            if not line:
                continue

            if line.startswith('c3f'):
                # parse color
                match = re.search(r'c3f\(([-\d.f]+),\s*([-\d.f]+),\s*([-\d.f]+)\)', line)
                if match:
                    current_color = [float(match.group(i).replace('f', '')) for i in range(1, 4)]

            elif line.startswith('v3f'):
                # parse vertex
                match = re.search(r'v3f\(([-\d.f]+),\s*([-\d.f]+),\s*([-\d.f]+)\)', line)
                if match:
                    vertex = [float(match.group(i).replace('f', '')) for i in range(1, 4)]
                    vertices.append(vertex)

                    # append face every time we have 4 vertices
                    if len(vertices) == 4:
                        if current_color is None:
                            raise ValueError("Vertex found before color")
                        faces.append([current_color, *vertices])
                        vertices.clear()  # reset for next face

        # in case file ends without empty line
        if vertices:
            if current_color is None or len(vertices) != 4:
                raise ValueError("Incomplete face at end of file")
            faces.append([current_color, *vertices])

        print(f'Input file size {file.tell()} bytes')

    return faces



def write_binary_quad(output_file, faces):

    binary = struct.pack('<I', len(faces))

    for face in faces:
        data = []
        for arr in face:
            data.extend(arr)
        print(data)
        binary += struct.pack("<15f", *data)

    print(f'Original size {len(binary)}')

    binary = zlib.compress(binary, level=9)
    print(f'Compressed size {len(binary)}')
    
    with open(output_file, 'wb') as file:
        file.write(binary)
        

if __name__ == '__main__':
    for i in ['octosphere_quads_96', 'octosphere_quads_384', 'octosphere_quads_1536', 'cube',]:
        faces = parse_quad_file(f'../models/{i}.txt')
        write_binary_quad(f'../models/{i}.qbin', faces)