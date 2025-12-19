package geometry

data class IndexedModel(val vs: List<Vertex3f>, val faces: List<IndexedFace>)
data class IndexedFace(val c: Color3f, val vIs: List<Int>)