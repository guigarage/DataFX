/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.util;

import java.io.ByteArrayOutputStream;

public class Base64 {

    private final static String base64 =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static byte[] decode(String encoded) {
        int i;
        byte output[] = new byte[3];
        int state;

        ByteArrayOutputStream data = new ByteArrayOutputStream(encoded.length());

        state = 1;
        for (i = 0; i < encoded.length(); i++) {
            byte c;
            {
                char alpha = encoded.charAt(i);
                if (Character.isWhitespace(alpha)) {
                    continue;
                }

                if ((alpha >= 'A') && (alpha <= 'Z')) {
                    c = (byte) (alpha - 'A');
                } else if ((alpha >= 'a') && (alpha <= 'z')) {
                    c = (byte) (26 + (alpha - 'a'));
                } else if ((alpha >= '0') && (alpha <= '9')) {
                    c = (byte) (52 + (alpha - '0'));
                } else if (alpha == '+') {
                    c = 62;
                } else if (alpha == '/') {
                    c = 63;
                } else if (alpha == '=') {
                    break;
                } else {
                    return null;
                }
            }

            switch (state) {
                case 1:
                    output[0] = (byte) (c << 2);
                    break;
                case 2:
                    output[0] |= (byte) (c >>> 4);
                    output[1] = (byte) ((c & 0x0F) << 4);
                    break;
                case 3:
                    output[1] |= (byte) (c >>> 2);
                    output[2] = (byte) ((c & 0x03) << 6);
                    break;
                case 4:
                    output[2] |= c;
                    data.write(output, 0, output.length);
                    break;
            }
            state = (state < 4 ? state + 1 : 1);
        }

        if (i < encoded.length()) {
            switch (state) {
                case 3:
                    data.write(output, 0, 1);
                    return (encoded.charAt(i) == '=') && (encoded.charAt(i + 1) == '=')
                            ? data.toByteArray() : null;
                case 4:
                    data.write(output, 0, 2);
                    return (encoded.charAt(i) == '=') ? data.toByteArray() : null;
                default:
                    return null;
            }
        } else {
            return (state == 1 ? data.toByteArray() : null);
        }

    }

    public static String encode(byte[] data) {

        char output[] = new char[4];
        int state = 1;
        int restbits = 0;
        int chunks = 0;

        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            int ic = (data[i] >= 0 ? data[i] : (data[i] & 0x7F) + 128);
            switch (state) {
                case 1:
                    output[0] = base64.charAt(ic >>> 2);
                    restbits = ic & 0x03;
                    break;
                case 2:
                    output[1] = base64.charAt((restbits << 4) | (ic >>> 4));
                    restbits = ic & 0x0F;
                    break;
                case 3:
                    output[2] = base64.charAt((restbits << 2) | (ic >>> 6));
                    output[3] = base64.charAt(ic & 0x3F);
                    encoded.append(output);

                    chunks++;
                    if ((chunks % 19) == 0) {
                        encoded.append("\r\n");
                    }
                    break;
            }
            state = (state < 3 ? state + 1 : 1);
        }

        switch (state) {
            case 2:
                output[1] = base64.charAt((restbits << 4));
                output[2] = output[3] = '=';
                encoded.append(output);
                break;
            case 3:
                output[2] = base64.charAt((restbits << 2));
                output[3] = '=';
                encoded.append(output);
                break;
        }

        return encoded.toString();
    }
}
