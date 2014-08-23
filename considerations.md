# Considerations

## Names of the commands

### Confliction with other names

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

This is not good:

```sh
$ git clone YOUR_APP_REPO.git REPO
$ cd REPO

# Maybe forcing this step causes troubles...
$ ./gradlew initGitDependencies

$ ./gradlew assemble
```


