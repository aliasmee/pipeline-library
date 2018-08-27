package io.aistarsea

import groovy.json.JsonOutput

// payload covert to json
def payloadToJson(channel, attachments, text="${JOB_NAME} [${BUILD_DISPLAY_NAME}](${BUILD_URL})") {
    custom = JsonOutput.toJson([
        text: "**${text}**",
        channel: channel,
        user: 'aliasmee',
        attachments: 'trap'

    ])

    custom.replaceAll(/"trap"/, "[${attachments}]")

}

return this
