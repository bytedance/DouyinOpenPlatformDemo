//
//  BDTDebugVC.h
//  ByteDance
//
//  Created by arvit on 2021/3/4.
//  Copyright © 2021 ByteDance. All rights reserved.
//
#import <UIKit/UIKit.h>
#import "BDTDebugConfigProtocol.h"

@class BDTDebugBaseCell;
@class BDTDebugNavigationBar;

@interface BDTDebugVC : UIViewController

/// 当前页面展示的数据源（如搜索后的结果）
@property (nonatomic, strong, nonnull, readonly) NSMutableArray <BDTDebugSectionItem *> *curDataArray;

/// 全部数据源
@property (nonatomic, strong, nonnull, readonly) NSMutableArray <BDTDebugSectionItem *> *allDataArray;

/// tableview
@property (nonatomic, strong, nonnull, readonly) UITableView *tableView;

/// nav bar
@property (nonatomic, strong, nonnull, readonly) BDTDebugNavigationBar *navBar;

/// 搜索框（可在 viewDidLoad 回调自定义样式）
@property (nonatomic, strong, nonnull, readonly) UISearchBar *searchBar;

/// 初始化方法
/// @param configer 自定义一些配置
- (instancetype _Nonnull)initWithConfiger:(nonnull id <BDTDebugConfigProtocol>)configer NS_DESIGNATED_INITIALIZER;
- (instancetype _Nonnull)init NS_UNAVAILABLE;
- (instancetype _Nonnull)initWithCoder:(NSCoder *_Nonnull)coder NS_UNAVAILABLE;
- (instancetype _Nonnull)initWithNibName:(nullable NSString *)nibNameOrNil bundle:(nullable NSBundle *)nibBundleOrNil NS_UNAVAILABLE;
+ (instancetype _Nonnull)new NS_UNAVAILABLE;

/// 刷新列表
/// PS: 如果操作了一个 item 后仅需刷新 item 对应的 cell，可直接调用 [baseCell updateWithItem:baseCell.debugItem]
/// @param shouldRebuildItems 是否需要重建 items（比如操作了一个 item 更新了数据，需要刷新另一个 item，此时传 YES。传 NO 仅调用 tableView 的 reloadData）
- (void)reloadDataWithRebuildItems:(BOOL)shouldRebuildItems;

/// navigation bar 高度
+ (CGFloat)navHeight;

@end
