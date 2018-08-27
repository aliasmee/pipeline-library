package io.aistarsea

import java.util.Optional
import hudson.tasks.test.AbstractTestResultAction
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult
import com.cloudbees.groovy.cps.NonCPS


@NonCPS
def colorLegible(color) {
    def result
    switch(color) {
        case 'good':
            result = '#2ECC71'
            break
        case 'warning':
            result = '#F1C40F'
            break
        case 'danger':
            result = '#E74C3C'
            break
        default:
            result = '#85929E'
    }
    return result
}


@NonCPS
def colorByResult(buildResult) {
    def color = ''
    switch(buildResult) {
        case 'SUCCESS':
            color = colorLegible('good')
            break
        case 'UNSTABLE':
            color = colorLegible('warning')
            break
        case 'FAILURE':
            color = colorLegible('danger')
            break
        default:
            color = colorLegible()
    }
    return color
}


@NonCPS
def getTestSummary() {
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        skipped = testResultAction.getSkipCount()

        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed + ' / ' + ${testResultAction.failureDiffString})
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}

@NonCPS
def getFailedTests() {
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def failedTestsString = "```"

    if (testResultAction != null) {
        def failedTests = testResultAction.getFailedTests()

        if (failedTests.size() > 9) {
            failedTests = failedTests.subList(0, 8)
        }

        for(CaseResult cr : failedTests) {
            failedTestsString = failedTestsString + "${cr.getFullDisplayName()}:\n${cr.getErrorDetails()}\n\n"
        }
        failedTestsString = failedTestsString + "```"
    }
    return failedTestsString
}


@NonCPS
def commitMsg() {
    def changeLogSets = currentBuild.changeSets
    def c = 0
    def msg = ""
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            msg += "${entry.msg} - ${entry.author}"
            c+=1
            if (c > 4) return msg += '\n ...\n'
        }
    }
    return msg
}

@NonCPS
def testMsg(branch, testResults, lastCommit, coverage='', color=colorLegible(), failedTest='') {
    def msg = ''
	  msg += """{"title":"Status:","text":">*${currentBuild.currentResult}*", "color":"${color}"},
	            {"title":"Branch:","text":">*${branch}*", "color":"${color}"},
              {"title":"Test Results:", "text":">*${testResults}*", "color":"${color}"},
              {"title":"Last Commit:", "text":">*${lastCommit}*", "color":"${color}"}"""
    if (coverage) {
        msg += coverageResults()
    }

    if (failedTest?.trim()) {
        msg += """,{"title":"Failed Test:", "text":"${failedTest}", "color":"${color}"}"""
    }

    return msg
}


@NonCPS
def coverageResults() {
  def msg = ''
  def color = colorByResult(currentBuild.currentResult)
  def coverageResults = currentBuild.rawBuild.getAction(hudson.plugins.jacoco.JacocoBuildAction.class)

  if (coverageResults != null) {
    def instructionCoverage = String.format('%.2f', coverageResults.getInstructionCoverage().getPercentageFloat())
    def lineCoverage = String.format('%.2f', coverageResults.getLineCoverage().getPercentageFloat())
    msg += """,{"title":"Coverage Results:","text":">• Instruction Coverage: ${instructionCoverage}\n>• Line Coverage: ${lineCoverage}", "color":"${color}"}"""
  }

  return msg
}


@NonCPS
def deployMsg(branch, environment, address, commits=null, coverage=null) {
    def msg = ''
    def color = colorByResult(currentBuild.currentResult)
    msg = """{"title":"Status:","text":">*${currentBuild.currentResult}*", "color":"${color}"},
	            {"title":"Branch:","text":">*${branch}*", "color":"${color}"},
              {"title":"Deployed to:", "text":">*${environment}*", "color":"${color}"}"""
    if (commits) {
        msg += """,{"title":"Commits:", "text":">*${commits}*", "color":"${color}"}"""
    }
    if (address) {
        msg += """,{"title":"Service URL:", "text":">*${address}*", "color":"${color}"}"""

    }
    msg += """,{"title":"Deploy Detail:", "text":">*${env.BUILD_URL}/console*", "color":"${color}"}"""
    return msg
}


@NonCPS
def errorMsg(message='') {
    def msg = ''
    def color = colorByResult(currentBuild.currentResult)
    msg = """{"title":"Status:","text":">*${currentBuild.currentResult}*", "color":"${color}"},
             {"title":"Stage Name:","text":">*${STAGE_NAME}*", "color":"${color}"},
             {"title":"Error Detail:","text":">*${env.BUILD_URL}/console*", "color":"${color}"}"""

    if (message) {
      msg += """,{"title":"Error Message:","text":">*```${message}```*", "color":"${color}"}"""
    }

    return msg
}

@NonCPS
def buildMsg(message='') {
    def msg = ''
    def color = colorByResult(currentBuild.currentResult)
    msg = """{"title":"Status:","text":">*${currentBuild.currentResult}*", "color":"${color}"},
             {"title":"Stage Name:","text":">*${STAGE_NAME}*", "color":"${color}"},
             {"title":"Build Detail:","text":">*${env.BUILD_URL}/console*", "color":"${color}"}"""

    if (message) {
        msg += """,{"title":"Message:","text":">*```${message}```*", "color":"${color}"}"""
    }

    return msg
}

@NonCPS
def taskStart(message='Build Started...') {
    def msg = ''
    def color = colorLegible('good')
    msg = """{"title":"Messages:","text":">*${message}*", "color":"${color}"},
             {"title":"Build Detail:","text":">*${env.BUILD_URL}/console*", "color":"${color}"}"""

    return msg
}

@NonCPS
def migrationMsg() {

}

return this

/*

{"text":"**[optimus-project, build #2](https://www.bing.com)**","channel":"devops","user":"aliasmee",
"attachments":[
	{"title":"Status:","text":"*Success*", "color":"##E74C3C"},
	{ "title":"Branch:","text":">*[master](https://www.bing.com)*", "color":"#E74C3C"},
    {"title":"Test Results:", "text":">*Passed:10, Failed:0, Skipped:0*", "color":"#E74C3C"},
    {"title":"Last Commit:", "text":">*[fix some error](https://www.bing.com)*", "color":"#E74C3C"},
    {"title":"FailedTest:", "text":"``` Nonedddd ```", "color":"#85929E"}
    ]
}
*/
