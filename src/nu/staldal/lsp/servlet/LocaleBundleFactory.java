/*
 * Copyright (c) 2004-2005, Mikael Ståldal
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Note: This is known as "the modified BSD license". It's an approved
 * Open Source and Free Software license, see
 * http://www.opensource.org/licenses/
 * and
 * http://www.gnu.org/philosophy/license-list.html
 */

package nu.staldal.lsp.servlet;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;


/**
 * Interface for localization bundle factories.
 *<p>
 * The resureces for a locale is stored in an {@link java.util.Map}.
 * The key in this map is either <code>[pageName]$[key]</code> for page specific 
 * resources, or just <code>[key]</code> for global resources. If a page specific 
 * resource is not found, a global resource with the same key will be used.
 */
public interface LocaleBundleFactory
{
    
    /**
     * Initialize this factory.
     *
     * @param classLoader    the {@link java.lang.ClassLoader} to use
     * @param servletContext the {@link javax.servlet.ServletContext}
     * 
     * @throws Exception  may throw any Exception 
     */    
    public void init(ClassLoader classLoader, ServletContext servletContext)
        throws Exception;    
    

    /**
     * Load a localization bundle for a given locale.
     *<p>
     * The localization bundles are cached, this method is only
     * invoked once for each locale.
     *<p>
     * This method should <em>not</em> attempt to load a default locale if 
     * the specified locale is not found. The framework will invoke this
     * method again with <code>null</code> as argument when nessecary.
     * 
     * @param locale        the {@link java.util.Locale},
     *                      or <code>null</code> for default
     *
     * @return the {@link java.util.Map} with localization data, 
     * or <code>null</code> if none found for the given locale
     * 
     * @throws Exception  may throw any Exception 
     */    
    public Map<String,String> loadBundle(Locale locale)
        throws Exception;    
}
