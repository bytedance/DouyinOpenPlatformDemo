import { Context } from '@ohos.abilityAccessCtrl';
import { window } from '@kit.ArkUI';
import { Configuration } from '@ohos.app.ability.Configuration';
import { AbilityConstant, common, Want } from '@kit.AbilityKit';

/**
 * @zhangtao.21
 * @date 2023/11/16
 */
export interface IAppAbilityAgent {
  onAbilityStageCreate(context: Context): void

  onUIAbilityCreate(context: common.UIAbilityContext, want: Want, launchParam: AbilityConstant.LaunchParam): void

  onUIAbilityNewWant(context: common.UIAbilityContext, want: Want, launchParam: AbilityConstant.LaunchParam): void

  onWindowStageCreate(context: Context): void

  onWindowStageEventChange(eventType: window.WindowStageEventType): void

  onConfigurationInitOrUpdate(newConfig: Configuration, isInit?: boolean, context?: Context): void

  onForeground(context: Context): void

  onBackground(context: Context): void

  onWindowStageDestroy(context: Context): void

  onUIAbilityDestroy(context: Context): void
}