/*
 * Copyright 2015 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.edu.nju.nioserver.http.cookie;


import lombok.extern.log4j.Log4j2;

import java.nio.CharBuffer;

import static cn.edu.nju.nioserver.http.cookie.CookieUtil.*;


/**
 * Parent of Client and Server side cookie decoders
 */
@Log4j2
public abstract class CookieDecoder {

    private final boolean strict;

    protected CookieDecoder(boolean strict) {
        this.strict = strict;
    }

    protected DefaultCookie initCookie(String header, int nameBegin, int nameEnd, int valueBegin, int valueEnd) {
        if (nameBegin == -1 || nameBegin == nameEnd) {
            log.debug("Skipping cookie with null name");
            return null;
        }

        if (valueBegin == -1) {
            log.debug("Skipping cookie with null value");
            return null;
        }

        CharSequence wrappedValue = CharBuffer.wrap(header, valueBegin, valueEnd);
        CharSequence unwrappedValue = unwrapValue(wrappedValue);
        if (unwrappedValue == null) {
            log.debug("Skipping cookie because starting quotes are not properly balanced in '{}'",
                    wrappedValue);
            return null;
        }

        final String name = header.substring(nameBegin, nameEnd);

        int invalidOctetPos;
        if (strict && (invalidOctetPos = firstInvalidCookieNameOctet(name)) >= 0) {
            if (log.isDebugEnabled()) {
                log.debug("Skipping cookie because name '{}' contains invalid char '{}'",
                        name, name.charAt(invalidOctetPos));
            }
            return null;
        }

        final boolean wrap = unwrappedValue.length() != valueEnd - valueBegin;

        if (strict && (invalidOctetPos = firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            if (log.isDebugEnabled()) {
                log.debug("Skipping cookie because value '{}' contains invalid char '{}'",
                        unwrappedValue, unwrappedValue.charAt(invalidOctetPos));
            }
            return null;
        }

        DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }
}
