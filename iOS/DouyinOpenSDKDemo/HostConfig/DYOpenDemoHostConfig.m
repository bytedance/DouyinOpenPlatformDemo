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

#import "DYOpenDemoHostConfig.h"
#import <BDTDebugBox/BDTDebugHelper.h>

static inline void DYOPEN_DEMO_RUN_ON_MAIN_THREAD_ASYNC(dispatch_block_t _Nonnull block) {
    if ([NSThread isMainThread]) {
        !block ?: block();
    } else {
        dispatch_async(dispatch_get_main_queue(), block);
    }
}

@implementation DYOpenDemoHostConfig

#pragma mark - DYOpenDemoHostConfigProtocol
+ (NSString *)clientKey
{
    // 两种方式任选一种即可
    NSString *clientKey = [[NSBundle mainBundle] infoDictionary][@"DouyinAppID"]; // 方式 1: 配在 info.plist
    if (clientKey.length <= 0) {
        clientKey = @"aw5hkvchiclb1oe2"; // 方式 2: 内置在代码里
    }
    return clientKey;
}

+ (void)getAccessTokenWithClientKey:(NSString *)clientKey authCode:(NSString *)authCode complete:(void (^)(NSDictionary * _Nullable, NSError * _Nullable))complete
{
    if (clientKey.length <= 0 || authCode.length <= 0) {
        !complete ?: complete(nil, [self errorWithCode:-1 errMsg:@"Invalid params" extraUserInfo:nil]);
        return;
    }
    /// 接口文档：https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/account-permission/get-access-token
    /// 此接口需要 APP_SECRET（需要保密，不可内置在客户端上），需要宿主服务端封装接口给客户端调用
    NSString *baseURLString = @"__HOST_SERVER_PROVIDE__";
    NSURL *url = [NSURL URLWithString:baseURLString];
    NSURLSessionDataTask *task = [[NSURLSession sharedSession] dataTaskWithURL:url completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        [self handleResultData:data netError:error complete:complete];
    }];
    [task resume];
}

+ (void)getShareIdWithClientKey:(NSString *)clientKey styleID:(NSString *)styleID complete:(void (^)(NSDictionary * _Nullable, NSError * _Nullable))complete
{
    if (clientKey.length <= 0) {
        !complete ?: complete(nil, [self errorWithCode:-1 errMsg:@"Invalid params" extraUserInfo:nil]);
        return;
    }
    /// 接口文档：https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/video-management/douyin/search-video/video-share-result/
    /// 此接口需要 APP_SECRET（需要保密，不可内置在客户端上），需要宿主服务端封装接口给客户端调用
    NSString *baseURLString = @"__HOST_SERVER_PROVIDE__";
    if (styleID.length > 0) {
        NSString *param = [NSString stringWithFormat:@"&source_style_id=%@", styleID];
        baseURLString = [baseURLString stringByAppendingString:param];
    }
    NSURL *url = [NSURL URLWithString:baseURLString];
    NSURLSessionDataTask *task = [[NSURLSession sharedSession] dataTaskWithURL:url completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        [self handleResultData:data netError:error complete:complete];
    }];
    [task resume];
}

+ (void)getClientCodeWithComplete:(void (^)(NSString * _Nullable, NSError * _Nullable))complete
{
    [BDTDebugHelper showAlertWithTitle:@"需自行获取 clientCode" message:@"获取接口应该由自己后台封装，防止 clientSecret 泄露" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
}

#pragma mark - Private Methods
+ (NSError *_Nonnull)errorWithCode:(NSInteger)errCode
                            errMsg:(NSString *_Nullable)errMsg
                     extraUserInfo:(NSDictionary <NSErrorUserInfoKey, id> *_Nullable)extraUserInfo
{
    NSMutableDictionary<NSErrorUserInfoKey, id> *dict = [NSMutableDictionary dictionary];
    dict[NSLocalizedDescriptionKey] = errMsg;
    [dict addEntriesFromDictionary:extraUserInfo];
    return [NSError errorWithDomain:@"developer.open-douyin.com" code:errCode userInfo:dict];
}

+ (void)handleResultData:(NSData *_Nullable)resultData
                netError:(NSError *_Nullable)netError
                complete:(void (^)(NSDictionary * _Nullable rspDict, NSError * _Nullable error))complete
{
    if (!resultData || netError) {
        DYOPEN_DEMO_RUN_ON_MAIN_THREAD_ASYNC(^{
            !complete ?: complete(nil, [self errorWithCode:-2 errMsg:@"NetWorkError" extraUserInfo:nil]);
        });
        return;
    }
    NSError *decodeError;
    NSDictionary *rspDict = [NSJSONSerialization JSONObjectWithData:resultData options:NSJSONReadingMutableContainers error:&decodeError];
    DYOPEN_DEMO_RUN_ON_MAIN_THREAD_ASYNC(^{
        !complete ?: complete(rspDict, (!decodeError ? nil : [self errorWithCode:-3 errMsg:@"Json decode error" extraUserInfo:@{ @"jsonError": decodeError }]));
    });
}

@end
