#!/usr/bin/env groovy

import io.aistarsea.PayloadLego

def call(Map config = [:]) {
    if (!config.endpoint) {
        echo 'endpoint is required!'
        return
    }

    // if (!config.text) {
    //     echo 'text is required!'
    //     return
    // }
//    color = config.color?: '#00BFFF'
    def mmUtils = new PayloadLego()
    if (!config.text) {
        config.text = "${JOB_NAME} [${BUILD_DISPLAY_NAME}](${BUILD_URL})"
    }
    def payload = mmUtils.payloadToJson(config.channel, config.attachments, config.text)

    sh "curl -X POST -d \'payload=${payload}\' ${config.endpoint}"
}
