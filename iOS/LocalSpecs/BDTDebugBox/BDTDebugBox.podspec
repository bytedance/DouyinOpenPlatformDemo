Pod::Spec.new do |s|
  s.name = "BDTDebugBox"
  s.version = "1.1.1"
  s.summary = "A short description of BDTDebugBox."
  s.license = {"type"=>"MIT", "file"=>"LICENSE"}
  s.authors = {"foo"=>"bar@bytedance.com"}
  s.homepage = "https://developer.open-douyin.com"
  s.description = "A container to add debug items quickly."
  s.source = {
    :http => 'https://sf3-ttcdn-tos.pstatp.com/obj/ies-douyin-opencn/open/BDTDebugBox-1.1.1.zip',
    :sha256 => '78696cfaae342f96d352c2190f9382e30e46c1470495fb66ceec217fa2dc3cc6'
  }
  s.ios.deployment_target    = '15.0'
  
  s.ios.vendored_framework   = 'BDTDebugBox.xcframework'
  s.resources = "Resources/*.bundle"
  s.pod_target_xcconfig = { 'OTHER_LDFLAGS' => '-ObjC -all_load' }
  s.frameworks = 'UIKit', 'Foundation'
end
