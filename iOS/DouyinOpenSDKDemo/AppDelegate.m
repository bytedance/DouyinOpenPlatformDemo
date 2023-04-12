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

#import "AppDelegate.h"
#import <DouyinOpenSDK/DouyinOpenSDKApplicationDelegate.h>
#import "DYOpenDemoConstants.h"
#import <BDTDebugBox/BDTDebugHelper.h>
#import "DYOpenDemoHostConfig.h"

@interface AppDelegate () <DouyinOpenSDKLogDelegate>

@end

@implementation AppDelegate

#pragma mark - Life Cycle
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    UIViewController *vc = [sb instantiateViewControllerWithIdentifier:@"DYOpenDemoViewController"];
    UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:vc];
    
    self.window = [[UIWindow alloc] init];
    self.window.rootViewController = navi;
    [self.window makeKeyAndVisible];
    
    // register DouyinOpenSDK
//    [self registerDouyinOpenSDKWithApplication:application launchOptions:launchOptions];
    
    return YES;
}

- (BOOL)application:(UIApplication *)application openURL:(nonnull NSURL *)url options:(nonnull NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options
{    
    if ([[DouyinOpenSDKApplicationDelegate sharedInstance] application:application openURL:url sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey] annotation:options[UIApplicationOpenURLOptionsAnnotationKey]]
        ) {
        return YES;
    }
    return NO;
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    if ([[DouyinOpenSDKApplicationDelegate sharedInstance] application:application openURL:url sourceApplication:sourceApplication annotation:annotation]) {
        return YES;
    }
    return NO;
}

#pragma mark - DouyinOpenSDK
- (void)registerDouyinOpenSDKWithApplication:(UIApplication *)application launchOptions:(NSDictionary *)launchOptions
{
    // 一、注册 clientKey，有两种方式
    // 1.1：SDK 内自动使用 info.plist 里的 DouyinAppID 字段
    NSString *clientKey = [[NSBundle mainBundle] infoDictionary][@"DouyinAppID"];
    if (clientKey.length > 0) {
        [[DouyinOpenSDKApplicationDelegate sharedInstance] application:application didFinishLaunchingWithOptions:launchOptions];
    }
    else {
        // 1.2：业务手动传入（此方法支持在后续其它地方调用，后调用的会覆盖前面的数据）
        clientKey = [DYOpenDemoHostConfig clientKey];
        [[DouyinOpenSDKApplicationDelegate sharedInstance] registerAppId:clientKey];
    }
    if (clientKey.length <= 0) {
        [BDTDebugHelper showAlertWithTitle:@"ClientKey 为空" message:@"1. 修改 +[DYOpenDemoHostConfig clientKey] 返回值\n\n2. 在 Xcode 中，点击 TARGETS - info，在下面的 URL Types 填入 ClientKey" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
    }
    
    // 二、注册日志回调
    [DouyinOpenSDKApplicationDelegate sharedInstance].logDelegate = self;
}

#pragma mark - DouyinOpenSDKLogDelegate
- (void)onLog:(NSString *)logInfo
{
    NSLog(@"%@", logInfo);
}

@end
