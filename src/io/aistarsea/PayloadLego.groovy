package io.aistarsea

import groovy.json.JsonOutput

// payload covert to json
def payloadToJson(channel, text, attachments) {
    JsonOutput.toJson([
        text: "**${text}**",
        channel: channel,
        user: 'aliasmee',
        attachments: ["${attachments}"]
    ])
}

return this
