import {
  DouYinOpenListener,
  douYinOpenSDK,
  HashtagTitleMarker,
  ImageObject,
  LandingPage,
  MixObject,
  MusicParamObject,
  OpenAuthRequest,
  OpenShareRequest,
  OpenShareResponse,
  OpenTitleObject,
  SupportApiName,
  VideoObject
} from '@douyin/opensdk-common-external';
import { DebugItem, IDebugItem, SimpleItemComponent } from './component/SimpleItemComponent';
import { common } from '@kit.AbilityKit';
import { DEMO_APP_LINK, OpenDemo } from '../opensdk/OpenConfig';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { photoAccessHelper } from '@kit.MediaLibraryKit';
import { fileIo, fileUri } from '@kit.CoreFileKit';

@Entry
@Component
struct Index {
  private context = getContext(this) as common.UIAbilityContext;
  debugList: IDebugItem[] = [
    {
      title: "授权",
      onClick: () => {
        let listener: DouYinOpenListener = {
          onSuccess: () => {

          },
          onError: () => {

          }
        }
        let authRequest: OpenAuthRequest = new OpenAuthRequest();
        authRequest.scope = "user_info"
        authRequest.state = "AuthUseCase";
        authRequest.callerLocalEntry = "EntryAbility"
        authRequest.redirectUri = DEMO_APP_LINK
        douYinOpenSDK.createDouYinOpenApi()?.authorize(this.context, authRequest, listener)
      }
    },
    {
      title: "投稿-拉起拍摄页",
      onClick: () => {
        let listener: DouYinOpenListener = {
          onSuccess: (response: OpenShareResponse) => {
            hilog.info(OpenDemo, "share", `success.`)
          },
          onError: (response: OpenShareResponse, errorCode: number, errMsg?: string) => {
            hilog.info(OpenDemo, "share", `error. errCode=${errorCode} errMsg=${errMsg}`)
          }
        }
        const request = this.buildShareRequest()
        request.landingPage = LandingPage.Record
        let openAPi = douYinOpenSDK.createDouYinOpenApi()
        if (openAPi?.isSupportApi(SupportApiName.SUPPORT_SHARE) !== true) {
          //当前抖音版本不支持
          return
        }
        openAPi?.share(this.context, request, listener)
      }
    },
    {
      title: "投稿-拉起编辑页-图片",
      onClick: () => {
        this.selectMedia("image").then((finalUri) => {
          let listener: DouYinOpenListener = {
            onSuccess: (response: OpenShareResponse) => {
              hilog.info(OpenDemo, "share", `success.`)
            },
            onError: (response: OpenShareResponse, errorCode: number, errMsg?: string) => {
              hilog.info(OpenDemo, "share", `error. errCode=${errorCode} errMsg=${errMsg}`)
            }
          }
          const request = this.buildShareRequest()
          let imageObject = new ImageObject()
          imageObject.imageUri = finalUri.pop()
          request.mediaObject = imageObject
          request.landingPage = LandingPage.Edit
          let openAPi = douYinOpenSDK.createDouYinOpenApi()
          if (openAPi?.isSupportApi(SupportApiName.SUPPORT_SHARE) !== true) {
            //当前抖音版本不支持
            return
          }
          openAPi?.share(this.context, request, listener)
        })
      }
    },
    {
      title: "投稿-拉起编辑页-视频",
      onClick: () => {
        this.selectMedia("video").then((finalUri) => {
          let listener: DouYinOpenListener = {
            onSuccess: (response: OpenShareResponse) => {
              hilog.info(OpenDemo, "share", `success.`)
            },
            onError: (response: OpenShareResponse, errorCode: number, errMsg?: string) => {
              hilog.info(OpenDemo, "share", `error. errCode=${errorCode} errMsg=${errMsg}`)
            }
          }
          const request = this.buildShareRequest()
          let videoObject = new VideoObject()
          videoObject.videoUri = finalUri.pop()
          request.mediaObject = videoObject
          request.landingPage = LandingPage.Edit
          let openAPi = douYinOpenSDK.createDouYinOpenApi()
          if (openAPi?.isSupportApi(SupportApiName.SUPPORT_SHARE) !== true) {
            //当前抖音版本不支持
            return
          }
          openAPi?.share(this.context, request, listener)
        })
      }
    },
    {
      title: "投稿-拉起编辑页-混合",
      onClick: () => {
        this.selectMedia("all", 50).then((mediaList) => {
          let listener: DouYinOpenListener = {
            onSuccess: (response: OpenShareResponse) => {
              hilog.info(OpenDemo, "share", `success.`)
            },
            onError: (response: OpenShareResponse, errorCode: number, errMsg?: string) => {
              hilog.info(OpenDemo, "share", `error. errCode=${errorCode} errMsg=${errMsg}`)
            }
          }
          const request = this.buildShareRequest()
          let mixObject = new MixObject()
          mixObject.uriList.push(...mediaList)
          request.mediaObject = mixObject
          request.landingPage = LandingPage.Edit
          let openAPi = douYinOpenSDK.createDouYinOpenApi()
          if (openAPi?.isSupportApi(SupportApiName.SUPPORT_MIX) !== true) {
            //当前抖音版本不支持
            return
          }
          openAPi?.share(this.context, request, listener)
        })
      }
    },
  ]

  build() {
    Column() {
      Text("抖音开放平台测试demo")
        .fontSize(19)
        .fontWeight(FontWeight.Bolder)
      Row() {
        List({ space: 8 }) {
          ForEach(this.debugList, (item: IDebugItem) => {
            ListItem() {
              SimpleItemComponent({ model: new DebugItem(item) }).padding({ left: 16, right: 16 })
            }
          })
        }
      }
      .width('100%')
    }.width("100%")
    .align(Alignment.TopStart)
    .justifyContent(FlexAlign.Center)
  }

  private selectMedia(mediaType: string = "image", maxSelectNumber: number = 1): Promise<Array<string>> {
    const photoSelectOptions = new photoAccessHelper.PhotoSelectOptions();
    if (mediaType === "video") {
      photoSelectOptions.MIMEType = photoAccessHelper.PhotoViewMIMETypes.VIDEO_TYPE; // 过滤选择媒体文件类型为IMAGE
    } else if (mediaType === "image") {
      photoSelectOptions.MIMEType = photoAccessHelper.PhotoViewMIMETypes.IMAGE_TYPE; // 过滤选择媒体文件类型为IMAGE
    } else if (mediaType === "all") {
      photoSelectOptions.MIMEType = photoAccessHelper.PhotoViewMIMETypes.IMAGE_VIDEO_TYPE; // 过滤选择媒体文件类型为图片与视频
    }
    photoSelectOptions.maxSelectNumber = maxSelectNumber; // 选择媒体文件的最大数目
    let uris: Array<string> = [];
    const photoViewPicker = new photoAccessHelper.PhotoViewPicker();
    return photoViewPicker.select(photoSelectOptions).then((photoSelectResult: photoAccessHelper.PhotoSelectResult) => {
      uris = photoSelectResult.photoUris;
      let result = Array<string>()
      for (const uri of uris) {
        let file = fileIo.openSync(uri, fileIo.OpenMode.READ_ONLY);
        let tempDir = this.context.getApplicationContext().tempDir;
        let fileName = file.name;
        let tmpFile = tempDir + '/' + fileName;
        fileIo.copyFileSync(file.fd, tmpFile)
        let finalUri = fileUri.getUriFromPath(tmpFile); // 获取到文件的uri
        result.push(finalUri)
      }
      return result
    })
  }

  private buildShareRequest(): OpenShareRequest {
    let request = new OpenShareRequest()
    request.state = "xxx" //传入 OpenAPI 中申请 ShareID，分享结果会通过 Webhooks 进行回调
    // https://developer.open-douyin.com/docs/resource/zh-CN/dop/develop/openapi/video-management/douyin/search-video/video-share-result
    request.callerLocalEntry = "EntryAbility" // 当前承接的Ability名
    request.redirectUri = DEMO_APP_LINK

    request.titleObject = new OpenTitleObject()
    request.titleObject.title = "text"
    request.titleObject.shortTitle = "shortTitle"
    let hashtagMarker = new HashtagTitleMarker()
    hashtagMarker.hashtag = "这是hashtag1"
    hashtagMarker.start = 1 // 插入到title中的位置
    request.titleObject.addMarkers(hashtagMarker)
    let hashtagMarker2 = new HashtagTitleMarker()
    hashtagMarker2.hashtag = "这是hashtag2"
    hashtagMarker2.start = 2 // 插入到title中的位置
    request.titleObject.addMarkers(hashtagMarker2)

    let bgMusic = new MusicParamObject()
    bgMusic.musicId = "7403619634367892278" //音乐id，在抖音app的任意音乐页面，点击右上角分享按钮，选择复制链接，将链接在pc浏览器打开。打开后，浏览器中链接内的数字即为音乐id。
    request.backgroundMusic = bgMusic
    return request
  }
}