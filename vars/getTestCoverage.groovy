#!groovy

def call(filePath='./coverage/.last_run.json') {
    def coverageFile = fileExists filePath
    if (coverageFile) {
        def coverageResult = readJSON file: filePath
        coveragePercent = coverageResult['result']['covered_percent']
        return coveragePercent
    } else {
        println "Coverage report file Not Found on ${filePath}"
        currentBuild.result = "FAILURE"
    }
}
