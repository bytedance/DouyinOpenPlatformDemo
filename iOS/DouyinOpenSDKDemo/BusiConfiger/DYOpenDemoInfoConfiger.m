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

#import "DYOpenDemoInfoConfiger.h"
#import <BDTDebugBox/BDTDebugVCStyleModel.h>
#import <BDTDebugBox/BDTDebugHelper.h>
#import <BDTDebugBox/BDTDebugBaseCell.h>
#import <DouyinOpenSDK/DouyinOpenSDKApplicationDelegate.h>
#import <DouyinOpenSDK/DouyinOpenSDKAuth.h>
#import <DouyinOpenSDK/DouyinOpenSDKShare.h>
#import "DYOpenDemoHostConfig.h"
#import "DYOpenDemoShareConfiger.h"
#import "AppDelegate.h"

@interface DYOpenDemoInfoConfiger ()

@property (nonatomic, weak, nullable) BDTDebugVC *debugVC;

@end

@implementation DYOpenDemoInfoConfiger

#pragma mark - Life Cycle
- (void)dealloc
{
    NSLog(@"DYOpenDemoInfoConfiger dealloc");
}

#pragma mark - BDTDebugConfigProtocol
- (BDTDebugVCStyleModel *)getStyleModelForBDTDebugVC:(BDTDebugVC *)bdtDebugVC
{
    BDTDebugVCStyleModel *model = [BDTDebugVCStyleModel new];
    model.navTitle = @"抖音开放平台";
    model.hideSearchBar = YES;
    model.hideRightButton = YES;
    return model;
}

- (nullable NSArray<BDTDebugSectionItem *> *)buildDataArrayForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC
{
    self.debugVC = bdtDebugVC;
    NSArray *array = @[
        [BDTDebugSectionItem itemWithTitle:@"🐳 当前信息" debugItems:@[
            [self openSDKVersionItem],
            [self bundleIDItem],
            [self installStateItem],
            [self registerSDKItem],
        ] isOpen:YES],
        [BDTDebugSectionItem itemWithTitle:@"🐯 开放能力" debugItems:@[
            [self authItem],
            [self shareItem],
            [self jumpToDYProfileItem],
        ] isOpen:YES],
    ];
    return array;
}

#pragma mark - Items
- (BDTDebugItem *)openSDKVersionItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextOnly];
    item.titleText = @"DouyinOpenSDK Version";
    item.descText = [[DouyinOpenSDKApplicationDelegate sharedInstance] currentVersion];
    return item;
}

- (BDTDebugItem *)bundleIDItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextOnly];
    item.titleText = @"BundleID";
    item.descText = [NSBundle mainBundle].bundleIdentifier;
    return item;
}

- (BDTDebugItem *)installStateItem
{
    BOOL hasInstall = [[DouyinOpenSDKApplicationDelegate sharedInstance] isAppInstalled];
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextOnly];
    item.titleText = @"是否已安装抖音";
    item.descText = hasInstall ? @"已安装" : @"未安装抖音，或未配置 LSApplicationQueriesSchemes";
    if (!hasInstall) {
        item.styleModel.descColor = [UIColor redColor];
    }
    return item;
}

- (BDTDebugItem *)registerSDKItem
{
    NSString *clientKey = [[DouyinOpenSDKApplicationDelegate sharedInstance] appId];
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"初始化 OpenSDK";
    item.descText = [NSString stringWithFormat:@"已初始化，Client Key: %@", clientKey];
    item.styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    if (clientKey.length <= 0) {
        item.descText = @"请先点击进行初始化";
        item.styleModel.descColor = [UIColor redColor];
    }
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
        [appDelegate registerDouyinOpenSDKWithApplication:nil launchOptions:nil];
        baseCell.debugItem.descText = [NSString stringWithFormat:@"已初始化，Client Key: %@", [[DouyinOpenSDKApplicationDelegate sharedInstance] appId]];
        baseCell.debugItem.styleModel = [BDTDebugCellStyleModel new];
        baseCell.debugItem.styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
        [baseCell updateWithItem:baseCell.debugItem];
    };
    return item;
}

- (BDTDebugItem *)authItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"去授权";
    item.styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        [self onClickAuth];
    };
    return item;
}

- (BDTDebugItem *)shareItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"去分享";
    item.styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        if ([[DouyinOpenSDKApplicationDelegate sharedInstance] appId].length <= 0) {
            [BDTDebugHelper showAlertWithTitle:@"ClientKey 为空" message:@"请先初始化 OpenSDK" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
            return;
        }
        BDTDebugVC *debugVC = [[BDTDebugVC alloc] initWithConfiger:[DYOpenDemoShareConfiger new]];
        [self.debugVC.navigationController pushViewController:debugVC animated:YES];
    };
    return item;
}

- (BDTDebugItem *)jumpToDYProfileItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"跳转个人主页";
    item.styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        if ([[DouyinOpenSDKApplicationDelegate sharedInstance] appId].length <= 0) {
            [BDTDebugHelper showAlertWithTitle:@"ClientKey 为空" message:@"请先初始化 OpenSDK" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
            return;
        }
        NSString *hostOpenID = @""; // 当前抖音登录用户的 OpenID
        NSString *targetOpenID = @""; // 目标用户的 OpenID
        if (hostOpenID.length <= 0 || targetOpenID.length <= 0) {
            [BDTDebugHelper showAlertWithTitle:@"OpenID 为空" message:@"请先自行实现 DYOpenDemoHostConfig 的 getAccessToken 接口得到" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
            return;
        }
        DouyinOpenSDKShareRequest *request = [[DouyinOpenSDKShareRequest alloc] init];
        request.shareAction = DouyinOpenSDKShareTypeJump;
        request.landedPageType = DouyinOpenSDKLandedPageProfile;
        request.openId = hostOpenID;
        request.targetOpenId = targetOpenID;
        [request sendShareRequestWithCompleteBlock:^(DouyinOpenSDKShareResponse * _Nonnull Response) {
            NSLog(@"finish jump, errCode: %ld, errMsg: %@", Response.errCode, Response.errString ?: @"");
        }];
    };
    return item;
}

#pragma mark - Private Methods
- (void)onClickAuth
{
    if ([[DouyinOpenSDKApplicationDelegate sharedInstance] appId].length <= 0) {
        [BDTDebugHelper showAlertWithTitle:@"ClientKey 为空" message:@"请先初始化 OpenSDK" btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
        return;
    }
    DouyinOpenSDKAuthRequest *request = [[DouyinOpenSDKAuthRequest alloc] init];
    request.permissions = [NSOrderedSet orderedSetWithObject:@"user_info"];
    // 可选附加权限（如有），用户可选择勾选/不勾选
//    request.additionalPermissions = [NSOrderedSet orderedSetWithObjects:
//                                     @{ @"permission": @"friend_relation", @"defaultChecked": @"1" },
//                                     @{ @"permission": @"message", @"defaultChecked": @"0" },
//                                     nil];
    [request sendAuthRequestViewController:self.debugVC completeBlock:^(DouyinOpenSDKAuthResponse * _Nonnull resp) {
        NSString *alertString = nil;
        if (resp.errCode == 0) {
            // 宿主需自行实现此接口
            [DYOpenDemoHostConfig getAccessTokenWithClientKey:[DYOpenDemoHostConfig clientKey] authCode:resp.code complete:nil];
            alertString = [NSString stringWithFormat:@"Auth success code: %@, permission: %@", resp.code, resp.grantedPermissions];
        }
        else {
            alertString = [NSString stringWithFormat:@"Auth failed code: %@, msg: %@", @(resp.errCode), resp.errString];
        }
        [BDTDebugHelper showAlertWithTitle:nil message:alertString btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
    }];
}

@end
