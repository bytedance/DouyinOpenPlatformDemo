@Component
export struct SimpleItemComponent {
  @Prop model: DebugItem

  build() {
    Row() {
      if (this.model.iconRes) {
        Image(this.model.iconRes)
          .width(20).height(20).margin({ right: 20 })
      }
      Text(this.model.title).fontWeight(FontWeight.Bold).layoutWeight(1)
      Image($r('app.media.icon_arrow'))
        .size({
          width: 16,
          height: 16
        })
    }.onClick(() => {
      if (!!this.model.onClick) {
        this.model.onClick?.(this.model.url)
        return
      }
    })
    .width("100%")
    .padding({ top: 8, bottom: 8 })
  }
}

@Observed
export class DebugItem {
  title: string
  url?: string
  iconRes?: ResourceStr
  onClick?: (url?: string) => void

  constructor(data: IDebugItem) {
    this.title = data.title
    this.url = data.url
    this.iconRes = data.iconRes
    this.onClick = data.onClick
  }
}


export interface IDebugItem {
  title: string
  url?: string
  /**
   * icon资源，
   */
  iconRes?: ResourceStr
  /**
   * 自己接管点击事件
   * @param url 传入的url
   */
  onClick?: (url?: string) => void
}