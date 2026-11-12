Pod::Spec.new do |s|
  s.name = "BDTDebugBox"
  s.version = "1.1.1"
  s.summary = "A short description of BDTDebugBox."
  s.license = {"type"=>"MIT", "file"=>"Frameworks/LICENSE"}
  s.authors = {"foo"=>"bar@bytedance.com"}
  s.homepage = "https://developer.open-douyin.com"
  s.description = "A container to add debug items quickly."
  s.source = { :path => '.' }
  s.ios.deployment_target    = '15.0'
  
  s.ios.vendored_framework   = 'Frameworks/BDTDebugBox.xcframework'
  s.resources = "Frameworks/Resources/*.bundle"
  s.pod_target_xcconfig = { 'OTHER_LDFLAGS' => '-ObjC -all_load' }
  s.frameworks = 'UIKit', 'Foundation'
end
