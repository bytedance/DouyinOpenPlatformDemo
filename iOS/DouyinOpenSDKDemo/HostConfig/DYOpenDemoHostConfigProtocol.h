// Copyright 2023 ByteDance and/or its affiliates.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#import <Foundation/Foundation.h>

@protocol DYOpenDemoHostConfigProtocol <NSObject>

@required
/// 宿主填入自己的 ClientKey
+ (NSString *_Nonnull)clientKey;

@optional
/// 使用授权返回的 code 获取 accessToken、openID 等信息
/// 接口文档：https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/account-permission/get-access-token
/// 获取后可进行后续其它 OpenAPI 调用：https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/list
+ (void)getAccessTokenWithClientKey:(NSString *_Nonnull)clientKey
                           authCode:(NSString *_Nonnull)authCode
                           complete:(void(^_Nullable)(NSDictionary *_Nullable rspDict, NSError *_Nullable error))complete;

/// 分享功能：获取 shareID
/// 接口文档：https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/video-management/douyin/search-video/video-share-result
+ (void)getShareIdWithClientKey:(NSString *_Nonnull)clientKey
                        styleID:(NSString *_Nullable)styleID
                       complete:(void (^_Nullable)(NSDictionary * _Nullable rspDict, NSError * _Nullable error))complete;


/// 获取 ClientCode
+ (void)getClientCodeWithComplete:(void(^_Nonnull)(NSString *_Nullable clientCode, NSError *_Nullable error))complete;

@end
