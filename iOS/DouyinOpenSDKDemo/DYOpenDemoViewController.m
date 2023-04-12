// Copyright 2023 ByteDance and/or its affiliates.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#import "DYOpenDemoViewController.h"
#import "DYOpenDemoInfoConfiger.h"

@interface DYOpenDemoViewController ()

@end

@implementation DYOpenDemoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"抖音开放平台";
}

- (IBAction)onClickSelectApp:(UIControl *)sender
{
    BDTDebugVC *debugVC = [[BDTDebugVC alloc] initWithConfiger:[DYOpenDemoInfoConfiger new]];
    [self.navigationController pushViewController:debugVC animated:YES];
}

@end
