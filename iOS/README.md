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

## 前提条件

首先注册一个**移动应用**，注册地址：[申请移动应用](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/app-mgmt/create-mobile-and-web-app)

## 运行 Demo

> Demo 已创建了类: [DYOpenDemoHostConfig](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfig.m)，该类实现了 [DYOpenDemoHostConfigProtocol](DouyinOpenSDKDemo/HostConfig/DYOpenDemoHostConfigProtocol.h)，在里面按提示修改即可（最主要的是配置 **ClientKey**）

1. 使用 `git clone + 仓库地址`，下载仓库到本地

2. 安装pod

   - 进入刚刚下载的 Demo 工程，进入 iOS 目录

   - 执行git lfs install 与 git lfs pull操作
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

# 更多文档

[官网接入文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/access/ios)

## License

本工程使用 [Apache-2.0 协议](LICENSE)
