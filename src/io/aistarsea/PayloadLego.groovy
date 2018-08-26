package io.aistarsea

import groovy.json.JsonOutput

// payload covert to json
def payloadToJson(channel, text, attachments) {
    custom = JsonOutput.toJson([
        text: "**${text}**",
        channel: channel,
        user: 'aliasmee',
        attachments: 'trap'

    ])

    custom.replaceAll(/"trap"/, "[${attachments}]")

}

return this
