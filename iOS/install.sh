#!/bin/bash

# 定义要下载的 zip 文件 URL 和本地解压路径
DOWNLOAD_URL="https://lf3-static.bytednsdoc.com/obj/eden-cn/vhojnulozbd/BDTDebugBox.framework.zip"
LOCAL_PATH="LocalSpecs/BDTDebugBox/Frameworks"

# 检查目标路径下是否存在已解压的框架文件及其内部的 BDTDebugBox 文件
FRAMEWORK_FILE="$LOCAL_PATH/BDTDebugBox.framework"
FRAMEWORK_BINARY="$FRAMEWORK_FILE/BDTDebugBox"
if [ -f "$FRAMEWORK_BINARY" ]; then
  echo "本地已存在完整的 BDTDebugBox.framework，无需下载。"
  exit 0
fi

# 创建目标路径（如果不存在）
mkdir -p "$LOCAL_PATH"

# 下载 zip 文件
echo "正在从 $DOWNLOAD_URL 下载 BDTDebugBox.zip..."
curl -L -o "${LOCAL_PATH}/BDTDebugBox.zip" "$DOWNLOAD_URL"

# 解压 zip 文件到目标路径
echo "正在解压 BDTDebugBox.zip..."
unzip -qo "${LOCAL_PATH}/BDTDebugBox.zip" -d "$LOCAL_PATH"

# 删除临时 zip 文件
rm "${LOCAL_PATH}/BDTDebugBox.zip"

echo "BDTDebugBox.framework 已成功解压至 $LOCAL_PATH。"