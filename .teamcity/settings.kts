import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.1"

project {
    subProject(TestSubproject)
    buildType(Build)
}

object TestSubproject : Project({
    name = "Test Subproject"
    buildType(SubBuild)
})

object Build : BuildType({
    name = "Build"

    steps {
        script {
            scriptContent = "echo 'Hello World!'"
        }
    }

    features {
        pullRequests {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            provider = github {
                authType = token {
                    token = "credentialsJSON:c0024323-e847-4c55-a1be-431f6f1c14e8"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
            }
        }
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    triggers {
        vcs {
        }
    }
})

object SubBuild : BuildType({
    name = "Build"

    steps {
        script {
            scriptContent = "echo 'Hello Subproject!'"
        }
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    triggers {
        vcs {
        }
    }
})
