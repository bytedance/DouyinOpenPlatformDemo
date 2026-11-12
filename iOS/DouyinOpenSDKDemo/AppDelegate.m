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

@interface DYOpenDemoSceneDelegate : UIResponder <UIWindowSceneDelegate>

@property (nonatomic, strong) UIWindow *window;

@end

@interface AppDelegate () <DouyinOpenSDKLogDelegate>

@end

@implementation AppDelegate

#pragma mark - Life Cycle
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
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

@implementation DYOpenDemoSceneDelegate

- (void)scene:(UIScene *)scene willConnectToSession:(UISceneSession *)session options:(UISceneConnectionOptions *)connectionOptions
{
    if (![scene isKindOfClass:[UIWindowScene class]]) {
        return;
    }
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    UIViewController *vc = [sb instantiateViewControllerWithIdentifier:@"DYOpenDemoViewController"];
    UINavigationController *navi = [[UINavigationController alloc] initWithRootViewController:vc];
    self.window = [[UIWindow alloc] initWithWindowScene:(UIWindowScene *)scene];
    self.window.rootViewController = navi;
    ((AppDelegate *)UIApplication.sharedApplication.delegate).window = self.window;
    [self.window makeKeyAndVisible];

    NSLog(@"[OpenSDKDemo] Scene connected, URL count: %lu", (unsigned long)connectionOptions.URLContexts.count);
    // 冷启动的回跳 URL 通过 connectionOptions 进入，与热启动共用处理入口。
    [self scene:scene openURLContexts:connectionOptions.URLContexts];
}

- (void)scene:(UIScene *)scene openURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts
{
    AppDelegate *appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
    for (UIOpenURLContext *context in URLContexts) {
        BOOL handled = [appDelegate application:UIApplication.sharedApplication
                                       openURL:context.URL
                             sourceApplication:context.options.sourceApplication
                                    annotation:context.options.annotation];
        NSLog(@"[OpenSDKDemo] Scene URL callback handled: %d", handled);
    }
}

- (void)sceneDidDisconnect:(UIScene *)scene
{
    AppDelegate *appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
    if (appDelegate.window == self.window) {
        appDelegate.window = nil;
    }
}

@end
