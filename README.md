[![Build Result](https://api.travis-ci.org/RalleYTN/XInput-Plugin-for-JInput.svg?branch=master)](https://travis-ci.org/RalleYTN/XInput-Plugin-for-JInput)
[![Coverage Status](https://coveralls.io/repos/github/RalleYTN/XInput-Plugin-for-JInput/badge.svg?branch=master)](https://coveralls.io/github/RalleYTN/XInput-Plugin-for-JInput?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f294893173c04364bbce3c0f94cdbd6f)](https://www.codacy.com/app/ralph.niemitz/XInput-Plugin-for-JInput?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=RalleYTN/XInput-Plugin-for-JInput&amp;utm_campaign=Badge_Grade)

# Description

Currently JInput does not support the XInput API from Windows so I decided to write a Wrapper for XInput API, which can be found [here](https://github.com/RalleYTN/XInput-Wrapper), and use it to write
a plugin for JInput. It just calls the already existing `DirectAndRawInputEnvironmentPlugin` and replaces the XInput controllers that the `DirectAndRawInputEnvironmentPlugin` reads with DirectInput with the
`XIController` of this plugin. To use the plugin you just have to change the way you retrieve the controller array a little and then you can finally ask for trigger inputs separately and use the vibration function.

### To retrieve the controllers

```java
ControllerEnvironment env = new XInputEnvironmentPlugin();

if(!env.isSupported()) {

	env = ControllerEnvironment.getDefaultEnvironment();
}

Controller[] controllers = env.getControllers();
```

## Changelog

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