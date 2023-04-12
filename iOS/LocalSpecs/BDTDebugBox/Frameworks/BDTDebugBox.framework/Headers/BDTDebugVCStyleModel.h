//
//  BDTDebugVCStyleModel.h
//  BDTDebugContainer
//
//  Created by arvit on 2021/9/8.
//
//  如果需要更改其它样式，可以在 viewDidLoadForBDTDebugVC: 回调进行修改
//  如果需要更改其它样式，可以在 viewDidLoadForBDTDebugVC: 回调进行修改
//  如果需要更改其它样式，可以在 viewDidLoadForBDTDebugVC: 回调进行修改

#import <Foundation/Foundation.h>

@class BDTDebugVC;

@interface BDTDebugVCStyleModel : NSObject

/// navigationBar 标题，默认为空（搜索框隐藏时才看得到）
@property (nonatomic, copy, nullable) NSString *navTitle;

/// 隐藏搜索框，默认不隐藏
@property (nonatomic, assign) BOOL hideSearchBar;

/// 搜索框的主体颜色（文字颜色等），默认 darkGrayColor（如果需要对搜索框更多定制样式，可在 viewDidLoad 回调处理）
@property (nonatomic, strong, nullable) UIColor *searchMainColor;

/// 隐藏右边按钮，默认不隐藏
@property (nonatomic, assign) BOOL hideRightButton;

/// tableview 类型，默认为 UITableViewStylePlain
@property (nonatomic, assign) UITableViewStyle tableViewStyle;

@end
