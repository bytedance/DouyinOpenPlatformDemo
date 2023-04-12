//
//  BDTDebugBaseCell.h
//  ByteDance
//
//  Created by arvit on 2021/5/7.
//  Copyright © 2021 ByteDance. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BDTDebugItem.h"

NS_ASSUME_NONNULL_BEGIN

@interface BDTDebugBaseCell : UITableViewCell

/// indexPath
@property (nonatomic, strong, nonnull) NSIndexPath *indexPath;

/// 数据源
@property (nonatomic, strong, nonnull) BDTDebugItem *debugItem;

/// 长按手势
@property (nonatomic, strong, nullable) UILongPressGestureRecognizer *longPressGesture;

#pragma mark - Overrides
/// 计算 cell 高度，默认 BDTDEBUG_CELL_HEIGHT
+ (CGFloat)calHeightWithData:(BDTDebugItem *)debugItem;

/// 设置 UI，默认实现为空
- (void)setupCustomUI;

/// 填充、刷新数据，基类有公共操作
- (void)updateWithItem:(BDTDebugItem *)debugItem NS_REQUIRES_SUPER;

/// 控制 switch 开关（如果有 UISwitch 的话）
/// @param enable YES: 开, NO: 关
/// @param prefix 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名 
- (void)enableSwitchIfPossibly:(BOOL)enable prefix:(NSString *)prefix;

/// 当前 Switch 开关状态（如果有 UISwitch 的话）
/// @return YES: 开   NO: 关
- (BOOL)isSwitchOn;

#pragma mark - Public Methods
/// 更新样式
/// @param titleLable 主 label
/// @param descLabel 副 label
/// @param styleModel 样式 model
- (void)updateTitleLabel:(UILabel *_Nullable)titleLable descLabel:(UILabel *_Nullable)descLabel styleModel:(BDTDebugCellStyleModel *_Nullable)styleModel;

@end

NS_ASSUME_NONNULL_END
