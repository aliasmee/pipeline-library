#!/usr/bin/env groovy

import io.aistarsea.PayloadLego

def call(Map config = [:]) {
    if (!config.endpoint) {
        echo 'endpoint is required!'
        return
    }

    if (!config.text) {
//        echo 'text is required!'
//        return
        config.text = "${JOB_NAME} [${BUILD_DISPLAY_NAME}](${BUILD_URL})"
        return config.text
    }
//    color = config.color?: '#00BFFF'
    def mmUtils = new PayloadLego()
    def payload = mmUtils.payloadToJson(config.channel, config.text, config.attachments)

    sh "curl -X POST -d \'payload=${payload}\' ${config.endpoint}"
}
