package io.aistarsea

import groovy.json.JsonOutput

// payload covert to json
def payloadToJson(channel, attachments, text) {
    custom = JsonOutput.toJson([
        text: "**${text}**",
        channel: channel,
        user: 'aliasmee',
        attachments: 'trap20180828'

    ])

    custom.replaceAll(/"trap20180828"/, "[${attachments}]")

}

return this
