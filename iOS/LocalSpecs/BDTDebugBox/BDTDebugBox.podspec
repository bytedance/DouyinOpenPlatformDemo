Pod::Spec.new do |s|
  s.name = "BDTDebugBox"
  s.version = "1.1.0-local"
  s.summary = "A short description of BDTDebugBox."
  s.license = {"type"=>"MIT", "file"=>"LICENSE"}
  s.authors = {"foo"=>"bar@bytedance.com"}
  s.homepage = "https://developer.open-douyin.com"
  s.description = "A container to add debug items quickly."
  s.source = { :path => '.' }
  s.ios.deployment_target    = '9.0'
  
  s.ios.vendored_framework   = 'Frameworks/BDTDebugBox.framework'
  s.resources = "Frameworks/BDTDebugBox.framework/Resources/*.*"
  s.pod_target_xcconfig = { 'OTHER_LDFLAGS' => '-ObjC -all_load', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
  s.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
  s.frameworks = 'UIKit', 'Foundation'
end
