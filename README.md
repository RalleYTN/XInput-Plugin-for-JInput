[![CircleCI](https://dl.circleci.com/status-badge/img/gh/RalleYTN/XInput-Plugin-for-JInput/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/RalleYTN/XInput-Plugin-for-JInput/tree/master)
[![CodeFactor](https://www.codefactor.io/repository/github/ralleytn/xinput-plugin-for-jinput/badge)](https://www.codefactor.io/repository/github/ralleytn/xinput-plugin-for-jinput)
[![](https://jitpack.io/v/RalleYTN/XInput-Plugin-for-JInput.svg)](https://jitpack.io/#RalleYTN/XInput-Plugin-for-JInput)


# Description

Since [jinput](https://github.com/jinput/jinput) was written to be platform independent it has no plugin for the Windows specific [XInput API](https://msdn.microsoft.com/de-de/library/windows/desktop/hh405053(v=vs.85)). This can cause XBox gamepads to not function properly. This library aims to solve the problem by providing such a plugin.

The components within this plugin were written with the [XInput-Wrapper](https://github.com/RalleYTN/XInput-Wrapper) to enable access to the [XInput API](https://msdn.microsoft.com/de-de/library/windows/desktop/hh405053(v=vs.85)).

### Code Example

```java
ControllerEnvironment env = new XInputEnvironmentPlugin();

if(!env.isSupported()) {

	env = ControllerEnvironment.getDefaultEnvironment();
}

Controller[] controllers = env.getControllers();
```

## Maven

In order to use XInput-Plugin-for-JInput as a Maven dependency in your own projects you first have to include Jitpack as a repository to your POM.

```xml
<project>
	...
	<repositories>
		...
		<repository>
			<id>jitpack.io</id>
			<url>https://jitpack.io</url>
		</repository>
		...
	</repositories>
	...
</project>
```

Then add the following as dependency:

```xml
<dependency>
	<groupId>com.github.RalleYTN</groupId>
	<artifactId>XInput-Plugin-for-JInput</artifactId>
	<!-- NOTE: You can also use the commit ID as version number -->
	<version>1.2.1</version>
</dependency>
```

## Changelog

### Version 1.2.1-SNAPSHOT

- TEMPORARY BUGFIX: rumblers do not work as intended due to a problem with unsigned integers in the XInput Wrapper library; a proper fix will come at a later date

### Version 1.2.0

- Made the library compatible with Java 11
- Updated dependencies
- Switched from Travis CI to CircleCI

### Version 1.1.0

- If a gamepad has no rumblers, no Rumbler objects will be returned.
- If a gamepad has no navigation (no START, BACK and POV) then these components will not be returned.
- Added a `module-info.java`.

### Version 1.0.0

- Release

## License

```
MIT License

Copyright (c) 2018 Ralph Niemitz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Links

- [Online Documentation](https://ralleytn.github.io/XInput-Plugin-for-JInput/)
- [Download](https://github.com/RalleYTN/XInput-Plugin-for-JInput/releases)