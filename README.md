# NTP-Clock

## Introduction
The NTP Clock Android app is designed to ensure that your device always displays the most accurate time when the network connection is available. It uses the Network Time Protocol (NTP) to synchronize the device's time with a reliable NTP server, such as `1.se.pool.ntp.org` in this project. When it is not available, it falls back to the system time on the device.

## Features
- Displays the current time on the user interface.
- Automatically updates the time every second.
- Retrieves the time from an NTP server if a network connection is available.
- Falls back to the device's system time when the network is unavailable.
- Provides user-friendly labels and color indicators for distinguishing between network time and system time. (GREEN/RED)
