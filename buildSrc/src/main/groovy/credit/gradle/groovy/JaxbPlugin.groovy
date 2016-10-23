package credit.gradle.groovy

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JaxbPlugin implements Plugin<Project> {

    static def GENERATED_DIR = 'src/generated/java'

    @Override
    void apply(Project p) {
        p.sourceSets {
            main.java {
                srcDirs += GENERATED_DIR
            }
        }

        p.configurations.create('jaxb')

        p.dependencies.add('jaxb', "com.sun.xml.bind:jaxb-core:${p.jaxbVersion}")
        p.dependencies.add('jaxb', "com.sun.xml.bind:jaxb-impl:${p.jaxbVersion}")
        p.dependencies.add('jaxb', "com.sun.xml.bind:jaxb-xjc:${p.jaxbVersion}")

        def extension = p.extensions.create('jaxb', JaxbExtension)
        p.afterEvaluate {
            if (!extension.xsd)
                throw new GradleException("'xsd' property is required")
            if (!extension.targetPackage)
                throw new GradleException("'targetPackage' property is required")
        }

        def xjc = p.tasks.create('xjc', Xjc)

        p.tasks['compileJava'].configure {
            dependsOn(xjc)
        }

        p.tasks['clean'].configure {
            delete(GENERATED_DIR)
        }
    }

}

class JaxbExtension {
    String xsd
    String targetPackage
}

class Xjc extends DefaultTask {

    @InputFile
    File getXsd() {
        project.file(project.extensions['jaxb'].xsd)
    }

    @OutputDirectory
    File getGeneratedDir() {
        project.file(JaxbPlugin.GENERATED_DIR)
    }

    @TaskAction
    def run() {
        project.mkdir(generatedDir)

        ant.taskdef(
                name: 'xjc',
                classname: 'com.sun.tools.xjc.XJCTask',
                classpath: project.configurations['jaxb'].asPath)
        ant.xjc(
                schema: xsd,
                destdir: generatedDir,
                package: project.extensions['jaxb'].targetPackage)
    }
}
