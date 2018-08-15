package io.aistarsea

import groovy.json.JsonOutput

// payload covert to json
def payloadToJson(channel, text, color) {
    JsonOutput.toJson([
        text: text,
        channel: channel,
        user: 'aliasmee',
        attachments: [[
            color: color,
            text: text
        ]]
    ])
}

return this
