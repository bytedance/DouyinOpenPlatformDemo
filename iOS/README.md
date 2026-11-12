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

本次改动将窗口创建和冷、热启动 URL 转发迁移到单窗口 UIScene。SDK 初始化仍保留原流程：进入功能页后点击“初始化 OpenSDK”，再使用授权、分享能力。

**当前尚未完成对外可直接运行 Duo 的依赖交付。** 发布前必须完成以下事项，外部开发者无需准备本地 SDK 或配置内部工具：

1. 正式发布支持 arm64 iOS Simulator 的 DouyinOpenSDK 新版本，使用 XCFramework 分别包含真机和模拟器切片，并保留资源 bundle。真机 arm64 切片不能代替模拟器切片。
2. 提供兼容的 BDTDebugBox 正式二进制，包含模拟器切片及折叠布局修复，更新本仓库的下载脚本和本地 podspec。
3. 在 Demo 中指定已发布的 SDK 版本，移除 Demo 和两个依赖 podspec 中的模拟器 arm64 排除配置。
4. 从干净 checkout 执行普通 `pod install`，在 Duo 上完成编译、启动及授权/分享验证。

上述依赖未更新前，当前 Podfile 仍保留旧包所需的 arm64 排除配置，不能仅凭本次 Scene 改动宣称 Duo 已可运行。依赖发布后，对外继续使用上文的正常安装步骤。

### 验证范围

- 此前使用临时验证依赖，检查过 Duo 启动、展开/收起/半折叠/旋转布局，以及普通 iPhone 模拟器横竖屏；该结果不代表仓库当前默认依赖已通过。
- 此前检查过网页授权取消、图片选择页取消，以及测试 URL 的冷/热启动 Scene 转发。测试 URL 转发不等于真实授权或分享回跳成功。
- 恢复手动初始化、移除模拟器专用跳转配置后的运行回归，以及正式依赖安装、真实应用间授权/分享回跳、真机和旧系统回归尚未完成。token 换取等接口仍需要实现 `DYOpenDemoHostConfig` 中的服务端占位接口。

# 更多文档

[官网接入文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

本工程使用 [Apache-2.0 协议](LICENSE)
