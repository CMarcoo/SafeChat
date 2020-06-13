## Contributing
When making a contribute to this project,
please first discuss the change you're going to make via a GitHub issue or email before making a change.

### Pull Requests
1. Be sure to be up to date with the latest version of the 'master' branch.
2. Create a well written explaination for what you have added or modified
3. Be patient and wait for developers to review it.

## Code Style

**Java Specifications**<br>
When contributing you must enforce Java 8 standards, any attempt to use Java 9 and above features will not be accepted.<br>
**Code flow**<br>
Avoid any use of nested blocks when possible, they make the code harder to read , hence we want to avoid them.<br>
Also avoid unnecessary long conditions, when possible save a reference instead of using multiple getters in a condition.<br>
Bad Example:
```java
public class MyClass {
private void coolMethod(CommandSender sender) {
    if (sender instanceof Player) {
        final Player player = (Player) sender;
        if (player.getName().equals("TheViperShow")) {
            if (player.hasPermission("perm")) {
                doSomething();
            }
        }
    }
}
}
```
Good example:
````java
public class MyClass {
    private void coolMethod(CommandSender sender) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        String playerName = player.getName();
        if (!playerName.equals("TheViperShow")) return;
        if (!player.hasPermission("perm")) return;
        doSomething();
    }
}
````

<br>Avoid at any cost nested classes.