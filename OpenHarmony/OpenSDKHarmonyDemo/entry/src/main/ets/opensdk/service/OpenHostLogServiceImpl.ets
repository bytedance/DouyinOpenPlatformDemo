import { IHostLogService } from '@douyin/opensdk-common-external';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { OpenSDK } from '../OpenConfig';


export class OpenHostLogServiceImpl implements IHostLogService {
  d(tag: string, message: string): void {
    hilog.debug(OpenSDK, tag, message)
  }

  i(tag: string, message: string): void {
    hilog.info(OpenSDK, tag, message)
  }

  w(tag: string, message: string): void {
    hilog.warn(OpenSDK, tag, message)
  }

  e(tag: string, message: string): void {
    hilog.error(OpenSDK, tag, message)
  }
}