中文README | [English](README.en_US.md)

## 概述

抖音开放平台在 Github 提供开源示例项目，用于演示如何集成 DouyinOpenSDK，该项目包含以下功能演示：

* 授权
* 分享
* 跳转

## 模块介绍

* APP 模块：授权、分享、通用能力的接入
    * 授权实现类 [DYOpenDemoInfoConfiger - authItem](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoInfoConfiger.m)
    * 分享实现类 [DYOpenDemoShareConfiger](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoShareConfiger.m)
    * 跳转实现类 [DYOpenDemoInfoConfiger - jumpToDYProfileItem](DouyinOpenSDKDemo/BusiConfiger/DYOpenDemoInfoConfiger.m)
* Base 模块 OpenAPI 接口，需要开发者配合服务端实现
    * 宿主注入实现 [DYOpenDemoHostConfig](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfig.m)

## 系统要求

- 本 Demo 最低系统版本由 iOS 9.0 提升至 **iOS 15.0**，iOS 9–14 无法安装新版 Demo；已安装的旧版本不受影响。此要求针对 Demo，不代表 DouyinOpenSDK 所有版本的最低系统要求。
- 使用 CocoaPods 安装依赖。Duo 验证环境为 Xcode 27.1 和配套 iOS 27.1 模拟器运行时。
- Demo 使用单窗口 UIScene 生命周期，普通 iPhone 也通过 Scene 创建窗口并处理 URL 回调。

## 前提条件

首先注册一个**移动应用**，注册地址：[申请移动应用](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/app-mgmt/create-mobile-and-web-app)

## 运行 Demo

> Demo 已创建了类: [DYOpenDemoHostConfig](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfig.m)，该类实现了 [DYOpenDemoHostConfigProtocol](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfigProtocol.h)，在里面按提示修改即可（最主要的是配置 **ClientKey**）

1. 使用 `git clone + 仓库地址`，下载仓库到本地

2. 安装pod

   - 进入刚刚下载的 Demo 工程，进入 iOS 目录

   - 执行 pod install，成功后会提示 Generating project & Integrating project

3. 编译&运行demo

   - 打开 iOS 目录下的 DouyinOpenSDKDemo.xcworkspace

   - 如果有苹果通配证书或企业签名，直接 run demo 即可体验最基本的授权、分享功能。  
   - 如果没有则需要进行以下修改：  
     * 修改 clientKey 为自己申请到的：  
       1. 修改 `+[DYOpenDemoHostConfig clientKey]` 返回值
       2. 在 Xcode 中，点击 `TARGETS - info`，在下面的 `URL Types` 填入 clientKey
     * 修改 bundle id：需要与 clientKey 匹配（即与官网平台申请填写的一致） 
     * 修改证书：使用自己可用的证书

## Duo 模拟器

当前默认依赖尚未提供本次验证所用的原生 arm64 模拟器包。**仅执行普通 `pod install` 还不能运行 Duo**：默认路径仍保留旧包及模拟器 arm64 排除配置。

1. 准备 DouyinOpenSDK 和 BDTDebugBox 的本地 CocoaPods 包，每个目录须包含对应 podspec、XCFramework 和资源 bundle。XCFramework 必须包含真正的 `arm64` **iOS Simulator** 切片，真机 arm64 切片不能代替。
2. 在 `iOS` 目录同时指定两个包的路径：

   ```bash
   DOUYIN_OPEN_SDK_PATH=/absolute/path/to/DouyinOpenSDK \
   BDT_DEBUG_BOX_PATH=/absolute/path/to/BDTDebugBox \
   pod install
   ```

   只有两个变量都设置时，Podfile 才不再添加模拟器 arm64 排除；本地 podspec 也不能排除该架构。指定 BDTDebugBox 路径时，跳过旧 framework 的下载脚本。这两个变量仅用于本地依赖覆盖，不需要私有 Pod 源或内部构建工具。
3. 打开 `DouyinOpenSDKDemo.xcworkspace`，选择 Duo 模拟器编译运行。按上文配置自己的 ClientKey、URL Scheme 和 Bundle ID；模拟器运行不需要真机签名证书。

Demo 在 AppDelegate 中初始化 SDK，在 SceneDelegate 中创建窗口，并将冷启动的 `connectionOptions.URLContexts` 和热启动的 `openURLContexts` 转交 SDK。模拟器下通过公开属性 `focusUseSchemaJump` 使用 Scheme 跳转，便于联调未关联 Universal Link 的客户端包；真机不设置该属性。

本地验证使用 DouyinOpenSDK 4.2.5 和 BDTDebugBox 1.1.0 的重新打包产物；BDTDebugBox 另含按容器尺寸及安全区更新布局的改动。这些依赖产物和依赖源码改动不包含在本仓库中，也未作为新版本发布。普通安装方式支持 Duo 仍需正式发布对应依赖，并更新默认依赖配置。

### 验证范围

- 使用上述本地依赖，已验证 Duo 启动、展开/收起/半折叠/旋转布局，以及普通 iPhone 模拟器横竖屏。
- 已验证网页授权取消、图片选择页取消，以及测试 URL 的冷/热启动 Scene 转发。测试 URL 转发不等于真实授权或分享回跳成功。
- Demo → 抖音 → Demo 的真实授权/分享回跳、真机及旧系统回归尚未完成。应用间联调还需要兼容的抖音模拟器包；token 换取等接口需要实现 `DYOpenDemoHostConfig` 中的服务端占位接口。

# 更多文档

[官网接入文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

本工程使用 [Apache-2.0 协议](LICENSE)
