/*
 * Copyright (c) 2001, Mikael Ståldal
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

package nu.staldal.lsp;

public final class TemplateProcessor
{
    private static void throwIllegalTemplate(String template)
        throws TemplateException
    {
        if (template.length() > 64)
            throw new TemplateException();
        else
            throw new TemplateException(template);
    }


	public static String processTemplate(
        char left, char right, char quot1, char quot2,
        String template, ExpressionEvaluator evaluator)
        throws TemplateException
	{
        if (evaluator == null)
            throw new NullPointerException("evaluator is null");

        int len = (template.length() > 128)
            ? template.length()+64
            : template.length()*2;
		StringBuffer sb = new StringBuffer(len);
		StringBuffer expr = null;
		char quote = 0;
		char brace = 0;

		for (int i = 0; i < template.length(); i++)
		{
			char c = template.charAt(i);
			if (expr == null)
			{
				if (c == left)
				{
					if (brace == 0)
					{
						brace = left;
					}
					else if (brace == left)
					{
						sb.append(left);
						brace = 0;
					}
					else if (brace == right)
					{
						throwIllegalTemplate(template);
					}
				}
				else if (c == right)
				{
					if (brace == 0)
					{
						brace = right;
					}
					else if (brace == right)
					{
						sb.append(right);
						brace = 0;
					}
					else if (brace == left)
					{
						throwIllegalTemplate(template);
					}
				}
				else
				{
					if (brace == left)
					{
						expr = new StringBuffer();
						expr.append(c);
						brace = 0;
					}
					else if (brace == right)
					{
						throwIllegalTemplate(template);
					}
					else
					{
						sb.append(c);
					}
				}
			}
			else // expr != null
			{
				if (c == quot1 || c == quot2)
				{
					expr.append(c);
					if (quote == 0)
					{
						quote = c;
					}
					else if (quote == c)
					{
						quote = 0;
					}
				}
				else if (c == right)
				{
					if (quote == 0)
					{
                        String exp = expr.toString();
                        String res;
                        try {
                            res = evaluator.eval(exp);
                        }
                        catch (Exception e)
                        {
                            throw new TemplateException(e);
                        }
                        sb.append(res);
						expr = null;
					}
					else
					{
						expr.append(c);
					}
				}
				else
				{
					expr.append(c);
				}
			}
		}

		if (brace != 0)
		{
		    throwIllegalTemplate(template);
		}

		return sb.toString();
	}
}
