<p align="center"><img src="https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/cover.svg" width="100%" align="center"/></p>
<p align="center"> <a href="https://www.android.com"><img src="https://img.shields.io/badge/platform-Android-yellow.svg" alt="platform"></a>
 <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat" alt="API"></a> <a href="https://github.com/ahmmedrejowan/DeviceInfo/blob/master/LICENSE"><img src="https://img.shields.io/github/license/ahmmedrejowan/DeviceInfo" alt="GitHub license"></a> </p>

 <p align="center"> <a href="https://github.com/ahmmedrejowan/DeviceInfo/issues"><img src="https://img.shields.io/github/issues/ahmmedrejowan/DeviceInfo" alt="GitHub issues"></a> <a href="https://github.com/ahmmedrejowan/DeviceInfo/network"><img src="https://img.shields.io/github/forks/ahmmedrejowan/DeviceInfo" alt="GitHub forks"></a> <a href="https://github.com/ahmmedrejowan/DeviceInfo/stargazers"><img src="https://img.shields.io/github/stars/ahmmedrejowan/DeviceInfo" alt="GitHub stars"></a> <a href="https://github.com/ahmmedrejowan/DeviceInfo/graphs/contributors"> <img src="https://img.shields.io/github/contributors/ahmmedrejowan/DeviceInfo" alt="GitHub contributors"></a>   </p>
<hr>

## Idea and Motivation
After trying out a bunch of device information apps, I got curious about how they work. That led me to think, "Why not make one myself?"

So, I used Kotlin and a bit of Java, plus some basic XML design, to create my own. Before diving in, I checked out similar apps to see what features I liked. Then, I made a list of what I wanted in my app. My main goal? To make it super easy to use and give you all the info you need about your device.

It took about a month to finish everything – planning, designing, building, and testing. But now, Device Info is good to go!

[Read the whole story](https://cv.rejowan.com/device-info/)

## Features
The app consists of five main components that work together to provide a seamless user experience.

**Dashboard**
- At-a-Glance Insights: Get a quick overview of your device’s most crucial information, all presented on a beautifully designed dashboard.

**Detailed Information**
- Component Breakdown: Explore in-depth details of your device’s CPU, GPU, OS, Display, RAM, Storage, Battery, and more.
- Connectivity and Network: Understand your device’s network capabilities, including Wi-Fi, Bluetooth, and NFC connections.

**Real-Time Monitoring**
- Performance Metrics: Monitor real-time data on CPU and RAM usage, as well as battery health, to keep your device running smoothly.

**App Management**
- Comprehensive App Lists: Manage both installed and system apps, sorted by name, size, and installation date, with detailed pages for each app.

**Hardware Testing**
- Interactive Tests: Verify the functionality of your device’s hardware, including multi-touch, flash, speaker, microphone, and various sensors.

## Screenshots

**Light Mode**

|Shot 1|Shot 2|Shot 3|Shot 4|
|---|---|---|---|
|  ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot1.jpg)  |  ![Shot2](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot2.jpg) | ![Shot3](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot3.jpg) | ![Shot4](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot4.jpg) |

**Dark Mode**

|Shot 5|Shot 6|Shot 7|Shot 8|
|---|---|---|---|
|  ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot5.jpg)  |  ![Shot2](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot6.jpg) | ![Shot3](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot7.jpg) | ![Shot4](https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/shot8.jpg) |

## Download

The app is not available on the Play Store yet. You can download the latest version (0.1) APK from here

<a href="https://github.com/ahmmedrejowan/DeviceInfo/releases/download/1.0.0/DeviceInfo.apk">
<img src="https://raw.githubusercontent.com/ahmmedrejowan/DeviceInfo/master/files/download.png" width="256px" align="center"/>
</a>

<br>

Check out the [releases](https://github.com/ahmmedrejowan/DeviceInfo/releases) section for more details.

## Tech Stack
<p><strong>Programming Languages</strong></p>
<ul>
<li><strong>Java &amp; Kotlin</strong>: For robust and efficient app development.</li>
<li><strong>XML</strong>: To craft the user interface with precision.</li>
</ul>
<p><strong>Database</strong></p>
<ul>
<li><strong>SQLite</strong>: For reliable local data storage and management.</li>
</ul>
<p><strong>Design Architecture</strong></p>
<ul>
<li><strong>MVVM (Model-View-ViewModel)</strong>: Ensures a clean separation of concerns and an organized codebase.</li>
</ul>
<p><strong>Integrated Development Environment (IDE)</strong></p>
<ul>
<li><strong>Android Studio</strong>: The official IDE for Android development, providing a comprehensive suite of tools for building apps.</li>
</ul>
<p><strong>Design Tools</strong></p>
<ul>
<li><strong>Figma</strong>: Utilized for high-fidelity UI/UX design and prototyping.</li>
</ul>
<p><strong>Libraries</strong></p>
<ul>
<li><strong><a href="https://github.com/airbnb/lottie-android" target="_blank" rel="noopener noreferrer">Lottie</a></strong>: For adding high-quality animations to the app.</li>
<li><strong><a href="https://github.com/InsertKoinIO/koin" target="_new">Koin</a>:</strong> A pragmatic lightweight dependency injection framework.</li>
<li><strong><a href="https://github.com/google/gson" target="_new">Gson</a></strong>: For parsing and serializing JSON data efficiently.</li>
<li><strong><a href="https://github.com/PhilJay/MPAndroidChart" target="_new">MPAndroidChart</a></strong>: For rendering complex charts and graphs.</li>
<li><strong><a href="https://github.com/bumptech/glide" target="_new">Glide</a></strong>: For efficient image loading and caching.</li>
<li><strong><a href="https://github.com/ahmmedrejowan/AndroidBatteryView">AndroidBatteryView</a></strong>: A custom library created specifically for displaying battery information.</li>
</ul>
<p><strong>Version Control</strong></p>
<ul>
<li><strong>GitHub</strong>: Used for source code management, allowing for collaborative development and version tracking.</li>
</ul>

## Releases
- [Verison 1.0.0](https://github.com/ahmmedrejowan/DeviceInfo/releases/tag/1.0.0)
    - Initial release
    - Basic device information
    - Real-time monitoring
    - App management
    - Hardware testing

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please fork this repository and contribute back using [pull requests](https://github.com/ahmmedrejowan/DeviceInfo/pulls).

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated.

Let me know which features you want in the future in `Request Feature` tab.

If this project helps you a little bit, then give a to Star ⭐ the Repo.

## License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2024 ahmmedrejowan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```