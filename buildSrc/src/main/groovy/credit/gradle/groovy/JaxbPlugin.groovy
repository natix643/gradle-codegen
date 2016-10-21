package credit.gradle.groovy

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JaxbPlugin implements Plugin<Project> {

    def GENERATED_DIR = 'src/generated/java'

    @Override
    void apply(Project project) {
        project.configurations.create('jaxb')
        project.dependencies {

        }

        def extension = project.extensions.create('jaxb', JaxbExtension)
        project.afterEvaluate {
            if (!extension.xsd) throw new GradleException("'xsd' property is required")
            if (!extension.targetPackage) throw new GradleException("'targetPackage' property is required")
        }

        def xjc = project.tasks.create('xjc', Xjc)
        project.tasks.compileJava.dependsOn(xjc)
        project.tasks.clean.configure { delete(GENERATED_DIR) }
    }

}

class JaxbExtension {
    String xsd
    String targetPackage
}

class Xjc extends DefaultTask {

    @InputFile
    File getWsdl() {
        project.file(project.jaxb.xsd)
    }

    @OutputDirectory
    File getGeneratedDir() {
        project.file(JaxbPlugin.GENERATED_DIR)
    }

    @TaskAction
    def run() {

    }
}