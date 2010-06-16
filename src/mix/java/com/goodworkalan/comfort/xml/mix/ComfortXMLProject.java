package com.goodworkalan.comfort.io.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Comfort XML.
 *
 * @author Alan Gutierrez
 */
public class ComfortXMLProject implements ProjectModule {
    /**
     * Build the project definition for Comfort XML.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.comfort-xml/comfort-xml/0.1.0.3")
                .depends()
                    .production("com.github.bigeasy.danger/danger/0.1.+0")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}