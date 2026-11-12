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

## Requirements

- This Demo raises its minimum OS version from iOS 9.0 to **iOS 15.0**. iOS 9–14 cannot install the updated Demo; existing installations of older versions are unaffected. This requirement applies to the Demo, not to every DouyinOpenSDK release.
- Install dependencies with CocoaPods. Duo validation used Xcode 27.1 and its iOS 27.1 simulator runtime.
- The Demo uses a single-window UIScene lifecycle, including window creation and URL handling on ordinary iPhones.

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

     

## Duo simulator

This change moves window creation and cold/warm URL forwarding to a single-window UIScene lifecycle. SDK initialization keeps the original flow: tap the initialization item on the capabilities page before using authorization or sharing.

BDTDebugBox is now version 1.1.1, with foldable-layout fixes and an XCFramework containing device arm64 and simulator arm64/x86_64 slices. A plain `pod install` downloads the public package and verifies its SHA-256; no local SDK or internal repository access is required.

**A new DouyinOpenSDK release is still required before external developers can run Duo directly:**

1. Publish a new DouyinOpenSDK release with arm64 iOS Simulator support. Package separate device and simulator slices in an XCFramework and retain resource bundles. A device arm64 slice is not interchangeable with a simulator slice.
2. Select the published SDK version in the Demo and remove simulator arm64 exclusions from the Podfile and SDK.
3. Run a plain `pod install` from a clean checkout, then verify building, launching, authorization and sharing on Duo.

Until DouyinOpenSDK is updated, the Podfile retains the arm64 exclusion required by the existing SDK. The Scene changes alone do not make Duo runnable. Once the dependencies are released, external developers should continue using the standard installation steps above.

### Validation scope

- Earlier checks with temporary validation dependencies covered Duo launch, expanded/closed/partially folded/rotated layouts, and portrait/landscape layouts on an ordinary iPhone simulator. These results do not validate the repository's current default dependencies.
- Earlier checks covered web authorization cancellation, image-picker cancellation, and cold/warm Scene forwarding of test URLs. Test URL forwarding does not establish successful authorization or share callbacks.
- Runtime regression after restoring manual initialization and removing the simulator-specific navigation setting remains pending, as do released-dependency installation, actual app-to-app authorization/share round trips, physical-device and older-OS regression. Token exchange still requires implementing the backend placeholders in `DYOpenDemoHostConfig`.

##  More

[SDK access document](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

This project is licensed under the [Apache-2.0 License](LICENSE).