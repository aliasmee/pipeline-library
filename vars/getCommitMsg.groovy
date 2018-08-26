#!/usr/bin/env groovy

def call() {
    def commit = getGitCommit()
    author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
    return author
}
