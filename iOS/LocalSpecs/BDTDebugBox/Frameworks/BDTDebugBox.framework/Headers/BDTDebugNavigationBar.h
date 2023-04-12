//
//  BDTDebugNavigationBar.h
//  BDTDebugBox
//
//  Created by arvitwu on 2022/11/3.
//

#import <UIKit/UIKit.h>

#define BDTDEBUG_TARGET_NAV_HEIGHT          (50) // 真正需要展示的 nav bar 高度（其它的为安全区域）
#define BDTDEBUG_LEFT_RIGHT_BUTTON_WIDTH    (50) // 左右按钮宽度

typedef NS_ENUM(NSInteger, BDTDebugNavBarStyle) {
    BDTDebugNavBarStyleLight    = 0, // 浅色
    BDTDebugNavBarStyleDark     = 1, // 深色
};

@class BDTDebugNavigationBar;
@protocol BDTDebugNavigationBarDelegate <NSObject>

@required
/// 点击左边按钮回调
- (void)bdtDebugNavigationBar:(BDTDebugNavigationBar *_Nonnull)navBar onClickLeftButton:(UIButton *_Nonnull)leftButton;

@optional
/// 点击右边按钮回调
- (void)bdtDebugNavigationBar:(BDTDebugNavigationBar *_Nonnull)navBar onClickRightButton:(UIButton *_Nullable)rightButton;

@end

@interface BDTDebugNavigationBar : UIView

/// 代理
@property (nonatomic, weak, nullable) id <BDTDebugNavigationBarDelegate> delegate;

/// 标题
@property (nonatomic, strong, nullable, readonly) UILabel *titleLabel;

/// 底部线条
@property (nonatomic, strong, nullable, readonly) CALayer *bottomLineLayer;

/// 左边按钮
@property (nonatomic, strong, nullable, readonly) UIButton *leftButton;

/// 右边按钮
@property (nonatomic, strong, nullable, readonly) UIButton *rightButton;

/// 初始化
- (instancetype _Nonnull)initWithStyle:(BDTDebugNavBarStyle)style NS_DESIGNATED_INITIALIZER;
- (instancetype _Nonnull)initWithFrame:(CGRect)frame NS_UNAVAILABLE;
- (instancetype _Nonnull)initWithCoder:(NSCoder *_Nonnull)coder NS_UNAVAILABLE;

/// 实际要展示的起点（上面的多余的是安全区域）
- (CGFloat)startPointY;

/// 高度
+ (CGFloat)navHeight;

@end
