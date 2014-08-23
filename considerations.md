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


