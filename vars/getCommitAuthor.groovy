#!/usr/bin/env groovy

def call() {
    def commit = getCommitHash()
    author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
    return author
}
