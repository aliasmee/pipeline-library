#!/usr/bin/env groovy

def call(String tunnelName = 'vpn') {
    sh(script: "sudo ipsec up ${tunnelName}", returnStatus: true)
    return this
}
