//
//  BDTDebugHelper.h
//  ByteDance
//
//  Created by arvit on 2021/3/4.
//  Copyright © 2021 ByteDance. All rights reserved.
//
#import <Foundation/Foundation.h>
#import "BDTDebugItem.h"
#import "BDTDebugConstants.h"

NS_ASSUME_NONNULL_BEGIN

@protocol BDTDebugConfigProtocol;

/*
#import "BDTDebugBusinessConfiger.h"
 */

// (因为都定义在一个 enum 里，合分支时比较容易有冲突，这里支持使用 NSString 来避免冲突，不过你得保证你自己 hard code 的 string 是唯一的）
// 唯一 ID key（结构体 BDTDebugID），可以选择用 enum 或 string（内部取 ID 会先判断 NSString 不为空则优先取，否则取 enum）
//      示例：使用 enum:     BDTDEBUG_ID(BDTDebugItemTagSwitch_PGSH, nil)
//      示例：使用 string:   BDTDEBUG_ID(0, @"arvitwu_wnsLog")
// 取值可用下面的宏 BDTDEBUG_IS_ENABLE_PERSIST、BDTDEBUG_IS_ENABLE_MEMORY
#define BDTDEBUG_ID(__enum, __string)                ((BDTDebugID){__enum, __string})

// 获取开关值
// 其实最终调的是下面的 BDTDEBUG_GET_VALUE_PERSIST、BDTDEBUG_GET_VALUE_MEMORY 宏，只是返回值转为 BOOL 了
// __enum 和 __string 二选一即可，如：
// BDTDEBUG_IS_ENABLE_PERSIST(BDTDebugItemTagSwitch_ADToast, nil, BDTDebugBusinessConfiger)
// BDTDEBUG_IS_ENABLE_PERSIST(0, @"BDTDebugAD_PrintSDKLog", BDTDebugBusinessConfiger)
#define BDTDEBUG_IS_ENABLE_PERSIST(__enum, __string, __busiConfigerClass)  ([[BDTDebugHelper getValueForType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesPersist busiConfigerClass:__busiConfigerClass.class] boolValue]) // 持久化缓存
#define BDTDEBUG_IS_ENABLE_MEMORY(__enum, __string, __busiConfigerClass)  ([[BDTDebugHelper getValueForType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesMemory busiConfigerClass:__busiConfigerClass.class] boolValue]) // 内存缓存

// 设置开关值
#define BDTDEBUG_MARK_ENABLE_PERSIST(__enum, __string, __boolValue, __busiConfigerClass) ([BDTDebugHelper setValue:@(__boolValue) forType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesPersist busiConfigerClass:__busiConfigerClass.class]) // 持久化缓存
#define BDTDEBUG_MARK_ENABLE_MEMORY(__enum, __string, __boolValue, __busiConfigerClass)  ([BDTDebugHelper setValue:@(__boolValue) forType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesMemory busiConfigerClass:__busiConfigerClass.class]) // 内存缓存

// 获取存储值
#define BDTDEBUG_GET_VALUE_PERSIST(__enum, __string, __busiConfigerClass) ([BDTDebugHelper getValueForType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesPersist busiConfigerClass:__busiConfigerClass.class]) // 持久化缓存
#define BDTDEBUG_GET_VALUE_MEMORY(__enum, __string, __busiConfigerClass)  ([BDTDebugHelper getValueForType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesMemory busiConfigerClass:__busiConfigerClass.class]) // 内存缓存

// 设置存储值
#define BDTDEBUG_SET_VALUE_PERSIST(__enum, __string, __objectValue, __busiConfigerClass) ([BDTDebugHelper setValue:__objectValue forType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesPersist busiConfigerClass:__busiConfigerClass.class]) // 持久化缓存
#define BDTDEBUG_SET_VALUE_MEMORY(__enum, __string, __objectValue, __busiConfigerClass)  ([BDTDebugHelper setValue:__objectValue forType:BDTDEBUG_ID(__enum, __string) saveTypes:BDTDebugItemSaveTypesMemory busiConfigerClass:__busiConfigerClass.class]) // 内存缓存

@interface BDTDebugHelper : NSObject

#pragma mark - switch
/// 获取存储值
/// @param itemTag 存储 key
/// @param saveTypes 存储类型
/// @param prefix 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名
+ (nullable id)getValueForType:(BDTDebugID)itemTag saveTypes:(BDTDebugItemSaveTypes)saveTypes prefix:(NSString *_Nullable)prefix;

/// @param busiConfigerClass 业务配置类，用于存储 key 的前缀
+ (nullable id)getValueForType:(BDTDebugID)itemTag saveTypes:(BDTDebugItemSaveTypes)saveTypes busiConfigerClass:(Class <BDTDebugConfigProtocol> _Nullable)busiConfigerClass;

/// 保存/清除存储值
/// @param value 为 nil 时表示清除存储值，非 nil 则进行存储
/// @param itemTag 存储 key
/// @param saveTypes 存储类型
/// @param prefix 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名 
+ (void)setValue:(nullable id)value forType:(BDTDebugID)itemTag saveTypes:(BDTDebugItemSaveTypes)saveTypes prefix:(NSString *_Nullable)prefix;

/// @param busiConfigerClass 业务配置类，用于存储 key 的前缀
+ (void)setValue:(nullable id)value forType:(BDTDebugID)itemTag saveTypes:(BDTDebugItemSaveTypes)saveTypes busiConfigerClass:(Class <BDTDebugConfigProtocol> _Nullable)busiConfigerClass;

#pragma mark - RecentUsed
/// 存储最近使用的文案
+ (void)saveRecentUsedContentWithItem:(BDTDebugItem *)debugItem;

/// 获取最近使用的所有标题
/// @param prefixAtSaveKey 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名
+ (NSMutableOrderedSet <NSString *> *)getRecentUsedTitleTextArrayWithPrefix:(NSString *_Nullable)prefixAtSaveKey;

/// 获取最近修改项，结果为一批 BDTDebugItem（不划分 section）
/// @param sourceArray 数据源
/// @param maxCount 结果的上限数量（≤ 0 表示不限制）
/// @param prefixAtSaveKey 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名
/// @return 一批 BDTDebugItem（不划分 section）
+ (NSMutableArray <BDTDebugItem *> *)recentUsedItemsWithSourceArray:(NSArray <BDTDebugSectionItem *> *)sourceArray
                                                          maxCount:(NSInteger)maxCount
                                                    prefixAtSaveKey:(NSString * _Nullable)prefixAtSaveKey;

/// 获取最近修改项，结果为一批 BDTDebugSectionItem（划分 section）
/// @param sourceArray 数据源
/// @param maxCount 最大数量
/// @param prefixAtSaveKey 存储 key 的前缀，一般为实现了 BDTDebugConfigProtocol 的业务类名
/// @return 一批 BDTDebugSectionItem（划分 section）
+ (NSMutableArray <BDTDebugSectionItem *> *)recentUsedSectionsWithSourceArray:(NSArray <BDTDebugSectionItem *> *)sourceArray
                                                                     maxCount:(NSInteger)maxCount
                                                              prefixAtSaveKey:(NSString *_Nullable)prefixAtSaveKey;

/// 在一批 BDTDebugItem 里查找，结果为一批 BDTDebugItem（不划分section）
/// @param searchText 搜索文本
/// @param sourceArray 数据源
/// @return 一批 BDTDebugItem（不划分section）
+ (NSMutableArray <BDTDebugItem *> *)searchItemsWithText:(NSString *)searchText
                                                inItems:(NSArray <BDTDebugItem *> *)sourceArray;

/// 在一批 BDTDebugSectionItem 里查找，结果为一批 BDTDebugItem（不划分section）
/// @param searchText 搜索文本
/// @param sourceArray 数据源
/// @return 一批 BDTDebugItem（不划分section）
+ (NSMutableArray <BDTDebugItem *> *)searchItemsWithText:(NSString *)searchText
                                             inSections:(NSArray <BDTDebugSectionItem *> *)sourceArray;

/// 在一批 BDTDebugSectionItem 里查找，结果为一批 BDTDebugSectionItem（划分section）
/// @param searchText 搜索文本
/// @param sourceArray 数据源
/// @return 一批 BDTDebugSectionItem（划分section）
+ (NSMutableArray <BDTDebugSectionItem *> *)searchSectionsWithText:(NSString *)searchText
                                                       inSections:(NSArray <BDTDebugSectionItem *> *)sourceArray;

#pragma mark - Alert#pragma mark - Alert & ActionSheet
/// 展示 alert
/// @param title 标题
/// @param message 内容
/// @param btnTextArray 按钮文案数组
/// @param extraConfigBlock 抛 vc 实例给业务额外定制。该 block 返回值: YES: 自己进行 present，NO: 内部取 topVC 进行 present
/// @param completeBlock 按钮的点击响应处理
+ (UIAlertController *)showAlertWithTitle:(nullable NSString *)title
                                  message:(nullable NSString *)message
                             btnTextArray:(NSArray <NSString *> *)btnTextArray
                         extraConfigBlock:(nullable BOOL(^)(UIAlertController *alertVC))extraConfigBlock
                            completeBlock:(nullable void(^)(NSInteger buttonIndex, NSString *buttonTitle, UIAlertController *alertVC))completeBlock;

/// 展示 ActionSheet
/// @param title 标题
/// @param message 内容
/// @param btnTextArray 按钮文案数组
/// @param extraConfigBlock 抛 vc 实例给业务额外定制。该 block 返回值: YES: 自己进行 present，NO: 内部取 topVC 进行 present
/// @param completeBlock 按钮的点击响应处理
+ (UIAlertController *)showActionSheetWithTitle:(nullable NSString *)title
                                        message:(nullable NSString *)message
                                   btnTextArray:(NSArray <NSString *> *)btnTextArray
                               extraConfigBlock:(nullable BOOL(^)(UIAlertController *alertVC))extraConfigBlock
                                  completeBlock:(nullable void(^)(NSInteger buttonIndex, NSString *buttonTitle, UIAlertController *alertVC))completeBlock;

#pragma mark - string
/// 全角转半角
+ (NSString *)fullToHalfString:(NSString *)targetString;

/// 半角转全角
+ (NSString *)halfToFullString:(NSString *)targetString;

#pragma mark - Utils
/// 查找第一响应者
+ (nullable UIView *)findFirstResponder:(UIView *)view;

/// 询问是否要退出 APP
+ (void)askToExitApp;

/// 退出 APP
+ (void)exitApp;

/// 顶部安全区域
+ (CGFloat)safeAreaTop;

/// 底部安全区域
+ (CGFloat)safeAreaBottom;

@end

NS_ASSUME_NONNULL_END
