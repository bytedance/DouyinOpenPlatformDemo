import { AbilityStage } from '@kit.AbilityKit';
import { appAbilityAgent } from '../init/base/AppAbilityAgent';

export default class DemoAbilityStage extends AbilityStage {
  /**
   * 模块初始化处
   */
  onCreate(): void {
    appAbilityAgent.onAbilityStageCreate(this.context)
  }
}