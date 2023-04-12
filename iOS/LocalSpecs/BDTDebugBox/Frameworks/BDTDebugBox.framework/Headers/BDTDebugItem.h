//
//  BDTDebugItem.h
//  ByteDance
//
//  Created by arvit on 2021/3/4.
//  Copyright © 2021 ByteDance. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@class BDTDebugBaseCell;
@class BDTDebugPair;

/// (因为都定义在一个 enum 里，合分支时比较容易有冲突，这里支持使用 NSString 来避免冲突，不过你得保证你自己 hard code 的 string 是唯一的）
/// 注意：需要存储数据时才需要设置 ID。
/// 唯一 ID key（结构体 BDTDebugID），可以选择用 enum 或 string（内部取 ID 会先判断 NSString 不为空则优先取，否则取 enum）
///      示例：使用 enum:     BDTDEBUG_ID(BDTDebugItemTagSwitch_PGSH, nil)
///      示例：使用 string:   BDTDEBUG_ID(0, @"arvitwu_wnsLog")
typedef struct {
    NSInteger idEnum;   // 整型唯一 ID（一般使用 Enum 归类所有整型，防止重复）
    NSString *idString; // 字符串唯一 ID（hard code 的话需自己确保唯一，可加上你自己的 rtx 前缀，如 arvitwu_wnsLog）
} BDTDebugID;

/// UI 类型
typedef NS_ENUM(NSUInteger, BDTDebugItemUIType) {
    BDTDebugItemUITypeTextUnknown           = 0, // 未知样式
    BDTDebugItemUITypeTextAndArrow          = 1, // 默认样式，左边文字，右边一个箭头（设置 descText 会显示在第二行）
    BDTDebugItemUITypeTextAndSwitch         = 2, // 左边文字，右边一个开关（设置 descText 会显示在第二行）
    BDTDebugItemUITypeTextField             = 3, // 左边文字描述，中间输入框，右边一个按钮（左边文字描述可为空）
    BDTDebugItemUITypeSingleSelect          = 4, // 打开 action sheet，选择某个选项
    BDTDebugItemUITypeTextOnly              = 5, // 仅左边文字（设置 descText 才会显示在第二行）
};

/// 数据保存类型，默认持久化
typedef NS_OPTIONS(NSInteger, BDTDebugItemSaveTypes) {
    BDTDebugItemSaveTypesPersist         = 1 << 0, // 持久化，默认值
    BDTDebugItemSaveTypesMemory          = 1 << 1, // 保存在内存
};

@interface BDTDebugCellStyleModel : NSObject

/// 主文本字体，[可选参数]，默认为系统字体 16.0
@property (nonatomic, strong, nullable) UIFont *titleFont;

/// 主文本颜色，[可选参数]，默认为 blackColor
@property (nonatomic, strong, nullable) UIColor *titleColor;

/// 副文本字体，[可选参数]，默认为系统字体 13.0
@property (nonatomic, strong, nullable) UIFont *descFont;

/// 副文本颜色，[可选参数]，默认为 1B1B1B
@property (nonatomic, strong, nullable) UIColor *descColor;

/// BDTDebugItemUITypeTextAndArrow 右边的样式，默认为 UITableViewCellAccessoryDisclosureIndicator
@property (nonatomic, assign) UITableViewCellAccessoryType accessoryType;

/// textField 边框颜色，[可选参数]，默认为 F5F5F5。borderWidth 默认为 1
@property (nonatomic, strong, nullable) UIColor *textFieldBorderColor;

/// textField 右边确认按钮边框颜色，[可选参数]，默认为 lightGrayColor
@property (nonatomic, strong, nullable) UIColor *confirmButtonBorderColor;

/// textField 右边确认按钮文字颜色，[可选参数]，默认为 darkGrayColor
@property (nonatomic, strong, nullable) UIColor *confirmButtonTextColor;

@end

// ---------------- BDTDebugItem
@interface BDTDebugItem : NSObject

#pragma mark - 必须参数
/// [必须参数] UI 类型
@property (nonatomic, assign) BDTDebugItemUIType uiType;

/// 主文本，[必须参数]
@property (nonatomic, strong, nonnull) NSString *titleText;

#pragma mark - 可选参数
/// 副文本，[可选参数]。第二行文字，或 textField 的内容等
/// 当为 textField 类型时，外部要 get/set textField.text 都用这个
@property (nonatomic, strong, nullable) NSString *descText;

/// 类型相关的，如字体、颜色等，[可选参数]，默认使用系统内置的
@property (nonatomic, strong, nullable) BDTDebugCellStyleModel *styleModel;

/// 按钮文本，[可选参数]，默认为空。为空时不显示按钮，此时的事件回调内部会改为在键盘消失时
@property (nonatomic, strong, nullable) NSString *buttonText;

/// TextField placeholder 文字，[可选参数]，默认为空
@property (nonatomic, strong, nullable) NSString *textFieldPlaceholder;

/// 注意：需要存储数据时才需要设置。
/// 唯一 ID struct，get itemID 时会在 struct BDTDebugID 里二选一（enum 或 NSString）
/// uiType 为 BDTDebugItemUITypeTextAndSwitch 时为[必须参数]，其它类型为[可选参数]（即如果需要 BDTDebug 组件内部记录信息则为[必须参数]）
/// 如对应业务枚举类型如 BDTDebugItemTag
@property (nonatomic, assign) BDTDebugID itemTag;

/// 数据保存类型，默认持久化，[可选参数]
@property (nonatomic, assign) BDTDebugItemSaveTypes saveTypes;

/// 主要操作，[可选参数]
/// BDTDebugItemUITypeTextAndArrow: 单击 cell
/// BDTDebugTextAndSwitchCell: 操作开关
/// BDTDebugTextFieldCell: 点击确认按钮
/// BDTDebugItemUITypeSingleSelect: 选中 actionSheet 一个选项后
@property (nonatomic, copy, nullable) void(^execBlock)(BDTDebugBaseCell *baseCell);

/// 次要操作，[可选参数]
/// 当一个 cell 需要有多个不同操作时才用到（如 BDTDebugItemUITypeTextAndArrow 上单击 cell 和操作开关要做不同的响应）
/// BDTDebugItemUITypeTextAndArrow: 单击 cell
@property (nonatomic, copy, nullable) void(^secondaryBlock)(BDTDebugBaseCell *baseCell);

/// 长按处理，[可选参数]
@property (nonatomic, copy, nullable) void(^longPressBlock)(BDTDebugBaseCell *baseCell);

/// cell 内部赋值完后，抛到业务让外部可自定义一些数据赋值等，[可选参数]
@property (nonatomic, copy, nullable) void (^customFillDataBlock)(BDTDebugBaseCell *baseCell);

/// 数据源 [可选参数]
/// 主要是给 BDTDebugItemUITypeSingleSelect 类型用，存储其打开后的 actionSheet 的数据源
@property (nonatomic, strong, nullable) NSArray <BDTDebugPair *> *dataSourceArray;

/// 隐藏 “全部不选中” 选项（仅 uiType 为 BDTDebugItemUITypeSingleSelect 时），默认显示此 pair [可选参数]
@property (nonatomic, assign) BOOL hideSelectNonePair;

/// 额外信息，[可选参数]
/// BDTDebug 组件内部用到的 key 名，统一都定义在 BDTDebugConstants.h 里
@property (nonatomic, strong, nullable) NSMutableDictionary *infoDict;

- (instancetype)init NS_UNAVAILABLE;
/// 初始化，必须参数才加到这方法里来
+ (BDTDebugItem *_Nonnull)itemWithUIType:(BDTDebugItemUIType)uiType;

/// 获取对应的 cell 类名
- (Class)getCellClassName;

/// 获取选中的数据源
- (nullable BDTDebugPair *)getSelectedPair;

/// 标记某个 pair 为选中状态
/// @param choosedKey 待选中的 pair.key，为 nil 表示全部不选中
- (void)selectPairWithKey:(nullable NSString *)choosedKey;

/// 获取 title / desc 等所有字符串组合成的字符（用于搜索）
- (NSString *_Nullable)getCombineText;

/// 是否有设置了 ID
- (BOOL)isIDExisted;

/// 通过 itemTag 获取 id，结果为 NSNumber 或 NSString，如果没有设置则返回 nil
+ (NSObject *_Nullable)itemIDFromItemTag:(BDTDebugID)itemTag;

@end

// ---------------- BDTDebugSectionItem
@interface BDTDebugSectionItem : NSObject

/// 是否展开
@property (nonatomic, assign) BOOL isOpen;

/// section 标题
@property (nonatomic, strong, nonnull) NSString *sectionTitle;

/// section 下的 debug 项
@property (nonatomic, strong) NSArray <BDTDebugItem *> *debugItems;

- (instancetype)init NS_UNAVAILABLE;
+ (BDTDebugSectionItem *)itemWithTitle:(NSString *)title
                          debugItems:(NSArray <BDTDebugItem *> *)debugItems;

+ (BDTDebugSectionItem *)itemWithTitle:(NSString *)title
                          debugItems:(NSArray <BDTDebugItem *> *)debugItems
                               isOpen:(BOOL)isOpen;
@end

// ---------------- BDTDebugPair
@interface BDTDebugPair : NSObject

/// key，[必须参数]
@property (nonatomic, strong, nonnull) NSString *key;

/// value，[必须参数]
@property (nonatomic, strong, nonnull) id value;

/// 额外信息，[可选参数]
@property (nonatomic, strong, nullable) NSMutableDictionary *infoDict;

/// 是否选中，默认 NO
@property (nonatomic, assign) BOOL isSelected;

- (instancetype)init NS_UNAVAILABLE;
+ (BDTDebugPair *)pairWithKey:(nonnull NSString *)key
                       value:(nonnull id)value;

@end

NS_ASSUME_NONNULL_END
