/**
 * Copyright 2016 Wultra s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "PA2RestResponseStatus.h"
#import "PA2Error.h"

/**
 Class representing an error PowerAuth 2.0 Standard API response.
 */
@interface PA2ErrorResponse : NSObject

@property (nonatomic, assign) PA2RestResponseStatus status;
@property (nonatomic, assign) NSUInteger httpStatusCode;
@property (nonatomic, strong) PA2Error* responseObject;

@end
