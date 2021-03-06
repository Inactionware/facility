/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.protocol;

import uapi.behavior.ActionIdentify;
import uapi.behavior.annotation.Action;
import uapi.behavior.annotation.ActionDo;
import uapi.common.ArgumentChecker;
import uapi.service.annotation.Service;

@Service
@Action
public class DecodeProtocol {

    public static final ActionIdentify actionId = ActionIdentify.toActionId(DecodeProtocol.class);

    @ActionDo
    public ResourceProcessing decode(
            final ResourceProcessing processing
    ) {
        ArgumentChecker.required(processing, "processing");

        var decoder = processing.decoder();
        if (decoder == null) {
            throw ProtocolException.builder()
                    .errorCode(ProtocolErrors.DECODER_NOT_DEFINED)
                    .variables(processing.originalRequest())
                    .build();
        }

        return decoder.decode(processing);
    }
}
