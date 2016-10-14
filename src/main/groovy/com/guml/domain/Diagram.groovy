package com.guml.domain

import net.sourceforge.plantuml.SourceStringReader

class Diagram {
    def id
    String dsl

    def buildImage() {
        if (dsl) {
            OutputStream os = new ByteArrayOutputStream()
            SourceStringReader reader = new SourceStringReader(dsl)
            reader.generateImage(os)
            os.toByteArray()
        }
    }
}
