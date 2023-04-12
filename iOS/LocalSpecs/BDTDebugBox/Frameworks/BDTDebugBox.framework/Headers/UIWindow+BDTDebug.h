//
//  UIWindow+BDTDebug.h
//  ByteDance
//
//  Created by arvitwu on 2022/7/1.
//  Copyright © 2022 ByteDance. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIWindow (BDTDebug)

/// 获取 key window
+ (nullable UIWindow *)bdtDebug_keyWindow;

/// 获取 topVC
+ (UIViewController *)bdtDebug_topViewController;

@end

NS_ASSUME_NONNULL_END
