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
**Lombok**<br>
When contributing to this project, you must make proper use of Lombok and adapt your code style to it: avoid writing anything that could be obtained with Lombok features.
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

**Other notices**:
<br>Avoid at any cost nested classes, They make reading a file harder.
If you do not find a proper package where the class could be put, you're allowed to make a properly named package (remember to follow Java naming conventions).
<br>You must follow the min-dif rule, don't make unnecessary changes and if not absolutely required. Anything that doesn't respect this will be rejected.