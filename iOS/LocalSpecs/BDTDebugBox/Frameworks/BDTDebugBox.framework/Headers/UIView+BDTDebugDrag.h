//
//  UIView+BDTDebugDrag.h
//  ByteDance
//
//  Created by arvitwu on 2022/7/1.
//  Copyright © 2022 ByteDance. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol BDTDebugBaseDragableViewDelegate <NSObject>

@optional
- (void)bdtDebugDragableViewDragging:(UIView *)view;
- (void)bdtDebugDragableViewBeginDrag:(UIView *)view;
- (void)bdtDebugDragableViewEndDrag:(UIView *)view lastX:(CGFloat)lastX lastY:(CGFloat)lastY;

@end

@interface UIView (BDTDebugDrag)

/// delegate
@property (nonatomic, weak, nullable) id<BDTDebugBaseDragableViewDelegate> bdtDebug_dragableDelegate;

/// 拖动区域（top: 最小 Y 值, left: 最小 X 值, bottom: 底部距离父 view 的高度, right: 右边距离父 view 的宽度）
/// 默认值是 UIEdgeInsetsZero，表示不能滑出屏幕外
@property (nonatomic, assign) UIEdgeInsets bdtDebug_customDragEdge;

/// 拖动手势
@property (nonatomic, strong, nullable) UIPanGestureRecognizer *bdtDebug_panGesture;

/// 是否可拖动，默认可拖动
- (void)bdtDebug_enableDragable:(BOOL)enable;

@end

NS_ASSUME_NONNULL_END
