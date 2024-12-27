import { DouYinOpenConfigBuilder, douYinOpenSDK } from '@douyin/opensdk-common-external';
import { HostEventServiceImpl } from './service/HostEventServiceImpl';
import { OpenHostLogServiceImpl } from './service/OpenHostLogServiceImpl';


export class DouYinOpenSDKInit {
  static init(context: Context) {
    let clientKey = "aw5hkvchiclb1oe2"
    let apiParam = new DouYinOpenConfigBuilder(clientKey)
      .setDebug(true)
      .setHostEventService(new HostEventServiceImpl())
      .setHostLogService(new OpenHostLogServiceImpl())
      .build();
    douYinOpenSDK.init(apiParam)
  }
}