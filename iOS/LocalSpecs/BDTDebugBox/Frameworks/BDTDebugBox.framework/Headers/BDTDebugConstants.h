//
//  BDTDebugConstants.h
//  ByteDance
//
//  Created by arvit on 2021/5/7.
//  Copyright © 2021 ByteDance. All rights reserved.
//

#ifndef BDT_DEBUG_CONSTANTS_H_
#define BDT_DEBUG_CONSTANTS_H_

#pragma mark - Helper
// -- concat
#define BDTDEBUG_STRING_CONCAT_(A, B) A ## B
#define BDTDEBUG_STRING_CONCAT(A, B) BDTDEBUG_STRING_CONCAT_(A, B)

// weak self
#define BDTDEBUG_WEAK_SELF(VAR) \
__weak __typeof__(VAR) BDTDEBUG_STRING_CONCAT(VAR, _weak_) = (VAR)

// strong self
#define BDTDEBUG_STRONG_SELF(VAR) \
_Pragma("clang diagnostic push") \
_Pragma("clang diagnostic ignored \"-Wshadow\"") \
__strong __typeof__(VAR) VAR = BDTDEBUG_STRING_CONCAT(VAR, _weak_) \
_Pragma("clang diagnostic pop")

// check self and return
#define BDTDEBUG_CHECK_SELF_AND_RETURN() BDTDEBUG_STRONG_SELF(self);            \
if (self == nil) return;                                                    \

// check self and return value
#define BDTDEBUG_CHECK_SELF_AND_RETURN_WITH(value) BDTDEBUG_STRONG_SELF(self);  \
if (self == nil) return value;

#pragma mark - Shorcut
// 空串判断
#define BDTDEBUG_IS_EMPTY_STR(x) (x == nil || [x isEqual:[NSNull null]] || ![x isKindOfClass:[NSString class]] || [x isEqualToString: @""]) // 判断是不是一个nil字符串或者@""
#define BDTDEBUG_SAFE_STR(x) (BDTDEBUG_IS_EMPTY_STR(x) ? @"" : x) // 拿到字符串（当为 nil 或 不为NSString 或为 null 时，获得空串；否则返回正确值）

// 颜色
#define BDTDEBUG_COLOR_WITH_HEX_STRING(__hex__, __alpha__) \
[UIColor colorWithRed:((float)(((__hex__) & 0xFF0000) >> 16))/255.0 \
green:((float)(((__hex__) & 0xFF00) >> 8))/255.0 \
blue:((float)((__hex__) & 0xFF))/255.0 \
alpha:(__alpha__)]

#endif
