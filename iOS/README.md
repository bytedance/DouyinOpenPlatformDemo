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

- iOS 15.0 及以上。需要支持 iOS 9–14 的项目请继续使用 SDK 4.2.5。
- CocoaPods；运行 Duo 需安装支持 Duo 的 Xcode 和模拟器运行时，本次使用 Xcode 27.1 / iOS 27.1。

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

Demo 使用 DouyinOpenSDK 4.2.6（授权、分享，不含名片）和 BDTDebugBox 1.1.1，支持 arm64 模拟器。依赖均从公开地址下载，BDTDebugBox 仍由 `install.sh` 自动下载。

1. 在 `iOS` 目录执行 `pod install --repo-update`。
2. 打开 `DouyinOpenSDKDemo.xcworkspace`，选择 Duo 模拟器运行。
3. 进入功能页，点击“初始化 OpenSDK”。

Demo 已接入单窗口 UIScene，原有手动初始化、授权和分享逻辑保持不变。真实应用间授权、分享需要在同一模拟器安装抖音模拟器包，并配置匹配的 clientKey、Bundle ID 和 URL Scheme；服务端接口按 `DYOpenDemoHostConfig` 中的提示实现。

已验证 Duo 启动、手动初始化、折叠/旋转及网页授权取消返回。真实应用间回跳、真机和旧系统尚未验证。

# 更多文档

[官网接入文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

本工程使用 [Apache-2.0 协议](LICENSE)
