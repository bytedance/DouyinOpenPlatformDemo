[中文README](README.md) | English

## Overview

A demo for DouyinOpenSDK, to demonstrate how to integrate `DouyinOpenSDK`, this project includes the following functional demonstrations:

* Auth
* Share
* Jump

## Module

* APP module
    * Auth implementation class [DYOpenDemoInfoConfiger - authItem](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoInfoConfiger.m)
    * Shared implementation class [DYOpenDemoShareConfiger](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoShareConfiger.m)
    * Jump implementation class [DYOpenDemoInfoConfiger - jumpToDYProfileItem](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoInfoConfiger.m)
* Base module: OpenAPI interface, the developer needs to cooperate with the server implementation
    * Host injection implementation [DYOpenDemoHostConfig](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfig.m)  

## Precondition

Firstly, you should register **a mobile application** on [Douyin Open Platform](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/app-mgmt/create-mobile-and-web-app).

## Run the Demo

> The demo has created a class: [DYOpenDemoHostConfig](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfig.m) which implementation interface [DYOpenDemoHostConfigProtocol](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfigProtocol.h), just modify as prompted inside (The most important thing is to configure the **ClientKey**)

1. Use `git clone + repo url`，download the repository locally

2. Install pod

   - Go to the downloaded Demo project and go to the iOS directory
   - run `pod install`，After success, you will see `Generating project & Integrating project`

3. Compile & Run Demo

   - Open the iOS directory DouyinOpenSDKDemo.xcworkspace

   - If you have an apple enterprise certificate, just run the demo directly to use the basic auth or share ability.

     If not you should change something like this:  

     * modify the clientKey applied for by yourself:  
       1. Modify the return value of `+[DYOpenDemoHostConfig clientKey]`
       2. In Xcode, click `TARGETS - info` and fill in the clientKey below `URL Types`
     * modify the bundle id: The bundle ID must match the clientKey (that is, the bundle ID must be the same as that in the application on the official website)
     * modify certificates: Use your own available certificates 

     

##  More

[SDK access document](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

This project is licensed under the [Apache-2.0 License](LICENSE).