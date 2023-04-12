//
//  BDTDebugConfigProtocol.h
//  BDTDebugContainer
//
//  Created by arvit on 2021/9/8.
//

#import <Foundation/Foundation.h>
#import "BDTDebugHelper.h"

@class BDTDebugVC;
@class BDTDebugSectionItem;
@class BDTDebugItem;
@class BDTDebugVCStyleModel;

@protocol BDTDebugConfigProtocol <NSObject>

@required
/// 构造 Debug 列表的数据源
/// @param bdtDebugVC debug vc 实例
/// @return Debug 页面列表数据源
- (nullable NSArray <BDTDebugSectionItem *> *)buildDataArrayForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC;

@optional
/// 存储数据所用 key 的前缀（主要是为了解决不同 business configer 存储数据不相互覆盖的问题。内部默认使用 business configer 类名作为前缀。）
/// @return 返回 nil 则内部默认使用 business configer 的类名。返回空字符串则使用空字符串为前缀。
- (nullable NSString *)prefixAtSaveKeyForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC;

/// BDTDebugVC viewDidLoad 执行完后
- (void)viewDidLoadForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC;

/// 获取自定义样式信息
- (BDTDebugVCStyleModel *_Nullable)getStyleModelForBDTDebugVC:(nonnull BDTDebugVC *)bdtDebugVC;

/// 点击右上角按钮的自定义响应事件（内部有默认事件，不赋值也会有响应）
- (void)bdtDebugVC:(nonnull BDTDebugVC *)bdtDebugVC customRightActionForButton:(nonnull UIButton *)button;

@end
