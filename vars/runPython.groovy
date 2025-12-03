// vars/runPython.groovy
def call(Map args = [:]) {
    // default args
    String pythonCmd = args.get('pythonCmd', 'python')
    String script = args.get('script', '')                 // path in workspace (Option A)
    String resource = args.get('resource', '')             // resource path inside library (Option B)
    String scriptArgs = args.get('scriptArgs', '')
    boolean captureOutput = args.get('captureOutput', false)

    if (!script && !resource) {
        error "Either 'script' (workspace path) or 'resource' (library resource path) must be provided"
    }

    // If resource is provided, write file to workspace using libraryResource
    if (resource) {
        echo "Writing resource ${resource} to workspace as embedded_script.py"
        def content = libraryResource(resource)   // resources/org/... path
        writeFile file: 'embedded_script.py', text: content
        script = 'embedded_script.py'
    }

    // Build command
    String fullCmd = "${pythonCmd} ${script} ${scriptArgs}".trim()

    if (captureOutput) {
        // capture stdout and return it
        def out = sh(returnStdout: true, script: fullCmd).trim()
        echo "Python output: ${out}"
        return out
    } else {
        sh(script: fullCmd)
        return null
    }
}
