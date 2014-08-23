# Considerations

Considerations of design to make 'gag' a useful plugin.

## Names of the commands

### Avoid conflicts with other names

Keeping Android plugin commands' availability is the highest priority.  
So names which have prefixes like following should be avoided:

* assemble
* build
* install
* check
* connected

## Workflow

### Avoiding manual operations

It is necessary to avoid forcing manual operations to team members.

Although team leaders who manages app's dependencies should know about this plugin's spec or status of dependencies,  
members shouldn't care about any of these things.

Projects should be built with normal commands to lower the cost of learning.  
For example, following commands always should be executed successfully.

```sh
$ git clone YOUR_APP_REPO.git REPO
$ cd REPO
$ ./gradlew assemble
```

### Configure with codes, not operations

All of the configurations to manage dependencies should be managed by codes.  
Because people may forget to do some operations,  
which causes unstable environment,  
and unfortunately if it doesn't cause any build errors, maybe it will cause very different bugs.

So this is not good:

```sh
$ git clone YOUR_APP_REPO.git REPO
$ cd REPO

# Maybe forcing this step causes troubles...
$ ./gradlew initGagDependencies

$ ./gradlew assemble
```

### Always update dependencies

To updating dependencies(repositories) only when there are any updates is not good workflow.  
We will forget to do this.

Following steps always should be done for each dependencies when building apps:

```
# If the REPO doesn't exist:
$ git clone REPO

# Check out the target version.
# REPO should not be edited,
# so discarding changes will be appropriate operation
$ git checkout --force VERSION
```

So updating task should be inserted before `:preBuild` task or something like that.

### Add new dependency smoothly

Currently, following steps will be needed to introduce a new Git dependency.

1. Add `repo` configuration to `git.dependencies` closure.
1. Add `compile project(':library:foo')` to `project.dependencies`.
1. Add `include :library:foo` to `settings.gradle`.
This is necessary to evaluate dependencies for gradle but it is not supported to add sub-projects dynamically by plugins.

Gradle evaluates `settings.gradle` before the configuration phase,  
so plugin configuration initializers(constructors) should prepare for gradle to be recognized as valid sub-projects.  
Example:

1. Make its directories
1. Create minimum files to be recognized as a project(`build.gradle`)

Without these steps, projects can't be built after checking out the root project repository.

Perhaps the second of the above steps should be replaced to `git clone` or something.
