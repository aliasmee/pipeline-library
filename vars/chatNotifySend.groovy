#!/usr/bin/env groovy

import io.aistarsea.PayloadLego

def call(Map config = [:]) {
    if (!config.endpoint) {
        echo 'endpoint is required!'
        return
    }


    def mmUtils = new PayloadLego()
    if (!config.text) {
        config.text = "${JOB_NAME} [${BUILD_DISPLAY_NAME}](${BUILD_URL})"
    }
    def payload = mmUtils.payloadToJson(config.channel, config.attachments, config.text)

    sh "curl -X POST -d \'payload=${payload}\' ${config.endpoint}"
}
