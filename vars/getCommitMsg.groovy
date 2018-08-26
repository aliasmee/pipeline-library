#!/usr/bin/env groovy

def call() {
    message = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
    return message
}
