# NTP-Clock

This Android application provides a simple NTP (Network Time Protocol) clock that displays the current time based on network availability. It retrieves the time from an NTP server when a network connection is available and falls back to the device's system time when there is no network connectivity.

## Introduction
The NTP Clock Android app is designed to ensure that your device always displays the most accurate time, even when the network connection is available or unavailable. It uses the Network Time Protocol (NTP) to synchronize the device's time with a reliable NTP server, such as `1.se.pool.ntp.org` in this project.

## Features
- Displays the current time on the user interface.
- Automatically updates the time every second.
- Retrieves the time from an NTP server if a network connection is available.
- Falls back to the device's system time when the network is unavailable.
- Provides user-friendly labels and color indicators for distinguishing between network time and system time. (GREEN/RED)
