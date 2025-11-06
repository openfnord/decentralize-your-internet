// This script will update all Jenkins plugins to the latest version available in the Jenkins Update Center.
// It will also print the list of plugins that were updated.
// Keep note that it has a different Syntax than the previous scripts.
// preferred syntax for Jenkins pipelines is the declarative syntax, which is the one used 
// in the previous scripts.
// just accept it for this pipeline ¯\_(ツ)_/¯
// you'll have to build this script a few times after each fail click on the last failed build number
// -> "console output" 
// -> "Administrators can decide whether to approve or reject this signature." 
// -> "Approve"
// repeat until success
//
// Recommendation: Run once a night
// H 4 * * * 


jenkins.model.Jenkins.getInstance().getUpdateCenter().getSites().each { site ->
  site.updateDirectlyNow(hudson.model.DownloadService.signatureCheck)
}

hudson.model.DownloadService.Downloadable.all().each { downloadable ->
  downloadable.updateNow();
}

def plugins = jenkins.model.Jenkins.instance.pluginManager.activePlugins.findAll {
  it -> it.hasUpdate()
}.collect {
  it -> it.getShortName()
}

println "Plugins to upgrade: ${plugins}"
long count = 0

jenkins.model.Jenkins.instance.pluginManager.install(plugins, false).each { f ->
  f.get()
  println "${++count}/${plugins.size()}.."
}

