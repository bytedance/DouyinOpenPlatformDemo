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

#import "DYOpenDemoShareConfiger.h"
#import <BDTDebugBox/BDTDebugVCStyleModel.h>
#import <BDTDebugBox/UIWindow+BDTDebug.h>
#import <BDTDebugBox/BDTDebugHelper.h>
#import <BDTDebugBox/BDTDebugBaseCell.h>
#import <DouyinOpenSDK/DouyinOpenSDKApplicationDelegate.h>
#import <DouyinOpenSDK/DouyinOpenSDKShare.h>
#import "DYOpenDemoHostConfig.h"
#import "DYOpenDemoConstants.h"
#import <QBImagePickerController/QBImagePickerController.h>

@interface DYOpenDemoShareConfiger () <QBImagePickerControllerDelegate>

@property (nonatomic, weak, nullable) BDTDebugVC *debugVC;

@property (nonatomic, copy, nullable) NSString *hashTagStr;
@property (nonatomic, copy, nullable) NSString *microAppIDStr;
@property (nonatomic, copy, nullable) NSString *microAppTitleStr;
@property (nonatomic, copy, nullable) NSString *microAppDescStr;
@property (nonatomic, copy, nullable) NSString *microAppLinkStr;
@property (nonatomic, copy, nullable) NSString *styleIDStr;
@property (nonatomic, copy, nullable) NSString *shareIDStr;

@property (nonatomic, strong, nullable) DouyinOpenSDKShareRequest *shareRequest;

@end

@implementation DYOpenDemoShareConfiger

#pragma mark - Life Cycle
- (void)dealloc
{
    NSLog(@"DYOpenDemoShareConfiger dealloc");
}

#pragma mark - BDTDebugConfigProtocol
- (BDTDebugVCStyleModel *)getStyleModelForBDTDebugVC:(BDTDebugVC *)bdtDebugVC
{
    BDTDebugVCStyleModel *model = [BDTDebugVCStyleModel new];
    model.navTitle = @"抖音分享";
    model.hideSearchBar = YES;
    model.hideRightButton = YES;
    return model;
}

- (nullable NSArray<BDTDebugSectionItem *> *)buildDataArrayForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC
{
    self.debugVC = bdtDebugVC;
    NSArray *array = @[
        [BDTDebugSectionItem itemWithTitle:@"🦁 Meta Info" debugItems:@[
            [self hashTagItem],
            [self microAppIDItem],
            [self microAppTitleItem],
            [self microAppDescItem],
            [self microAppLinkItem],
            [self styleIDItem],
            [self shareIDItem],
        ] isOpen:NO],
        [BDTDebugSectionItem itemWithTitle:@"🦊 分享内容" debugItems:@[
            [self shareImageItem],
            [self shareVideoItem],
        ] isOpen:YES],
    ];
    return array;
}

#pragma mark - Items
- (BDTDebugItem *)hashTagItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"HashTag";
    item.textFieldPlaceholder = @"HashTag need open in web";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        self.hashTagStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)microAppIDItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"小程序 appID";
    item.buttonText = @"默认";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        baseCell.debugItem.descText = kDYOpenDemoDefaultMicroAppId;
        [baseCell updateWithItem:baseCell.debugItem];
        self.microAppIDStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)microAppTitleItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"小程序标题";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        self.microAppTitleStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)microAppDescItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"小程序描述";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        self.microAppDescStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)microAppLinkItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"小程序链接";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        self.microAppLinkStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)styleIDItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"styleID";
    item.buttonText = @"默认";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        baseCell.debugItem.descText = kDYOpenDemoDefaultStyleID;
        [baseCell updateWithItem:baseCell.debugItem];
        self.styleIDStr = baseCell.debugItem.descText;
    };
    return item;
}

- (BDTDebugItem *)shareIDItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextField];
    item.titleText = @"shareID";
    item.buttonText = @"请求";
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        [DYOpenDemoHostConfig getShareIdWithClientKey:[DYOpenDemoHostConfig clientKey] styleID:self.styleIDStr complete:^(NSDictionary * _Nullable rspDict, NSError * _Nullable error) {
            NSString *shareIDStr = rspDict[@"data"][@"share_id"];
            if (shareIDStr.length > 0) {
                baseCell.debugItem.descText = shareIDStr;
                [baseCell updateWithItem:baseCell.debugItem];
                self.shareIDStr = shareIDStr;
            }
            else {
                NSLog(@"[DYOpenDemo] request ShareID failed");
            }
        }];
    };
    return item;
}

- (BDTDebugItem *)shareImageItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"分享图片";
    BDTDebugCellStyleModel *styleModel = [BDTDebugCellStyleModel new];
    styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    item.styleModel = styleModel;
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        [self showImageSelectVCWithMediaType:QBImagePickerMediaTypeImage];
    };
    return item;
}

- (BDTDebugItem *)shareVideoItem
{
    BDTDebugItem *item = [BDTDebugItem itemWithUIType:BDTDebugItemUITypeTextAndArrow];
    item.titleText = @"分享视频";
    BDTDebugCellStyleModel *styleModel = [BDTDebugCellStyleModel new];
    styleModel.titleFont = [UIFont boldSystemFontOfSize:15];
    item.styleModel = styleModel;
    item.execBlock = ^(BDTDebugBaseCell * _Nonnull baseCell) {
        [self showImageSelectVCWithMediaType:QBImagePickerMediaTypeVideo];
    };
    return item;
}

#pragma mark - QBImagePickerControllerDelegate
- (void)qb_imagePickerController:(QBImagePickerController *)imagePickerController didFinishPickingAssets:(NSArray *)assets {
    NSMutableArray *ids = [NSMutableArray array];
    for (PHAsset *asset in assets) {
        if (![asset isKindOfClass:[PHAsset class]]) {
            NSAssert(NO, @"[DYOpenDemo] not PHAsset");
            continue;
        }
        [ids addObject:asset.localIdentifier];
    }
    
    QBImagePickerMediaType mediaType = imagePickerController.mediaType;
    [imagePickerController dismissViewControllerAnimated:YES completion:^{
        if (mediaType == QBImagePickerMediaTypeImage) {
            [self shareMediaIds:ids type:DouyinOpenSDKShareMediaTypeImage];
        }
        else if (mediaType == QBImagePickerMediaTypeVideo) {
            [self shareMediaIds:ids type:DouyinOpenSDKShareMediaTypeVideo];
        }
    }];
}

- (void)qb_imagePickerControllerDidCancel:(QBImagePickerController *)imagePickerController
{
    [imagePickerController dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Private Methods
- (void)showImageSelectVCWithMediaType:(QBImagePickerMediaType)mediaType
{
    QBImagePickerController *imagePickerVC = [[QBImagePickerController alloc] init];
    imagePickerVC.delegate = self;
    imagePickerVC.allowsMultipleSelection = YES;
    imagePickerVC.maximumNumberOfSelection = 12;
    imagePickerVC.showsNumberOfSelectedAssets = YES;
    imagePickerVC.mediaType = mediaType;
    [self.debugVC presentViewController:imagePickerVC animated:YES completion:nil];
}

- (void)shareMediaIds:(NSArray *)media type:(DouyinOpenSDKShareMediaType)type
{
    // 构造小程序信息
    NSMutableDictionary *microAppInfoDict = [NSMutableDictionary dictionary];
    microAppInfoDict[@"identifier"] = self.microAppIDStr;
    microAppInfoDict[@"title"] = self.microAppTitleStr;
    microAppInfoDict[@"desc"] = self.microAppDescStr;
    microAppInfoDict[@"startPageURL"] = self.microAppLinkStr;
    
    // 构造和视频版定的额外信息
    NSMutableDictionary *productExtraInfo = [NSMutableDictionary dictionary];
    productExtraInfo[@"styleID"] = self.styleIDStr;
    
    // 构造 shareReq 信息
    DouyinOpenSDKShareRequest *shareReq = [[DouyinOpenSDKShareRequest alloc] init];
    shareReq.localIdentifiers = media;
    shareReq.mediaType = type;
    shareReq.state = self.shareIDStr;
    NSMutableDictionary *extraInfo = [NSMutableDictionary dictionary];
    if (microAppInfoDict.count > 0) {
        extraInfo[@"mpInfo"] = microAppInfoDict;
    }
    if (productExtraInfo.count > 0) {
        extraInfo[@"product_extra_info"] = productExtraInfo;
    }
    if (extraInfo.count > 0) {
        shareReq.extraInfo = extraInfo;
    }
    
    // 开始发送请求
    self.shareRequest = shareReq;
    [self.shareRequest sendShareRequestWithCompleteBlock:^(DouyinOpenSDKShareResponse * _Nonnull Response) {
        NSString *alertString = nil;
        if (Response.errCode == 0) {
            alertString = [NSString stringWithFormat:@"Share succeed"];
        }
        else {
            alertString = [NSString stringWithFormat:@"Share failed error code : %@ , error msg : %@", @(Response.errCode), Response.errString];
        }
        [BDTDebugHelper showAlertWithTitle:nil message:alertString btnTextArray:@[@"OK"] extraConfigBlock:nil completeBlock:nil];
    }];
}

@end
