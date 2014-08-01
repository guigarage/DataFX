/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
* This package contains the entry points for providing data obtained from
* external sources.
* Data can come from a variety of sources, in a variety of formats.
* The retrieval of the data is achieved using one of the classes in 
* the {@link org.datafx.io} package.
* <p>
* DataFX distinguishes between 2 types of external data:
* <ul>
* <li>single item, containing a single instance of data. The data itself can
* be very simple or very complex.
* In case the {@link org.datafx.provider.ObjectDataProvider} is the
* provider you have to use. If you prefer to configure this provider using
* the builder pattern, you can leverage the {@link org.datafx.provider.ObjectDataProviderBuilder}
* class.
* <li>a list of items, containing instances that share the same characteristics
* and that can be casted to a list of instances of the same Java Class.
* If the external data you are retrieving contains a list of items, you should
* use the {@link org.datafx.provider.ListDataProvider}, or the
* corresponding builder class {@link org.datafx.provider.ListDataProviderBuilder}.
* </ul>
*/
package io.datafx.provider;
