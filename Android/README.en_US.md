[中文README](README.md) | English

## Overview

An Demo for DouyinOpenSDK, to demonstrate how to integrate DouyinOpenSdk, this project includes the following functional demonstrations:

* Auth
* Share
* Jump

## Module

* app module
    * Shared implementation class [DouYinShare](app/src/main/java/com/bytedance/sdk/douyin/open/ability/share/DouYinShare.kt)
    * Auth implementation class [DouyinLoginManager](app/src/main/java/com/bytedance/sdk/douyin/open/ability/auth/DouyinLoginManager.kt)
    * Jump implementation class [DouYinCommonAbility](app/src/main/java/com/bytedance/sdk/douyin/open/ability/common/DouYinCommonAbility.kt)
* base module: OpenApi interface, the developer needs to cooperate with the server implementation
    * Host injection implementation [HostConfig](base/src/main/java/com/bytedance/sdk/douyin/open/base/config/HostConfig.kt)

## Precondition

Firstly, you should register a mobile application
on [Douyin Open Platform](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/app-mgmt/create-mobile-and-web-app).

## Step

* implementation interface [HostConfig](base/src/main/java/com/bytedance/sdk/douyin/open/base/config/HostConfig.kt).
* initialize HostConfig in[CustomApplication](app/src/main/java/com/bytedance/sdk/douyin/open/CustomApplication.kt).

##  More

[SDK access document](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/sdk)

## License

This project is licensed under the Apache-2.0 License.





















