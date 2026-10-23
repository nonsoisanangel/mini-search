# AI Notes

A log of where AI tools helped, where they got things wrong, and how I caught it.


### Entry 1: AI tool upgraded the project to Java 25 without verifying it
- **Tool:** (Copilot / Cursor / Claude Code / etc.)
- **I asked for: a java upgrade of the project
- **It produced: a change setting the complier release to java 25 and a readme update
- **What was wrong or weak: its own summary said the Java 25 compile and tests were skipped, so nothing was verified. My machine has Java 17, so the project would not have compiled. Nothing in the code needs Java 25 either.
- **How I caught it: I read the summary closely and checked pom.xml with grep, which showed 25 on the release line.
- **How I fixed it: I set it back to 17 and confirmed that mvn test passed


