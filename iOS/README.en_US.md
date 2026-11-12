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

The default dependencies do not yet provide the native arm64 simulator packages used for this validation. **A plain `pod install` is not sufficient to run on Duo**: the default path retains the existing packages and the simulator arm64 exclusion.

1. Prepare local CocoaPods packages for DouyinOpenSDK and BDTDebugBox. Each directory must contain its podspec, XCFramework and resource bundles. The XCFramework must include an `arm64` **iOS Simulator** slice; a device arm64 slice is not interchangeable.
2. Set both package paths when installing from the `iOS` directory:

   ```bash
   DOUYIN_OPEN_SDK_PATH=/absolute/path/to/DouyinOpenSDK \
   BDT_DEBUG_BOX_PATH=/absolute/path/to/BDTDebugBox \
   pod install
   ```

   The Podfile stops adding the simulator arm64 exclusion only when both variables are set. The local podspecs must not exclude that architecture either. Setting the BDTDebugBox path also skips the old framework download script. These variables only override local dependencies; no private Pod sources or internal build tools are required.
3. Open `DouyinOpenSDKDemo.xcworkspace`, select a Duo simulator, and build and run. Configure your ClientKey, URL Scheme and Bundle ID as described above. Simulator execution does not require a device signing certificate.

AppDelegate initializes the SDK. SceneDelegate creates the window and forwards both cold-start `connectionOptions.URLContexts` and warm-start `openURLContexts` to the SDK. On simulators, the public `focusUseSchemaJump` property enables Scheme-based navigation for client packages without Universal Link association. This property is not set on devices.

Local validation used repackaged DouyinOpenSDK 4.2.5 and BDTDebugBox 1.1.0. The BDTDebugBox package also includes layout changes based on container bounds and safe areas. These dependency artifacts and source changes are not included in this repository and have not been published as new releases. Supporting Duo through the default installation path still requires releasing compatible dependencies and updating the default dependency configuration.

### Validation scope

- With those local dependencies, verified Duo launch, expanded/closed/partially folded/rotated layouts, and portrait/landscape layouts on an ordinary iPhone simulator.
- Verified web authorization cancellation, image-picker cancellation, and cold/warm Scene forwarding of test URLs. Test URL forwarding does not establish successful authorization or share callbacks.
- Actual Demo → Douyin → Demo authorization/share round trips, physical-device regression and older-OS regression remain unverified. App-to-app testing also requires a compatible Douyin simulator package. Token exchange and related operations require implementing the backend placeholders in `DYOpenDemoHostConfig`.

##  More

[SDK access document](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

This project is licensed under the [Apache-2.0 License](LICENSE).