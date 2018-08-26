#!/usr/bin/env groovy

def call() {
    def commitHash = sh(script: "git rev-parse --short HEAD", returnStdout: true)
    return commitHash? commitHash.trim(): ''
}
