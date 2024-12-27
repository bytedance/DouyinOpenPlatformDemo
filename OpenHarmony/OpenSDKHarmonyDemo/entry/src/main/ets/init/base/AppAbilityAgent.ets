import { IAppAbilityAgent } from './IAppAbilityAgent'
import { Configuration } from '@ohos.app.ability.Configuration'
import window from '@ohos.window'
import { DouYinOpenSDKInit } from '../../opensdk/DouYinOpenSDKInit'
import AbilityConstant from '@ohos.app.ability.AbilityConstant'
import Want from '@ohos.app.ability.Want'
import { douYinOpenSDK } from '@douyin/opensdk-common-external'
import { common } from '@kit.AbilityKit'

class AppAbilityAgent implements IAppAbilityAgent {
  tag = "AppAbilityAgent"

  onAbilityStageCreate(context: Context): void {
    //使用前初始化
    DouYinOpenSDKInit.init(context)
  }

  onUIAbilityCreate(context: common.UIAbilityContext, want: Want, launchParam: AbilityConstant.LaunchParam): void {
    douYinOpenSDK.doResult(context, want)
  }

  onUIAbilityNewWant(context: common.UIAbilityContext, want: Want, launchParam: AbilityConstant.LaunchParam): void {
    douYinOpenSDK.doResult(context, want)
  }

  onWindowStageCreate(context: Context): void {

  }

  onWindowStageEventChange(eventType: window.WindowStageEventType): void {

  }

  onConfigurationInitOrUpdate(newConfig: Configuration, isInit?: boolean | undefined,
    context?: Context | undefined): void {

  }

  onForeground(context: Context): void {

  }

  onBackground(context: Context): void {

  }

  onWindowStageDestroy(context: Context): void {

  }

  onUIAbilityDestroy(context: Context): void {

  }
}

export const appAbilityAgent: IAppAbilityAgent = new AppAbilityAgent()