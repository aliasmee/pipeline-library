#!/usr/bin/env groovy

def call(String tunnelName = 'vpn') {
    sh(script: "sudo ipsec down ${tunnelName}", returnStatus: true)
    return this
}
