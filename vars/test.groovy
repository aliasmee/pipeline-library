x = ''
y = 3
tag = x ?: y
println(tag)

currentBuild = [:]
currentBuild['result'] = 'FAILURE'

if (tag) {
  println("x is max y")
} else {
  println("x is small y")
}

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

def coverageResults() {
  def msg = ''
  def color = colorByResult(currentBuild.result)
//  def coverageResults = currentBuild.rawBuild.getAction(hudson.plugins.jacoco.JacocoBuildAction.class)

//  if (coverageResults != null) {
    def instructionCoverage = String.format('%.2f', 10.987)
    def lineCoverage = String.format('%.2f', 20.999)
    msg += """,{"title":"Coverage Results:","text":">• Instruction Coverage: ${instructionCoverage}\n>• Line Coverage: ${lineCoverage}", "color":"${color}"}"""
//  }

  return msg
}


def testMsg(branch, testResults, lastCommit, coverage='', failedTest='', color=colorLegible()) {
    def msg = ''
	  msg += """{"title":"Status:","text":"*${currentBuild.result}*", "color":"${color}"},
	            {"title":"Branch:","text":">*${branch}*", "color":"${color}"},
              {"title":"Test Results:", "text":">*${testResults}*", "color":"${color}"},
              {"title":"Last Commit:", "text":">*${lastCommit}*", "color":"${color}"}"""
    if (coverage) {
        msg += coverage
    }

    if (failedTest?.trim()) {
        msg += """,{"title":"Failed Test:", "text":"${failedTest}", "color":"${color}"}"""
    }

    return msg
}

println("---------hahah")
println testMsg('master','10','fix some error')


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


println colorByResult('good')


currentBuild = [:]
currentBuild['result'] = 'FAILURE'
env = [:]
env['BUILD_URL'] = 'http://www.google.com'

def deployMsg(branch, environment, address, commits=null, coverage=null) {
    def msg = ''
    def color = colorByResult(currentBuild.result)
    msg = """{"title":"Status:","text":">*${currentBuild.result}*", "color":"${color}"},
	            {"title":"Branch:","text":">*${branch}*", "color":"${color}"},
              {"title":"Deployed to:", "text":">*${environment}*", "color":"${color}"}"""
    if (commits) {
        msg += """,{"title":"Commits:", "text":">*${commits}*", "color":"${color}"}"""
    }
    if (address) {
        msg += """,{"title":"Service URL:", "text":">*${address})*", "color":"${color}"}"""

    }
    msg += """,{"title":"Deploy Detail:", "text":">*${env.BUILD_URL})*", "color":"${color}"}"""
    println msg
}

deployMsg('master', 'staging', 'https://test.zhulu.tld',"fix some error, aliasmee")


STAGE_NAME = 'Build Image'
env['BUILD_URL'] = 'http://www.google.com'


def errorMsg(message) {
    def msg = ''
    def color = colorByResult(currentBuild.result)
    msg = """{"title":"Status:","text":">*${currentBuild.result}*", "color":"${color}"},
             {"title":"Stage Name:","text":">*${STAGE_NAME}*", "color":"${color}"},
             {"title":"Error Detail:","text":">*${env.BUILD_URL}*", "color":"${color}"}"""

    if (message) {
      msg += """,{"title":"Error Message:","text":">*```${message}```*", "color":"${color}"}"""
    }

    println msg
}

errorMsg('DB migration 执行失败')
env['BUILD_URL'] = 'http://www.google.com'


def buildMsg(message) {
    def msg = ''
    def currentBuild = [:]
    currentBuild['result'] = 'SUCCESS'
    def color = colorByResult(currentBuild.result)
    msg = """{"title":"Status:","text":">*${currentBuild.result}*", "color":"${color}"},
             {"title":"Stage Name:","text":">*${STAGE_NAME}*", "color":"${color}"},
             {"title":"Build Detail:","text":">*${env.BUILD_URL}*", "color":"${color}"}"""

    if (message) {
        msg += """,{"title":"Message:","text":">*```${message}```*", "color":"${color}"}"""
    }

    return msg
}

imageName = "optimus:v0.3"

println buildMsg("镜像 ${imageName} 构建成功")


def taskStart(message='Build Started...') {
    def msg = ''
    def color = colorLegible('good')
    msg = """{"title":"Messages:","text":">*${message}*", "color":"${color}"},
             {"title":"Build Detail:","text":">*${env.BUILD_URL}*", "color":"${color}"}"""

    return msg
}

println taskStart()



def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    def summary = ""

    if (testResultAction != null) {
        total = testResultAction.getTotalCount()
        failed = testResultAction.getFailCount()
        skipped = testResultAction.getSkipCount()

        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
    return summary
}
