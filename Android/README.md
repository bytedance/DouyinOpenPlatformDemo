中文README | [English](README.en_US.md)

## 概述

抖音开放平台在 Github 提供开源示例项目，用于演示如何集成 DouyinOpenSdk，该项目包含以下功能演示：

* 授权
* 分享
* 跳转

## 模块介绍

* app模块 授权、分享、通用能力的接入
    * 分享实现类 [DouYinShare](app/src/main/java/com/bytedance/sdk/douyin/open/ability/share/DouYinShare.kt)
    * 授权实现类 [DouyinLoginManager](app/src/main/java/com/bytedance/sdk/douyin/open/ability/auth/DouyinLoginManager.kt)
    * 跳转实现类 [DouYinCommonAbility](app/src/main/java/com/bytedance/sdk/douyin/open/ability/common/DouYinCommonAbility.kt)
* base模块 OpenApi接口，需要开发者配合服务端实现
    * 宿主注入实现[HostConfig](base/src/main/java/com/bytedance/sdk/douyin/open/base/config/HostConfig.kt)

## 前提条件

首先注册一个**移动应用**，注册地址：[申请移动应用](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/app-mgmt/create-mobile-and-web-app)

## 操作步骤

* 实现[HostConfig](base/src/main/java/com/bytedance/sdk/douyin/open/base/config/HostConfig.kt) 接口
* 在 [CustomApplication](app/src/main/java/com/bytedance/sdk/douyin/open/CustomApplication.kt) 中注入上面的实现类

# 更多文档

[官网接入文档](https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/sdk/mobile-app/sdk)

## License

本工程使用 [Apache-2.0 协议](LICENSE-DouyinOpenSDKDemo)  



























