package com.goodworkalan.comfort.xml.mix;

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
                .produces("com.github.bigeasy.comfort-xml/comfort-xml/0.1.0.5")
                .depends()
                    .production("com.github.bigeasy.danger/danger/0.+3")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
